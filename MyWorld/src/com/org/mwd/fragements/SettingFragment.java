package com.org.mwd.fragements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.org.mwd.activities.LoginActivity;
import com.org.mwd.activities.PersonalCenterActivity;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.util.MyUtil;
import com.org.mwd.view.SwitchButton;
import com.org.mwd.view.SwitchButton.OnChangeListener;
import com.org.mwd.vo.SaveBean;
import com.org.mwd.vo.UserBean;
import com.org.myworld.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener{
	RelativeLayout person_center_rl,about_us_rl,update_soft_rl,mylocation_rl;
	View view = null;
	Button back_bt,exit_bt,exitprogram_btn;
	SwitchButton wiperSwitch;
	SaveBean save = null;
	ImageView personImg_iv;
	int isShare;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			view  = inflater.inflate(R.layout.fragment_setting, null);
			return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		save = (SaveBean) this.getActivity().getApplicationContext();
		if(null == save.getUb()){
			this.startActivity(new Intent(this.getActivity(),LoginActivity.class));
		}
		
		personImg_iv =   (ImageView) view.findViewById(R.id.fragment_settingfragment_personImg_iv);
		person_center_rl =  (RelativeLayout) view.findViewById(R.id.fragment_settingfragment_managerAccount_rl);
		about_us_rl =  (RelativeLayout) view.findViewById(R.id.fragment_settingfragment_aboutus_rl);
		update_soft_rl =  (RelativeLayout) view.findViewById(R.id.fragment_settingfragment_update_rl);
		mylocation_rl  =  (RelativeLayout) view.findViewById(R.id.fragment_settingfragment_mylocation_rl);
		exit_bt = (Button) view.findViewById(R.id.fragment_settingfragment_exit_btn);
		exitprogram_btn = (Button) view.findViewById(R.id.fragment_settingfragment_exitprogram_btn);
		wiperSwitch = (SwitchButton) view.findViewById(R.id.wiperSwitch1);
		
		
		person_center_rl.setOnClickListener(this);
		about_us_rl.setOnClickListener(this);
		update_soft_rl.setOnClickListener(this);
		exit_bt.setOnClickListener(this);
		exitprogram_btn.setOnClickListener(this);
		if(null != save.getUb().getExtra2() && save.getUb().getExtra2().length()>0){  //设置图片
	         byte[] imgByte = MyUtil.hex2byte((save.getUb().getExtra2()));
	         Bitmap bitmap  = BitmapFactory.decodeByteArray(imgByte, 0,
					imgByte.length, null);
	        personImg_iv.setImageBitmap(bitmap);
		}else{
			personImg_iv.setImageResource(R.drawable.default_userphoto);
		}
		if(save.getUb().getIsShare() != 1){
			wiperSwitch.performClick();
		}
		wiperSwitch.setOnChangeListener(new OnChangeListener(){
			@Override
			public void onChange(SwitchButton sb, boolean state) {
				
				if(state){
					isShare =1;
				}else{
					isShare =0;
				}
				UpdateUserIsShareTask upt =new UpdateUserIsShareTask();
				upt.execute(new String[]{isShare+""});
			}
			
		});
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch(id){
			case R.id.fragment_settingfragment_managerAccount_rl:
				this.startActivity(new Intent(this.getActivity(),PersonalCenterActivity.class));
				break;
			case R.id.fragment_settingfragment_aboutus_rl:
				break;
			case R.id.fragment_settingfragment_update_rl:
				break;
			case R.id.fragment_settingfragment_mylocation_rl:
				break;
			case R.id.fragment_settingfragment_exit_btn:
				Intent intent = new Intent(this.getActivity(),LoginActivity.class); 
				startActivity(intent); 
				save.setUb(null);
				save.setCookie(null);
				save.setIsrun(false);
				break;
			case R.id.fragment_settingfragment_exitprogram_btn:
				/*Intent intent01 = new Intent(Intent.ACTION_MAIN);  
  	            intent01.addCategory(Intent.CATEGORY_HOME);  
  	            intent01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
  	            startActivity(intent01); */ 
				save.setUb(null);
				save.setCookie(null);
				save.setIsrun(false);
				ExitApplication.getInstance().exit();

				break;
		}
	}

	// 申请加入该群
			public class UpdateUserIsShareTask extends AsyncTask<String, Integer, String> {
				String groupId = null;
				@Override
				protected String doInBackground(String... arg0) {
					String PATH = "http://" + MyUtil.getIP()
							+ ":8080/MyWorldService/UpdateUserIsShareServlet";
					String result = "0";
					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost post = new HttpPost(PATH);
						List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
						if(null != save.getCookie()){  
							 post.setHeader("Cookie", "sessionId=" + save.getCookie().getValue());  
				            }  
						params.add(new BasicNameValuePair("isShare", arg0[0]));
						HttpEntity formEntity = new UrlEncodedFormEntity(params,
								HTTP.UTF_8);
						post.setEntity(formEntity);
						HttpResponse response = httpclient.execute(post);
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							InputStream is = response.getEntity().getContent();
							result = MyUtil.inStreamToString(is);
							System.out.println("result1:" + result);
						}
					} catch (Exception e) {
					}
					return result;
				}

				@Override
				protected void onPostExecute(String result) {
					if("-1".equalsIgnoreCase(result)){
						Toast.makeText(SettingFragment.this.getActivity().getApplicationContext(), "你还未登陆！！!", Toast.LENGTH_SHORT)
						.show();
					}else if("1".equalsIgnoreCase(result)){
						UserBean ub = save.getUb();
						ub.setIsShare(isShare);
						save.setUb(ub);
						Toast.makeText(SettingFragment.this.getActivity().getApplicationContext(), "发送成功", Toast.LENGTH_SHORT)
						.show();
					}else if("0".equalsIgnoreCase(result)){
						Toast.makeText(SettingFragment.this.getActivity().getApplicationContext(), "发送失败！！！", Toast.LENGTH_SHORT)
						.show();
					}
				}
			}
	
}
