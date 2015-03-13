package com.org.mwd.vo;

public class MessageBean {
	private int id; 
	private int sendId;
	private int receiverId;
	private String content;
	private String image;
	private int type;
	private String sendTime;
	private int status;
	private int groupId;
	private String extra1;
	private String extra2;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSendId() {
		return sendId;
	}
	public void setSendId(int sendId) {
		this.sendId = sendId;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public MessageBean(int id, int sendId, int receiverId, String content,
			String image, int type, String sendTime, int status, int groupId,
			String extra1, String extra2) {
		super();
		this.id = id;
		this.sendId = sendId;
		this.receiverId = receiverId;
		this.content = content;
		this.image = image;
		this.type = type;
		this.sendTime = sendTime;
		this.status = status;
		this.groupId = groupId;
		this.extra1 = extra1;
		this.extra2 = extra2;
	}
	
	
	

}
