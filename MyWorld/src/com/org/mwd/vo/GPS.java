package com.org.mwd.vo;

import java.io.Serializable;

public class GPS implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int userId;
	private int groupId;
	private String longitude;
	private String latitude;
	private String nick;
	private String phone;
	private String name;
	private String image;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public GPS(int userId, int groupId, String longitude, String latitude,
			String nick, String phone, String name, String image) {
		super();
		this.userId = userId;
		this.groupId = groupId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.nick = nick;
		this.phone = phone;
		this.name = name;
		this.image = image;
	}
	
}
