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
 * Reactor2ֻ���1����
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
public class Reactor2 implements Runnable {
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
			// �����ܵ�
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			// �󶨵�ַ �������127.0.0.1��ֻ���ڱ��ز���
			//InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 9527);
			serverChannel.bind(new InetSocketAddress(9527));
			//serverChannel.bind(isa);
			// ����ѡ����
			Selector selector = Selector.open();
			// ���÷�����
			serverChannel.configureBlocking(false);
			// ע�ᵽselector
			serverChannel.register(selector, SelectionKey.OP_ACCEPT).attach(id++);
			// System.out.println("��ʼ���� 9527 �˿�....");
			log.info("��������9527�˿�");
			listener(selector);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void listener(Selector selector) {
		// TODO Auto-generated method stub
		int lookId = -1;
		// �Ƿ���ִ������
		boolean execute = false;

		// ִ�е�����
		String executeCommand = "";
		
		boolean firstConnection = true;
		
		boolean gg = false;

		// ʱ���� (��)
		int time = 5;// s

		while (true) {
			try {
				// ���� ֱ���о����¼�Ϊֹ
				int count = selector.select();
				// System.out.println("��������" + count);
				// log.debug("��������" + count);

				Set<SelectionKey> selectedKeys = selector.selectedKeys();

				Iterator<SelectionKey> it = selectedKeys.iterator();

				while (it.hasNext()) {

					SelectionKey selectionKey = (SelectionKey) it.next();

					if (!selectionKey.isValid())
						continue;

					// ������
					if (selectionKey.isAcceptable()) {
						log.debug("************** ������ ***************");
						log.debug("��������:"+count);
						System.out.println("----------������----------");
						ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
						serverChannel.accept().configureBlocking(false).register(selector,
								SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						lookId = 1;
					}

					// �ɶ� ��ȡ���ݷ���һ
					if (selectionKey.isReadable()) {
						//log.debug("************* �ɶ� ****************\n");
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(byfferSize);
						int len = 0;
						
						try {
							len = socketChannel.read(buff);
							//�쳣�ر�
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
							//�쳣�ر�
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
						// ��ȡ����
						String data = getString(buff);
						//��������״̬ //data.equals(up_locked) || data.equals(up_unlocked)
						if (firstConnection && dataCheck(data)){
							firstConnection = false;
							int lookState = Integer.parseInt(data.substring(3, 4));
							//������
							stateService.onlineByLookId(1, data);
							log.debug("##########################");
							log.debug("ͨ�ųɹ�����״̬Ϊ:"+(lookState == 2 ? "��":"����"));
							log.debug("##########################");
						}
						/**
						 * ������ִ�У���ȡ��������
						 */
						else if (execute) {
							log.debug("���Ի�ȡ��������");
							if (dataCheck(data)) {
								String feedback = feedback(data);
								log.debug("----------------------------------");
								log.debug("ִ�е�����:"+executeCommand);
								log.debug("��������:"+data);
								log.debug(feedback);
								log.debug("");
								
								gg = false;//�ָ�
								execute = false;
								executeCommand = "";
								System.out.println(jedis.set("lookNumber=1","true"));
								stateService.updateLookState(1, data);
								
							} else {
								log.debug("----------------------------------");
								log.debug("�����쳣,��������:"+data);
								log.debug("ִ�е�����:"+executeCommand);
								log.debug("");
								if (gg){//�ڶ��η����쳣 �ж�Ϊִ��ʧ��
									
									execute = false;
									executeCommand = "";
									jedis.set("lookNumber=1", "false");
									log.debug("ִ��ʧ��");
									log.debug("");
								}else {
									//�����ٴλ�ȡ
									/**
									 * ��ȡ��������Ϊ
									 * ��һ�λ�ȡ������Ϊ111111���ڶ��β��Ƿ�������AB.....
									 */
									gg = true;
								}
							}
						/**
						 * û������ִ�У���ȡ��������
						 */
						} else {
							System.out.println(data);
						}
					}

					// ��д
					if (selectionKey.isWritable()) {
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						ByteBuffer buff = ByteBuffer.allocate(byfferSize);
						//�״�����
						if (firstConnection){
							//firstConnection = false;
							//��ѯ����Ϣ
							buff.put(down_query.getBytes());
							buff.clear();
							socketChannel.write(buff);
						} else if (hasCommand() && executeCommand.equals("")) {
							execute = true;
							String command = getCommand();
							executeCommand = command;
							if (command.equals(down_lockup)) {
								log.debug("------------------------");
								log.debug("ִ��\t��\t����");
								log.debug("");
							} else if (command.equals(down_unlocking)) {
								log.debug("------------------------");
								log.debug("ִ��\t����\t����");
								log.debug("");
							} else if (command.equals(down_query)) {
								log.debug("------------------------");
								log.debug("ִ��\t��ѯ\t����");
								log.debug("");
							}
							buff.put(command.getBytes());
							buff.clear();// д����ʱ����
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
				return "�������";
			}
			case up_lock_completion: {
				return "�����";
			}
			case up_locked: {
				return "��������:��״̬";
			}
			case up_unlocked: {
				return "��������:��״̬";
			}
			case heartbeat_packet: {
				return "������ 111111";
			}
		}
		return null;
	}
	
	/**
	 * ���ӶϿ���־
	 */
	private void debugDisconnect(){
		log.debug("<------------------------>");
		log.debug("���ӶϿ�");
		log.debug("<------------------------>");
	}
	
	private void clearCommand(){
		String key = "lookNumber=1";
		if (jedis.exists(key))
		jedis.del(key);
	}
}
