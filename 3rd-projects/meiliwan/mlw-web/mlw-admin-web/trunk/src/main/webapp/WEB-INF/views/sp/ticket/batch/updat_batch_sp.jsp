<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/ticket/batch/update-to-sp" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="batchId"  type="hidden" value="${batchId}"/>
			<input name="handle"  type="hidden" value="1"/>
            <p>
                <span style="color: #0000ff">关联活动为关联前台活动页，即支持该批次的优惠券的商品聚合页!</span>
            </p>
			<p>
				<label>活动链接：</label>
                <input name="actUrl" class="required textInput" type="text" value="${batch.actUrl}" style="width: 200px"></textarea>
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