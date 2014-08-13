<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/ticket/batch/disable-batch" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="batchId"  type="hidden" value="${batchId}"/>
			<input name="handle"  type="hidden" value="1"/>
            <p>
                <span style="color: #0000ff">确认将优惠券停用，停用后将不能再恢复使用!</span>
            </p>
			<p>
				<label>请填写原因：</label>
                <textarea name="disableDescp" class="required textInput" cols="50" rows="5"></textarea>
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