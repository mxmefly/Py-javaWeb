<%@page import="cn.sunline.permission.repository.User"%>
<%@page import="cn.sunline.Constants" %>
<%@ page import="org.apache.log4j.Logger" %>
<%--<%@ page import="cn.sunline.saml.respository.entity.UserEntity" %>--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	Logger log = Logger.getLogger(this.getClass());
	String contextPath = request.getContextPath();
	User authUser = (User)session.getAttribute("authUser");
    String userName = "";
    String aliasName = "";
    String userId = "";
    String userType = "";

	userName = (String)request.getAttribute("userIdentity");
	log.info("登录用户："+userName);

	if(userName == null){//没有通过统一认证跳入
		log.info("没有通过统一认证跳入");
		if (authUser == null) {
		    log.info("authUser == null，登录失败，跳转login.jsp重新登录");
			response.sendRedirect(contextPath+"/login.jsp");
		}else {
			log.info("authUser != null，登录成功");
			if("1".equals(authUser.getIsAdmin())){
				userType = "超级管理员";
			} else if("2".equals(authUser.getIsAdmin())){
				userType = "管理员";
			} else {
				userType = "普通用户";
			}
			userName = authUser.getUserName();
			aliasName = authUser.getUserAlias();
			userId = authUser.getUserId();
		}
	}else{//通过统一认证跳入
		/*log.info("通过统一认证跳入");
		log.info("查询登录用户：");
		UserEntity cu = new UserEntity();
		User loginUser = (User)cu.getUser(userName);
		if(loginUser == null){
			log.info("系统中不存在当前登录用户，确认后，请通过管理员同步用户信息");
			response.sendRedirect(contextPath+"/login.jsp");
		}
		//log.info(loginUser.toString());
		session.setAttribute("authUser",loginUser);
		userName = loginUser.getUserName();
		aliasName = loginUser.getUserAlias();
		userId = loginUser.getUserId();*/
	}



    /*if (authUser == null) {
    	response.sendRedirect(contextPath+"/login.jsp");
    } else {
    	if("1".equals(authUser.getIsAdmin())){
    		userType = "超级管理员";
    	} else if("2".equals(authUser.getIsAdmin())){
    		userType = "管理员";
    	} else {
    		userType = "普通用户";
    	}
    	userName = authUser.getUserName();
        aliasName = authUser.getUserAlias();
        userId = authUser.getUserId();
    }*/
%>
<html>
	<head>
	<title>信息科技动态风险监测系统</title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="<%=contextPath%>/css/sunline-default.css" type="text/css" rel="stylesheet" />
		<link href="<%=contextPath%>/css/igmain_all.css" rel="stylesheet" type="text/css" />
		<%--<link href="<%=contextPath %>/jqueryui/jquery-ui.css" rel="stylesheet">--%>

		<link href="<%=contextPath %>/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
		<link href="<%=contextPath %>/bootstrap-component/fontawesome-free-5.2.0-web/css/all.min.css" rel="stylesheet">

		<%--自定义样式--%>
		<link href="<%=contextPath %>/custom-style/app.css" rel="stylesheet">
		<link href="<%=contextPath %>/custom-style/menu.css" rel="stylesheet">

		<script src="<%=contextPath %>/jquery/jquery.js" type="text/javascript" ></script>
		<script src="<%=contextPath%>/version/js/framework/base/core/lang.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/js/ig_main.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/js/sys_main.js"></script>
		<%--<script src="<%=contextPath %>/jqueryui/jquery-ui.js" type="text/javascript" ></script>--%>

		<script src="<%=contextPath %>/bootstrap3/js/bootstrap.min.js" type="text/javascript"></script>
		<script src="<%=contextPath %>/bootstrap-component/sweetalert2/sweetalert2.all.min.js" type="text/javascript"></script>

		<script type="text/javascript" src="<%=contextPath%>/js/main_changpwd.js"></script>
		<script type="text/javascript">
		if(window.addEventListener) {
			if(/firefox/i.test(navigator.userAgent)){
				FixPrototypeForGecko(); 
			}	
		} 
		 
		  function  FixPrototypeForGecko()
		  {
		      HTMLElement.prototype.__defineGetter__("runtimeStyle",element_prototype_get_runtimeStyle);
		      window.constructor.prototype.__defineGetter__("event",window_prototype_get_event);
		      Event.prototype.__defineGetter__("srcElement",event_prototype_get_srcElement);
		      Event.prototype.__defineGetter__("fromElement",  element_prototype_get_fromElement);
		      Event.prototype.__defineGetter__("toElement", element_prototype_get_toElement);
		 
		  }  
		 
		  function  element_prototype_get_runtimeStyle() { return  this.style; }
		  function  window_prototype_get_event() { return  SearchEvent(); }
		  function  event_prototype_get_srcElement() { return  this.target; }  

		  function element_prototype_get_fromElement() {
		      var node;
		      if(this.type == "mouseover") node = this.relatedTarget;
		      else if (this.type == "mouseout") node = this.target;
		      if(!node) return;
		      while (node.nodeType != 1)
		          node = node.parentNode;
		      return node;
		  }
		 
		  function  element_prototype_get_toElement() {
		          var node;
		          if(this.type == "mouseout")  node = this.relatedTarget;
		          else if (this.type == "mouseover") node = this.target;
		          if(!node) return;
		          while (node.nodeType != 1)
		             node = node.parentNode;
		          return node;
		  }
		 
		  function  SearchEvent()
		  {
		      if(document.all) return  window.event;  
		 
		      func = SearchEvent.caller;  
		 
		      while(func!=null){
		          var  arg0=func.arguments[0];  
		 
		          if(arg0 instanceof Event) {
		              return  arg0;
		          }
		         func=func.caller;
		      }
		      return   null;
		  }
		
		var contextPath='<%=contextPath%>';
		var url = '<%=contextPath%>/version/RMIServlet';
		function logout() {
			location.href ="<%=request.getContextPath()%>/session_out.jsp";
		};
		function help(){
			window.open("<%=contextPath%>/help/OperatorManual.pdf","_bank");
		}
		</script>
	</head>
	<body>
			<div id="mainHeader">
				 <table width="100%" cellpadding="0" cellspacing="0" class="headbg">
					<tr> 
						<td width="189px" style="text-align: center;"><img src="<%=contextPath%>/images/logo-hf.png"></td>
						<%--<td>
							<div id="header"></div>
							<div id="menuItem"></div>
						</td>--%>
						<td class="sidebar-collapse" id="sidebar-collapse">
							<i class="collapse-icon fa fa-bars"></i>
						</td>
						<td style="padding-left: 70px;font-size: 18px;color: #fff;">信息科技动态风险监测</td>
						<td width="300px" align="right">
							<div class="message-bar">
								<a href="#" onclick="showMsgInfo();">
									<i class="icon fa fa-envelope"></i>
									<span class="badge" id="message-bar-badge"></span>
								</a>
							</div>
							<div id="toolbar">
								<div class="user-bar-title">
									<img class="user-bar-img" src="<%=contextPath%>/images/cs.png"></img>
									<span>&nbsp;&nbsp;</span>
									<span class="user-bar-name"><%=aliasName%></span>
								</div>
								<ul class="user-bar-items">
									<li>
										<a href="#" onClick="changepwd();">修改密码</a>
									</li>
									<li>
										<a href="#" onClick="help();">帮助</a>
									</li>
									<li class="user-bar-item-logout">
										<a href="#" onClick="logout();">注销</a>
									</li>
								</ul>
								<%--<span valign="center" class="user">欢迎<strong><%=userType%></strong>&nbsp;(<%=aliasName%>)&nbsp;登录系统</span>
								<%
									if(!"true".equals(Constants.isUap)){//如果不是通过uap认证
								%>
								<span valign="center" class="home"><a href="#" onClick="changepwd();">修改密码</a></span>
								<%} %>
								<span valign="center" class="help"><a href="#" onClick="help();">帮助</a></span>
								<%
									if(!"true".equals(Constants.isUap)){//如果是通过uap认证
								%>
								<span valign="center" class="logout"><a href="#" onClick="logout();">注销</a></span>
								<%} %>--%>
                             </div>
						</td>
					</tr>
				</table>
				<%--<div id="header"></div>
				<div id="menuItem"></div>--%>
			</div>
			<div id="content">
				<div class="page-sidebar" id="sidebar">

				</div>
				<div class="page-content">
					<iframe id="frmright" name="frmright" marginheight="0" marginwidth="0" frameborder="0" scrolling="no" height="100%" width="100%" src=""></iframe>
				</div>
			</div>

			<div id="footer">
				<div class="copyright">恒丰银行版权所有 | 鲁ICP备05031325号|总行地址：中国·烟台市南大街248号 邮编：264000 </div>
			</div>

			<div class="modal fade" tabindex="-1" role="dialog" id="changepwddialog">
				<div class="modal-dialog" role="document" style="width: 500px;">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							<h4 class="modal-title">密码修改</h4>
						</div>
						<div class="modal-body">
							<form class="form-horizontal">
								<input type="hidden" id="user_id" value="<%=userId%>"/>
								<div class="form-group">
									<label for="old_user_password" class="col-sm-3 col-md-3 col-lg-3 control-label">旧密码</label>
									<div class="col-sm-9 col-md-9 col-lg-9">
										<input type="password" class="form-control" id="old_user_password">
									</div>
								</div>
								<div class="form-group">
									<label for="new_user_password" class="col-sm-3 col-md-3 col-lg-3 control-label">新密码</label>
									<div class="col-sm-9 col-md-9 col-lg-9">
										<input type="password" class="form-control" id="new_user_password">
									</div>
								</div>
								<div class="form-group">
									<label for="user_confirm_password" class="col-sm-3 col-md-3 col-lg-3 control-label">新确认密码</label>
									<div class="col-sm-9 col-md-9 col-lg-9">
										<input type="password" class="form-control" id="user_confirm_password">
									</div>
								</div>
							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="saveUserBtn" onClick="saveUserPwd();">确定</button>
							<button type="button" class="btn btn-warning" data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>

			<script type="text/javascript">
				$(document).ready(function(){
                    getUserMsgCount();
				});

				//获取未读提示信息个数
				function getUserMsgCount(){
                    var url="<%=request.getContextPath()%>/version/RMIServlet";

                    var systemObj = {
                        className:'UserMessageServices',
                        methodName:'getUnreadMsgNum',
                        params:JSON.stringify([])
                    };

					$.post(url,systemObj,function (data) {
						var dataObj = $.parseJSON(data);
						if(dataObj.retCode == '0'){
							$(".badge").html(dataObj.result);
						}else{
                            $(".badge").html('0');
						}
                    });
				}

				//点击图标显示消息管理页面
				function showMsgInfo(){
                    loadMenuItem(7,'/pages/sm/user_message/user_message.jsp','Id381817666e7961c0166e798ebeb0002','消息管理','系统管理');
                    getUserMsgCount();
				}

				function iFrameHeight() {
			        //var ifm= document.getElementById("frmright");
			        //SetCwinHeight(ifm);
			        //var subWeb = document.frames ? document.frames["frmright"].document :ifm.contentDocument;
			       // if(ifm != null && subWeb != null) {
			        //    ifm.height = subWeb.body.scrollHeight;
			        //}
			    }
			    
			    function SetCwinHeight(iframeObj){
				  if (document.getElementById){
				   if (iframeObj && !window.opera){
				    if (iframeObj.contentDocument && iframeObj.contentDocument.body.offsetHeight){
				     iframeObj.height = iframeObj.contentDocument.body.offsetHeight;
				    }else if(document.frames[iframeObj.name].document && document.frames[iframeObj.name].document.body.scrollHeight){
				     iframeObj.height = document.frames[iframeObj.name].document.body.scrollHeight;
				    }
				   }
				  }
				 }
				function getheight(){
//					var height=window.screen.availHeight;
					var height = window.innerHeight;

					if(height>500){
//						document.getElementById('content').style.height=(height-141-42)+'px';
						document.getElementById('content').style.height=(height-49-28)+'px';
					}else{
						document.getElementById('content').style.height='400px';
					}
				}
				getheight();
				
				function debounce(callback, delay, context){  
				    if (typeof(callback) !== "function") {  
				        return;  
				    }  
				    delay = delay || 150;  
				    context = context || null;  
				    var timeout;  
				    var runIt = function(){  
				            callback.apply(context);  
				        };  
				    return (function(){  
				        window.clearTimeout(timeout);  
				        timeout = window.setTimeout(runIt, delay);  
				    });  
				}
				  
				window.onresize= debounce(getheight, 50);
			</script>

	</body>
</html>