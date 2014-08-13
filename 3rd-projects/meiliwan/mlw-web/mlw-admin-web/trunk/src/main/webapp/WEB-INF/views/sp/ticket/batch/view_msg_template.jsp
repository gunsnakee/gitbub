<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<h2 class="contentTitle">编辑发送优惠券模板</h2>

<div class="pageFormContent" layoutH="60">

    <c:if test="${empty msgs}">
        <fieldset>
            <legend></legend>
            <dl class="nowrap">
                <dd><textarea name="textarea1" cols="80" rows="2" readonly="true">暂无相关消息模板</textarea></dd>
            </dl>
        </fieldset>
    </c:if>
    <c:if test="${!empty msgs}">
        <c:forEach var="e" items="${msgs}">
            <fieldset>
                <legend><c:if test="${e.contentType == 1}">手机发送短信模板</c:if><c:if test="${e.contentType == 2}">邮箱发送短信模板</c:if><c:if test="${e.contentType == 3}">用户账户站内信模板</c:if>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a target="dialog" width="600" title="修改<c:if test="${e.contentType == 1}">手机发送短信模板</c:if><c:if test="${e.contentType == 2}">邮箱发送短信模板</c:if><c:if test="${e.contentType == 3}">用户账户站内信模板</c:if>" href="/ticket/batch/index?dtType=1&handle=1&tmpId=${e.tmpId}"><span style="color:#0000ff;background-color: #ffffff">修改模板</span></a></legend>
                <dl class="nowrap">
                    <br>
                    <dd><textarea name="textarea1" cols="80" rows="4" readonly="true">${e.content}</textarea></dd>
                </dl><br>
            </fieldset>

        </c:forEach>
    </c:if>
</div>
