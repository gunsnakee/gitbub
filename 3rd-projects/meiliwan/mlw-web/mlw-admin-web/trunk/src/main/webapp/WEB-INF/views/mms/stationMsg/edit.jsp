<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">站内信查看</h2>

<div class="pageContent">
    <form action="/base/config/edit" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <input type="hidden" value="${msg.id}" name="id">
    <div class="pageFormContent" layoutH="92">
    <fieldset>
        <legend>站内信查看</legend>
        <dl>
            <dt>发送人：</dt>
            <dd>${msg.adminName}</dd>
        </dl>
        <dl>
            <dt>接受人：</dt>
            <dd>${msg.nickName}</dd>
        </dl>
        <dl>
            <dt>发送时间：</dt>
            <dd><fmt:formatDate value="${msg.createTime}"/></dd>
        </dl>
        <dl>
            <dt>内容：</dt>
            <dd>
            </dd>
        </dl>
        <dl>${msg.content}</dl>

    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>

    </form>
</div>