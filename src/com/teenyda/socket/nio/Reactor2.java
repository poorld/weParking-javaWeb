package com.teenyda.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.teenyda.service.admin.StateServiceImpl;

import redis.clients.jedis.Jedis;

/**
 * Reactor2只针对1号锁
 * <h2>下行数据</h2>
 * <p>
 * 下行数据是指服务器发送指令到客户端
 * </p>
 * <p>
 * ABX1X2X3X4X5
 * </p>
 * <ul>
 * <li>AB 数据头</li>
 * <li>X1 车位锁地址 测试板为 1</li>
 * <li>X2 开关控制 1 开锁（把锁合起来） ( 板子上D4亮代表） 0 锁（把锁立起来） （板子上D4灭代表） 2 锁状态查询</li>
 * <li>X3 预留默认0</li>
 * <li>X4 预留默认0</li>
 * <li>X5 位和校验 X1+X2+X3+X4</li>
 * </ul>
 * <p>
 * 如：AB11002 表示 1# 开锁 D4亮
 * </p>
 * <p>
 * AB10001 表示 1# 锁 D4灭
 * </p>
 * <p>
 * AB12003 表示 查询1# 锁 状态
 * </p>
 * 
 * <h2>上行数据：</h2>
 * <p>
 * ABX1X2X3X4X5
 * </p>
 * <ul>
 * <li>AB 数据头</li>
 * <li>X1 车位锁地址 测试板为 1</li>
 * <li>X2 开关控制 1 开锁 ( 板子收到开锁命令之后，开锁 D4亮，反馈回去 开锁完成）</li>
 * <li>0 锁 ( 板子收到 锁命令之后， 锁 D4灭，反馈回去 锁完成）</li>
 * <li>2 锁状态 ( 板子收到查询命令之后，反馈回去 当前为锁状态 ）</li>
 * <li>3 开锁状态( 板子收到查询命令之后，反馈回去 当前为开锁状态</li>
 * <li>X3 预留默认0</li>
 * <li>X4 预留默认0</li>
 * <li>X5 位和校验 X1+X2+X3+X4</li>
 * </ul>
 * 
 * <p>
 * 如：AB11002 表示 1# 开锁完成 D4亮
 * </p>
 * <p>
 * AB10001 表示 1# 锁 完成 D4灭
 * </p>
 * <p>
 * AB12003 表示 收到查询命令之后反馈 1# 锁 为锁 状态
 * </p>
 * <p>
 * AB13004 表示 收到查询命令之后反馈 1# 锁 为开锁 状态
 * </p>
 */
public class Reactor2 implements Runnable {
	/**
	 * 下行数据 开锁
	 */
	private static final String down_unlocking = "AB11002";
	/**
	 * 下行数据 锁
	 */
	private static final String down_lockup = "AB10001";
	/**
	 * 下行数据 查询
	 */
	private static final String down_query = "AB12003";

	/**
	 * 上行数据 开锁完成
	 */
	private static final String up_unlock_completion = "AB11002";
	/**
	 * 上行数据 锁完成
	 */
	private static final String up_lock_completion = "AB10001";
	/**
	 * 上行数据 收到查询命令之后反馈 锁 为锁 状态
	 */
	private static final String up_locked = "AB12003";
	/**
	 * 上行数据 收到查询命令之后反馈 1# 锁 为开锁 状态
	 */
	private static final String up_unlocked = "AB13004";
	/**
	 * 上行数据 心跳包
	 */
	private static final String heartbeat_packet = "111111";

	private static int id = 10000;

	private static int byfferSize = 7;

	Jedis jedis = new Jedis();

	private static Logger log = Logger.getLogger(Reactor2.class);

	private static StateServiceImpl stateService = new StateServiceImpl();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		try {
			// 创建管道
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			// 绑定地址 如果绑定了127.0.0.1，只能在本地测试
			//InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 9527);
			serverChannel.bind(new InetSocketAddress(9527));
			//serverChannel.bind(isa);
			// 创建选择器
			Selector selector = Selector.open();
			// 配置非阻塞
			serverChannel.configureBlocking(false);
			// 注册到selector
			serverChannel.register(selector, SelectionKey.OP_ACCEPT).attach(id++);
			// System.out.println("开始监听 9527 端口....");
			log.info("开启监听9527端口");
			listener(selector);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void listener(Selector selector) {
		// TODO Auto-generated method stub
		int lookId = -1;
		// 是否有执行命令
		boolean execute = false;

		// 执行的命令
		String executeCommand = "";
		
		boolean firstConnection = true;
		
		boolean gg = false;

		// 时间间隔 (秒)
		int time = 5;// s

		while (true) {
			try {
				// 阻塞 直到有就绪事件为止
				int count = selector.select();
				// System.out.println("连接数量" + count);
				// log.debug("连接数量" + count);

				Set<SelectionKey> selectedKeys = selector.selectedKeys();

				Iterator<SelectionKey> it = selectedKeys.iterator();

				while (it.hasNext()) {

					SelectionKey selectionKey = (SelectionKey) it.next();

					if (!selectionKey.isValid())
						continue;

					// 可连接
					if (selectionKey.isAcceptable()) {
						log.debug("************** 可连接 ***************");
						log.debug("连接数量:"+count);
						System.out.println("----------可连接----------");
						ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
						serverChannel.accept().configureBlocking(false).register(selector,
								SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						lookId = 1;
					}

					// 可读 读取数据方案一
					if (selectionKey.isReadable()) {
						//log.debug("************* 可读 ****************\n");
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(byfferSize);
						int len = 0;
						
						try {
							len = socketChannel.read(buff);
							//异常关闭
							if (len == -1) {
								debugDisconnect();
								stateService.offlineByLookId(1);
								
								selectionKey.cancel();
								socketChannel.socket().close();
								socketChannel.close();
								
								clearCommand();
								
								firstConnection = true;
								gg = false;
								execute = false;
								executeCommand = "";
								
								continue;
							}
						} catch (IOException e) {
							//异常关闭
							// e.printStackTrace();
							debugDisconnect();
							stateService.offlineByLookId(1);
							
							selectionKey.cancel();
							socketChannel.socket().close();
							socketChannel.close();
							
							
							clearCommand();
							
							firstConnection = true;
							gg = false;
							execute = false;
							executeCommand = "";
							
							System.out.println(e.getMessage());
							log.debug(e.getMessage());
							continue;
						}
						// 获取数据
						String data = getString(buff);
						//解析锁的状态 //data.equals(up_locked) || data.equals(up_unlocked)
						if (firstConnection && dataCheck(data)){
							firstConnection = false;
							int lookState = Integer.parseInt(data.substring(3, 4));
							//锁上线
							stateService.onlineByLookId(1, data);
							log.debug("##########################");
							log.debug("通信成功！锁状态为:"+(lookState == 2 ? "锁":"开锁"));
							log.debug("##########################");
						}
						/**
						 * 有命令执行，获取反馈数据
						 */
						else if (execute) {
							log.debug("尝试获取反馈数据");
							if (dataCheck(data)) {
								String feedback = feedback(data);
								log.debug("----------------------------------");
								log.debug("执行的命令:"+executeCommand);
								log.debug("反馈命令:"+data);
								log.debug(feedback);
								log.debug("");
								
								gg = false;//恢复
								execute = false;
								executeCommand = "";
								System.out.println(jedis.set("lookNumber=1","true"));
								stateService.updateLookState(1, data);
								
							} else {
								log.debug("----------------------------------");
								log.debug("反馈异常,反馈数据:"+data);
								log.debug("执行的命令:"+executeCommand);
								log.debug("");
								if (gg){//第二次反馈异常 判定为执行失败
									
									execute = false;
									executeCommand = "";
									jedis.set("lookNumber=1", "false");
									log.debug("执行失败");
									log.debug("");
								}else {
									//尝试再次获取
									/**
									 * 获取两次是因为
									 * 第一次获取的数据为111111，第二次才是反馈数据AB.....
									 */
									gg = true;
								}
							}
						/**
						 * 没有命令执行，获取正常数据
						 */
						} else {
							System.out.println(data);
						}
					}

					// 可写
					if (selectionKey.isWritable()) {
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(byfferSize);
						//首次连接
						if (firstConnection){
							//firstConnection = false;
							//查询锁信息
							buff.put(down_query.getBytes());
							buff.clear();
							socketChannel.write(buff);
						} else if (hasCommand() && executeCommand.equals("")) {
							execute = true;
							String command = getCommand();
							executeCommand = command;
							if (command.equals(down_lockup)) {
								log.debug("------------------------");
								log.debug("执行\t锁\t命令");
								log.debug("");
							} else if (command.equals(down_unlocking)) {
								log.debug("------------------------");
								log.debug("执行\t开锁\t命令");
								log.debug("");
							} else if (command.equals(down_query)) {
								log.debug("------------------------");
								log.debug("执行\t查询\t命令");
								log.debug("");
							}
							buff.put(command.getBytes());
							buff.clear();// 写数据时调用
							socketChannel.write(buff);
							
						}
					}
					
					if (firstConnection){
						Thread.sleep(3000);
					}
					it.remove();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getString(ByteBuffer buff) {
		// TODO Auto-generated method stub
		String str = "";
		try {
			for (int i = 0; i < buff.position(); i++) {
				str += (char) buff.get(i);
			}
			return str;
		} catch (Exception e) {
			return "";
		}
	}

	private boolean hasCommand() {
		if(jedis.exists("lookNumber=1")){
			String value = jedis.get("lookNumber=1");
			if (value == null){
				return false;
			}else if(value.equals("false") || value.equals("true")){
				return false;
			}else {
				return true;
			}
		}
		return false;
	}

	/**
	 * key value markId=100001 lookNumber=1 lookNumber=1 AB12003
	 */
	private String getCommand() {
		return jedis.get("lookNumber=1");
	}
	
	private boolean dataCheck(String data){
		return data.length() == 7 && data.substring(0, 2).equals("AB");
	}
	
	private String feedback(String data){
		switch (data) {
			case up_unlock_completion: {
				return "开锁完成";
			}
			case up_lock_completion: {
				return "锁完成";
			}
			case up_locked: {
				return "反馈数据:锁状态";
			}
			case up_unlocked: {
				return "反馈数据:开状态";
			}
			case heartbeat_packet: {
				return "心跳包 111111";
			}
		}
		return null;
	}
	
	/**
	 * 连接断开日志
	 */
	private void debugDisconnect(){
		log.debug("<------------------------>");
		log.debug("连接断开");
		log.debug("<------------------------>");
	}
	
	private void clearCommand(){
		String key = "lookNumber=1";
		if (jedis.exists(key))
		jedis.del(key);
	}
}
