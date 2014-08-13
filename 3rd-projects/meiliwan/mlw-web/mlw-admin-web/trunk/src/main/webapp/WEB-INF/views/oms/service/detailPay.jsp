<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<style Type="text/css">
<!--
  #ret_foot p{padding: 5px 10px 5px 10px;}
  #ret_foot .rowBox{border: solid #808080 1px;margin: 10px;overflow: hidden;}
-->
</style>
<div class="pageContent" layoutH="10">
	<div class="searchBar" id="product_list_searchBar">
		<table class="searchContent pageFormContent">
			<tr>
				<td><label>退换货编号：</label></td>
				<td>${retOrder.retordId}</td>
				
				 <td><label>类型：</label></td>
				<td>退货</td>
				
				<td><label>退换货状态:</label></td>
				<td>

				<c:forEach var="o" varStatus="i" items="${retOrderStatus}">
					<c:if test="${ordi.orderItemStatus==o.code}">
						${o.desc}
					</c:if>
				</c:forEach>

			</td>
			</tr>
			<tr>
				<td><label>申请时间:</label></td>
				<td>
				<fmt:formatDate value="${retOrder.retTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
				<td><label>涉及订单：</label></td>
				<td>${retOrder.oldOrdId}</td>
				<td></td>
				<td></td>
			</tr>
			
		</table>
	</div>

<div id="ret_foot">
<!-- ============================================== -->
<div class="pageContent" id="ret_detail">
    <table class="table" width="1150" layoutH="590" >
        <thead>
        <tr>   
            <th align="center">商品编号</th>
			<th align="center">商品标题</th>
			<th align="center">申请数量</th>
			<th align="center">数量</th>
			<th align="center">美丽价</th>
			<th align="center">总量</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty retOrder}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
			<tr target="retordItemId"  rel="${e.retordItemId}">
				<td>${retOrder.proId}</td>
				<td>${retOrder.proName}</td>
				<td>${ordi.saleNum}</td>
				<td>${retOrder.proCount}</td>
				<td>${ordi.uintPrice}</td>
				<td>${retOrder.retTotalAmount}</td>
			</tr>
        </tbody>
    </table>
</div>



<div class="rowBox">
		
	<p><span>问题描述：${retOrder.applyComments}</span></p>
	<p>凭证图片：

	<c:forEach var="z" items="${pics}">
		<div class="imgCube" style="float:left;"><a href="${z}" target="_blank"><img src="${z}80x80.jpg" /></a></div>
	</c:forEach>
	</p>
</div>

<form method="post" action="" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);" id="retorder_detail_form">

	<div class="rowBox">
		
		<p><span>联系人：${retOrder.recvName}</span><span>联系电话：${retOrder.mobile} ${retOrder.phone}</span></p>
		<p>顾客收件地址：${retOrder.province} ${retOrder.city} ${retOrder.county} ${retOrder.town}</p>
		<p>${retOrder.detailAddr}</p>
	</div>

	<div class="rowBox">
		<p><input type="checkbox" name="needRetpro" value="1"  <c:if test="${retOrder.needRetpro==1}">checked</c:if>>是否需要退货</p>
		<p>退回地址：${mlwAddr}</p>
	</div>

	<div class="rowBox">
		<p>预计商品退款金额：${retOrder.retTotalAmount}</p>
		<c:if test="ordiStatus.statusCode==80">
		<p>实际商品退款金额：${retOrder.retPayAmount}</p>
		</c:if>
	</div>

</form> 
</div>

</div>
<script type="text/javascript">
$(function(){


$("#ret_apply_agreeChange").click(function(check){

	var $form = $("#retorder_detail_form");
	$form.attr("action","/oms/service/agreeChange");
	$form.submit();
	check.preventDefault();
});

$("#ret_apply_agreeBack").click(function(check){

	var $form = $("#retorder_detail_form");
	$form.attr("action","/oms/service/agreeRecede");
	$form.submit();
	check.preventDefault();
});

$("#ret_apply_disAgree").click(function(check){

	var $form = $("#retorder_detail_form");
	$form.attr("action","/oms/service/disAgree");
	$form.submit();
	check.preventDefault();
});

})
</script>