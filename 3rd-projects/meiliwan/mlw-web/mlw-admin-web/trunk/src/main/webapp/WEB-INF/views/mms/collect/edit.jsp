<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">收藏夹查看</h2>

<div class="pageContent">
    <form action="/base/config/edit" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <input type="hidden" value="${msg.id}" name="id">
    <div class="pageFormContent" layoutH="92">
    <fieldset>
        <legend></legend>
        <dl>
            <dt>用户：</dt>
            <dd>${collect.nickName}</dd>
        </dl>
        <dl>
            <dt>商品id：</dt>
            <dd>${collect.proId}</dd>
        </dl>
        <dl>
            <dt>商品：</dt>
            <dd>${collect.proName}</dd>
        </dl>
        <dl>
            <dt>收藏时间：</dt>
            <dd><fmt:formatDate value="${collect.createTime}"/></dd>
        </dl>
        <dl>${msg.content}</dl>

    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>

    </form>
</div>