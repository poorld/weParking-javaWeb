package com.teenyda.dao.admin;

public interface LookDao {
	/**
	 * һ������
	 */
	void available();
	
	/**
	 * һ��������
	 */
	void disabled();
	
	void offline();
	
	void offlineByLookId(int lookid);
	
	void onlineByLookId(int lookid,int state);
	
	void availableByLookId(int lookid);
	
	void disabledByLookId(int lookid);
}
