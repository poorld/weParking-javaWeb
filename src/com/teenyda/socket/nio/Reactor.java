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
 * <h2>��������</h2>
 * <p>
 * ����������ָ����������ָ��ͻ���
 * </p>
 * <p>
 * ABX1X2X3X4X5
 * </p>
 * <ul>
 * <li>AB ����ͷ</li>
 * <li>X1 ��λ����ַ ���԰�Ϊ 1</li>
 * <li>X2 ���ؿ��� 1 ������������������ ( ������D4������ 0 ���������������� ��������D4����� 2 ��״̬��ѯ</li>
 * <li>X3 Ԥ��Ĭ��0</li>
 * <li>X4 Ԥ��Ĭ��0</li>
 * <li>X5 λ��У�� X1+X2+X3+X4</li>
 * </ul>
 * <p>
 * �磺AB11002 ��ʾ 1# ���� D4��
 * </p>
 * <p>
 * AB10001 ��ʾ 1# �� D4��
 * </p>
 * <p>
 * AB12003 ��ʾ ��ѯ1# �� ״̬
 * </p>
 * 
 * <h2>�������ݣ�</h2>
 * <p>
 * ABX1X2X3X4X5
 * </p>
 * <ul>
 * <li>AB ����ͷ</li>
 * <li>X1 ��λ����ַ ���԰�Ϊ 1</li>
 * <li>X2 ���ؿ��� 1 ���� ( �����յ���������֮�󣬿��� D4����������ȥ ������ɣ�</li>
 * <li>0 �� ( �����յ� ������֮�� �� D4�𣬷�����ȥ ����ɣ�</li>
 * <li>2 ��״̬ ( �����յ���ѯ����֮�󣬷�����ȥ ��ǰΪ��״̬ ��</li>
 * <li>3 ����״̬( �����յ���ѯ����֮�󣬷�����ȥ ��ǰΪ����״̬</li>
 * <li>X3 Ԥ��Ĭ��0</li>
 * <li>X4 Ԥ��Ĭ��0</li>
 * <li>X5 λ��У�� X1+X2+X3+X4</li>
 * </ul>
 * 
 * <p>
 * �磺AB11002 ��ʾ 1# ������� D4��
 * </p>
 * <p>
 * AB10001 ��ʾ 1# �� ��� D4��
 * </p>
 * <p>
 * AB12003 ��ʾ �յ���ѯ����֮���� 1# �� Ϊ�� ״̬
 * </p>
 * <p>
 * AB13004 ��ʾ �յ���ѯ����֮���� 1# �� Ϊ���� ״̬
 * </p>
 */
public class Reactor implements Runnable {
	/**
	 * �������� ����
	 */
	private static final String down_unlocking = "AB11002";
	/**
	 * �������� ��
	 */
	private static final String down_lockup = "AB10001";
	/**
	 * �������� ��ѯ
	 */
	private static final String down_query = "AB12003";

	/**
	 * �������� �������
	 */
	private static final String up_unlock_completion = "AB11002";
	/**
	 * �������� �����
	 */
	private static final String up_lock_completion = "AB10001";
	/**
	 * �������� �յ���ѯ����֮���� �� Ϊ�� ״̬
	 */
	private static final String up_locked = "AB12003";
	/**
	 * �������� �յ���ѯ����֮���� 1# �� Ϊ���� ״̬
	 */
	private static final String up_unlocked = "AB13004";
	/**
	 * �������� ������
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
			// �����ܵ�
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			// �󶨵�ַ
			InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 9527);
			serverChannel.bind(isa);
			// ����ѡ����
			Selector selector = Selector.open();
			// ���÷�����
			serverChannel.configureBlocking(false);
			// ע�ᵽselector
			serverChannel.register(selector, SelectionKey.OP_ACCEPT).attach(id++);
//			System.out.println("��ʼ���� 9527 �˿�....");
			log.info("��������9527�˿�");
			listener(selector);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void listener(Selector selector) {
		// TODO Auto-generated method stub
		String markId = "";
		int lookId = -1;
		//�Ƿ���ִ������
		boolean execute = false;
		
		//ִ�к������
		boolean executeResult = false;
		
		//ִ�е�����
		String executeCommand = "";
		
		//ʱ���� (��)
		int time = 5;//s
		
		//ִ���������
		int executeCount = 5;
		
		while (true) {
			try {
				Thread.sleep(time * 1000);// InterruptedException
				// ���� ֱ���о����¼�Ϊֹ
				int count = selector.select();
				//System.out.println("��������" + count);
				//log.debug("��������" + count);
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				
				Iterator<SelectionKey> it = selectedKeys.iterator();
				
				while (it.hasNext()) {
					
					SelectionKey selectionKey = (SelectionKey) it.next();
					
					if (!selectionKey.isValid())
						continue;
					
					// ������
					if (selectionKey.isAcceptable()) {
						
						//System.out.println("----------������----------");
						ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
						serverChannel.accept().configureBlocking(false)
								.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE)
								.attach("markId=" + id++);
//						System.out.println(selectionKey.attachment() + "������");
						log.debug(selectionKey.attachment() + "������");
					}
					
					// �ɶ� ��ȡ���ݷ���һ
					if (selectionKey.isReadable()) {

//						System.out.println("----------�ɶ�----------");
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(byfferSize);
						int len = 0;
						markId = (String) selectionKey.attachment();
						
						try {
							len = socketChannel.read(buff);
//							System.out.println("len="+len);
							if (len == -1) {
								log.error(markId+"�����˶Ͽ�");
								selectionKey.cancel();
								socketChannel.socket().close();
								socketChannel.close();
								System.out.println(jedis.del(markId));
								if (lookId != -1){
									stateService.offlineByLookId(lookId);
								}
//								System.out.println(jedis.keys("markId=*"));
								log.debug("ʣ������:"+jedis.keys("markId=*"));
//								System.out.println("�������������ر�����");
								continue;
							}
						} catch (IOException e) {
//							e.printStackTrace();
							log.error(markId+"�����˶Ͽ�");
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
							log.debug("ʣ������:"+jedis.keys("markId=*"));
//							System.out.println("�������������ر�����");
							continue;
						}
						//��ȡ����
						String data = getString(buff);
						// System.out.println(markId + ",��ȡ����:" + data);
						log.info(markId+" -> ��ȡ����:"+data);
						//�״����ӣ��鿴�Ƿ���ڱ�ǣ����������ѯ��ȡ
						//��ѯ����markId=100001Ϊ����lookNumber=1Ϊֵ����
						if (!jedis.exists(markId)) {
							log.debug("���Ի�ȡ��id");
							// У������
							if (data.length() == 7 && data.substring(0, 2).equals("AB")) {
								// ��ȡ�����
								lookId = Integer.parseInt(data.substring(2, 3));
								String state = data.substring(3, 4);
								// System.out.println("��ѯ�ɹ�����ȡ�����Ϊ:" + lookNumber);
								//AB12003
								log.debug("��ȡ�����Ϊ:"+lookId);
								//stateService.onlineByLookId(lookId, Integer.parseInt(state));
								jedis.set(markId, "lookNumber=" + lookId);
								// System.out.println(markId + " -- " + lookNumber);
								log.debug(markId+ "<---->" + lookId);
							}
						} else if (execute) {
							log.debug("���Ի�ȡ��������");
							// ��������ִ�У���ȡ��������
							if (data.length() == 7 && data.substring(0, 2).equals("AB")) {
								if (executeCommand.equals(data)) {
									execute = false;
									executeCommand = "";
									executeResult = true;
									time = 5;
									executeCount = 5;
									// �������ݳɹ���ɾ������
									//if (executeResult) {
										System.out.println(jedis.del(jedis.get(markId)) > 0 ? "ִ������ɹ�" : "ִ������ʧ��");
									//}
								}
								switch (data) {
									case up_unlock_completion: {
										//System.out.println("--------�������--------");
										log.debug("�������");
										break;
									}
									case up_lock_completion: {
										//System.out.println("--------�����--------");
										log.debug("�����");
										break;
									}
									case up_locked: {
										//System.out.println("--------��������:��״̬--------");
										log.debug("��������:��״̬");
										break;
									}
									case up_unlocked: {
										//System.out.println("--------��������:��״̬--------");
										log.debug("��������:��״̬");
										break;
									}
								}
							}else {
								//System.out.println("�����쳣");
								log.debug("�����쳣");
							}
						}
					}
					// �ɶ� ��ȡ���ݷ�����
					/*
					 * if (selectionKey.isReadable()) {
					 * System.out.println("----------�ɶ�----------"); 
					 * SocketChannel soChannel =(SocketChannel) selectionKey.channel(); 
					 * ByteBuffer buff =ByteBuffer.allocate(size); 
					 * int len = 0; 
					 * try {
					 * System.out.println(buff);//[pos=0 lim=7 cap=7] 
					 * len = soChannel.read(buff);
					 * //buff.flip(); 
					 * System.out.println(buff);//[pos=0 lim=7 cap=7] û�� 
					 * if (len ==-1) {
					 *  //˵���ͻ����ѹر�
					 *  //�ر��� .... �� 
					 *  }else { 
					 *  String str = new String(buff.array(),0, len); 
					 *  System.out.println("��ȡ���ݣ�"+str); 
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

					// ��д
					if (selectionKey.isWritable()) {
						//System.out.println("----------��д----------");
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(48);
						markId = (String) selectionKey.attachment();
						//System.out.println(markId);
						if (!jedis.exists(markId)) {
							//System.out.println("��һ������,���Ͳ�ѯָ��");
							log.debug(markId+"���Ի�ȡ����Ϣ");
							String infoCommand = "AB12003";
							buff.put(infoCommand.getBytes());
							buff.clear();// д����ʱ����
							socketChannel.write(buff);
						} else if (hasCommand(markId) && executeCount>0) {
							// ������
							//System.out.println(markId+",executeCount="+executeCount);
							log.debug(markId+"����������,ʣ��ִ�д���:"+executeCount);
							time = 1;
							execute = true;
							String command = getCommand(markId);
							executeCommand = command;
							executeCount--;
							//System.out.println("ִ������:" + command);
							log.debug("ִ������ ----> " + command);
							if (command.equals(down_lockup)) {
								// System.out.println("-------ִ�� |��|����--------");
								log.debug("\n------------------------");
								log.debug("|ִ��\t��\t����");
								log.debug("------------------------");
							} else if (command.equals(down_unlocking)) {
								//System.out.println("-------ִ�� |����|����--------");
								log.debug("\n------------------------");
								log.debug("|ִ��\t����\t����");
								log.debug("------------------------");
							} else if (command.equals(down_query)) {
								//System.out.println("-------ִ�� |��ѯ|����--------");
								log.debug("\n------------------------");
								log.debug("|ִ��\t��ѯ\t����");
								log.debug("------------------------");
							}
							buff.put(command.getBytes());
							buff.clear();// д����ʱ����
							socketChannel.write(buff);
							// �������ݳɹ���ɾ������
							/*if (executeResult) {
								System.out.println(jedis.del(jedis.get(markId)) > 0 ? "ִ������ɹ�" : "ִ������ʧ��");
							}*/
						}else {
							//����������
							buff.put(heartbeat_packet.getBytes());
							buff.clear();// д����ʱ����
							socketChannel.write(buff);
						}

					}
					
					if (!executeResult && executeCount == 0) {
						//System.out.println("ִ��ʧ��");
						log.error("ִ��ʧ��,���"+executeCommand);
						//�ָ�ʱ��
						time = 5;
						//ִ�д���
						executeCount = 5;
						//ɾ������
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
		//System.out.println("��ȡ���markId=" + markId);
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
