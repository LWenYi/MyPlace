package com.org.mwd.vo;

public class Group_memberBean {
	
	private int id;
	private int groupId;
	private int userId;
	private int isShare;
	private String time;
	private int status;
	private String userName;
	private String groupName;
	private String extra1;
	private String extra2;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public Group_memberBean(int id, int groupId, int userId, int isShare,
			String time, int status, String userName, String groupName,
			String extra1, String extra2) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.userId = userId;
		this.isShare = isShare;
		this.time = time;
		this.status = status;
		this.userName = userName;
		this.groupName = groupName;
		this.extra1 = extra1;
		this.extra2 = extra2;
	}
}