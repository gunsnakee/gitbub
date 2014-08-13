<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<style Type="text/css">
<!--
  #orderDetail p{padding: 5px 0 0 0;}
-->
</style>


<div class="pageContent" layoutH="122" id="orderDetail">

<h2 class="contentTitle">订单详情</h2>
<p>订单编号：${ord.orderId}</p>
<p>订单状态：
<c:forEach var="e" items="${orderStateList}">
	<c:if test="${e.code == ord.orderStatus}">${e.desc}</c:if>
</c:forEach>
</p>
<p>支付方式：
<c:forEach var="e" items="${payList}">
	<c:forEach var="f" items="${payFormMap}">
		<c:if test="${f.code == e.payCode}">${f.desc}　</c:if>
	</c:forEach>
</c:forEach>
</p>


<h2 class="contentTitle">订单跟踪</h2>
<table class="table" width="100%">
		<thead>
			<tr>
				<th width="140">处理时间</th>
				<th>处理信息</th>
				<th width="100">操作人</th>
			</tr>
		</thead>
		<tbody>
		
			<c:forEach var="e" items="${logList}">
			<tr>
				<td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
				<td>${e.content}</td>
				<td>
					${e.opteratorName}
				</td>
			</tr>
			</c:forEach>
		</tbody>
</table>		


<h2 class="contentTitle">收货地址：</h2>
<p>
<span>省：${addr.province} </span>
<span>市：${addr.city} </span>
<span>地区：${addr.county} </span>
<span>街道：${addr.town} </span>
</p>
<p>收货人：${addr.recvName}</p>
<p>详细地址：${addr.detailAddr} </p>
<p>手机：${addr.mobile} </p>
<p>电话：${addr.phone} </p>
<p>邮编：${addr.zipCode} </p>

<h2 class="contentTitle">支付方式：</h2>
<c:forEach var="e" items="${payList}">
	<c:forEach var="f" items="${payFormMap}">
		<c:if test="${f.code == e.payCode}">${f.desc}　</c:if>
	</c:forEach>
</c:forEach>


<h2 class="contentTitle">发票信息：</h2>
	<c:if test="${ord.isInvoice==1}">
		<p>发票类型：
		<c:forEach var="e" items="${invoiceTypeList}">
			<c:if test="${e.code == invoidce.invoiceType}">${e.desc}</c:if>
		</c:forEach>
		</p>
		<p>发票抬头：${invoidce.invoiceHead}</p>
		<p>发票内容：${invoidce.invoiceContent}</p>
	</c:if>
	<c:if test="${ord.isInvoice!=1}">
		<p>不需要开发票</p>
	</c:if>
<h2 class="contentTitle">商品清单：</h2>
<table class="table" width="100%" id="ord_detail">
	<thead>
	<tr>
		<th align="center">商品编号</th>
		<th align="center">商品条形码</th>
		<th align="center">商品标题</th>
		<th align="center">单价</th>
		<th align="center">数量</th>
        <th align="center">储位</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach var="e" varStatus="i" items="${ordiList}">
	<tr>
		<td>${e.proId}</td>
		<td>${e.proBarCode}</td>
		<td height="50px">
		<div style="overflow:visible;height: 50px; ">
			<p style="padding: 0 0 7px 0;text-align: left; ">${e.proName}</p>
			<p style="padding: 0 0 7px 0;text-align: left; ">编号：${e.proId}
			购买方式：
			<c:forEach var="o" items="${buyType}">
				<c:if test="${o.code == e.buyType}">${o.desc}</c:if>
			</c:forEach>
			
			</p>
		</div>
		</td>
		<td>${e.uintPrice}
		</td>
		<td>${e.saleNum}</td>
        <td>${e.remark}</td><%--这里remark用于存放储位--%>
	</tr>
	</c:forEach>
		
	</tbody>
</table>
        
<h2 class="contentTitle">结算信息：</h2>
    <div style="line-height: 16px; background-color: #F8F6F2; margin-bottom: 13px; border-bottom: 5px solid #EFEAE5; border-left: 5px solid #EFEAE5; border-right: 5px solid #EFEAE5; position: relative; z-index: 40; padding: 8px 20px 30px;">
        <p style="padding: 10px 10px 0 0;text-align: right;">商品金额总计：
        	<span>¥</span>
        	<span><fmt:formatNumber type="number" value="${ord.orderSaleAmount}" minFractionDigits="2" maxFractionDigits="2"/></span>
        </p>
        <p style="padding: 10px 10px 0 0;text-align: right;">运费：
            <span>¥</span>
            <span><fmt:formatNumber type="number" value="${ord.transportFee} " minFractionDigits="2" maxFractionDigits="2"/></span>
        </p>
        <p style="padding: 10px 10px 0 0;text-align: right;">美丽钱包：
            <span>- ¥</span>
            <span><fmt:formatNumber type="number" value="${walletPay} " minFractionDigits="2" maxFractionDigits="2"/></span>
        </p>
        <%--<c:if test="${giftCardPay > 0}"></c:if>--%>
        <p style="padding: 10px 10px 0 0;text-align: right;">礼品卡：
            <span>- ¥</span>
            <span><fmt:formatNumber type="number" value="${giftCardPay} " minFractionDigits="2" maxFractionDigits="2"/></span>
        </p>
        
        <c:if test="${ord.favorableTotalAmount > 0}">
        	<p style="padding: 10px 10px 0 0;text-align: right;">美丽湾优惠：
	            <span>- ¥</span>
	            <span><fmt:formatNumber type="number" value="${ord.favorableTotalAmount} " minFractionDigits="2" maxFractionDigits="2"/></span>
	        </p>
        </c:if>
        
        <p style="padding: 10px 10px 0 0;text-align: right;">您需支付：
            <span>¥</span>
            <span><fmt:formatNumber type="number" value="${needToPay} " minFractionDigits="2" maxFractionDigits="2"/></span>
        </p>
        
        <c:if test="${ord.orderStatus == 10}">
	        <p style="padding: 10px 10px 0 0;text-align: right;">
	        	<a href="/oms/favourable/discount?orderId=${ord.orderId}&needToPay=${needToPay + ord.favorableTotalAmount}" target="dialog" style="color: #0000ff">修改订单金额</a>
	        </p>
        </c:if>
        
    </div>
    
    <c:if test="${not empty favlogList}">
	    <h2 class="contentTitle">修改价格记录：</h2>
		<table class="table" width="100%">
			<thead>
				<tr>
					<th width="140">处理时间</th>
					<th width="100">优惠方式</th>
					<th width="100">优惠数值</th>
					<th>原因</th>
					<th width="100">操作人</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${favlogList}">
				<tr>
					<td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
					<td>${e.favourableName}</td>
					<td>${e.favourableValue}<c:if test="${e.favourableId == 2}">元</c:if><c:if test="${e.favourableId == 1}">%</c:if></td>
					<td>${e.content}</td>
					<td>${e.opteratorName}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</div>

<script type="text/javascript">
$(function(){

//$("#ord_detail .grid .gridTbody td div").css("height","50px");

})
</script>
