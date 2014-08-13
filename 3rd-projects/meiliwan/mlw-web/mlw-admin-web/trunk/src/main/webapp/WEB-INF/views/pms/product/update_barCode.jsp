<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/updateskuBarcode" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${proId}"/>
		    <input name="handle"  type="hidden" value="1"/>
            <input name="cacheState"  type="hidden" value="1"/>
            <fieldset>
                <legend>条形码</legend>

                <p>
                    <label>条形码：</label>
                    <input name="barCode" class="required digit" minlength="9" maxlength="13"   value="${barCode}"/>
                </p>
            </fieldset>
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

<script type="text/javascript"></script>
