// 使用 jQuery 异步提交表单
 $(function(){
	 var winwidth = 0;
	 var pstwidth = 0;
	 var psinfotwidth = 0;
	 var rightpart = 0;
		if (window.innerWidth) {
			winwidth = window.innerWidth - 200;
			$("#middlepart").css("width", winwidth + 'px');
			$("#toppart").css("width", winwidth + 'px');
			pstwidth = winwidth - 40;
			$("#pst").css("width", pstwidth + 'px');
			rightpart = winwidth - 130;
			$("#rightpart").css("width", rightpart + 'px');
			psinfotwidth = winwidth - 200;
			$("#personinfo").css("width", psinfotwidth + 'px');
			$("#modifypsw").css("width", psinfotwidth + 'px');
			
		} else if ((document.body) && (document.body.clientWidth)) {
			winwidth = window.clientWidth - 200;
			$("#middlepart").css("width", winwidth + 'px');
			$("#toppart").css("width", winwidth + 'px');  
			pstwidth = winwidth - 40;
			$("#pst").css("width", pstwidth + 'px');
			rightpart = winwidth - 130;
			$("#rightpart").css("width", rightpart + 'px');
			psinfotwidth = winwidth - 200;
			$("#personinfo").css("width", psinfotwidth + 'px');
			$("#modifypsw").css("width", psinfotwidth + 'px');s
	
		} 
 
	 $.ajax({
		  type: "post",
		  url: "ModifyUserInfo",
		  data: {action:"showuserinfo"},
		  dataType:"json",
		  success : function(data){;
				  $("#person_head_pic").attr("src","Download?groupImage="+data.uImage+"");
				  $("#munber").html(data.uName);
				  if(data.uAddress == "null"){
					  $("#address").val("");
				  }else{
					  $("#address").val(data.uAddress);
				  }
				  if(data.uEmail == "null"){
					  $("#email").val("");
				  }else{
					  $("#email").val(data.uEmail);
				  }
				  if(data.uDesc == "null"){
					  $("#desc").val("");
				  }else{
					  $("#desc").val(data.uDesc);
				  }
				  if(data.uBirthday == "null"){
					  $(".biuuu1").val("");
				  }else{
					  $(".biuuu1").val(data.uBirthday);
				  }
				  $("#nick").val(data.uNick);
				  $("#phone").val(data.uPhone);
				  if(data.uGender=="0"){
					  $(":radio[name='mGender'][value='0']").attr("checked",true);
				  }
				  if(data.uGender=="1"){
					  $(":radio[name='mGender'][value='1']").attr("checked",true);
				  }
				  
				  if(data.uIsShare=="0"){
					  $(":radio[name='mIsShare'][value='0']").attr("checked",true);
				  }
				  if(data.uIsShare=="1"){
					  $(":radio[name='mIsShare'][value='1']").attr("checked",true);
				  }
				  

		  }
	 });
	 $("#nick").blur(function(){
		 if($("#nick").val()==""){
			 $("#ps_nick").html("昵称不能为空");
			 $("#nick").focus();
	     }else if($("#nick").val().length > 7 ){
	    	 $("#ps_nick").html("群名称长度不能超过7");
			 $("#nick").select(); 	
	     } else{
	    	 $("#ps_nick").html("");
	     }
	 });
	 $("#phone").blur(function(){
		 if(isNaN($("#phone").val())){
	         	$("#ps_phone").html("联系电话只能为纯数字");
	         	$("#phone").select();
		 }else{
	    	 $("#ps_phone").html("");
	     }
	 });
	 $("#email").blur(function(){
		 var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;;
         if(!reg.test($("#email").val())&&$("#email").val()!=""){
         	$("#ps_email").html("邮箱格式不正确");
         	$("#ps_email").select();
          }else{
        	  $("#ps_email").html("");
          }
	 });
     $(".contact_form").ajaxForm({
     //定义返回JSON数据，还包括xml和script格式
     type: "post",
	 url: "ModifyUserInfo",
	 data: {action:"modify"},
     dataType:'json',
     beforeSubmit: function() {
         //表单提交前做表单验证
    	 var filepath=$("#headimg").val();
    	 var extStart=filepath.lastIndexOf(".");
         var ext=filepath.substring(extStart,filepath.length).toUpperCase();
         if(filepath!=""&&ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
            $("#ps_img").html("头像必须是图片格式");
            return false;
         }
         if($("#nick").val()==""){
        	 $("#ps_nick").html("昵称不能为空");
			 $("#nick").focus();
			 return false;
         }
         if($("#nick").val().length > 7 ){
	    	 $("#ps_nick").html("群名称长度不能超过7");
			 $("#nick").select(); 
			 return false;
	     }
         if(isNaN($("#phone").val())){
        	$("#ps_phone").html("联系电话只能为纯数字");
          	return false;
          }
          var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;;
          if(!reg.test($("#email").val())&&$("#email").val()!=""){
          	$("#ps_email").html("邮箱格式不正确");
           	return false;
           }
         return true;
     },
     success: function(data) {
    	  //提交成功后调用
    	 if(data.is=="pass"){
    		 $("#ps_img").html("图片不能超过100K");
		  }else if(data.is=="low"){
			  $("#ps_img").html("图片过小或者不存在");
		  }else{
			  if(data.result=="0"){
				  	 alert("修改成功");
		    		 location.reload();
		    	  }else{
		    		  $("#ps_error").html("修改失败");
		    	  }  
		  }
    	  
        }
     });
 	//修改密码
 	$("#modifypsw_button").click(function () {
 		$("#psw").html("");
 		$("#newpswerror").html("");
 		$("#againpsw").html("");
 		if($("#oldpsw").val() == ""){
 			$("#psw").html("请输入原密码");
 		}else if($("#newpsw").val().length<6 ||$("#newpsw").val().length>18){
 			$("#newpswerror").html("密码必须为6到18位!");
		}else if($("#newpsw").val()!=$("#newpsw2").val()){
			$("#againpsw").html("两次密码不一致");
		}else{
 			$.ajax({
 				  type: "post",
 				  url: "ModifyUserInfo",
 				  data: {action:"modifypassword",oldpsw: $("#oldpsw").val(),newpsw: $("#newpsw").val()},
 				  dataType:"json", 
 				  success : function(data){
 					if(data.issucess=="0"){
 						
 	                    $("#newpsw").val("");
 	                    $("#oldpsw").val("");
 	                    $("#newpsw2").val("");
 	            		$("#psw").html("");
 	            		$("#newpswerror").html("");
 	            		$("#againpsw").html("");
 	            		$("#psw_error").html("修改成功");
 	            		
 					}else if(data.issucess=="1"){
 						 $("#psw_error").html("修改失败");
 					}else{
 						$("#psw").html("原密码有误，请重新输入");			
 					}
 				  }
 			  }); 

 		}
 	});
 });