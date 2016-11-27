<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.*"%>
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
<script type="text/javascript"
	src="static/js/bootstrap-datepicker.min.js"></script>
<!-- 多选下拉框样式 -->
<link rel="stylesheet" href="static/css/inputiconplus.css" />
<script type="text/javascript">
	$(top.hangge());
	$(document).ready(function(){
		
	});
	
	//保存
	function searchChart(){
		var result=true;
	    if($("#openTimeStart").val()==""){
			$("#openTimeStart").tips({
				side:3,
	            msg:'开始时间不能为空',
	            bg:'#AE81FF',
	            time:2
	        });
		
			return false;
		  }
	    if($("#openTimeEnd").val()==""){
			$("#openTimeEnd").tips({
				side:3,
	            msg:'结束时间不能为空',
	            bg:'#AE81FF',
	            time:2
	        });
	
			return false;
		  }
	     if($("#openTimeEnd").val()<$("#openTimeStart").val()){
			$("#openTimeEnd").tips({
				side:3,
	            msg:'结束时间不能小于开始时间',
	            bg:'#AE81FF',
	            time:2
	        });
		/* 	 $("#openTimeEnd").focus();  */
			return false;
		  }
	     
	     if(result==true){
	    	 
	      $("#chartForm").submit();
	     }
	  
	  <%--   //ajax提交form表单
	    $.ajax({
            cache: true,
            type: "POST",
            url:'<%=basePath%>historyOrderController/findChart.do',
			data : $('#chartForm').serialize(),
			async : false,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
		
			}
		}); --%>
	}
</script>
</head>
<body>
		<%
							String strXML = "";

							strXML += "<graph caption='对比表' xAxisName='月份' yAxisName='值' decimalPrecision='0' formatNumberScale='0'>";
							strXML += "<set name='1' value='462' color='AFD8F8'/>";
							strXML += "<set name='2' value='857' color='F6BD0F'/>";
							strXML += "<set name='3' value='671' color='8BBA00'/>";
							strXML += "<set name='4' value='494' color='FF8E46'/>";
							strXML += "<set name='5' value='761' color='008E8E'/>";
							strXML += "<set name='6' value='960' color='D64646'/>";
							strXML += "<set name='7' value='629' color='8E468E'/>";
							strXML += "<set name='8' value='622' color='588526'/>";
							strXML += "<set name='9' value='376' color='B3AA00'/>";
							strXML += "<set name='10' value='494' color='008ED6'/>";
							strXML += "<set name='11' value='761' color='9D080D'/>";
							strXML += "<set name='12' value='960' color='A186BE'/>";
							strXML += "</graph>";
							//Create the chart - Column 3D Chart with data from strXML variable using dataXML method
						%>
	<form name="chartForm" id="chartForm" method="post" action="historyOrderController/findChart.do">
	    <div id="zhongxin">
			<table id="table_report" class="table table-bordered table-hover">
				<tr>
					<td style="text-align: center; width: 90px;"><span
						class="btn btn-small btn-info ">起始时间:</span></td>

					<td><input class="span10 date-picker" name="openTimeStart"
						id="openTimeStart" value="${pd.openTimeStart}" type="text"
						data-date-format="yyyy-mm-dd" readonly="readonly"
						style="padding-top: 10px;" placeholder="起始日期" title="起始日期" /></td>
					<td style="text-align: center; width: 90px;"><span
						class="btn btn-small btn-info ">结束时间:</span></td>
					<td><input class="span10 date-picker" name="openTimeEnd"
						id="openTimeEnd" value="${pd.openTimeEnd}" type="text"
						data-date-format="yyyy-mm-dd" readonly="readonly" style=""
						placeholder="结束日期" title="结束日期" /></td>
					<td style="text-align: center; width: 90px;"><a
						class="btn btn-mini btn-primary" onclick="searchChart();">查詢</a> <a
						class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					</td>
				</tr>

			</table>

			<div style="width: 100%" class="center">
			<c:if test="${strXML != ''&& strXML !=null}">
	
				<table border="0" width="50%">
				
					<tr>
						<td><jsp:include page="../../../FusionChartsHTMLRenderer.jsp"
								flush="true">
								<jsp:param name="chartSWF" value="static/FusionCharts/Column3D.swf" />
								<jsp:param name="strURL" value="" />
								<jsp:param name="strXML"  value="${strXML}" />
					            <jsp:param name="chartId" value="myNext" />
								<jsp:param name="chartWidth" value="630" />
								<jsp:param name="chartHeight" value="320" />
								<jsp:param name="debugMode" value="false" />
							</jsp:include></td>
					</tr>
					
				</table>
					</c:if>
			</div>
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
			//日期框
			$('.date-picker').datepicker();
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