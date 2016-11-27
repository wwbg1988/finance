<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
		
<script type="text/javascript">
	top.hangge();
	//保存
	$(document).ready(function(){
		if($("#money").val()!=""){
			$("#money").attr("readonly","readonly");
			$("#money").css("color","gray");
		
		}
	});
	function save() {
		//判断是否是正整数
		var re = /^[0-9]*[1-9][0-9]*$/ ;
		var money=$("#money").val();
		if ($("#money").val() == "") {
			$("#money").tips({
				side : 3,
				msg : '金额不能为空',
				bg : '#AE81FF',
				time : 2
			});
			$("#money").focus();
			return false;

		}else{	
			//充值金额长度
			var money_length=$("#money").val().length;
		    if(money_length>=10){
				$("#money").tips({
					side : 3,
					msg : '金额长度必须小于10位',
					bg : '#AE81FF',
					time : 2
				});
				return false;
			}
			if(!(/^(\+|-)?\d+$/.test( money )) || money <= 0){
				$("#money").tips({
					side : 3,
					msg : '金额只能是正整数',
					bg : '#AE81FF',
					time : 2
				});
				$("#money").focus();
				return false;
            }
		}
			
		

		$("#rechargeMoneyForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
</script>
</head>
<body>
	<form action="web/user/rechargeMoney.do" name="rechargeMoneyForm" id="rechargeMoneyForm" method="post">
         <input type="hidden" name="USER_ID" id="user_id" value="${pd.USER_ID }"/>
         <input type="hidden" name="id"  value="${pd.id }"/>
         <input type="hidden" name="isFailure" id="isFailure" value="${pd.isFailure }"/>
		<div id="zhongxin">
		
			  <table id="table_report" class="table table-bordered table-hover">
				
					<tr>
					    <td style="width:90px;text-align: center;padding-top: 13px;"> <span class="btn btn-small btn-info ">充值金额:</span></td>
						<c:if test="${pd.money!=''&&pd.money!=null}">
						<td><input type="text" name="money" id="money" value="${pd.money}"  title="名称" style="width: 158px;" /></td>
						</c:if>
						<c:if test="${pd.money==''||pd.money==null}">
						<td><input type="text" name="money"  id="money" placeholder="这里输入充值金额" title="名称" style="width: 158px;" /></td>
					    </c:if>
					</tr>
				</table>
				<table class="center" style="width:100%" >
				    <tr>
					<td style="text-align: center;"><a
						class="btn btn-mini btn-primary" onclick="save();">充值</a> <a
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
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		
		<script type="text/javascript">
		
		$(function() {
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
		
			
		});
		
		</script>
</body>
</html>
