<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="/WEB-INF/tld/mlw-web-form.tld" prefix="mlw" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <script src="/js/jquery-1.7.2.js" type="text/javascript"></script>
    <script src="/js/jquery.cookie.js" type="text/javascript"></script>
    <script src="/js/jquery.validate.js" type="text/javascript"></script>
</head>
<body>

<form id="ret_pay_form" action="/oms/service/retpayForm" method="post">

	退款金额：<input type="text" name="totalAmount" value="" class="required number" max="100000"> 
	<input type="hidden" name="payCode" value="MLW_ACCOUNT_WALLET">
	<input type="hidden" name="retordItemId" value="${retordItemId}">
	<input type="hidden" name="handler" value="1">
	<input type="submit" name="refund" value="退款">
</form>

<script>



jQuery.extend(jQuery.validator.messages, {
  required: "必选字段",
  remote: "请修正该字段",
  email: "请输入正确格式的电子邮件",
  url: "请输入合法的网址",
  date: "请输入合法的日期",
  dateISO: "请输入合法的日期 (ISO).",
  number: "请输入合法的数字",
  digits: "只能输入整数",
  creditcard: "请输入合法的信用卡号",
  equalTo: "请再次输入相同的值",
  accept: "请输入拥有合法后缀名的字符串",
  maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
  minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
  rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
  range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
  max: jQuery.validator.format("请输入一个最大为{0} 的值"),
  min: jQuery.validator.format("请输入一个最小为{0} 的值")
});



$("#ret_pay_form").validate({
	submitHandler:function(form){
		var amount = $("input[name=totalAmount]").val();

		if(amount>0){
			form.submit();
			return true;
		}
		alert("退款金额需要大于0");
		return false;
	}
});
</script>
</body>
</html>