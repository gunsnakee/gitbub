<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/oms/order/payList">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/oms/order/payList" method="post"
          id="order_wait_queryForm" name="order_wait_queryForm">
        <div class="searchBar" id="product_list_searchBar">
			<table class="searchContent pageFormContent">
				<tr>
                    <td><label>订单编号:</label></td>
                    <td><input type="text" name="order_id" value="${search.orderId}"/></td>
					
                    <td><label>收货人:</label></td>
                    <td><input type="text" name="recv_name" value="${search.recvName}"/></td>
                </tr>
				<tr>
                    <td><label>商品ID:</label></td>
                    <td><input type="text" name="pro_id" value="<c:if test='${search.proId>0}'>${search.proId}</c:if>"/></td>
					
                    <td><label>商品标题:</label></td>
                    <td><input type="text" name="pro_name" value="${search.proName}"/></td>
                </tr>
				<tr>
                    <td><label>实收款:</label></td>
                    <td><input type="text" name="real_pay_amount_min" value="<c:if test='${search.realPayAmountMin>0}'>${search.realPayAmountMin}</c:if>" />至
					<input type="text" name="real_pay_amount_max" value="<c:if test='${search.realPayAmountMax>0}'>${search.realPayAmountMax}</c:if>" style="float:none"/></td>
					
                    <td><label>下单时间:</label></td>
                    <td><input type="text" name="createTimeMin" value="" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>至<input type="text" name="createTimeMax" value="" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/></td>
                </tr>
				<tr>
                    <td><label>购买类型:</label></td>
                    <td><select name="buyType">
					<c:forEach var="e" items="${buyType}">
                        <option value='${e.code}' <c:if test="${e.code == search.buyType}">selected="selected"</c:if>>${e.desc}</option>
                    </c:forEach>
					</select></td>
					
                    <td><label>支付方式:</label></td>
                    <td><select name="pay_form">
						<option value="0">全部</option>
					<c:forEach var="e" items="${payFormMap}">
                        <option value='${e.code}' <c:if test="${e.code == search.payCode}">selected="selected"</c:if>>${e.desc}</option>
                    </c:forEach>
					</select></td>
                </tr>
				<tr>
                    <td><label>状态更新时间:</label></td>
                    <td>
					<select name="sort_value">
					<c:forEach var="e" items="${sortValueMap}">
                        <option value='${e.key}' <c:if test="${e.key == sortValue}">selected="selected"</c:if>>${e.value}</option>
                    </c:forEach>
					</select>
					</td>
					
                    <td>订单状态</td>
                    <td>
						<select name="order_state">
					<c:forEach var="e" items="${payStatus}">
                        <option value='${e.code}' <c:if test="${e.code == search.orderItemStatus}">selected="selected"</c:if>>${e.desc}</option>
                    </c:forEach>
					</select>
					<span> </span>
					<button type="submit">筛选</button>

						<button id="order_wait_clean">重置</button>
					</td>
                </tr>
			</table>
		
		
        </div>
    </form>
</div>


<!-- ==============================================
<div class="tabs" currentIndex="1" eventType="click">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li ><a href="javascript:order_first_status1();" ><span>标题1</span></a></li>
					<li><a href="javascript:;"><span>标题2</span></a></li>
					<li><a href="demo_page2.html" class="j-ajax"><span>标题3</span></a></li>
				</ul>
			</div>
		</div>
		<div class="tabsContent" style="height:20px;">
			<div>
			<div class="panelBar">
				<ul class="toolBar">
						<li><a title="确实要把订单批量取消吗?" target="selectedTodo" rel="ids" href="/oms/order/cancel" class="delete"><span>批量取消</span></a></li>
						<li class="line">line</li>
						<li><a title="确实要把订单批量取消吗?" target="selectedTodo" rel="ids" href="/oms/order/cancel" class="delete"><span>批量发货</span></a></li>
						<li class="line">line</li>
				</ul>
			</div>
	
			</div>
			<div>内容2</div>
			<div>wwwwww</div>
		</div>
	
	</div>
 ============================================== -->


<div class="pageContent">
   

    <table class="table" width="1150" layoutH="260" >
        <thead>
        <tr>
			<th align="center"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
			
			<th colSpan="2" align="center">商品信息</th>
            <th align="center">实收款</th>
            <th align="center">订单状态</th>
			<th align="center">收货人</th>
			<th align="center">支付方式</th>
			<th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
		
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
		
        <c:if test="${!empty pc.entityList}">
			
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="orderId"  rel="${e.orderId}">
					<td><input name="ids" value="${e.orderId}" type="checkbox"></td>
					<td colspan="7" align="center">
					订单编号：${e.orderId} 
					下单时间：
					
					<fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
					
					付款时间：
					<fmt:formatDate value="${e.payTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
					</td>
				</tr>
				<c:forEach var="b" varStatus="j" items="${e.ordiList}">
				
					<c:if test="${j.index == 0}">
					<tr>
						<td>${b.proId}</td>
						<td>${b.proName}</td>
						<td>${b.uintPrice} X ${b.saleNum}</td>
						<td rowSpan="${fn:length(e.ordiList)}">
						${e.realPayAmount}</td>
						<td rowSpan="${fn:length(e.ordiList)}">
						<c:forEach var="o" items="${payStatus}">
							<c:if test="${o.code == e.orderStatus}">${o.desc}</c:if>
						</c:forEach>
						</td>
						<td rowSpan="${fn:length(e.ordiList)}">
						${b.recvName}</td>
						<td rowSpan="${fn:length(e.ordiList)}">
						
						<c:forEach var="o" items="${e.ordPays}">
							<c:forEach var="f" items="${payFormMap}">
								<c:if test="${f.code == o.payCode}">${f.desc}</c:if>
							</c:forEach>
						</c:forEach>
						</td>
						<td rowSpan="${fn:length(e.ordiList)}">
							<div style="overflow:visible;">
							<c:if test="${30 != e.orderStatus}">
							<p style="padding: 2px 0 0 0;">
							<a title="确实要把订单取消吗?" target="ajaxTodo" href="/oms/order/cancel?ids=${e.orderId}" class="delete">
							<span>取消</span></a>
							</p>
							</c:if>
							
							<p style="padding: 4px 0 0 0;">
							<a target="navTab" href="/oms/order/detail?order_id=${e.orderId}">查看订单详情</a>
							</p>
							<c:if test="${30 != e.orderStatus}">
							<p style="padding: 4px 0 0 0;">
							<a target="dialog" href="/oms/order/addComment?orderId=${e.orderId}">添加备注</a>
							</p>
							</c:if>
							</div>
						</td>
					</tr>
					</c:if>
					<c:if test="${j.index != 0}">
						<tr>
							<td>${b.proId}</td>
							<td>${b.proName}</td>
							<td>${b.uintPrice} X ${b.saleNum}</td>
						</tr>
					</c:if>
					
				</c:forEach>
				
				
            </c:forEach>
        </c:if><!-- -->
		
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
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

	alert("ee");
	var $form= $("#order_wait_queryForm");
	$form.submit();
	check.preventDefault();
});



})
function order_first_status1(check){
	alert("aa");
	var $form= $("#order_wait_queryForm");
	$form.submit();
	check.preventDefault();
}
</script>
