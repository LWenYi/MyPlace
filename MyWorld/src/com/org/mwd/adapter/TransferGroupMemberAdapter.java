package com.org.mwd.adapter;

import java.util.ArrayList;
import java.util.List;

import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransferGroupMemberAdapter extends BaseAdapter {
	
	Context context;
	private List<UserBean> 	userList = new ArrayList<UserBean>();
	private LayoutInflater mInflater;
	int type;

	public TransferGroupMemberAdapter(Context context,List<UserBean> list) {
		this.context =context;
		userList = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view =convertView; 
		if(userList == null || userList.isEmpty()){
			return convertView;
		}
		ViewHolder holder; 
		if(view ==null) 
		{
		 holder = new ViewHolder();
		view=mInflater.inflate(R.layout.item_transfer_group_member, parent, false);
		holder.item_rl = (RelativeLayout) view.findViewById(R.id.fragment_friend_rl);
		holder.nick_tv = (TextView) view.findViewById(R.id.fragment_friend__name_tv);
		holder.desc_tv = (TextView) view.findViewById(R.id.fragment_friend__desc_tv);
		holder.image_iv = (ImageView)view.findViewById(R.id.fragment_friend_img_iv);
		holder.location_iv = (ImageView) view.findViewById(R.id.fragment_friend_location_iv);
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
		
		if(null != user.getDesc() && !"".equalsIgnoreCase(user.getDesc())){
			holder.desc_tv.setText(user.getDesc());
		}else{
			holder.desc_tv.setText("这个人很懒，什么也没留下！");
		}
		holder.item_rl.setBackgroundColor(Color.WHITE);
		/*if(user.getStatus() == 0){
			holder.item_rl.setBackgroundResource(R.color.offline);
			String desc = holder.desc_tv.getText().toString();
			holder.desc_tv.setText("[离线]"+desc);
			//holder.desc_tv.setTextColor(Color.GRAY);
		}else{
			holder.item_rl.setBackgroundColor(Color.WHITE);
		}*/
		holder.image_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
			}
			
		});
		if(user.getImage() == null){
			holder.image_iv.setImageResource(R.drawable.default_userphoto);
		}else{
			  byte[] imgByte = MyUtil.hex2byte(user.getImage());
			  Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, null);
			  holder.image_iv.setImageBitmap(bitmap);
		}
		holder.location_iv.setVisibility(8);
		return view;
	}

		


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();	
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


}