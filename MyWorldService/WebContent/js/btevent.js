$(function() {
		
	// 蒙版添加好友
	$('#add_friend').click(function() {
		
		$('#popWindow').css("display", "block");
		$('#add_friend_pop').css("display", "block");
	});
	
	$('#addfriendclose').click(function() {
		$('#popWindow').css("display", "none");
		$('#add_friend_pop').css("display", "none");
		$("#addTable").empty();
		$("#add_message").empty();
		$("#friends2").click();
		$("#addfriend_text").val("");
	});
	// 蒙版添加群
	$('#add_group').click(function() {
		
		$('#popWindow').css("display", "block");
		$('#add_group_pop').css("display", "block");
		
	});
	$('#addgroupclose').click(function() {
		$('#popWindow').css("display", "none");
		$('#add_group_pop').css("display", "none");
		$("#groups").click();
		$("#addTable_g").empty();
		$("#add_gmessage").empty();
		$("#addgroup_text").val("");

	});
	// 左右收缩切换图片
	$("#btn_toggleLeft").toggle(	
			function(){
				$(".tog").attr('src','image/jt_1.png');
			},
			function(){
				$(".tog").attr('src','image/jt_2.png');
			}
		);
	
	$(window).resize(function() { 
		var winHeight = 0;
		if (window.innerHeight) {
			winHeight = window.innerHeight - 109;
			$("#content").css("height", winHeight + 'px');
			$("#container").css("height", winHeight + 'px');
			$("#result").css("height", winHeight + 'px');
			$("#leftBar").css("height", winHeight + 'px');
		} else if ((document.body) && (document.body.clientHeight)) {
			winHeight = document.body.clientHeight - 109;
			$("#content").css("height", winHeight + 'px');
			$("#container").css("height", winHeight + 'px');
			$("#result").css("height", winHeight + 'px');
			$("#leftBar").css("height", winHeight + 'px');
		}
	}); 
	var winHeight = 0;
	if (window.innerHeight) {
		winHeight = window.innerHeight - 109;
		$("#content").css("height", winHeight + 'px');
		$("#container").css("height", winHeight + 'px');
		$("#result").css("height", winHeight + 'px');
		$("#leftBar").css("height", winHeight + 'px');
	} else if ((document.body) && (document.body.clientHeight)) {
		winHeight = document.body.clientHeight - 109;
		$("#content").css("height", winHeight + 'px');
		$("#container").css("height", winHeight + 'px');
		$("#result").css("height", winHeight + 'px');
		$("#leftBar").css("height", winHeight + 'px');
	}
	// 文本框焦点事件
	$(".username").focus(function() {
		$("#col").css("border-color", "#2565DA");
	});
	$(".username").blur(function() {
		$("#col").css("border-color", "#B9B9B9");
	});
	$(".userpsw").focus(function() {
		$("#col2").css("border-color", "#2565DA");
	});
	$(".userpsw").blur(function() {
		$("#col2").css("border-color", "#B9B9B9");
	});

	/* 好友与群组切换 */
	var $li = $("#kind li");
	$li.click(function() {

		$(this).addClass("selected").siblings().removeClass("selected");
		var index = $li.index(this);

		$("#row2 > div").eq(index).show().siblings().hide();
	});

	// 群列表切换
	$("#my_group").click(function() {
		$("#my_group_part").toggle();
	});
	$("#other_group").click(function() {
		$("#other_group_part").toggle();
	});
	
	
	$("#groups").click(function() {
		  $("#my_group_part").empty();
		  $("#other_group_part").empty();
		  $("#my_group_part").find('*').unbind();
		  $("#other_group_part").find('*').unbind();
		  $('.first_li').unbind();
		  $('.second_li').unbind();
		  $('.mygroup_li').unbind();
		  $('.other_group').unbind();
		  $('.othergroup_li').unbind();
		  var idArray = {};
		  var groupIndex;
		  var idArray_other ={};
		  var groupIndex_other;
		  var groupMenberid;
		  var groupId;
		  var other_groupMenberid ;
		  var other_groupId ;
		 
		  $.ajax({
			  type: "post",
			  url: "GroupManage",
			  data: {action: "showallgroup"},
			  dataType:"json", 
			  success : function(data){
					  $.each(data, function(i,item) {
						  if(item.manager=="0"){
							     $("#my_group_part").append("<div id='my_group_list"+i+"' class='my_group_list'><div id='g_head_pic"+i+"' class='g_head_pic'><img src='Download?groupImage="+item.gimage+"' class='g_headPicture' id='mg_headPicture"+i+"'></div><div id='group_name"+i+"' class='group_name'>"+item.gname+"</div><div id='group_location'><img src='image/location.png' class='g_location' id='my_g_location"+i+"' ></div></div>");
							     	
							     idArray[i] = item.id;
							     $.ajax({
									  type: "post",
									  url: "GroupManage",
									  data: {groupId: item.id,action: "locationgroup"},
									  dataType:"json",
									  success : function(data){
										  $("#my_g_location"+i+"").click(function() {
											  map.clearOverlays();
											  $.each(data, function(j,item) {										 
												  if(item.ulongitude=="nogps"){
														 // alert("暂无该成员GPS信息!");
												   }else{
														  var imgid="userimage"+j+"";
														  GroupLocation(item.ulongitude,item.ulatitude,item.uimage,item.unick,item.uname,item.uphone,imgid);
												   }
												 
											  });
											  
										 	});
										     // 我的群成员列表展示
										  var mygroupperson=0;
										 	$("#g_head_pic"+i+",#group_name"+i+"").click(function() {
										 		if(mygroupperson==0){
										 			mygroupperson=1;
										 			
												    // $("#my_group_person"+i+"").toggle();
											 	    $.each(data, function(e,item) {					

					 	    	
												 	    $("#my_group_list"+i+"").after("<div id='my_group_person"+item.uname+"_"+i+"' class='my_group_person'><div id='mygroup_person_pic'><img src='Download?groupImage="+item.uimage+"' class='mygroup_personPicture' id='mygroup_headPicture"+e+"_"+i+"'></div><div id='mygroup_person_name'>"+item.unick+"</div><div id='group_location'><img src='image/location.png' class='mygroup_person_location' id='mygroup_person_location"+e+"_"+i+"' /></div></div>");  		
												 	    $("#mygroup_person_location"+e+"_"+i+"").click(function(){
															  var imgid="friendimage"+e+"";
															  if(item.ulongitude=="nogps"){
																  alert("暂无该成员GPS信息!");
															  }else{
														    	  theLocation(item.ulongitude,item.ulatitude,item.uimage,item.unick,item.uname,item.uphone,imgid);
															  }
			
													    });
												 	   
													  
													    // 群成员右击
												 	    $("#my_group_person"+item.uname+"_"+i+"").bind('contextmenu',function(e){
												 	    	$(".vmenuMenber").css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();
												 	    	// 去除页面的右键菜单
												 	    	return false;
										    				
												 	    });
												 	   $("#my_group_person"+item.uname+"_"+i+"").mousedown(function(){
											    			if(event.button==2){
											    				 groupMenberid = item.uid;
											    				 groupId = idArray[i];
											    				
											    				
											    			}
											    	
											    		});
													    
											 	   });  
											 	    
											 	   
										    		
											 	   
										 		}else{
										 			mygroupperson=0;
										 			$.each(data, function(e,item) {	
													    $("#my_group_person"+item.uname+"_"+i+"").remove();
											 		 });  
										 		}
										 		
										    });
																		
									  }
								  }); 
							     // 右击菜单
					    		$("#my_group_list"+i+"").bind('contextmenu',function(e){
					    			$(".vmenu").css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();
				    				// 去除页面的右键菜单
				    				return false;
					    				
					    		});
					    		$("#my_group_list"+i+"").mousedown(function(){
					    			if(event.button==2){
					    				groupIndex = i;
					    			}
					    	
					    		});
					    		
					    		
				
						 }else if(item.manager=="1"){
							 $("#other_group_part").append("<div id='my_othergroup_list"+i+"' class='my_othergroup_list'><div id='og_head_pic"+i+"' class='g_head_pic'><img src='Download?groupImage="+item.gimage+"' class='g_headPicture' id='og_headPicture"+i+"'></div><div id='ogroup_name"+i+"' class='group_name'>"+item.gname+"</div><div id='group_location'><img src='image/location.png' class='g_location' id='other_g_location"+i+"' ></div></div>");
							 	
							 idArray_other[i] = item.id;
						     $.ajax({
								  type: "post",
								  url: "GroupManage",
								  data: {groupId: item.id,action: "locationgroup"},
								  dataType:"json",
								  success : function(data){
									  $("#other_g_location"+i+"").click(function() {
										  map.clearOverlays();
										  $.each(data, function(j,item) {									  
											  if(item.ulongitude=="nogps"){
												 // alert("暂无该成员GPS信息!");
											  }else{											
												  var imgid="userimage"+j+"";
												  GroupLocation(item.ulongitude,item.ulatitude,item.uimage,item.unick,item.uname,item.uphone,imgid);
											  }
			
										  });
										  
									 	});
									     // 我加入的群成员列表展示
									  var ogroupperson=0;
									 	$("#og_head_pic"+i+",#ogroup_name"+i+"").click(function() {
									 		if(ogroupperson==0){
									 			ogroupperson=1;
										 	    $.each(data, function(e,item) {							    	
											 	    $("#my_othergroup_list"+i+"").after("<div id='o_group_person"+item.uname+"_"+i+"' class='other_group_person'><div id='mygroup_person_pic'><img src='Download?groupImage="+item.uimage+"' class='mygroup_personPicture' id='ogroup_headPicture"+e+"_"+i+"'></div><div id='ogroup_person_name'>"+item.unick+"</div><div id='group_location'><img src='image/location.png' class='mygroup_person_location' id='ogroup_person_location"+e+"_"+i+"' /></div></div>");  		
											 	    $("#ogroup_person_location"+e+"_"+i+"").click(function(){										 	    
											 	    	 if(item.ulongitude=="nogps"){
															  alert("暂无该成员GPS信息!");
														  }else{
															  var imgid="friendimage"+e+"";
															  theLocation(item.ulongitude,item.ulatitude,item.uimage,item.unick,item.uname,item.uphone,imgid);
														  }
											 	    	
												    });
											 	    
											 	    // 我加入的群成员右击
											 	    $("#o_group_person"+item.uname+"_"+i+"").bind('contextmenu',function(e){
											 	    	$(".vmenuother_Menber").css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();
											 	    	// 去除页面的右键菜单
											 	    	return false;
									    				
											 	    });
											 	   $("#o_group_person"+item.uname+"_"+i+"").mousedown(function(){
										    			if(event.button==2){
										    				 other_groupMenberid = item.uid;
										    				 other_groupId = idArray[i];							    				
										    			}
										    	
										    		});
										 	   });  
										 	   
									 		}else{
									 			ogroupperson=0;
									 			$.each(data, function(e,item) {	
												    $("#o_group_person"+item.uname+"_"+i+"").remove();
										 		 });  
									 		}
									 		
									    });
																	
								  }
							  }); 
						     // 右击菜单
				    		$("#my_othergroup_list"+i+"").bind('contextmenu',function(e){
				    			$(".vmenu2").css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();
				    				// 去除页面的右键菜单
				    				return false;
				    		});
				    		$("#my_othergroup_list"+i+"").mousedown(function(){
				    			if(event.button==2){
				    				groupIndex_other = i;
				    			}
				    	
				    		});
				    		
						 }else{						
						   $("#my_account").text(item.managergroupsize);
						   $("#other_account").text(item.othergroupsize);
						  }
					  });
					  
					  var $li = $('.first_li');
			    		$li.click(function() {
			    			var index = $li.index(this);
							if(index==0){
								// 蒙版 群资料
								$('#popWindow').css("display", "block");
								$('#groupinfo').css("display", "block");								
								var arrayValue = idArray[groupIndex];
								 $.ajax({
									  type: "post",
									  url: "GroupManage",
									  data: {groupId: arrayValue,action: "Seegroupprofile"},
									  dataType:"json",
									  success : function(data){
										  var groupType;
                                          var groupsize;
                                          $("#table1").empty();
                                          $("#table_g").empty();
                                          
										  $("#table1").append("<tr><td id='g_img' width='80px'><img src='Download?groupImage="+data.groupImage+"' id='groupimg' class='groupimg' /></td><td>"+data.groupName+"("+data.munber+")"+"</td></tr>");
										  if(data.groupType == "0"){
											 
											  groupType = "同事好友";
										  }
										  if(data.groupType == "1"){
											  groupType = "兴趣爱好";
										  }
										  if(data.groupType == "2"){
											  groupType = "志愿活动";
										  }
										  if(data.groupType == "3"){
											  groupType = "外出旅游";
										  }  
										  if(data.groupsize == "10"){
									
											  groupsize = "10人";
										  }
										  if(data.groupsize == "30"){
											  groupsize = "30人";
										  }
										  if(data.groupsize == "50"){
											  groupsize = "50人";
										  }
										 
										  $("#table_g").append("<tr><td>群分类：</td><td class='data'>"+groupType+"</td></tr><tr><td>群规模：</td><td class='data'>"+groupsize+"</td></tr><tr><td>创建时间：</td><td class='data'>"+data.createTime+"</td></tr><tr><td>群描述:</td><td class='data'>"+data.desc+"</td></tr>");		 
									  
									}
								});
								
								$('#groupinf_close').click(function() {
									$('#popWindow').css("display", "none");
									$('#groupinfo').css("display", "none");
								});
								
							}
							if(index==1){						
								var arrayValue = idArray[groupIndex];
								 $.ajax({
									  type: "post",
									  url: "GroupManage",
									  data: {groupId: arrayValue,action1:"index",action:"modifygroupprofile"},
									  dataType:"json",
									  success : function(data){
											 location.href = "modify_group.html";
									  }
								 });								
									
							}
							if(index==2){
								if(confirm("确定解散该群？")){
									var arrayValue = idArray[groupIndex];
									 $.ajax({
										  type: "post",
										  url: "GroupManage",
										  data: {groupId: arrayValue,action:"deletegroup"},
										  dataType:"json",
										  success : function(data){
											  if(data.isdelete == "0"){
												  alert("解散群成功！");
												  $('.vmenu').hide();	
												  $("#groups").click();
												  var idArray = {};
											  }else{
												  alert("解散群失败！");
												  $('.vmenu').hide();
												  $('.vmenu2').hide();
												  $('.vmenuMenber').hide();
											  }
											  
										  }
									 });
								}
								$('.vmenu').hide();
								$('.vmenu2').hide();
								$('.vmenuMenber').hide();		
							}
							if(index==3){
								var arrayValue = idArray[groupIndex];
								 $.ajax({
									  type: "post",
									  url: "GroupManage",
									  data: {groupId: arrayValue,action1:"index",action:"groupshare"},
									  dataType:"json",
									  success : function(data){
										  if(data.issuccess=="0"){
											  location.href = "share.html";
										  }
											 
									  }
								 });
							}
								
			    		 });

			    		$(".first_li ").hover(function () {
			    				$(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
			    			}, 
			    			function () {
			    				$(this).css('background-color' , '#fff' );
			    		});
			    		
			    		// 我加入的群--右击
			    		var $li2 = $('.second_li');
			    		$li2.click(function() {
			    			var index2 = $li2.index(this);
			    			if(index2==0){
								// 蒙版 群资料
								$('#popWindow').css("display", "block");
								$('#groupinfo').css("display", "block");								
								var arrayValue = idArray_other[groupIndex_other];
								 $.ajax({
									  type: "post",
									  url: "GroupManage",
									  data: {groupId: arrayValue,action: "Seegroupprofile"},
									  dataType:"json",
									  success : function(data){
										  var groupType;
                                          var groupsize;
                                          $("#table1").empty();
                                          $("#table_g").empty();
                                          
										  $("#table1").append("<tr><td id='g_img' width='80px'><img src='Download?groupImage="+data.groupImage+"' id='groupimg' class='groupimg' /></td><td>"+data.groupName+"("+data.munber+")"+"</td></tr>");
										  if(data.groupType == "0"){
											 
											  groupType = "同事好友";
										  }
										  if(data.groupType == "1"){
											  groupType = "兴趣爱好";
										  }
										  if(data.groupType == "2"){
											  groupType = "志愿活动";
										  }
										  if(data.groupType == "3"){
											  groupType = "外出旅游";
										  }  
										  if(data.groupsize == "10"){
									
											  groupsize = "10人";
										  }
										  if(data.groupsize == "30"){
											  groupsize = "30人";
										  }
										  if(data.groupsize == "50"){
											  groupsize = "50人";
										  }
										 
										  $("#table_g").append("<tr><td>群主：</td><td class='data'>"+data.manager+"</td></tr><tr><td>群分类：</td><td class='data'>"+groupType+"</td></tr><tr><td>群规模：</td><td class='data'>"+groupsize+"</td></tr><tr><td>创建时间：</td><td class='data'>"+data.createTime+"</td></tr><tr><td>群描述:</td><td class='data'>"+data.desc+"</td></tr>");
										 
									  }
								 });
								$('#groupinf_close').click(function() {
									$('#popWindow').css("display", "none");
									$('#groupinfo').css("display", "none");
								});
						
							}
							
							if(index2==1){
								if(confirm("确定退出该群？")){
									var arrayValue=idArray_other[groupIndex_other];	
										 $.ajax({
											  type: "post",
											  url: "GroupManage",
											  data: {groupId: arrayValue,action:"exitgroup"},
											  dataType:"json",
											  success : function(data){
												  if(data.issuccess == "0"){
													  alert("退出群成功！");
													  $("#groups").click();
													  var idArray_other ={};
													  $('.vmenu').hide();
									  
												  }else{
													  alert("退出群失败！");
													  $('.vmenu2').hide();		
												  }
												  
											  }
										 });
								}
								$('.vmenu').hide();
								$('.vmenu2').hide();
								$('.vmenuMenber').hide();		
							}
							if(index2==2){
								var arrayValue=idArray_other[groupIndex_other];	
								 $.ajax({
									  type: "post",
									  url: "GroupManage",
									  data: {groupId: arrayValue,action1:"index",action:"groupshare"},
									  dataType:"json",
									  success : function(data){
										  if(data.issuccess=="0"){
											  location.href = "share.html";
										  }
											 
									  }
								 });	
							}
							
			    						
			    		 });
			    		
			    		$(".second_li ").hover(function () {
			    			
			    			$(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
			    			}, 
			    			function () {
			    				$(this).css('background-color' , '#fff' );
			    		});
			    		// 我管理的群成员
			    		var $mygroup_li = $('.mygroup_li');
			    		$mygroup_li.click(function() {
			    			var index = $mygroup_li.index(this);
			    			if(index==0){				
								$('#popWindow').css("display", "block");
								$('#friendsinfo').css("display", "block");	
								$("#ftitle").html("查看成员资料");
									 $.ajax({
										  type: "post",
										  url: "FriendsManage",				  
										  data: {friendsId: groupMenberid,action:"Seefriendsprofile"},
										  dataType:"json",
										  success : function(data){
											 $("#table_f").empty();
											 var sex,birthday,address,phone;
											 if(data.friendsSex=="0"){
												 sex = "男";
											 }
											 else if(data.friendsSex=="1"){
												 sex = "女";
											 }else{
												 sex = "不详";
											 }
											 if(data.friendsBirthday=="null"){
												 birthday="";
											 }else{
												 birthday=data.friendsBirthday;
											 }
											 if(data.friendsAddress=="null"){
												 address="";
											 }else{
												 address=data.friendsAddress;
											 }
											 if(data.friendsPhone=="null"){
												 phone="";
											 }else{
												 phone=data.friendsPhone;
											 }
											  $("#table_f").append("<tr><td>头像：</td><td class='data'><img src='Download?groupImage="+data.friendsimg+"' class='friendsimg' /></td></tr><tr><td>昵称：</td><td class='data'>"+data.friendsfnick+"("+data.friendsname+")</td></tr><tr><td>性别：</td><td class='data'>"+sex+"</td></tr><tr><td>生日:</td><td class='data'>"+birthday+"</td></tr><tr><td>电话号码:</td><td class='data'>"+phone+"</td></tr><tr><td>地址:</td><td class='data'>"+address+"</td></tr>");
											 
										  }
									 });

								
								$('#friendsinfo_close').click(function() {
									$('#popWindow').css("display", "none");
									$('#friendsinfo').css("display", "none");
								});
					
			    			}
			    			if(index==1){	
			    				if(confirm("确定踢除该成员？")){
									$.ajax({
										  type: "post",
										  url: "GroupManage",
										  data: {userId: groupMenberid,groupId:groupId,action:"deletegroupmenber"},
										  dataType:"json",
										  success : function(data){
											 if(data.issuccess=="0"){
												 alert("成功踢除该成员！");
												 $("#groups").click();
											 }else if(data.issuccess=="2"){
												 alert("不能踢除群主！");
											 }else{
												 alert("踢除该成员失败!");
											 }
											 $('.vmenu').hide();
											 $('.vmenu2').hide();
											 $('.vmenuMenber').hide();	
										  }
									 });
								}
				
			    			}else{
			    				$('.vmenu').hide();
								$('.vmenu2').hide();
								$('.vmenuMenber').hide();	
			    			}	
			    				
			    			if(index==2){	
			    				if(confirm("确定转让群给该成员？")){
									$.ajax({
										  type: "post",
										  url: "GroupManage",
										  data: {userId: groupMenberid,groupId:groupId,action:"changegroupmanager"},			  
										  dataType:"json",
										  success : function(data){
											 if(data.ischange=="0"){
												 alert("转让群成功！！");
												 $("#groups").click();
											 }else if(data.ischange=="2") {
												  alert("你已经是该群群主！");
											 }else{
												   alert("转让群失败！");
											 }
											 $('.vmenu').hide();
											 $('.vmenu2').hide();
											 $('.vmenuMenber').hide();	
										  }
									 });
								}
				
			    			}else{
			    				$('.vmenu').hide();
								$('.vmenu2').hide();
								$('.vmenuMenber').hide();	
			    			}	
							
			    		 });

			    		$(".mygroup_li ").hover(function () {
			    			$(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
			    			}, 
			    			function () {
			    				$(this).css('background-color' , '#fff' );
			    		});
			    		// 我加入的群成员
			    		var $othergroup_li = $('.othergroup_li');
			    		$othergroup_li.click(function() {	
			    			var index = $othergroup_li.index(this);
			    			if(index==0){
			    				$('#popWindow').css("display", "block");
								$('#friendsinfo').css("display", "block");
								$("#ftitle").html("查看成员资料");
									 $.ajax({
										  type: "post",
										  url: "FriendsManage",				  
										  data: {friendsId: other_groupMenberid,action:"Seefriendsprofile"},
										  dataType:"json",
										  success : function(data){
											 $("#table_f").empty();
											 var sex,birthday,address,phone;
											 if(data.friendsSex=="0"){
												 sex = "男";
											 }
											 else if(data.friendsSex=="1"){
												 sex = "女";
											 }else{
												 sex = "不详";
											 }
											 if(data.friendsBirthday=="null"){
												 birthday="";
											 }else{
												 birthday=data.friendsBirthday;
											 }
											 if(data.friendsAddress=="null"){
												 address="";
											 }else{
												 address=data.friendsAddress;
											 }
											 if(data.friendsPhone=="null"){
												 phone="";
											 }else{
												 phone=data.friendsPhone;
											 }
											 $("#table_f").append("<tr><td>头像：</td><td class='data'><img src='Download?groupImage="+data.friendsimg+"' class='friendsimg' /></td></tr><tr><td>昵称：</td><td class='data'>"+data.friendsfnick+"("+data.friendsname+")</td></tr><tr><td>性别：</td><td class='data'>"+sex+"</td></tr><tr><td>生日:</td><td class='data'>"+birthday+"</td></tr><tr><td>电话号码:</td><td class='data'>"+phone+"</td></tr><tr><td>地址:</td><td class='data'>"+address+"</td></tr>");
											 
										  }
									 });

								
								$('#friendsinfo_close').click(function() {
									$('#popWindow').css("display", "none");
									$('#friendsinfo').css("display", "none");
								});

			    			}
			    			
			    			
			    			$('.vmenu').hide();
							$('.vmenu2').hide();
							$('.vmenuMenber').hide();
							$(".vmenuother_Menber").hide();
							
			    		 });

			    		$(".othergroup_li ").hover(function () {
			    			$(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
			    			}, 
			    			function () {
			    				$(this).css('background-color' , '#fff' );
			    		});
			    		
			    		$("body").click(function(){
			    			$('.vmenu').hide();
							$('.vmenu2').hide();
							$('.vmenuMenber').hide();	
							$('.vmenuother_Menber').hide();	
							$('.vmenufrineds').hide();	
							
			    		});
			    		$("body").mousedown(function(){
			    			if(event.button==2){
			    				$('.vmenu').hide();
								$('.vmenu2').hide();
								$('.vmenuMenber').hide();	
								$('.vmenuother_Menber').hide();	
								$('.vmenufrineds').hide();	
			    			}
			    	
			    		});
			    		
			  }
		  
		  }); 
	});

	
});