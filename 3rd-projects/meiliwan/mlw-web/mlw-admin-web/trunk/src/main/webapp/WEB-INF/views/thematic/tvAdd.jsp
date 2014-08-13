<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <form action="/tv/add" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" name="handle" value="1">
        <div class="pageFormContent" layoutH="58">
            <fieldset>
                <dl>
                    <dt>专题名称：</dt>
                    <dd><input name="pageName"  class="required"  type="text" minlength="1" maxlength="10"></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">添加</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
            </ul>
        </div>
    </form>
</div>