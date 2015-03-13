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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindFriendresultAdapter extends BaseAdapter {
	
	Context context;
	private List<UserBean> 	userList = new ArrayList<UserBean>();
	private LayoutInflater mInflater;
	int type;
	SaveBean save;
	public FindFriendresultAdapter(Context context,List<UserBean> list) {
		this.context =context;
		userList = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		save = (SaveBean) context.getApplicationContext();
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view =convertView; 
		if(userList == null || userList.isEmpty()){
			TextView tv =new TextView(context);
			tv.setText("暂无好友搜索结果！！！！");
			return tv;
		}
		ViewHolder holder; 
		if(view ==null) 
		{
		 holder = new ViewHolder();
		view=mInflater.inflate(R.layout.item_find_friend_result, parent, false);
		holder.nick_tv = (TextView) view.findViewById(R.id.activity_add_find_item_name_tv);
		holder.image_iv = (ImageView)view.findViewById(R.id.activity_add_find_item_icon_iv);
		holder.addfriend_btn = (Button) view.findViewById(R.id.item_find_friend_addfriend_btn);
		view.setTag(holder);
		}else 
		{ 
			holder=(ViewHolder)view.getTag(); 
		}  
		final UserBean user = userList.get(position);
		if (user == null ) {
			return convertView;
		}
		holder.nick_tv.setText(user.getNick());
		if(null == user.getImage()){
			holder.image_iv.setImageResource(R.drawable.default_userphoto);
		}else{
			  byte[] imgByte = MyUtil.hex2byte(user.getImage());
			  Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, null);
			  holder.image_iv.setImageBitmap(bitmap);
		}
		holder.addfriend_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if("1".equalsIgnoreCase(user.getExtra2())){
					Toast.makeText(context, "你们已经是好友关系！！！！",
						     Toast.LENGTH_LONG).show();
				}else if(user.getId() == save.getUb().getId()){
					Toast.makeText(context, "自己不能加自己为好友！！！！",
						     Toast.LENGTH_LONG).show();
				}else{
					 ApplyAddFriendTask aft = new ApplyAddFriendTask();
					 aft.execute(new String[]{String.valueOf(user.getId()),null,"isApplyAddFriend"});				}
				
			}
			
		});
		return view;
	}
	@Override
	public int getCount() {
		return userList.size();	
	}
	@Override
	public Object getItem(int position) {
		return position;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	static class ViewHolder { 
        TextView nick_tv; 
        ImageView image_iv;
        Button addfriend_btn;
    } 
	//添加好友的异步任务
	public class ApplyAddFriendTask extends AsyncTask<String, Integer, String> {
		String friendId=null;
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			 String PATH = "http://"+MyUtil.getIP()+":8080/MyWorldService/ApplyAddFriendServlet";  
			 System.out.println("path:"+PATH);
			 String result = "failure";
			 friendId =arg0[0];
			 try{
				 HttpClient httpclient = new DefaultHttpClient(); 
			     HttpPost post = new HttpPost(PATH);  
			     List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();  
			     if (null != save.getCookie()) {
						post.setHeader("Cookie", "sessionId="
								+ save.getCookie().getValue());
				 } 
			     params.add(new BasicNameValuePair("friendId",arg0[0])); 
			     params.add(new BasicNameValuePair("content", arg0[1]));
			     params.add(new BasicNameValuePair("action",arg0[2])); 
			     HttpEntity formEntity = new UrlEncodedFormEntity(params,HTTP.UTF_8);  
			     post.setEntity(formEntity); 
			     HttpParams hparams = new BasicHttpParams(); 
	             HttpConnectionParams.setConnectionTimeout(hparams, 5000); //设置连接超时
	             HttpConnectionParams.setSoTimeout(hparams, 10000); //设置请求超时
			     HttpResponse response = httpclient.execute(post);  
			     if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
			         InputStream is = response.getEntity().getContent();  
			         result = MyUtil.inStreamToString(is);
			         System.out.println("result:"+result);
			     }else{
			    	 System.out.println("请求服务器失败！！");
			     }  
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			if ("4".equalsIgnoreCase(result)) {
				Toast.makeText(context, "申请添加好友成功，等待对方验证！！！",
					     Toast.LENGTH_SHORT).show();
			}else if ("5".equalsIgnoreCase(result))  {
				Toast.makeText(context, "对不起,申请添加好友失败！！！",
					     Toast.LENGTH_SHORT).show();
			}else if ("-1".equalsIgnoreCase(result))  {
				Toast.makeText(context, "对不起,请登录后再试！！！",
					     Toast.LENGTH_SHORT).show();
			}else if ("2".equalsIgnoreCase(result))  {
				Toast.makeText(context, "您已经发送申请，请不要重复发送！",
					     Toast.LENGTH_SHORT).show();
			}else if("1".equalsIgnoreCase(result)){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);   
				 final EditText desc = new EditText(context);
				     builder.setTitle("请输入验证信息：");  
				     builder.setView(desc);  
				     builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				         public void onClick(DialogInterface dialog, int whichButton) {  
				        	 ApplyAddFriendTask aft = new ApplyAddFriendTask();
							 aft.execute(new String[]{friendId,desc.getText().toString(),"ApplyAddFriend"});
				         }  
				     });  
				     builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
				         public void onClick(DialogInterface dialog, int whichButton) { 
				         }  
				     });  
				   builder.create().show(); 
			}else if("3".equalsIgnoreCase(result)){
				Toast.makeText(context, "你们已经是好友关系，请刷新数据！！！",
					     Toast.LENGTH_SHORT).show();
			}
		}
	}
}