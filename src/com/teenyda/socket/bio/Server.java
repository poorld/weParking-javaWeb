package com.teenyda.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import redis.clients.jedis.Jedis;

/**
 * @author ���� E-mail:
 * @date ����ʱ�䣺2018��6��2�� ����9:01:37
 * @version 1.0
 * @parameter
 * @since
 * @return
 * 
 */
public class Server implements Runnable {
	private Jedis jedis = new Jedis();
	private static final String defaultData = "111111";
	private static final String look_query_close_state = "AB12003";//��ס
	private static final String look_query_open_state = "AB13004";//û��
	private static final String look_query_state = "AB12003";//��ѯ����
	private static final String look_execute_close_state = "AB10001";//������
	private static final String look_execute_open_state = "AB11002";//��������
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
		System.out.println("����������");
		String address = socket.getInetAddress().getHostAddress();
		System.out.println("ip:" + address + "������");
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
					System.out.println("��������:"+data);
					jedis.set("state", data);
					switch (data) {
						case look_execute_open_state:
							System.out.println("�����ɹ�");
							break;
						case look_execute_close_state: 
							System.out.println("����ס");
							break;
						case look_query_open_state: 
							System.out.println("��ѯ��״̬:��");
							break;
						case look_query_close_state:
							System.out.println("��ѯ��״̬:��");
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
						System.out.println("����������:");
						System.out.println("��ǰ����ţ�" + lookid + ",���" + look);
						lookState = Integer.parseInt(look);// 0-1(��)
						//0������
						if (lookState == 0) {
							try {
								executeCommand(os, look_execute_close_state);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						//1������
						if (lookState == 1) {
							try {
								executeCommand(os,look_execute_open_state);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						//2��ѯ
						if (lookState == 2) {
							try {
								executeCommand(os, look_query_state);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Long del = jedis.del("lookid=1");
						if(del>0){
							System.out.println("ɾ�����ɹ�");
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
		System.out.println("ִ������:"+command);
		currentCommand = command;
		os.write(command.getBytes());
		os.flush();
	}

}
