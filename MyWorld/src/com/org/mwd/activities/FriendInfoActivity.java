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

import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.FriendBean;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendInfoActivity extends Activity {
	private ImageView person_img_iv, location_iv;
	private Button add_friend_bt, delfriend_bt;
	private TextView nick_tv, gender_tv, address_tv, number_tv, birth_tv,
			email_tv, desc_tv, back_tv;
	private SaveBean save;
	private UserBean ub, user;
	private RelativeLayout location_rl;
	private LinearLayout add_friend_ll, delfriend_ll;
	private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_friend_info);
		ExitApplication.getInstance().addActivity(this);
		save = (SaveBean) this.getApplicationContext();
		user = save.getUb();
		init();
		Intent i = this.getIntent();
		String isFriend = i.getStringExtra("isFriend");
		from = i.getStringExtra("from");
		ub = (UserBean) i.getSerializableExtra("friendInfo");
		if (ub.getId() == user.getId()) { // 登录用户个人资料
			add_friend_ll.setVisibility(8);
			delfriend_ll.setVisibility(8);
			location_rl.setVisibility(8);
		} else if ("1".equalsIgnoreCase(isFriend)) { // 登录用户好友资料
			add_friend_ll.setVisibility(8);
			delfriend_ll.setVisibility(0);
			location_rl.setVisibility(0);
		} else { // 陌生人资料
			add_friend_ll.setVisibility(0);
			delfriend_ll.setVisibility(8);
			location_rl.setVisibility(8);
		}
		setFriendInfo();

	}

	// 初始化组件
	private void init() {
		back_tv = (TextView) findViewById(R.id.activity_friend_info_back_tv);
		location_iv = (ImageView) findViewById(R.id.activity_friend_info_location_iv);
		person_img_iv = (ImageView) findViewById(R.id.activity_friend_info_img_iv);
		add_friend_bt = (Button) findViewById(R.id.activity_friend_info_add_friend_bt);
		delfriend_bt = (Button) findViewById(R.id.activity_friend_info_delfriend_bt);
		nick_tv = (TextView) findViewById(R.id.activity_friend_info_nick_tv);
		gender_tv = (TextView) findViewById(R.id.activity_friend_info_gender_tv);
		address_tv = (TextView) findViewById(R.id.activity_friend_info_address_tv);
		number_tv = (TextView) findViewById(R.id.activity_friend_info_number_tv);
		birth_tv = (TextView) findViewById(R.id.activity_friend_info_birth_tv);
		email_tv = (TextView) findViewById(R.id.activity_friend_info_email_tv);
		desc_tv = (TextView) findViewById(R.id.activity_friend_info_desc_tv);
		location_rl = (RelativeLayout) findViewById(R.id.activity_friend_info_location_rl);
		add_friend_ll = (LinearLayout) findViewById(R.id.activity_friend_info_add_friend_ll);
		delfriend_ll = (LinearLayout) findViewById(R.id.activity_friend_info_delfriend_ll);
		add_friend_bt.setOnClickListener(new OnClickListener() { // 添加好友
					@Override
					public void onClick(View arg0) {
						if ("1".equalsIgnoreCase(user.getExtra2())) {
							Toast.makeText(FriendInfoActivity.this,
									"你们已经是好友关系！！！！", Toast.LENGTH_LONG).show();
						} else {
							ApplyAddFriendTask aft = new ApplyAddFriendTask();
							aft.execute(new String[] {
									String.valueOf(user.getId()), null,
									"isApplyAddFriend" });
						}

					}
				});
		delfriend_bt.setOnClickListener(new OnClickListener() { // 删除好友
					@Override
					public void onClick(View arg0) {
						delFriendTask dft = new delFriendTask();
						dft.execute();
					}
				});
		back_tv.setOnClickListener(new OnClickListener() { // 返回
			@Override
			public void onClick(View arg0) {
				FriendInfoActivity.this.finish();
				if ("1".equalsIgnoreCase(from)) {
					Intent intent = new Intent(FriendInfoActivity.this,
							MainActivity.class);
					intent.putExtra("fragment", "contact");
					intent.putExtra("what", "friend");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					FriendInfoActivity.this.startActivity(intent);
				}

			}
		});
		location_iv.setOnClickListener(new OnClickListener() { // 定位好友
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(FriendInfoActivity.this,
								MainActivity.class);
						intent.putExtra("fragment", "friend_location");
						intent.putExtra("friendId", ub.getId());
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						FriendInfoActivity.this.startActivity(intent);

					}
				});

	}

	private void setFriendInfo() {

		if (null != ub.getImage()) { // 设置昵称
			byte[] imgByte = MyUtil.hex2byte(ub.getImage());
			Bitmap bitmap = null;
			if (null != imgByte) {
				bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
						imgByte.length, null);
				person_img_iv.setImageBitmap(bitmap);
			}
			person_img_iv.setImageResource(R.drawable.default_userphoto);
		} else {
			person_img_iv.setImageResource(R.drawable.default_userphoto);
		}
		if (ub.getNick() != null) { // 设置昵称
			nick_tv.setText(ub.getNick());
		} else {
			nick_tv.setText(ub.getName());
		}
		if (ub.getGender() == 0) { // 设置性别
			gender_tv.setText("男");
		} else if (ub.getGender() == 1) {
			gender_tv.setText("女");
		} else {
			gender_tv.setText("不详");
		}
		if (ub.getAddress() != null) { // 设置地址
			address_tv.setText(ub.getAddress());
		} else {
			address_tv.setText("");
		}
		if (ub.getName() != null) { // 设置账号
			number_tv.setText(ub.getName());
		} else {
			number_tv.setText("无");
		}
		if (ub.getBirthday() != null) {
			birth_tv.setText(ub.getBirthday());
		} else {
			birth_tv.setText("不详！！");
		}
		if (ub.getEmail() != null) {
			email_tv.setText(ub.getEmail());
		} else {
			email_tv.setText("暂无邮箱！！");
		}
		if (ub.getDesc() != null) {
			desc_tv.setText(ub.getDesc());
		}

	}

	// 添加好友的异步任务
	public class ApplyAddFriendTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/ApplyAddFriendServlet";
			System.out.println("path:" + PATH);
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("friendId", String.valueOf(ub
						.getId())));
				params.add(new BasicNameValuePair("content", arg0[1]));
				params.add(new BasicNameValuePair("action", arg0[2]));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpParams hparams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(hparams, 5000); // 设置连接超时
				HttpConnectionParams.setSoTimeout(hparams, 10000); // 设置请求超时
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result:" + result);
				} else {
					System.out.println("请求服务器失败！！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("4".equalsIgnoreCase(result)) {
				Toast.makeText(FriendInfoActivity.this, "申请添加好友成功，等待对方验证！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("5".equalsIgnoreCase(result)) {
				Toast.makeText(FriendInfoActivity.this, "对不起,申请添加好友失败！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(FriendInfoActivity.this, "对不起,请登录后再试！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("2".equalsIgnoreCase(result)) {
				Toast.makeText(FriendInfoActivity.this, "您已经发送申请，请不要重复发送！",
						Toast.LENGTH_SHORT).show();
			} else if ("1".equalsIgnoreCase(result)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FriendInfoActivity.this);
				final EditText desc = new EditText(FriendInfoActivity.this);
				builder.setTitle("请输入验证信息：");
				builder.setView(desc);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								ApplyAddFriendTask aft = new ApplyAddFriendTask();
								aft.execute(new String[] {
										String.valueOf(user.getId()),
										desc.getText().toString(),
										"ApplyAddFriend" });
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
				builder.create().show();
			} else if ("3".equalsIgnoreCase(result)) {
				Toast.makeText(FriendInfoActivity.this, "你们已经是好友关系，请刷新数据！！！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 删除好友的异步任务
	public class delFriendTask extends AsyncTask<String, Integer, String> {
		int friendId = 0;

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/DelFriendServlet";
			System.out.println("path:" + PATH);
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("phone", save.getUb()
						.getName()));
				params.add(new BasicNameValuePair("psw", save.getUb()
						.getPassword()));
				params.add(new BasicNameValuePair("userId", String.valueOf(save
						.getUb().getId())));
				params.add(new BasicNameValuePair("friendId", String.valueOf(ub
						.getId())));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpParams hparams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(hparams, 5000); // 设置连接超时
				HttpConnectionParams.setSoTimeout(hparams, 10000); // 设置请求超时
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result:" + result);
				} else {
					System.out.println("请求服务器失败！！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("success".equalsIgnoreCase(result)) {
				List<FriendBean> flist = save.getFriendList();
				if (flist != null) {
					for (int i = 0; i < flist.size(); i++) {
						if (friendId == flist.get(i).getFriendId()) {
							flist.remove(i);
							save.setFriendList(flist);
							break;
						}
					}
				}

				FriendInfoActivity.this.finish();
				Toast.makeText(getApplicationContext(), "删除好友成功！！！",
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent(
						FriendInfoActivity.this.getApplicationContext(),
						MainActivity.class);
				i.putExtra("fragment", "contact");
				FriendInfoActivity.this.startActivity(i);
			} else {
				FriendInfoActivity.this.finish();
				Toast.makeText(getApplicationContext(), "删除好友失败！！！",
						Toast.LENGTH_SHORT).show();
			}

		}
	}
}
