package com.teenyda.bean;

public class Order {
	private int id;
	private String orderId;//����id
	private String openId;//�û�id
	private String lookId;//����id
	private int orderState;//����״̬
	private String startTime;//��ʼʱ��
	private String endTime;//����ʱ��
	private String stayTime;//ͣ��ʱ��
	private String spend;//���ѽ��
	private String time;//���ɶ���ʱ��
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
