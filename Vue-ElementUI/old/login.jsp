<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (!cn.sunline.framework.ConfigHelper.getInstance().isConfigFileOK()) {
		response.sendRedirect(request.getContextPath() + "/version/config");
		return;
	}
	String path = request.getContextPath();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%--<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css" type="text/css" media="all" />--%>
	<link href="<%=path %>/bootstrap3/css/bootstrap.min.css" rel="stylesheet">

	<script src="<%=path%>/jquery/jquery-1.11.1.js" type="text/javascript" ></script>
	<script src="<%=path%>/jqplugin/jquery.fileDownload.js"	type="text/javascript"></script>
	<script src="<%=path%>/jqplugin/browser.info.js"	type="text/javascript"></script>

	<title>信息科技动态风险监测系统</title>
</head>

<style type="text/css">
	.login-left{
		/*background: #8c8cd6;
		background: #45454a;*/
		padding: 0px;
	}
	.bg-img{
		width:100%;
		height:100%;
		/*opacity: 0.3;*/
	}
	.login-right{
		background-color: white;
		height: 100%;
	}
	.login-header{
		margin: 15px 0 40px;
		font-size: 18px;
		text-align: center;
	}
	.login-account, .login-password{
		margin-top: 16px;
		border-bottom: 1px solid #d9d1d1;
		border-radius: 0;
	}
	.login-account img, .login-password img{
		width: 25px;
		height: 25px;
		margin-right: 20px;
	}
	.login-input{
		-webkit-box-flex: 1;
		-ms-flex: 1;
		flex: 1;
		padding: 0;
		overflow: hidden;
		font-family: inherit;
		font-size: inherit;
		font-weight: inherit;
		background: transparent;
		border: none;
		outline: none;
		resize: none;
		height:48px;
		width:78%;
	}
	.login-right-container{
		top: 15%;
		position: absolute;
		left: 30px;
		right: 30px;
	}
	.login-btn{
		width: 100%;
		margin-top: 20px;
		height: 36px;
	}
	.login-footer{
		bottom: 50px;
		position: absolute;
		left: 0;
		right: 0;
		color: #6d6d6d;
		text-align: center;
	}
	.app-name{
		margin-top: 15px;
	}
	.warn-info{
		color: red;
		margin-top: 20px;
		display: none;
	}
	.download-tips p{
		font-size: 14px !important;
	}
	.download-img{
		text-align: center;
		padding-bottom: 30px;
	}
</style>

<body>
	<div class="">
		<div class="jumbotron" style="height:100%;padding: 0;margin: 0;">
			<div class="container" style="padding: 0;margin: 0;width: 100%">
				<div class="col-md-9 col-lg-9 login-left">
					<img class="bg-img" src="<%=path%>/images/login-bg0.jpg"></img>
				</div>
				<div class="col-md-3 col-lg-3 login-right">
					<div class="login-right-container">
						<div class="login-header">
							<img class="hf-img" src="<%=path%>/images/HF-Logo-120.png"></img>
							<div class="app-name"><span>信息科技动态风险监测</span></div>
						</div>
						<div class="login-container">
							<form>
								<div class="login-account">
									<img src="<%=path%>/images/login-username.png">
									<input type="text" id="username" name="username" class="login-input" placeholder="用户名">
								</div>
								<div class="login-password">
									<img src="<%=path%>/images/login-password.png">
									<input type="password" id="password" name="password" class="login-input" placeholder="密码">
								</div>
								<div class="warn-info">
									<span id="warnInfo"></span>
								</div>
								<button type="button" class="btn btn-primary login-btn" onClick="login()">登录</button>
							</form>
						</div>
						<div class="download-browser" style="display:none">
							<div class="download-img">
								<img src="<%=path %>/images/firefox.jpg"  onclick="downloadBrowser();" style="cursor: pointer;" title="由于您登陆本系统使用的浏览器版本过低，请点击下载火狐浏览，安装后，使用火狐浏览器登陆本系统，谢谢！" />
							</div>
							<div class="download-tips">
								<p align="center" >由于您登陆本系统使用的浏览器版本过低，请按照如下指示操作：</p>
								<p>1、点击上面的“火狐浏览器”图片，弹出下载对话框</p>
								<p>2、保存火狐浏览器文件到电脑中，开始下载</p>
								<p>3、下载完成后，在电脑中找到该文件，点击运行，按照向导安装</p>
								<p>4、安装完成后，使用火狐浏览器登陆系统</p>
							</div>
						</div>
					</div>
					<div class="login-footer">恒丰银行版权所有</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$('#username').focus();
		document.onkeydown = function(e){
			var ev = document.all ? window.event : e;
			if(ev.keyCode==13) {
				login();
			}
		}
	});

    checkBrowser();

	function login(){
		var userName = document.getElementById("username").value;
		var password = document.getElementById("password").value;
		/*if(!check()){
			return;
		}*/
		if(userName == ""){
            $("#warnInfo").html("").html("用户名不能为空");
            $(".warn-info").css("display","block");
		}else if(password == ""){
            $("#warnInfo").html("").html("密码不能为空");
            $(".warn-info").css("display","block");
		}else{
			var url="<%=request.getContextPath()%>/version/RMIServlet";

			var tem = [{userName:userName,password:password}];

			var systemObj = {
				className:'AuthService',
				methodName:'getLoginUser',
				params:JSON.stringify(tem)
			};

			var getRoles = $.post(url, systemObj,
					function(items){
						callback(items);
					});

			getRoles.error(function(){
                $("#warnInfo").html("").html("出现错误,请联系管理员.");
                $(".warn-info").css("display","block");
			});

			function callback(data){
				var dataObj = $.parseJSON(data);
				if(dataObj.retCode=='0') {
					var cbObj = dataObj.result;
					var bl = cbObj[0].bl;
					var tip = cbObj[0].tip;
					if (bl) {
						location.href="<%=request.getContextPath()%>/logRedirect.jsp";
					} else {
                        $("#warnInfo").html("").html(tip);
                        $(".warn-info").css("display","block");
					}
				} else {
                    $("#warnInfo").html("").html("获取用户信息失败！");
                    $(".warn-info").css("display","block");
				}
			}
		}
	}

    function checkBrowser(){
        var browser= $browserInfo$.getBrowserInfo();
        if(browser){
            var verNum= browser.version.match(/^\d+/)[0];
            if(browser.type === "ie"){
                if(verNum < 10){
                    setDisplayDownload();
                    //alert('你的IE浏览器版本太低,请使用IE10或以上版本，请点击右上角链接，下载火狐浏览器！');
                    return false;
                }
            }else if(browser.type === "firefox"){
                if(verNum < 30){
                    setDisplayDownload();
                    return false;
                }
            }else if(browser.type === "chrome"){
                if(verNum < 29){
                    setDisplayDownload();
                    return false;
                }
            }
        }
        return true;
    }

    function setDisplayDownload(){
        $(".login-header").css("display","none");
        $(".login-container").css("display","none");
        $(".download-browser").css("display","block");
	}

    function downloadBrowser(){
        $.fileDownload('<%=path %>/download/Firefox.exe', {
            successCallback: function (url) {
            },
            failCallback: function (html, url) {
            }
        });
    }
</script>
</html>