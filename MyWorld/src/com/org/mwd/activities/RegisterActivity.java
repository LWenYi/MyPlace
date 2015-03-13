package com.org.mwd.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	private TextView phone_tv, nick_tv, psw_tv, bcak_tv;
	private ImageView person_image;
	private Button register_btn;
	private String phone, nick, psw, imgPath, imgType;
	private Uri uri;
	private Bitmap bitmap;
	private SaveBean save;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ExitApplication.getInstance().addActivity(this);
		save = (SaveBean) this.getApplicationContext();
		person_image = (ImageView) this
				.findViewById(R.id.activity_register_person_image);
		phone_tv = (TextView) this.findViewById(R.id.activity_register_phone);
		nick_tv = (TextView) this.findViewById(R.id.activity_register_nick);
		psw_tv = (TextView) this.findViewById(R.id.activity_register_psw);
		register_btn = (Button) this.findViewById(R.id.activity_register_btn);
		bcak_tv = (TextView) this
				.findViewById(R.id.activity_rigister_return_tv);
		System.out.println(person_image + "--" + register_btn);
		person_image.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		bcak_tv.setOnClickListener(this);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_userphoto);
		imgPath = "D:/client/photos/default/default.jpg";
		imgType = "jpg";

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RegisterActivity.this.finish();
			Intent i = new Intent();
			i.setClass(this, LoginActivity.class);
			this.startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		System.out.println(id);
		switch (id) {
		case R.id.activity_rigister_return_tv:
			this.finish();
			break;
		case R.id.activity_register_btn:
			phone = phone_tv.getText().toString();
			nick = nick_tv.getText().toString();
			psw = psw_tv.getText().toString();
			if (psw == "" || psw.length() < 6) {
				Toast.makeText(this, "密码小于6位！！！", Toast.LENGTH_SHORT).show();
			} else if (!MyUtil.isPhoneNumberValid(phone)) {
				Toast.makeText(this, "手机格式不对！！！", Toast.LENGTH_SHORT).show();
			} else {
				registerTask rt = new registerTask();
				rt.execute();
			}

			break;
		case R.id.activity_register_person_image:
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

		}

	}

	// 注册的异步任务
	public class registerTask extends AsyncTask<String, Integer, String> {
		Cookie cookie = null;
		String imgStr = null;

		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/RegisterServlet";
			String result = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (bitmap != null) {
					byte[] bytes = MyUtil.Bitmap2Bytes(bitmap);
					imgStr = MyUtil.byte2hex(bytes);
				}
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("nick", nick));
				params.add(new BasicNameValuePair("psw", psw));
				params.add(new BasicNameValuePair("imgStr", imgStr));
				params.add(new BasicNameValuePair("imgType", imgType));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					Cookie cooki;
					String cookname, cookvalue;
					List<Cookie> cookies = ((AbstractHttpClient) httpclient)
							.getCookieStore().getCookies();
					if (cookies.isEmpty()) {
					} else {
						for (int i = 0; i < cookies.size(); i++) {
							// 保存cookie
							cooki = cookies.get(i);
							cookname = cooki.getName().trim();
							cookvalue = cooki.getValue().trim();
							if (cookname.equals("sessionId")) {
								cookie = cooki;
								System.out.println("-------------cookieName:"
										+ cookname + "------cookieValue:"
										+ cookvalue);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "2";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("0".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "注册失败！！！",
						Toast.LENGTH_LONG).show();
			} else if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "该手机用户已经注册！！！",
						Toast.LENGTH_LONG).show();
			} else if ("2".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "请求服务器超时！！",
						Toast.LENGTH_LONG).show();
			} else if (null != result) {
				Toast.makeText(getApplicationContext(), "注册成功！！！",
						Toast.LENGTH_LONG).show();
				Gson gson = new Gson();
				UserBean ub = gson.fromJson(result, new TypeToken<UserBean>() {
				}.getType());
				ub.setPassword(psw);
				if (null != ub.getImage() && ub.getImage().length() > 0) {
					String fileType = MyUtil.getFileType(ub.getImage());
					String imgPath = RegisterActivity.this.getCacheDir() + "/"
							+ "MyWorld/" + ub.getName() + "/" + ub.getName()
							+ "." + fileType;
					MyUtil.saveImgByStr(imgPath, imgStr);
					ub.setImage(imgPath);
					// 创建一个sharedpreferences对象为test
					SharedPreferences mySharedPreferences = getSharedPreferences(
							"personImg", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putString(ub.getName(), imgPath);
					// 提交数据
					editor.commit();
				}
				if (null != cookie) {
					save.setCookie(cookie);
				}
				save.setUb(ub);
				save.setIsrun(true);
				RegisterActivity.this.finish();
				keepLoginThread klt = new keepLoginThread();
				new Thread(klt).start();
				Intent intent = new Intent(RegisterActivity.this,
						MainActivity.class);
				RegisterActivity.this.startActivity(intent);
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
			if (1024 * 20 > size) {
				person_image.setImageBitmap(bitmap);
			} else {
				int scalSize = (int) Math.sqrt(size / 10240);
				// bitmap =MyUtil.loadResBitmap(imgPath, scalSize);
				bitmap = MyUtil.loadResBitmap(imgPath, scalSize);
				size = bitmap.getHeight() * bitmap.getWidth();
				System.out.println("size:" + size + "scalSize:" + scalSize);
				if (1024 * 20 > size) {
					person_image.setImageBitmap(bitmap);
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

	public class keepLoginThread implements Runnable {
		@Override
		// 在run方法中定义任务
		public void run() {
			while (true) {
				try {
					Thread.sleep(20 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String PATH = "http://" + MyUtil.getIP()
						+ ":8080/MyWorldService/KeepLoginServlet";
				String result = null;
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost post = new HttpPost(PATH);
					if (null != save.getCookie()) {
						post.setHeader("Cookie", "sessionId="
								+ save.getCookie().getValue());
					}
					HttpParams hparams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(hparams, 5000); // 设置连接超时
					HttpConnectionParams.setSoTimeout(hparams, 10000); // 设置请求超时
					HttpResponse response = httpclient.execute(post);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						InputStream is = response.getEntity().getContent();
						result = MyUtil.inStreamToString(is);
						System.out.println("keeplogin:" + result);
						if ("0".equalsIgnoreCase(result)) {
							RegisterActivity.this.finish();
							Intent i = new Intent(RegisterActivity.this,
									LoginActivity.class);
							RegisterActivity.this.startActivity(i);
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

}
