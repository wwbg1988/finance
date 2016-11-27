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
		var result=true;
		if ($("#currency").val() == "") {
			 $("#currency").tips({
					side:3,
		            msg:'请填写货币名称',
		            bg:'#AE81FF',
		            time:2
		        });
			$("#currency").focus();
			return false;

		}
		if ($("#currencyRates").val() == "") {
			 $("#currencyRates").tips({
					side:3,
		            msg:'请填写货币利率',
		            bg:'#AE81FF',
		            time:2
		        });
			$("#currencyRates").focus();
			return false;

		}else{
			  // 非负数：0 | 正数
			  var check= /^\d+(\.{0,1}\d+){0,1}$/;
				if(!(check.test($("#currencyRates").val())) || $("#currencyRates").val() <= 0||$("#currencyRates").val() > 1){
					$("#currencyRates").tips({
						side : 3,
						msg : '货币利率只能是0-1之间的数字',
						bg : '#AE81FF',
						time : 2
					});
					$("#currencyRates").focus();
					return false;
	            }
		  }
		
		if ($("#isEnable").val() == "") {
			$("#isEnable").tips({
				side:3,
	            msg:'请选择启用状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("isEnable").focus();
			
			return false;
		}
		
		  //ajax提交form表单
	    $.ajax({
            cache: true,
            type: "POST",
            url:'<%=basePath%>currencyConfigController/add.do',
            data:$('#currencyConfigAdd').serialize(),// 你的formid
            async: false,
            error: function(request) {
                alert("Connection error");
            },
            success: function(data) {
                    
	        if (data.success == true) {
					alert(data.msg);
					location.href='<%=basePath%>currencyConfigController/list.do';    
				} else {
					result=false;
					alert(data.msg);
				    location.href='<%=basePath%>currencyConfigController/addPage.do';
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
	<form  name="currencyConfigAdd" id="currencyConfigAdd" method="post">
	<input id="id" name="id" type="hidden" value="${currencyConfigDto.id}" />
		<div id="zhongxin">

			<table id="table_report" class="table table-bordered table-hover">
				<tr>
					<td style="width: 90px; text-align: center; padding-top: 13px;">
						<span class="btn btn-small btn-info ">货币名称:</span>
					</td>
					<td><input type="text" name="currency" id="currency"
						placeholder="这里输入货币名称" title="名称" style="width: 158px;" /></td>
				</tr>
				<tr>
					<td style="width: 90px; text-align: center; padding-top: 13px;">
						<span class="btn btn-small btn-info ">货币利率:</span>
					</td>
					<td><input type="text" name="currencyRates" id="currencyRates"
						placeholder="这里输入货币利率" title="名称" style="width: 158px;" /></td>
				</tr>
				<tr>
					<td style="width: 90px; text-align: center; padding-top: 13px;">
						<span class="btn btn-small btn-info ">是否启用:</span>
					</td>
					<td style="padding-top: 15px;"><select class="chzn-select" name="isEnable" id="isEnable"
						data-placeholder="请选择父角色"
						style="vertical-align: top; width: 170px;">
							<option value="1">启用</option>
							<option value="0">禁用</option>
					</select></td>
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
