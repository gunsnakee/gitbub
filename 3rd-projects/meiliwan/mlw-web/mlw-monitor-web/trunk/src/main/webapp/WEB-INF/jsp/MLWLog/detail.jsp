<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div>
	<p>
		<label>ID：${bean.id}</label>
	</p><br>
	
	<pre>${bean.remark}
	${bean.info}</pre>
</div>
