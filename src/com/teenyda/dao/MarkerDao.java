package com.teenyda.dao;

import java.util.List;

import com.teenyda.bean.Marker;

/**
 * µØÍ¼±ê¼Ç
 * @author Administrator
 *
 */
interface MarkerDao {
	public List<Marker> getMarkers();
	
	public int markersCount();
	
	public void updateMarker(int state,int lookid);
	
	public void updateIcon(int state,int lookid);
	
	public boolean addMarkers(Marker marker,int state);
	
	void removeMarker(int lookid);
}
