<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">设置密码</h2>

<div class="pageContent">
    <form action="/bkstage/admin/resetFinancePwd" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <input type="hidden" value="${opt}" name="opt">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>设置密码</legend>
                <c:if test='${"old" == opt}'>
                    <dl>
                    <dt>输入原密码：</dt>
                    <dd>
                        <input id="oldPwd"  class="required" name="oldPwd"  type="password" >
                        <span class="info">${msg}</span>
                    </dd>
                </dl>
                </c:if>
                <dl>
                    <dt>设置新密码：</dt>
                    <dd>
                        <input id="financePwd"  class="required alphanumeric mlwpwd" name="financePwd"  type="password" >
                    </dd>
                </dl>
                <dl>
                    <dt>确认新密码：</dt>
                    <dd>
                        <input id="confirmPwd" name="confirmPwd" class="required mlwpwd" equalto="#financePwd"  type="password" >
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