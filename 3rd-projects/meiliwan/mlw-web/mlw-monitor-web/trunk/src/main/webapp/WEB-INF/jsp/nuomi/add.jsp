<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div>
	
	<form method="post" action="/nuomi/add" class="pageForm required-validate"   onsubmit="return dialogAjaxDone(this)">
		
		查询key:<input name="name" value="">
		<p></p>
		条形码:<input name="value" value="">
		<select name="type">
			<option value="nuomi">糯米</option>
			<option value="wowotuan">窝窝团</option>
		</select>
		<button type="submit" onkeydown="if(13==event.keyCode){return false;}">保存</button>

 
	</form>

</div>
