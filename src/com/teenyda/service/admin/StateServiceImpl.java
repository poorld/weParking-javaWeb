package com.teenyda.service.admin;

import com.teenyda.dao.admin.LookDaoImpl;
import com.teenyda.dao.admin.MarkDaoImpl;

public class StateServiceImpl implements StateService{
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
	
	private final int look_close = 2;//锁状态
	private static final int available = 1;
	
	private final int look_open = 3; //开锁状态
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
	 * 锁上线时，修改锁的状态
	 */
	@Override
	public void onlineByLookId(int lookid,String data) {
		// TODO Auto-generated method stub
		System.out.println(data);
		int lookState = Integer.parseInt(data.substring(3, 4));
		//锁开着，代表正在使用
		if (data.equals(up_unlocked)) {
			lookState = disabled;//2
		}
		//锁着，代表可以使用
		else if (data.equals(up_locked)) {
			lookState = available;//1
		}
		System.out.println(lookid+"号锁上线,修改状态:"+lookState);
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
		//锁开着，代表正在使用
		if (state.equals(up_unlock_completion)) {
			lookDao.disabledByLookId(0);
			markDao.disabledleByLookId(0);
		}
		//锁着，代表可以使用
		else if (state.equals(up_lock_completion)) {
			lookDao.availableByLookId(0);
			markDao.availableByLookId(0);
		}
	}

}
