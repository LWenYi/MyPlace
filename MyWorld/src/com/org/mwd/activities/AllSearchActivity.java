package com.org.mwd.activities;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.org.mwd.util.ExitApplication;
import com.org.myworld.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AllSearchActivity extends Activity implements OnClickListener,
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
		OnGetRoutePlanResultListener {
	/**
	 * PoiSearch
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// 搜索标记
	boolean flag_01 = false;
	boolean flag_02 = false;

	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	// 关键字输入窗口
	private AutoCompleteTextView searchkey = null;
	private ArrayAdapter<String> sugAdapter = null;
	String key = null;// 搜索关键字
	// infowindow
	private InfoWindow minfoWindow;
	private Button gohere;
	private LatLng latlng;
	private LinearLayout routesearch_ll;
	// 所以按钮
	private Button back;
	private Button search;
	private Button bus;
	private Button drive;
	private Button walk;
	private Button clean;
	/**
	 * 路线搜索
	 */
	RoutePlanSearch mRPSearch = null;
	PlanNode startNode;
	PlanNode endNode;

	private AutoCompleteTextView start;
	private AutoCompleteTextView end;
	private String start_str;
	private String end_str;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener ;
	private LatLng mLatlng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_all_search);
		ExitApplication.getInstance().addActivity(this);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmap);
		mBaiduMap = mMapView.getMap();
		/**
		 * 设置地图中心点
		 */
		LatLng center = new LatLng(23.0977530000, 113.2886510000);
		MapStatus mMapStatus = new MapStatus.Builder().target(center).zoom(12)
				.build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(u);
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);

		// 获取所有按钮
		back = (Button) findViewById(R.id.activity_all_search_back_bt);
		search = (Button) findViewById(R.id.activity_all_search_search_bt);
		bus = (Button) findViewById(R.id.activity_all_search_bus_bt);
		drive = (Button) findViewById(R.id.activity_all_search_drive_bt);
		walk = (Button) findViewById(R.id.activity_all_search_walk_bt);
		clean = (Button) findViewById(R.id.activity_all_search_clean_bt);
		// 添加按钮点击事件
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		bus.setOnClickListener(this);
		drive.setOnClickListener(this);
		walk.setOnClickListener(this);
		clean.setOnClickListener(this);
		// 隐藏按钮
		bus.setVisibility(View.INVISIBLE);
		drive.setVisibility(View.INVISIBLE);
		walk.setVisibility(View.INVISIBLE);
		clean.setVisibility(View.INVISIBLE);
		// 获取路线搜索布局
		routesearch_ll = (LinearLayout) findViewById(R.id.activity_all_search_searchcontent_ll);

		searchkey = (AutoCompleteTextView) findViewById(R.id.activity_all_search_searchkey);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		searchkey.setAdapter(sugAdapter);

		searchkey.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				routesearch_ll.setVisibility(View.INVISIBLE);// 隐藏路线搜索布局
				flag_01 = false;
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city("广州"));
			}

		});

		searchkey.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				key = arg0.getItemAtPosition(arg2).toString();// 获取搜索关键字
				System.out.println("搜索啦啦啦啦啦啦fgdshdhgjgf" + key);

			}

		});
		// 初始化搜索模块，注册事件监听
		mRPSearch = RoutePlanSearch.newInstance();
		mRPSearch.setOnGetRoutePlanResultListener(this);
		// 获取搜索起点和终点
		start = (AutoCompleteTextView) findViewById(R.id.activity_all_search_start_et);
		end = (AutoCompleteTextView) findViewById(R.id.activity_all_search_end_et);
		start.setAdapter(sugAdapter);
		end.setAdapter(sugAdapter);
		start.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city("广州"));
			}

		});
		start.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				start_str = arg0.getItemAtPosition(arg2).toString();
				startNode = PlanNode.withCityNameAndPlaceName("广州", start_str);
				System.out.println("搜索啦啦啦啦啦啦起点" + start_str);

			}

		});
		end.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city("广州"));
			}

		});
		end.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				end_str = arg0.getItemAtPosition(arg2).toString();
				endNode = PlanNode.withCityNameAndPlaceName("广州", end_str);
				System.out.println("搜索啦啦啦啦啦啦终点" + end_str);

			}

		});
		/**
		 * 添加地图的单击事件，隐藏出现的详细信息布局和InfoWindow
		 */
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
		});
		//启动定位
		Location();
	}

	/**
	 * 定位初始化
	 */
	public void Location() {
		// 地图初始化
		// mBaiduMap = mMapView.getMap();
		myListener = new MyLocationListenner();
		// 开启定位地图
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		// 定位初始化
		LocationClientOption options = new LocationClientOption();
		options.setOpenGps(true);
		options.setCoorType("bd09ll");
		options.setScanSpan(600000);
		//
		mLocClient.setLocOption(options);
		mLocClient.start();
		Log.d("error", "this method had done!");
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (mLocClient == null || mBaiduMap == null) {
				return;
			}
			mLatlng = new LatLng(location.getLatitude(),location.getLongitude());
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.activity_all_search_back_bt:
			Intent main_intent = new Intent(AllSearchActivity.this,
					MainActivity.class);
			AllSearchActivity.this.startActivity(main_intent);
			break;
		case R.id.activity_all_search_search_bt:
			if (startNode != null || endNode != null) {
				flag_01 = true;
				mBaiduMap.clear();
				mMapView.setVisibility(View.VISIBLE);
				bus.setVisibility(View.VISIBLE);
				drive.setVisibility(View.VISIBLE);
				walk.setVisibility(View.VISIBLE);
				clean.setVisibility(View.VISIBLE);
				mRPSearch.transitSearch((new TransitRoutePlanOption())
						.from(startNode).city("广州").to(endNode));
			} else if (!flag_01) {
				if (key != null || key != "") {
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.keyword(key).city("广州"));
					System.out.println("搜索啦啦啦啦啦啦" + key);
				} else {
					Toast.makeText(AllSearchActivity.this, "请输入搜索关键字",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.poi_infowindow_gohere:
			mBaiduMap.clear();
			if(mLatlng != null){
				startNode = PlanNode.withLocation(mLatlng);
				endNode = PlanNode.withLocation(latlng);
				mMapView.setVisibility(View.VISIBLE);
				bus.setVisibility(View.VISIBLE);
				drive.setVisibility(View.VISIBLE);
				walk.setVisibility(View.VISIBLE);
				clean.setVisibility(View.VISIBLE);
				flag_02 = true;
				mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(
						startNode).to(endNode));
			}else{
				Toast.makeText(AllSearchActivity.this, "无法获取当前位置", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.activity_all_search_bus_bt:
			mBaiduMap.clear();

			mRPSearch.transitSearch((new TransitRoutePlanOption())
					.from(startNode).city("广州").to(endNode));

			break;
		case R.id.activity_all_search_drive_bt:
			mBaiduMap.clear();

			mRPSearch.drivingSearch((new DrivingRoutePlanOption()).from(
					startNode).to(endNode));

			break;
		case R.id.activity_all_search_walk_bt:
			mBaiduMap.clear();
			mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(
					startNode).to(endNode));
			break;
		case R.id.activity_all_search_clean_bt:

			if (flag_01) {
				mBaiduMap.clear();
				mMapView.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.INVISIBLE);
				bus.setVisibility(View.INVISIBLE);
				drive.setVisibility(View.INVISIBLE);
				walk.setVisibility(View.INVISIBLE);
				start.setText("");
				end.setText("");
				startNode = null;
				endNode = null;
				flag_01 = false;
			} else if (!flag_01) {
				mBaiduMap.clear();
				mMapView.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.INVISIBLE);
				routesearch_ll.setVisibility(View.VISIBLE);
				searchkey.setText("");
			}
			if (flag_02) {
				mBaiduMap.clear();
				mMapView.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.INVISIBLE);
				bus.setVisibility(View.INVISIBLE);
				drive.setVisibility(View.INVISIBLE);
				walk.setVisibility(View.INVISIBLE);
				startNode = null;
				endNode = null;
				flag_02 = false;
			}
			break;
		}

	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(AllSearchActivity.this, "抱歉，未找到结果，请检查网络",
					Toast.LENGTH_SHORT).show();
		} else {
			View v;
			TextView poi_name;
			TextView poi_address;
			v = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.poi_infowimdow, null);
			poi_name = (TextView) v.findViewById(R.id.poi_infowindow_name);
			poi_address = (TextView) v
					.findViewById(R.id.poi_infowindow_address);
			poi_name.setText(result.getName());
			poi_address.setText(result.getAddress());
			gohere = (Button) v.findViewById(R.id.poi_infowindow_gohere);
			gohere.setOnClickListener(AllSearchActivity.this);
			latlng = new LatLng(result.getLocation().latitude,
					result.getLocation().longitude);
			minfoWindow = new InfoWindow(v, latlng, 0);
			mBaiduMap.showInfoWindow(minfoWindow);
			Toast.makeText(AllSearchActivity.this,
					result.getName() + ": " + result.getAddress(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			mMapView.setVisibility(View.VISIBLE);
			clean.setVisibility(View.VISIBLE);
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			System.out.println("有结果啊有结果!");
			return;
		}

	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));
			// }
			return true;
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(AllSearchActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(AllSearchActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(AllSearchActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {

			WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		mRPSearch.destroy();
		mMapView.onDestroy();
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView = null;

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}
}
