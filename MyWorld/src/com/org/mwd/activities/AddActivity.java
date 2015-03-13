package com.org.mwd.activities;

import java.io.InputStream;
import java.util.ArrayList;
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
import com.org.mwd.adapter.FindFriendresultAdapter;
import com.org.mwd.adapter.FindGroupResultAdapter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends Activity implements OnClickListener {
	private EditText key_et;
	private RelativeLayout create_group_rl;
	private Button find_group_bt, find_friend_bt;
	private ListView result_lv;
	private TextView back_tv;
	private SaveBean save;
	private String keyWord;
	// ListView的Adapter
	private FindFriendresultAdapter adapter;
	private List<UserBean> ulist;
	// ListView底部View
	private String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		ExitApplication.getInstance().addActivity(this);
		save = (SaveBean) this.getApplicationContext();
		Intent intent = this.getIntent();
		from = intent.getStringExtra("contact");
		if (null == save.getUb() || null == save.getCookie()) {
			Intent i = new Intent(this, LoginActivity.class);
			this.startActivity(i);
		}
		back_tv = (TextView) this.findViewById(R.id.activity_add_back_tv);
		key_et = (EditText) this.findViewById(R.id.activity_add_key_et);
		find_friend_bt = (Button) this
				.findViewById(R.id.activity_add_findfriend_bt);
		find_group_bt = (Button) this
				.findViewById(R.id.activity_add_findgroup_bt);
		create_group_rl = (RelativeLayout) this
				.findViewById(R.id.activity_add_create_group);
		result_lv = (ListView) this.findViewById(R.id.android_add_list_lv);

		back_tv.setOnClickListener(this);
		find_friend_bt.setOnClickListener(this);
		find_group_bt.setOnClickListener(this);
		create_group_rl.setOnClickListener(this);
		ulist = new ArrayList<UserBean>();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		Intent i = null;
		switch (id) {
		case R.id.activity_add_back_tv: // 后退
			this.finish();
			if(from != null){
				i = new Intent(this, MainActivity.class); 
				i.putExtra("fragment", "contact");
				this.startActivity(i);
			}else{
				i = new Intent(this, MainActivity.class); 
				i.putExtra("fragment", "contact");
				i.putExtra("what", "friend");
				this.startActivity(i);
			}

			break;
		case R.id.activity_add_findfriend_bt: // 查找好友
			if (ulist != null && ulist.size() > 0) {
				ulist.clear();
			}
			keyWord = key_et.getText().toString();
			getUserListSizeTask gulst = new getUserListSizeTask();
			gulst.execute();

			break;
		case R.id.activity_add_findgroup_bt: // 查找群
			keyWord = key_et.getText().toString();
			ShowGroupListTask sui = new ShowGroupListTask();
			sui.execute(new String[] { keyWord, save.getUb().getName(),
					save.getUb().getPassword() });
			break;
		case R.id.activity_add_create_group: // 创建群
			this.finish();
			i = new Intent(this, CreateGroupActivity.class);
			this.startActivity(i);
			break;
		}
	}

	// 根据关键字查找用户的数量
	public class getUserListSizeTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindUserByKeyServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				;
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("keyWord", keyWord));
				params.add(new BasicNameValuePair("what", "1"));
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
			Gson gson = new Gson();
			System.out.println("788989:"+result);
			if (null != result) {
				List<Integer> idlist = gson.fromJson(result,
						new TypeToken<List<Integer>>() {
						}.getType());
				// 绑定监听器
				result_lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long arg3) {
						Intent intent = new Intent();
						intent.setClass(AddActivity.this,
								FriendInfoActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("friendInfo",
								ulist.get(position));
						intent.putExtras(bundle);
						intent.putExtra("isFriend", ulist.get(position)
								.getExtra2());
						AddActivity.this.startActivity(intent);
					}
				});
				for (int i = 0; i < idlist.size(); i++) {
					ShowUserListTask sul = new ShowUserListTask();
					sul.execute(new String[] { String.valueOf(String
							.valueOf(idlist.get(i))) });
				}
			}
		}

	}

	// 根据关键字显示好友列表
	public class ShowUserListTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindUserByKeyServlet";
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("id", arg0[0]));
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
			if (!"failure".equalsIgnoreCase(result)) { // 查找到数据
				System.out.println("result:" + result);
				Gson gson = new Gson();
				UserBean fUser = gson.fromJson(result,
						new TypeToken<UserBean>() {
						}.getType());
				if (null != fUser) {
					ulist.add(fUser);
					if (null != ulist && ulist.size() == 1) {
						adapter = new FindFriendresultAdapter(AddActivity.this,
								ulist);
						result_lv.setAdapter(adapter);
					}
					adapter.notifyDataSetChanged();
				} else {// 没找到
					Toast.makeText(getApplicationContext(), "信息不存在！",
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	}

	// 根据关键字显示群列表
	public class ShowGroupListTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindGroupByKeyServlet";
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("keyWord", arg0[0]));
				params.add(new BasicNameValuePair("name", arg0[1]));
				params.add(new BasicNameValuePair("psw", arg0[2]));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equalsIgnoreCase(result)) { // 查找到数据
				System.out.println("result:" + result);
				Gson gson = new Gson();
				final List<GroupBean> list = gson.fromJson(result,
						new TypeToken<List<GroupBean>>() {
						}.getType());
				FindGroupResultAdapter adapter = new FindGroupResultAdapter(
						AddActivity.this, list);
				result_lv.setAdapter(adapter);
				// user_lv.setOnCreateContextMenuListener(AddActivity.this);
				// //注册上下文菜单需要传LocalActivity 的上下文
				result_lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long arg3) {
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("group", list.get(position));
						intent.putExtras(bundle);
					}
				});

			} else {// 没找到
				Toast.makeText(getApplicationContext(), "信息不存在！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
