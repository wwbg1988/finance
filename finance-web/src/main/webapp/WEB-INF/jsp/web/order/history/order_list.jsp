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
<!-- jsp文件头和头部 -->
<%@ include file="../../../system/admin/top.jsp"%>
<!-- 引入 -->
<script type="text/javascript">
	window.jQuery
			|| document
					.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");
</script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>

<script type="text/javascript" src="static/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="static/js/jquery.dataTables.bootstrap.js"></script>

<script type="text/javascript"
	src="static/js/bootstrap-datepicker.min.js"></script>
<!-- 日期框 -->
<script type="text/javascript" src="static/js/bootbox.min.js"></script>
<!-- 确认窗口 -->
<!-- 引入 -->

<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<!--提示框-->

<script type="text/javascript">
	
</script>
</head>
<body>

	<div class="container-fluid" id="main-container">


		<div id="page-content" class="clearfix">

			<div class="row-fluid">


				<div class="row-fluid">

					<!-- 检索  -->
					<form action="historyOrderController/findAllHistoryOrder.do"
						method="post" name="historyOrderForm" id="historyOrderForm">
						<table>

							<tr>
								<td align="left" style="padding-bottom: 13px;"><span
									style="font-size: x-large; font-weight: bolder;"><a
										class="btn btn-mini btn-search"><font color="#000000;">搜索查询:</font></a></span></td>
								<%-- <td>
								<input autocomplete="off" id="nav-search-input" type="text" name="symbol" value="${pd.symbol}" placeholder="" />
								</td> --%>
									<td style="vertical-align: top;"><select
									class="chzn-select" name="symbol" id="nav-search-input"
									data-placeholder="请选择货币"
									style="vertical-align: top; width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<c:forEach items="${currencyList}" var="currency">
											<option value="${currency.currency }"
												<c:if test="${pd.symbol==currency.currency}">selected</c:if>>${currency.currency }</option>
										</c:forEach>
								</select></td>
								
								<%-- <td style="width: 130px;"><span class="input-icon">
										<input style="width: 110px;" autocomplete="off"
										id="search-tick" type="text" name="tick" value="${pd.tick}"
										placeholder="请输入订单号" /> <i id="nav-search-icon"
										class="icon-search"></i>
								</span></td> --%>
								<td style="vertical-align: top;"><select
									class="chzn-select" name="USER_ID" id="user_id"
									data-placeholder="请选择用户"
									style="vertical-align: top; width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<c:forEach items="${userList}" var="user">
											<option value="${user.USER_ID }"
												<c:if test="${pd.USER_ID==user.USER_ID}">selected</c:if>>${user.NAME }</option>
										</c:forEach>
								</select></td>

								<td style="vertical-align: top;"><select title="买卖类型"
									class="chzn-select" name="cmd" id="cmd" data-placeholder="买卖类型"
									style="vertical-align: top; width: 120px;">
										<c:forEach items="${cmdMap}" var="m">
											<option value="${m.value}"
												<c:if test="${pd.cmd==m.value}">selected</c:if>>${m.key}</option>
										</c:forEach>

								</select></td>

								<c:if test="${pd.userName == 'admin' }">
									<td><input class="span10 date-picker" name="openTimeStart"
										id="openTimeStart" value="${pd.openTimeStart}" type="text"
										data-date-format="yyyy-mm-dd" readonly="readonly"
										style="width: 108px;" placeholder="开仓时间(起始)" title="开仓时间" />-</td>
									<td><input class="span10 date-picker" name="openTimeEnd"
										id="openTimeEnd" value="${pd.openTimeEnd}" type="text"
										data-date-format="yyyy-mm-dd" readonly="readonly"
										style="width: 108px;" placeholder="开仓时间(结束)" title="开仓时间" /></td>
									<td style="vertical-align: top;"><select title="是否包含平仓"
										class="chzn-select" name="eveningUp" id="eveningUp"
										data-placeholder="是否包含平仓"
										style="vertical-align: top; width: 120px;">
																<option value=""></option>
											<c:forEach items="${eveningMap}" var="m">
												<option value="${m.value}"
													<c:if test="${pd.eveningUp==m.value}">selected</c:if>>${m.key}</option>
											</c:forEach>

									</select></td>
								</c:if>
								<c:if test="${QX.cha == 1 }">
									<td style="vertical-align: top;"><button
											class="btn btn-mini btn-light" onclick="search();" title="检索">
											<i id="nav-search-icon" class="icon-search"></i>
										</button></td>
									<td style="vertical-align: top;"><button
											class="btn btn-mini btn-light" onclick="clearOrder();"
											title="清空">
											<i id="nav-search-icon" class="icon-remove"></i>
										</button></td>
									<td style="vertical-align: top;"><a
										class="btn btn-mini btn-light" onclick="ordertoExcel();"
										title="导出到EXCEL"><i id="nav-search-icon"
											class="icon-download-alt"></i></a></td>

								</c:if>
								<c:if
									test="${accountResultDto!=null && accountResultDto.accountCount!=0}">
									<td align="left" style="padding-bottom: 13px;"><span
										style="font-size: x-large; font-weight: bolder;"><a
											class="btn btn-mini btn-info"><font color="#000000;">结算结果:</font></a></span></td>
									<td style="width: 125px; padding-bottom: 10px;"><span
										class="input-icon"><span class="btn btn-small btn-primary"
											style="height: 20px;">
												成交量:${accountResultDto.accountCount}</span></span></td>
									<td style="width: 120px; padding-bottom: 10px;"><span
										class="input-icon"><span class="btn btn-small btn-primary"
											style="height: 20px;">
												订单条数:${accountResultDto.accountSize}</span></span></td>
								</c:if>
								<c:if
									test="${accountResultDto.accountCount!=0 && accountResultDto.accountCommission!=null && accountResultDto.accountCommission!=''}">
									<td style="width: 140px; padding-bottom: 10px;"><span
										class="input-icon"><span
											class="btn btn-small btn-purple" style="height: 20px;">
												结算佣金:${accountResultDto.accountCommission}</span></span></td>
								</c:if>
							</tr>
						</table>
						<!-- 检索  -->


						<table id="table_report"
							class="table table-striped table-bordered table-hover">

							<thead>
								<tr>
									<th>序号</th>
									<th>订单号</th>
									<th>所属用户</th>
									<th>货币名称</th>
									<th>开仓价格</th>
									<th><i class="icon-time hidden-phone"></i>开仓时间</th>
									<th>结束价格</th>
									<th><i class="icon-time hidden-phone"></i>结束时间</th>
									<th class="center">类型</th>
									<th class="center">数量</th>
									<th class="center">盈亏</th>
									<th>备注</th>

									<!-- <th class="center">操作</th> -->
								</tr>
							</thead>

							<tbody>

								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty historyOrderList}">
										<c:forEach items="${historyOrderList}" var="historyOrder"
											varStatus="vs">
											<tr>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td>${historyOrder.tick}</td>
												<td>${historyOrder.userName}</td>
												<td>${historyOrder.symbol}</td>
												<td>${historyOrder.openPrice}</td>
												<td>${historyOrder.openTimeFormatString}</td>
												<td>${historyOrder.closePrice}</td>
												<td>${historyOrder.closeTimeFormatString}</td>

												<td class="center"><c:if
														test="${historyOrder.cmd == 0 }">
														<span class="btn btn-small btn-info "> 买入</span>
													</c:if> <c:if test="${historyOrder.cmd == 1 }">
														<span class="btn btn-small btn-purple"> 卖出</span>
													</c:if></td>
												<td class='center'>${historyOrder.total}</td>
												<td class="center"><span
													class="btn btn-small btn-danger" style="width: 60px;">
														${historyOrder.profit}</span></td>
												<td>${historyOrder.comment}</td>

												<%-- 	<td style="width: 60px;">
													<div class='hidden-phone btn-group'>
														 <a class='btn btn-mini btn-danger'
															title="删除"
															onclick="delcolor('${historyOrder.id }');"><i
															class='icon-trash'></i></a>
													</div>
												</td> --%>
											</tr>
										</c:forEach>

									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<td colspan="10" class="center">没有相关数据</td>
										</tr>
									</c:otherwise>
								</c:choose>


							</tbody>
						</table>
						<div class="page-header position-relative">
							<table style="width: 100%;">
								<tr>
									<c:if test="${pd.userName == 'admin' }">
										<td style="vertical-align: top;"><span
											class="btn btn-small btn-primary" style="width: 60px;"
											onclick="findChart();">查看图表</span></td>
									</c:if>
									<td style="vertical-align: top;"><div class="pagination"
											style="float: right; padding-top: 0px; margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
					</form>
				</div>




				<!-- PAGE CONTENT ENDS HERE -->
			</div>
			<!--/row-->

		</div>
		<!--/#page-content-->
	</div>
	<!--/.fluid-container#main-container-->

	<!-- 返回顶部  -->
	<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse"> <i
		class="icon-double-angle-up icon-only"></i>
	</a>


	<!-- 引入 -->
	<script type="text/javascript">
		window.jQuery
				|| document
						.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");
	</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>

	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript"
		src="static/js/bootstrap-datepicker.min.js"></script>
	<!-- 日期框 -->
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<!-- 引入 -->


	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!--提示框-->

	<script type="text/javascript">
		$(top.hangge());

		//检索
		function search() {
			top.jzts();
			$("#historyOrderForm").submit();
		}
		//清空
		function clearOrder() {
			$("#nav-search-input").val('');
			//$("#search-tick").val('');
			$("#user_id").val('');
			$("#cmd").val('2');
			$("#eveningUp").val('');
			$("#openTimeStart").val('');
			$("#openTimeEnd").val('');
			$("#historyOrderForm").submit();
		}
		//结算结果查询
		function accountSearch() {

		}
		//导出excel
		function ordertoExcel(){
			var symbol=$("#nav-search-input").val();
		    var userId=$("#user_id").val();
			var cmd=$("#cmd").val();
			var openTimeStart=$("#openTimeStart").val();
			var openTimeEnd=$("#openTimeEnd").val();
			window.location.href='<%=basePath%>historyOrderController/excel.do?symbol='+symbol+'&userId='+userId+'&cmd='+cmd+'&openTimeStart='+openTimeStart+'&openTimeEnd='+openTimeEnd;
		}
		
		//查看图标
		function findChart(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="历史订单图表";
			 diag.URL = '<%=basePath%>historyOrderController/findChartPage.do';
			 diag.Width = 650;
			 diag.Height = 450;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage('${page.currentPage}');
					 }
				}
				diag.close();
			 };
	          diag.show(); 
		  }
	</script>

	<script type="text/javascript">
		$(function() {
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

			//复选框
			$('table th input:checkbox').on(
					'click',
					function() {
						var that = this;
						$(this).closest('table').find(
								'tr > td:first-child input:checkbox').each(
								function() {
									this.checked = that.checked;
									$(this).closest('tr').toggleClass(
											'selected');
								});

					});

		});
	</script>

</body>
</html>

