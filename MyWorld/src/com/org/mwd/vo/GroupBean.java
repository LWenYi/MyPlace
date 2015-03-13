package com.org.mwd.vo;

import java.io.Serializable;

public class GroupBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; 
	private String groupName;
	private String groupImage;
	private int groupType;
	private int groupsize;
	private String createTime;
	private String desc;
	private int managerId;
	private int status;
	private String extra1;
	private String extra2;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupImage() {
		return groupImage;
	}
	public void setGroupImage(String groupImage) {
		this.groupImage = groupImage;
	}
	public int getGroupType() {
		return groupType;
	}
	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}
	public int getGroupsize() {
		return groupsize;
	}
	public void setGroupsize(int groupsize) {
		this.groupsize = groupsize;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getManagerId() {
		return managerId;
	}
	public void setManagerId(int managerId) {
		this.managerId = managerId;
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
	public GroupBean(int id, String groupName, String groupImage, int groupType,
			int groupsize, String createTime, String desc, int managerId,
			int status, String extra1, String extra2) {
		super();
		this.id = id;
		this.groupName = groupName;
		this.groupImage = groupImage;
		this.groupType = groupType;
		this.groupsize = groupsize;
		this.createTime = createTime;
		this.desc = desc;
		this.managerId = managerId;
		this.status = status;
		this.extra1 = extra1;
		this.extra2 = extra2;
	}
	public GroupBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
