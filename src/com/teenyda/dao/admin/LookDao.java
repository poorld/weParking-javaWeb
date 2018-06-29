package com.teenyda.dao.admin;

public interface LookDao {
	/**
	 * 一键可用
	 */
	void available();
	
	/**
	 * 一键不可用
	 */
	void disabled();
	
	void offline();
	
	void offlineByLookId(int lookid);
	
	void onlineByLookId(int lookid,int state);
	
	void availableByLookId(int lookid);
	
	void disabledByLookId(int lookid);
}
