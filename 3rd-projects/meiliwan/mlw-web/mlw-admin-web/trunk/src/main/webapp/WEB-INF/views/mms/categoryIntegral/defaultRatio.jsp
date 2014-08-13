<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">默认积分比例设置</h2>

<div class="pageContent">
    <form id="form"  action="/mms/categoryIntegral/defaultRatio" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" value="1" name="handle">
        <input id="id" type="hidden" name="id" value="${rule.id}">
        <input id="categoryName" type="hidden" name="categoryName" value="默认积分比例">
        <input id="firstCategoryId" type="hidden" name="firstCategoryId"  value="0">
        <input id="secondCategoryId" type="hidden" name="secondCategoryId"  value="0">
        <input id="categoryId" type="hidden" name="categoryId"  value="0">
        <input id="createTime" type="hidden" name="createTime"  value="${rule.createTime}">
        <input id="updateTime" type="hidden" name="updateTime"  value="${rule.updateTime}">
        <input id="isDel" type="hidden" name="isDel"  value="-1">
        <input id="ruleType" type="hidden" name="ruleType"  value="1">
        <input id="state" type="hidden" name="state"  value="0">
    <div class="pageFormContent" layoutH="97">
    <fieldset>
        <legend>默认积分比例设置</legend>
        <dl>
            <dt>积分：</dt>
            <dd><input name="ratio" type="text" value="${rule.ratio}" class="required" min="1" max="1000" />%</dd>
        </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button id="save" type="submit">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>