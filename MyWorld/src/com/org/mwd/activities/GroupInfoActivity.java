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
import com.org.mwd.adapter.TransferGroupMemberAdapter;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroupInfoActivity extends Activity implements OnClickListener {
	private RelativeLayout manageGroup_rl, activity_group_number_rl;
	private TextView groupname_tv, groupnum_tv, desc_tv, groupType_tv,
			groupmember_tv, back_tv;
	private ImageView groupImg_iv;
	private Button location_bt, exitgroup_bt, addgroup_bt, dismissgroup_bt,
			transfergroup_bt;
	private SaveBean save;
	private UserBean ub;
	private GroupBean g = null;
	private List<UserBean> ulist = new ArrayList<UserBean>();
	private TransferGroupMemberAdapter adapter;

	// private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_group_info);
		ExitApplication.getInstance().addActivity(this);
		initCompent();
		save = (SaveBean) this.getApplicationContext();
		ub = save.getUb();
		Intent i = this.getIntent();
		g = (GroupBean) i.getSerializableExtra("groupinfo");
		// from = i.getStringExtra("from");
		if (g != null) {
			FindMemberCountInfoTask fmcf = new FindMemberCountInfoTask();
			fmcf.execute();
			if (ub.getId() == g.getManagerId()) {
				exitgroup_bt.setVisibility(8);
				addgroup_bt.setVisibility(8);
				dismissgroup_bt.setVisibility(0);
				transfergroup_bt.setVisibility(0);
				manageGroup_rl.setVisibility(0);
			} else {
				manageGroup_rl.setVisibility(4);
				IsGroupMemberInfoTask isgit = new IsGroupMemberInfoTask();
				isgit.execute();
			}
			setGroupInfo();
			GetGroupInfoByGroupIdTask ggibt = new GetGroupInfoByGroupIdTask();
			ggibt.execute();
		} else {
			System.out.println("群信息为空！！！");
		}
	}

	// 设置群信息
	private void setGroupInfo() {
		groupname_tv.setText(g.getGroupName()); // 群名称
		groupnum_tv.setText(g.getExtra1()); // 群号
		if (null == g.getExtra2() || g.getExtra2().length() <= 0) {// 群头像
			groupImg_iv.setImageResource(R.drawable.default_groupphoto);
		} else {
			byte[] imgByte = MyUtil.hex2byte(g.getExtra2());
			if (null == imgByte) {
				groupImg_iv.setImageResource(R.drawable.default_groupphoto);
			} else {
				Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
						imgByte.length, null);
				groupImg_iv.setImageBitmap(bitmap);
			}

		}
		// 群类型
		if (g.getGroupType() == 0) {
			groupType_tv.setText("同事朋友");
		} else if (g.getGroupType() == 1) {
			groupType_tv.setText("兴趣爱好");
		} else if (g.getGroupType() == 2) {
			groupType_tv.setText("志愿活动");
		} else if (g.getGroupType() == 3) {
			groupType_tv.setText("外出旅游");
		}
		if (null == g.getDesc() || "".equalsIgnoreCase(g.getDesc())) {
			desc_tv.setText("群主很懒,你有什么办法！");
		} else {
			desc_tv.setText(g.getDesc());
		}

	}

	// 初始化各个组件
	private void initCompent() {
		groupname_tv = (TextView) this
				.findViewById(R.id.activity_groupinfo_groupname_tv);
		groupnum_tv = (TextView) this
				.findViewById(R.id.activity_groupinfo_num_tv);
		groupType_tv = (TextView) this
				.findViewById(R.id.acyivity_groupinfo_myType_tv);
		groupmember_tv = (TextView) this
				.findViewById(R.id.acyivity_groupinfo_groupmember_tv);
		desc_tv = (TextView) this.findViewById(R.id.activity_groupinfo_desc_tv);

		exitgroup_bt = (Button) this
				.findViewById(R.id.activity_groupinfo_exitgroup_bt);
		addgroup_bt = (Button) this
				.findViewById(R.id.activity_groupinfo_addgroup_bt);
		transfergroup_bt = (Button) this
				.findViewById(R.id.activity_groupinfo_transfergroup_bt);
		dismissgroup_bt = (Button) this
				.findViewById(R.id.activity_groupinfo_dismissgroup_bt);

		groupImg_iv = (ImageView) findViewById(R.id.activity_groupinfo_groupImg_iv);
		back_tv = (TextView) findViewById(R.id.activity_group_info_back_tv);
		location_bt = (Button) findViewById(R.id.activity_group_location_bt);

		manageGroup_rl = (RelativeLayout) findViewById(R.id.activity_groupinfo_manageGroup_rl);
		manageGroup_rl.setOnClickListener(this);

		activity_group_number_rl = (RelativeLayout) findViewById(R.id.activity_group_number_rl);
		activity_group_number_rl.setOnClickListener(this);

		exitgroup_bt.setOnClickListener(this);
		addgroup_bt.setOnClickListener(this);
		transfergroup_bt.setOnClickListener(this);
		dismissgroup_bt.setOnClickListener(this);
		back_tv.setOnClickListener(this);
		location_bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_groupinfo_manageGroup_rl:
			Intent i = new Intent(this, EditGroupInfoActivity.class);
			i.putExtra("groupinfo", g);
			this.startActivity(i);
			break;
		case R.id.activity_groupinfo_exitgroup_bt:
			exitGroupTask egt = new exitGroupTask();
			egt.execute();
			break;
		case R.id.activity_groupinfo_addgroup_bt:
			ApplyAddGroupTask agtt = new ApplyAddGroupTask();
			agtt.execute(new String[] { null, String.valueOf(g.getId()), "1" });
			break;
		case R.id.activity_group_number_rl:
			Intent intent = new Intent();
			intent.setClass(this, GroupMemberListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("groupInfo", g);
			intent.putExtras(bundle);
			this.startActivity(intent);
			break;
		case R.id.activity_groupinfo_transfergroup_bt:
			adapter = new TransferGroupMemberAdapter(this, ulist);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GroupInfoActivity.this);
			LayoutInflater mInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(
					R.layout.activity_group_info_listview, null);
			final ListView meberber_lv = (ListView) view
					.findViewById(R.id.activity_group_info_listview_lv);
			meberber_lv.setAdapter(adapter);
			meberber_lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					new AlertDialog.Builder(GroupInfoActivity.this)
							.setTitle("你确定要转让该群吗！！！")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											TransferGroupTask tfgt = new TransferGroupTask();
											tfgt.execute(new Integer[] { ulist
													.get(position).getId() });
										}
									}).setNegativeButton("取消", null).show();
				}

			});
			ulist.removeAll(ulist);
			findAllGroupMemberIDTask fagt = new findAllGroupMemberIDTask();
			fagt.execute();
			builder.setTitle("请选择转让的群成员！！！");
			builder.setView(meberber_lv);
			builder.setNegativeButton("取消", null);
			builder.create().show();
			break;
		case R.id.activity_groupinfo_dismissgroup_bt:
			new AlertDialog.Builder(this)
					.setTitle("你确定要解散该群吗！！！")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									DismissGroupTask dgt = new DismissGroupTask();
									dgt.execute();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.activity_group_info_back_tv:
			this.finish();
			break;
		case R.id.activity_group_location_bt:
			intent = new Intent(this, MainActivity.class);
			intent.putExtra("fragment", "group_location");
			intent.putExtra("groupId", g.getId());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
			break;
		}

	}

	// 转让群给其他群成员
	public class TransferGroupTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindMemberServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("creatorId", String
						.valueOf(arg0[0])));
				params.add(new BasicNameValuePair("groupId", String.valueOf(g
						.getId())));
				params.add(new BasicNameValuePair("action", "TransferGroup"));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result1:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "转让群成功！！!",
						Toast.LENGTH_SHORT).show();
				GroupInfoActivity.this.finish();
				// Intent i = new
				// Intent(GroupInfoActivity.this.getApplicationContext(),MainActivity.class);
			} else if ("0".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "转让群失败！！!",
						Toast.LENGTH_SHORT).show();
				setGroupInfo();
			} else if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你还未登陆！！!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 查找用户是否是该群成员
	public class IsGroupMemberInfoTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindMemberServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("groupId", String.valueOf(g
						.getId())));
				params.add(new BasicNameValuePair("action",
						"getAllMemberByGroupId"));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result1:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("1".equalsIgnoreCase(result)) {
				exitgroup_bt.setVisibility(0);
				addgroup_bt.setVisibility(8);
				dismissgroup_bt.setVisibility(8);
				transfergroup_bt.setVisibility(8);
				setGroupInfo();
			} else if ("0".equalsIgnoreCase(result)) {
				exitgroup_bt.setVisibility(8);
				addgroup_bt.setVisibility(0);
				dismissgroup_bt.setVisibility(8);
				transfergroup_bt.setVisibility(8);
				setGroupInfo();
			} else if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你还未登陆！！!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 查找该群的人数
	public class FindMemberCountInfoTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindMemberServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("groupId", String.valueOf(g
						.getId())));
				params.add(new BasicNameValuePair("action",
						"getAllMemberCountByGroupId"));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result1:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你还未登陆！！!",
						Toast.LENGTH_SHORT).show();
			} else {
				groupmember_tv.setText(result + "人");
			}
		}
	}

	// 申请加入该群
	public class ApplyAddGroupTask extends AsyncTask<String, Integer, String> {
		String groupId = null;

		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/ApplyAddGroupServlet";
			String result = null;
			groupId = arg0[1];
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("content", arg0[0]));
				params.add(new BasicNameValuePair("groupId", arg0[1]));
				params.add(new BasicNameValuePair("what", arg0[2]));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result1:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你还未登陆！！!",
						Toast.LENGTH_SHORT).show();
			} else if ("1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "申请信息发送成功",
						Toast.LENGTH_SHORT).show();
			} else if ("0".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "申请信息发送失败！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("2".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "申请信息已经发送，请不要重复发送！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("3".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你已经是该群成员！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("4".equalsIgnoreCase(result)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getApplicationContext());
				final EditText desc = new EditText(getApplicationContext());
				builder.setTitle("请输入验证信息：");
				builder.setView(desc);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Toast.makeText(getApplicationContext(),
										desc.getText().toString(),
										Toast.LENGTH_LONG).show();
								ApplyAddGroupTask agtt = new ApplyAddGroupTask();
								agtt.execute(new String[] {
										desc.getText().toString(), groupId,
										null });
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
				builder.create().show();
			}
		}
	}

	// 解散该群
	public class DismissGroupTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/DismissGroupServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userId", String.valueOf(ub
						.getId())));
				params.add(new BasicNameValuePair("groupId", String.valueOf(g
						.getId())));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result1:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("1".equalsIgnoreCase(result)) {
				Intent intent = new Intent(GroupInfoActivity.this,
						MainActivity.class);
				intent.putExtra("fragment", "contact");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				GroupInfoActivity.this.startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	// 退出该群
	public class exitGroupTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/ExitGroupServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("phone", ub.getName()));
				params.add(new BasicNameValuePair("psw", ub.getPassword()));
				params.add(new BasicNameValuePair("userId", String.valueOf(ub
						.getId())));
				params.add(new BasicNameValuePair("groupId", String.valueOf(g
						.getId())));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result1:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_SHORT).show();
			}

		}
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
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
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
				List<Integer> ilist = gson.fromJson(result,
						new TypeToken<List<Integer>>() {
						}.getType());
				for (int i = 0; i < ilist.size(); i++) {
					findAllGroupMemberInfoTask fmt = new findAllGroupMemberInfoTask();
					fmt.execute(new String[] { String.valueOf(ilist.get(i)) });
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
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
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
				ulist.add(user);
				Comparator<UserBean> comparator = new Comparator<UserBean>() {
					public int compare(UserBean s1, UserBean s2) {
						return s2.getStatus() - s1.getStatus();
					}
				};
				Collections.sort(ulist, comparator);
				adapter.notifyDataSetChanged();
			}
		}
	}

	// 查找群资料的异步任务
	public class GetGroupInfoByGroupIdTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/UpdateGroupInfoServlet";
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("groupId", g.getId() + ""));
				params.add(new BasicNameValuePair("action",
						"getGroupInfoByGroupId"));
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

			if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你还未登陆！！!",
						Toast.LENGTH_SHORT).show();
			} else if ("1".equalsIgnoreCase(result)) {
				Gson gson = new Gson();
				GroupBean gb = gson.fromJson(result,
						new TypeToken<GroupBean>() {
						}.getType());
				if (null != gb) {
					setGroupInfo();
					g = gb;
				}

			} else if ("0".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "查找群信息失败！！！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
