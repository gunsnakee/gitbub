<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<style Type="text/css">
<!--
  #apply_table td p{padding: 5px 0 0 0;}
-->
</style>
<form id="pagerForm" method="post" action="/oms/service/apply">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/oms/service/apply" method="post"
          id="order_apply_queryForm">
        <div class="searchBar">
			<table class="searchContent pageFormContent">
				<tr>
                    <td><label>退换货单号:</label></td>
                    <td><input type="text" name="orderId" value="${search.orderId}"/></td>
					
                    <td><label>退换货状态:</label></td>
                    <td>
					<select name="statusCode">
					<c:forEach var="e" items="${applyStatus}">
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
                    <td><input type="text" name="createTimeMin" value='<fmt:formatDate value="${search.orderTimeStart}" pattern="yyyy-MM-dd HH:mm:ss"/>' class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>至<input type="text" name="createTimeMax" value='<fmt:formatDate value="${search.orderTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>' class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/></td>
					<td></td>
					<td>
						<button type="submit">筛选</button>
						<button id="ret_apply_clean">重置</button>
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
						<c:if test="${e.applyRetType==o.code}">
							${o.desc}
						</c:if>
					</c:forEach>
					</td>
					<td>
					<fmt:formatDate value="${b.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
					</td>
					<td>
					<c:forEach var="o" varStatus="i" items="${allStatus}">
						<c:if test="${b.orderItemStatus==o.code}">
							${o.desc}
						</c:if>
					</c:forEach>
					</td>
					<td>
						<p style="padding: 3px;">
                            <a title="查看处理" target="navTab" href="/oms/service/detail?retordItemId=${b.orderItemId}"><span style="color: #0000ff">查看处理</span></a>
						<p>
						<!--<p style="padding: 3px;">
                            <a class="apply_refund" href="#" onclick="openwin('${b.orderItemId}')"><span style="color: #0000ff">退款</span></a>
						<p>-->
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

$("#ret_apply_clean").click(function(check){
	$("#order_apply_queryForm input").each(function(i){
		var val = $(this).val("");
			
	});
	
	$("#order_apply_queryForm select").each(function(i){
		$(this).val("0")
	});
	check.preventDefault();
});


})

function openwin(orderItemId) { 
	window.open ("/oms/service/retpayForm?retordItemId="+orderItemId, "newwindow", "height=100, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no"); 
}

</script>