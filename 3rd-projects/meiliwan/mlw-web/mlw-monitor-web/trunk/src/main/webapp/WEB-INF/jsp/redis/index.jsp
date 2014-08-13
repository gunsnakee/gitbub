<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>redisMonitor</title>

	<script type="text/javascript" src="/js/jquery-1.8.3.min.js" charset="utf-8"></script>
	<link href="/resources/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="/resources/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link href="/css/redis/style.css" rel="stylesheet">

	<link href="/resources/messager/messenger.css" rel="stylesheet">
	<link href="/resources/messager/messenger-theme-future.css" rel="stylesheet">
	<script type="text/javascript" src="/resources/messager/messenger.min.js"></script>
	<script type="text/javascript" src="/resources/messager/messagerUtil.js"></script>

	<link href="/resources/sco/scojs.css" rel="stylesheet">
	<script type="text/javascript" src="/resources/sco/sco.modal.js"></script>
	<script type="text/javascript" src="/resources/sco/sco.confirm.js"></script>
	<!-- ====================== -->

	<script type="text/javascript" src="/resources/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="/resources/bootstrap/js/bootstrap-dropdown.js"></script>
	<script type="text/javascript" src="/resources/bootstrap/js/bootstrap-scrollspy.js"></script>
	<script type="text/javascript" src="/resources/bootstrap/js/bootstrap-button.js"></script>


</head>
<body>
	<div style="padding-left: 20px;">
		<select id="redis_uuid_select" name="uuid">
			<c:forEach var="rs" items="${redisServerList }">
				<option value="${rs.uuid }" <c:if test="${ rs.uuid==uuid}">selected="selected"</c:if>> 
					${rs.description }
		   		</option>
		    </c:forEach>
		</select>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav info">
					<table class="table table-hover">
						<thead>
							<tr>
								<th colspan="2">Infomation</th>
							</tr>
						</thead>
						<tbody id="redis_server_status" >
						  <c:forEach var="r" items="${rifList}">
						       <tr id="${r.key}">
								  <td title="${r.key}:${r.desctiption }">${r.desctiption}</td> 
								  <td>${r.value}</td>
							   </tr>
						  </c:forEach> 
						</tbody>
					</table>
				</div>
				<!--/.well -->
			</div>
			<!--/span-->
			<div class="span9">
				<!-- <div class="hero-unit chart">
					<div id="container"></div>
				</div>
				<div class="hero-unit chart">
					<div id="keysChart"></div>
				</div> -->
				<div class="hero-unit chart info">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>id</th>
								<th>创建时间</th>
								<th>执行时间</th>
								<th>用时(毫秒)</th>
								<th>执行命令</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="op" items="${opList }">
						    <tr>
								<td>${op.id }</td>
								<td>${op.createTime }</td>
								<td>${op.executeTime }</td>
								<td>${op.usedTime }</td>
								
								<td><p style="width:600px;word-wrap: break-word;word-break: normal;">${op.args }</p></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>

			</div>
			<!--/span-->
		</div>
		<!--/row-->
	</div>
<script>
$("#redis_uuid_select").change(function(){
	var $this = $(this);
	var uuid = $this.val();
	var data = {"uuid":uuid};
	$.ajax({
      type: "POST",
      url: "/redis/index",
      data:data,
    }).done(function( msg ) {
      
      $("#content").html(msg);

    });

});
	//.val();
var $serverStatus = $("#redis_server_status");
/*
"keyspace_hits", "keyspace_misses",
"db0", "used_memory_human", "used_memory", "used_memory_rss",
"used_memory_peak", "rejected_connections", "connected_clients"
*/
$("#keyspace_hits").insertBefore($serverStatus);
$("#keyspace_misses").insertBefore($serverStatus);
$("#db0").insertBefore($serverStatus);
$("#used_memory_human").insertBefore($serverStatus);
$("#used_memory").insertBefore($serverStatus);
$("#used_memory_rss").insertBefore($serverStatus);
$("#used_memory_peak").insertBefore($serverStatus);
$("#rejected_connections").insertBefore($serverStatus);
$("#connected_clients").insertBefore($serverStatus);
</script>
</body>
</html>
