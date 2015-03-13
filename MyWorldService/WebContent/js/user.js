$(function(){

	findalluser(1);
	function findalluser(page1) {
		$.ajax({
			  type: "post",
			  url: "AdmiUserinfo",
			  data: {action :"showUserinfo",page:page1},
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
							  var sex,email,phone;
							  if(item.sex=="0"){
								  sex="男";
							  }else if(item.sex=="1"){
								  sex="女";
							  }else if(item.sex=="2"){
								  sex="不详";
							  }
							  if(item.email==null || item.email == ""){
	
								  email="暂无";
							  }else{
								  email=item.email;
							  }
							  if(item.phone==null || item.phone == ""){
								  phone="暂无";
							  }else{
								  phone=item.phone;
							  }
						     $("#usertb").append("<tr class='content'><td width='60px'><img src='Download?groupImage="+item.img+"' class='himg' /></td><td width='100px'>"+item.munber+"</td><td width='100px'>"+item.nick+"</td><td width='50px'>"+sex+"</td><td width='120px'>"+email+"</td><td width='100px'>"+phone+"</td><td width='140px'> <input type='button' name='open' value='开启' id='openButton"+item.munber+"' class='openButton'/><input type='button' name='forbid' value='禁用' id='forbidButton"+item.munber+"' class='forbidButton' /></td></tr>");
						     if(item.status=="2"){
						    	 $("#openButton"+item.munber+"").click(function(){
						    		 if(confirm("确定开启此用户？")){
							    		 $.ajax({
											  type: "post",
											  url: "AdmiUserinfo",
											  data: {action :"openStatus",userid:item.id},
											  dataType:"json", 
											  success : function(data){
												  if(data.isopen=="0"){
													  alert("开启此用户成功！");
													  findalluser(page);
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
											  url: "AdmiUserinfo",
											  data: {action :"forbidStatus",userid:item.id},
											  dataType:"json", 
											  success : function(data){
												  if(data.isforbid=="0"){
													  alert("禁用此用户成功！");
													  findalluser(page);
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
						     
						     
						     
						  } 
		              });
					     
							$("#pre").click(function(){
								if(page > 1 ){
									page =page -1;
									$.ajax({
										  type: "post",
										  url: "AdmiUserinfo",
										  data: {action :"pagebutton",page:page},
										  dataType:"json", 
										  success : function(data){
											  $(".content").remove();
											  $.each(data, function(i,item) {
												  var sex,email,phone;
												  if(item.sex=="0"){
													  sex="男";
												  }else if(item.sex=="1"){
													  sex="女";
												  }else if(item.sex=="2"){
													  sex="不详";
												  }
												  if(item.email==null){
													  
													  email="暂无";
												  }else{
													  email=item.email;
												  }
												  if(item.phone==null){
													  phone="暂无";
												  }else{
													  phone=item.phone;
												  }
												  $("#usertb").append("<tr class='content'><td width='60px'><img src='Download?groupImage="+item.img+"' class='himg' /></td><td width='100px'>"+item.munber+"</td><td width='100px'>"+item.nick+"</td><td width='50px'>"+sex+"</td><td width='120px'>"+email+"</td><td width='100px'>"+phone+"</td><td width='140px'> <input type='button' name='open' value='开启' id='openButton"+item.munber+"' class='openButton'/><input type='button' name='forbid' value='禁用' id='forbidButton"+item.munber+"' class='forbidButton' /></td></tr>");
											     if(item.status=="2"){
											    	 $("#openButton"+item.munber+"").click(function(){
											    		 if(confirm("确定开启此用户？")){
												    		 $.ajax({
																  type: "post",
																  url: "AdmiUserinfo",
																  data: {action :"openStatus",userid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isopen=="0"){
																		  alert("开启此用户成功！");
																		  findalluser(page);
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
																  url: "AdmiUserinfo",
																  data: {action :"forbidStatus",userid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isforbid=="0"){
																		  alert("禁用此用户成功！");
																		  findalluser(page);
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
					
							$("#next").click(function(){
								if(page < maxpage){
									page =page + 1;
									$.ajax({
										  type: "post",
										  url: "AdmiUserinfo",
										  data: {action :"pagebutton",page:page},
										  dataType:"json", 
										  success : function(data){
											  $(".content").remove();
											  $.each(data, function(i,item) {
												  var sex,email,phone;
												  if(item.sex=="0"){
													  sex="男";
												  }else if(item.sex=="1"){
													  sex="女";
												  }else if(item.sex=="2"){
													  sex="不详";
												  }
												  if(item.email==null){
													  
													  email="暂无";
												  }else{
													  email=item.email;
												  }
												  if(item.phone==null){
													  phone="暂无";
												  }else{
													  phone=item.phone;
												  }
												 $("#usertb").append("<tr class='content'><td width='60px'><img src='Download?groupImage="+item.img+"' class='himg' /></td><td width='100px'>"+item.munber+"</td><td width='100px'>"+item.nick+"</td><td width='50px'>"+sex+"</td><td width='120px'>"+email+"</td><td width='100px'>"+phone+"</td><td width='140px'> <input type='button' name='open' value='开启' id='openButton"+item.munber+"' class='openButton'/><input type='button' name='forbid' value='禁用' id='forbidButton"+item.munber+"' class='forbidButton' /></td></tr>");
											     if(item.status=="2"){
											    	 $("#openButton"+item.munber+"").click(function(){
											    		 if(confirm("确定开启此用户？")){
												    		 $.ajax({
																  type: "post",
																  url: "AdmiUserinfo",
																  data: {action :"openStatus",userid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isopen=="0"){
																		  alert("开启此用户成功！");
																		  findalluser(page);
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
																  url: "AdmiUserinfo",
																  data: {action :"forbidStatus",userid:item.id},
																  dataType:"json", 
																  success : function(data){
																	  if(data.isforbid=="0"){
																		  alert("禁用此用户成功！");
																		  findalluser(page);
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