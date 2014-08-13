<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">

	<form method="post" action="/base/infoitem/add" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" name="handle" value="1">
        <div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>资讯类别名称：</label>
				<input type="text" name="itemName" minlength="1" class="required" maxlength="40"/>
			</div>
            <div class="unit">
                <label>英文名：</label>
                <input type="text" name="fileName" minlength="1" class="required lettersonly" maxlength="32"/>资讯英文名或简称<%--主要用于生成静态页的文件目录--%>
            </div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	
</div>

