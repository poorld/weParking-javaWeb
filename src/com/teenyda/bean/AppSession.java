package com.teenyda.bean;

public class AppSession {
	//是否有session
	private boolean hasSession;
	//是否过期
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
