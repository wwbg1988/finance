
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String baseIp = request.getScheme() + "://" +  request.getServerName() + "/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
	<head>
	<base href="<%=basePath%>">	
	<!-- jsp文件头和头部 -->
<%@ include file="../system/admin/top.jsp"%>
	</head>
</head>

<body>
		<div class="container-fluid" id="main-container">
<div id="page-content">
						
  <div class="row-fluid">

	<div class="row-fluid">
	
	<form action="web/user/findAllUserInfo.do" method="post"
		name="userInfoForm" id="userInfoForm">
			<!-- 检索  -->
			<table>
							<tr>
								<td><span class="input-icon"> <input
										autocomplete="off" id="nav-search-input" type="text"
										name="userName" value="${pd.userName}" placeholder="这里输入户名" />
										<i id="nav-search-icon" class="icon-search"></i>
								</span>
								</td>
								 <td>
								 <span class="input-icon"> <input autocomplete="off"
										id="nav-search-input" type="text" name="userTelphone"
										value="${pd.userTelphone}" placeholder="这里输入手机号码" /> <i
										id="nav-search-icon" class="icon-search"></i>
								</span>
								</td>
								<td style="vertical-align: top;">
								<select class="chzn-select" name="userState" id="userState" data-placeholder="请选择状态"
									style="vertical-align: top; width: 120px;">
										<option value=""></option>
						                <option value="">全部</option>
										<option value="1">通过</option>
										<option value="0">注册</option>
								</select>
								</td>
								 <td>
								<span class="input-icon"> <input autocomplete="off"
										id="nav-search-input" type="text" name="certificateNo"
										value="${pd.certificateNo}" placeholder="这里输入件号码" /> <i
										id="nav-search-icon" class="icon-search"></i>
								</span>
								</td>
								
								
								<c:if test="${QX.cha == 1 }">
									<td style="vertical-align: top;"><button
											class="btn btn-mini btn-light" onclick="search();" title="检索">
											<i id="nav-search-icon" class="icon-search"></i>
										</button></td>
								</c:if>
							</tr>
						</table>
			<!-- 检索  结束-->
		<table id="table_report"
			class="table table-striped table-bordered table-hover">

			<thead>
				<tr align="center">
				
					<th>序号</th>
					<th>用户名</th>
					<th>审核状态</th>
					<th>证件号码</th>
					<th>图片地址</th>
					<th>电话号码</th>
					<th>创建时间</th>
					<th class="center">操作</th>
				</tr>
			</thead>

			<tbody>
				<!--${productMethod.productionMethod}-->
				<!-- 开始循环 -->
				<c:choose>
					<c:when test="${not empty userInfoList}">
						<c:forEach items="${userInfoList}" var="userInfo" varStatus="pm">
							<tr>
								<td class='center' style="width: 30px;">${pm.index+1}</td>
								<td>${userInfo.userName}</td>
								<td><c:if test="${userInfo.userState == 0 }">
								 <span class="btn btn-small btn-info ">  待审核</span>
								</c:if> <c:if test="${userInfo.userState == 1 }">
								  	 <span class="btn btn-small btn-success "> 注册完成</span>
								</c:if>
							    <c:if test="${userInfo.userState == 2 }">
								  	 <span class="btn btn-small btn-success "> 在线</span>
								</c:if>
								  <c:if test="${userInfo.userState == 3 }">
								  	 <span class="btn btn-small btn-success "> 离线</span>
								</c:if>
								</td>
								<td>${userInfo.certificateNo}</td>
								<td><c:if test="${not empty userInfo.certificateUrl}">
										<img src="<%=baseIp%>${userInfo.certificateUrl}"
											name="certificateUrl" style="width: 70px; height: 70px;">
									</c:if></td>
								<td>${userInfo.userTelphone}</td>
								<td>${userInfo.createTime}</td>
								<td><a class='btn btn-mini btn-danger' title="审核"
									onclick="auditUser('${userInfo.id}')"></i>审核</a></td>
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
	</div></div></div></div>
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
	//删除
	function auditUser(id){
		var return_msg="";
		
		bootbox.confirm("确定要审核通过吗?", function(result) {
			if(result) {
				top.jzts();
			var url="<%=basePath%>web/user/audit.do";

		        $.post(url, {
						id : id
					}, function(result) {
						  return_msg = result.msg;
							nextPage(${page.currentPage});
					        if(return_msg!=""){
								alert(return_msg);
								
							}
					 location.href='<%=basePath%>web/user/findAllUserInfo.do';
					}, 'JSON');
		}
			});
	}
	
	</script>
</body>
</html>