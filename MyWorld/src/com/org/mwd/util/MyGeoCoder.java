package com.org.mwd.util;


import android.content.Context;
import android.util.SparseArray;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.org.mwd.vo.SaveBean;

/**
 * 此demo用来展示如何进行地理编码搜索（用地址检索坐标）、反地理编码搜索（用坐标检索地址）
 */
public class MyGeoCoder implements OnGetGeoCoderResultListener {
	// 搜索模块，也可去掉地图模块独立使用
	GeoCoder mSearch = null;
	private SparseArray<String> address;
	SaveBean save = null;
	int userId;
	public MyGeoCoder(LatLng ll ,Context context,int userId){
		save = (SaveBean) context.getApplicationContext();
		this.address = save.getAddress();
		this.userId = userId;
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		//System.out.println("address"+result+"-----"+result.getAddress());
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			return;
		}
		address.put(userId,result.getAddress());
		save.setAddress(address);
	}

	
}
