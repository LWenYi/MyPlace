$(function(){
	findallgroup(1);
	function findallgroup(page1) {
		$.ajax({
			  type: "post",
			  url: "AdminGroupinfo",
			  data: {action :"showGroupinfo",page:page1},
			  dataType:"json", 
			  success : function(data){ 
				  $(".content").remove();
				  if(data!=""){
					  var maxpage ;
					  var page = page1 ;
					  $.each(data, function(i,item) {
						  if(i==0){
							  maxpage = item.maxpage;
							  $("#page").html(page);	
							  $("#maxpage").html(maxpage);
						  }else{
							  var groupsize;
							  var grouptype;
							  if(item.groupsize=="10"){
								  groupsize="10人";
							  }
							  if(item.groupsize=="30"){
								  groupsize="30人";
							  }
							  if(item.groupsize=="50"){
								  groupsize="50人";
							  }
							  if(item.grouptype=="0"){
								  grouptype="同事好友";
							  }
							  if(item.grouptype=="1"){
								  grouptype="兴趣爱好";
							  }
							  if(item.grouptype=="2"){
								  grouptype="志愿活动";
							  }
							  if(item.grouptype=="3"){
								  grouptype="外出旅游";
							  }
							  $("#grouptb").append("<tr class='content'><td width='60px'><img src='Download?groupImage="+item.img+"' class='gimg' /></td><td width='60px'>"+item.munber+"</td><td width='80px'>"+item.name+"</td><td width='90px'>"+item.manager+"</td><td width='90px'>"+grouptype+"</td><td width='70px'>"+groupsize+"</td><td width='90px'>"+item.creattime+"</td><td width='140px'> <input type='button' name='open' value='开启' id='openButton"+item.munber+"' class='openButton'/><input type='button' name='forbid' value='禁用' id='forbidButton"+item.munber+"' class='forbidButton'/></td></tr>");											    
						     if(item.status=="2"){
						    	 $("#openButton"+item.munber+"").click(function(){
						    		 if(confirm("确定开启该群？")){
							    		 $.ajax({
											  type: "post",
											  url: "AdminGroupinfo",
											  data: {action :"openStatus",groupid:item.id},
											  dataType:"json", 
											  success : function(data){
												  if(data.isopen=="0"){
													  alert("开启该群成功！");
													  findallgroup(page);
												  }else{
													  alert("开启该群失败！");
												  }
											  }
							    		 });
						    		 }else{
						    			 
						    		 }
							     });
						    	 $("#forbidButton"+item.munber+"").css("opacity","0.5");
						    	 $("#forbidButton"+item.munber+"").removeAttr("cursor");
						     }else{
						    	 $("#forbidButton"+item.munber+"").click(function(){
						    		 if(confirm("确定禁用该群？")){
							    		 $.ajax({
											  type: "post",
											  url: "AdminGroupinfo",
											  data: {action :"forbidStatus",groupid:item.id},
											  dataType:"json", 
											  success : function(data){
												  if(data.isforbid=="0"){
													  alert("禁用该群成功！");
													  findallgroup(page);
												  }else{
													  alert("禁用该群失败！");
												  }
											  }
							    		 });
						    		 }else{
						    			 
						    		 }
							     });
						    	 $("#openButton"+item.munber+"").css("opacity","0.5");
						    	 $("#openButton"+item.munber+"").removeAttr("cursor");
						     }
						     
						     
						     
						  } 
		              });
					  
							$("#pre").click(function(){
								if(page > 1 ){
									page =page -1;
									$.ajax({
										  type: "post",
										  url: "AdminGroupinfo",
										  data: {action :"pagebutton",page:page},
										  dataType:"json", 
										  success : function(data){
											  $(".content").remove();
											  $.each(data, function(i,item) {
												  var groupsize;
												  var grouptype;
												  if(item.groupsize=="10"){
													  groupsize="10人";
												  }
												  if(item.groupsize=="30"){
													  groupsize="30人";
												  }
												  if(item.groupsize=="50"){
													  groupsize="50人";
												  }
												  if(item.grouptype=="0"){
													  grouptype="同事好友";
												  }
												  if(item.grouptype=="1"){
													  grouptype="兴趣爱好";
												  }
												  if(item.grouptype=="2"){
													  grouptype="志愿活动";
												  }
												  if(item.grouptype=="3"){
													  grouptype="外出旅游";
												  }
												  
												  $("#grouptb").append("<tr class='content'><td width='60px'><img src='Download?groupImage="+item.img+"' class='gimg' /></td><td width='60px'>"+item.munber+"</td><td width='80px'>"+item.name+"</td><td width='90px'>"+item.manager+"</td><td width='90px'>"+grouptype+"</td><td width='70px'>"+groupsize+"</td><td width='90px'>"+item.creattime+"</td><td width='140px'> <input type='button' name='open' value='开启' id='openButton"+item.munber+"' class='openButton'/><input type='button' name='forbid' value='禁用' id='forbidButton"+item.munber+"' class='forbidButton'/></td></tr>");											     if(item.status=="2"){
											    	 $("#openButton"+item.munber+"").click(function(){
											    		 if(confirm("确定开启该群？")){
												    		 $.ajax({
																  type: "post",
																  url: "AdminGroupinfo",
																  data: {action :"openStatus",groupid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isopen=="0"){
																		  alert("开启该群成功！");s
																		  findallgroup(page);
																	  }else{
																		  alert("开启该群失败！");
																	  }
																  }
												    		 });
											    		 }else{
											    			 
											    		 }
												     });
											    	 $("#forbidButton"+item.munber+"").css("opacity","0.5");
											    	 $("#forbidButton"+item.munber+"").removeAttr("cursor");
											     }else{
											    	 $("#forbidButton"+item.munber+"").click(function(){
											    		 if(confirm("确定禁用该群？")){
												    		 $.ajax({
																  type: "post",
																  url: "AdminGroupinfo",
																  data: {action :"forbidStatus",groupid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isforbid=="0"){
																		  alert("禁用该群成功！");
																		  findallgroup(page);
																	  }else{
																		  alert("禁用此用户失败该群！");
																	  }
																  }
												    		 });
											    		 }else{
											    			 
											    		 }
												     });
											    	 $("#openButton"+item.munber+"").css("opacity","0.5");
											    	 $("#openButton"+item.munber+"").removeAttr("cursor");
											    	
											     }
											  });
										  }
									});
									
									$("#page").html(page);
					
								}
								
							});
					
							$("#next").click(function(){
								if(page < maxpage){
									page =page + 1;
									$.ajax({
										  type: "post",
										  url: "AdminGroupinfo",
										  data: {action :"pagebutton",page:page},
										  dataType:"json", 
										  success : function(data){
											  $(".content").remove();
											  $.each(data, function(i,item) {
												  var groupsize;
												  var grouptype;
												  if(item.groupsize=="10"){
													  groupsize="10人";
												  }
												  if(item.groupsize=="30"){
													  groupsize="30人";
												  }
												  if(item.groupsize=="50"){
													  groupsize="50人";
												  }
												  if(item.grouptype=="0"){
													  grouptype="同事好友";
												  }
												  if(item.grouptype=="1"){
													  grouptype="兴趣爱好";
												  }
												  if(item.grouptype=="2"){
													  grouptype="志愿活动";
												  }
												  if(item.grouptype=="3"){
													  grouptype="外出旅游";
												  }
												  
												  $("#grouptb").append("<tr class='content'><td width='60px'><img src='Download?groupImage="+item.img+"' class='gimg' /></td><td width='60px'>"+item.munber+"</td><td width='80px'>"+item.name+"</td><td width='90px'>"+item.manager+"</td><td width='90px'>"+grouptype+"</td><td width='70px'>"+groupsize+"</td><td width='90px'>"+item.creattime+"</td><td width='140px'> <input type='button' name='open' value='开启' id='openButton"+item.munber+"' class='openButton'/><input type='button' name='forbid' value='禁用' id='forbidButton"+item.munber+"' class='forbidButton'/></td></tr>");											     if(item.status=="2"){
											    	 $("#openButton"+item.munber+"").click(function(){
											    		 if(confirm("确定开启此用户？")){
												    		 $.ajax({
																  type: "post",
																  url: "AdminGroupinfo",
																  data: {action :"openStatus",groupid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isopen=="0"){
																		  alert("开启此用户成功！");
																		  findallgroup(page);
																	  }else{
																		  alert("开启此用户失败！");
																	  }
																  }
												    		 });
											    		 }else{
											    			 
											    		 }
												     });
											    	 $("#forbidButton"+item.munber+"").css("opacity","0.5");
											    	 $("#forbidButton"+item.munber+"").removeAttr("cursor");
											     }else{
											    	 $("#forbidButton"+item.munber+"").click(function(){
											    		 if(confirm("确定禁用此用户？")){
												    		 $.ajax({
																  type: "post",
																  url: "AdminGroupinfo",
																  data: {action :"forbidStatus",groupid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isforbid=="0"){
																		  alert("禁用此用户成功！");
																		  findallgroup(page);
																	  }else{
																		  alert("禁用此用户失败！");
																	  }
																  }
												    		 });
											    		 }else{
											    			 
											    		 }
												     });
											    	 $("#openButton"+item.munber+"").css("opacity","0.5");
											    	 $("#openButton"+item.munber+"").removeAttr("cursor");
											     }
											  });
										  }
									});
									$("#page").html(page);	
								}
							});
						
						
				  }else{
					  $("#page").html(1);	
					  $("#maxpage").html(1); 
				  }	
			  }
		});
	};
	
	
});