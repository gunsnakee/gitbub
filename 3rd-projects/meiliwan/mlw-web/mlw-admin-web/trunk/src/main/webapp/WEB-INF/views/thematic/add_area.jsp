<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <form method="post" action="/thematic/add-area" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" name="handle" value="1">
        <input type="hidden" name="pageId" value="${pageId}">
        <div class="pageFormContent" layoutH="58">
            <dl>
                <dt><input name="areaType"  class="required"  type="radio" value="0" checked="checked">商品区</dt>
                <dd><input name="areaType"  class="required"  type="radio" value="1">图片区</dd>
            </dl>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
            </ul>
        </div>
    </form>

</div>