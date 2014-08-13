<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/oms/favourable/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56" >
			<input name="ofId"  type="hidden" value="${ordFavourable.id}"/>
            <input name="handle"  type="hidden" value="1"/>
            <p>
                <label>修改类型：</label>
                <label><c:if test="${empty ordFavourable.name}">暂无</c:if><c:if test="${!empty ordFavourable.name}">${ordFavourable.name}</c:if></label>
            </p>
            <p>
                <label>折扣下限：</label>
                <label><input name="lower" style="width: 100px;" class="required" type="text"  value="${ordFavourable.lowerDiscount}"/>%</label>
            </p>
            <p>
                <label>折扣上限：</label>
                <label><input name="upper" style="width: 100px;" class="required" type="text"  value="${ordFavourable.upperDiscount}"/>%</label>
            </p>
            <p>
            	<span style="color: red">（折扣为2-3整数，3位只能是100。例如：0.5折填写05，七五折填写为75，八折填写为80）</span>
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
