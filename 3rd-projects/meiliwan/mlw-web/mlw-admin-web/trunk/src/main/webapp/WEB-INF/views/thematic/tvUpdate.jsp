<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <form action="/tv/update" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
        <input type="hidden" name="handle" value="1">
        <input type="hidden" name="id" value="${bean.id}">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <dl>
                    <dt>专题名称：</dt>
                    <dd><input name="pageName"  class="required" value="${bean.pageName}" maxlength="10" type="text"></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">取消</button></div></div></li>
            </ul>
        </div>
    </form>
</div>