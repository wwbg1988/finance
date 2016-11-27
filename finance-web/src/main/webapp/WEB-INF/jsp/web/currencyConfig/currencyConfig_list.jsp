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
<%@ include file="../../system/admin/top.jsp"%>
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
					<form action="currencyConfigController/findCurrencyConfigList.do" method="post"
						name="currencyConfigForm" id="currencyConfigForm">
						
						<!-- 检索  -->


						<table id="table_report"
							class="table table-striped table-bordered table-hover">

							<thead>
								<tr>
									<th>序号</th>
									<th >货币名称</th>
									<th >货币利率</th>
									<th >是否启用</th>
								    <th >创建时间</th>
									<th class="center">操作</th>
								</tr>
							</thead>

							<tbody>

								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty currencyConfigList}">
										<c:forEach items="${currencyConfigList}" var="currencyConfig"
											varStatus="vs">
											<tr>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td ><span class="btn btn-small btn-info" style="width: 80px;">${currencyConfig.currency}</span></td>
												<td ><div class="progress progress-mini progress-danger" style="height:19px;width: 50%;"  title="${currencyConfig.currencyRates*100}%"><div class="bar" style="width:${currencyConfig.currencyRates*100}%;">  <span class="sr-only" style="line-height:18px;">${currencyConfig.currencyRates*100}%</span></div></div></td>
											<%-- 	<td><c:if test="${currencyConfig.isEnable == 1 }">
														启用
													</c:if> <c:if test="${currencyConfig.isEnable == 0 }">
													     禁用
													</c:if></td> --%>
													<td ><label><input type="checkbox" class="ace-switch ace-switch-3" <c:if test="${currencyConfig.isEnable == 1 }">checked="checked"</c:if>  onclick="editConfigEnable('${currencyConfig.id}')"/><span class="lbl"></span></label></td>
												<td >${currencyConfig.createTimeFormatString}</td>
												<td style="width: 120px;"  class='center'>
													<div class='hidden-phone btn-group'>
														<a class='btn btn-mini btn-info' title="编辑"
															onclick="editAccount('${currencyConfig.id }');"><i
															class='icon-edit'></i></a> <a class='btn btn-mini btn-danger'
															title="删除"
															onclick="delcolor('${currencyConfig.id }');"><i
															class='icon-trash'></i></a>
													</div>
												</td>
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
									<td style="vertical-align: top;"><a
										class="btn btn-small btn-success" onclick="add();">新增</a></td>
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
			$("#currencyConfigForm").submit();
		}
	
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增货币利率配置项";
			 diag.URL = '<%=basePath%>currencyConfigController/addPage.do';
			 diag.Width = 365;
			 diag.Height = 270;
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
		//	 diag.OnLoad = function() {
		//	 	$(diag.innerDoc.getElementsByClassName('dwrap')).css('width', '276px');
		//	 }
			 diag.show();
		}
		
		//修改
		function editAccount(id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑货币利率配置项";
			 diag.URL = '<%=basePath%>currencyConfigController/editPage.do?id='+id;
			 diag.Width = 365;
			 diag.Height = 250;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					nextPage('${page.currentPage}');
				}
				diag.close();
			 };
			 diag.show();
		}
	
		//删除
		function delcolor(color_id){
			var return_msg="";
			bootbox.confirm("确定要删除改配置项吗?", function(result) {
				if(result) {
					top.jzts();
					var url = '<%=basePath%>currencyConfigController/delete.do';
					$.ajaxSetup({   
			            async : false  
			        }); 
					$.post(url, {
						id : color_id
					}, function(result) {
						return_msg=result.msg;
						alert(return_msg);
					
					}, 'JSON');
				    location.href='<%=basePath%>currencyConfigController/findCurrencyConfigList.do';
				}

			});
		} 
		
		function editConfigEnable(id){
		        var url = "<%=basePath%>currencyConfigController/editConfigEnable.do?id="+id;
				$.get(url,function(data){
					if(data=="success"){
						//document.location.reload();
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

