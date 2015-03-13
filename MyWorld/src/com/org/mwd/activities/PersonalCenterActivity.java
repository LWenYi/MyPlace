package com.org.mwd.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalCenterActivity extends Activity implements OnClickListener {
	private ImageView activity_personalcenter_userPhoto_iv;
	private EditText userNick_et, userEmail_et, userAddress_et;
	private TextView userAccount_tv, userBirthday_tv, userGender_tv, back_tv;
	private RelativeLayout edit_info_rl, edit_nick_rl, edit_email_rl,
			edit_gender_rl, edit_brith_rl, edit_address_rl;
	private Bitmap bitmap;
	private Uri uri;
	private String uname, psw, nick, brithday, email, address, gender, imgPath,
			imgType, imgStr;

	final int LIST_DIALOG = 0x113;
	final int LIST_CHOOSEPHOTO = 0x114;
	// 定义3个列表项的名称
	private String[] usergender = new String[] { "男", "女", "不详" };
	// 定义3个列表项对应的图标
	private int[] imageIds = new int[] { R.drawable.boysign,
			R.drawable.girlsign, R.drawable.buxiang };

	// 定义3个列表项的名称
	private String[] strphoto = new String[] { "拍照", "从相册选择", "取消" };
	// 定义3个列表项对应的图标
	private int[] photosign = new int[] { R.drawable.takephotosign,
			R.drawable.photosign, R.drawable.buxiang };

	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;

	private int mYear;
	private int mMonth;
	private int mDay;
	SaveBean save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		ExitApplication.getInstance().addActivity(this);
		findComponentView();
		save = (SaveBean) this.getApplicationContext();
		UserBean ub = save.getUb();
		uname = ub.getName();
		psw = ub.getPassword();
		gender = ub.getGender() + "";
		setEditInfo(ub);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		setDateTime();
	}

	/**
	 * 设置日期
	 */
	private void setDateTime() {
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDateDisplay();
	}

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		userBirthday_tv.setText(new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			updateDateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case LIST_DIALOG:
			Builder b = new AlertDialog.Builder(this);
			// 创建一个List对象，List对象的元素是Map
			List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < usergender.length; i++) {
				Map<String, Object> listItem = new HashMap<String, Object>();
				listItem.put("imageIds", imageIds[i]);
				listItem.put("usergender", usergender[i]);
				listItems.add(listItem);
			}
			// 创建一个SimpleAdapter
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.activity_personalcenter_editgender, new String[] {
							"usergender", "imageIds" }, new int[] {
							R.id.gender, R.id.gendersign });

			// 为对话框设置多个列表
			b.setAdapter(simpleAdapter
			// 为列表项的单击事件设置监听器
					, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// which代表哪个列表项被单击了
							gender = which + "";
							System.out.println("gender:" + gender);
							userGender_tv.setText(usergender[which]);

						}
					});
			// 创建对话框
			return b.create();
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
		}

		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	/**
	 * 处理日期和时间控件的Handler
	 */
	@SuppressLint("HandlerLeak")
	Handler dateandtimeHandler = new Handler() {

		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PersonalCenterActivity.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}

	};

	// 填充用户数据
	private void setEditInfo(UserBean ub) {
		System.out.println("personimage:" + ub.getImage());
		if (null != ub.getExtra2() && ub.getExtra2().length() > 0) { // 设置图片
			byte[] imgByte = MyUtil.hex2byte((ub.getExtra2()));
			bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length,
					null);
			activity_personalcenter_userPhoto_iv.setImageBitmap(bitmap);
			bitmap = null;
		} else {
			activity_personalcenter_userPhoto_iv
					.setImageResource(R.drawable.default_userphoto);
		}

		userAccount_tv.setText(ub.getName());
		if (ub.getNick() != null) { // 设置昵称
			userNick_et.setText(ub.getNick());
		} else {
			userNick_et.setHint("请输入");
		}
		if (ub.getEmail() != null)
			userEmail_et.setText(ub.getEmail());
		else
			userEmail_et.setHint("请输入");
		if (ub.getBirthday() != null)
			userBirthday_tv.setText(ub.getBirthday());
		else
			userBirthday_tv.setHint("请输入");
		if (ub.getAddress() != null)
			userAddress_et.setText(ub.getAddress());
		else
			userAddress_et.setHint("请输入");
		if (ub.getGender() == 0) { // 设置性别
			userGender_tv.setText("男");
		} else if (ub.getGender() == 1) {
			userGender_tv.setText("女");
		} else {
			userGender_tv.setText("不详");
		}

	}

	// 初始化各个组件
	private void findComponentView() {
		// TODO Auto-generated method stub
		activity_personalcenter_userPhoto_iv = (ImageView) this
				.findViewById(R.id.activity_personalcenter_userPhoto_iv);
		activity_personalcenter_userPhoto_iv.setOnClickListener(this);

		userAccount_tv = (TextView) this
				.findViewById(R.id.activity_personalcenter_userAccount_tv);
		userNick_et = (EditText) this
				.findViewById(R.id.activity_personalcenter_userNick_et);

		userEmail_et = (EditText) this
				.findViewById(R.id.activity_personalcenter_userEmail_et);

		userGender_tv = (TextView) this
				.findViewById(R.id.activity_personalcenter_userGender_tv);

		userBirthday_tv = (TextView) this
				.findViewById(R.id.activity_personalcenter_userBirthday_tv);

		userAddress_et = (EditText) this
				.findViewById(R.id.activity_personalcenter_userSeat_et);

		edit_info_rl = (RelativeLayout) this
				.findViewById(R.id.activity_personalcenter_edit_info_rl);// 编辑按钮
		edit_info_rl.setOnClickListener(this);

		edit_nick_rl = (RelativeLayout) this
				.findViewById(R.id.activity_personalcenter_editNick_rl);
		edit_nick_rl.setOnClickListener(this);

		edit_email_rl = (RelativeLayout) this
				.findViewById(R.id.activity_personalcenter_editEmail_rl);
		edit_email_rl.setOnClickListener(this);

		edit_gender_rl = (RelativeLayout) this
				.findViewById(R.id.activity_personalcenter_editGender_rl);
		edit_gender_rl.setOnClickListener(this);

		edit_brith_rl = (RelativeLayout) this
				.findViewById(R.id.activity_personalcenter_editBirthday_rl);
		edit_brith_rl.setOnClickListener(this);

		edit_address_rl = (RelativeLayout) this
				.findViewById(R.id.activity_personalcenter_editAddress_rl);
		edit_address_rl.setOnClickListener(this);

		back_tv = (TextView) this
				.findViewById(R.id.activity_personal_center_return_tv);
		back_tv.setOnClickListener(this);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_personalcenter_userPhoto_iv:// 更改用户头像
			showDialog(LIST_CHOOSEPHOTO);
			break;
		case R.id.activity_personalcenter_edit_info_rl:// 编辑按钮点击事件
			nick = userNick_et.getText().toString();
			email = userEmail_et.getText().toString();
			brithday = userBirthday_tv.getText().toString();
			address = userAddress_et.getText().toString();
			UpdateTask ut = new UpdateTask();
			ut.execute();
			break;
		case R.id.activity_personal_center_return_tv: // 返回设置界面
			/*
			 * Intent i = new Intent(this, MainActivity.class);
			 * i.putExtra("fragment", "setting"); this.startActivity(i);
			 */
			this.finish();
		case R.id.activity_personalcenter_editNick_rl: // 编辑昵称
			removeEditable();
			userNick_et.setFocusableInTouchMode(true);
			userNick_et.setEnabled(true);
			userNick_et.setBackgroundResource(android.R.drawable.edit_text);
			break;
		case R.id.activity_personalcenter_editEmail_rl:// 编辑邮箱
			removeEditable();
			userEmail_et.setFocusableInTouchMode(true);
			userEmail_et.setEnabled(true);
			userEmail_et.setBackgroundResource(android.R.drawable.edit_text);
			break;
		case R.id.activity_personalcenter_editGender_rl:// 编辑性别
			removeEditable();
			showDialog(LIST_DIALOG);
			// userGender_et.setFocusableInTouchMode(true);
			// userGender_et.setEnabled(true);
			// userGender_et.setBackgroundResource(android.R.drawable.edit_text);
			break;
		case R.id.activity_personalcenter_editBirthday_rl: // 编辑出生日期
			removeEditable();
			Message msg = new Message();
			if (edit_brith_rl.equals((RelativeLayout) v)) {
				msg.what = PersonalCenterActivity.SHOW_DATAPICK;
			}
			PersonalCenterActivity.this.dateandtimeHandler.sendMessage(msg);
			break;

		case R.id.activity_personalcenter_editAddress_rl:// 编辑地址
			removeEditable();
			userAddress_et.setFocusableInTouchMode(true);
			userAddress_et.setEnabled(true);
			userAddress_et.setBackgroundResource(android.R.drawable.edit_text);
			break;

		}
	}

	// 重置各个组件为开始状态
	public void removeEditable() {
		userNick_et.setFocusableInTouchMode(false);
		userNick_et.setEnabled(false);
		userNick_et
				.setBackgroundResource(R.color.activity_personal_center_editview_color);

		userEmail_et.setFocusableInTouchMode(false);
		userEmail_et.setEnabled(false);
		userEmail_et
				.setBackgroundResource(R.color.activity_personal_center_editview_color);

		userAddress_et.setFocusableInTouchMode(false);
		userAddress_et.setEnabled(false);
		userAddress_et
				.setBackgroundResource(R.color.activity_personal_center_editview_color);

	}

	// 修改个人资料的异步任务
	public class UpdateTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/UpdatePersonalInfoServlet";
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				} else {
					return result;
				}
				if (bitmap != null) {
					byte[] bytes = MyUtil.Bitmap2Bytes(bitmap);
					imgStr = MyUtil.byte2hex(bytes);
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("uname", uname));
				params.add(new BasicNameValuePair("psw", psw));
				params.add(new BasicNameValuePair("nick", nick));
				params.add(new BasicNameValuePair("email", email));
				params.add(new BasicNameValuePair("brithday", brithday));
				params.add(new BasicNameValuePair("address", address));
				params.add(new BasicNameValuePair("gender", gender));
				params.add(new BasicNameValuePair("imgStr", imgStr));
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
			if ("failure".equalsIgnoreCase(result)) {
			} else {
				UserBean ub = save.getUb();
				ub.setNick(nick);
				ub.setEmail(email);
				ub.setBirthday(brithday);
				ub.setAddress(address);
				ub.setGender(Integer.valueOf(gender));
				String imgPath = PersonalCenterActivity.this.getCacheDir()
						+ "/" + "MyWorld/" + ub.getName() + "/" + ub.getName()
						+ "." + imgType;
				if (imgStr != null && imgStr.length() > 0 && null != imgType) {
					MyUtil.saveImgByStr(imgPath, imgStr);
					ub.setImage(imgPath);
					ub.setExtra2(imgStr);
				} else {
					ub.setImage(null);
					ub.setExtra2(null);
				}
				// 创建一个sharedpreferences对象为test
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"personImg", Activity.MODE_PRIVATE);
				// 实例化SharedPreferences.Editor对象（第二步）
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// 用putString的方法保存数据
				editor.putString(ub.getName(), imgPath);
				// 提交数据
				editor.commit();
				save.setUb(ub);
				Toast.makeText(getApplicationContext(), "修改个人信息成功！！！",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(PersonalCenterActivity.this,
						PersonalCenterActivity.class);
				PersonalCenterActivity.this.startActivity(i);
				PersonalCenterActivity.this.finish();
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
				activity_personalcenter_userPhoto_iv.setImageBitmap(bitmap);

			} else {
				int scalSize = (int) Math.sqrt(size / 10240);
				bitmap = MyUtil.loadResBitmap(imgPath, scalSize);
				size = bitmap.getHeight() * bitmap.getWidth();
				System.out.println("size:" + size + "scalSize:" + scalSize);
				if (1024 * 20 >= size) {
					activity_personalcenter_userPhoto_iv.setImageBitmap(bitmap);
				} else {
					Toast.makeText(this, "图片大小超过20K，请重新选择！！！",
							Toast.LENGTH_LONG).show();
					imgPath = null;
					imgType = null;
					bitmap = null;

				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
