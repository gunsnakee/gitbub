<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div>
	

	<form method="post" action="/player/addPlayer" class="pageForm required-validate"   onsubmit="return dialogAjaxDone(this)">
		
		姓名:<input name="name" value="">
		<p></p>
		手机:<input name="mobile" value="">
		<p></p>
		邮箱:<input name="email" value="">
		<button type="submit" onkeydown="if(13==event.keyCode){return false;}">保存</button>


	</form>

</div>
