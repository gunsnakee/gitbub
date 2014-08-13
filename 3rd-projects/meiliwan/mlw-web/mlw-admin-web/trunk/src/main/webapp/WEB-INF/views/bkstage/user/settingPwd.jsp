<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">设置密码</h2>

<div class="pageContent">
    <form action="/bkstage/user/settingPwd" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>设置密码</legend>
                <dl>
                    <dt>登陆账号：</dt>
                    <dd><input name="loginName"  readonly="readonly"   type="text" value="${obj.loginName}" /></dd>
                </dl>
                <dl>
                    <dt>登录密码：</dt>
                    <dd>
                        <input id="loginPwd"  class="required" name="oldPassword"  type="password" >
                    </dd>
                </dl>
                <dl>
                    <dt>设置新密码：</dt>
                    <dd>
                        <input id="adminRestPwd"  class="required alphanumeric mlwpwd" name="password"  type="password" >
                    </dd>
                </dl>
                <dl>
                    <dt>确认新密码：</dt>
                    <dd>
                        <input id="adminRestPwdRep" name="passwordRep" class="required mlwpwd" equalto="#adminRestPwd"  type="password" >
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