package com.teenyda.bean;

public class Response {
	private String state;
	private String msg;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Response [state=" + state + ", msg=" + msg + "]";
	}
	
	
}