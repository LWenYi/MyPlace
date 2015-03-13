package com.org.mwd.adapter;


import java.util.ArrayList;
import java.util.List;

import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.GPS;
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

public class GPSInfoAdapter extends BaseAdapter {
	
	Context context;
	private List<GPS> 	gpsList = new ArrayList<GPS>();
	private LayoutInflater mInflater;
	int type;

	public GPSInfoAdapter(Context context,List<GPS> list) {
		this.context =context;
		gpsList = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view =convertView; 
		if(gpsList == null || gpsList.isEmpty()){
			return convertView;
		}
		ViewHolder holder; 
		if(view ==null) 
		{
		 holder = new ViewHolder();
		view=mInflater.inflate(R.layout.item_gpsinfo, parent, false);
		holder.nick_tv = (TextView) view.findViewById(R.id.activity_groupmember_list_item_membername_tv);
		holder.image_iv = (ImageView)view.findViewById(R.id.activity_groupmember_list_item_memberphoto_iv);
		view.setTag(holder);
		}else 
		{ 
			holder=(ViewHolder)view.getTag(); 
		}  
		final GPS mgps = gpsList.get(position);
		if (mgps == null ) {
			return convertView;
		}
		holder.nick_tv.setText(mgps.getNick());
		System.out.println("sad++++++++++++++++++++sad++++++++++:"+mgps.getNick());
		holder.image_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
			}
			
		});
		if(mgps.getImage() == null){
			holder.image_iv.setImageResource(R.drawable.default_userphoto);
		}else{
			  byte[] imgByte = MyUtil.hex2byte(mgps.getImage());
			  Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, null);
			  holder.image_iv.setImageBitmap(bitmap);
		}
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gpsList.size();	
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
        ImageView image_iv; 

    } 


}