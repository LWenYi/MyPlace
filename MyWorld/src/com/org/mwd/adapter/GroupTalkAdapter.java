package com.org.mwd.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.mwd.activities.GroupInfoActivity;
import com.org.mwd.util.MyUtil;
import com.org.mwd.view.RoundImageView;
import com.org.mwd.vo.MessageBean;
import com.org.mwd.vo.SaveBean;
import com.org.myworld.R;

public class GroupTalkAdapter extends BaseAdapter implements OnClickListener {

	Context context;
	private List<MessageBean> messageList;
	private MessageBean m;
	private LayoutInflater mInflater;
	int type;
	SaveBean save;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	public GroupTalkAdapter(Context context, List<MessageBean> list) {
		this.context = context;
		messageList = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		save = (SaveBean) context.getApplicationContext();
	}

	// 每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		m = messageList.get(position);
		if (m.getSendId() == save.getUb().getId()) {
			return TYPE_1;
		} else {
			return TYPE_2;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// View view =convertView;
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		m = messageList.get(position);
		if (m == null) {
			return null;
		}
		// 无convertView，需要new出各个控件
		if (convertView == null) {
			// 按当前所需的样式，确定new的布局
			switch (type) {
			case TYPE_1:
				convertView = mInflater.inflate(
						R.layout.item_chat_to, parent, false);
				;
				holder1 = new ViewHolder1();
				holder1.membername_tv = (TextView) convertView
						.findViewById(R.id.item_chat_to_name_tv);
				holder1.message_date = (TextView)convertView
						.findViewById(R.id.item_chat_to_time_tv);
				holder1.grouptalk_ll = (LinearLayout) convertView
						.findViewById(R.id.item_chat_ll);
				holder1.memberphoto_iv = (RoundImageView) convertView
						.findViewById(R.id.item_chat_to_photo_iv);
				holder1.content_tv = (TextView) convertView
						.findViewById(R.id.item_chat_to_content_tv);
				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = mInflater.inflate(
						R.layout.item_chat_from, parent, false);
				holder2 = new ViewHolder2();
				holder2.message_date = (TextView)convertView
						.findViewById(R.id.item_chat_from_time_tv);
				holder2.memberphoto_iv = (ImageView) convertView
						.findViewById(R.id.item_chat_from_memberphoto_iv);
				holder2.membername_tv = (TextView) convertView
						.findViewById(R.id.item_chat_from_membername_tv);
				holder2.content_tv = (TextView) convertView
						.findViewById(R.id.item_chat_from_content_tv);
				convertView.setTag(holder2);
				break;
			}
		} else {
			// 有convertView，按样式，取得不用的布局
			switch (type) {
			case TYPE_1:
				holder1 = (ViewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				holder2 = (ViewHolder2) convertView.getTag();
				break;
			}
		}
		// 设置资源
		switch (type) {
		case TYPE_1:
			// 填充消息的发送者
			if (m.getSendId() == save.getUb().getId())
				holder1.membername_tv.setText(m.getExtra1());
			// 填充发送者的图片
			if (m.getExtra2() == null) {
				holder1.memberphoto_iv.setImageResource(R.drawable.ic_launcher);
			} else {
				byte[] imgByte = MyUtil.hex2byte(m.getImage());
				Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
						imgByte.length, null);
				holder1.memberphoto_iv.setImageBitmap(bitmap);
				holder1.memberphoto_iv.setTag(position);
			}
			// 填充消息内容
			holder1.content_tv.setText(m.getContent());
			holder1.message_date.setText(m.getSendTime());
			break;
		case TYPE_2:
			// 填充消息的发送者
			if (m.getSendId() != save.getUb().getId())
				holder2.membername_tv.setText(m.getExtra1());
			// 填充发送者的图片
			if (null == m.getImage()) {
				holder2.memberphoto_iv.setImageResource(R.drawable.default_userphoto);
			} else {
				byte[] imgByte = MyUtil.hex2byte(m.getImage());
				Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
						imgByte.length, null);
				holder2.memberphoto_iv.setImageBitmap(bitmap);
				holder2.memberphoto_iv.setTag(position);
			}
			// 填充消息内容
			holder2.content_tv.setText(m.getContent());
			holder2.message_date.setText(m.getSendTime());
			break;
		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
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
		System.out.println("view:" + v.getId());
		switch (v.getId()) {
		case R.id.activity_add_find_item_icon_iv:
			Intent intent = new Intent(context, GroupInfoActivity.class);
			context.startActivity(intent);
			break;
		case R.id.activity_add_find_item_location_iv:
			break;
		}

	}

	static class ViewHolder1 {

		TextView membername_tv, content_tv,message_date;
		RoundImageView memberphoto_iv;
		LinearLayout grouptalk_ll;
	}

	static class ViewHolder2 {
		TextView membername_tv, content_tv,message_date;
		ImageView memberphoto_iv;
	}
}
