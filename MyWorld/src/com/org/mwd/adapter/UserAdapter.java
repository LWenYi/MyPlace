package com.org.mwd.adapter;

import java.util.ArrayList;
import java.util.List;

import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {
	
	Context context;
	private List<UserBean> 	userList = new ArrayList<UserBean>();
	private LayoutInflater mInflater;
	int type;

	public UserAdapter(Context context,List<UserBean> list) {
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
		view=mInflater.inflate(R.layout.item_user, parent, false);
		holder.nick_tv = (TextView) view.findViewById(R.id.activity_add_find_item_name_tv);
		holder.image_iv = (ImageView)view.findViewById(R.id.activity_add_find_item_icon_iv);
		holder.location_iv = (ImageView) view.findViewById(R.id.activity_add_find_item_location_iv);
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
		
		holder.image_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
			}
			
		});
		if(user.getImage() == null){
			holder.image_iv.setImageResource(R.drawable.ic_launcher);
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
        TextView nick_tv; 
        ImageView image_iv,location_iv; 

    } 


}