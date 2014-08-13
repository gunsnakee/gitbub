<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/mms/passport/myOrderList">
    <input type="hidden" id="uid" name="uid" value="${uid}"/>
    <input type="hidden" id="order_state" name="order_state" value="${order_state}"/>
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return dialogSearch(this);" action="/mms/passport/myOrderList" method="post"
          id="order_wait_queryForm" name="order_wait_queryForm">

        <input type="hidden" name="uid" value="${uid}"/>

        <div class="searchBar" id="product_list_searchBar">
			<table class="searchContent pageFormContent">
				<tr>
                    <td>订单状态</td>
                    <td>
						<select name="order_state" >
                            <option value="">全部</option>
					<c:forEach var="e" items="${orderStateList}">
                        <option value='${e.code}' <c:if test="${e.code == order_state}">selected="selected"</c:if>>${e.desc}</option>
                    </c:forEach>
					</select>
					<span> </span>
					   <button type="submit">筛选</button>
					</td>
                </tr>
			</table>
        </div>
    </form>
</div>

<div class="pageContent">
   <div class="panelBar">
    </div>
    <table class="list" width="100%" layoutH="150" >
        <thead>
        <tr>
            <th align="center">商品编号</th>
            <th align="center">商品信息</th>
            <th align="center">美丽价(元)*数量</th>
            <th align="center">实收款(元)</th>
            <th align="center">订单状态</th>
			<th align="center">收货人</th>
			<th align="center">支付方式</th>
			<th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" colspan="8"><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="orderId"  rel="${e.orderId}" style="background: #ecf8ff">
					<td colspan="8" align="left">
					订单编号：${e.orderId} 
					下单时间：<fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                    <c:if test="${!empty e.payTime}">付款时间：<fmt:formatDate value="${e.payTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></c:if>
					</td>
				</tr>
				<c:forEach var="b" varStatus="j" items="${e.ordiList}">
					<c:if test="${j.index == 0}">
					<tr align="center">
						<td>${b.proId}</td>
						<td>${b.proName}</td>
						<td>${b.uintPrice} X ${b.saleNum}</td>
						<td rowspan="${fn:length(e.ordiList)}">${e.realPayAmount}</td>
						<td rowspan="${fn:length(e.ordiList)}">
						<c:forEach var="o" items="${orderStateList}">
							<c:if test="${o.code == e.orderStatus}">${o.desc}</c:if>
						</c:forEach>
						</td>
						<td rowspan="${fn:length(e.ordiList)}">${b.recvName}</td>
						<td rowspan="${fn:length(e.ordiList)}">
						<c:forEach var="o" items="${e.ordPays}">
							<c:forEach var="f" items="${payFormMap}">
								<c:if test="${f.code == o.payCode}">${f.desc}</c:if>
							</c:forEach>
						</c:forEach>
						</td>
						<td rowspan="${fn:length(e.ordiList)}">
							<p style="padding: 4px 0 0 0;">
							<a target="dialog" href="/oms/order/detail?order_id=${e.orderId}" width="800" height="600">查看详情</a>
							</p>
						</td>
					</tr>
					</c:if>
					<c:if test="${j.index != 0}">
						<tr align="center">
							<td>${b.proId}</td>
							<td>${b.proName}</td>
							<td>${b.uintPrice} X ${b.saleNum}</td>
						</tr>
					</c:if>
				</c:forEach>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/dialog_page.jsp" %>
</div>
			


<script type="text/javascript">
$(function(){

$("#order_wait_clean").click(function(check){
	$("#order_wait_queryForm input").each(function(i){
		var val = $(this).val("");
	});
	
	$("#order_wait_queryForm select").each(function(i){
		$(this).val("0")
	});
	check.preventDefault();
});

$("#order_first_status1").click(function(check){
	var $form= $("#order_wait_queryForm");
	$form.submit();
	check.preventDefault();
});

});

var cancelOrderCallBack = function(){
    $.pdialog.reload('/mms/passport/myOrderList?uid='+$('#uid').val()+'&order_state='+$('#order_state').val(),{},'dialog_account_info');
}

function order_first_status1(check){
	
	var $form= $("#order_wait_queryForm");
	$form.submit();
	check.preventDefault();
}
</script>
