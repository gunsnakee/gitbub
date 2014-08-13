<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/updateskuPrice" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${proId}"/>
		    <input name="handle"  type="hidden" value="1"/>
            <input name="cacheState"  type="hidden" value="1"/>
            <fieldset>
                <legend>商品价格</legend>
                <fieldset>
                    <dl>
                        <dt>美丽价：</dt>
                        <dd>
                            <input type="text" id="mlwPrice" size="25" name="mlwPrice" class="required number" value="${mlwPrice}" min="0.01"/> 元
                        </dd>
                    </dl>
                    <dl>
                        <dt>市场价：</dt>
                        <dd>
                            <input type="text" id="marketPrice" size="25" name="marketPrice" class="required number" value="${marketPrice}" min="0.01"/> 元
                        </dd>
                    </dl>
                    <dl>
                        <dt>进货价：</dt>
                        <dd>
                            <input type="text" id="tradePrice" size="25" name="tradePrice" class="required number" value="${tradePrice}" min="0.01"/>  元
                        </dd>
                    </dl>
                </fieldset>

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
