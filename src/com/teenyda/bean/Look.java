package com.teenyda.bean;

import java.math.BigDecimal;

public class Look {
	private int lookid;//��λ�����
	private int state;//����״̬
	private String longitude;//����
	private String latitude;//γ��
	private String address;//��λ����ַ
	private int count;//����ʹ�ô���
	private int battery;//��ص���
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
