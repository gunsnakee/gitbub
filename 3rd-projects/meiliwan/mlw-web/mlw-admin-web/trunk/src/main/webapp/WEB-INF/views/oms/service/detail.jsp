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
				<td><c:forEach var="o" varStatus="i" items="${retType}">
					<c:if test="${retOrder.applyRetType==o.code}">
						${o.desc}
					</c:if>
				</c:forEach></td>
				
				<td><label>退换货状态:</label></td>
				<td>
				<c:forEach var="o" varStatus="i" items="${retOrderStatus}">
					<c:if test="${ordi.orderItemStatus==o.code}">
						${o.desc}
					</c:if>
				</c:forEach></td>
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
<form method="post" action="" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);" id="retorder_detail_form">

<!-- ============================================== -->
<div class="pageContent" id="ret_detail">
    <table  border="1px" width="900" layoutH="590" >
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
				<td width="80px" id="retMaxSaleNum">${ordi.saleNum}</td>
				<td width="80px">
				<a href="javascript:;" class="reduce" onclick="setAmount.reduce('#proCount')">-</a>
					<input type="text" size="5" name="proCount" class="input-bar" value="${retOrder.proCount}" id="proCount" onkeyup="setAmount.modify('#proCount');">
				<a href="javascript:;" class="add" onclick="setAmount.add('#proCount')">+</a>
				</td>
				<td id="uintPrice">${ordi.uintPrice}
				</td>
				<td id="retTotalAmount">${retOrder.retTotalAmount}
				
				</td>
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
	
	

	<div class="rowBox">
		
		<p><span>联系人：${retOrder.recvName}</span><span>联系电话：${retOrder.mobile} ${retOrder.phone}</span></p>
		<p>顾客收件地址：${retOrder.province} ${retOrder.city} ${retOrder.county} ${retOrder.town}</p>
		<p>${retOrder.detailAddr}</p>
	</div>

	<div class="rowBox">
		<p>用户申请退换货类型：
		<c:forEach var="o" varStatus="i" items="${retType}">
			<c:if test="${retOrder.applyRetType==o.code}">
				${o.desc}
			</c:if>
		</c:forEach></p>
		<p><input type="checkbox" name="needRetpro" value="1" checked>是否需要退货</p>
		<p>退回地址：${mlwAddr}</p>
	</div>

	<div class="rowBox">
		<p>预计商品退款金额：${retOrder.retPayAmount}</p>
		<c:if test="${ordi.orderItemStatus==10}">
		<p>给顾客退运费:<span><input type="radio" name="needRetFare" id="backTransportFeeYes" value="1">是
		<input name="backTransportFee" type="text" value="" id="backTransportFee" class="number textInput valid disabled" disabled="disabled">
		</span>
		<span><input type="radio" name="needRetFare" checked id="backTransportFeeNo" value="0">否</span>
		</p>
		</c:if>
		<c:if test="${ordi.orderItemStatus!=10}">
		<p>给顾客退运费:<span>${retOrder.retPayFare}</span></p>
		</c:if>
	</div>

	<div class="rowBox">
		<p>请选择原因</p>
		<p>
		<c:forEach var="o" varStatus="i" items="${reasonType}">
			<span><input name="reasonType" type="radio" value="${o.code}" 
			 <c:if test="${o.code==retOrder.returnReason}">checked</c:if>
			<c:if test="${i.last && ordi.orderItemStatus==10}">checked</c:if>
			>
			${o.desc}</span>
		</c:forEach>
		</p>
	</div>

	<c:if test="${ordi.orderItemStatus==10}">
	<div class="rowBox">
		<input type="hidden" name="retordItemId" value="${retOrder.retordItemId}">
		<input type="hidden" name="retordId" value="${retOrder.retordId}">
		<p>
		<c:if test="${retOrder.applyRetType=='CHG'}">
		<button id="ret_apply_agreeChange">同意换货</button>
		<button id="ret_apply_agreeRecede">调整为退货</button>
		</c:if>
		<c:if test="${retOrder.applyRetType=='REC'}">
		<button id="ret_apply_agreeChange">调整为换货</button>
		<button id="ret_apply_agreeRecede">同意退货</button>
		</c:if>
		<button id="ret_apply_disAgree">拒绝</button></p>
	</div>
	</c:if>
</form> 
</div>

</div>

<script type="text/javascript">
//商品数量控制
var setAmount = {
min: 1,
max: $("#retMaxSaleNum").text(),
count: $("#proCount").val(),
countEl: $("#proCount"),
uPrice:$("#uintPrice").text(),
totalAmountEL:$("#retTotalAmount"),
add: function() {
		
		if(this.count >= this.max){
			alert("\u5546\u54c1\u6570\u91cf\u6700\u591a\u4e3a" + this.max);
			return false;
		}
		this.count++;
		this.countEl.val(this.count);
		
		this.totalAmountEL.text((this.count*this.uPrice).toFixed(2));
		
        return void 0;
},
reduce: function() {

		if(1 >= this.count){
			alert("\u5546\u54c1\u6570\u91cf\u6700\u5c11\u4e3a" + this.min);
			return false;
		}
		this.count--;
		this.countEl.val(this.count)
		this.totalAmountEL.text((this.count*this.uPrice).toFixed(2));
		return void 0; 
        
},
modify: function() {
       var t = parseInt(this.countEl.val(), 10);
		if(isNaN(t) || 1 > t || t > this.max){
			alert("\u5546\u54c1\u6570\u91cf\u5e94\u8be5\u5728" + this.min + "-" + this.max + "\u4e4b\u95f4");
			this.countEl.val(this.count);
			return false;
		}
		this.count = t;
		this.countEl.val(this.count);
		this.totalAmountEL.text((this.count*this.uPrice).toFixed(2));
		return void 0; 
		
}};


$(function(){

//是否给顾客退运费
$("#backTransportFeeYes").click(function(){

	var chk = $(this).attr("checked")
	if(chk){
		$("#backTransportFee").removeAttr("disabled").removeClass('disabled');
	}
});
$("#backTransportFeeNo").click(function(){

	var chk = $(this).attr("checked")
	if(chk){
		$("#backTransportFee").val(0).attr("disabled","true").addClass("textInput disabled");
	}
});

$("#ret_apply_agreeChange").click(function(check){

	var $form = $("#retorder_detail_form");
	$form.attr("action","/oms/service/agreeChange");
	$form.submit();
	check.preventDefault();
});

$("#ret_apply_agreeRecede").click(function(check){

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