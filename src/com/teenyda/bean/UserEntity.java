package com.teenyda.bean;

public class UserEntity {
	private int id;
	private String nickName;//΢������
	private String phoneNumber;//�ֻ���
	private String plateNumber;//���ƺ�
	private String province;//ʡ��
	private String city;//����
	private String openid;//�û�Ψһ��ʶ��
	private String avatarurl;//ͷ���ַ
	private float balance;//���
	private int carvoucher;//�ó���
	private String time;//ע��ʱ��
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAvatarurl() {
		return avatarurl;
	}
	public void setAvatarurl(String avatarurl) {
		this.avatarurl = avatarurl;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public int getCarvoucher() {
		return carvoucher;
	}
	public void setCarvoucher(int carvoucher) {
		this.carvoucher = carvoucher;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", nickName=" + nickName + ", phoneNumber=" + phoneNumber + ", plateNumber="
				+ plateNumber + ", province=" + province + ", city=" + city + ", openid=" + openid + ", avatarurl="
				+ avatarurl + ", balance=" + balance + ", carvoucher=" + carvoucher + ", time=" + time + "]";
	}
	
	
}
