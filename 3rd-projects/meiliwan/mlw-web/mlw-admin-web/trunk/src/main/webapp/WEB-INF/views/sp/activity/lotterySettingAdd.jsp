<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form  method="post" action="/sp/lottery/add" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)" >
		<div class="pageFormContent" layoutH="56">
			<input name="id" type="hidden" size="30" value="${setting.id}"/>
			<p>
				<label>奖项名称</label>
				<input class="required" minlength="1" maxlength="50" name="lotteryName" type="text" size="30" value="${setting.lotteryName}"/>
			</p>
			<p>
				<label>奖品名称</label>
				<input class="required" minlength="1" maxlength="200" name="lotteryProduct" type="text" size="30" value="${setting.lotteryProduct}"/>
			</p>
			<p>
				<label>奖品数量</label>
				<input class="required number" name="total" type="text" size="30" value="${setting.total}"/>
			</p>
			<p>
				<label>可能中奖数量</label>
				<input class="required" readonly="readonly" name="possibility" type="text" size="30" value="${setting.possibility}"/>
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


</script>