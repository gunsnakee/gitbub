<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/card/card/freeze" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="cardId"  type="hidden" value="${cardId}"/>
            <input name="state"  type="hidden" value="${state}"/>
			<input name="handle"  type="hidden" value="1"/>
			<p>
				<label><c:if test="${state == 1}">冻结</c:if><c:if test="${state == 0}">解除冻结</c:if>礼品卡原因：</label>
                <textarea name="descp" class="required textInput" cols="50" rows="5"></textarea>
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