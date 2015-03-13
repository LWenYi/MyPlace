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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;

import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
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
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.mwd.activities.FriendTalkActivity;
import com.org.mwd.activities.GroupTalkActivity;
import com.org.mwd.adapter.GPSInfoAdapter;
import com.org.mwd.util.MyGeoCoder;
import com.org.mwd.util.MyUtil;
import com.org.mwd.vo.FriendBean;
import com.org.mwd.vo.GPS;
import com.org.mwd.vo.GPSBean;
import com.org.mwd.vo.GroupBean;
import com.org.mwd.vo.SaveBean;

import com.org.myworld.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MapFragment extends Fragment implements
		OnGetPoiSearchResultListener, OnClickListener,
		OnGetRoutePlanResultListener, OnGetSuggestionResultListener {
	MapView mMapView = null;
	/**
	 * 列出所有好友及群组
	 */
	/**
	 * 百度定位
	 */
	private View view;
	private LocationClient mlocClient;
	private BDLocationListener mylistener = null;
	private BaiduMap mBaiduMap;
	private SaveBean save = null;
	private GPSBean mygps = new GPSBean();
	private LatLng mLatLng = null;
	private GPS mgps = null;
	private List<GPS> gpsList;
	private int friendId = 0;
	/**
	 * 点击搜索
	 */
	private EditText searchkey_et = null;// 点击触发搜索功能
	/**
	 * 好友当前地理位置
	 */
	LatLng fLatLng = null;
	// popupwindow的点击按钮
	private ImageView call_bt;
	private ImageView sendmessage_bt;
	private ImageView gohere_bt;
	private String phonenum;
	private int userId = 0;
	private int groupId = 0;
	// 切换按钮
	private Button bus;
	private Button drive;
	private Button walk;
	private Button clean;
	private Button more;
	// routeplant搜索相关
	RoutePlanSearch mRPSearch = null;
	// 浏览路线节点相关
	private  OverlayManager routeOverlay = null;
	private WalkingRouteOverlay walkOverlay = null;
	private PlanNode startNode;
	private PlanNode endNode;
	private ListView gpsInfo_lv;
	private GPSInfoAdapter adapter;
	private RelativeLayout fragment_map_open_rl, fragment_map_close_rl,
			groupmember_list_rl, search, compass, follow, normal;
	// popupwindow下拉菜单
	private PopupWindow ppv;

	/**
	 * 所有搜索功能
	 */
	private Button poi_bakc_bt;
	private LinearLayout forclick_ll;
	private LinearLayout all_search_ll;// 所有搜索布局
	// 搜索标记
	boolean flag_01 = false;
	boolean flag_02 = false;
	// Poi搜索
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	// 关键字输入窗口
	private AutoCompleteTextView searchkey = null;
	private ArrayAdapter<String> sugAdapter = null;
	private EditText poi_keyword;
	String key = null;// 搜索关键字
	private Button poi_routeplant_search_bt;
	private InfoWindow poi_infoWindow;
	private Button poi_gohere;
	private LatLng poi_latlng;
	// routeplant搜索
	private AutoCompleteTextView start_atv;
	private AutoCompleteTextView end_atv;
	private String start_str;
	private String end_str;

	@SuppressLint("CutPasteId")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SDKInitializer.initialize(this.getActivity().getApplicationContext());
		view = inflater.inflate(R.layout.fragment_map, container, false);
		save = (SaveBean) this.getActivity().getApplicationContext();
		gpsList = new ArrayList<GPS>();
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		/**
		 * 设置地图中心点
		 */
		LatLng center = new LatLng(23.0977530000, 113.2886510000);
		MapStatus mMapStatus = new MapStatus.Builder().target(center).zoom(12)
				.build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(u);
		/**
		 * 调用定位方法
		 */
		Location();
		/**
		 * 百度地图添加marker点击事件
		 */
		OnMarkerClickListener myOnMarkerClickListener = new MyOnMarkerClickListener();
		mBaiduMap.setOnMarkerClickListener(myOnMarkerClickListener);
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
		forclick_ll = (LinearLayout) view
				.findViewById(R.id.fragment_map_forclick_ll);
		all_search_ll = (LinearLayout) view
				.findViewById(R.id.fragment_map_all_Search_ll);
		searchkey_et = (EditText) view
				.findViewById(R.id.fragment_map_searchsign_tv);
		searchkey_et.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				all_search_ll.setVisibility(View.VISIBLE);
				forclick_ll.setVisibility(View.GONE);
				bus.setVisibility(View.INVISIBLE);
				drive.setVisibility(View.INVISIBLE);
				walk.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.INVISIBLE);
				mBaiduMap.clear();
			}
		});
		fragment_map_open_rl = (RelativeLayout) view
				.findViewById(R.id.fragment_map_open_rl);
		fragment_map_open_rl.setOnClickListener(this);

		fragment_map_close_rl = (RelativeLayout) view
				.findViewById(R.id.fragment_map_close_rl);
		fragment_map_close_rl.setOnClickListener(this);

		groupmember_list_rl = (RelativeLayout) view
				.findViewById(R.id.fragment_map_groupmember_list_rl);
		/**
		 * Poi搜索
		 */
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		poi_keyword = (EditText) view
				.findViewById(R.id.fragment_map_all_search_searchkey);
		searchkey = (AutoCompleteTextView) view
				.findViewById(R.id.fragment_map_all_search_searchkey);
		sugAdapter = new ArrayAdapter<String>(MapFragment.this.getActivity()
				.getApplicationContext(),
				R.layout.activity_login_complete_textview_item);
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
				flag_01 = false;
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city("广州"));
			}

		});
		poi_bakc_bt = (Button) view
				.findViewById(R.id.fragment_map_all_search_back_bt);
		poi_bakc_bt.setOnClickListener(this);
		/**
		 * routeplan搜索
		 */
		start_atv = (AutoCompleteTextView) view
				.findViewById(R.id.fragment_map_all_search_start_atv);
		end_atv = (AutoCompleteTextView) view
				.findViewById(R.id.fragment_map_all_search_end_atv);
		start_atv.setAdapter(sugAdapter);
		end_atv.setAdapter(sugAdapter);
		start_atv.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				EditText start_et = (EditText) view
						.findViewById(R.id.fragment_map_all_search_start_atv);
				start_str = start_et.getText().toString();
				startNode = PlanNode.withCityNameAndPlaceName("广州", start_str);
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city("广州"));
			}
		});
		end_atv.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				EditText end_et = (EditText) view
						.findViewById(R.id.fragment_map_all_search_end_atv);
				end_str = end_et.getText().toString();
				endNode = PlanNode.withCityNameAndPlaceName("广州", end_str);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city("广州"));
			}
		});
		poi_routeplant_search_bt = (Button) view
				.findViewById(R.id.fragment_map_all_search_search_bt);
		poi_routeplant_search_bt.setOnClickListener(this);
		// 初始化控件
		bus = (Button) view.findViewById(R.id.fragment_map_bus_bt);
		drive = (Button) view.findViewById(R.id.fragment_map_drive_bt);
		walk = (Button) view.findViewById(R.id.fragment_map_walk_bt);
		clean = (Button) view.findViewById(R.id.fragment_map_clean_bt);
		more = (Button) view.findViewById(R.id.fragment_map_routesearch_bt);
		gpsInfo_lv = (ListView) view
				.findViewById(R.id.fragment_map_groupmember_lv);
		gpsInfo_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				GPS gps = gpsList.get(position);
				// 把好友位置设为中心点
				LatLng friend_center = new LatLng(Double.parseDouble(gps
						.getLatitude()), Double.parseDouble(gps.getLongitude()));
				MapStatus mMapStatus = new MapStatus.Builder().target(
						friend_center).build();
				MapStatusUpdate u = MapStatusUpdateFactory
						.newMapStatus(mMapStatus);
				mBaiduMap.setMapStatus(u);
				showInfoWindow(gps); //弹出信息窗口		
			}
		});
		bus.setOnClickListener(this);
		drive.setOnClickListener(this);
		walk.setOnClickListener(this);
		clean.setOnClickListener(this);
		more.setOnClickListener(this);

		bus.setVisibility(View.INVISIBLE);
		drive.setVisibility(View.INVISIBLE);
		walk.setVisibility(View.INVISIBLE);
		clean.setVisibility(View.INVISIBLE);
		// 初始化搜索模块，注册事件监听
		mRPSearch = RoutePlanSearch.newInstance();
		mRPSearch.setOnGetRoutePlanResultListener(this);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Bundle bundle = getArguments();
		System.out.println("++++bundle:" + bundle);
		if (bundle != null) {
			String what = bundle.getString("what");
			if ("friend_location".equalsIgnoreCase(what)) { // 定位好友位置
				friendId = bundle.getInt("friendId");
				GetUserGPSInfoTask ggps = new GetUserGPSInfoTask();
				ggps.execute(new String[] { String.valueOf(friendId) });
			} else if ("group_location".equalsIgnoreCase(what)) { // 定位群所有成员位置
				if (gpsList != null) {
					gpsList.clear();
				}
				int groupId = bundle.getInt("groupId");
				GetGroupGPSInfoTask gggps = new GetGroupGPSInfoTask();
				gggps.execute(new String[] { String.valueOf(groupId) });
			}
		}
	}
	/**
	 * 定位初始化
	 */
	public void Location() {
		// 地图初始化
		// mBaiduMap = mMapView.getMap();
		mylistener = new MyLocationListener();
		// 开启定位地图
		mBaiduMap.setMyLocationEnabled(true);
		mlocClient = new LocationClient(this.getActivity()
				.getApplicationContext());
		mlocClient.registerLocationListener(mylistener);
		// 定位初始化
		LocationClientOption options = new LocationClientOption();
		options.setOpenGps(true);
		options.setCoorType("bd09ll");
		options.setScanSpan(600000);
		mlocClient.setLocOption(options);
		mlocClient.start();
		Log.d("error", "this method had done!");
	}
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (mlocClient == null || mBaiduMap == null) {
				return;
			}
			mlocClient.requestLocation();
			MyLocationData data = new MyLocationData.Builder()
					.accuracy(location.getRadius()).direction(100)
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(data);
			mLatLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			/**
			 * 保存个人GPS信息
			 */
			String la = String.valueOf(location.getLatitude());
			String lo = String.valueOf(location.getLongitude());
			if ("4.9E-324".equalsIgnoreCase(la) || "4.9E-324".equals(lo)) {
				la = "23.0977530000";
				lo = "113.2886510000";
			}
			mygps.setLatitude(la);
			mygps.setLongitude(lo);
			// 添加个人定位头像图标

			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLatLng);
			try {
				mBaiduMap.animateMapStatus(u);
			} catch (Exception e) {
				System.out.println("----------------null:MapStatusUpdate----------------------");
			}
			if (null != save.getCookie()) {
				SaveUserGPSInfoTask st = new SaveUserGPSInfoTask();
				st.execute();
			}
		}
		@Override
		public void onReceivePoi(BDLocation arg0) {
		}
	}
	/**
	 * 添加头好友位置信息
	 */
	public class MyOnMarkerClickListener implements OnMarkerClickListener,
			OnClickListener {
		@Override
		public boolean onMarkerClick(final Marker marker) {
			// 获取marker中的数据
			mgps = (GPS) marker.getExtraInfo().get("gps");
			showInfoWindow(mgps);
			return true;
		}
		@Override
		public void onClick(View arg0) {
		}
	}
	/**
	 * 添加好友位置图标，定位图标
	 */
	public void addLocationMarker(GPS gps) {
		LatLng ll_marker = null;
		OverlayOptions over = null;
		Marker locMarker = null;
		/**
		 * 好友位置图标,头像图标
		 */
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.fragment_map_marker, null);
		TextView nick = (TextView) view
				.findViewById(R.id.fragment_map_marker_nick);
		ImageView icon = (ImageView) view
				.findViewById(R.id.fragment_map_marker_icon);
		if (gps.getImage() != null && !gps.getImage().equalsIgnoreCase("null")) {
			System.out.println("cccccccccccccccccccccccccccccc="
					+ gps.getImage());
			byte[] imgByte = MyUtil.hex2byte(gps.getImage());
			Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
					imgByte.length, null);
			new BitmapDescriptorFactory();
			icon.setImageBitmap(bitmap);

		} else {
			icon.setImageResource(R.drawable.default_userphoto);
		}
		nick.setText(gps.getNick());
		BitmapDescriptor locMarker_icon = BitmapDescriptorFactory
				.fromView(view);
		ll_marker = new LatLng((Double.parseDouble(gps.getLatitude())),
				(Double.parseDouble(gps.getLongitude())));
		Bundle bundle = new Bundle();
		bundle.putSerializable("gps", gps);
		over = new MarkerOptions().position(ll_marker).icon(locMarker_icon)
				.zIndex(9).extraInfo(bundle);

		locMarker = (Marker) (mBaiduMap.addOverlay(over));
		locMarker.setExtraInfo(bundle);

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll_marker);
		mBaiduMap.setMapStatus(u);

	}
	/**
	 * 弹出用户坐标相关信息窗口
	 */
	public void showInfoWindow(GPS mgps) {
		View infoView;
		TextView friend_address, distance_tv, friend_nick;
		infoView = LayoutInflater.from(
				getActivity().getApplicationContext()).inflate(
				R.layout.fragment_map_location, null);
		friend_nick = (TextView) infoView
				.findViewById(R.id.fragment_map_location_nick_tv);
		friend_address = (TextView) infoView
				.findViewById(R.id.fragment_map_location_address_tv);
		distance_tv = (TextView) infoView
				.findViewById(R.id.fragment_map_location_distance_tv);
		gohere_bt = (ImageView) infoView
				.findViewById(R.id.fragment_map_location_gohere_iv);
		call_bt = (ImageView) infoView
				.findViewById(R.id.fragment_map_location_call_iv);
		sendmessage_bt = (ImageView) infoView
				.findViewById(R.id.fragment_map_location_sendmessage_iv);
		gohere_bt.setOnClickListener(MapFragment.this);
		call_bt.setOnClickListener(MapFragment.this);
		sendmessage_bt.setOnClickListener(MapFragment.this);
		if(mgps.getUserId() == save.getUb().getId()){
			gohere_bt.setVisibility(8);
			call_bt.setVisibility(8);
			sendmessage_bt.setVisibility(8);
		}else{
			gohere_bt.setVisibility(0);
			call_bt.setVisibility(0);
			sendmessage_bt.setVisibility(0);
		}
		// 将marker的经纬度信息转化为地图上的坐标
		//final LatLng ll = marker.getPosition();
		LatLng ll = new LatLng(Double.parseDouble(mgps.getLatitude()),
				Double.parseDouble(mgps
						.getLongitude()));
		double distance = DistanceUtil.getDistance(mLatLng, ll);
		if (distance >= 1000) {
			int km = ((int) distance) / 1000;
			distance_tv.setText("相距：" + km + "公里");
		} else if (distance >= 0) {
			int km = (int) distance;
			distance_tv.setText("相距：" + km + "米");
		} else {
			distance_tv.setText("相距：不详");
		}
		friend_address.setText("地址："+save.getAddress().get(mgps.getUserId()));
		friend_nick.setText("昵称： " + mgps.getNick());
		Point p = mBaiduMap.getProjection().toScreenLocation(ll);
		p.y -= 47;
		LatLng markerll = mBaiduMap.getProjection().fromScreenLocation(p);
		InfoWindow mInfoWindow = new InfoWindow(infoView, markerll, 0);
		// 保存用户当前infowindow群ID或userID
		if (0 != mgps.getGroupId()) {
			groupId = mgps.getGroupId();
			userId = 0;
		} else if (0 != mgps.getUserId()) {
			userId = mgps.getUserId();
			groupId = 0;
		}
		// 显示InfoWindow
		mBaiduMap.showInfoWindow(mInfoWindow);
		phonenum = mgps.getPhone();
		fLatLng = new LatLng(Double.parseDouble(mgps.getLatitude()),
				Double.parseDouble(mgps.getLongitude()));
		// 设置起终点信息
		startNode = PlanNode.withLocation(mLatLng);
		endNode = PlanNode.withLocation(fLatLng);
	}
	public void onActivityCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 退出时销毁定位
		mlocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 注销所有传感器的监听
		mMapView.onPause();
	}
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.fragment_map_location_call_iv: // 打电话
			if (phonenum != null && phonenum != "") {
				Uri uri = Uri.parse("tel:" + phonenum);
				Intent call_intent = new Intent(Intent.ACTION_CALL, uri);
				MapFragment.this.startActivity(call_intent);
			}

			break;
		case R.id.fragment_map_location_sendmessage_iv:
			if (0 != groupId) {
				List<GroupBean> list = save.getGroupList();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == groupId) {
						Intent intent = new Intent();
						intent.setClass(MapFragment.this.getActivity(),
								GroupTalkActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("groupInfo", list.get(i));
						intent.putExtras(bundle);
						intent.putExtra("position", String.valueOf(i));
						MapFragment.this.getActivity().startActivity(intent);
					}
				}
			}
			if (0 != userId) {
				List<FriendBean> list = save.getFriendList();
				int len = list.size();
				for (int i = 0; i < len; i++) {
					if (list.get(i).getFriendId() == userId) {
						Intent intent = new Intent();
						intent.setClass(MapFragment.this.getActivity(),
								FriendTalkActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("friendInfo", list.get(i));
						intent.putExtras(bundle);
						MapFragment.this.getActivity().startActivity(intent);
					}
				}
			}
			break;
		case R.id.fragment_map_location_gohere_iv: // 规划与好友之间的线路
			// mBaiduMap.clear();
			mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(
					startNode).to(endNode));
			bus.setVisibility(View.VISIBLE);
			drive.setVisibility(View.VISIBLE);
			walk.setVisibility(View.VISIBLE);
			clean.setVisibility(View.VISIBLE);
			flag_02 = true;
			break;
		case R.id.fragment_map_bus_bt: // 公交路线
			if (flag_02) {
				mBaiduMap.clear();
				mRPSearch.transitSearch((new TransitRoutePlanOption())
						.from(startNode).city("广州").to(endNode));
				if (null != gpsList && gpsList.size() > 0) {
					for (int i = 0; i < gpsList.size(); i++) {
						addLocationMarker(gpsList.get(i));
					}
				}
			} else {
				mBaiduMap.clear();
				mRPSearch.transitSearch((new TransitRoutePlanOption())
						.from(startNode).city("广州").to(endNode));
			}

			break;
		case R.id.fragment_map_drive_bt: // 驾车路线
			if (flag_02) {
				mBaiduMap.clear();
				mRPSearch.drivingSearch((new DrivingRoutePlanOption()).from(
						startNode).to(endNode));
				if (null != gpsList && gpsList.size() > 0) {
					for (int i = 0; i < gpsList.size(); i++) {
						addLocationMarker(gpsList.get(i));
					}
				}
			} else {
				mBaiduMap.clear();
				mRPSearch.drivingSearch((new DrivingRoutePlanOption()).from(
						startNode).to(endNode));
			}

			break;
		case R.id.fragment_map_walk_bt: // 步行路线
			if (flag_02) {
				mBaiduMap.clear();
				mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(
						startNode).to(endNode));
				if (null != gpsList && gpsList.size() > 0) {
					for (int i = 0; i < gpsList.size(); i++) {
						addLocationMarker(gpsList.get(i));
					}
				}

			} else {
				mBaiduMap.clear();
				mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(
						startNode).to(endNode));
			}

			break;
		case R.id.fragment_map_clean_bt: // 恢复原状
			if (null != gpsList && gpsList.size() > 0) {
				for (int i = 0; i < gpsList.size(); i++) {
					addLocationMarker(gpsList.get(i));
				}
			}
			if (flag_01) {
				clean.setVisibility(View.INVISIBLE);
				bus.setVisibility(View.INVISIBLE);
				drive.setVisibility(View.INVISIBLE);
				walk.setVisibility(View.INVISIBLE);
				mBaiduMap.clear();
				start_atv.setText("");
				end_atv.setText("");
				startNode = null;
				endNode = null;
				flag_01 = false;
			} else if (!flag_01) {
				mBaiduMap.clear();
				bus.setVisibility(View.INVISIBLE);
				drive.setVisibility(View.INVISIBLE);
				walk.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.INVISIBLE);
				searchkey.setText("");
			} else if (flag_02) {
				// 点击删除按钮，删除路线规划图，重画好友位置图标
				OnMarkerClickListener myOnMarkerClickListener = new MyOnMarkerClickListener();
				mBaiduMap.setOnMarkerClickListener(myOnMarkerClickListener);
				mBaiduMap.clear();
				bus.setVisibility(View.INVISIBLE);
				drive.setVisibility(View.INVISIBLE);
				walk.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.INVISIBLE);
				flag_02 = false;
			}
			break;
		case R.id.fragment_map_open_rl:
			groupmember_list_rl.setVisibility(0);
			// fragment_map_open_rl.setBackgroundColor(0xc0000000);
			fragment_map_close_rl.setVisibility(0);
			fragment_map_open_rl.setVisibility(8);
			break;
		case R.id.fragment_map_close_rl:
			groupmember_list_rl.setVisibility(8);
			fragment_map_close_rl.setVisibility(8);
			fragment_map_open_rl.setVisibility(0);
			break;

		case R.id.fragment_map_routesearch_bt:
			if (ppv != null && ppv.isShowing()) {
				ppv.dismiss();
			} else {
				View v = LayoutInflater.from(
						getActivity().getApplicationContext()).inflate(
						R.layout.more_popupwindow, null);
				compass = (RelativeLayout) v
						.findViewById(R.id.more_popupwindow_compass_rl);
				search = (RelativeLayout) v
						.findViewById(R.id.more_popupwindow_near_rl);
				follow = (RelativeLayout) v
						.findViewById(R.id.more_popupwindow_follow_rl);
				normal = (RelativeLayout) v
						.findViewById(R.id.more_popupwindow_normal_rl);

				compass.setOnClickListener(this);
				search.setOnClickListener(this);
				follow.setOnClickListener(this);
				normal.setOnClickListener(this);

				ppv = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				ppv.setBackgroundDrawable(getDrawable());// 获取背景背景

				ppv.setOutsideTouchable(true);// 设置允许在外点击消失
				ppv.update();// 刷新状态（必须刷新否则无效）
				ppv.setBackgroundDrawable(new BitmapDrawable());// 点击返回时，popupwindow消失
				ppv.showAsDropDown(more, 5, 5);

			}

			break;
		case R.id.more_popupwindow_near_rl:
			ppv.dismiss();
			if(null !=save.getUb()){
				mBaiduMap.clear();
				if (gpsList != null) {
					gpsList.clear();
				}
				getNearbyFriendGPSInfoTask gnfs = new getNearbyFriendGPSInfoTask();
				gnfs.execute();
			}else{
				Toast.makeText(getActivity(), "登陆后才能查找附近好友！！", Toast.LENGTH_SHORT)
				.show();
			}
			break;
		case R.id.more_popupwindow_compass_rl:
			ppv.dismiss();
			ppv.dismiss();
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					LocationMode.COMPASS, true, null));
			break;
		case R.id.more_popupwindow_follow_rl:
			ppv.dismiss();
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					LocationMode.FOLLOWING, true, null));
			break;
		case R.id.more_popupwindow_normal_rl:
			ppv.dismiss();
			Location();
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					LocationMode.NORMAL, true, null));
			break;
		case R.id.fragment_map_all_search_search_bt:
			if (startNode != null || endNode != null) {
				flag_01 = true;
				mBaiduMap.clear();
				mMapView.setVisibility(View.VISIBLE);
				bus.setVisibility(View.VISIBLE);
				drive.setVisibility(View.VISIBLE);
				walk.setVisibility(View.VISIBLE);
				clean.setVisibility(View.VISIBLE);
				mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(
						startNode).to(endNode));
				System.out.println(startNode + "nfkd" + endNode);
			} else if (!flag_01) {
				mBaiduMap.clear();
				clean.setVisibility(View.VISIBLE);
				key = poi_keyword.getText().toString();
				if (key != null || key != "") {
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.keyword(key).city("广州"));
					System.out.println("搜索啦啦啦啦啦啦" + key);
				} else {
					Toast.makeText(
							MapFragment.this.getActivity()
									.getApplicationContext(), "请输入搜索关键字",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.fragment_map_all_search_back_bt:
			mBaiduMap.clear();
			all_search_ll.setVisibility(View.GONE);
			forclick_ll.setVisibility(View.VISIBLE);
			bus.setVisibility(View.INVISIBLE);
			drive.setVisibility(View.INVISIBLE);
			walk.setVisibility(View.INVISIBLE);
			clean.setVisibility(View.INVISIBLE);
			break;
		case R.id.poi_infowindow_gohere:
			mBaiduMap.clear();
			if (mLatLng != null) {
				startNode = PlanNode.withLocation(mLatLng);
				endNode = PlanNode.withLocation(poi_latlng);
				mMapView.setVisibility(View.VISIBLE);
				bus.setVisibility(View.VISIBLE);
				drive.setVisibility(View.VISIBLE);
				walk.setVisibility(View.VISIBLE);
				clean.setVisibility(View.VISIBLE);
				flag_02 = true;
				mRPSearch.walkingSearch((new WalkingRoutePlanOption()).from(
						startNode).to(endNode));
			} else {
				Toast.makeText(
						MapFragment.this.getActivity().getApplicationContext(),
						"无法获取当前位置", Toast.LENGTH_SHORT).show();
			}

			break;
		}

	}

	/**
	 * 为popupwindow弹出菜单生成一个 透明的背景图片
	 * 
	 * @return
	 */
	private Drawable getDrawable() {
		ShapeDrawable bgdrawable = new ShapeDrawable(new OvalShape());
		bgdrawable.getPaint().setColor(
				MapFragment.this.getResources().getColor(
						android.R.color.transparent));
		return bgdrawable;
	}

	/**
	 * Poi搜索
	 */
	// 添加兴趣点覆盖物
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
			Toast.makeText(
					MapFragment.this.getActivity().getApplicationContext(),
					"抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		} else {
			View v;
			TextView poi_name;
			TextView poi_address;
			v = LayoutInflater.from(
					MapFragment.this.getActivity().getApplicationContext())
					.inflate(R.layout.poi_infowimdow, null);
			poi_name = (TextView) v.findViewById(R.id.poi_infowindow_name);
			poi_address = (TextView) v
					.findViewById(R.id.poi_infowindow_address);
			poi_name.setText(result.getName());
			poi_address.setText(result.getAddress());

			poi_gohere = (Button) v.findViewById(R.id.poi_infowindow_gohere);
			poi_gohere.setOnClickListener(MapFragment.this);

			poi_latlng = new LatLng(result.getLocation().latitude,
					result.getLocation().longitude);

			poi_infoWindow = new InfoWindow(v, poi_latlng, 0);
			mBaiduMap.showInfoWindow(poi_infoWindow);
			Toast.makeText(
					MapFragment.this.getActivity().getApplicationContext(),
					result.getName() + ": " + result.getAddress(),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(
					MapFragment.this.getActivity().getApplicationContext(),
					"抱歉，未找到结果,请输入正确的地点", Toast.LENGTH_SHORT).show();
			// clean.setVisibility(View.INVISIBLE);
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

	/**
	 * routeplant搜索
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(
					MapFragment.this.getActivity().getApplicationContext(),
					"抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			Toast.makeText(
					MapFragment.this.getActivity().getApplicationContext(),
					"抱", Toast.LENGTH_SHORT).show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(
					MapFragment.this.getActivity().getApplicationContext(),
					"抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		System.out.println("sdsdsdasdasdasda------------------");
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(
					MapFragment.this.getActivity().getApplicationContext(),
					"抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			walkOverlay = new WalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(walkOverlay);
			walkOverlay.setData(result.getRouteLines().get(0));
			walkOverlay.addToMap();
			walkOverlay.zoomToSpan();
		}

	}

	// 上传GPS的异步任务
	public class SaveUserGPSInfoTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/SaveUserGPSInfoServlet";
			System.out.println("path:" + PATH);
			String result = "failure";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				} else {
					return result;
				}
				params.add(new BasicNameValuePair("latitude", mygps
						.getLatitude()));
				params.add(new BasicNameValuePair("longitude", mygps
						.getLongitude()));
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
			if ("failure".equalsIgnoreCase(result)) {
			} else {

			}
		}
	}

	// 根据用户id获取GPS的异步任务
	public class GetUserGPSInfoTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/GetUserGPSInfoServlet";
			System.out.println("path:" + PATH);
			String result = "-1";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				} else {
					return result;
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("friendId", arg0[0]));
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
			if ("-1".equalsIgnoreCase(result)) { // 获取到好友的GPS信息
				Toast.makeText(getActivity(), "你还未登陆！！", Toast.LENGTH_SHORT)
				.show();
			} else if ("0".equalsIgnoreCase(result)) { // 获取到好友的GPS信息
				Toast.makeText(getActivity(), "查询GPS失败！", Toast.LENGTH_SHORT)
				.show();
			}else {
				Gson gson = new Gson();
				GPS gps = gson.fromJson(result, new TypeToken<GPS>() {
				}.getType());
				LatLng ll = new LatLng(Double.parseDouble(gps.getLatitude()),
						Double.parseDouble(gps.getLongitude()));
				@SuppressWarnings("unused")
				MyGeoCoder mGeocoder = new MyGeoCoder(ll, MapFragment.this
						.getActivity().getApplicationContext(), gps.getUserId());
				addLocationMarker(gps);
				gpsList.add(gps);
				GPSInfoAdapter madapter = new GPSInfoAdapter(MapFragment.this
						.getActivity().getApplicationContext(), gpsList);
				gpsInfo_lv.setAdapter(madapter);
				System.out.println(gps);
			}
		}
	}

	// 获取群成员GPS的异步任务
	public class GetGroupGPSInfoTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			String PATH = "http://" + MyUtil.getIP()
					+ ":8080/MyWorldService/GetGroupGPSInfoServlet";
			System.out.println("path:" + PATH);
			String result = "-1";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(PATH);
				if (null != save.getCookie()) {
					post.setHeader("Cookie", "sessionId="
							+ save.getCookie().getValue());
				} else {
					return result;
				}
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("groupId", arg0[0]));
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
					System.out.println("gps:" + result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if ("-1".equalsIgnoreCase(result)) {
				Toast.makeText(getActivity(), "你还未登陆！！", Toast.LENGTH_SHORT)
						.show();
			} else{
				Gson gson = new Gson();
				gpsList = gson.fromJson(result, new TypeToken<List<GPS>>() {
				}.getType());
				if (null != gpsList && gpsList.size() > 0) {
					groupmember_list_rl.setVisibility(8);
					fragment_map_close_rl.setVisibility(8);
					fragment_map_open_rl.setVisibility(0);
					GPSInfoAdapter madapter = new GPSInfoAdapter(
							MapFragment.this.getActivity()
									.getApplicationContext(), gpsList);
					gpsInfo_lv.setAdapter(madapter);
					for (int i = 0; i < gpsList.size(); i++) {
						
						addLocationMarker(gpsList.get(i));
						LatLng ll = new LatLng(Double.parseDouble(gpsList
								.get(i).getLatitude()),
								Double.parseDouble(gpsList.get(i)
										.getLongitude()));
						@SuppressWarnings("unused")
						MyGeoCoder mGeocoder = new MyGeoCoder(ll,
								MapFragment.this.getActivity()
										.getApplicationContext(), gpsList
										.get(i).getUserId());
						if(gpsList.get(i).getUserId() == save.getUb().getId()){
						}else{
						}
					
					}
				} else {
					Toast.makeText(getActivity(), "没有群成员的GPS信息",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	// 获取附近好友GPS的异步任务
		public class getNearbyFriendGPSInfoTask extends
				AsyncTask<String, Integer, String> {
			@Override
			protected String doInBackground(String... arg0) {
				String PATH = "http://" + MyUtil.getIP()
						+ ":8080/MyWorldService/GetNearbyFriendGPSInfoServlet";
				System.out.println("path:" + PATH);
				String result = "-1";
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost post = new HttpPost(PATH);
					List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
					if (null != save.getCookie()) {
						post.setHeader("Cookie", "sessionId="
								+ save.getCookie().getValue());
					} else {
						return result;
					}
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
						System.out.println("gps:" + result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				if ("-1".equalsIgnoreCase(result)) {
					Toast.makeText(getActivity(), "你还未登陆！！", Toast.LENGTH_SHORT)
					.show();
				} else {
					Gson gson = new Gson();
					gpsList = gson.fromJson(result, new TypeToken<List<GPS>>() {
					}.getType());
					groupmember_list_rl.setVisibility(8);
					fragment_map_close_rl.setVisibility(8);
					fragment_map_open_rl.setVisibility(0);
					adapter = new GPSInfoAdapter(MapFragment.this.getActivity()
							.getApplicationContext(), gpsList);
					gpsInfo_lv.setAdapter(adapter);
					for (int i = 0; i < gpsList.size(); i++) {
						LatLng ll = new LatLng(Double.parseDouble(gpsList.get(i)
								.getLatitude()), Double.parseDouble(gpsList.get(i)
								.getLongitude()));
						@SuppressWarnings("unused")
						MyGeoCoder mGeocoder = new MyGeoCoder(ll, MapFragment.this
								.getActivity().getApplicationContext(), gpsList
								.get(i).getUserId());
						addLocationMarker(gpsList.get(i));
					}
				}
			}
		}

}
