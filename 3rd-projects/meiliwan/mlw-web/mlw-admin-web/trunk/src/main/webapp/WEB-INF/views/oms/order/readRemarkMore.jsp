<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/oms/order/addRemarkMore" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			
			<p style="height: auto;">
				<label>备注：</label>
				<input type="hidden" name="orderIds" value="${orderIds}">
				<input type="hidden" name="navTabId" value="${navTabId}">
				<textarea name="comment" cols="60" rows="5" maxlength="255"></textarea>
			</p>
		</div>
		<div class="formBar">
			<ul>
                <c:if test='${sessionScope.bkstage_user.menus["226"]!=null}'>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
                </c:if>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
