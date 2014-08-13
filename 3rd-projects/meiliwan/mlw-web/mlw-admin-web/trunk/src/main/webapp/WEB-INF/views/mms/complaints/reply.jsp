<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">回复投诉</h2>

<div class="pageContent">
    <form action="/mms/complaints/reply?handle=1" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="90">
        <input name="id"  type="hidden" value="${entity.id}">
        <br>
        <fieldset>
            <legend>回复投诉</legend>
            <dl class="nowrap">
                <dt>投诉内容：</dt>
                <dd><textarea class="textarea4" readonly="true" cols="75" rows="3" >${entity.content}</textarea></dd>
            </dl>
            <dl class="nowrap">
                <dt>回复内容：</dt>
                <dd><textarea name="replyContent" class="required textInput focus" minlength="5" maxlength="500" cols="75" rows="3"></textarea></dd>
            </dl>
        </fieldset>
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