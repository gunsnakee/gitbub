<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/oms/service/pay">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/oms/service/pay" method="post"
          id="ret_pay_form" name="ret_pay_search_form">
        <div class="searchBar">
			<table class="searchContent pageFormContent">
				<tr>
                    <td><label>退换货单号:</label></td>
                    <td><input type="text" name="orderId" value="${search.orderId}"/></td>
					
                    <td><label>退换货状态:</label></td>
                    <td>
					<select name="statusCode">
					<c:forEach var="e" items="${payStatus}">
                        <option value='${e.code}' <c:if test="${e.code == search.orderItemStatus}">selected="selected"</c:if>>${e.desc}</option>
                    </c:forEach>
					</select>
					</td>
                </tr>
				<tr>
                    <td><label>商品编号:</label></td>
                    <td><input type="text" name="proId" value='<c:if test="${search.proId != 0}">${search.proId}</c:if>'/></td>
					
                    <td><label>商品标题:</label></td>
                    <td><input type="text" name="proName" value="${search.proName}"/></td>
                </tr>
				<tr>
                    <td><label>申请时间:</label></td>
                    <td><input type="text" name="createTimeMin" value="" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>至<input type="text" name="createTimeMax" value="" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/></td>
					<td></td>
					<td>
						<button type="submit">筛选</button>
						<button id="ret_pay_clean">重置</button>
					</td>
                </tr>
			</table>
        </div>
    </form>
</div>

<!-- ============================================== -->
<div class="pageContent">
    <table class="table" width="1150" layoutH="160" id="apply_table">
        <thead>
        <tr>
            <th align="center">退换货编号</th>
            <th align="center">商品编号</th>
			<th align="center">商品标题</th>
			<th align="center">涉及订单</th>
			<th align="center">数量</th>
			<th align="center">类型</th>
			<th align="center">申请时间</th>
			<th align="center">退换货状态</th>
			<th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
		
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
		
        <c:if test="${!empty pc.entityList}">
			
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
			
				<c:forEach var="b" varStatus="j" items="${e.ordiList}">
                <tr target="orderItemId"  rel="${b.orderItemId}">
					<td>${b.orderId}</td>
					<td>${b.proId}</td>
					<td>${b.proName}</td>
					<td>${e.oldOrderId}</td>
					<td>${b.saleNum}</td>
					<td>
					<c:forEach var="o" items="${retType}">
						<c:if test="${b.orderType==o.code}">
							${o.desc}
						</c:if>
					</c:forEach>
					</td>
					<td>
					<fmt:formatDate value="${b.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
					</td>
					<td>
		
					<c:forEach var="o" varStatus="i" items="${payStatus}">
						<c:if test="${b.orderItemStatus==o.code}">
							${o.desc}
						</c:if>
					</c:forEach>
					
					</td>
					<td>
						<p style="padding: 3px;">
                            <a title="查看处理" target="navTab" href="/oms/service/detailPay?retordItemId=${b.orderItemId}"><span style="color: #0000ff">查看处理</span></a>
						</p>
						
						<c:if test="${b.orderItemStatus==30}">
						<p style="padding: 3px;">
                            <a title="你确认要收货吗？" target="ajaxTodo" href="/oms/service/payReceive?retordItemId=${b.orderItemId}" ><span style="color: #0000ff">确认收货</span></a>
						</p>
						<p style="padding: 3px;">
                            <a title="你确认要发起协商吗？" target="dialog" href="/oms/service/consultation?retordItemId=${b.orderItemId}" ><span style="color: #0000ff">发起协商</span></a>
						</p>
						</c:if>
						
						<c:if test="${b.orderItemStatus==70||b.orderItemStatus==76}">
						<p style="padding: 3px;">
                            <a class="apply_refund" href="#" onclick="openwin('${b.orderItemId}')"><span style="color: #0000ff">退款</span></a>
						<p>
						
						</c:if>
					</td>
				</tr>
				</c:forEach>
            </c:forEach>
        </c:if>
		
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>

<script type="text/javascript">
$(function(){

$("#ret_pay_clean").click(function(check){
	$("#ret_pay_form input").each(function(i){
		var val = $(this).val("");
			
	});
	
	$("#ret_pay_form select").each(function(i){
		$(this).val("0")
	});
	check.preventDefault();
});

});

function openwin(orderItemId) { 
	window.open ("/oms/service/retpayForm?retordItemId="+orderItemId, "newwindow", "height=200, width=500, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
}

</script>
