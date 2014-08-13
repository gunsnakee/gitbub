<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <script src="http://www.meiliwan.com/js/jquery-1.8.3.min.js"></script>
</head>
<body>

	<c:if test="${result}">
            
        
	<p>
	用户编号：${uid}
	</p>
	<p>退款金额：${retPayAmount}
	</p>
	

	</c:if>

	<c:if test="${!result}">
	退款失败
	</c:if>
	<input id="payFormConform" type="button" value="确定">


<script>
    $("#payFormConform").click(function(){
		returnvalue();
	});
	

function returnvalue() {

	var fatherDocument = window.opener.document;
	//调回pay页面
	var form = fatherDocument.getElementById("ret_pay_form");
	if(!form){
		window.close();
	}
	window.opener.navTabSearch(form);
	window.close();
}
</script>
</body>
</html>