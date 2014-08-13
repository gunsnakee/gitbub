<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">新增促销推荐的商品</h2>

<div class="pageContent">
    <form action="/pms/promotion/add?handle=1" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="90">
        <br>
        <fieldset>
            <legend>新增促销推荐的商品</legend>
            <dl class="nowrap">
                <dt>商品ID：</dt>
                <dd><input type="text" class="required digits" name="proId"></dd>
            </dl>
            <dl class="nowrap">
                <dt>序号：</dt>
                <dd><input type="text" class="required digits" min="1" max="10" name="sequence"></dd>
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