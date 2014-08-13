<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/ticket/batch/index" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="tmpId"  type="hidden" value="${template.tmpId}"/>
            <input name="dtType"  type="hidden" value="1"/>
			<input name="handle"  type="hidden" value="2"/>
            <fieldset>
                <legend><c:if test="${template.contentType == 1}">手机发送短信模板</c:if><c:if test="${template.contentType == 2}">邮箱发送短信模板</c:if><c:if test="${template.contentType == 3}">用户账户站内信模板</c:if></legend>
                <dl class="nowrap">
                     <dd><span>提示：<span style="color: red">含有{xx}表示占位符,{pwd}表示激活码,{tkNo}表示券号,{date}表示截止日期,{type}表示券类型,{price}表示券面值</span></span></dd>
                </dl>
                <dl class="nowrap">
                    <dd><textarea name="msgContent" cols="70" rows="4">${template.content}</textarea></dd>
                </dl>
            </fieldset>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button id="sendCard" type="submit">保  存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取  消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
