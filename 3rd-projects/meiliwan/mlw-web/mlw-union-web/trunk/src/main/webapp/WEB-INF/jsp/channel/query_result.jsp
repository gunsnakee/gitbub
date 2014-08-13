<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/inc/taglib.jsp"%>
<!DOCTYPE HTML>

<link type="text/css" rel="stylesheet"
	href="/css/jquery-ui-1.10.3.custom.min.css" />
<link type="text/css" rel="stylesheet"
	href="/css/jquery-ui-timepicker-addon.css" />

<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/js/jquery-ui-timepicker-zh-CN.js"></script>
<style>
.pageContent td {
	border: 1px solid blue;
	text-align: center;
	border-spacing: 0;
	border-margin: 0;
}

th {
	border: 1px solid blue;
	text-align: center;
	border-spacing: 0;
	border-margin: 0;
}

.red-font {
	color: #FF0000;
}
</style>
<div class="pageHeader" style="width: 100%;">
	<!-- onsubmit="return navTabSearch(this);" -->
	<form id="queryForm" action="/channel/query" method="post"
		name="order_wait_queryForm">
		<div class="searchBar" id="product_list_searchBar">
			<table class="searchContent pageFormContent">
				<tr>
					<td><label>下单时间:</label></td>
					<td><input type="text" name="startTime"
						value='<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'
						class="date" id="startDate" dateFmt="yyyy-MM-dd HH:mm:ss"
						style="float: none" size="23" /> 至 <input type="text"
						name="endTime" id="endDate"
						value='<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'
						class="date" dateFmt="yyyy-MM-dd HH:mm:ss" style="float: none"
						size="23" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span id="dateInfoSpan" style="color: red;">${dateInValid }</span>
						</td>
				</tr>
				<tr>
					<td><label>订单状态:</label></td>
					<td><select name="orderStatus">
							<option value=""
								<c:if test="${orderStatus == ''}">selected="selected"</c:if>>全部</option>
							<option value="10"
								<c:if test="${orderStatus == '10'}">selected="selected"</c:if>>待付款</option>
							<option value="30"
								<c:if test="${orderStatus == '30'}">selected="selected"</c:if>>待发货</option>
							<option value="40"
								<c:if test="${orderStatus == '40'}">selected="selected"</c:if>>等待确认收货</option>
							<option value="60"
								<c:if test="${orderStatus == '60'}">selected="selected"</c:if>>交易成功</option>
							<option value="80"
								<c:if test="${orderStatus == '80'}">selected="selected"</c:if>>已取消</option>
					</select></td>
				</tr>
				<tr>
					<td><label>渠道:</label></td>
					<td><c:if test='${userName != "admin" }'>
							<input type="hidden" value="${source }" name="source">
							<label>${channelMap[source] }</label>
						</c:if> <c:if test='${ userName == "admin"}'>
							<select name="source">
								<option value=""
									<c:if test="${source == ''}">selected="selected"</c:if>>全部</option>
								<c:forEach items="${channelMap }" var="channel">
									<option value="${channel.key }"
										<c:if test="${source == channel.key }">selected="selected"</c:if>>${channel.value }</option>
								</c:forEach>
							</select>
						</c:if></td>
				</tr>
				<tr>
					<td>
						<button type="button"
							class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
							id="export-order-list">
							<span class="ui-button-text">导出订单表</span>
						</button>
						<button type="button" id="filter-order-list"
							class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
							<span class="ui-button-text">筛选</span>
						</button>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>

<div class="pageContent" style="width: 100%;">
    <div align="left">总金额：${totalPrice } 元</div>
	<table class="widefat post fixed" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<th style="text-align: center;" width="">序 号</th>
				<th style="text-align: center;" width="">渠 道</th>
				<th style="text-align: center;" width="">订单号</th>
				<th style="text-align: center;" width="">订单价格</th>
				<th style="text-align: center;" width="">订单状态</th>
				<th style="text-align: center;" width="">下单时间</th>
				<th style="text-align: center;" width="">商品ID</th>
				<th style="text-align: center;" width="">商品名称</th>
				<th style="text-align: center;" width="">商品单价</th>
				<th style="text-align: center;" width="">商品数量</th>
			</tr>
		</thead>
		<tbody id="log_content">
			<c:if test="${empty uoList}">
				<tr>
					<td style="text-align: center;" COLSPAN="8"><font
						color="#dc143c">暂无数据</font></td>
				</tr>
			</c:if>

			<c:if test="${!empty uoList}">
				<c:forEach items="${uoList }" var="uo" varStatus="stt">
					<tr>
						<td rowspan="${fn:length(uo.proInfos)}">${stt.count }</td>
						<td rowspan="${fn:length(uo.proInfos)}">${channelMap[uo.sourceidId] }</td>
						<td rowspan="${fn:length(uo.proInfos)}">${uo.orderId }</td>
						<td rowspan="${fn:length(uo.proInfos)}">${uo.totalPrice }</td>
						<td rowspan="${fn:length(uo.proInfos)}">${uo.orderStringStatus }</td>
						<td rowspan="${fn:length(uo.proInfos)}"><fmt:formatDate
								value="${uo.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<c:forEach items="${uo.proInfos }" var="pro" varStatus="stt"
							begin="0" end="0">
							<td>${pro.proId }</td>
							<td>${pro.proName }</td>
							<td>${pro.price }</td>
							<td>${pro.buyNum }</td>
						</c:forEach>
					</tr>

					<c:if test="${fn:length(uo.proInfos) > 1}">
						<c:forEach items="${uo.proInfos }" var="pro" varStatus="stt"
							begin="1">
							<tr>
							<td>${pro.proId }</td>
							<td>${pro.proName }</td>
							<td>${pro.price }</td>
							<td>${pro.buyNum }</td>
							</tr>
						</c:forEach>
					</c:if>
				</c:forEach>
			</c:if>

		</tbody>
	</table>

</div>
<script type="text/javascript">
	//初始化时间控件
	$('.date').datetimepicker({
		timeFormat : "HH:mm:ss",
		dateFormat : "yy-mm-dd"
	});

	function compareDate(){
       var startTime = $('#startDate').val();
       var endTime = $('#endDate').val();

       if (startTime > endTime){
           $('#dateInfoSpan').text('开始时间不能大于结束时间');
           return false;
       }

       return true;
	}

	$('#export-order-list').click(function() {
		if(compareDate()){
  		  //$('#queryForm').attr({'action':'/union/download'}).submit();
		  $('#queryForm').attr({
			'action' : '/channel/export'
		  }).submit();
		}
	});

	$('#filter-order-list').click(function() {
		if(compareDate()){
		  $('#queryForm').attr({
			'action' : '/channel/query'
		  }).submit();
		}
	});
</script>