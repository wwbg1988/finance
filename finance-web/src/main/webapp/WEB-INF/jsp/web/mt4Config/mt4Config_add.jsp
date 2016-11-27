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
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<meta charset="utf-8" />
<title></title>
<meta name="description" content="overview & stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="static/css/bootstrap.min.css" rel="stylesheet" />
<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="static/css/font-awesome.min.css" />
<!-- 下拉框 -->
<link rel="stylesheet" href="static/css/chosen.css" />
<link rel="stylesheet" href="static/css/ace.min.css" />
<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
<link rel="stylesheet" href="static/css/ace-skins.min.css" />
<link rel="stylesheet" href="static/css/font-awesome.min.css" />
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<!-- 多选下拉框样式 -->
<link rel="stylesheet" href="static/css/inputiconplus.css" />
<script type="text/javascript">
	$(top.hangge());
	$(document).ready(function(){
		
	});
	
	//保存
	function save(){
		var result=true;
	    if($("#serverIp").val()==""){
			$("#serverIp").tips({
				side:3,
	            msg:'请填写IP地址',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverIp").focus();
			return false;
		  }
	    if($("#serverPort").val()==""){
			$("#serverPort").tips({
				side:3,
	            msg:'请填写端口号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverPort").focus();
			return false;
		  }
	    if($("#lgManage").val()==""){
			$("#lgManage").tips({
				side:3,
	            msg:'请填写登录账号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#lgManage").focus();
			return false;
		  }
	    if($("#lgPword").val()==""){
			$("#lgPword").tips({
				side:3,
	            msg:'请填写登录密码',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#lgPword").focus();
			return false;
		  }
	    if($("#mid").val()==""){
			$("#mid").tips({
				side:3,
	            msg:'请填写mid',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#mid").focus();
			return false;
		  }
	      if($("#userlever").val()==""){
			 $("#userleve").tips({
				side:3,
	            msg:'请填写用户等级',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#userlever").focus();
			return false;
		  }
	    //ajax提交form表单
	    $.ajax({
            cache: true,
            type: "POST",
            url:'<%=basePath%>mt4Config/add.do',
            data:$('#configForm').serialize(),// 你的formid
            async: false,
            error: function(request) {
                alert("Connection error");
            },
            success: function(data) {
                    
	        if (data.success == true) {
					bootbox.alert(data.msg);
					location.href='<%=basePath%>mt4Config/list.do';    
				} else {
					result=false;
					alert(data.msg);
				    location.href='<%=basePath%>mt4Config/addPage.do';
				}
			}
		});
		$("#zhongxin").hide();
		$("#zhongxin2").show();
		if (result == true) {
			//只有返回成功，才会关闭当前dialog;
			top.Dialog.close();
		}

	}
</script>
</head>
<body>
	<form name="configForm" id="configForm" method="post">
		<input id="id" name="id" type="hidden" value="${mt4ConfigDto.id}" />
		<div id="zhongxin">
			<table class="center">
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td style="text-align: left"><span class="btn btn-small btn-info" style="width: 70px;margin-bottom: 10px;">IP地址：</span>&nbsp;</td>
					<td><input type="text" name="serverIp" id="serverIp"
						maxlength="32" placeholder="这里输入IP地址" title="IP地址" /></td>
				</tr>
				<tr>
					<td style="text-align: left"><span class="btn btn-small btn-info" style="width: 70px;margin-bottom: 10px;">端口号：</span></td>
					<td><input type="text" name="serverPort" id="serverPort"
						maxlength="32" placeholder="这里输入端口号" title="端口号" /></td>
				</tr>
				<tr>
					<td style="text-align: left"><span class="btn btn-small btn-info" style="width: 70px;margin-bottom: 10px;">登录帐号：</span></td>
					<td><input type="text" name="lgManage" id="lgManage"
						maxlength="32" placeholder="这里输入登录帐号" title="登录帐号" /></td>
				</tr>
				<tr>
					<td style="text-align: left"><span class="btn btn-small btn-info" style="width: 70px;margin-bottom: 10px;">登录密码：</span></td>
					<td><input type="text" name="lgPword" id="lgPword"
						maxlength="32" placeholder="这里输入登录密码" title="登录密码" /></td>
				</tr>
				<tr>
					<td style="text-align: left"><span class="btn btn-small btn-info" style="width: 70px;margin-bottom: 10px;">mid：</span></td>
					<td><input type="text" name="mid" id="mid" maxlength="32" disabled="disabled"
						placeholder="这里输入mid"  value="000" title="mid" /></td>
				</tr>
				<tr>
					<td style="text-align: left"><span class="btn btn-small btn-info" style="width: 70px;margin-bottom: 10px;">user_lever：</span></td>
					<td><input type="text" name="userlever" id="userlever" disabled="disabled"
						maxlength="32" value="100" placeholder="这里输入用户等级" title="用户等级" /></td>
				</tr>
				<tr>
					<td style="text-align: center;" colspan="2"><a
						class="btn btn-mini btn-primary" onclick="save();">保存</a> <a
						class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					</td>
				</tr>
			</table>
		</div>

		<div id="zhongxin2" class="center" style="display: none">
			<br /> <br /> <br /> <br /> <img src="static/images/jiazai.gif" /><br />
			<h4 class="lighter block green"></h4>
		</div>

	</form>

	<!-- 引入 -->
	<script type="text/javascript">
		window.jQuery
				|| document
						.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");
	</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<script src="static/js/ace.min.js"></script>
	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript" src="static/js/inputiconplus.js"></script>
	<!-- 加减号 -->

	<script type="text/javascript">
		$(function() {
			$(".mselect").css("margin-bottom", "none");

			//单选框
			$chosen = $(".chzn-select");
			$chosen.chosen();
			//下拉框
			$(".chzn-select").chosen();
			$(".chzn-select-deselect").chosen({
				allow_single_deselect : true
			});

		});
	</script>
</body>
</html>