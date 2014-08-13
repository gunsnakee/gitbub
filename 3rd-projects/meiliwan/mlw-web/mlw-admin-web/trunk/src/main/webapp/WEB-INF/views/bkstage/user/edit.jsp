<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">个人信息设置</h2>

<div class="pageContent">
    <form action="/bkstage/admin/edit" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
    <input type="hidden" value="1" name="handle">
        <input name="adminId" value="${obj.adminId}" type="hidden"/>
    <div class="pageFormContent" layoutH="97">
    <fieldset>
        <legend>个人信息设置</legend>
        <dl>
            <dt>后台人员登陆账号：</dt>
            <dd><input name="loginName"  readonly="readonly"   type="text" value="${obj.loginName}" /></dd>
        </dl>
        <dl>
            <dt>用户名称：</dt>
            <dd><input name="adminName" class="required" type="text" value="${obj.adminName}"  /></dd>
        </dl>
        <dl>
            <dt>电话：</dt>
            <dd><input   name="adminPhone" type="text"  value="${obj.adminPhone}" /></dd>
        </dl>
        <dl>
            <dt>部门：</dt>
            <dd><input name="adminDep" type="text" value="${obj.department}"  /></dd>
        </dl>
        <dl>
            <dt>邮件：</dt>
            <dd><input name="adminEmail" type="text"  value="${obj.adminEmail}" />
                <input name="state" type="hidden"  value="0" /></dd>
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