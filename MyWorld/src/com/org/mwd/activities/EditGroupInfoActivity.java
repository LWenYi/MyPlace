package com.org.mwd.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;
import com.org.myworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditGroupInfoActivity extends Activity implements OnClickListener {
	private ImageView groupphoto_iv, groupName_iv,
			groupgroupDesc_iv;
	private Button uploadphoto_iv, savebtn_btn;
	private RelativeLayout edit_groupType_rl;
	private TextView groupType_tv,back_tv;
	private EditText groupName_et, groupgroupDesc_et;
	int type = 0;
	private String imgPath, imgType, imgStr;;
	private Bitmap bitmap;
	private Uri uri;
	private SaveBean save;
	private GroupBean g;
	private int groupType;
	private String groupname, desc;
	final int LIST_CHOOSEPHOTO = 0x114;
	final int LIST_GROUPTYPE = 0x115;
	// 定义3个列表项的名称
	private String[] strphoto = new String[] { "拍照", "从相册选择", "取消" };
	// 定义3个列表项对应的图标
	private int[] photosign = new int[] { R.drawable.takephotosign,
			R.drawable.photosign, R.drawable.buxiang };
	private String[] grouptype = new String[] { "同事朋友", "兴趣爱好", "志愿活动", "外出旅游" };

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_edit_groupinfo);
		ExitApplication.getInstance().addActivity(this);
		save = (SaveBean) this.getApplicationContext();
		Intent i = this.getIntent();
		g = (GroupBean) i.getSerializableExtra("groupinfo");

		
		back_tv = (TextView) this
				.findViewById(R.id.activity_edit_groupinfo_back_tv);
		back_tv.setOnClickListener(this);

		uploadphoto_iv = (Button) this
				.findViewById(R.id.activity_editgroup_uploadphoto_btn);
		uploadphoto_iv.setOnClickListener(this);

		groupName_iv = (ImageView) this
				.findViewById(R.id.activity_edit_groupinfo_groupName_iv);
		groupName_iv.setOnClickListener(this);

		groupgroupDesc_iv = (ImageView) this
				.findViewById(R.id.activity_edit_groupinfo_groupgroupDesc_iv);
		groupgroupDesc_iv.setOnClickListener(this);

		edit_groupType_rl = (RelativeLayout) this
				.findViewById(R.id.activity_edit_groupinfo_groupType_rl);
		edit_groupType_rl.setOnClickListener(this);

		savebtn_btn = (Button) this
				.findViewById(R.id.activity_editgroup_savebtn_btn);
		savebtn_btn.setOnClickListener(this);

		groupphoto_iv = (ImageView) this
				.findViewById(R.id.activity_editgroup_groupphoto_iv);

		groupType_tv = (TextView) this
				.findViewById(R.id.activity_edit_groupinfo_grouptype_tv);

		groupName_et = (EditText) this
				.findViewById(R.id.activity_edit_groupinfo_groupName_et);

		groupgroupDesc_et = (EditText) this
				.findViewById(R.id.activity_edit_groupinfo_groupgroupDesc_et);
		if (null != g) {
			setGroupInfo();
			GetGroupInfoByGroupIdTask ggibt = new GetGroupInfoByGroupIdTask();
			ggibt.execute();
		}
	}

	private void setGroupInfo() {
		if (null != g.getExtra2() && g.getExtra2().length() > 0) {
			byte[] imgByte = MyUtil.hex2byte(g.getExtra2());
			Bitmap bm = BitmapFactory.decodeByteArray(imgByte, 0,
					imgByte.length, null);
			groupphoto_iv.setImageBitmap(bm);
		} else {
			groupphoto_iv.setImageResource(R.drawable.default_userphoto);
		}
		groupType_tv.setText(grouptype[g.getGroupType()]);
		groupName_et.setText(g.getGroupName());
		if (null == g.getDesc() || "".equalsIgnoreCase(g.getDesc())) {
			groupgroupDesc_et.setText("群主很懒,你有什么办法！");
		} else {
			groupgroupDesc_et.setText(g.getDesc());
		}

	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case LIST_CHOOSEPHOTO:
			Builder photoBuilder = new AlertDialog.Builder(this);
			// 创建一个List对象，List对象的元素是Map
			List<Map<String, Object>> photolistItems = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < strphoto.length; i++) {
				Map<String, Object> photolistItem = new HashMap<String, Object>();
				photolistItem.put("photosign", photosign[i]);
				photolistItem.put("strphoto", strphoto[i]);
				photolistItems.add(photolistItem);
			}
			// 创建一个SimpleAdapter
			SimpleAdapter photosimpleAdapter = new SimpleAdapter(this,
					photolistItems, R.layout.common_editphoto, new String[] {
							"strphoto", "photosign" }, new int[] {
							R.id.strphoto, R.id.photosign });

			// 为对话框设置多个列表
			photoBuilder.setAdapter(photosimpleAdapter
			// 为列表项的单击事件设置监听器
					, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// which代表哪个列表项被单击了
							switch (which) {
							case 0:
								Intent intent0 = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								String path = Environment
										.getExternalStorageDirectory()
										.toString()
										+ "/images";
								File path1 = new File(path);
								if (!path1.exists()) {
									path1.mkdirs();
								}
								File file = new File(path1, System
										.currentTimeMillis() + ".jpg");
								uri = Uri.fromFile(file);
								intent0.putExtra(MediaStore.EXTRA_OUTPUT, uri);
								startActivityForResult(intent0, 2);
								break;
							case 1:
								Intent intent = new Intent();
								/* 开启Pictures画面Type设定为image */
								intent.setType("image/*");
								/* 使用Intent.ACTION_GET_CONTENT这个Action */
								intent.setAction(Intent.ACTION_GET_CONTENT);
								/* 取得相片后返回本画面 */
								startActivityForResult(intent, 1);
								System.out.println("image");
								break;
							case 2:
								dialog.dismiss();
							}
						}
					});
			// 创建对话框
			return photoBuilder.create();
		case LIST_GROUPTYPE:
			Builder b = new AlertDialog.Builder(this);
			// 创建一个List对象，List对象的元素是Map
			List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < grouptype.length; i++) {
				Map<String, Object> listItem = new HashMap<String, Object>();
				listItem.put("grouptype", grouptype[i]);
				listItems.add(listItem);
			}
			// 创建一个SimpleAdapter
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.common_choose_grouptype,
					new String[] { "grouptype" }, new int[] { R.id.grouptype });
			// 为对话框设置多个列表
			b.setAdapter(simpleAdapter
			// 为列表项的单击事件设置监听器
					, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// which代表哪个列表项被单击了
							groupType = which;
							groupType_tv.setText(grouptype[which]);
						}
					});
			// 创建对话框
			return b.create();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_edit_groupinfo_back_tv:
			this.finish();
			break;
		case R.id.activity_editgroup_uploadphoto_btn:
			showDialog(LIST_CHOOSEPHOTO);
			break;
		case R.id.activity_edit_groupinfo_groupType_rl:
			showDialog(LIST_GROUPTYPE);
			break;
		case R.id.activity_edit_groupinfo_groupName_iv:
			removeEditable();
			groupName_et.setFocusableInTouchMode(true);
			groupName_et.setEnabled(true);
			groupName_et.setBackgroundResource(android.R.drawable.edit_text);
			break;
		case R.id.activity_edit_groupinfo_groupgroupDesc_iv:
			removeEditable();
			groupgroupDesc_et.setFocusableInTouchMode(true);
			groupgroupDesc_et.setEnabled(true);
			groupgroupDesc_et
					.setBackgroundResource(android.R.drawable.edit_text);
			break;
		case R.id.activity_editgroup_savebtn_btn:
			groupname = groupName_et.getText().toString();
			desc = groupgroupDesc_et.getText().toString();
			if (null == groupname || "".equalsIgnoreCase(groupname)) {
				Toast.makeText(this, "群名不能为空", Toast.LENGTH_SHORT).show();
			} else {
				UpdateGroupInfoTask ugit = new UpdateGroupInfoTask();
				ugit.execute();
			}
			break;
		}
	}

	// 重置各个组件为开始状态
	public void removeEditable() {
		groupName_et.setFocusableInTouchMode(false);
		groupName_et.setEnabled(false);
		groupName_et
				.setBackgroundResource(R.color.activity_personal_center_editview_color);
		
		groupgroupDesc_et.setFocusableInTouchMode(false);
		groupgroupDesc_et.setEnabled(false);
		groupgroupDesc_et
				.setBackgroundResource(R.color.activity_personal_center_editview_color);
	}

	@Override
	// 用于获取用户从本地相册选取的图片
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				Uri uri0 = data.getData();
				Log.e("uri", uri0.getPath());
				ContentResolver cr = this.getContentResolver();
				try {
					bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri0));
					/* 将Bitmap设定到ImageView */
					String[] proj = { MediaStore.Images.Media.DATA };
					// 好像是android多媒体数据库的封装接口，具体的看Android文档
					@SuppressWarnings("deprecation")
					Cursor cursor = managedQuery(uri0, proj, null, null, null);
					// 按我个人理解 这个是获得用户选择的图片的索引值
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// 将光标移至开头 ，这个很重要，不小心很容易引起越界
					cursor.moveToFirst();
					// 最后根据索引值获取图片路径
					imgPath = cursor.getString(column_index);
					imgType = MyUtil.getFileType(imgPath);
					System.out.println(imgPath);
					System.out.println(imgType);
				} catch (FileNotFoundException e) {
					Log.e("Exception", e.getMessage(), e);
				}
			} else if (requestCode == 2) {
				ContentResolver cr = this.getContentResolver();
				bitmap = null;
				try {
					bitmap = BitmapFactory
							.decodeStream(cr.openInputStream(uri));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("uri++", uri.getPath());
				imgPath = uri.getPath();
				imgType = MyUtil.getFileType(imgPath);
			}
			int size = bitmap.getHeight() * bitmap.getWidth();
			System.out.println("size:" + size);
			if (1024 * 20 >= size) {
				groupphoto_iv.setImageBitmap(bitmap);
			} else {
				int scalSize = (int) Math.sqrt(size / 10240);
				bitmap = MyUtil.loadResBitmap(imgPath, scalSize);
				size = bitmap.getHeight() * bitmap.getWidth();
				System.out.println("size:" + size + "scalSize:" + scalSize);
				if (1024 * 20 >= size) {
					groupphoto_iv.setImageBitmap(bitmap);
				} else {
					Toast.makeText(this, "图片大小超过10K，请重新选择！！！",
							Toast.LENGTH_LONG).show();
					imgPath = null;
					imgType = null;
					bitmap = null;

				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 修改群资料的异步任务
	public class UpdateGroupInfoTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/UpdateGroupInfoServlet";
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				if (bitmap != null) {
					byte[] bytes = MyUtil.Bitmap2Bytes(bitmap);
					imgStr = MyUtil.byte2hex(bytes);
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("groupId", g.getId() + ""));
				params.add(new BasicNameValuePair("desc", desc));
				params.add(new BasicNameValuePair("groupname", groupname));
				params.add(new BasicNameValuePair("grouptype", groupType + ""));
				params.add(new BasicNameValuePair("imgStr", imgStr));
				params.add(new BasicNameValuePair("imgType", imgType));
				params.add(new BasicNameValuePair("action", "updateGroupInfo"));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你还未登陆！！!",
						Toast.LENGTH_SHORT).show();
			} else if ("1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "修改群新信息成功！！！",
						Toast.LENGTH_SHORT).show();
				g.setGroupName(groupname);
				g.setGroupType(groupType);
				g.setDesc(desc);
				if (imgStr != null && imgStr.length() > 0) {
					g.setExtra2(imgStr);
				} else {
					g.setExtra2(null);
					g.setGroupImage(null);
				}
				Intent i = new Intent(EditGroupInfoActivity.this,GroupInfoActivity.class);
				i.putExtra("groupinfo", g);
				EditGroupInfoActivity.this.startActivity(i);
				EditGroupInfoActivity.this.finish();
				
			} else if ("0".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "修改群新信息失败！！！",
						Toast.LENGTH_SHORT).show();
			} else if ("2".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你已经不是群主！！！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 查找群资料的异步任务
	public class GetGroupInfoByGroupIdTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/UpdateGroupInfoServlet";
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("groupId", g.getId() + ""));
				params.add(new BasicNameValuePair("action",
						"getGroupInfoByGroupId"));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result:" + result);
				}
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你还未登陆！！!",
						Toast.LENGTH_SHORT).show();
			} else if ("1".equalsIgnoreCase(result)) {
				Gson gson = new Gson();
				g = gson.fromJson(result, new TypeToken<GroupBean>() {
				}.getType());
				setGroupInfo();
			} else if ("0".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "查找群信息失败！！！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
