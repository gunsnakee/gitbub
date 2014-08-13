<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/update-integral" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${proId}"/>
            <input name="optType"  type="hidden" value="${optType}"/>
			<input name="handle"  type="hidden" value="1"/>
            <input name="petId"  type="hidden" value="<c:if test="${'add' == optType}">0</c:if><c:if test="${'update' == optType}">${integral.id}</c:if>"/>
			<p>
				<label>商品额外积分：</label>
				<input name="intralValue" class="required" type="text" size="20"  value="<c:if test="${'add' == optType}">0</c:if><c:if test="${'update' == optType}">${integral.value}</c:if>"/>
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
<script type="text/javascript">



</script>