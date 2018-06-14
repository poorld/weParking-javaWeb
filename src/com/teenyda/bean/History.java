package com.teenyda.bean;

public class History {
	private String money;
	private String address;
	private String time;
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "History [money=" + money + ", address=" + address + ", time=" + time + "]";
	}
	
	
}
