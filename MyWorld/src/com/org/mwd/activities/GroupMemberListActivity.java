package com.org.mwd.activities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.mwd.adapter.MemberAdapter;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class GroupMemberListActivity extends Activity {
	private ListView user_lv;
	private SaveBean save;
	private GroupBean g;
	private List<UserBean> list;
	private MemberAdapter adapter;
	private TextView back_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_group_member_list);
		ExitApplication.getInstance().addActivity(this);
		user_lv = (ListView) this
				.findViewById(R.id.activity_groupmember_memberlist_lv);
		back_tv = (TextView) this
				.findViewById(R.id.activity_groupmember_return_tv);
		back_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GroupMemberListActivity.this.finish();

			}

		});
		save = (SaveBean) this.getApplicationContext();
		Intent i = this.getIntent();
		g = (GroupBean) i.getSerializableExtra("groupInfo");
		list = new ArrayList<UserBean>();
		adapter = new MemberAdapter(this, list);
		user_lv.setAdapter(adapter);
		findAllGroupMemberIDTask fagt = new findAllGroupMemberIDTask();
		fagt.execute();
	}

	// 查找所有成员的id
	public class findAllGroupMemberIDTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/findAllGroupMemberServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("what", "getCount"));
				params.add(new BasicNameValuePair("groupId", String.valueOf(g
						.getId())));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("uid1:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equals(result)) {
				Gson gson = new Gson();
				List<Integer> list = gson.fromJson(result,
						new TypeToken<List<Integer>>() {
						}.getType());
				for (int i = 0; i < list.size(); i++) {
					findAllGroupMemberInfoTask fmt = new findAllGroupMemberInfoTask();
					fmt.execute(new String[] { String.valueOf(list.get(i)) });
				}
			}

		}
	}

	// 查找所有成员的信息
	public class findAllGroupMemberInfoTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/findAllGroupMemberServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("what", "getMember"));
				params.add(new BasicNameValuePair("userId", arg0[0]));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equals(result)) {
				Gson gson = new Gson();
				UserBean user = gson.fromJson(result,
						new TypeToken<UserBean>() {
						}.getType());
				list.add(user);
				Comparator<UserBean> comparator = new Comparator<UserBean>() {
					public int compare(UserBean s1, UserBean s2) {
						return s2.getStatus() - s1.getStatus();
					}
				};
				Collections.sort(list, comparator);
				adapter.notifyDataSetChanged();
			}
		}
	}

}
