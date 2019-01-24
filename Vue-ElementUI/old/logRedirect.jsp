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
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css" type="text/css" media="all" />  
	<script src="<%=path%>/jquery/jquery-1.11.1.js" type="text/javascript" ></script>
	<script src="<%=path%>/jqplugin/browser.info.js"	type="text/javascript"></script>
	<script src="<%=path%>/jqplugin/jquery.fileDownload.js"	type="text/javascript"></script>
	<script type="text/javascript">
		if(check()){
			$("#_browser").css("display","none");   
			parent.window.location.href ="<%=request.getContextPath()%>/main.jsp";
		}
	   function setDisplay(){
	   		$("#_browser").css("display","");   		
	   }
	   
	   function check(){
		   var browser= $browserInfo$.getBrowserInfo();
			if(browser){			
				var verNum= browser.version.match(/^\d+/)[0];
				if(browser.type === "ie"){
					if(verNum < 10){
						setDisplay();
						//alert('你的IE浏览器版本太低,请使用IE10或以上版本，请点击右上角链接，下载火狐浏览器！');
						return false;
					}
				}else if( browser.type === "firefox"){
					if(verNum < 30){
						//alert('你的火狐浏览器版本太低,请使用不低于30的火狐浏览器，请点击右上角链接，下载火狐浏览器！');
						setDisplay();
						return false;
					}
				}else if( browser.type === "chrome"){
					if(verNum < 29){
						//alert('你的google浏览器版本太低,请使用不低于29的浏览器版本，请点击右上角链接，下载火狐浏览器！');
						setDisplay();
						return false;
					}
				}
			}
			return true;		
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
</head>
<body>
<div class="main-content">
	<div id="_browser" style="display:''">
		<table width="100%" cellpadding="0" cellspacing="0" >
			<tr><td align="center"><img src="<%=path %>/images/firefox.jpg"  onclick="downloadBrowser();" style="cursor: pointer;" title="由于您登陆本系统使用的浏览器版本过低，请点击下载火狐浏览，安装后，使用火狐浏览器登陆本系统，谢谢！" /></td></tr>
		</table>
		<p align="center" >由于您登陆本系统使用的浏览器版本过低，请按照如下指示操作：</p>
		<p style="padding-left:530px">1、点击上面的“火狐浏览器”图片，弹出下载对话框</p>
		<p style="padding-left:530px">2、保存火狐浏览器文件到电脑中，开始下载</p>
		<p style="padding-left:530px">3、下载完成后，在电脑中找到该文件，点击运行，按照向导安装</p>
 		<p style="padding-left:530px">4、安装完成后，使用火狐浏览器登陆系统</p>
	</div>
</div>
</body>
</html>