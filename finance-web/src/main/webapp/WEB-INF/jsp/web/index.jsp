
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
</head>
<script type="text/javascript">
	window.jQuery
			|| document
					.write("<script src='<%=basePath%>static/js/jquery-1.9.1.min.js'>\x3C/script>");
</script>

<script type="text/javascript" src="<%=basePath%>static/js/jquery.tips.js"></script>
<body>

	<div id="dd"></div>
		<script type="text/javascript">
		var username = "${userName}";
		alert(username);
		 $("#dd").html("恭喜您:"+username+", 登陆成功");
	</script>
</body>
</html>