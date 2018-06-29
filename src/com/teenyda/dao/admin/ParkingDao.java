package com.teenyda.dao.admin;

import java.util.List;

import com.teenyda.bean.Look;

public interface ParkingDao {
	List<Look> getParkings();
	
	boolean updateParking(Look look);
	
	int addParking(Look look);
	
	void remove(int lookid);
	

}
