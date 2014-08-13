<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">基础配置项编辑</h2>

<div class="pageContent">
    <form action="/base/config/editSeo" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" value="6" name="handle">
        <input type="hidden" value="${config.sysConfigId}" name="sysConfigId">
        <input id="sysConfigCode" type="hidden" value="${config.sysConfigCode}" name="sysConfigCode">
    <div class="pageFormContent" layoutH="92">
    <fieldset>
        <legend>基础配置项编辑</legend>
        <dl>
            <dt>编码名称：</dt>
            <dd>${config.sysConfigName}</dd>
        </dl>
        <dl>
            <dt>编码：</dt>
            <dd>${config.sysConfigCode}</dd>
        </dl>
        <dl class="nowrap">
            <dt>编码值：</dt>
            <textarea name="sysConfigValue" class="required textInput" cols="80" rows="2" maxlength="2000">${config.sysConfigValue}</textarea>
        </dl>
        <dl class="nowrap">
            <dt>描述：</dt>
            <dd>
                ${config.descp}
            </dd>
        </dl>
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

