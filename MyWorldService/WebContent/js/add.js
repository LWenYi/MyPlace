$(function() {
	//查找好友
	$("#find_friend_btn").click(function () {
		if($("#addfriend_text").val()==""){
			$("#addTable").empty();
			$("#add_message").empty();
			$("#add_message").append("输入的值不能为空");
		}else{
			$.ajax({
				  type: "post",
				  url: "FriendsManage",
				  data: {friends_name : $("#addfriend_text").val(),isfind:"0",action: "Lookingfriends"},
				  dataType:"json", 
				  success : function(data){
					if(data.nouser=="0"){
						$("#addTable").empty();
						$("#add_message").empty();
						$("#add_message").append("找不到此用户！");
					}else{
						$("#addTable").empty();
						$("#add_message").empty();
						$("#addTable").append("<tr><td width='200px'>"+data.nick+"（"+data.name+"）</td><td align='left'><input id='addfriend_btn' type='button' value='添加' class='addfriend_btn' /></tr>");
						$("#addfriend_btn").click(function () {
							$.ajax({
								  type: "post",
								  url: "FriendsManage",
								  data: {friends_name : $("#addfriend_text").val(),isfind:"1",action: "Lookingfriends"},
								  dataType:"json", 
								  success : function(data){
									if(data.isadd=="1"){
										$("#add_message").empty();
										$("#add_message").append("<tr><td>该用户已是你的好友！</td></tr>");
									}else if(data.isadd=="0"){
										//打开发送信息的蒙版
										$('#popWindow').css("display", "none");
										$('#add_friend_pop').css("display", "none");
										$('#popWindow').css("display", "block");
										$('#request_pop').css("display", "block");
										$("#request_btn").click(function () {
											$.ajax({
												  type: "post",
												  url: "AcceptMessage",
												  data: {content : $("#request_txt").val(),receiver:data.friendsname,action: "addfriendmessage"},
												  dataType:"json", 
												  success : function(data){
													if(data.issuccess=="0"){
														$("#request_message").css("color","#000000");
														$("#request_message").html("发送成功!");
													}else if(data.issuccess=="1"){
														$("#request_message").css("color","red");
														$("#request_message").html("发送失败!");
													}else if(data.issuccess=="2"){
														$("#request_message").css("color","red");
														$("#request_message").html("已经发送过请求!");
														
													}
												  }
											  }); 
										});									
									}else if(data.isadd=="2"){
										$("#add_message").empty();
										$("#add_message").append("<tr><td>不能加自己为好友！</td></tr>");
									}
								  }
							  }); 
						});
						
					}
				  }
			  }); 
		}
		
	});
	
	//加群
	$("#find_group_btn").click(function () {
		if($("#addgroup_text").val()==""){
			$("#addTable_g").empty();
			$("#add_gmessage").empty();
			$("#add_gmessage").append("输入的值不能为空");
		}else{
			$.ajax({
				  type: "post",
				  url: "GroupManage",
				  data: {group_key : $("#addgroup_text").val(),isfind:"0",action:"addgroup"},
				  dataType:"json", 
				  success : function(data){
					if(data.nogroup=="0"){
						$("#addTable_g").empty();
						$("#add_gmessage").empty();
						$("#add_gmessage").append("<tr><td>找不到该群！</td></tr>");
					}else{
						$("#addTable_g").empty();
						$("#add_gmessage").empty();
					    $.each(data, function(e,item) {										 	    	
					    	$("#addTable_g").append("<tr><td width='200px'>"+item.gname+"（"+item.gnumber+"）</td><td align='left'><input class='addgroup_btn' id='addgroup_btn"+e+"' type='button' value='加入群' /></tr>");
					    	$("#addgroup_btn"+e+"").click(function () {						
								$.ajax({
									  type: "post",
									  url: "GroupManage",
									  data: {groupid : item.groupId,groupname:item.gname,isfind:"1",action:"addgroup"},
									  dataType:"json", 
									  success : function(data){
										if(data.isadd=="1"){
											$("#add_gmessage").empty();
											$("#add_gmessage").append("<tr><td>您已是该群成员！</td></tr>");
										}else{	
											//打开发送信息的蒙版
											$('#popWindow').css("display", "none");
											$('#add_group_pop').css("display", "none");
											$('#popWindow').css("display", "block");
											$('#request_pop').css("display", "block");
											$("#request_btn").click(function () {
												$.ajax({
													  type: "post",
													  url: "AcceptMessage",
													  data: {content : $("#request_txt").val(),receiver:data.groupid,action: "addgroupmessage"},
													  dataType:"json", 
													  success : function(data){
														if(data.issuccess=="0"){
															$("#request_message").css("color","#000000");
															$("#request_message").html("发送成功!");
														}else if(data.issuccess=="1"){
															$("#request_message").css("color","red");
															$("#request_message").html("发送失败!");
														}else if(data.issuccess=="2"){
															$("#request_message").css("color","red");
															$("#request_message").html("已经发送过请求!");
														}
													  }
												  }); 
											});								
										}
									  }
								  }); 
							});
					    });    	
						
						
					}
				  }
			  }); 
		}
		
	});
	
	
});