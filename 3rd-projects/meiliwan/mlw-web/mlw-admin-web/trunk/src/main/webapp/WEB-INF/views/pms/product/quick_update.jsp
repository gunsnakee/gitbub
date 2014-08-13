<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/quickUpdate" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${proId}"/>
				<input name="handle"  type="hidden" value="1"/>
			<c:if test="${'price' == where}">
                <p>
                    <label>美丽价：</label>
                    <input name="mlwPrice" class="required number" type="text" size="20" min="0.01"  value="${bean.mlwPrice}"/>
                </p>
                <p>
                    <label>市场价：</label>
                    <input name="marketPrice" class="required number" type="text" size="20" min="0.01" value="${bean.marketPrice}"/>
                </p>
                <p>
                    <label>进货价：</label>
                    <input name="tradePrice" class="required number" type="text" size="20" min="0.01" value="${bean.tradePrice}"/>
                </p>
			</c:if>
			<c:if test="${'stock' == where}">
                <p style="text-align: center">
                    <label><select name="upType">
                        <option value="1" selected="selected">增加库存量</option>
                        <option value="0">减少库存量</option>
                    </select></label>
                    <input name="stock" class="required digits" type="text" size="15" min="0" value=""/>(原有商品可用库存(${sellStock}))
                </p>
			</c:if>
            <c:if test="${'safe' == where}">
                <p>
                    <label>安全库存：</label>
                    <input name="safeStock" class="required digits" type="text" size="20" min="0"  value="${safeStock}"/>
                </p>
            </c:if>
            <c:if test="${'sale' == where}">
                <p>
                    <label>实际销量：</label>
                    <input class="required digits" type="text" size="20" readonly="true" min="0" value="${action.realSaleNum}"/>
                </p>
                <p>
                    <label>显示销量：</label>
                    <input name="showSale" class="required digits" type="text" size="20" min="0"  value="${action.showSaleNum}"/><span>(可修改)</span>
                </p>
            </c:if>
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
