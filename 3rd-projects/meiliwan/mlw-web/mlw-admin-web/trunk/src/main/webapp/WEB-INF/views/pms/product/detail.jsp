<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<link rel="stylesheet" type="text/css" href="/css/upload.css">

<div class="pageFormContent nowrap" layoutH="15" style="left:15px; ">
    <fieldset  >
        <legend >公共信息</legend>
        <dl>
            <dt>&nbsp;&nbsp;SPU ID：</dt>
            <dd>
                ${product.spuId}
            </dd>
        </dl>
        <dl>
            <dt>&nbsp;&nbsp;类目：</dt>
            <dd>
                ${firstCategory}< ${secondCategory} < ${thirdCategory}
            </dd>
        </dl>
        <dl>
            <dt>&nbsp;&nbsp;商品标题：</dt>
            <dd>
                ${product.proName}<span> </span> ${product.skuName}
            </dd>
        </dl>
        <c:if test='${product.state ==1 || product.state ==2 }'>
            <dl>
                <dt><%--<a herf="XXX" style="color: blue;">&nbsp;去修改</a></dt>--%>
                <dd>
                </dd>
            </dl>
        </c:if>

    </fieldset>
    <fieldset>
        <legend>规格信息</legend>
        <c:forEach var="skuProp" varStatus="i" items="${skuPropList}">
            <dl>
                <dt>${skuProp.name}：</dt>
                <dd>${skuProp.descp}</dd>
            </dl>
        </c:forEach>

    </fieldset>

    <fieldset>
        <legend>条形码</legend>
        <dl>
            <dt>&nbsp;&nbsp;条形码：</dt>
            <dd>
                ${product.barCode}
            </dd>
        </dl>
        <c:if test='${product.state ==1 || product.state ==2 }'>
            <c:if test='${sessionScope.bkstage_user.menus["fbc6dbcdd095a53dd83528a36752c053"]!=null}'>
                <div><a class="button" target="dialog"  href="/pms/product/updateskuBarcode?proId=${product.proId}"><span>修改</span></a>
                </div>
            </c:if>
        </c:if>

    </fieldset>

    <fieldset>
        <legend>商品价格</legend>
        <dl>
            <dt>&nbsp;&nbsp;美丽价：</dt>
            <dd>
                ${product.mlwPrice} 元
            </dd>
        </dl>
        <dl>
            <dt>&nbsp;&nbsp;市场价：</dt>
            <dd>
                ${product.marketPrice} 元
            </dd>
        </dl>
        <dl>
            <dt>&nbsp;&nbsp;进货价：</dt>
            <dd>
                ${product.tradePrice} 元
            </dd>
        </dl>
        <c:if test='${product.state ==1 || product.state ==2 }'>
            <c:if test='${sessionScope.bkstage_user.menus["7f625eea7d2b4726bf1dfbb5cd5db2f7"]!=null}'>
                <div id="id"><a class="button" target="dialog"  href="/pms/product/updateskuPrice?proId=${product.proId}"><span>修改</span></a></div>
            </c:if>
        </c:if>

    </fieldset>

    <fieldset>
        <legend>库存</legend>
        <dl>
            <dt>&nbsp;&nbsp;库存：</dt>
            <dd>
                ${stock.sellStock}
            </dd>
        </dl>
        <dl>
            <dt>&nbsp;&nbsp;安全库存：</dt>
            <dd>
                ${stock.safeStock}
            </dd>
        </dl>
        <c:if test='${product.state ==1 || product.state ==2 }'>
            <c:if test='${sessionScope.bkstage_user.menus["fbc9b39d16c51510f067b1f36e8f1b5c"]!=null}'>
                <div id="id"><a class="button" target="dialog"  href="/pms/product/updateskuStock?proId=${product.proId}"><span>修改</span></a></div>
            </c:if>
        </c:if>

    </fieldset>

    <fieldset>
        <legend>预售相关</legend>
        <dl>
            <dt>&nbsp;&nbsp;预售状态：</dt>
            <dd>
                <span style="color: red"><c:if test="${preSale == 0}">现货</c:if><c:if test="${preSale == 1}">预售</c:if></span>
            </dd>
        </dl>
        <dl>
            <dt>&nbsp;&nbsp;预售结束时间：</dt>
            <dd>
                <c:if test="${empty product.presaleEndTime}">暂无</c:if><c:if test="${!empty product.presaleEndTime}"><fmt:formatDate value="${product.presaleEndTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></c:if>
            </dd>
        </dl>
        <dl>
            <dt>&nbsp;&nbsp;预计发货时间：</dt>
            <dd>
                <c:if test="${empty product.presaleSendTime}">暂无</c:if><c:if test="${!empty product.presaleSendTime}"><fmt:formatDate value="${product.presaleSendTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></c:if>
            </dd>
        </dl>
        <c:if test='${product.state ==1 || product.state ==2 }'>
            <c:if test='${sessionScope.bkstage_user.menus["69276ac16c2eac6c750ecf29660aa30e"]!=null}'>
                <div id="id"><a class="button" target="dialog"  href="/pms/product/updateskuPresale?proId=${product.proId}"><span>修改</span></a></div>
            </c:if>
        </c:if>

    </fieldset>

    <fieldset>
        <legend>自定义SKU参数</legend>
        <c:forEach var="sProp" varStatus="i" items="${product.skuSelfProps}">
            <dl>
                <dt>${sProp.selfPropName}</dt>
                <dd>${sProp.selfPropValue}</dd>
            </dl>
        </c:forEach>
        <c:if test='${product.state ==1 || product.state ==2 }'>
            <c:if test='${sessionScope.bkstage_user.menus["c6733605afad72312d66fe828c55fdc4"]!=null}'>
                <div id="id"><a class="button" target="dialog"  href="/pms/product/updateskuSelfProp?proId=${product.proId}">
                    <span>修改</span></a></div>
            </c:if>
        </c:if>
    </fieldset>

    <fieldset>
        <legend>地方频道</legend>
        <dl>
            <dt>&nbsp;&nbsp;是否加入地方频道：<c:if test='${product.isFalls==0}'>否</c:if>
                <c:if test='${product.isFalls==1}'>是</c:if></dt>
            <dd>
                <div class="uploadify-progress" >
                    <img class="imgCube" width="" height="${product.height}"
                         src="${product.fallsImageUri}"></div>
            </dd>
        </dl>
        <c:if test='${product.state ==1 || product.state ==2 }'>
            <c:if test='${sessionScope.bkstage_user.menus["1635eb2b730d9bffa751cbca73e5df31"]!=null}'>
                <div id="id"><a class="button"  target="dialog"  href="/pms/product/updatesku-nationimage?proId=${product.proId}">
                    <span>修改</span></a></div>
            </c:if>
        </c:if>
    </fieldset>


</div>
<script type="text/javascript">
    $(document).ready(function () {

    });
</script>
