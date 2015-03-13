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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.org.mwd.activities.GroupInfoActivity;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;
import com.org.myworld.R;

public class FindGroupResultAdapter extends BaseAdapter implements OnClickListener{
	Context context;
	private List<GroupBean> 	groupList;
	private LayoutInflater mInflater;
	int type;
	int pos;
	SaveBean save;

	public FindGroupResultAdapter(Context context,List<GroupBean> list) {
		this.context =context;
		groupList = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		save = (SaveBean) context.getApplicationContext();
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view =convertView; 
		ViewHolder holder; 
		if(view ==null) 
		{
		 holder = new ViewHolder();
		view=mInflater.inflate(R.layout.item_find_group_result, parent, false);
		holder.groupNmae_tv = (TextView) view.findViewById(R.id.activity_add_find_item_name_tv);
		holder.groupImg_iv = (ImageView)view.findViewById(R.id.activity_add_find_item_icon_iv);
		holder.addgroup_btn = (Button) view.findViewById(R.id.item_find_group_addgroup_btn);
		view.setTag(holder);
		}else 
		{ 
			holder=(ViewHolder)view.getTag(); 
		}  
		final GroupBean g = groupList.get(position);
		if (g == null) {
			return null;
		}
		holder.groupNmae_tv.setText(g.getGroupName());
		holder.groupImg_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,GroupInfoActivity.class);
				Bundle bundle = new Bundle();
				System.out.println("pos:"+pos);
				bundle.putSerializable("groupinfo", g);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		if(g.getExtra2() == null){
			holder.groupImg_iv.setImageResource(R.drawable.default_groupphoto);
		}else{
			byte[] imgByte = MyUtil.hex2byte(g.getExtra2());
			Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, null);
			holder.groupImg_iv.setImageBitmap(bitmap);
			holder.groupImg_iv.setTag(position);
		}
		holder.addgroup_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				 ApplyAddGroupTask agtt = new ApplyAddGroupTask();
				 agtt.execute(new String[]{ null,String.valueOf(g.getId()),"1"});
			}
		});
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupList.size();	
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
		System.out.println("view:"+v.getId());
		switch(v.getId()){
			/*case R.id.activity_add_find_item_icon_iv:
				Intent intent = new Intent(context,GroupInfoActivity.class);
				Bundle bundle = new Bundle();
				pos = (Integer) v.getTag();
				System.out.println("pos:"+pos);
				bundle.putSerializable("groupinfo", groupList.get(pos));
				intent.putExtras(bundle);
				context.startActivity(intent);
				break;*/
			case R.id.activity_add_find_item_location_iv:
				break;
		}
		
	}
	
	 static class ViewHolder { 
         TextView groupNmae_tv; 
         ImageView groupImg_iv; 
         Button addgroup_btn;

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
					if(null != save.getCookie()){  
						 post.setHeader("Cookie", "sessionId=" + save.getCookie().getValue());  
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
				if("-1".equalsIgnoreCase(result)){
					Toast.makeText(context, "你还未登陆！！!", Toast.LENGTH_SHORT)
					.show();
				}else if("1".equalsIgnoreCase(result)){
					Toast.makeText(context, "申请信息发送成功", Toast.LENGTH_SHORT)
					.show();
				}else if("0".equalsIgnoreCase(result)){
					Toast.makeText(context, "申请信息发送失败！！！", Toast.LENGTH_SHORT)
					.show();
				}else if("2".equalsIgnoreCase(result)){
					Toast.makeText(context, "申请信息已经发送，请耐心等待对方验证！！！", Toast.LENGTH_SHORT)
					.show();
				}else if("3".equalsIgnoreCase(result)){
					Toast.makeText(context, "你已经是该群成员！！！", Toast.LENGTH_SHORT)
					.show();
				}else if("4".equalsIgnoreCase(result)){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);   
					 final EditText desc = new EditText(context);
					     builder.setTitle("请输入验证信息：");  
					     builder.setView(desc);  
					     builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					         public void onClick(DialogInterface dialog, int whichButton) {  
					         Toast.makeText(context,desc.getText().toString() , Toast.LENGTH_LONG).show();
					         ApplyAddGroupTask agtt = new ApplyAddGroupTask();
							 agtt.execute(new String[]{ desc.getText().toString(),groupId,null});
					         }  
					     });  
					     builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
					         public void onClick(DialogInterface dialog, int whichButton) {  
					 
					         }  
					     });  
					   builder.create().show(); 
				}
			}
		}
}

