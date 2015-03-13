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
import com.org.mwd.adapter.ApplyMessageaAdapter;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.ApplyMessageBean;
import com.org.mwd.vo.SaveBean;
import com.org.myworld.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class MessageFragment extends Fragment {
	private View view;
	private ListView list_lv;
	private SaveBean save;
	private List<ApplyMessageBean> messageList;
	private ApplyMessageaAdapter adapter;
	//获取申请消息的线程
    private GetApplyMessageTask gam;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_message, container, false);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		save = (SaveBean) MessageFragment.this.getActivity()
				.getApplicationContext();
		messageList = new ArrayList<ApplyMessageBean>();		
		list_lv = (ListView) view.findViewById(R.id.fragment_message__list_lv);
		GetApplyMessageIdTask gamt = new GetApplyMessageIdTask();
		gamt.execute();
	}

	// 获取未处理的申请入群信息ID列表
	public class GetApplyMessageIdTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/GetApplyMessageServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
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
			if ("0".equalsIgnoreCase(result)) {
			} else if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(
						MessageFragment.this.getActivity()
								.getApplicationContext(), "未登录！！",
						Toast.LENGTH_SHORT).show();
			} else {
				Gson gson = new Gson();
				List<Integer> messageIdList = gson.fromJson(result,
						new TypeToken<List<Integer>>() {
						}.getType());
				for (int i = 0; i < messageIdList.size(); i++) {
				    gam = new GetApplyMessageTask();
					gam.execute(new Integer[] { messageIdList.get(i) });
				}
			}

		}
	}
	// 获取未处理的申请入群信息列表
	public class GetApplyMessageTask extends
			AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/GetApplyMessageServlet";
			String result = "0";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("what", "2"));
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
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			if ("0".equalsIgnoreCase(result)) {
				//无申请消息
			} else if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(
						MessageFragment.this.getActivity()
								.getApplicationContext(), result,
						Toast.LENGTH_SHORT).show();
			} else {
				Gson gson = new Gson();
				ApplyMessageBean amb = gson.fromJson(result,
						new TypeToken<ApplyMessageBean>() {
						}.getType());
				messageList.add(amb);
				if (messageList.size() == 1) {
					adapter = new ApplyMessageaAdapter(MessageFragment.this
							.getActivity().getApplicationContext(), messageList);
					list_lv.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
			}

		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(null !=gam){
			gam.cancel(true);
		}
	}
	
	
	
}
