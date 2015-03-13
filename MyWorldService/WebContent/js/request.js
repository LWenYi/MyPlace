$(function(){
	$("#request_close").click(function(){
		//关闭发送信息的蒙版
		$('#popWindow').css("display", "none");
		$('#request_pop').css("display", "none");	
		//清空
		$("#addTable").empty();
		$("#add_message").empty();
		$("#addfriend_text").val("");
		$("#request_message").empty();
		$("#request_txt").val("");
		$("#addTable_g").empty();
		$("#add_gmessage").empty();
		$("#addgroup_text").val("");

	});
	
	$("#message_close").click(function(){
		//关闭消息的蒙版
		$('#popWindow').css("display", "none");
		$('#message_pop').css("display", "none");
		 location.href = "index.html";
		
	});
	
});