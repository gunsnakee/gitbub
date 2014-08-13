<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">评价详情</h2>

<div class="pageContent">
    <form action="/pms/comment/reply?handle=1" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="97">
        <input name="id"  type="hidden" value="${entity.id}">
        <fieldset>
            <legend>评价详情</legend>
            <dl>
                <dt>评价商品：</dt>
                <dd><input type="text" value="${entity.proName}" disabled /></dd>
            </dl>
            <dl>
                <dt>评分：</dt>
                <dd><input type="text" value="${entity.score}" disabled /></dd>
            </dl>
            <dl>
                <dt>评价用户：</dt>
                <dd><input type="text" value="${entity.nickName}" disabled /></dd>
            </dl>
            <dl>
                <dt>订单号：</dt>
                <dd><input type="text" value="${entity.orderId}" disabled /></dd>
            </dl>
            <dl>
                <dt>有用次数：</dt>
                <dd><input type="text" value="${entity.usefulCount}" disabled /></dd>
            </dl>
            <dl>
                <dt>无用次数：</dt>
                <dd><input type="text" value="${entity.uselessCount}" disabled /></dd>
            </dl>
            <dl>
                <dt>评价时间：</dt>
                <dd><input type="text" value="${entity.commentTime}" disabled /></dd>
            </dl>
            <dl>
                <dt>评论内容：</dt>
                <dd><textarea class="" cols="50" rows="4" disabled>${entity.content}</textarea></dd>
            </dl>
            <dl>
                <dt>回复内容：</dt>
                <dd><textarea  name="replyContent" cols="50" rows="4" <c:if test="${empty entity.replyContent}">class="required textInput focus"  minlength="5" maxlength="400" </c:if> <c:if test="${!empty entity.replyContent}">disabled</c:if> ><c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if></textarea></dd>
            </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <c:if test="${empty entity.replyContent}"><li><div class="buttonActive"><div class="buttonContent"><button type="submit">确定回复</button></div></div></li></c:if>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>