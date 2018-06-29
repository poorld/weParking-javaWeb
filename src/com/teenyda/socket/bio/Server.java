package com.teenyda.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import redis.clients.jedis.Jedis;

/**
 * @author 作者 E-mail:
 * @date 创建时间：2018年6月2日 下午9:01:37
 * @version 1.0
 * @parameter
 * @since
 * @return
 * 
 */
public class Server implements Runnable {
	private Jedis jedis = new Jedis();
	private static final String defaultData = "111111";
	private static final String look_query_close_state = "AB12003";//锁住
	private static final String look_query_open_state = "AB13004";//没锁
	private static final String look_query_state = "AB12003";//查询命令
	private static final String look_execute_close_state = "AB10001";//锁命令
	private static final String look_execute_open_state = "AB11002";//开锁命令
	private static String currentCommand = "";
	private static boolean execute = true;
	private static final int currentLook = 1;
	private static int lookState = -1;
	private static String command = "";
	private Socket socket;

	public Server(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		System.out.println("创建新连接");
		String address = socket.getInetAddress().getHostAddress();
		System.out.println("ip:" + address + "已连接");
		try {
			socket.setKeepAlive(true);
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			lintener(currentLook, os);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = is.read(buff)) != -1) {
				String data = new String(buff, 0, len);
				execute = true;
				if (data.equals(defaultData)) {
					System.out.println(data);
					jedis.set("state", "default");
				}else {
					System.out.println("反馈数据:"+data);
					jedis.set("state", data);
					switch (data) {
						case look_execute_open_state:
							System.out.println("开锁成功");
							break;
						case look_execute_close_state: 
							System.out.println("以锁住");
							break;
						case look_query_open_state: 
							System.out.println("查询锁状态:开");
							break;
						case look_query_close_state:
							System.out.println("查询锁状态:关");
							break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void lintener(int lookid,OutputStream os) {

		new Thread() {
			@Override
			public void run() {
				while (true) {
					Boolean exists = jedis.exists("lookid=" + lookid);
					if (exists && execute) {
						String look = jedis.get("lookid=" + lookid);
						System.out.println("监听到命令:");
						System.out.println("当前锁编号：" + lookid + ",命令：" + look);
						lookState = Integer.parseInt(look);// 0-1(开)
						//0立起来
						if (lookState == 0) {
							try {
								executeCommand(os, look_execute_close_state);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						//1放下来
						if (lookState == 1) {
							try {
								executeCommand(os,look_execute_open_state);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						//2查询
						if (lookState == 2) {
							try {
								executeCommand(os, look_query_state);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Long del = jedis.del("lookid=1");
						if(del>0){
							System.out.println("删除键成功");
						}
						execute = false;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void executeCommand(OutputStream os,String command) throws IOException{
		System.out.println("执行命令:"+command);
		currentCommand = command;
		os.write(command.getBytes());
		os.flush();
	}

}
