package com.teenyda.service.admin;

import com.teenyda.dao.admin.LookDaoImpl;
import com.teenyda.dao.admin.MarkDaoImpl;

public class StateServiceImpl implements StateService{
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
	
	private final int look_close = 2;//��״̬
	private static final int available = 1;
	
	private final int look_open = 3; //����״̬
	private static final int disabled = 2;
	private LookDaoImpl lookDao = new LookDaoImpl();
	private MarkDaoImpl markDao = new MarkDaoImpl();
	
	@Override
	public void allAvailable() {
		// TODO Auto-generated method stub
		lookDao.available();
		markDao.available();
	}

	@Override
	public void allNnavailable() {
		// TODO Auto-generated method stub
		lookDao.disabled();
		markDao.disabled();
	}

	@Override
	public void allOffline() {
		// TODO Auto-generated method stub
		lookDao.offline();
	}

	/**
	 * ������ʱ���޸�����״̬
	 */
	@Override
	public void onlineByLookId(int lookid,String data) {
		// TODO Auto-generated method stub
		System.out.println(data);
		int lookState = Integer.parseInt(data.substring(3, 4));
		//�����ţ���������ʹ��
		if (data.equals(up_unlocked)) {
			lookState = disabled;//2
		}
		//���ţ��������ʹ��
		else if (data.equals(up_locked)) {
			lookState = available;//1
		}
		System.out.println(lookid+"��������,�޸�״̬:"+lookState);
		lookDao.onlineByLookId(lookid-1, lookState);
		markDao.online(lookid-1, lookState);
	}

	@Override
	public void offlineByLookId(int lookid) {
		// TODO Auto-generated method stub
		lookDao.offlineByLookId(lookid-1);
	}

	@Override
	public void updateLookState(int lookid, String state) {
		// TODO Auto-generated method stub
		//�����ţ���������ʹ��
		if (state.equals(up_unlock_completion)) {
			lookDao.disabledByLookId(0);
			markDao.disabledleByLookId(0);
		}
		//���ţ��������ʹ��
		else if (state.equals(up_lock_completion)) {
			lookDao.availableByLookId(0);
			markDao.availableByLookId(0);
		}
	}

}
