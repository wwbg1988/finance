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
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>

<script type="text/javascript"
	src="<%=basePath%>static/js/jquery.tips.js"></script>

<script type="text/javascript">
	top.hangge();
	//保存

	function save() {
		if ($("#PARENT_ID").val() == "") {
			$("#PARENT_ID").tips({
				side:3,
	            msg:'请选择父角色',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#PARENT_ID").focus();
			
			return false;
		}
		if ($("#roleName").val() == "") {
			 $("#roleName").tips({
					side:3,
		            msg:'请填写角色名称',
		            bg:'#AE81FF',
		            time:2
		        });
			$("#roleName").focus();
			return false;

		}

		$("#form1").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
</script>
</head>
<body>
	<form action="role/add.do" name="form1" id="form1" method="post">

		<div id="zhongxin">
		
			  <table id="table_report" class="table table-bordered table-hover">
					<tr>
						<td style="width:90px;text-align: center;padding-top: 13px;"> <span class="btn btn-small btn-info ">父角色:</span></td>
							<td><select class="chzn-select" name="PARENT_ID" 	id="PARENT_ID" data-placeholder="请选择父角色" style="vertical-align: top; width:170px;">
							<option value="" id="ou"></option>
							<c:forEach items="${roleList}" var="role">
								<option value="${role.ROLE_ID }"
									<c:if test="${role.ROLE_ID == pd.ROLE_ID }">selected</c:if>>${role.ROLE_NAME }</option>
							</c:forEach>
					</select></td>
					</tr>
					<tr>
					    <td style="width:90px;text-align: center;padding-top: 13px;"> <span class="btn btn-small btn-info ">角色名:</span></td>
						<td><input type="text" name="ROLE_NAME" id="roleName" placeholder="这里输入角色名称" title="名称" style="width: 158px;" /></td>
					</tr>
				</table>
				<table class="center" style="width:100%" >
				    <tr>
					<td style="text-align: center;"><a
						class="btn btn-mini btn-primary" onclick="save();">保存</a> <a
						class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					</td>
				    </tr>
				</table>
		</div>
	</form>

	<div id="zhongxin2" class="center" style="display: none">
		<img src="static/images/jzx.gif" style="width: 50px;" /><br />
		<h4 class="lighter block green"></h4>
	</div>
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
