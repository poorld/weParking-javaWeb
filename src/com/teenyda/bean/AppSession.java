package com.teenyda.bean;

public class AppSession {
	//�Ƿ���session
	private boolean hasSession;
	//�Ƿ����
	private boolean expired;
	public boolean isHasSession() {
		return hasSession;
	}
	public void setHasSession(boolean hasSession) {
		this.hasSession = hasSession;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	@Override
	public String toString() {
		return "AppSession [hasSession=" + hasSession + ", expired=" + expired + "]";
	}
	
	
}
