<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/inc/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="/css/jquery-ui-1.10.3.custom.min.css" />
<link type="text/css" rel="stylesheet" href="/css/jquery-ui-timepicker-addon.css" />

<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-timepicker-addon.js"></script>
  <script type="text/javascript" src="/js/jquery-ui-timepicker-zh-CN.js"></script>
<style type="text/css">
tbody tr td:nth-child (n* 6){
	border-right: 2px solid #333;
	border:
}

.red-font{
	color:#FF0000;
}
</style>	
<title>sf query</title>
</head>
<body>
<div class="pageHeader" style="width: 100%;">
	<form id="queryForm"  action="/sf/query" method="post"
		name="order_wait_queryForm">
		<div class="searchBar" id="product_list_searchBar">
			<table class="searchContent pageFormContent">
				<tr>
					<td><label>下单时间:</label></td>
					<td><input type="text" name="startTime"
						value='<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'
						class="date" id="startDate" dateFmt="yyyy-MM-dd HH:mm:ss" style="float: none" size="25" />
						至 <input type="text" name="endTime"
						value='<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'
						class="date" dateFmt="yyyy-MM-dd HH:mm:ss" style="float: none" size="25" />
					</td>
				</tr>
				<tr>
					<td><label>订单状态:</label></td>
					<td><select name="orderStatus">
							<option value="-1" <c:if test="${orderStatus == '-1'}">selected="selected"</c:if>>全部</option>
							<option value="30" <c:if test="${orderStatus == '30'}">selected="selected"</c:if>>待发货</option>
							<option value="40" <c:if test="${orderStatus == '40'}">selected="selected"</c:if>>等待确认收货</option>
							<option value="50" <c:if test="${orderStatus == '50'}">selected="selected"</c:if>>已收货已付款</option>
							<option value="60" <c:if test="${orderStatus == '60'}">selected="selected"</c:if>>交易成功</option>
					</select>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="filter_submit" >
						<span class="ui-button-text">查询</span>
					</button>
					
					</td>
				</tr>
				<tr>
					<td>
						<button type="button"
							class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="down_template">
							<span class="ui-button-text">门店信息模板下载</span>
						</button>
					</td>
					<td>
						<button type="button" id="upload_shop_info"
							class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
							<span class="ui-button-text">上传门店信息</span>
						</button>
						<button type="button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="export_excel_file">
							<span class="ui-button-text">导出订单表</span>
						</button>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>

<div class="pageContent" style="width: 100%;">
	<table class="widefat post fixed"
		 border="1" cellspacing="0"    bordercolor="#00CCCC">
		<thead>
			<tr>
				<th style="text-align: center;"width="">门店序号</th>
				<th style="text-align: center;" width="">省份</th>
				<th style="text-align: center;" width="">区域</th>
				<th style="text-align: center;" width="">门店名称</th>
				<th style="text-align: center;" width="">门店帐号</th>
				<th style="text-align: center;" width="">订单号</th>
				<th style="text-align: center;" width="">下单时间</th>
				<th style="text-align: center;" width="">付款时间</th>
				<th style="text-align: center;" width="">订单状态</th>
				<th style="text-align: center;" width="">订单价格</th>
				<th style="text-align: center;" width="">订单价格(不含邮费)</th>
				<th style="text-align: center;" width="">商品ID</th>
				<th style="text-align: center;" width="">商品名称</th>
				<th style="text-align: center;" width="">商品单价</th>
				<th style="text-align: center;" width="">商品数量</th>
			</tr>
		</thead>
		<tbody id="log_content">
			<c:if test="${empty sfOrders}">
				<tr>
					<td style="text-align: center;" COLSPAN="8"><font
						color="#dc143c">暂无数据</font></td>
				</tr>
			</c:if>

            <c:if test="${!empty sfOrders}">
              <c:forEach items="${sfOrders}" var="sf">
                <tr>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.shopId}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.province}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.area}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.shopName}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.shopEmail}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.orderId}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}"><fmt:formatDate value="${sf.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}"><fmt:formatDate value="${sf.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.orderStatus}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.orderTotalPrice}</td>
                  <td rowspan="${fn:length(sf.orderGoodsItems)}">${sf.orderRealPay}</td>
                 <c:forEach items="${sf.orderGoodsItems}" var="pro" varStatus="stt" begin="0" end="0">
                  <td>${pro.goodsId }</td>
                  <td>${pro.goodsName }</td>
                  <td>${pro.goodsPrice }</td>
                  <td>${pro.saleNum} </td>
                 </c:forEach>
                 </tr>
                 <c:if test="${fn:length(sf.orderGoodsItems) > 1}">
                 
                 <c:forEach items="${sf.orderGoodsItems}" var="pro" varStatus="stt" begin="1" >
                 <tr>
                  <td>${pro.goodsId }</td>
                  <td>${pro.goodsName }</td>
                  <td>${pro.goodsPrice }</td>
                  <td>${pro.saleNum} </td>
                  </tr>
                 </c:forEach>
                 </c:if>
              </c:forEach>
            </c:if>

		</tbody>
	</table>
	
	<div id="fileUpDiv" style="display:none;position:absolute;left:200px;top:200px;width:200px;height:100px;background-color:gray;">
	   <form action="/sf/uploadShopInfo" enctype="multipart/form-data" method="post">
	      <input type="file" name="upfile" id="upfile"/>
	      <input type="submit" value="上 传">
	      <a href="javascript:$('#fileUpDiv').hide()">取消</a>
	   </form>
	</div>
	
	<c:if test="${!empty sfShops}">
<br />
<br />
<br />
<br />
<h4><font color="#FF0000">重要提示</font></h4>
<h5>您所上传的店面信息内，有以下帐号有错误，无法查询店面信息的销售情况，请重新确认您所填的帐号的有效性<br />
您可以修改帐号信息然后重新上传，继续操作</h5>
<table class="red-font"
		style="border: 1px; border-color: red;">
		<thead>
			<tr>
				<th style="text-align: center;"width="">序号</th>
				<th style="text-align: center;" width="">省份</th>
				<th style="text-align: center;" width="">区域</th>
				<th style="text-align: center;" width="">门店名称</th>
				<th style="text-align: center;" width="">门店代码</th>
				<th style="text-align: center;" width="">美丽湾（门店邮箱）</th>
			</tr>
		</thead>
		<tbody id="log_content">
			<c:if test="${empty sfShops}">
				<tr>
					<td style="text-align: center;" COLSPAN="8"><font
						color="#dc143c">暂无数据</font></td>
				</tr>
			</c:if>

            <c:if test="${!empty sfShops}">
              <c:forEach items="${sfShops}" var="sf">
                <tr>
                  <td >${sf.shopId}</td>
                  <td >${sf.provinces}</td>
                  <td >${sf.area}</td>
                  <td >${sf.shopName}</td>
                  <td >${sf.shopCode}</td>
                  <td >${sf.email}</td>
                </tr>
              </c:forEach>
            </c:if>

		</tbody>
	</table>

</c:if>

</div>
<script type="text/javascript" >
	//初始化时间控件
	$('.date').datetimepicker({
		timeFormat : "HH:mm:ss",
		dateFormat : "yy-mm-dd"
	});
	$('#down_template').click(function(){
		$('#queryForm').attr({'action':'/sf/download'}).submit();
	});
	$('#upload_shop_info').click(function(){
		//$('#queryForm').attr({'action':'/sf/download'}).submit();
		$('#fileUpDiv').show();
	});
	$('#filter_submit').click(function(){
		$('#queryForm').attr({'action':'/sf/query'}).submit();
	});
	$('#export_excel_file').click(function(){
		$('#queryForm').attr({'action':'/sf/exportFile'}).submit();
	});
</script>
</body>
</html>