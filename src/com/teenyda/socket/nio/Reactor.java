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
public class Reactor implements Runnable {
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
	
	private static Logger log = Logger.getLogger(Reactor.class);
	
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
			// 绑定地址
			InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 9527);
			serverChannel.bind(isa);
			// 创建选择器
			Selector selector = Selector.open();
			// 配置非阻塞
			serverChannel.configureBlocking(false);
			// 注册到selector
			serverChannel.register(selector, SelectionKey.OP_ACCEPT).attach(id++);
//			System.out.println("开始监听 9527 端口....");
			log.info("开启监听9527端口");
			listener(selector);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void listener(Selector selector) {
		// TODO Auto-generated method stub
		String markId = "";
		int lookId = -1;
		//是否有执行命令
		boolean execute = false;
		
		//执行后反馈结果
		boolean executeResult = false;
		
		//执行的命令
		String executeCommand = "";
		
		//时间间隔 (秒)
		int time = 5;//s
		
		//执行命令次数
		int executeCount = 5;
		
		while (true) {
			try {
				Thread.sleep(time * 1000);// InterruptedException
				// 阻塞 直到有就绪事件为止
				int count = selector.select();
				//System.out.println("连接数量" + count);
				//log.debug("连接数量" + count);
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				
				Iterator<SelectionKey> it = selectedKeys.iterator();
				
				while (it.hasNext()) {
					
					SelectionKey selectionKey = (SelectionKey) it.next();
					
					if (!selectionKey.isValid())
						continue;
					
					// 可连接
					if (selectionKey.isAcceptable()) {
						
						//System.out.println("----------可连接----------");
						ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
						serverChannel.accept().configureBlocking(false)
								.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE)
								.attach("markId=" + id++);
//						System.out.println(selectionKey.attachment() + "已连接");
						log.debug(selectionKey.attachment() + "已连接");
					}
					
					// 可读 读取数据方案一
					if (selectionKey.isReadable()) {

//						System.out.println("----------可读----------");
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(byfferSize);
						int len = 0;
						markId = (String) selectionKey.attachment();
						
						try {
							len = socketChannel.read(buff);
//							System.out.println("len="+len);
							if (len == -1) {
								log.error(markId+"与服务端断开");
								selectionKey.cancel();
								socketChannel.socket().close();
								socketChannel.close();
								System.out.println(jedis.del(markId));
								if (lookId != -1){
									stateService.offlineByLookId(lookId);
								}
//								System.out.println(jedis.keys("markId=*"));
								log.debug("剩余连接:"+jedis.keys("markId=*"));
//								System.out.println("服务器已主动关闭连接");
								continue;
							}
						} catch (IOException e) {
//							e.printStackTrace();
							log.error(markId+"与服务端断开");
							selectionKey.cancel();
							socketChannel.socket().close();
							socketChannel.close();
							jedis.del(markId);
							System.out.println(jedis.keys("markId=*"));
							System.out.println(e.getMessage());
							if (lookId != -1){
								stateService.offlineByLookId(lookId);
							}
							log.debug(e.getMessage());
							log.debug("剩余连接:"+jedis.keys("markId=*"));
//							System.out.println("服务器已主动关闭连接");
							continue;
						}
						//获取数据
						String data = getString(buff);
						// System.out.println(markId + ",读取数据:" + data);
						log.info(markId+" -> 读取数据:"+data);
						//首次连接，查看是否存在标记，不存在则查询获取
						//查询后，以markId=100001为键，lookNumber=1为值缓存
						if (!jedis.exists(markId)) {
							log.debug("尝试获取锁id");
							// 校验数据
							if (data.length() == 7 && data.substring(0, 2).equals("AB")) {
								// 获取锁编号
								lookId = Integer.parseInt(data.substring(2, 3));
								String state = data.substring(3, 4);
								// System.out.println("查询成功，获取锁编号为:" + lookNumber);
								//AB12003
								log.debug("获取锁编号为:"+lookId);
								//stateService.onlineByLookId(lookId, Integer.parseInt(state));
								jedis.set(markId, "lookNumber=" + lookId);
								// System.out.println(markId + " -- " + lookNumber);
								log.debug(markId+ "<---->" + lookId);
							}
						} else if (execute) {
							log.debug("尝试获取反馈数据");
							// 有新命令执行，获取反馈数据
							if (data.length() == 7 && data.substring(0, 2).equals("AB")) {
								if (executeCommand.equals(data)) {
									execute = false;
									executeCommand = "";
									executeResult = true;
									time = 5;
									executeCount = 5;
									// 反馈数据成功，删除命令
									//if (executeResult) {
										System.out.println(jedis.del(jedis.get(markId)) > 0 ? "执行命令成功" : "执行命令失败");
									//}
								}
								switch (data) {
									case up_unlock_completion: {
										//System.out.println("--------开锁完成--------");
										log.debug("开锁完成");
										break;
									}
									case up_lock_completion: {
										//System.out.println("--------锁完成--------");
										log.debug("锁完成");
										break;
									}
									case up_locked: {
										//System.out.println("--------反馈数据:锁状态--------");
										log.debug("反馈数据:锁状态");
										break;
									}
									case up_unlocked: {
										//System.out.println("--------反馈数据:开状态--------");
										log.debug("反馈数据:开状态");
										break;
									}
								}
							}else {
								//System.out.println("反馈异常");
								log.debug("反馈异常");
							}
						}
					}
					// 可读 读取数据方案二
					/*
					 * if (selectionKey.isReadable()) {
					 * System.out.println("----------可读----------"); 
					 * SocketChannel soChannel =(SocketChannel) selectionKey.channel(); 
					 * ByteBuffer buff =ByteBuffer.allocate(size); 
					 * int len = 0; 
					 * try {
					 * System.out.println(buff);//[pos=0 lim=7 cap=7] 
					 * len = soChannel.read(buff);
					 * //buff.flip(); 
					 * System.out.println(buff);//[pos=0 lim=7 cap=7] 没变 
					 * if (len ==-1) {
					 *  //说明客户端已关闭
					 *  //关闭流 .... 略 
					 *  }else { 
					 *  String str = new String(buff.array(),0, len); 
					 *  System.out.println("读取数据："+str); 
					 *  } 
					 *  } catch (IOException e) {
					 * e.printStackTrace(); 
					 * soChannel.socket().close(); 
					 * soChannel.close();
					 * selectionKey.cancel(); 
					 * continue; 
					 * } 
					 * }
					 */

					// 可写
					if (selectionKey.isWritable()) {
						//System.out.println("----------可写----------");
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(48);
						markId = (String) selectionKey.attachment();
						//System.out.println(markId);
						if (!jedis.exists(markId)) {
							//System.out.println("第一次连接,发送查询指令");
							log.debug(markId+"尝试获取锁信息");
							String infoCommand = "AB12003";
							buff.put(infoCommand.getBytes());
							buff.clear();// 写数据时调用
							socketChannel.write(buff);
						} else if (hasCommand(markId) && executeCount>0) {
							// 有命令
							//System.out.println(markId+",executeCount="+executeCount);
							log.debug(markId+"监听到命令,剩余执行次数:"+executeCount);
							time = 1;
							execute = true;
							String command = getCommand(markId);
							executeCommand = command;
							executeCount--;
							//System.out.println("执行命令:" + command);
							log.debug("执行命令 ----> " + command);
							if (command.equals(down_lockup)) {
								// System.out.println("-------执行 |锁|命令--------");
								log.debug("\n------------------------");
								log.debug("|执行\t锁\t命令");
								log.debug("------------------------");
							} else if (command.equals(down_unlocking)) {
								//System.out.println("-------执行 |开锁|命令--------");
								log.debug("\n------------------------");
								log.debug("|执行\t开锁\t命令");
								log.debug("------------------------");
							} else if (command.equals(down_query)) {
								//System.out.println("-------执行 |查询|命令--------");
								log.debug("\n------------------------");
								log.debug("|执行\t查询\t命令");
								log.debug("------------------------");
							}
							buff.put(command.getBytes());
							buff.clear();// 写数据时调用
							socketChannel.write(buff);
							// 反馈数据成功，删除命令
							/*if (executeResult) {
								System.out.println(jedis.del(jedis.get(markId)) > 0 ? "执行命令成功" : "执行命令失败");
							}*/
						}else {
							//发送心跳包
							buff.put(heartbeat_packet.getBytes());
							buff.clear();// 写数据时调用
							socketChannel.write(buff);
						}

					}
					
					if (!executeResult && executeCount == 0) {
						//System.out.println("执行失败");
						log.error("执行失败,命令："+executeCommand);
						//恢复时间
						time = 5;
						//执行次数
						executeCount = 5;
						//删除命令
						jedis.del(jedis.get(markId));
					}
					it.remove();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
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

	private boolean hasCommand(String markId) {
		return jedis.exists(jedis.get(markId));
	}

	/** 
	 * key 				value
	 * markId=100001  lookNumber=1
	 * lookNumber=1  	AB12003
	 * */	
	private String getCommand(String markId) {
		//System.out.println("获取命令，markId=" + markId);
		log.debug("getCommand(),"+markId);
		Set<String> keys = jedis.keys("lookNumber=*");
		log.debug("keys -> "+keys);
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			// key = "lookNumber=1"
			String key = iter.next();
			// value = "AB12003"
			String command = jedis.get(key);
			if (jedis.get(markId).equals(key)) {
				return command;
			}
		}
		return "";
	}

}
