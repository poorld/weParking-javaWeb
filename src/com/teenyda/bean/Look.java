package com.teenyda.bean;

import java.math.BigDecimal;

public class Look {
	private int lookid;//车位锁编号
	private int state;//车锁状态
	private String longitude;//经度
	private String latitude;//纬度
	private String address;//车位锁地址
	private int count;//车锁使用次数
	private int battery;//电池电量
	public int getLookid() {
		return lookid;
	}
	public void setLookid(int lookid) {
		this.lookid = lookid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getBattery() {
		return battery;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}
	@Override
	public String toString() {
		return "Look [lookid=" + lookid + ", state=" + state + ", longitude=" + longitude + ", latitude="
				+ latitude + ", address=" + address + ", count=" + count + ", battery=" + battery + "]";
	}
	
	
}
