package com.teenyda.service;

import java.util.List;

import com.teenyda.bean.Marker;
import com.teenyda.dao.MarkerDaoImpl;

import redis.clients.jedis.Jedis;

public class MarkersServiceImap implements MarkersService{
	MarkerDaoImpl markerDao = new MarkerDaoImpl();
	private Jedis jedis = new Jedis();
	@Override
	public List<Marker> getMarkers() {
		// TODO Auto-generated method stub
		return markerDao.getMarkers();
	}
	@Override
	public boolean isUpdate() {
		// TODO Auto-generated method stub'
		String count = markerDao.markersCount()+"";
		String markersCount = jedis.get("markersCount");
		if (count.equals(markersCount)) {
			return false;
		}
		return true;
	}
	
	@Override
	public void updateMarker(int state,int lookid) {
		// TODO Auto-generated method stub
		markerDao.updateMarker(state,lookid);//ÐÞ¸Ä×´Ì¬
		markerDao.updateIcon(state, lookid);//ÐÞ¸ÄÍ¼±ê
	}

}
