package com.org.mwd.activities;

import java.io.InputStream;
import java.lang.reflect.Field;
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
import com.org.mwd.util.ProgressDlgUtil;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class LoginActivity extends Activity implements OnClickListener,
		OnTouchListener {
	EditText psw_et;
	TextView findpsw_tv, register_tv;
	ImageView person_image;
	Button login_btn;
	String phone, psw;
	AutoCompleteTextView phone_actv;
	SaveBean save;
	CheckBox cb_mima, cb_auto;
	SharedPreferences sp, mysp;
	LoginTask lt;
	FindPasswordTask fpt;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		save = (SaveBean) getApplicationContext();
		ExitApplication.getInstance().addActivity(this);
		person_image = (ImageView) this
				.findViewById(R.id.activity_login_person_image);
		login_btn = (Button) this.findViewById(R.id.activity_login_btn);
		findpsw_tv = (TextView) this
				.findViewById(R.id.activity_login_findpassword);
		register_tv = (TextView) this
				.findViewById(R.id.activity_login_register);
		phone_actv = (AutoCompleteTextView) this
				.findViewById(R.id.activity_login_phone);
		psw_et = (EditText) this.findViewById(R.id.activity_login_psw);
		cb_mima = (CheckBox) this.findViewById(R.id.activity_login_cb_mima);
		cb_auto = (CheckBox) this.findViewById(R.id.activity_login_cb_auto);

		login_btn.setOnClickListener(this);
		person_image.setOnClickListener(this);
		findpsw_tv.setOnClickListener(this);
		register_tv.setOnClickListener(this);
		sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
		mysp = this.getSharedPreferences("lastUserInfo", MODE_PRIVATE); // 最后一个记住密码的用户
		phone_actv.setText(mysp.getString("phone", ""));
		psw_et.setText(mysp.getString("password", ""));
		phone_actv.setThreshold(1);// 输入1个字母就开始自动提示
		// 判断记住密码多选框的状态
		if (sp.getBoolean("ISCHECK", false)) {
			// 设置默认是记录密码状态
			cb_mima.setChecked(true);
			// 判断自动登陆多选框状态
			if (sp.getBoolean("AUTO_ISCHECK", false)) {
				// 设置默认是自动登录状态
				cb_auto.setChecked(true);
				// 自动登陆
				phone = phone_actv.getText().toString();
				psw = psw_et.getText().toString();
				lt = new LoginTask();
				lt.execute();

			}
		}
		// 设置phone_et的监听事件
		phone_actv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 根据用户输入自动提示用户登陆过的账号
				String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				// sp.getAll()返回一张hash map
				// keySet()得到的是a set of the keys.
				// hash map是由key-value组成的
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						LoginActivity.this,
						R.layout.activity_login_complete_textview_item,
						allUserName);
				phone_actv.setAdapter(adapter);// 设置数据适配器

				// 自动装配用户头像
				SharedPreferences sharedPreferences = getSharedPreferences(
						"personImg", Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				String imgPath = sharedPreferences.getString(phone_actv
						.getText().toString(), "");
				if (imgPath != null && imgPath != "") {
					System.out.println("imgPath:" + imgPath);
					Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
					if (null != bitmap) {
						person_image.setImageBitmap(bitmap);
					} else {
						person_image.setImageResource(R.drawable.default_userphoto);
					}

				} else {
					person_image.setImageResource(R.drawable.default_userphoto);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				psw_et.setText(sp
						.getString(phone_actv.getText().toString(), ""));// 自动输入密码
			}
		});

		// 监听记住密码多选框按钮事件
		cb_mima.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (cb_mima.isChecked()) {
					System.out.println("记住密码已选中");
					sp.edit().putBoolean("ISCHECK", true).commit();

				} else {

					System.out.println("记住密码没有选中");
					sp.edit().putBoolean("ISCHECK", false).commit();

				}

			}
		});
		// 监听自动登录多选框事件
		cb_auto.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (cb_auto.isChecked()) {
					System.out.println("自动登录已选中");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("自动登录没有选中");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});

		// 从sharedpreferences中拿到imgpath
		SharedPreferences sharedPreferences = getSharedPreferences("personImg",
				Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String imgPath = sharedPreferences.getString(phone_actv.getText()
				.toString(), "");
		System.out.println("title:" + imgPath);
		if (imgPath != null && imgPath != "") {
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
			person_image.setImageBitmap(bitmap);
		} else {
			person_image.setImageResource(R.drawable.default_userphoto);
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LoginActivity.this.finish();
			Intent i = new Intent();
			i.setClass(this, MainActivity.class);
			this.startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		System.out.println(id);
		switch (id) {
		case R.id.activity_login_register:
			Intent i = new Intent();
			i.setClass(this, RegisterActivity.class);
			this.startActivity(i);
			break;
		case R.id.activity_login_btn:
			phone = phone_actv.getText().toString();
			psw = psw_et.getText().toString();
			lt = new LoginTask();
			lt.execute();
			break;
		case R.id.activity_login_findpassword:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);
			LayoutInflater mInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(
					R.layout.activity_login_find_password, null);
			final EditText userAccount_et = (EditText) view
					.findViewById(R.id.activity_personalcenter_userAccount_et);
			final EditText userEmail_et = (EditText) view
					.findViewById(R.id.activity_personalcenter_userEmail_et);
			builder.setTitle("请输入账号及预留邮箱！！！");
			builder.setView(view);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);
							} catch (Exception e) {
								e.printStackTrace();
							}
							String userAccount = userAccount_et.getText()
									.toString();
							String userEmail = userEmail_et.getText()
									.toString();
							if (!MyUtil.isPhoneNumberValid(userAccount)) {
								Toast.makeText(getApplicationContext(),
										"账号格式不对！！！", Toast.LENGTH_LONG).show();
							} else if (!MyUtil.isEmail(userEmail)) {
								Toast.makeText(getApplicationContext(),
										"邮箱格式不对！！！", Toast.LENGTH_LONG).show();
							} else {
								try {
									Field field = dialog.getClass()
											.getSuperclass()
											.getDeclaredField("mShowing");
									field.setAccessible(true);
									field.set(dialog, true);
								} catch (Exception e) {
									e.printStackTrace();
								}
								fpt = new FindPasswordTask();
								fpt.execute(new String[] {
										userAccount_et.getText().toString(),
										userEmail_et.getText().toString() });
							}

						}
					});
			builder.setNegativeButton("取消", null);
			builder.create().show();
			break;

		}
	}

	// 登陆的异步任务
	public class LoginTask extends AsyncTask<String, Integer, String> {
		Cookie cookie = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDlgUtil.showProgressDlg("正在进行登录....", LoginActivity.this,
					lt);
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/LoginServlet";
			System.out.println("path:" + PATH);
			String result = "-1";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("psw", psw));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpParams hparams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(hparams, 5000); // 设置连接超时
				HttpConnectionParams.setSoTimeout(hparams, 20000); // 设置请求超时
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result:" + result);
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
				Thread.sleep(1 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
				return "3";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			ProgressDlgUtil.stopProgressDlg();
			if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "用户名或密码有误,或者还未注册！！！",
						Toast.LENGTH_LONG).show();
			} else if ("2".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "你的账号已被管理员禁止登陆！！",
						Toast.LENGTH_LONG).show();
			} else if ("3".equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(), "请求超时！！！",
						Toast.LENGTH_LONG).show();
			} else{
				Gson gson = new Gson();
				UserBean ub = gson.fromJson(result, new TypeToken<UserBean>() {
				}.getType());
				System.out.println("Loginimage:" + ub.getImage());
				ub.setPassword(psw);
				if (null != ub.getExtra2() && ub.getExtra2().length() > 0) {
					String fileType = MyUtil.getFileType(ub.getImage());
					String imgPath = LoginActivity.this.getCacheDir() + "/"
							+ "MyWorld/" + ub.getName() + "/" + ub.getName()
							+ "." + fileType;
					MyUtil.saveImgByStr(imgPath, ub.getExtra2());
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
				if (cb_mima.isChecked()) {
					// 记住用户名、密码、
					Editor editor = sp.edit();
					sp.edit().putString(phone, psw).commit();
					editor.commit();
					Editor myEditor = mysp.edit();
					myEditor.putString("phone", phone);
					myEditor.putString("password", psw);
					myEditor.commit();
				}
				save.setUb(ub);
				save.setIsrun(true);
				LoginActivity.this.finish();
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_login_register:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Intent i = new Intent();
				i.setClass(this, RegisterActivity.class);
				this.startActivity(i);
				this.finish();
				register_tv.setTextColor(R.color.activity_login_tv_color);
				return true;
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				register_tv.setTextColor(Color.WHITE);
				return false;
			}
			break;
		case R.id.activity_login_findpassword:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				findpsw_tv.setTextColor(R.color.activity_login_tv_color);
				return true;
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				findpsw_tv.setTextColor(Color.WHITE);
				return false;
			}
			break;
		}
		return false;
	}

	public class FindPasswordTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDlgUtil.showProgressDlg("正在请求重置密码....", LoginActivity.this,
					fpt);
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/ForgetPassword";
			System.out.println("path:" + PATH);
			String result = "false";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("username", arg0[0]));
				params.add(new BasicNameValuePair("mail", arg0[1]));
				HttpEntity formEntity = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(formEntity);
				HttpParams hparams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(hparams, 5000); // 设置连接超时
				HttpConnectionParams.setSoTimeout(hparams, 10000); // 设置请求超时
				HttpResponse response = httpclient.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					result = MyUtil.inStreamToString(is);
					System.out.println("result:" + result);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			ProgressDlgUtil.stopProgressDlg();
			Gson gson = new Gson();
			ResultJson data = gson.fromJson(result,
					new TypeToken<ResultJson>() {
					}.getType());
			if ("nouser".equalsIgnoreCase(data.result)) {
				Toast.makeText(getApplicationContext(), "用户账号不存在，请重新输入!!!",
						Toast.LENGTH_LONG).show();
			}
			if ("falsemain".equalsIgnoreCase(data.result)) {
				Toast.makeText(getApplicationContext(), "邮箱不存在，请重新输入!!!",
						Toast.LENGTH_LONG).show();
			}
			if ("false".equalsIgnoreCase(data.result)) {
				Toast.makeText(getApplicationContext(), "邮件发送失败!!!",
						Toast.LENGTH_LONG).show();
			}
			if ("success".equalsIgnoreCase(data.result)) {
				Toast.makeText(getApplicationContext(), "成功发送重置密码到邮箱，请接收!!!",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	public class ResultJson {
		String result;
	}
}
