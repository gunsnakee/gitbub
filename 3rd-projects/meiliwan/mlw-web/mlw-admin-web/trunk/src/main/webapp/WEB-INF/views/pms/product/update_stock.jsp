<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/updateskuStock" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${proId}"/>
		    <input name="handle"  type="hidden" value="1"/>
            <input name="cacheState"  type="hidden" value="1"/>
            <fieldset>
                <legend>库存</legend>
                <p style="text-align: center">
                    <label><select name="upType">
                        <option value="1" selected="selected">增加库存量</option>
                        <option value="0">减少库存量</option>
                    </select></label>
                    <input name="stock" class="digits" type="text" size="15" min="0" value=""/>(原有商品可用库存(${sellStock}))
                </p>
                <p>
                    <label>安全库存：</label>
                    <input name="safeStock" class="required digits" type="text" size="20" min="0"  value="${safeStock}"/>
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
