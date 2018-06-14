package com.teenyda.bean;

public class Data {
	private String isRegister;
	private String rdSession;
	
	
	public String getIsRegister() {
		return isRegister;
	}


	public void setIsRegister(String isRegister) {
		this.isRegister = isRegister;
	}


	public String getRdSession() {
		return rdSession;
	}


	public void setRdSession(String rdSession) {
		this.rdSession = rdSession;
	}


	@Override
	public String toString() {
		return "Data [isRegister=" + isRegister + ", rdSession=" + rdSession + "]";
	}
}
