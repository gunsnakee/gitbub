<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<h2 class="contentTitle">站内信-编辑</h2>
<div class="pageContent">

    <form method="post" action="/mms/passport/messageSubmit" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <div class="pageFormContent nowrap" layoutH="97">

            <input type="hidden" name="uid" value="${pc.uid}" />
            <input type="hidden" name="nickName" value="${pc.nickName}" />
            <input type="hidden" name="sendMore" value="${sendMore}" />
            <div id="div_uids"></div>
            <c:if test="${sendMore !='true'}">
                <dl>
                    <dt>用户昵称： ${pc.nickName}</dt>
                </dl>
            </c:if>

            <dl>
                <dt>站内信内容：</dt>
                <dd>
                    <textarea   cols="60" rows="5" name="msgContent" class="required" maxlength="800"  alt="请填写站内信内容"/>
                </dd>
            </dl>

        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit" target="ajaxTodo">提交</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
            </ul>
        </div>
    </form>

</div>

<script>

    $(function(){
        $('#div_uids').empty();
        $('input:checked[name="ids"]').each(function(){
            $('<input type="hidden" name="uids" value="'+$(this).val()+'">').appendTo('#div_uids');
        });
    })

</script>

