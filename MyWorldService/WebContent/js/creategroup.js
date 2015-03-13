// 使用 jQuery 异步提交表单
 $(function(){
	 var winwidth = 0;
		if (window.innerWidth) {
			winwidth = window.innerWidth - 200;
			$("#middle").css("width", winwidth + 'px');
			$("#cgtop").css("width", winwidth + 'px');
		} else if ((document.body) && (document.body.clientWidth)) {
			winwidth = window.clientWidth - 200;
			$("#middle").css("width", winwidth + 'px');
			$("#cgtop").css("width", winwidth + 'px');
		} 
	 
	 $("#group_name").blur(function(){
		 if($("#group_name").val()==""){
			 $("#cmsg").html("群名称不能为空");
			 $("#group_name").focus();
	     }else if( $("#group_name").val().length > 7 ){
	    	 $("#cmsg").html("群名称长度不能超过7");
			 $("#group_name").select();
	     }else{
	    	 $("#cmsg").html("");
	     }
	 });
	 $("#cgindex").click(function(){
			location.href="index.html";
		});
     $("#cgform").ajaxForm({
     //定义返回JSON数据，还包括xml和script格式
     type: "post",
	 url: "GroupManage",
	 data: {action: "creategroup"},
     dataType:'json',
     beforeSubmit: function() {
         //表单提交前做表单验证
    	 var filepath=$("#group_head_pic").val();
    	 var extStart=filepath.lastIndexOf(".");
    	 var ext=filepath.substring(extStart,filepath.length).toUpperCase();
    	 if(filepath == ""){
    		 $("#imgerror").html("头像不能为空");
    		 return false;
    	 }else if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
            $("#imgerror").html("头像必须是图片格式");
            return false;
         }else {
        	 $("#imgerror").html("");
         }
    	 if($("#group_name").val()==""){
    		 $("#cmsg").html("群名称不能为空");
			 $("#group_name").focus();
    		 return false;
    	 }else if( $("#group_name").val().length > 7 ){
	    	 $("#cmsg").html("群名称长度不能超过7");
			 $("#group_name").select();
			 return false;
	     }else{
	    	 $("#cmsg").html("");
	     }
    	 return true;
         
     },
     success: function(data) {
    	  //提交成功后调用
    	 if(data.is=="pass"){
    		 $("#imgerror").html("图片大小不能超过100K");
		  }else if(data.is=="low"){
			  $("#imgerror").html("头像图片过小或者不存在");
		  }else{
			  if(data.result=="0"){
				  	location.href = "index.html";
		    	  }else{
		    		 $("#cg_error").html("创建群失败");
		    	  }  
		  }
    	  
        }
     });
 });
