package com.teenyda.bean;

public class Order {
	private int id;
	private String orderId;//订单id
	private String openId;//用户id
	private String lookId;//车锁id
	private int orderState;//订单状态
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String stayTime;//停留时间
	private String spend;//花费金额
	private String time;//生成订单时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getLookId() {
		return lookId;
	}
	public void setLookId(String lookId) {
		this.lookId = lookId;
	}
	public int getOrderState() {
		return orderState;
	}
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStayTime() {
		return stayTime;
	}
	public void setStayTime(String stayTime) {
		this.stayTime = stayTime;
	}
	public String getSpend() {
		return spend;
	}
	public void setSpend(String spend) {
		this.spend = spend;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", orderId=" + orderId + ", openId=" + openId + ", lookId=" + lookId
				+ ", orderState=" + orderState + ", startTime=" + startTime + ", endTime=" + endTime + ", stayTime="
				+ stayTime + ", spend=" + spend + ", time=" + time + "]";
	}
	
	
	
}
