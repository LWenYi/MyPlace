$(function(){
	$("#admin").blur(function(){
		if($("#admin").val() == ""){
			$("#error").html("账号不能为空");
			$("#admin").focus();
		}else{
			$("#error").html("");
		}
	});
	$("#adminpsw").blur(function(){
		if($("#adminpsw").val() == ""){
			$("#error").html("密码不能为空");
			$("#adminpsw").focus();
		}else{
			$("#error").html("");
		}
	});
	$("#Adminlogin").click(function(){
		if($("#error").val().length == 0 && $("#admin").val() == "" ){
			$("#error").html("账号不能为空");
		}else if($("#error").val().length == 0 && $("#adminpsw").val() == ""){
			$("#error").html("密码不能为空");
		}else if($("#error").val().length == 0 && $("#admin").val() != "" && $("#adminpsw").val() != "" ){
			$.ajax({
				  type: "post",
				  url: "AdminLoginServlet",
				  data: {adminname:$("#admin").val(),adminpassword:$("#adminpsw").val()},
				  dataType:"json", 
				  success : function(data){
					 if(data.islogin =="1" ){
						 $("#error").html("账号或密码错误");
					 }else{
						 location.href = "adminIndex.html";
						 
					 }
				  }
			});
		}
		
	});
	
	
});