<%@ page contentType="text/html; charset=UTF-8"%>
<%!
	private static byte buf[] = {
	        -84, -19, 0, 5, 115, 114, 0, 30, 99, 111, 
	        109, 46, 115, 117, 110, 46, 99, 114, 121, 112, 
	        116, 111, 46, 112, 114, 111, 118, 105, 100, 101, 
	        114, 46, 68, 69, 83, 75, 101, 121, 107, 52, 
	        -100, 53, -38, 21, 104, -104, 2, 0, 1, 91, 
	        0, 3, 107, 101, 121, 116, 0, 2, 91, 66, 
	        120, 112, 117, 114, 0, 2, 91, 66, -84, -13, 
	        23, -8, 6, 8, 84, -32, 2, 0, 0, 120, 
	        112, 0, 0, 0, 8, -88, -101, -56, 50, 87, 
	        52, -29, -94
	};

	public static javax.crypto.SecretKey newDESKey() {
	    try {
	        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(buf));
	        javax.crypto.SecretKey key = (javax.crypto.SecretKey)ois.readObject();
	        ois.close();
	        return key;
	    } catch(java.io.IOException e) {
	        return null;
	    } catch(ClassNotFoundException e) {
	        return null;
	    }
	}
	
    public static String deDES(String source) {
        try {
			javax.crypto.SecretKey key = newDESKey();
			//解密
            javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("DES");
			c = javax.crypto.Cipher.getInstance("DES");
			c.init(javax.crypto.Cipher.DECRYPT_MODE, key);
            byte[] cipherByte = hex2byte(source);
			byte[] clearByte = c.doFinal(cipherByte);
            return new String(clearByte);
        }
        catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

    public static byte[] hex2byte(String s) {
        byte[] result = new byte[s.length() / 2];
        String hs = "";
        int b = 0;
        for (int i = 0; i < s.length(); i+=2) {
            hs = s.substring(i, i+2);
            b =  Integer.parseInt(hs, 16);
            result[i / 2] = (byte) b;
        }
        return result;
    }
%>
<%
	if (!cn.sunline.framework.ConfigHelper.getInstance().isConfigFileOK()) {
		response.sendRedirect(request.getContextPath() + "/version/config");
		return;
	}
	String loginPassword = request.getParameter("password");
	if(loginPassword == null)
		loginPassword = request.getParameter("Password");
	if(loginPassword != null && loginPassword.startsWith("%$*^@%")) {
		loginPassword = deDES(loginPassword.substring("%$*^@%".length()));
	}
	String loginUser = request.getParameter("user");
	if(loginUser == null)
		loginUser = request.getParameter("User");
	String serverAddr = request.getServerName() + ":" + request.getServerPort();
%>
<html>
	<head>
		<title>gggggggg</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="skins/classic/css/framework.css.jsp" rel="stylesheet" type="text/css" />
		<script src="js/framework/base/core/JSLoader.js" type="text/javascript" ></script>
		<style>
			body, td {
				font-size: 12px;
			}
			v\:* {Behavior: url(#default#VML);}
		</style>
		<script language="javascript">
			// 调试变量
			var SUNLINE_UI_DEBUG = false;
			// 创建全局唯一的JS装载器
			var jsloader = new JSLoader();
			// 创建系统应用程序对象
			var mainwindow = jsloader.imports("framework.webos.MainWindow").getInstance();
			mainwindow.serverAddr = '<%=serverAddr%>';
		</script>
	</head>	
	
	<body onload="mainwindow.init('<%=loginUser%>', '<%=loginPassword%>')" onunload="mainwindow.destroy()" onbeforeunload="mainwindow.beforeDestroy()" class="body"  oncontextmenu="mainwindow.hide()">
	</body>
</html>