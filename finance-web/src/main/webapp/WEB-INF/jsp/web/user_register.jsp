<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8" />
<title>用户注册</title>
<link href="static/css/bootstrapRegister.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<link rel="stylesheet" href="static/css/register.css" type="text/css">
</head>
<script type="text/javascript"
	src="<%=basePath%>static/js/jquery.tips.js"></script>
<!--提示框-->
<script  type="text/javascript">
//判断用户是否存在

	function hasUser(){
		
		var userName = $.trim($("#userName").val());
	    $.ajax({
			type: "POST",
			url: '<%=basePath%>web/user/hasUser.do',
			data : {
				userName : userName
			},
			dataType : 'json',
			cache : false,
			success : function(data) {
				if ("success" != data.result) {
					$("#userName").tips({
						side : 3,
						msg : '账号已存在',
						bg : '#AE81FF',
						time : 3
					});
					setTimeout("$('#userName').val('')", 1000);
				}
			}
		});
	}

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
		} else if (/[\u4e00-\u9fa5]/g.test($("#userName").val())) {
			$("#userName").tips({
				side : 3,
				msg : '用户名不能为汉字',
				bg : '#AE81FF',
				time : 2
			});
			$("#userName").focus();
			return false;
		}
		//名称校验
		if ($("#NAME").val() == "") {
			$("#NAME").tips({
				side : 3,
				msg : '用户姓名不能为空',
				bg : '#AE81FF',
				time : 2
			});
			$("#NAME").focus();
			return false;
		}

		var myreg = /^(((13[0-9]{1})|159)+\d{8})$/;

		if ($("#password").val() == "") {
			$("#password").tips({
				side : 3,
				msg : '密码不能为空',
				bg : '#AE81FF',
				time : 2
			});
			$("#password").focus();
			return false;
		} else if (!isTruePwd($("#password").val())) {
			$("#password").tips({
				side : 3,
				msg : '密码为6-15位,且包含大小写字母和数字',
				bg : '#AE81FF',
				time : 2
			});
			$("#password").focus();
			return false;
		}

		if ($("#password").val() != $("#chkpwd").val()) {

			$("#chkpwd").tips({
				side : 3,
				msg : '两次密码不相同',
				bg : '#AE81FF',
				time : 2
			});

			$("#chkpwd").focus();
			return false;
		}
		if ($("#EMAIL").val() == "") {

			$("#EMAIL").tips({
				side : 3,
				msg : '输入邮箱',
				bg : '#AE81FF',
				time : 2
			});
			$("#EMAIL").focus();
			return false;
		} else if (!ismail($("#EMAIL").val())) {
			$("#EMAIL").tips({
				side : 3,
				msg : '邮箱格式不正确',
				bg : '#AE81FF',
				time : 2
			});
			$("#EMAIL").focus();
			return false;
		}
		//证件号校验
		if ($("#certificateNo").val() == "") {
			$("#certificateNo").tips({
				side : 3,
				msg : '证件号码不能为空',
				bg : '#AE81FF',
				time : 2
			});
			$("#certificateNo").focus();
			return false;
		} else if (!isCardNo($("#certificateNo").val())) {
			$("#certificateNo").tips({
				side : 3,
				msg : '证件号码格式不正确',
				bg : '#AE81FF',
				time : 2
			});
			$("#EMAIL").focus();
			return false;
		}
		if ($("#PHONE").val() == "") {

			$("#PHONE").tips({
				side : 3,
				msg : '输入手机号',
				bg : '#AE81FF',
				time : 2
			});
			$("#PHONE").focus();
			return false;
		} else if ($("#PHONE").val().length != 11
				&& !myreg.test($("#PHONE").val())) {
			$("#PHONE").tips({
				side : 3,
				msg : '手机号格式不正确',
				bg : '#AE81FF',
				time : 2
			});
			$("#PHONE").focus();
			return false;
		}
		//图片校验
		if ($("#imgUrl").val() == "") {
			$("#imgUrl").tips({
				side : 3,
				msg : '请上传图片',
				bg : '#AE81FF',
				time : 2
			});
			$("#imgUrl").focus();
			return false;
		}
		if (result = true) {
			$("#webUserFrom").submit();
		}
	}

	function ismail(mail) {
		return (new RegExp(
				/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/)
				.test(mail));
	}

	// 验证身份证 
	function isCardNo(card) {
		var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		return pattern.test(card);
	}
	// 验证密码:6-20位，只能有大小写字母和数字，并且大小写字母和数字都要有。
	function isTruePwd(pwd) {
		var pattern = /(^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z\d]{6,15}$)/;
		return pattern.test(pwd);
	}
</script>
<body>

	<div class="container">
		<div class="row">
			<form action="web/user/add.do" name="webUserFrom" id="webUserFrom"
				class="form-horizontal" method="post" enctype="multipart/form-data">
				<c:if test="${not empty pd.ROLE_ID}">
				<input type="hidden" name="ROLE_ID" id="ROLE_ID" value="${pd.ROLE_ID}" />
					<div class="col-xs-6 pull-right register">
						<div>用户注册表单</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">用户账号：</label>
							<div class="col-xs-8">
								<input type="text" name="USERNAME" id="userName"
									placeholder="请输入登录账号" onblur="hasUser();" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">用户姓名：</label>
							<div class="col-xs-8">
								<input type="text" name="NAME" id="NAME" placeholder="请输入姓名" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
							<div class="col-xs-8">
								<input type="password" name="PASSWORD" id="password"
									placeholder="请输入密码" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">确认密码：</label>
							<div class="col-xs-8">
								<input type="password" name="chkpwd" id="chkpwd" maxlength="32"
									placeholder="请确认输入密码" title="确认密码" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</label>
							<div class="col-xs-8">
								<input type="text" name="EMAIL" id="EMAIL" placeholder="请输入邮箱" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">证件号码：</label>
							<div class="col-xs-8">
								<input type="text" name="certificateNo" id="certificateNo"
									placeholder="请输入证件号码" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">手机号码：</label>
							<div class="col-xs-8">
								<input type="text" name="PHONE" id="PHONE" placeholder="请输入手机号码" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label">证件图片：</label>
							<div class="col-xs-8">
								<input type="file" name="imgUrl" id="imgUrl" accept="image/*" />
							</div>
							<label class="col-xs-1">*</label>
						</div>
						<div class="col-xs-4 col-xs-offset-4">
							<a class="btn btn-mini btn-primary" onclick="save();">注册用户</a>
						</div>
					</div>
				</c:if>
				<c:if test="${empty pd.ROLE_ID}">
					<tr>
						<td colspan="2"><h5>您的注册连接有误，请联系管理员.联系电话：0371-2854555</h5></td>
					</tr>
				</c:if>

			</form>
		</div>
	</div>
	</body>
	<script type="text/javascript">
		window.jQuery
				|| document
						.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");
	</script>
</html>