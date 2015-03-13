$(function(){
	//忘记密码  
	$(".fgpsw").click(function(){
		$('#popWindow').css("display", "block");
		$('#forgotpsw').css("display", "block");
		$('#loginLayer').css("display", "none");
		
	});
	$('#forgetpsw_close').click(function() {
		$('#popWindow').css("display", "none");
		$('#forgotpsw').css("display", "none");
		$(".fpssw_error").html("");
		$("#fg_username").val("");
		$("#fg_mail").val("");
		$('#loginform')[0].reset(); 
	    $("#error_message").html("");
	    $('#registform')[0].reset(); 
	    $('#rg_error_message').html("");
	});
	
	$("#fgbutton").click(function(){
		
		 $.ajax({
			  type: "post",
			  url: "ForgetPassword",
			  data: {username:$("#fg_username").val(),mail:$("#fg_mail").val()},
			  dataType:"json", 
			  success : function(data){
				  $(".fpssw_error").css("color","red");
				  if(data.result=="nouser"){
					  $(".fpssw_error").html("输入用户账号有误，请重新输入");
				  }
				  if(data.result=="falsemain"){
					  $(".fpssw_error").html("输入用户邮箱有误，请重新输入");
				  }
				  if(data.result=="false"){
					  $(".fpssw_error").html("发送失败");
				  }
				  if(data.result=="success"){
					 $(".fpssw_error").css("color","green");
					 $(".fpssw_error").html("发送成功，请查收邮箱");
					  
				  }
			  }
		  }); 
	});
	
	//登陆处理
	$('#login_btn').click(function () {
		  $.ajax({
			  type: "post",
			  url: "LoginServletWeb",
			  data: {phone: $('#lphone').val(),psw:$('#lpassword').val()},
			  success : function(data){
				  if( $('#lphone').val()==""){
					  $("#error_message").html("用户名不能为空!");	
				  }else if($('#lpassword').val()==""){
					  $("#error_message").html("密码不能为空!");	
				  }else if(data=="false"){
					  $("#error_message").html("账号或密码错误，请重新输入!");
				  }else if(data=="forgin"){
					  $("#error_message").html("账号被冻结！");
				  }else{
					  window.location.reload();
				  }
				
			  }
		  }); 
	 });
   //转向注册或登陆
	$('#create').click(function () {
	    $('#loginform')[0].reset(); 
	    $("#error_message").html("");
		$('#loginLayer').css("display", "none");
		$('#registLayer').css("display", "block");
	 }); 
	 $('#go_login').click(function () {
		    $('#registform')[0].reset(); 
		    $('#rg_error_message').html("");
			$('#loginLayer').css("display", "block");
			$('#registLayer').css("display", "none");
			$("#tf1,#tf2,#tf3,#tf4").removeClass("rtrue");
			$("#tf1,#tf2,#tf3,#tf4").removeClass("rfalse");

	 });
	 //验证注册账号
	 $("#rphone").blur(function(){
		 if($('#rphone').val()==""){
			 $("#tf1").removeClass("rtrue"); 
			 $("#rg_error_message").html("账号不能为空，请重新输入!");
			 $("#tf1").addClass("rfalse");
		 }else if(!(/^1[3|4|5|8][0-9]\d{8}$/.test($('#rphone').val()))){
			 $("#tf1").removeClass("rtrue"); 
			 $("#rg_error_message").html("手机号格式有误，请重新输入!");
			 $("#tf1").addClass("rfalse");
		 }else{
			 $.ajax({
				  type: "post",
				  url: "RegisterServletWeb",
				  data:{phone: $('#rphone').val(),register:"no"},
				  success : function(data){
					  if(data=="1"){
							 $("#tf1").removeClass("rtrue"); 
						     $("#rg_error_message").html("账号已存在，请重新输入!");
							 $("#tf1").addClass("rfalse");
					  }else{
						  $("#rg_error_message").html("");
						  $("#tf1").removeClass("rfalse"); 
						  $("#tf1").addClass("rtrue");
					  }
					
				  }
			  }); 
		 }

	 });
	 //验证注册昵称
	 $(".nickname").blur(function(){
			if($('#rnick').val()==""){
				 $("#tf2").removeClass("rtrue"); 
				 $("#rg_error_message").html("昵称不能为空，请重新输入!");
				 $("#tf2").addClass("rfalse");
			}else if($('#rnick').val().length > 7){
				 $("#tf2").removeClass("rtrue"); 
				 $("#rg_error_message").html("昵称长度不能超过7");
				 $('#rnick').select();
				 $("#tf2").addClass("rfalse");
			}else{
				 $("#tf2").removeClass("rfalse"); 
				 $("#rg_error_message").html("");
				 $("#tf2").addClass("rtrue");
			}
	 });
	 //验证密码
	 $(".psw").blur(function(){
			if($('#rpsw').val()==""){
				 $("#tf3").removeClass("rtrue");
				 $("#rg_error_message").html("密码不能为空，请重新输入!");
				 $("#tf3").addClass("rfalse");
			}else if($('#rpsw').val().length<6 ||$('#rpsw').val().length>18){
				 $("#tf3").removeClass("rtrue");
				 $("#rg_error_message").html("密码必须为6到18位，请重新输入!");
				 $("#tf3").addClass("rfalse");
			}else{
				 $("#tf3").removeClass("rfalse");
				 $("#rg_error_message").html("");
				 $("#tf3").addClass("rtrue");
			}
	 });
	 $(".againpsw").blur(function(){
			if($('#rpsw').val()!=$('#againpsw').val()){
				 $("#tf4").removeClass("rtrue");
				 $("#rg_error_message").html("两次输入密码不一致，请重新输入!");
				 $("#tf4").addClass("rfalse");
			}else{
				 $("#tf4").removeClass("rfalse");
				 $("#rg_error_message").html("");
				 $("#tf4").addClass("rtrue");
			}
	 });
	 //提交注册
	 $('#regist_btn').click(function () {
		   $(".rfalse").parent().prev().children().trigger("blur");
		   if($(".rtrue").length==4 && $(".checkbox").attr("checked")==true){
			   $.ajax({
					  type: "post",
					  url: "RegisterServletWeb",
					  data:{phone: $('#rphone').val(),nick:$('#rnick').val(),psw:$('#rpsw').val(),register:"ok"},
					  success : function(data){
						  window.location.reload();
					  }
				  }); 
			} 
			    return false;
	 });
});