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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.mwd.fragements.ContactFragment;
import com.org.mwd.fragements.MapFragment;
import com.org.mwd.fragements.MessageFragment;
import com.org.mwd.fragements.SettingFragment;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private RelativeLayout map_layout = null;
	private RelativeLayout contact_layout = null;
	private RelativeLayout message_layout = null;
	private RelativeLayout setting_layout = null;
	private RelativeLayout count_rl = null;
	private Fragment map_fragment = null;
	private Fragment contact_fragment = null;
	private Fragment message_fragment = null;
	private Fragment setting_fragment = null;
	private TextView count_tv = null;
	private long mExitTime;
	private SaveBean save = null;
	private Handler mHandler;
	private Bundle bundle = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 
		 * */
		SDKInitializer.initialize(this.getApplicationContext());
		ExitApplication.getInstance().addActivity(this);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findComponentView();
		save = (SaveBean) this.getApplicationContext();
		bundle = new Bundle();
		setContentView(R.layout.activity_main);
		// 用户已登录，开启保持登陆的线程
		if (null != save.getCookie() && save.isIsrun()) {
			keepLoginThread klt = new keepLoginThread();
			Thread kt = new Thread(klt);
			kt.start();
		}
		// 初始化组件
		findComponentView();
		// 获取其它Activity跳转时传来的信息
		Intent i = this.getIntent();
		String what = i.getStringExtra("fragment");
		if ("contact".equalsIgnoreCase(what)) { // 转到联系人界面
			if (null == contact_fragment) {
				contact_fragment = new ContactFragment();
			}
			bundle.clear();
			bundle.putString("what", i.getStringExtra("what"));
			contact_fragment.setArguments(bundle);
			setCurrentContent(contact_layout, contact_fragment);
		} else if ("message".equalsIgnoreCase(what)) {// 转到消息界面
			if (null == message_fragment) {
				message_fragment = new MessageFragment();
			}
			setCurrentContent(message_layout, message_fragment);
		} else if ("setting".equalsIgnoreCase(what)) {// 转到设置界面
			if (null == setting_fragment) {
				setting_fragment = new SettingFragment();
			}
			setCurrentContent(setting_layout, setting_fragment);
		} else if ("frindInfo".equalsIgnoreCase(what)) { // 转到好友信息界面
			findFriendInfoTask ffit = new findFriendInfoTask();
			ffit.execute(new String[] { i.getStringExtra("friendId") });
		} else if ("friend_location".equalsIgnoreCase(what)) {// 转到地图界面
			int friendId = i.getIntExtra("friendId", 0);
			if (friendId != 0) {
				if (null == map_fragment) {
					map_fragment = new MapFragment();
				}
				bundle.clear();
				bundle.putInt("friendId", friendId);
				bundle.putString("what", "friend_location");
				map_fragment.setArguments(bundle);
				setCurrentContent(map_layout, map_fragment);

			} 
		} else if ("group_location".equalsIgnoreCase(what)) {// 转到地图界面
			int groupId = i.getIntExtra("groupId", 0);
			if (groupId != 0) {
				if (null == map_fragment) {
					map_fragment = new MapFragment();
				}
				bundle.clear();
				bundle.putInt("groupId", groupId);
				bundle.putString("what", "group_location");
				map_fragment.setArguments(bundle);
				setCurrentContent(map_layout, map_fragment);
			}
		} else if ("add_poiresult".equalsIgnoreCase(what)) {  
			if (null == map_fragment) {
				map_fragment = new MapFragment();
			}
			bundle.clear();
			bundle.putString("what", "poisearch");
			map_fragment.setArguments(bundle);
			setCurrentContent(map_layout, map_fragment);
		} else if ("nearby_friend".equalsIgnoreCase(what)) {
			if (null == map_fragment) {
				map_fragment = new MapFragment();
			}
			bundle.clear();
			bundle.putString("what", "nearby_friend");
			map_fragment.setArguments(bundle);
			setCurrentContent(map_layout, map_fragment);
		} else {
			// 设置mapfraghent为默认页面
			map_fragment = new MapFragment();
			setCurrentContent(map_layout, map_fragment);
		}
	}
	/**
	 * 初始化组件
	 * */ 
	private void findComponentView() {
		count_tv = (TextView) this
				.findViewById(R.id.activity_main_message_count_tv);
		count_rl = (RelativeLayout) this
				.findViewById(R.id.activity_main_message_count_rl);
		map_layout = (RelativeLayout) this
				.findViewById(R.id.activity_main_map_layout);
		contact_layout = (RelativeLayout) this
				.findViewById(R.id.activity_main_contact_layout);
		message_layout = (RelativeLayout) this
				.findViewById(R.id.activity_main_message_layout);
		setting_layout = (RelativeLayout) this
				.findViewById(R.id.activity_main_setting_layout);

		map_layout.setOnClickListener(this);
		contact_layout.setOnClickListener(this);
		message_layout.setOnClickListener(this);
		setting_layout.setOnClickListener(this);
		mHandler = new Handler(MainActivity.this.getApplicationContext()
				.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 定时与服务器的连通返回结果
				String result = msg.getData().getString("result");
				count_rl.setVisibility(8);
				if ("-1".equalsIgnoreCase(result)) {
					Toast.makeText(getApplicationContext(), "请求服务器超时，请重新登陆！！！",
							Toast.LENGTH_LONG).show();
					save.setUb(null);
					save.setCookie(null);
					save.setIsrun(false);
					MainActivity.this.finish();
					Intent i = new Intent(MainActivity.this,
							LoginActivity.class);
					MainActivity.this.startActivity(i);
				} else if ("-2".equalsIgnoreCase(result)) {
					save.setUb(null);
					save.setCookie(null);
					save.setIsrun(false);
					Toast.makeText(getApplicationContext(), "你被管理员强制下线",
							Toast.LENGTH_LONG).show();
					MainActivity.this.finish();
					Intent i = new Intent(MainActivity.this,
							LoginActivity.class);
					MainActivity.this.startActivity(i);
				} else {// 显示申请信息
					if ("0".equalsIgnoreCase(result)) {
						count_rl.setVisibility(8);
					} else {
						count_rl.setVisibility(0);
						count_tv.setText(result);
					}
				}

			}
		};
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				save.setIsrun(false);
				ExitApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.activity_main_map_layout:
			if (null == map_fragment) {
				map_fragment = new MapFragment();
			}
			setCurrentContent(map_layout, map_fragment);
			break;
		case R.id.activity_main_contact_layout:
			if (null != save.getCookie() && save.isIsrun()) {
				if (null == contact_fragment) {
					contact_fragment = new ContactFragment();
				}
				setCurrentContent(contact_layout, contact_fragment);
			} else {
				this.startActivity(new Intent(this, LoginActivity.class));
			}

			break;
		case R.id.activity_main_message_layout:
			if (null != save.getCookie() && save.isIsrun()) {
				if (null == message_fragment) {
					message_fragment = new MessageFragment();
				}
				setCurrentContent(message_layout, message_fragment);
			} else {
				this.startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.activity_main_setting_layout:
			if (null != save.getCookie() && save.isIsrun()) {
				if (null == setting_fragment) {
					setting_fragment = new SettingFragment();
				}
				setCurrentContent(setting_layout, setting_fragment);
			} else {
				this.startActivity(new Intent(this, LoginActivity.class));
			}
			break;

		}

	}

	// 设置contactfragment显示的内容
	private void setCurrentContent(RelativeLayout cilcked_layout, Fragment frg) {
		map_layout.setEnabled(true);
		contact_layout.setEnabled(true);
		message_layout.setEnabled(true);
		setting_layout.setEnabled(true);
		cilcked_layout.setEnabled(false);

		map_layout
				.setBackgroundResource(R.drawable.activity_main_bottom_bgcolor);
		contact_layout
				.setBackgroundResource(R.drawable.activity_main_bottom_bgcolor);
		message_layout
				.setBackgroundResource(R.drawable.activity_main_bottom_bgcolor);
		setting_layout
				.setBackgroundResource(R.drawable.activity_main_bottom_bgcolor);
		cilcked_layout.setBackgroundResource(R.color.big_blue);
		FragmentTransaction transaction = this.getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.content, frg).commit();

	}

	// 根据显示好友Id查找好友信息并显示
	public class findFriendInfoTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindUserByIdServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
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
			if (!"failure".equalsIgnoreCase(result)) { // 查找到数据
				Gson gson = new Gson();
				UserBean friend = gson.fromJson(result,
						new TypeToken<UserBean>() {
						}.getType());
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, FriendInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("friend", friend);
				intent.putExtras(bundle);
				MainActivity.this.startActivity(intent);
			} else {// 没找到
				Toast.makeText(MainActivity.this.getApplicationContext(),
						"信息不存在！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class keepLoginThread implements Runnable {
		@Override
		// 在run方法中定义任务
		public void run() {
			while (save.isIsrun()) {
				String PATH = "http://" + MyUtil.getIP()
						+ ":8080/MyWorldService/KeepLoginServlet";
				String result = null;
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost post = new HttpPost(PATH);
					if (null != save.getCookie()) {
						post.setHeader("Cookie", "sessionId="
								+ save.getCookie().getValue());
					}
					HttpParams hparams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(hparams, 5000); // 设置连接超时
					HttpConnectionParams.setSoTimeout(hparams, 10000); // 设置请求超时
					HttpResponse response = httpclient.execute(post);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						InputStream is = response.getEntity().getContent();
						result = MyUtil.inStreamToString(is);
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("count", result);
						msg.setData(bundle);
						mHandler.sendMessage(msg);

					}
					Thread.sleep(10 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
