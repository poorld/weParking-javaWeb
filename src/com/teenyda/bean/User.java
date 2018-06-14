package com.teenyda.bean;

public class User {
	private String appid;
	private String secret;
	private String js_code;
	private String encryptedData;
	private String iv;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
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
	@Override
	public String toString() {
		return "User [appid=" + appid + ", secret=" + secret + ", js_code=" + js_code + ", encryptedData="
				+ encryptedData + ", iv=" + iv + "]";
	}
	
	
}
