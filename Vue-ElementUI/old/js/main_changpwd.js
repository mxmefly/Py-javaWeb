function logout() {
	window.close();
};

function closeJsp(){
	if($("#changepwddialog").dialog('isOpen')){
		$("#changepwddialog").dialog('close');
	}
}

/*var pwdBts =  [{
     id: "saveUserBtn",
     text: "确定",
     click: function() {saveUserPwd()}
 },{
     id: "closeBtn",
     text: "取消",
     click: function() {closeJsp()}
 }];*/

function changepwd(){
	$("#changepwddialog").modal();
	//$("#changepwddialog").dialog({ modal: true,minHeight:40,maxHeight:200,width:300,title:"修改密码",buttons:pwdBts});
}


function dataCheck(){
	console.log("----------")
	if($("#old_user_password").val()==''){
    	//alert('旧密码不能为空!');
		swal("警告", "旧密码不能为空！","warning");

    	return false;
    }
	if($("#new_user_password").val()==''){
    	//alert('新密码不能为空空!');
		swal("警告", "新密码不能为空空！","warning");
    	return false;
    }
	if($("#new_user_password").val() != $("#user_confirm_password").val()){
    	//alert('两次输入的用户密码不一样!');
		swal("警告", "两次输入的用户密码不一样！","warning");
    	return false;
    }
	return true;
}


function saveUserPwd(){
	if(!dataCheck()){
		return;
	}
	var userObj = [{
		userId:$("#user_id").val(),
		userPwd:$("#new_user_password").val(),
		userDesc:$("#old_user_password").val()
	}];
	
	var paramObj = {
		className:'PermissionManagerServices',
		methodName:'updateUserPwd',
		params:lang.toJSONString(userObj)
	};
	
	var postUser = $.post(url, paramObj, 
		function(data){
			var dataObj=eval("("+data+")");
			console.log(dataObj)
			if(dataObj && dataObj.result){
				if(dataObj.result.success){
					//alert(dataObj.result.msg);
					swal("成功", dataObj.result.msg,"success");
					$("#changepwddialog").modal("hide");
					 resetForm();
				}else{
					//alert(dataObj.result.msg);
					swal("失败", dataObj.result.msg,"error");
					if(dataObj.result.msg.indexOf("会话") != -1){
						location.href = contextPath + "/session_out.jsp";
					}
				}
		    } else {
		    	alert("服务器异常，请重新登录后再修改密码！");
		    }
	});
	postUser.error(function(){ alert("创建用户失败,请联系管理员."); });
}


function resetForm(){
	$("#new_user_password").val(""),
	$("#old_user_password").val("")
	$("#user_confirm_password").val("");
}
