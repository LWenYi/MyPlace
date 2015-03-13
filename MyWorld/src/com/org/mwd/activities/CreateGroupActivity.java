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
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
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

public class CreateGroupActivity extends Activity implements OnClickListener {
	private ImageView person_iv;
	private RelativeLayout group_size_rl, group_type_rl;
	private EditText group_name, group_desc;
	private Button finsh_create;
	private TextView size_tv, type_tv, back_tv;
	private String imgPath, imgType;
	private int type = 0, size = 10;
	private Uri uri;
	private Bitmap bitmap;
	SaveBean save;
	final int LIST_GROUPTYPE = 0x115;
	private String[] grouptype = new String[] { "同事朋友", "兴趣爱好", "志愿活动", "外出旅游" };

	final int LIST_GROUPSIZE = 0x116;
	private String[] groupsize = new String[] { "10人群", "30人群", "50人群" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_create_group);
		ExitApplication.getInstance().addActivity(this);
		save = (SaveBean) this.getApplicationContext();
		back_tv = (TextView) this
				.findViewById(R.id.activity_create_group_back_tv);
		person_iv = (ImageView) this
				.findViewById(R.id.activity_create_group_person_image);
		group_name = (EditText) this
				.findViewById(R.id.activity_create_group_group_name);
		group_desc = (EditText) this
				.findViewById(R.id.activity_create_group_group_desc);
		group_size_rl = (RelativeLayout) this
				.findViewById(R.id.activity_create_group_group_size_rl);
		group_type_rl = (RelativeLayout) this
				.findViewById(R.id.activity_create_group_group_type_rl);
		finsh_create = (Button) this
				.findViewById(R.id.activity_create_group_finsh_create);
		size_tv = (TextView) this
				.findViewById(R.id.activity_create_group_group_size_tv);
		type_tv = (TextView) this
				.findViewById(R.id.activity_create_group_group_type_tv);

		back_tv.setOnClickListener(this);
		person_iv.setOnClickListener(this);
		group_size_rl.setOnClickListener(this);
		group_type_rl.setOnClickListener(this);
		finsh_create.setOnClickListener(this);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_userphoto);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
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
							type = which;
							type_tv.setText(grouptype[which]);

						}
					});
			// 创建对话框
			return b.create();
		case LIST_GROUPSIZE:
			Builder groupsize_builder = new AlertDialog.Builder(this);
			// 创建一个List对象，List对象的元素是Map
			List<Map<String, Object>> groupsize_listItems = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < groupsize.length; i++) {
				Map<String, Object> groupsize_listItem = new HashMap<String, Object>();
				groupsize_listItem.put("groupsize", groupsize[i]);
				groupsize_listItems.add(groupsize_listItem);
			}
			// 创建一个SimpleAdapter
			SimpleAdapter groupsize_simpleAdapter = new SimpleAdapter(this,
					groupsize_listItems, R.layout.common_choose_grouptype,
					new String[] { "groupsize" }, new int[] { R.id.grouptype });
			// 为对话框设置多个列表
			groupsize_builder.setAdapter(groupsize_simpleAdapter
			// 为列表项的单击事件设置监听器
					, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// which代表哪个列表项被单击了
							size = which;
							size_tv.setText(groupsize[which]);

						}
					});
			// 创建对话框
			return groupsize_builder.create();
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.activity_create_group_back_tv:
			this.finish();
			this.startActivity(new Intent(this, AddActivity.class));
			break;
		case R.id.activity_create_group_person_image:
			new AlertDialog.Builder(this)
					.setTitle("选择图片")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(new String[] { "拍照", "从相册选择" }, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
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
										intent0.putExtra(
												MediaStore.EXTRA_OUTPUT, uri);
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
									}
									dialog.dismiss();
								}
							}).setPositiveButton("确定", null)
					.setNegativeButton("取消", null).show();
			break;
		case R.id.activity_create_group_group_size_rl:
			showDialog(LIST_GROUPSIZE);
			break;
		case R.id.activity_create_group_group_type_rl:
			showDialog(LIST_GROUPTYPE);
			break;
		case R.id.activity_create_group_finsh_create:
			SaveBean save = (SaveBean) this.getApplicationContext();
			UserBean ub = save.getUb();
			if ("".equalsIgnoreCase(group_name.getText().toString())) {
				Toast.makeText(getApplicationContext(), "群名不能为空！！",
						Toast.LENGTH_SHORT).show();
			} else {
				CreateGroupTask cgt = new CreateGroupTask();
				cgt.execute(new String[] { ub.getId() + "", ub.getName(),
						ub.getPassword() });
			}

			break;
		}
	}

	// 登陆的异步任务
	public class CreateGroupTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/CreateGroupServlet";
			String result = "failure";
			GroupBean gb = new GroupBean();
			gb.setGroupName(group_name.getText().toString());
			gb.setManagerId(Integer.parseInt(arg0[0]));
			gb.setGroupType(type);
			gb.setGroupsize(size);
			gb.setCreateTime(MyUtil.getDateTime());
			gb.setDesc(group_desc.getText().toString());
			gb.setStatus(1);
			gb.setGroupImage(imgType);
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				String imgStr = null;
				if (bitmap != null) {
					byte[] bytes = MyUtil.Bitmap2Bytes(bitmap);
					imgStr = MyUtil.byte2hex(bytes);
					gb.setExtra2(imgStr);
				}
				Gson gson = new Gson();
				String groupInfo = gson.toJson(gb);
				System.out.println("groupInfo:" + gson.toJson(gb));
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				}
				params.add(new BasicNameValuePair("groupInfo", groupInfo));
				params.add(new BasicNameValuePair("imgType", imgType));
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
			if ("success".equalsIgnoreCase(result)) {
				CreateGroupActivity.this.finish();
				Toast.makeText(getApplicationContext(), "创建群成功",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_SHORT).show();
			}
		}

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
				person_iv.setImageBitmap(bitmap);
			} else {
				int scalSize = (int) Math.sqrt(size / 10240);
				bitmap = MyUtil.loadResBitmap(imgPath, scalSize);
				size = bitmap.getHeight() * bitmap.getWidth();
				System.out.println("size:" + size + "scalSize:" + scalSize);
				if (1024 * 20 >= size) {
					person_iv.setImageBitmap(bitmap);
				} else {
					Toast.makeText(this, "图片大小超过20K，请重新选择！！！",
							Toast.LENGTH_LONG).show();
					imgPath = null;
					imgType = null;
					bitmap = null;
					person_iv.setImageResource(R.drawable.default_groupphoto);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
