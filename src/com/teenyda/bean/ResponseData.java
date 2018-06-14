package com.teenyda.bean;

public class ResponseData {
	private String errMsg;
	private int errCode;
	private Object Data;
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public Object getData() {
		return Data;
	}
	public void setData(Object data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "ResponseData [errMsg=" + errMsg + ", errCode=" + errCode + ", Data=" + Data + "]";
	}
	
}
