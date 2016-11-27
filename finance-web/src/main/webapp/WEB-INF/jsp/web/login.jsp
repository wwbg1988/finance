<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css"
	href="http://jqueryui.com/latest/themes/base/ui.all.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="http://jqueryui.com/latest/jquery-1.3.2.js"></script>
<script type="text/javascript"
	src="http://jqueryui.com/latest/ui/ui.core.js"></script>
<script type="text/javascript"
	src="http://jqueryui.com/latest/ui/ui.dialog.js"></script>
<link href="/js/kindeditor-4.1.10/themes/default/default.css"
	type="text/css" rel="stylesheet">
<link href="static/css/bootstrap.min.css" rel="stylesheet" />
<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="static/css/font-awesome.min.css" />
<!-- 下拉框 -->
<link rel="stylesheet" href="static/css/chosen.css" />
<link rel="stylesheet" href="static/css/ace.min.css" />
<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
<link rel="stylesheet" href="static/css/ace-skins.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>web用户登录</title>
</head>
<script type="text/javascript">
	window.jQuery
			|| document
					.write("<script src='<%=basePath%>static/js/jquery-1.9.1.min.js'>\x3C/script>");
</script>

<script type="text/javascript" src="<%=basePath%>static/js/jquery.tips.js"></script>
<!--提示框-->
<script>
	//保存
	function save() {
		var result = true;
		//名称校验
		if ($("#userName").val() == "") {
			$("#userName").tips({
				side : 3,
				msg : '用户名不能为空名称',
				bg : '#AE81FF',
				time : 2
			});
			$("#userName").focus();
			return false;
		}

		if ($("#userPassword").val() == "") {
			$("#userPassword").tips({
				side : 3,
				msg : '密码不能为空',
				bg : '#AE81FF',
				time : 2
			});
			$("#userPassword").focus();
			return false;
		}
		if (result = true) {
			$("#webUserFrom").submit();
		}
	}
</script>
<body>

	<H2 style="margin-left: 800px">用户登录</H2>
	<form action="<%=basePath%>web/user/login.do" name="webUserFrom" id="webUserFrom"
		method="post" enctype="multipart/form-data"
		style="margin-left: 700px; border: 3">
		<table>
			<tr>
				<td><h3>用户名：</h3></td>
				<td><input type="text" name="userName" id="userName" /></td>
			</tr>
			<tr>
				<td><h3>密码：</h3></td>
				<td><input type="password" name="userPassword"
					id="userPassword" /></td>
			</tr>
			<tr>
				<td colspan="2"><a class="btn btn-mini btn-primary"
					onclick="save();">登录</a>&nbsp;&nbsp;&nbsp; <a
					class="btn btn-mini btn-primary" href="<%=basePath%>web/user/toRegister.do">注册</a>
				</td>

			</tr>

		</table>

	</form>

</body>
</html>