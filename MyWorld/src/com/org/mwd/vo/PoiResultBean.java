package com.org.mwd.vo;

import java.io.Serializable;

import com.baidu.mapapi.search.poi.PoiResult;

public class PoiResultBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7437076290019460208L;
	private PoiResult mPoiResult ;
	public PoiResultBean(){
		
	}
	public PoiResult getmPoiResult() {
		return mPoiResult;
	}
	public void setmPoiResult(PoiResult mPoiResult) {
		this.mPoiResult = mPoiResult;
	}
	
}
