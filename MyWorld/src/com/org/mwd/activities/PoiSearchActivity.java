package com.org.mwd.activities;


import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.org.mwd.util.ExitApplication;
import com.org.mwd.vo.PoiResultBean;
import com.org.mwd.vo.SaveBean;
import com.org.myworld.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PoiSearchActivity extends Activity implements OnGetPoiSearchResultListener, OnClickListener{
	private PoiSearch mPoiSearch = null;
	private Button search_bt,nearby_friend_bt;
	private EditText searchkey_et = null;
	PoiResultBean mPoiResult = new PoiResultBean();
	SaveBean save = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_poi_search);
		ExitApplication.getInstance().addActivity(this);
		searchkey_et = (EditText) findViewById(R.id.activity_poi_search_et);
		search_bt = (Button) findViewById(R.id.activity_poi_search_bt);
		nearby_friend_bt = (Button) findViewById(R.id.activity_poi_nearby_friend_bt);
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		nearby_friend_bt.setOnClickListener(this);
		search_bt.setOnClickListener(this);
		save= (SaveBean) this.getApplicationContext();
		
	}
	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mPoiResult.setmPoiResult(result);
			/*Bundle bundle = new Bundle();
			bundle.putSerializable("poisearch",mPoiResult);
			bundle.putString("fragment", "add_poiresult");
			
			main_intent.putExtras(bundle);*/
			Intent main_intent = new Intent(PoiSearchActivity.this,MainActivity.class);
			SaveBean save = (SaveBean) this.getApplicationContext();
			save.setmPoiResult(result);
			main_intent.putExtra("fragment", "add_poiresult");
			PoiSearchActivity.this.startActivity(main_intent);
			return;
		}
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.activity_poi_search_bt:
			String key = searchkey_et.getText().toString();
			mPoiSearch.searchInCity((new PoiCitySearchOption()).keyword(key).city("广州"));
			break;
		case R.id.activity_poi_nearby_friend_bt:
			if(save.getUb() != null){
				Intent intent = new Intent(PoiSearchActivity.this,MainActivity.class);
				intent.putExtra("fragment", "nearby_friend");
				PoiSearchActivity.this.startActivity(intent);
			}else{
				Toast.makeText(
						this,"请先登录！！！！", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		
	}

}
