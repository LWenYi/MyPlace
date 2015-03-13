package com.org.mwd.adapter;

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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.mwd.activities.FriendInfoActivity;
import com.org.mwd.activities.MainActivity;

import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.ApplyMessageBean;

import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

public class ApplyMessageaAdapter extends BaseAdapter implements
		OnClickListener {

	Context context;
	private List<ApplyMessageBean> messageList;
	private LayoutInflater mInflater;
	int type;
	SaveBean save;
	PopupWindow popuWindow;
	Button accept_btn, refuse_btn;


	public ApplyMessageaAdapter(Context context, List<ApplyMessageBean> list) {
		this.context = context;
		messageList = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		save = (SaveBean) context.getApplicationContext();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		final ViewHolder holder;
		final ApplyMessageBean m = messageList.get(position);
		if (null == m) {
			return null;
		}
		if (null == view) {
			holder = new ViewHolder();
			view = mInflater
					.inflate(R.layout.item_apply_message, parent, false);
			holder.choose_btn = (Button) view
					.findViewById(R.id.fragment_message_apply_item_choose_btn);
			holder.image_iv = (ImageView) view
					.findViewById(R.id.fragment_message_apply_item_image_iv);
			holder.tip_tv = (TextView) view
					.findViewById(R.id.fragment_message_apply_item_tip_tv);
			holder.username_tv = (TextView) view
					.findViewById(R.id.fragment_message_apply_item_username_tv);
			holder.groupname_tv = (TextView) view
					.findViewById(R.id.fragment_message_apply_item_groupname_tv);
			holder.content_tv = (TextView) view
					.findViewById(R.id.fragment_message_apply_item_content_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		// 填充消息的发送者
		holder.username_tv.setText(m.getUserNick());
		if (0 != m.getGroupId()) {
			holder.groupname_tv.setText(m.getGroupName());
		} else {
			holder.tip_tv.setText("申请加你为好友！！！");
			holder.groupname_tv.setText("");
		}
		holder.content_tv.setText("附加消息：" + m.getContent());
		// 填充发送者的图片
		if (null == m.getUserImage()) {
			holder.image_iv.setImageResource(R.drawable.default_userphoto);
		} else {
			byte[] imgByte = MyUtil.hex2byte(m.getUserImage());
			Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
					imgByte.length, null);
			holder.image_iv.setImageBitmap(bitmap);
			holder.image_iv.setTag(position);
		}
		holder.image_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				findFriendInfoTask ffit = new findFriendInfoTask();
				ffit.execute(new String[] { String.valueOf(m.getUserId()) });
			}

		});
		holder.choose_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (popuWindow != null) {
					popuWindow.dismiss();
				}
				View poView = mInflater.inflate(
						R.layout.fragment_message_popuwindow, null);
				popuWindow = new PopupWindow(poView, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				// 这里设置显示PopuWindow之后在外面点击是否有效。如果为false的话，那么点击PopuWindow外面并不会关闭PopuWindow。
				popuWindow.setOutsideTouchable(true);// 不能在没有焦点的时候使用
				popuWindow.setFocusable(true);
				refuse_btn = (Button) poView
						.findViewById(R.id.fragment_message_popuwindow_refuse_btn);
				accept_btn = (Button) poView
						.findViewById(R.id.fragment_message_popuwindow_accept_btn);
				refuse_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						RefuseApplyTask rat = new RefuseApplyTask();
						rat.execute(new Integer[] {
								messageList.get(position).getMessageId(),
								position });
						popuWindow.dismiss();

					}
				});
				accept_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						AcceptApplyTask aat = new AcceptApplyTask();
						aat.execute(new Integer[] {
								messageList.get(position).getMessageId(),
								position });
						popuWindow.dismiss();

					}
				});
				ColorDrawable cd = new ColorDrawable(-0000);
				popuWindow.setBackgroundDrawable(cd);
				popuWindow.showAsDropDown(holder.choose_btn);
			}

		});

		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public void onClick(View v) {
		System.out.println("view:" + v.getId());
		switch (v.getId()) {
		case R.id.activity_add_find_item_location_iv:
			break;
		}

	}

	static class ViewHolder {
		TextView username_tv, groupname_tv, content_tv, tip_tv;
		ImageView image_iv;
		Button choose_btn;
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
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("id", friendId));
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
				intent.setClass(context, FriendInfoActivity.class);
				System.out.println("ub:" + ub.getName());
				Bundle bundle = new Bundle();
				bundle.putSerializable("friendInfo", ub);
				intent.putExtras(bundle);
				intent.putExtra("isFriend", "0");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else {// 没找到
				Toast.makeText(context, "信息不存在！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 拒绝申请
	public class RefuseApplyTask extends AsyncTask<Integer, Integer, String> {
		int position;

		@Override
		protected String doInBackground(Integer... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/RefuseApplyServlet";
			position = arg0[1];
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("messageId", String
						.valueOf(arg0[0])));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("0".equalsIgnoreCase(result)) {
				Toast.makeText(context.getApplicationContext(), "申请请求处理失败！！",
						Toast.LENGTH_SHORT).show();
			} else if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(context.getApplicationContext(), "用户还未登陆",
						Toast.LENGTH_SHORT).show();
			} else if ("1".equalsIgnoreCase(result)) {
				Toast.makeText(context.getApplicationContext(), "申请请求处理成功！！！",
						Toast.LENGTH_SHORT).show();
				messageList.remove(position);
				Intent i = new Intent(context,MainActivity.class);
				i.putExtra("fragment", "message");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}

		}
	}

	// 接受入群申请
	public class AcceptApplyTask extends AsyncTask<Integer, Integer, String> {
		int position;

		@Override
		protected String doInBackground(Integer... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/AcceptApplyServlet";
			position = arg0[1];
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("messageId", String
						.valueOf(arg0[0])));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "请求服务器超时！！！";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("1".equalsIgnoreCase(result)) {
				Toast.makeText(context.getApplicationContext(), "添加群成员成功！！",
						Toast.LENGTH_SHORT).show();
				messageList.remove(position);
				Intent i = new Intent(context,MainActivity.class);
				i.putExtra("fragment", "message");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			} else if ("3".equalsIgnoreCase(result)) {
				Toast.makeText(context.getApplicationContext(), "添加好友成功！！！",
						Toast.LENGTH_SHORT).show();
				messageList.remove(position);
				Intent i = new Intent(context,MainActivity.class);
				i.putExtra("fragment", "message");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
				context.getApplicationContext().startActivity(i);
			} else if ("4".equalsIgnoreCase(result)) {
				Toast.makeText(context.getApplicationContext(), "添加好友失败！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("5".equalsIgnoreCase(result)) {
				Toast.makeText(context.getApplicationContext(), "你们已经是好友关系！！！",
						Toast.LENGTH_LONG).show();
			}

		}
	}
}
