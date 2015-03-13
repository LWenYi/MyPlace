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
import com.org.mwd.adapter.GroupTalkAdapter;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.view.MsgListView;
import com.org.mwd.view.MsgListView.OnRefreshListener;
import com.org.mwd.vo.GroupBean;
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

public class GroupTalkActivity extends ListActivity implements OnClickListener {
	private Button groupinfo_btn, sendmessage_btn;
	private TextView groupname_tv, back_tv;
	private GroupBean gb;
	private SaveBean save;
	private UserBean ub;
	private EditText mEditTextContent;
	// 聊天内容的适配器
	private GroupTalkAdapter gta;
	// listview
	private MsgListView mListView;
	private String content, datetime, type, receiverId;
	private int getTime = 10 * 1000;
	// 聊天的内容
	private List<MessageBean> mDataArrays = new ArrayList<MessageBean>();
	private List<MessageBean> mDataArrays1 = new ArrayList<MessageBean>();
	private static boolean isrun = true;
	private static boolean isempty = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouptalk);
		ExitApplication.getInstance().addActivity(this);
		isrun = true;
		isempty = true;
		mListView = (MsgListView) findViewById(android.R.id.list);
		mListView.setonRefreshListener(new OnRefreshListener() {
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
						new GetGroupHistoricalNewsTask().execute();// 刷新监听中，真正执行刷新动作
						mListView.onRefreshComplete();
					}
				}.execute();
			}
		});
		initView();
		initData();
	}

	protected void onDestroy() {
		isrun = false;
		isempty = false;
		super.onDestroy();
	}

	// 初始化一些View
	private void initView() {
		// 拿到传递群信息
		Intent i = this.getIntent();
		gb = (GroupBean) i.getSerializableExtra("groupInfo");
		System.out.println(gb.getGroupName());
		save = (SaveBean) this.getApplicationContext();
		ub = save.getUb();
		// 设置群的名字
		groupname_tv = (TextView) findViewById(R.id.activity_grouptalk_groupname);
		groupname_tv.setText(gb.getGroupName());
		mEditTextContent = (EditText) findViewById(R.id.activity_grouptalk_sendmessage_et);

		back_tv = (TextView) this
				.findViewById(R.id.activity_grouptalk_returnsign_tv);
		back_tv.setOnClickListener(this);
		groupinfo_btn = (Button) this
				.findViewById(R.id.activity_grouptalk_groupinfo_btn);
		groupinfo_btn.setOnClickListener(this);
		sendmessage_btn = (Button) findViewById(R.id.activity_grouptalk_sendmessage_btn);
		sendmessage_btn.setOnClickListener(this);
	}

	// 准备好群聊天消息
	private void initData() {
		new Thread(new ShowMessageThread()).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_grouptalk_returnsign_tv:
			this.finish();
			break;
		case R.id.activity_grouptalk_groupinfo_btn:
			Intent intent = new Intent(this, GroupInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("groupinfo", gb);
			bundle.putString("from", "groupTalk");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtras(bundle);
			this.startActivity(intent);
			break;
		case R.id.activity_grouptalk_sendmessage_btn:
			content = mEditTextContent.getText().toString();
			datetime = MyUtil.getDateTime();
			type = "1";
			receiverId = "0";
			if (TextUtils.isEmpty(mEditTextContent.getText())) {
				System.out.println("message is null!!");
				break;
			} else {
				SendMessageTask smt = new SendMessageTask();
				smt.execute();
			}
			break;
		}
	}

	// 用于用户刚打开这个群聊天Activity时拿到最后6条消息并展示出来
	public class ShowMessageListTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindMessagesFromGroupServlet";
			// String PATH =
			// "http://"+MyUtil.getIP()+":8080/MyWorldService/UpdateMessagesFromGroupServlet";
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
				params.add(new BasicNameValuePair("groupId", String.valueOf(gb
						.getId())));
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
					gta = new GroupTalkAdapter(GroupTalkActivity.this,
							mDataArrays);
					mListView.setAdapter(gta);
					mListView.setSelection(mDataArrays.size() - 1);
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
				System.out.println("Thread!!!");
				UpdateMessageTask umlt = new UpdateMessageTask();
				umlt.execute();
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
				params.add(new BasicNameValuePair("groupId", String.valueOf(gb
						.getId())));
				params.add(new BasicNameValuePair("receiverId", receiverId));
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
				Gson gson = new Gson();
				mDataArrays1 = gson.fromJson(result,
						new TypeToken<List<MessageBean>>() {
						}.getType());
				// MyUtil.ArrayListCompose(mDataArrays, mDataArrays1);
				mDataArrays.addAll(mDataArrays1);
				gta = new GroupTalkAdapter(GroupTalkActivity.this, mDataArrays);
				mListView.setAdapter(gta);
				mListView.setSelection(mDataArrays.size() - 1);
				mEditTextContent.setText(null);
			} else {// 没找到

			}
		}
	}

	// 用于定时更新消息列表，让用户可以定时接受到新的消息
	public class UpdateMessageTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			// String PATH =
			// "http://"+MyUtil.getIP()+":8080/MyWorldService/FindMessagesFromGroupServlet";
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/UpdateMessagesFromGroupServlet";
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
				params.add(new BasicNameValuePair("groupId", String.valueOf(gb
						.getId())));
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
					// MyUtil.ArrayListCompose(mDataArrays, mDataArrays1);
					mDataArrays.addAll(mDataArrays1);
					gta = new GroupTalkAdapter(GroupTalkActivity.this,
							mDataArrays);
					mListView.setAdapter(gta);
					mListView.setSelection(mDataArrays.size() - 1);
				} else {// 没找到
					System.out.println("no new messages!!");
				}
			} else {// 没找到

			}
		}
	}

	// 用于查看历史消息列表，让用户可以接受到历史消息
	public class GetGroupHistoricalNewsTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			// String PATH =
			// "http://"+MyUtil.getIP()+":8080/MyWorldService/FindMessagesFromGroupServlet";
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/GetGroupHistoricalNewsServlet";
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
				params.add(new BasicNameValuePair("groupId", String.valueOf(gb
						.getId())));
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
					// MyUtil.ArrayListCompose(mDataArrays1, mDataArrays);
					mDataArrays1.addAll(mDataArrays);
					MyUtil.ArrayListExchange(mDataArrays, mDataArrays1);
					gta = new GroupTalkAdapter(GroupTalkActivity.this,
							mDataArrays);
					mListView.setAdapter(gta);
					gta.notifyDataSetChanged();
					mListView.setSelection(i);
				} else {// 没找到
					System.out.println("no old messages!!");
				}
			} else {// 没找到

			}
		}
	}
}
