package com.org.mwd.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.mwd.activities.GroupInfoActivity;
import com.org.mwd.activities.MainActivity;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;
import com.org.myworld.R;

public class GroupAdapter extends BaseAdapter implements OnClickListener{

	Context context;
	private List<GroupBean> 	groupList;
	private LayoutInflater mInflater;
	int type;
	int pos;
	SaveBean save;

	public GroupAdapter(Context context,List<GroupBean> list) {
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
		view=mInflater.inflate(R.layout.item_group, parent, false);
		holder.groupNmae_tv = (TextView) view.findViewById(R.id.activity_add_find_item_name_tv);
		holder.descr_tv = (TextView) view.findViewById(R.id.activity_add_find_item_descr_tv);
		holder.groupImg_iv = (ImageView)view.findViewById(R.id.activity_add_find_item_icon_iv);
		holder.location_iv = (ImageView) view.findViewById(R.id.activity_add_find_item_location_iv);
		view.setTag(holder);
		}else 
		{ 
			holder=(ViewHolder)view.getTag(); 
		}  
		final GroupBean g = groupList.get(position);
		if (g == null) {
			return null;
		}
		holder.groupNmae_tv.setText(g.getGroupName()+"("+g.getExtra1()+")");
		if(null == g.getDesc() || "".equalsIgnoreCase(g.getDesc())){
			holder.descr_tv.setText("群主很懒,你有什么办法！");
		}else{
			holder.descr_tv.setText(g.getDesc());
		}
		
		holder.groupImg_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,GroupInfoActivity.class);
				Bundle bundle = new Bundle();
				System.out.println("pos:"+pos);
				bundle.putSerializable("groupinfo", g);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
			
		});
		if(null ==g.getExtra2() ){
			holder.groupImg_iv.setImageResource(R.drawable.default_groupphoto);
		}else{
			byte[] imgByte = MyUtil.hex2byte(g.getExtra2());
			Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, null);
			holder.groupImg_iv.setImageBitmap(bitmap);
			holder.groupImg_iv.setTag(position);
		}
		
		holder.location_iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,MainActivity.class);
				intent.putExtra("fragment", "group_location");
				intent.putExtra("groupId", g.getId());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
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
         TextView groupNmae_tv,descr_tv; 
         ImageView groupImg_iv,location_iv; 

     } 

}
