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
import com.org.mwd.adapter.FriendTalkAdapter;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.view.MsgListView;
import com.org.mwd.view.MsgListView.OnRefreshListener;
import com.org.mwd.vo.FriendBean;
import com.org.mwd.vo.MessageBean;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;

import com.org.myworld.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FriendTalkActivity extends ListActivity implements OnClickListener {
	// 一些view的定义
	private Button friendinfo_btn, sendmessage_btn;
	private TextView friendname_tv, back_tv;
	private MsgListView message_list;
	private EditText messageContent;
	// 聊天内容的适配器
	private FriendTalkAdapter fta;
	// 所需的一些数据的对象的定义
	private FriendBean friend = null;
	private String content, datetime, type, groupId;
	private SaveBean save;
	private UserBean ub;
	private static boolean isrun = true;
	private static boolean isempty = true;
	private int getTime = 10 * 1000;
	// 聊天的内容
	private List<MessageBean> mDataArrays = new ArrayList<MessageBean>();
	private List<MessageBean> mDataArrays1 = new ArrayList<MessageBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendtalk);
		ExitApplication.getInstance().addActivity(this);
		isrun = true;
		isempty = true;
		message_list = (MsgListView) findViewById(android.R.id.list);
		message_list.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						new GetfriendHistoricalNewsTask().execute();// 刷新监听中，真正执行刷新动作
						message_list.onRefreshComplete();
					}
				}.execute();
			}
		});
		initView();
		initData();
	}

	// 初始化一些View
	private void initView() {
		save = (SaveBean) this.getApplicationContext();
		ub = save.getUb();

		back_tv = (TextView) this
				.findViewById(R.id.activity_friendtalk_returnsign_tv);
		back_tv.setOnClickListener(this);
		friendinfo_btn = (Button) this
				.findViewById(R.id.activity_friendtalk_friendinfo_btn);
		friendinfo_btn.setOnClickListener(this);
		sendmessage_btn = (Button) this
				.findViewById(R.id.activity_friendtalk_send_bt);
		sendmessage_btn.setOnClickListener(this);

		messageContent = (EditText) this
				.findViewById(R.id.activity_friendtalk_sendContent_et);
		friendname_tv = (TextView) this
				.findViewById(R.id.activity_friendtalk_friendname_tv);
		Intent i = this.getIntent();
		friend = (FriendBean) i.getSerializableExtra("friendInfo");
		if (null != friend) {
			friendname_tv.setText(friend.getFriendName());
		} else {
			this.finish();
		}
	}

	// 准备好群聊天消息
	private void initData() {
		new Thread(new ShowMessageThread()).start();
	}

	protected void onDestroy() {
		isrun = false;
		isempty = false;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.activity_friendtalk_returnsign_tv:
			this.finish();
			i = new Intent(this, MainActivity.class);
			i.putExtra("fragment", "contact");
			i.putExtra("what", "friend");
			this.startActivity(i);
			break;
		case R.id.activity_friendtalk_friendinfo_btn:
			findFriendInfoTask ffit = new findFriendInfoTask();
			ffit.execute(new String[] { String.valueOf(friend.getFriendId()) });
			break;
		case R.id.activity_friendtalk_send_bt:
			content = messageContent.getText().toString();
			datetime = MyUtil.getDateTime();
			type = "0";
			groupId = "0";
			if (TextUtils.isEmpty(messageContent.getText())) {
				System.out.println("friend-message is null!!");
				break;
			} else {
				SendMessageTask smt = new SendMessageTask();
				smt.execute();
			}
			break;
		}

	}

	// 根据好友Id查找好友信息并显示
	public class findFriendInfoTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindUserByIDServlet";
			String result = null;
			String friendId = arg0[0];
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("id", friendId));
				// params.add(new BasicNameValuePair("phone", arg0[1]));
				// params.add(new BasicNameValuePair("psw", arg0[2]));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("resultFriendInfo:" + result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equalsIgnoreCase(result)) { // 查找到数据
				Gson gson = new Gson();
				UserBean ub = gson.fromJson(result, new TypeToken<UserBean>() {
				}.getType());
				Intent intent = new Intent();
				intent.setClass(FriendTalkActivity.this,
						FriendInfoActivity.class);
				System.out.println("ub:" + ub.getName());
				Bundle bundle = new Bundle();
				bundle.putSerializable("friendInfo", ub);
				intent.putExtras(bundle);
				intent.putExtra("isFriend", "1");
				FriendTalkActivity.this.startActivity(intent);
			} else {// 没找到
				Toast.makeText(FriendTalkActivity.this, "信息不存在！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 用于用户刚打开这个群聊天Activity时拿到最后6条消息并展示出来
	public class ShowMessageListTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindMessagesFromFriendServlet";
			// String PATH =
			// "http://"+MyUtil.getIP()+":8080/MyWorldService/UpdateMessagesFromGroupServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("receiverId", String
						.valueOf(friend.getFriendId())));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("message:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equalsIgnoreCase(result)) { // 查找到消息数据
				Gson gson = new Gson();
				mDataArrays = gson.fromJson(result,
						new TypeToken<List<MessageBean>>() {
						}.getType());
				System.out.println("show messages size:" + mDataArrays.size());
				if (!mDataArrays.isEmpty()) {
					fta = new FriendTalkAdapter(FriendTalkActivity.this,
							mDataArrays);
					message_list.setAdapter(fta);
					message_list.setSelection(mDataArrays.size() - 1);
					new Thread(new UpdateMessageThread()).start();
				}
			} else {// 没找到

			}
		}
	}

	// 该线程用于定时刷新消息列表，起定时器的作用
	class UpdateMessageThread implements Runnable {
		public void run() {
			while (isrun) {
				System.out.println("Friend new Thread!!!");
				UpdateFriendMessageTask ufmt = new UpdateFriendMessageTask();
				ufmt.execute();
				try {
					Thread.sleep(getTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 该线程用于刷新一开始时的消息列表即还没有消息的时候，起定时器的作用
	class ShowMessageThread implements Runnable {
		public void run() {
			while (isempty) {
				if (mDataArrays.isEmpty()) {
					System.out.println("show messages Thread!!!");
					ShowMessageListTask smlt = new ShowMessageListTask();
					smlt.execute();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					isempty = false;
				}
			}
		}
	}

	// 用于用户发送消息且发送完成后展示在ListView上
	public class SendMessageTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/AddMessageServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("sendId", String.valueOf(ub
						.getId())));
				params.add(new BasicNameValuePair("phone", String.valueOf(ub
						.getPhone())));
				params.add(new BasicNameValuePair("psw", String.valueOf(ub
						.getPassword())));
				params.add(new BasicNameValuePair("groupId", groupId));
				params.add(new BasicNameValuePair("receiverId", String
						.valueOf(friend.getFriendId())));
				params.add(new BasicNameValuePair("content", content));
				params.add(new BasicNameValuePair("nick", ub.getNick()));
				params.add(new BasicNameValuePair("datetime", datetime));
				params.add(new BasicNameValuePair("type", type));
				if (!mDataArrays.isEmpty()) {
					params.add(new BasicNameValuePair("LastMessageId", String
							.valueOf(mDataArrays.get(mDataArrays.size() - 1)
									.getId())));
				} else {
					params.add(new BasicNameValuePair("LastMessageId", "0"));
				}
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result01:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equalsIgnoreCase(result)) { // 查找到消息数据
				if (!"nomessage".equalsIgnoreCase(result)) {
					Gson gson = new Gson();
					mDataArrays1 = gson.fromJson(result,
							new TypeToken<List<MessageBean>>() {
							}.getType());
					MyUtil.ArrayListCompose(mDataArrays, mDataArrays1);
					fta = new FriendTalkAdapter(FriendTalkActivity.this,
							mDataArrays);
					message_list.setAdapter(fta);
					message_list.setSelection(mDataArrays.size() - 1);
					messageContent.setText(null);
				}
			} else {// 没找到

			}
		}
	}

	// 用于定时更新消息列表，让用户可以定时接受到新的消息
	public class UpdateFriendMessageTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			// String PATH =
			// "http://"+MyUtil.getIP()+":8080/MyWorldService/FindMessagesFromGroupServlet";
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/UpdateMessagesFromFriendServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userId", String.valueOf(ub
						.getId())));
				params.add(new BasicNameValuePair("phone", String.valueOf(ub
						.getPhone())));
				params.add(new BasicNameValuePair("psw", String.valueOf(ub
						.getPassword())));
				params.add(new BasicNameValuePair("friendId", String
						.valueOf(friend.getFriendId())));
				params.add(new BasicNameValuePair("LastMessageId", String
						.valueOf(mDataArrays.get(mDataArrays.size() - 1)
								.getId())));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result01:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equalsIgnoreCase(result)) { // 查找到消息数据
				if (!"NoMessage".equalsIgnoreCase(result)) {
					Gson gson = new Gson();
					mDataArrays1 = gson.fromJson(result,
							new TypeToken<List<MessageBean>>() {
							}.getType());
					MyUtil.ArrayListCompose(mDataArrays, mDataArrays1);
					fta = new FriendTalkAdapter(FriendTalkActivity.this,
							mDataArrays);
					message_list.setAdapter(fta);
					message_list.setSelection(mDataArrays.size() - 1);
				} else {// 没找到
					System.out.println("no new messages!!");
				}
			} else {// 没找到

			}
		}
	}

	// 用于查看历史消息列表，让用户可以接受到历史消息
	public class GetfriendHistoricalNewsTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			// String PATH =
			// "http://"+MyUtil.getIP()+":8080/MyWorldService/FindMessagesFromGroupServlet";
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/GetFriendHistoricalNewsServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userId", String.valueOf(ub
						.getId())));
				params.add(new BasicNameValuePair("phone", String.valueOf(ub
						.getPhone())));
				params.add(new BasicNameValuePair("psw", String.valueOf(ub
						.getPassword())));
				params.add(new BasicNameValuePair("friendId", String
						.valueOf(friend.getFriendId())));
				if (!mDataArrays.isEmpty()) {
					params.add(new BasicNameValuePair("FirstMessageId", String
							.valueOf(mDataArrays.get(0).getId())));
				} else {
					params.add(new BasicNameValuePair("FirstMessageId", "0"));
				}
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result01:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!"failure".equalsIgnoreCase(result)) { // 查找到消息数据
				if (!"NoMessage".equalsIgnoreCase(result)) {
					Gson gson = new Gson();
					mDataArrays1 = gson.fromJson(result,
							new TypeToken<List<MessageBean>>() {
							}.getType());
					int i = mDataArrays1.size();
					MyUtil.ArrayListCompose(mDataArrays1, mDataArrays);
					MyUtil.ArrayListExchange(mDataArrays, mDataArrays1);
					fta = new FriendTalkAdapter(FriendTalkActivity.this,
							mDataArrays);
					message_list.setAdapter(fta);
					fta.notifyDataSetChanged();
					message_list.setSelection(i - 1);
				} else {// 没找到
					System.out.println("no old messages!!");
				}
			} else {// 没找到

			}
		}
	}
}
