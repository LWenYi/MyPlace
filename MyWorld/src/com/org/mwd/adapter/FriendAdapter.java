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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.mwd.activities.FriendInfoActivity;
import com.org.mwd.activities.MainActivity;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.FriendBean;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

public class FriendAdapter extends BaseAdapter {
	Context context;
	private List<FriendBean> 	friendList;
	private LayoutInflater mInflater;
	SaveBean save; 
	int type;

	public FriendAdapter(Context context,List<FriendBean> list) {
		this.context =context;
		friendList = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		save = (SaveBean) context.getApplicationContext();
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =convertView; 
		ViewHolder holder; 
		if(view ==null) 
		{
		holder = new ViewHolder();
		view=mInflater.inflate(R.layout.item_friend, parent, false);
		holder.item_rl = (RelativeLayout) view.findViewById(R.id.fragment_friend_rl);
		holder.nick_tv = (TextView) view.findViewById(R.id.fragment_friend__name_tv);
		holder.image_iv = (ImageView)view.findViewById(R.id.fragment_friend_img_iv);
		holder.location_iv = (ImageView) view.findViewById(R.id.fragment_friend_location_iv);
		holder.desc_tv = (TextView) view.findViewById(R.id.fragment_friend__desc_tv);
		view.setTag(holder);
		}else 
		{ 
			holder=(ViewHolder)view.getTag(); 
		}  
		final FriendBean friend = friendList.get(position);
		if (friend == null) {
			return null;
		}
		Log.i("user", friend.getFriendName());
		holder.nick_tv.setText(friend.getFriendName());
		if(friend.getStatus() == 0){
			holder.item_rl.setBackgroundResource(R.color.offline);
			String desc = friend.getExtra1();
			if(desc != null && !"".equalsIgnoreCase(desc)){
				holder.desc_tv.setText("[离线]"+desc);
			}else{
				holder.desc_tv.setText("[离线] 这人很懒，什么也没有！！");
			}
		}else{
			holder.item_rl.setBackgroundResource(R.drawable.common_item_rl_bg);
			String desc = friend.getExtra1();
			if(desc != null && !"".equalsIgnoreCase(desc)){
				holder.desc_tv.setText("[在线]"+friend.getExtra1());
			}else{
				holder.desc_tv.setText("[在线] 这人很懒，什么也没有！！");
			}
			
		}
		if(null == friend.getExtra2()){
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_userphoto);
			  if(friend.getStatus() == 1){
				  holder.image_iv.setImageBitmap(bitmap);
			  }else{
				  holder.image_iv.setImageBitmap(MyUtil.toGrayscale(bitmap));
			  }
		}else{
			  byte[] imgByte = MyUtil.hex2byte(friend.getExtra2());
			  Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, null);
			  if(friend.getStatus() == 1){
				  holder.image_iv.setImageBitmap(bitmap);
			  }else{
				  holder.image_iv.setImageBitmap(MyUtil.toGrayscale(bitmap));
			  }
			  
		}
		holder.location_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,MainActivity.class);
				intent.putExtra("fragment", "friend_location");
				intent.putExtra("friendId", friend.getFriendId());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		holder.image_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				findFriendInfoTask ffit = new findFriendInfoTask();
				ffit.execute(new String[] {String.valueOf(friend.getFriendId())});
				
			}
			
		});
		return view;
	}

		


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friendList.size();	
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
	
	 static class ViewHolder { 

         TextView nick_tv,desc_tv; 
         ImageView image_iv,location_iv; 
         RelativeLayout item_rl;
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
					intent.setClass(context,
							FriendInfoActivity.class);
					System.out.println("ub:" + ub.getName());
					Bundle bundle = new Bundle();
					bundle.putSerializable("friendInfo", ub);
					intent.putExtras(bundle);
					intent.putExtra("isFriend", "1");
					intent.putExtra("from", "1");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} else {// 没找到
					Toast.makeText(context, "信息不存在！",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
}
