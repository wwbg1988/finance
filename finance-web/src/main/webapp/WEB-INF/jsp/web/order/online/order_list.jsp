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
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
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
					<form action="onlineOrder/findAllOrder.do" method="post"
						name="onlineOrderForm" id="onlineOrderForm">
						<table>

							<tr>
								<td align="left" style="padding-bottom: 13px;"><span
									style="font-size: x-large; font-weight: bolder;"><a
										class="btn btn-mini btn-search"><font color="#000000;">搜索查询:</font></a></span>
								</td>
								
							<%-- 	<td><input autocomplete="off" id="nav-search-input"
									type="text" name="currencyType" value="${pd.currencyType}"
									placeholder="请输入货币名称" /></td> --%>
								<td style="vertical-align: top;"><select
									class="chzn-select" name="currencyType" id="nav-search-input"
									data-placeholder="请选择货币"
									style="vertical-align: top; width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<c:forEach items="${currencyList}" var="currency">
											<option value="${currency.currency }"
												<c:if test="${pd.currencyType==currency.currency}">selected</c:if>>${currency.currency }</option>
										</c:forEach>
								</select></td>
								<%-- <td><span class="input-icon"> <input
										autocomplete="off" id="tick-input" type="text"
										name="tick" value="${pd.tick}" placeholder="请输入订单号" /> <i
										id="nav-search-icon" class="icon-search"></i>
								</span></td> --%>
								<td style="vertical-align: top;"><select
									class="chzn-select" name="USER_ID" id="order_user_id"
									data-placeholder="请选择用户"
									style="vertical-align: top; width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<c:forEach items="${userList}" var="user">
											<option value="${user.USER_ID }"
												<c:if test="${pd.USER_ID==user.USER_ID}">selected</c:if>>${user.NAME }</option>
										</c:forEach>
								</select></td>
								<td style="vertical-align: top;"><select title="买卖类型";
									class="chzn-select" name="direction" id="order_direction"
									data-placeholder="买卖类型"
									style="vertical-align: top; width: 120px;">
										
										<c:forEach items="${cmdMap}" var="m">
											<option value="${m.value}"
												<c:if test="${pd.direction==m.value}">selected</c:if>>${m.key}</option>
										</c:forEach>
								</select></td>
								<c:if test="${QX.cha == 1 }">
									<td style="vertical-align: top;"><button
											class="btn btn-mini btn-light" onclick="search();" title="检索">
											<i id="nav-search-icon" class="icon-search"></i>
										</button></td>
									<td style="vertical-align: top;"><button
											class="btn btn-mini btn-light" onclick="clearOnlineOrder();"
											title="清空">
											<i id="nav-search-icon" class="icon-remove"></i>
										</button></td>
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

									<th class="center">类型</th>
									<th class="center">开仓价格</th>
									<th>数量</th>
									<th>分钟(min)</th>
									<th><i class="icon-time hidden-phone"></i>开仓时间</th>
									<!-- <th class="center">操作</th> -->
								</tr>
							</thead>

							<tbody>

								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty onlineOrderDtoList}">
										<c:forEach items="${onlineOrderDtoList}" var="onlineOrder"
											varStatus="vs">
											<tr>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td>${onlineOrder.tick}</td>
												<td>${onlineOrder.userName}</td>
												<td>${onlineOrder.currencyType}</td>

												<td class="center"><c:if
														test="${onlineOrder.direction == 0 }">
														<span class="btn btn-small btn-info "> 买入</span>
													</c:if> <c:if test="${onlineOrder.direction == 1 }">
														<span class="btn btn-small btn-purple"> 卖出</span>
													</c:if></td>
												<td class="center"><span
													class="btn btn-small btn-danger" style="width: 60px;">
														${onlineOrder.price}</span></td>
												<td>${onlineOrder.total}</td>
												<td><font
													style="font-size: 15px; font-weight: bolder; color: gray;">${onlineOrder.comment}</font></td>
												<td><font
													style="font-size: 15px; font-weight: bolder; color: gray;">${onlineOrder.timeFormatString}</font></td>
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
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
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
		function search(){
			
		
			top.jzts();
			$("#onlineOrderForm").submit();
		}
		
		setInterval("search()",2000);
		//清空
		function clearOnlineOrder() {
			$("#nav-search-input").val('');
			$("#tick-input").val('');
			$("#order_user_id").val('');
			$("#order_direction").val('2');
		    $("#onlineOrderForm").submit();
		}
	
		//删除
		function delcolor(color_id){
			var return_msg="";
			bootbox.confirm("确定要删除["+343+"]吗?", function(result) {
				if(result) {
					top.jzts();
					var url = '<%=basePath%>mt4Config/delete.do';
					$.ajaxSetup({   
			            async : false  
			        }); 
					$.post(url, {
						id : color_id
					}, function(result) {
						return_msg=result.msg;
						alert(return_msg);
					
					}, 'JSON');
				    location.href='<%=basePath%>mt4Config/findAllMt4Config.do';
								}

							});
		}
	</script>

	<script type="text/javascript">
		$(function() {

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

