<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/updateskuSelfProp" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${proId}"/>
		    <input name="handle"  type="hidden" value="1"/>
            <input name="cacheState"  type="hidden" value="1"/>
            <fieldset>
                <legend>商品自定义属性</legend>
                <c:if test="${!empty psplist}">
                    <c:forEach var="psp" items="${psplist}">
                        <dl>
                            <input name="selfPropId" type="hidden" value="${psp.id}">
                            <dt><input name="selfPropName" type="text" style="width: 110px" value="${psp.selfPropName}"></dt>
                            <dd><input name="selfPropValue" type="text" style="width: 150px" value="${psp.selfPropValue}"></dd>
                        </dl>
                    </c:forEach>
                </c:if>
                <div  id="update-sku-chideren"><a class="button" href="javascript:;"><span>添加自定义属性</span></a></div>
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

<script type="text/javascript">
$(document).ready(function () {
    $("#update-sku-chideren").click(function(){
        $(this).before('<dl><input name="selfPropId" type="hidden" value="0"><dt><input name="selfPropName" type="text" style="width: 110px"></dt> <dd><input name="selfPropValue" type="text" style="width: 150px"></dd></dl>');
    });
});
</script>
