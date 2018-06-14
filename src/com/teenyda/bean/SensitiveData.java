package com.teenyda.bean;

public class SensitiveData {
	private String js_code;
	private String encryptedData;
	private String iv;
	private String sessionKey;
	public String getJs_code() {
		return js_code;
	}
	public void setJs_code(String js_code) {
		this.js_code = js_code;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	@Override
	public String toString() {
		return "SensitiveData [js_code=" + js_code + ", encryptedData=" + encryptedData + ", iv=" + iv + ", sessionKey="
				+ sessionKey + "]";
	}

	
	
	
}
