<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form id="trans_edit_company_from" method="post" action="/base/trans/update" class="pageForm required-validate" >
						
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>物流公司名称：</label>
				<input name="id" type="hidden" value="${bean.id}" />
				<input class="required" name="name" type="text" size="30" value="${bean.name}" />
			</p>
			
			<p>
				<label>支付形式：</label>
				<input type="checkbox" name="supportCash" value="1" <c:if test="${1== bean.supportCash}">checked</c:if>/>现金
				<input type="checkbox" name="supportPos" value="1" <c:if test="${1== bean.supportPos}">checked</c:if>/>POS机
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
$("#trans_edit_company_from").validate({
	submitHandler:function(form){
		
		var pmin = $("#trans_edit_company_from input[name='servicePriceMin']").val();
		var pmax = $("#trans_edit_company_from input[name='servicePriceMax']").val();
		
		if(pmin>=pmax){
			this.showErrors({
			  "servicePriceMax": "最大价格需大于最小价格"
			});
			return false;
		}
		
		return validateCallback(form, navTabAjaxDone);
	}    
}); 

</script>