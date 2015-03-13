package com.org.mwd.vo;

import java.io.Serializable;

public class FriendBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int userId;
	private int friendId;
	private String friendName;
	private int isShare;
	private String time;
	private String extra1;
	private String extra2;
	private int status;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getFriendId() {
		return friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public int getIsShare() {
		return isShare;
	}
	public void setIsShare(int isShare) {
		this.isShare = isShare;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public FriendBean(int id, int userId, int friendId, String friendName,
			int isShare, String time, String extra1, String extra2,int status) {
		super();
		this.id = id;
		this.userId = userId;
		this.friendId = friendId;
		this.friendName = friendName;
		this.isShare = isShare;
		this.time = time;
		this.extra1 = extra1;
		this.extra2 = extra2;
		this.status = status;
	}
	
	
}
