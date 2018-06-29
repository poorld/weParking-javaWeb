package com.teenyda.service.admin;

interface StateService {
	void allAvailable();
	void allNnavailable();
	void allOffline();
	//锁上线
	void onlineByLookId(int lookid,String data);
	//锁下线
	void offlineByLookId(int lookid);
	void updateLookState(int lookid, String state);
}
