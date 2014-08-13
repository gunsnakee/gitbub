<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form id="ret_consultation" method="post" action="/oms/service/consultation"  class="pageForm required-validate" onsubmit="return consultationCallback(this);">
		
		<div class="pageFormContent" layoutH="56">
		<p>
			<label>选择协商方式：</label>
			<input name="handle" type="hidden" value="1"/>
			<input name="retordItemId" type="hidden" value="${retordItemId}"/>
		</p>
		<p>
			<input name="isback" type="checkbox" value="1"/>退款
			<input name="retMoney" type="text" size="10" maxlength="32" style="float: none;"/>元
		<p>
		<p>
			<label>说明：</label>
			<textarea name="comment" cols="80" rows="4" maxlength="200"></textarea>
		</p>
		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">

function consultationCallback(_form){

	
	validateCallback(_form,dialogAjaxDone);
	var retordItemId = $("#ret_consultation input[name=retordItemId]").val();
	var retMoney = $("#ret_consultation input[name=retMoney]").val();
	openwin(retordItemId,retMoney);
	return false;
	
}

function openwin(orderItemId,retMoney) { 
	window.open ("/oms/service/retpayForm?handle=1&retMoney="+retMoney+"&retordItemId="+orderItemId, "newwindow", "height=100, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
}


</script>