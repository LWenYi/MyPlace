package com.org.mwd.vo;

import java.io.Serializable;

public class UserBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String password;
	private String nick;
	private String image;
	private int  gender;
	private String birthday;
	private String desc;
	private String phone;
	private String address;
	private int isShare;
	private int status;
	private String realName;
	private String email;
	private String extra2;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getIsShare() {
		return isShare;
	}
	public void setIsShare(int isShare) {
		this.isShare = isShare;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getExtra2() {
		return extra2;
	}
	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}
	public UserBean(int id, String name, String password, String nick,
			String image, int gender, String birthday, String desc,
			String phone, String address, int isShare, int status,
			String realName, String email, String extra2) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.nick = nick;
		this.image = image;
		this.gender = gender;
		this.birthday = birthday;
		this.desc = desc;
		this.phone = phone;
		this.address = address;
		this.isShare = isShare;
		this.status = status;
		this.realName = realName;
		this.email = email;
		this.extra2 = extra2;
	}
	public UserBean() {
		// TODO Auto-generated constructor stub
	}
	
}
