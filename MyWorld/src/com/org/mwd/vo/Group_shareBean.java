package com.org.mwd.vo;

public class Group_shareBean {
	
	private int id;
	private String content;
	private String image;
	private int userId;
	private String uploadTime;
	private int groupId;
	private String extra1;
	private String extra2;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
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
	public Group_shareBean(int id, String content, String image, int userId,
			String uploadTime, int groupId, String extra1, String extra2) {
		super();
		this.id = id;
		this.content = content;
		this.image = image;
		this.userId = userId;
		this.uploadTime = uploadTime;
		this.groupId = groupId;
		this.extra1 = extra1;
		this.extra2 = extra2;
	}
	
	
	

}
