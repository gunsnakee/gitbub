<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <form id="addForm" action="/antispam/keyword/add" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" value="1" name="handle">
    <div class="pageFormContent" layoutH="97">
	    <fieldset>
	        <dl>
	            <dt>敏感词：</dt>
	            <dd><input class="required" name="word" type="text" /></dd>
	        </dl>
	    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit" >提交</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">取消</button></div></div></li>
        </ul>
    </div>
    </form>
</div>