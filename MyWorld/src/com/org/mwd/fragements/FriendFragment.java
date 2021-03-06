package com.org.mwd.fragements;

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
import com.org.mwd.activities.FriendInfoActivity;
import com.org.mwd.adapter.FriendAdapter;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.FriendBean;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FriendFragment extends Fragment {
	private View view = null;
	private ListView friend_lv;
	private SaveBean save;
	private List<FriendBean> friendList = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_friend, null);
		return view;
	}

	public void onStart() {
		super.onStart();
		friend_lv = (ListView) view.findViewById(R.id.fragment_friend_list_lv);
		save = (SaveBean) this.getActivity().getApplicationContext();
		friendList = save.getFriendList();
		if (friendList == null) {
			ShowFriendListTask sfl = new ShowFriendListTask();
			sfl.execute(new String[] { String.valueOf(save.getUb().getId()),
					save.getUb().getName(), save.getUb().getPassword() });
		} else {

			FriendAdapter adapter = new FriendAdapter(
					FriendFragment.this.getActivity(), friendList);
			friend_lv.setAdapter(adapter);
			friend_lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					findFriendInfoTask ffit = new findFriendInfoTask();
					ffit.execute(new String[] { String.valueOf(friendList.get(
							position).getFriendId()) });
				}
			});
		}
	}
	// 根据显示好友列表
	public class ShowFriendListTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindAllFriendByIdServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userId", arg0[0]));
				params.add(new BasicNameValuePair("phone", arg0[1]));
				params.add(new BasicNameValuePair("psw", arg0[2]));
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
			if (!"failure".equalsIgnoreCase(result)) { // 查找到数据
				Gson gson = new Gson();
				final List<FriendBean> list = gson.fromJson(result,
						new TypeToken<List<FriendBean>>() {
						}.getType());
				save.setFriendList(list);
				FriendAdapter adapter = new FriendAdapter(
						FriendFragment.this.getActivity(), list);
				friend_lv.setAdapter(adapter);
				// user_lv.setOnCreateContextMenuListener(AddActivity.this);
				// //注册上下文菜单需要传LocalActivity 的上下文
				friend_lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long arg3) {
						// local_image_gv.showContextMenu(); //用这个方法,itemInfo为空
						// user_lv.showContextMenuForChild(view);
						findFriendInfoTask ffit = new findFriendInfoTask();
						ffit.execute(new String[] { String.valueOf(list.get(
								position).getFriendId()) });
					}
				});
			} else {// 没找到
				Toast.makeText(
						FriendFragment.this.getActivity()
								.getApplicationContext(), "信息不存在！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	// 根据显示好友Id查找好友信息并显示
	public class findFriendInfoTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindUserByIdServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userId", arg0[0]));
				// params.add(new BasicNameValuePair("phone", arg0[1]));
				// params.add(new BasicNameValuePair("psw", arg0[2]));
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
			if (!"failure".equalsIgnoreCase(result)) { // 查找到数据
				Gson gson = new Gson();
				UserBean friend = gson.fromJson(result,
						new TypeToken<UserBean>() {
						}.getType());
				Intent intent = new Intent();
				intent.setClass(FriendFragment.this.getActivity(),
						FriendInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("friend", friend);
				intent.putExtras(bundle);
				FriendFragment.this.getActivity().startActivity(intent);

			} else {// 没找到
				Toast.makeText(
						FriendFragment.this.getActivity()
								.getApplicationContext(), "信息不存在！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
