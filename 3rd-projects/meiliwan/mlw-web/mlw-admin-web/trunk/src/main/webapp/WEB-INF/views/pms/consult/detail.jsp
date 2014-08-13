<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">咨询详情</h2>

<div class="pageContent">
    <form action="/pms/consult/reply?handle=1" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="97">
        <input name="id"  type="hidden" value="${entity.id}">
        <fieldset>
            <legend>咨询详情</legend>
            <dl>
                <dt>咨询商品：</dt>
                <dd><input type="text" value="${entity.proName}" disabled /></dd>
            </dl>
            <dl>
                <dt>咨询类型：</dt>
                <dd>
                    <c:choose>
                        <c:when test="${entity.consultType == 1}">
                            商品咨询
                        </c:when>
                        <c:when test="${entity.consultType == 2}">
                            物流及库存
                        </c:when>
                        <c:when test="${entity.consultType == 3}">
                            支付问题
                        </c:when>
                        <c:when test="${entity.consultType == 4}">
                            促销及赠品
                        </c:when>
                        <c:when test="${entity.consultType == 5}">
                            其他
                        </c:when>
                    </c:choose>
                </dd>
            </dl>
            <dl>
                <dt>咨询用户：</dt>
                <dd><input type="text" value="${entity.nickName}" disabled /></dd>
            </dl>
            <dl>
                <dt>咨询时间：</dt>
                <dd><input type="text" value="${entity.createTime}" disabled /></dd>
            </dl>
            <dl>
                <dt>咨询内容：</dt>
                <dd><textarea class="" cols="50" rows="4" disabled>${entity.content}</textarea></dd>
            </dl>
            <dl>
                <dt>回复内容：</dt>
                <dd><textarea  name="replyContent" cols="50" rows="4" <c:if test="${empty entity.replyContent}">class="required textInput focus"  minlength="5" maxlength="1000" </c:if> <c:if test="${!empty entity.replyContent}">disabled</c:if> ><c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if></textarea></dd>
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