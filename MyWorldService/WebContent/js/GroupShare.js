$(function() {
	var winwidth = 0;
	if (window.innerWidth) {
		winwidth = window.innerWidth - 200;
		$("#middle").css("width", winwidth + 'px');
		$("#topshare").css("width", winwidth + 'px');
		$("#sharepart").css("width", winwidth + 'px');
	} else if ((document.body) && (document.body.clientWidth)) {
		winwidth = window.clientWidth - 200;
		$("#middle").css("width", winwidth + 'px');
		$("#topshare").css("width", winwidth + 'px');
		$("#sharepart").css("width", winwidth + 'px');
	} 
	
	$("#shareindex").click(function(){
		location.href="index.html";
	});
	var bar = $('.bar');
	var percent = $('.percent');
	$(".progress").css("display", "none");
	$.ajax({
		  type: "post",
		  url: "GroupManage",
		  data: {action1:"showgroupshare",action:"groupshare"},
		  dataType:"json",
		  success : function(data){
			  $("#mysharetb").empty();
			  $("#othersharetb").empty();
			  $.each(data, function(i,item) {
				  if(item.isme == "0"){
					  $("#mysharetb").append("<tr><td width='15%'><img src='Download?groupImage="+item.image+"' id='sharepic'/></td><td width='25%'>"+item.content+"</td><td width='25%'>"+item.uploadTime+"</td><td width='20%'>"+item.usernick+"</td><td width='15%'><input type='button' value='删除' id='sharedelete"+item.id+"' class='sharedelete'/></td></tr>");
					  $("#sharedelete"+item.id+"").click(function() {
						  $.ajax({
							  type: "post",
							  url: "GroupManage",
							  data: {action1:"deletegroupshare",deleteid:item.id,action:"groupshare"},
							  dataType:"json",
							  success : function(data){
								  if(data.isdelete == "0"){
									location.reload();
								  }else{
									alert("删除失败");
								  }
							  }
						 });
						});
				  }else if(item.isme == "1"){
					  $("#othersharetb").append("<tr><td width='15%'><img src='Download?groupImage="+item.image+"' id='sharepic'/></td><td width='25%'>"+item.content+"</td><td width='25%'>"+item.uploadTime+"</td><td width='20%'>"+item.usernick+"</td><td width='15%'><input type='button' value='下载' id='downfilebutton"+item.id+"' class='downfilebutton'/><a href='Download?groupImage="+item.image+"' style='display:none;' id='downfile"+item.id+"'>下载</a></td></tr>");
					  $("#downfilebutton"+item.id+"").click(function() {
						  $("#downfile"+item.id+"").get(0).click();
					  });
				  }
				  
			  });
		  }
	 });
	 $("#mgform").ajaxForm({
	     //定义返回JSON数据，还包括xml和script格式
	     type: "post",
		 url: "GroupManage",
		 data: {action1:"addGroupShare",action:"groupshare"},
	     dataType:'json',
	     beforeSubmit: function() {
	         //表单提交前做表单验证
	    	 var filepath=$("#headimg").val();
	    	 //$("#img1").attr("src",filepath);
	    	 if(filepath ==""){
	    		 $("#uppic_error").html("请选择上传的图片！");
		         return false; 
	    	 }
	    	 var extStart=filepath.lastIndexOf(".");
	         var ext=filepath.substring(extStart,filepath.length).toUpperCase();
	         if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
	        	 $("#uppic_error").html("图片限于bmp,png,gif,jpeg,jpg格式");
	            return false;
	         }else{
	             return true;
	         }
	
	     },
	     beforeSend: function() {
	         
	     	$(".progress").css("display", "block");
	         var percentVal = '0%';
	         bar.width(percentVal);
	         percent.html(percentVal);
	     },
	     uploadProgress: function(event, position, total, percentComplete) {
	         var percentVal = percentComplete + '%';
	         bar.width(percentVal);
	         percent.html(percentVal);
	 		//console.log(percentVal, position, total);
	     },
	     success: function(data) {
	    	  //提交成功后调用
	    	 if(data.is=="pass"){
	    		 $("#uppic_error").html("头像文件的大小不能超过50兆");
			  }else if(data.is=="low"){
				  $("#uppic_error").html("头像文件过小或者不存在");
			  }else{
				  if(data.result=="0"){
					     var percentVal = '100%';
				         bar.width(percentVal)
				         percent.html(percentVal);
			    		 location.reload();
			    	  }else{
			    		 alert("上传失败");
			    	  }  
			  }
	    	  
	        },complete: function(xhr) {
	    		
	    	}
	     });

});