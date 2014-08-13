<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">编辑修改产地</h2>

<div class="pageContent">
    <form action="/pms/place/update?handle=1" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="97">
        <input name="id"  type="hidden" value="${entity.placeId}">
        <fieldset>
        <legend>编辑修改产地</legend>
        <dl>
            <dt>产地名称：</dt>
            <dd><input name="placeName"  class="required"  type="text" value="${entity.placeName}" /></dd>
        </dl>
        <dl>
            <dt>产地英文名称：</dt>
            <dd><input name="enName" type="text" value="${entity.enName}" /></dd>
        </dl>
        <dl>
            <dt>扩展名称：</dt>
            <dd><input name="otherName" type="text" value="${entity.otherName}"/></dd>
        </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存修改</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>