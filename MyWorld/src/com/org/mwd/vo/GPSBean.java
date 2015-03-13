package com.org.mwd.vo;

public class GPSBean {
	private int id;
	private String longitude;
	private String latitude;
	private  String getTime;
	private int userId;
	private String extra1;
	private String extra2;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getGetTime() {
		return getTime;
	}
	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getExtra1() {
		return extra1;
	}
	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}
	public String getExtra2() {
		return extra2;
	}
	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}
	public GPSBean(int id, String longitude, String latitude, String getTime,
			int userId, String extra1, String extra2) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.getTime = getTime;
		this.userId = userId;
		this.extra1 = extra1;
		this.extra2 = extra2;
	}
	public GPSBean() {
		// TODO Auto-generated constructor stub
	}

	
	
}
