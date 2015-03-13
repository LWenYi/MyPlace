package com.org.mwd.vo;

import java.util.List;
import org.apache.http.cookie.Cookie;
import com.baidu.mapapi.search.poi.PoiResult;
import android.app.Application;
import android.util.SparseArray;

public class SaveBean  extends Application{

		private UserBean ub = null;   // 登录用户信息
		private boolean isrun = false;  //定时与后台通信线程的运行判断
		private List<FriendBean>  friendList = null;  //好友列表
		private List<GroupBean>  GroupList = null;  //群列表
		private SparseArray<UserBean>  userMap =  new SparseArray<UserBean>();
		private PoiResult mPoiResult ;  //查找结果，用于获取GPS对应的真实地址
		private Cookie cookies;     //保存用户的sessionID
		private SparseArray<String>  address =  new SparseArray<String>();
		
		public SparseArray<String> getAddress() {
			return address;
		}
		public void setAddress(SparseArray<String> address) {
			this.address = address;
		}
		public boolean isIsrun() {
			return isrun;
		}
		public void setIsrun(boolean isrun) {
			this.isrun = isrun;
		}
	    public Cookie getCookie(){    
	        return cookies;
	    }
	    public void setCookie(Cookie cks){
	        cookies = cks;
	    }
	

		public PoiResult getmPoiResult() {
			return mPoiResult;
		}

		public void setmPoiResult(PoiResult mPoiResult) {
			this.mPoiResult = mPoiResult;
		}


		public SparseArray<UserBean> getUserMap() {
			return userMap;
		}

		public void setUserMap(SparseArray<UserBean> userMap) {
			this.userMap = userMap;
		}

		public List<GroupBean> getGroupList() {
			return GroupList;
		}

		public void setGroupList(List<GroupBean> groupList) {
			GroupList = groupList;
		}

		public List<FriendBean> getFriendList() {
			return friendList;
		}

		public void setFriendList(List<FriendBean> list) {
			this.friendList = list;
		}

		public UserBean getUb() {
			return ub;
		}

		public void setUb(UserBean ub) {
			this.ub = ub;
		}
	
		
}
