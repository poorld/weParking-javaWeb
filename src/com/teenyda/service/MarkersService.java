package com.teenyda.service;

import java.util.List;

import com.teenyda.bean.Marker;

public interface MarkersService {
	List<Marker> getMarkers();
	
	boolean isUpdate();
	
	void updateMarker(int state,int lookid);
}
