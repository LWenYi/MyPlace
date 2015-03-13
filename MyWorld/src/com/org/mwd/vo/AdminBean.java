package com.org.mwd.vo;

public class AdminBean {
	private int id;
	private String admin;
	private String password;
	private int status;
	private String extra1;
	private String extra2;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public AdminBean(int id, String admin, String password, int status,
			String extra1, String extra2) {
		super();
		this.id = id;
		this.admin = admin;
		this.password = password;
		this.status = status;
		this.extra1 = extra1;
		this.extra2 = extra2;
	}
	public AdminBean() {
		// TODO Auto-generated constructor stub
	}
	
	

}
