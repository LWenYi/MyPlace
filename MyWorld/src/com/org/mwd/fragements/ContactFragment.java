package com.org.mwd.fragements;

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
import com.org.mwd.activities.AddActivity;
import com.org.mwd.activities.FriendTalkActivity;
import com.org.mwd.activities.GroupTalkActivity;
import com.org.mwd.activities.LoginActivity;
import com.org.mwd.adapter.FriendAdapter;
import com.org.mwd.adapter.GroupAdapter;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.FriendBean;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;
import com.org.myworld.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactFragment extends Fragment implements OnClickListener {
	private TextView fragment_contact_add,myfriend_tv,mygroup_tv;
	private RelativeLayout friend_list_rl, group_list_rl;
	private LinearLayout groupmember_ll, friendlist_ll;
	private ListView contact_list_lv, groupmember_lv;
	private View view = null,fragment_contact_animline_v;
	SaveBean save = null;
	private Animation anim_togrouplist, anim_tofriendlist;
	//获取好友列表线程
	private ShowFriendListTask sfl;
	//获取群列表
	private ShowGroupListTask sgl;
	private List<FriendBean> flist =null;
	private List<GroupBean> glist =null;
	private Comparator<FriendBean> comparator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_contact, container, false);
		return view;

	}

	@Override
	public void onStart() {
		super.onStart();
		save = (SaveBean) this.getActivity().getApplicationContext();
		comparator = new Comparator<FriendBean>() {
			public int compare(FriendBean s1, FriendBean s2) {
				return s2.getStatus() - s1.getStatus();
			}
		};
		init(); //初始化组件
		if (null == save.getCookie()){ //用户未登录
			Intent intent = new Intent();
			intent.setClass(ContactFragment.this.getActivity(),
					LoginActivity.class);
			ContactFragment.this.getActivity().startActivity(intent);
		}
		Bundle bundle = getArguments();
		if ( null != bundle) {  //用户之前离开时的界面，默认是群界面
			String what = bundle.getString("what");
			if (null != what) {
				friend_list_rl.performClick();
			} else {
				group_list_rl.performClick();
			}
		} else {
			group_list_rl.performClick();
		}

	}
	private void init() {
		contact_list_lv = (ListView) view
				.findViewById(R.id.fragment_contact_list_lv);
		groupmember_lv = (ListView) view
				.findViewById(R.id.fragment_contact_groupmember_lv);
		friend_list_rl = (RelativeLayout) view
				.findViewById(R.id.fragment_contact_friend_rl);
		group_list_rl = (RelativeLayout) view
				.findViewById(R.id.fragment_contact_group_rl);
		friendlist_ll = (LinearLayout) view
				.findViewById(R.id.fragment_contact_friendlist_ll);
		groupmember_ll = (LinearLayout) view
				.findViewById(R.id.fragment_contact_groupmember_ll);

		fragment_contact_animline_v = (View) view
				.findViewById(R.id.fragment_contact_animline_v);

		fragment_contact_add = (TextView) view
				.findViewById(R.id.fragment_contact_add);
		myfriend_tv = (TextView) view.findViewById(R.id.fragment_contact_myfriend_tv);
		mygroup_tv = (TextView) view.findViewById(R.id.fragment_contact_mygroup_tv);
		
		fragment_contact_add.setOnClickListener(this);
		friend_list_rl.setOnClickListener(this);
		group_list_rl.setOnClickListener(this);
		contact_list_lv
		.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View view, int position, long arg3) {
				Intent intent = new Intent();
				intent.setClass(
						ContactFragment.this.getActivity(),
						FriendTalkActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("friendInfo",
						flist.get(position));
				intent.putExtras(bundle);
				ContactFragment.this.getActivity()
						.startActivity(intent);
			}
		});
		groupmember_lv
		.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View v, int position, long arg3) {
				Intent intent = new Intent();
				intent.setClass(
						ContactFragment.this.getActivity(),
						GroupTalkActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("groupInfo",
						glist.get(position));
				intent.putExtras(bundle);
				intent.putExtra("position",
						String.valueOf(position));
				ContactFragment.this.getActivity()
						.startActivity(intent);

			}
		});

	}
	@Override
	public void onPause() {
		super.onPause();
	}

	// 点击事件
	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.fragment_contact_add:
			Intent i = new Intent(this.getActivity(), AddActivity.class);
			i.putExtra("from", "contact");
			this.startActivity(i);
			break;
		case R.id.fragment_contact_friend_rl:
			anim_tofriendlist = AnimationUtils.loadAnimation(this.getActivity()
					.getApplicationContext(),
					R.anim.fragment_comtact_tofriendlist);
			fragment_contact_animline_v.startAnimation(anim_tofriendlist);
			
			friend_list_rl.setClickable(false);
			group_list_rl.setClickable(true);
			myfriend_tv.setTextColor(R.color.fragment_contact_txtcolor_checked);
			mygroup_tv.setTextColor(R.color.fragment_contact_txtcolor_unchecked);

			friendlist_ll.setVisibility(0);
			groupmember_ll.setVisibility(8);
			long startTime = System.nanoTime(); 
			if(null !=save.getFriendList() && save.getFriendList().size()>0){
				flist = save.getFriendList();
				FriendAdapter adapter = new FriendAdapter(
						ContactFragment.this.getActivity()
								.getApplicationContext(), flist);
				Comparator<FriendBean> comparator = new Comparator<FriendBean>() {
					public int compare(FriendBean s1, FriendBean s2) {
						return s2.getStatus() - s1.getStatus();
					}
				};
				Collections.sort(flist, comparator);
				contact_list_lv.setAdapter(adapter);
				
			}
			long consumingTime = System.nanoTime() - startTime; //消耗時間
		    System.out.println(consumingTime);
		    System.out.println(consumingTime/1000000+"秒");
			sfl = new ShowFriendListTask();
			sfl.execute(new String[] { String.valueOf(save.getUb().getId()),
					save.getUb().getName(), save.getUb().getPassword() });
			break;
		case R.id.fragment_contact_group_rl:
			friend_list_rl.setClickable(true);
			group_list_rl.setClickable(false);
			mygroup_tv.setTextColor(R.color.fragment_contact_txtcolor_checked);
			myfriend_tv.setTextColor(R.color.fragment_contact_txtcolor_unchecked);
			
			anim_togrouplist = AnimationUtils.loadAnimation(this.getActivity()
					.getApplicationContext(),
					R.anim.fragment_comtact_togrouplist);
			fragment_contact_animline_v.startAnimation(anim_togrouplist);

			friendlist_ll.setVisibility(8);
			groupmember_ll.setVisibility(0);
			
			if(null != save.getGroupList() && save.getGroupList().size() >0){
				glist =save.getGroupList();
				GroupAdapter adapter = new GroupAdapter(ContactFragment.this
						.getActivity().getApplicationContext(), glist);
				groupmember_lv.setAdapter(adapter);
			}

			sgl = new ShowGroupListTask();
			sgl.execute(new String[] { String.valueOf(save.getUb().getId()),
					save.getUb().getName(), save.getUb().getPassword() });
			break;
		}
	}

	// 显示好友列表
	public class ShowFriendListTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindAllFriendByIdServlet";
			System.out.println(PATH);
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
				 flist = gson.fromJson(result,
						new TypeToken<List<FriendBean>>() {
						}.getType());
				if (flist != null && flist.size() > 0) {
					save.setFriendList(flist);
					FriendAdapter adapter = new FriendAdapter(
							ContactFragment.this.getActivity()
									.getApplicationContext(), flist);
					
					Collections.sort(flist, comparator);
					contact_list_lv.setAdapter(adapter);
				} else {
					Toast.makeText(
							ContactFragment.this.getActivity()
									.getApplicationContext(), "你还没好友！！！！",
							Toast.LENGTH_SHORT).show();
				}
			} 
		}
	}

	// 显示群列表
	public class ShowGroupListTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/FindAllGroupByIdServlet";
			System.out.println(PATH);
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userId", arg0[0]));
				params.add(new BasicNameValuePair("phone", arg0[1]));
				params.add(new BasicNameValuePair("psw", arg0[2]));
				params.add(new BasicNameValuePair("what", "allgroup"));
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
				 glist = gson.fromJson(result,
						new TypeToken<List<GroupBean>>() {
						}.getType());
				GroupAdapter adapter = null;
				if (null != glist && glist.size()>0 ) {
					save.setGroupList(glist);
					adapter = new GroupAdapter(ContactFragment.this
							.getActivity().getApplicationContext(), glist);
					groupmember_lv.setAdapter(adapter);
				}else{
				}
			} else {// 没找到
				Toast.makeText(
						ContactFragment.this.getActivity()
								.getApplicationContext(), "信息不存在！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(null != sfl){
			sfl.cancel(true);
		}
		if(null != sgl){
			sgl.cancel(true);
		}
	}
	
	

}