    var map;
	var bus;
	var drive;
	var walking;
	function initialize() {
		map = new BMap.Map("container"); // 创建Map实例
		map.centerAndZoom("广州", 12); // 初始化地图,用城市名设置地图中心点
		map.addControl(new BMap.NavigationControl());
		map.addControl(new BMap.ScaleControl());
		map.addControl(new BMap.OverviewMapControl());
		map.addControl(new BMap.MapTypeControl());
		map.setCurrentCity("广州"); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用

		var stCtrl = new BMap.PanoramaControl(); //构造全景控件
		stCtrl.setOffset(new BMap.Size(20, 40));
		map.addControl(stCtrl);//添加全景控件
		map.enableScrollWheelZoom(true);
		// 添加定位控件
		var geolocationControl = new BMap.GeolocationControl();
		geolocationControl.addEventListener("locationSuccess", function(e) {
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r) {
				var myGeo = new BMap.Geocoder();
				// 根据坐标得到地址描述    
				myGeo.getLocation(r.point, function(result) {
					if (result) {
						var opts = {
							width : 250, // 信息窗口宽度    
							height : 80, // 信息窗口高度    
							title : "我的位置" // 信息窗口标题   
						};
						var infoWindow = new BMap.InfoWindow(result.address,
								opts); // 创建信息窗口对象    
						map.openInfoWindow(infoWindow, r.point); // 打开信息窗口
					}
				});

			}, {
				enableHighAccuracy : true
			});
		});
		geolocationControl.addEventListener("locationError", function(e) {
			// 定位失败事件
			alert(e.message);
		});
		map.addControl(geolocationControl);

		//搜索下拉列表提示
		var ac = new BMap.Autocomplete( //建立一个自动完成的对象
		{
			"input" : "search",
			"location" : map
		});

		var myValue;
		ac.addEventListener("onconfirm", function(e) { //鼠标点击下拉列表后的事件
			var _value = e.item.value;
			myValue = _value.province + _value.city + _value.district
					+ _value.street + _value.business;
			setPlace();
		});

		function setPlace() {
			map.clearOverlays(); //清除地图上所有覆盖物
			var local = new BMap.LocalSearch(map, {
				renderOptions : {
					map : map,
					panel : "result"
				},
				onResultsHtmlSet : function() {
					$("#result").css("display", "block");
					$("#leftBar").css("display", "none");
					$("#rightPage").css("margin-left", "300px");
					leftpd=0;
				}
			});
			local.search(myValue);
		}
		var startauto = new BMap.Autocomplete( //建立一个自动完成的对象
		{
			"input" : "start",
			"location" : map
		});
		var endauto = new BMap.Autocomplete( //建立一个自动完成的对象
		{
			"input" : "end",
			"location" : map
		});
		bus = new BMap.TransitRoute(map, {
			renderOptions : {
				map : map,
				panel : "result"
			},
			onResultsHtmlSet : function() {
				$("#result").css("display", "block");
				$("#leftBar").css("display", "none");
				$("#rightPage").css("margin-left", "300px");
				leftpd=0;
			}
		});
		drive = new BMap.DrivingRoute(map, {
			renderOptions : {
				map : map,
				panel : "result",
				enableDragging : true
			//起终点可进行拖拽
			},
			onResultsHtmlSet : function() {
				$("#result").css("display", "block");
				$("#leftBar").css("display", "none");
				$("#rightPage").css("margin-left", "300px");
				leftpd=0;
			}
		});
		walking = new BMap.WalkingRoute(map, {
			renderOptions : {
				map : map,
				panel : "result",
				autoViewport : true
			},
			onResultsHtmlSet : function() {
				$("#result").css("display", "block");
				$("#leftBar").css("display", "none");
				$("#rightPage").css("margin-left", "300px");
				leftpd=0;
			}
		});
		bus.clearResults();
		drive.clearResults();
		walking.clearResults();
	}

	function loadScript() {
		var script = document.createElement("script");
		script.src = "http://api.map.baidu.com/api?v=2.0&ak=ef3pm2IQlce5HbrwEUm7y60L&callback=initialize";
		document.body.appendChild(script);
	}
	window.onload = loadScript;
	//搜索
	function Search() {
		map.clearOverlays();
		var ss = document.getElementById("search").value;
		var local = new BMap.LocalSearch(map, {
			renderOptions : {
				map : map,
				panel : "result"
			},
			onResultsHtmlSet : function() {
				$("#result").css("display", "block");
				$("#leftBar").css("display", "none");
				$("#rightPage").css("margin-left", "300px");
				leftpd=0;
			}
		});
		local.search(ss);
	}
	//公交查询
	function Bus() {
		map.clearOverlays();
		var start = document.getElementById("start").value;
		var end = document.getElementById("end").value;
		bus.clearResults();
		bus.search(start, end);
	}
	//驾车
	function Drive() {
		map.clearOverlays();
		var start = document.getElementById("start").value;
		var end = document.getElementById("end").value;
		drive.clearResults();
		drive.search(start, end);
	}
	function Walk() {
		map.clearOverlays();
		var start = document.getElementById("start").value;
		var end = document.getElementById("end").value;
		walking.clearResults();
		walking.search(start, end);
	}
	function theLocation(longitude,latitude,image,fnick,name,phone,imgid) {
		map.clearOverlays();
		// 创建地理编码实例      
		var myGeo = new BMap.Geocoder();      
		// 根据坐标得到地址描述    
		myGeo.getLocation(new BMap.Point(longitude,latitude), function(result){      
		                 if (result){   
		                	    var poi = new BMap.Point(longitude,latitude);
		                	    map.centerAndZoom(poi, 16);
		                	    map.enableScrollWheelZoom();
		                	    var content =
			             			"<div style='float:left; height:80px'>"+
			             			"<img id='"+imgid+"' src='Download?groupImage="+image+"' width='70' height='70'style='float:left;margin-top:8px;' />" +
			             			"<div style='margin-left:75px; height:70px; margin-top:-5px;'>" +
			             			"<p>"+fnick+"("+name+")</p>" +
			             			"<p>联系电话："+phone+"</p>" +
			             			"<p>地址："+result.address+"</p>" +
			             			"</div></div>";

		                	    //创建检索信息窗口对象
		                	    var searchInfoWindow = null;
		                		searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
		                				title  : "好友位置",      //标题
		                				width  : 290,             //宽度
		                				height : 105,              //高度
		                				panel  : "F",         //检索结果面板
		                				enableAutoPan : true,     //自动平移
		                				searchTypes   :[
		                					BMAPLIB_TAB_SEARCH,   //周边检索
		                 					BMAPLIB_TAB_TO_HERE,  //到这里去
		                					BMAPLIB_TAB_FROM_HERE //从这里出发
		                				]
		                			});
		                	    var marker = new BMap.Marker(poi); //创建marker对象
		                	    //marker.enableDragging(); //marker可拖拽
		                	    marker.addEventListener("click", function(e){
		                		    searchInfoWindow.open(marker);
		                	    });
		                	    map.addOverlay(marker); //在地图中添加marker
		                	    searchInfoWindow.open(marker);
		             		   
		                  }      
		});
		
	}
	//function errorImg(img) { 
	//	img.src = "./image/tidai.jpg"; 
	//	img.onerror = null; 
//	} 
	
	function GroupLocation(longitude,latitude,image,fnick,name,phone,imgid) {	
	

		map.centerAndZoom(new BMap.Point(longitude,latitude), 12);
		/*var geolocation1 = new BMap.Geolocation();
		geolocation1.getCurrentPosition(function(r){
			if(this.getStatus() == BMAP_STATUS_SUCCESS){
				map.panTo(r.point);
			}
			else {
				var dw = new BMap.Point(longitude,latitude);
				map.panTo(dw);
			}        
		},{enableHighAccuracy: true})*/
		var pt = new BMap.Point(longitude,latitude);
		
		var myIcon = new BMap.Icon("Download?groupImage="+image+"", new BMap.Size(50,50),{ anchor: new BMap.Size(25, 75)});   
		myIcon.setImageSize(new BMap.Size(50,50));
		var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
		var marker1 = new BMap.Marker(pt); //创建marker对象
		map.addOverlay(marker1);
		map.addOverlay(marker2);               // 将标注添加到地图中
		var opts = {
				  position : pt,    // 指定文本标注所在的地理位置
				  offset   : new BMap.Size(-20, -95)    //设置文本偏移量
		};	
		var label = new BMap.Label(""+fnick+"", opts);  // 创建文本标注对象
		label.setStyle({
					border : "none" ,
					fontSize : "12px",
					color : "#228ce4" ,
					fontWeight : "bold" ,
					fontFamily:"微软雅黑" ,
					background : "transparent" ,
		});
		map.addOverlay(label);		
		marker1.addEventListener("click", function(){   
			marker2.hide();
			label.hide();
			
			// 创建地理编码实例      
			var myGeo = new BMap.Geocoder();      
			// 根据坐标得到地址描述    
			myGeo.getLocation(new BMap.Point(longitude,latitude), function(result){      
			                 if (result){   
			                	    var poi = new BMap.Point(longitude,latitude);
			                	    map.centerAndZoom(poi, 16);
			                	    map.enableScrollWheelZoom();
			                	    var content =
				             			"<div style='float:left; height:80px'>"+
				             			"<img id='"+imgid+"' src='Download?groupImage="+image+"' width='70' height='70'style='float:left;margin-top:8px;'  />" +
				             			"<div style='margin-left:75px; height:70px; margin-top:-5px;'>" +
				             			"<p>"+fnick+"("+name+")</p>" +
				             			"<p>联系电话："+phone+"</p>" +
				             			"<p>地址："+result.address+"</p>" +
				             			"</div></div>";

			                	    //创建检索信息窗口对象
			                	    var searchInfoWindow = null;
			                	    
			                		searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
			                				title  : "好友位置",      //标题
			                				width  : 290,             //宽度
			                				height : 105,              //高度
			                				panel  : "F",         //检索结果面板
			                				enableAutoPan : true,     //自动平移
			                				searchTypes   :[
			                					BMAPLIB_TAB_SEARCH,   //周边检索
			                 					BMAPLIB_TAB_TO_HERE,  //到这里去
			                					BMAPLIB_TAB_FROM_HERE //从这里出发
			                				]
			                			});
			                	    var marker = new BMap.Marker(poi); //创建marker对象                	   
			                	    marker.addEventListener("click", function(e){
			                	    	marker2.hide();
			                			label.hide();
			                		    searchInfoWindow.open(marker);
			                	    });
			                	    map.addOverlay(marker); //在地图中添加marker
			                	    searchInfoWindow.open(marker);
			                	    searchInfoWindow.addEventListener("close", function(e) {    	      
			                	        marker2.show();
			                			label.show();
			                	    });
			                  }      
			});
    	   
		});				
		marker2.addEventListener("click", function(){     
			marker2.hide();
			label.hide();

			// 创建地理编码实例      
			var myGeo = new BMap.Geocoder();      
			// 根据坐标得到地址描述    
			myGeo.getLocation(new BMap.Point(longitude,latitude), function(result){      
			                 if (result){   
			                	    var poi = new BMap.Point(longitude,latitude);
			                	    map.centerAndZoom(poi, 16);
			                	    map.enableScrollWheelZoom();
			                	    var content =
				             			"<div style='float:left; height:80px'>"+
				             			"<img id='"+imgid+"' src='Download?groupImage="+image+"' width='70' height='70'style='float:left;margin-top:8px;'  />" +
				             			"<div style='margin-left:75px; height:70px; margin-top:-5px;'>" +
				             			"<p>"+fnick+"("+name+")</p>" +
				             			"<p>联系电话："+phone+"</p>" +
				             			"<p>地址："+result.address+"</p>" +
				             			"</div></div>";

			                	    //创建检索信息窗口对象
			                	    var searchInfoWindow = null;
			                		searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
			                				title  : "好友位置",      //标题
			                				width  : 290,             //宽度
			                				height : 105,              //高度
			                				panel  : "F",         //检索结果面板
			                				enableAutoPan : true,     //自动平移
			                				searchTypes   :[
			                					BMAPLIB_TAB_SEARCH,   //周边检索
			                 					BMAPLIB_TAB_TO_HERE,  //到这里去
			                					BMAPLIB_TAB_FROM_HERE //从这里出发
			                				]
			                			});
			                	    var marker = new BMap.Marker(poi); //创建marker对象
			                	    marker.addEventListener("click", function(e){
			                	    	marker2.hide();
			                			label.hide();
			                		    searchInfoWindow.open(marker);
			                	    });
			                	    map.addOverlay(marker); //在地图中添加marker
			                	    searchInfoWindow.open(marker);
			                	    searchInfoWindow.addEventListener("close", function(e) {
			                	        marker2.show();
			                			label.show();
			                	    });	
			                  }      
			});
    	                					            
		});

	}

	
	
	