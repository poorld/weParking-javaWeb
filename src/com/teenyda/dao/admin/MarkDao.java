package com.teenyda.dao.admin;

public interface MarkDao {
	void available();
	
	void disabled();
	
	void online(int lookid,int state);
	
	void availableByLookId(int lookid);
	
	void disabledleByLookId(int lookid);
}
