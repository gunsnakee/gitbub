<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>


<div id="requestCount">
	<h2>今天订单: 截止<fmt:formatDate value="${vo.todayEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/></h2>
	<p>订单有效数:${today.effective}</p>
	<p>货到付款下单数:${today.createCod}</p>
	<p>在线支付下单数:${today.createPay}</p>
	<p>订单取消数:${today.cancel}</p>
	<p>支付完成数:${today.payFinish}</p>

	<h2>昨天订单: 截止<fmt:formatDate value="${vo.yesterdayEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/></h2>
    <p>订单有效数:${vo.yesterdayEndTimeCount.effective}</p>
    <p>货到付款下单数:${vo.yesterdayEndTimeCount.createCod}</p>
	<p>在线支付下单数:${vo.yesterdayEndTimeCount.createPay}</p>
	<p>订单取消数:${vo.yesterdayEndTimeCount.cancel}</p>
	<p>支付完成数:${vo.yesterdayEndTimeCount.payFinish}</p>

    <h2>昨天总订单:</h2>
    <p>订单有效数:${yesterday.effective}</p>
    <p>货到付款下单数:${yesterday.createCod}</p>
	<p>在线支付下单数:${yesterday.createPay}</p>
	<p>订单取消数:${yesterday.cancel}</p>
	<p>支付完成数:${yesterday.payFinish}</p>
</div>

