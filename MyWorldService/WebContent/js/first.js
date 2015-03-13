var leftpd=0;
$(function(){
	 $("#friends2").click(function() {
	 $("#head_line1").empty();
	 $("#showfriends").empty();
	 $("#head_line1").find('*').unbind();
	 $("#showfriends").find('*').unbind();
	 $('.frineds_li').unbind();
	
	 var friendsId;
	 $.ajax({
		  type: "post",
		  url: "FriendsManage",
		  data:{action: "showfriends"},
		  dataType:"json", 
		  success : function(data){
			  if(data!=""){
				  $.each(data, function(i,item) {
					  if(i==0){
						  $("#head_line1").append("<div id='out'>退出</div><div id='kong'> &nbsp;</div><div id='yzmesg'>消息("+item.messagesize+")</div><div id='kong'> &nbsp;</div><div id='userinfo'>"+item.name+"</div><div id='hello'>您好！</div>");
						  $("#yzmesg").click(function(){
							    $("#yzuser").empty();
								//打开消息的蒙版
								$('#popWindow').css("display", "block");
								$('#message_pop').css("display", "block");
								 $.ajax({
									  type: "post",
									  url: "MessageManage",
									  data:"action=showmessage",
									  dataType:"json",
									  success : function(data){
										  $.each(data, function(i,item) {
											  if(item.type=="friend"){
												  $("#yzuser").append("<tr><td width='50px' valign='top'><img src='Download?groupImage="+item.img+"' class='pic'/></td><td width='250px' valign='top'><p class='fj1'><span class='reqname'>"+item.sendnick+"</span>请求加你为好友</p><p class='fujia'>附加消息："+item.content+"</p></td><td width='160px' valign='top'><input id='agree"+i+"' class='agree' type='button' value='同意' /><input id='refuse"+i+"' class='refuse'  type='button'value='拒绝' /></td></tr>");	
												  $("#agree"+i+"").click(function(){
													  $.ajax({
														  type: "post",
														  url: "FriendsManage",
														  data:{friends_name: item.sendname,action:"addfriends"},
														  dataType:"json",
														  success : function(data){
															  if(data.isadd=="0"){
																  $("#refuse"+i+"").click();
															  }else{
																  alert("操作失败");
																  $("#yzmesg").click();
															  }
														  }
													  }); 
												  });
												  $("#refuse"+i+"").click(function(){
													  $.ajax({
														  type: "post",
														  url: "MessageManage",
														  dataType:"json",
														  data:{messageid: item.messageid,action:"refuse"},
														  success : function(data){
															  if(data.isdelete=="0"){
																  $("#yzmesg").click();
															  }else{
																  alert("操作失败");
																  $("#yzmesg").click();
															  }
														  }
													  }); 
												  });
											  }else if(item.type=="group"){
												  $("#yzuser").append("<tr><td width='50px' valign='top'><img src='Download?groupImage="+item.img+"' class='pic'/></td><td width='250px' valign='top'><p class='fj1'><span class='reqname'>"+item.sendnick+"</span>请求加入群<span class='reqname'>"+item.groupname+"</span></p><p class='fujia'>附加消息："+item.content+"</p></td><td width='160px' valign='top'><input id='agree"+i+"' class='agree' type='button' value='同意' /><input id='refuse"+i+"' class='refuse'  type='button'value='拒绝' /></td></tr>");
                                                  $("#agree"+i+"").click(function(){
                                                	  $.ajax({
														  type: "post",
														  url: "GroupManage",
														  dataType:"json",
														  data:{groupid: item.groupid,groupname:item.groupname,action:"successaddgroup",uid:item.sendid,unick:item.sendnick},
														  success : function(data){
															  if(data.isadd=="0"){
																  $("#refuse"+i+"").click();
															  }else{
																  alert("操作失败");
																  $("#yzmesg").click();
															  }
														  }
													  }); 
												  });
												  $("#refuse"+i+"").click(function(){
													  $.ajax({
														  type: "post",
														  url: "MessageManage",
														  dataType:"json",
														  data:{messageid: item.messageid,action:"refuse"},
														  success : function(data){
															  if(data.isdelete=="0"){
																  $("#yzmesg").click();
															  }else{
																  alert("操作失败");
																  $("#yzmesg").click();
															  }
														  }
													  }); 
												  });
											  }
											 
										  });
									  }
								  }); 
								
						});
						  $('#out').click(function () {
								 $.ajax({
									  type: "post",
									  url: "Logout",
									  data:"action=outUser",
									  success : function(data){
										  window.location.reload();
									  }
								  }); 
						   });
						  $('#userinfo').click(function () {
							  window.location.href='personal_info.html';
						  });

						   
					  }else{
					     $("#showfriends").append("<div id='friend_list"+i+"' class='friend_list'><div id='head_pic'><img id='friendimg"+i+" 'src='Download?groupImage="+item.img+"' class='headPicture'/></div><div id='friends_name'>"+item.name+"</div><div id='friends_location'><img src='image/location.png' class='location' id='location"+i+"'/></div></div>");
					    if(item.statue=="0"){
					    	$("#friend_list"+i+"").fadeTo(1000, 0.5); 
					    }
					     $("#location"+i+"").click(function () {
					    		 $.ajax({
									  type: "post",
									  url: "FriendsManage",
									  data: {fid: item.id,action:"locationfriend"},
									  dataType:"json",
									  success : function(data){
										  if(data.nogps=="1"){
										       alert("暂无该用户GPS信息");
										  }else{
												var imgid="friendimage"+i+"";
												theLocation(data.longitude,data.latitude,data.image,data.fnick,data.name,data.phone,imgid);
										  }
										
									  }
								  });  				    	
						 });
					   //右击菜单
							$("#friend_list"+i+"").bind('contextmenu',function(e){
								$(".vmenufrineds").css({ left: e.pageX, top: e.pageY, zIndex: '101' }).show();
									//去除页面的右键菜单
									return false;
							});
							$("#friend_list"+i+"").mousedown(function(){
				    			if(event.button==2){
				    				friendsId = item.id;
				    			}
				    	
				    		});
							
					  }
	              });
				
				
				 $("#result").css("display", "none");
				 $("#leftBar").css("display", "block");
				 leftpd=1;
			  }	else{
				  $("#head_line1").append("<div id='regist'>注册</div><div id='kong'>&nbsp;</div><div id='login'>登陆</div>");
				// 蒙版登陆注册
					$('#login').click(function() {
						$('#popWindow').css("display", "block");
						$('#loginLayer').css("display", "block");
					});
					$('#loginclose').click(function() {
						$('#popWindow').css("display", "none");
						$('#loginLayer').css("display", "none");
						$('#loginform')[0].reset(); 
					    $("#error_message").html("");
					    $('#registform')[0].reset(); 
					    $('#rg_error_message').html("");
					});
					// 蒙版注册
					$('#regist').click(function() {
						$('#popWindow').css("display", "block");
						$('#registLayer').css("display", "block");
					});
					$('#registclose').click(function() {
						$('#popWindow').css("display", "none");
						$('#registLayer').css("display", "none");
						$('#loginform')[0].reset(); 
					    $("#error_message").html("");
					    $('#registform')[0].reset(); 
					    $('#rg_error_message').html("");
					});
			  }
			
		  	}
	 	
	 	}); 
	 
		var $frineds_li = $('.frineds_li');
		$frineds_li.click(function() {
			var index = $frineds_li.index(this);
			if(index==0){
												
				$('#popWindow').css("display", "block");
				$('#friendsinfo').css("display", "block");
				$("#ftitle").html("查看好友资料");
				 $.ajax({
					  type: "post",
					  url: "FriendsManage",				  
					  data: {friendsId: friendsId,action:"Seefriendsprofile"},
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
				if(confirm("确定删除该好友？")){
					 $.ajax({
						  type: "post",
						  url: "FriendsManage",				  
						  data: {friendsId: friendsId,action:"deletefriend"},
						  dataType:"json",
						  success : function(data){
							  if(data.isdelete == "0"){
								  alert("删除成功！");
								  $('.vmenufrineds').hide();	
								  $("#friends2").click();
								  
							  }else{
								  alert("删除失败！");
								  $('.vmenufrineds').hide();
								
							  }
							  
						  }
					 });
		
				}
			
				$('.vmenufrineds').hide();		
			}
			
		 });
	
		$(".frineds_li ").hover(function () {
			$(this).css({backgroundColor : '#E0EDFE' , cursor : 'pointer'});
			}, 
			function () {
				$(this).css('background-color' , '#fff' );
		});
		$("body").click(function(){
			$('.vmenufrineds').hide();	
			
		});
	 
	});	
	$("#friends2").click();
	 /*  缩放    */
	  $('#btn_toggleLeft').click(function () {
	 	
	 	var result = document.getElementById("result");
	 	var leftBar = document.getElementById("leftBar");            
	 	var content = document.getElementById("rightPage");
	 	if(leftpd==1){
	 		if (leftBar.style.display != "none" ) {                		
	 			leftBar.style.display = "none";
	 			content.style.marginLeft = 0;         
	 		}else{ 		
	 			leftBar.style.display = "block";                
	 			content.style.marginLeft = "300px";            
	 		} 
	 	}else{
	 		if (result.style.display != "none" ) {                		
	 			result.style.display = "none";
	 			content.style.marginLeft = 0;         
	 		}else{ 		
	 			result.style.display = "block";                
	 			content.style.marginLeft = "300px";            
	 		} 	
	 	}
	 });

		//  搜索和路线的切换
		$("#route_bt").click(function(){
			$("#search_part").css("display", "none");
			$("#route_part").css("display", "block");
		});
		$("#search_bt").click(function(){
			$("#search_part").css("display", "block");
			$("#route_part").css("display", "none");
			leftpd=0;
		});
		//  切换通讯录
		$("#address_book").click(function(){			
			 $.ajax({
				  type: "post",
				  url: "FriendsManage",
				  data:{action: "address"},
				  dataType:"json", 
				  success : function(data){		 
					  if(data == "" ){
						  alert("请先登录！");
					  }else{						 
						  $("#result").css("display", "none");
						  $("#leftBar").css("display", "block");
						  $("#rightPage").css("margin-left", "300px");
						  leftpd=1;
					  }
				  }
			 });
			
		});
		$("#searchbutton").click(function(){
			$("#result").css("display", "block");
			$("#leftBar").css("display", "none");
			$("#rightPage").css("margin-left", "300px");
			leftpd=0;
		});
		
		$("#busbutton").click(function(){
			$("#result").css("display", "block");
			$("#leftBar").css("display", "none");
			$("#rightPage").css("margin-left", "300px");
			leftpd=0;
		});
		$("#drivebutton").click(function(){
			$("#result").css("display", "block");
			$("#leftBar").css("display", "none");
			$("#rightPage").css("margin-left", "300px");
			leftpd=0;
		});
		$("#walkbutton").click(function(){
			$("#result").css("display", "block");
			$("#leftBar").css("display", "none");
			$("#rightPage").css("margin-left", "300px");
			leftpd=0;
		});	
		//跳转创建群
		$("#create_group").click(function(){
			window.location.href='CreatGroup.html';
		});	
	 

	 
});
