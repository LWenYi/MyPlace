// 使用 jQuery 异步提交表单
 $(function(){
	 var winwidth = 0;
	if (window.innerWidth) {
		winwidth = window.innerWidth - 200;
		$("#middlem").css("width", winwidth + 'px');
		$("#mggtop").css("width", winwidth + 'px');
	} else if ((document.body) && (document.body.clientWidth)) {
		winwidth = window.clientWidth - 200;
		$("#middlem").css("width", winwidth + 'px');
		$("#mggtop").css("width", winwidth + 'px');
	}
	$("#mgindex").click(function(){
		location.href="index.html";
	});
	 $.ajax({
		  type: "post",
		  url: "GroupManage",
		  data: {action1:"showgroupinfo",action:"modifygroupprofile"},
		  dataType:"json",
		  success : function(data){

				  $("#m_gheadimg").attr("src","Download?groupImage="+data.groupImage+"");
				  $("#m_gname").val(data.groupName);
				  if(data.groupType=="0"){
					  $("#m_gkind option[value='0']").attr("selected","selected");
				  }
				  if(data.groupType=="1"){
					  $("#m_gkind option[value='1']").attr("selected","selected");
				  }
				  if(data.groupType=="2"){
					  $("#m_gkind option[value='2']").attr("selected","selected");
				  }
				  if(data.groupType=="3"){
					  $("#m_gkind option[value='3']").attr("selected","selected");
				  }
				  var groupsize ;
				  if(data.groupsize=="10"){
					  groupsize = "10人";
					  //$("#m_gsize option[value='10']").attr("selected","selected");
				  }
				  if(data.groupsize=="30"){
					  groupsize = "30人";
					  //$("#m_gsize option[value='20']").attr("selected","selected");
				  }
				  if(data.groupsize=="50"){
					  groupsize = "50人";
					  //$("#m_gsize option[value='30']").attr("selected","selected");
				  }
				  
				  $("#m_groupsize").html(groupsize);
				  $("#m_gdesc").val(data.desc);

		  }
	 });
	 $("#m_gname").blur(function(){
		 if($("#m_gname").val()==""){
			 $("#mgsg").html("群名称不能为空");
			 $("#m_gname").focus();
			 return false;
	     }else if( $("#m_gname").val().length > 7){
	    	 $("#mgsg").html("群名称长度不能超过7");
			 $("#m_gname").select();
	     }else{
	    	 $("#mgsg").html("");
	     }
	 });
     $("#mgform").ajaxForm({
     //定义返回JSON数据，还包括xml和script格式
     type: "post",
     url: "GroupManage",
	 data: {action1:"modify",action:"modifygroupprofile"},
     dataType:'json',
     beforeSubmit: function() {
         //表单提交前做表单验证
    	 var filepath=$("#up_m_gheadimg").val();
    	 //$("#img1").attr("src",filepath);
    	 var extStart=filepath.lastIndexOf(".");
         var ext=filepath.substring(extStart,filepath.length).toUpperCase();
         if(filepath!=""&&ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
            $("#mg_imgerror").html("头像必须是图片格式");
            return false;
         }
         if($("#m_gname").val()=="" ){
        	 $("#m_gname").focus();
    		 return false;
    	 }
         return true;
     },
     success: function(data) {
    	  //提交成功后调用
    	 if(data.is=="pass"){
    		 $("#mg_imgerror").html("图片大小不能超过100K");
		  }else if(data.is=="low"){
			  $("#mg_imgerror").html("头像文件过小或者不存在");
		  }else{
			  if(data.result=="0"){
				  	//$("#mg_error").html("修改成功");
		    		//location.reload();
				  	location.href = "index.html";
		    	  }else{
		    		 $("#mg_error").html("修改失败");
		    	  }  
		  }
    	  
        }
     });
 });