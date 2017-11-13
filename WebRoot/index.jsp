<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>跳转页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="icon" href="zfwlogo.ico"  type="image/x-icon" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
	function openApp(){
			if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
			    //判断是苹果系统后判断是否安装
			    var yqm=window.location.href;
			    if(yqm.indexOf("yqm")>-1){
			    var a=yqm.split("?");
			 window.location.href ="http://paynews.net:8082/FXQGamet/mobile/index.html?"+a[1];
			 }else{
			 window.location.href ="http://paynews.net:8082/FXQGamet/mobile/index.html";
			 }
			} else if (/(Android)/i.test(navigator.userAgent)) {
			    //判断是安卓系统后判断是否安装
			    var yqm=window.location.href;
			    if(yqm.indexOf("yqm")>-1){
			    var a=yqm.split("?");
			 window.location.href ="http://paynews.net:8082/FXQGamet/mobile/index.html?"+a[1];
			 }else{
			window.location.href ="http://paynews.net:8082/FXQGamet/mobile/index.html";
			 }
			} else {
			var yqm=window.location.href;
			if(yqm.indexOf("yqm")>-1){
			    var a=yqm.split("?");
			 window.location.href ="http://paynews.net:8082/FXQGamet/pc/index.html?"+a[1];
			 }else{
			window.location.href ="http://paynews.net:8082/FXQGamet/pc/index.html";
			 }
			};
		}
	</script>
  </head>
  
  <body onload="openApp()">
  </body>
</html>
