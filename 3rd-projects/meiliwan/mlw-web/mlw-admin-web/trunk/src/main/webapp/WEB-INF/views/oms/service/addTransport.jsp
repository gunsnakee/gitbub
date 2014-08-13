a<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/oms/service/send" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			
			<p>
				<label>货运单号：</label>
				<input type="hidden" name="orderId" value="${ordi.orderId}">
                <input type="hidden" name="retordItemId" value="${ordi.orderItemId}">

				<input type="hidden" name="hander" value="1">
				<select name="company">
				<c:forEach var="o" items="${companys}">
					<option value="${o.code}">${o.desc}</option>
				</c:forEach>
				</select>
				<input name="transportInfo" type="text" maxlength="50">
			</p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
