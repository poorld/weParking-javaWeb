package com.teenyda.service.admin;

interface StateService {
	void allAvailable();
	void allNnavailable();
	void allOffline();
	//������
	void onlineByLookId(int lookid,String data);
	//������
	void offlineByLookId(int lookid);
	void updateLookState(int lookid, String state);
}
