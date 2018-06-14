package com.teenyda.service.admin;

import java.util.List;

import com.teenyda.bean.Look;
import com.teenyda.bean.Marker;
import com.teenyda.dao.MarkerDaoImpl;
import com.teenyda.dao.admin.ParkingDaoImpl;

public class ParkingService {
	ParkingDaoImpl parkingDao = new ParkingDaoImpl();
	MarkerDaoImpl markerDao = new MarkerDaoImpl();
	
	public List<Look> getParkings() {
		return parkingDao.getParkings();
	}
	
	public boolean updateParking(Look look) {
		markerDao.updateMarker(look.getState(), look.getLookid());
		markerDao.updateIcon(look.getState(), look.getLookid());
		return parkingDao.updateParking(look);
	}
	
	public boolean addParking(Look look) {
		Marker marker = new Marker();
		int id = parkingDao.addParking(look);
		marker.setId(id);
		marker.setTitle(look.getAddress());
		marker.setLatitude(look.getLatitude());
		marker.setLongitude(look.getLongitude());
		System.out.println("id="+id);
		markerDao.addMarkers(marker, look.getState());
		return true;
	}
	
	public void removeParking(int lookid) {
		parkingDao.remove(lookid);
		markerDao.removeMarker(lookid);
	}
}
