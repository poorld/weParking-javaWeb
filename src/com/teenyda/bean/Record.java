package com.teenyda.bean;

public class Record {
	private int id;
	private String openid;
	private String orderid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	@Override
	public String toString() {
		return "Record [id=" + id + ", openid=" + openid + ", orderid=" + orderid + "]";
	}
	
	
}
