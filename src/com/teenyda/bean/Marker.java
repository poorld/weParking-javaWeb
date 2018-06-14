package com.teenyda.bean;

public class Marker {
	private int id;
	private String title;
	private String iconPath;
	private String latitude;
	private String longitude;
	private String width;
	private String height;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	@Override
	public String toString() {
		return "Marker [id=" + id + ", title=" + title + ", iconPath=" + iconPath + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", width=" + width + ", height=" + height + "]";
	}
	
}
