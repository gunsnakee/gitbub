<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">查看退换货详情</h2>

<div class="pageContent">
<form action="/bkstage/menu/edit" method="post" class="pageForm required-validate"
      onsubmit="return validateCallback(this, navTabAjaxDone)">
<input type="hidden" value="1" name="handle">

<div class="pageFormContent" layoutH="97">
<fieldset>
    <legend>退换货信息</legend>
    <dl>
        <dt style="background:#bdd2ff">退换货编号：</dt>
        <dd style="background: beige">${ret.retordOrderId}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">类型：退货</dt>
        <dd style="background: beige"><c:if test="${fn:substring(ret.retType, 0, 3)=='RET'}">退货</c:if><c:if test="${fn:substring(ret.retType, 0, 3)=='CHG'}">换货</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">退换货状态:</dt>
        <dd style="background: beige">
            <c:forEach items="${retStatus}" var="rs">
                <c:if test="${rs.code==ret.retStatus}">${rs.desc}</c:if>
            </c:forEach>
        </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">申请时间：</dt>
        <dd style="background: beige"><fmt:formatDate value="${ret.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">创建类型：</dt>
        <dd style="background: beige"><c:if test="${ret.createType==0}">用户自建</c:if><c:if test="${ret.createType==1}">客服创建</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff"> 涉及订单：</dt>
        <dd style="background: beige">${ret.oldOrderId}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">支付方式：</dt>
        <dd style="background: beige"><c:if test="${ret.ordiList[0].orderType =='RCD'}">货到付款</c:if><c:if test="${ret.ordiList[0].orderType !='RCD'}">网上支付</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">是否开发票：</dt>
        <dd style="background: beige"><c:if test="${ord.isInvoice==1}">是</c:if><c:if test="${ord.isInvoice!=1}">否</c:if></dd>
    </dl>
</fieldset>
<br>
<br>
<hr>
<br>
<br>
<h2> 申请退换货的商品 </h2>
<table class="table" width="900">
    <thead>
    <tr>
        <th>已选商品</th>
        <th>商品编号</th>
        <th>商品条形码</th>
        <th>商品标题</th>
        <th>购买数量</th>
        <th>申请退换货数量</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${ret.ordiList}" var="oi">
        <tr>
            <td><input type="checkbox" <c:if test="${oi.state==1}">checked="checked"</c:if>> </td>
            <td>${oi.proId}</td>
            <td>${oi.proBarCode}</td>
            <td>${oi.proName}</td>
            <td>${oi.proCateId}</td>
            <td><input type="text" value="${oi.saleNum}"></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<br>
<br>
<hr>
<br>
<br>
<fieldset>
    <legend>用户退换货详情</legend>
    <dl>
        <dt style="background:#bdd2ff">服务类型：</dt>
        <dd style="background: beige"><c:if test="${ret.userRetType=='RET'}">退货</c:if><c:if test="${ret.userRetType=='CHG'}">换货</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">退款的付款方式：</dt>
        <dd style="background: beige"><c:if test="${ret.retPayType=='MLW_WALLET'}">美丽钱包</c:if><c:if test="${ret.retPayType=='THIRD_BANK'}">支付银行</c:if><c:if test="${ret.retPayType=='THIRD_ALIPAY'}">支付宝</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">银行名称:</dt>
        <dd style="background: beige"><input type="text" value="${ret.serviceRetBank}"></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">银行账户名：</dt>
        <dd style="background: beige"><input type="text" value="${ret.serviceRetCardName}"></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">银行卡号：</dt>
        <dd style="background: beige"><input type="text" value="${ret.serviceRetCardNum}"></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">开户行：</dt>
        <dd style="background: beige"><input type="text" value="${ret.serviceRetOpenBank}"></dd>
    </dl>
    <dl style="  height: auto;">
        <dt style="background:#bdd2ff">问题描述：</dt>
        <dd style="background: beige; height: auto">
            ${ret.applyComments}
        </dd>
    </dl>
    <dl style="width: auto; height: auto;">
        <dt style="background:#bdd2ff">上传凭证：</dt>
        <dd style="background: beige;width: auto; height: auto;">
            <c:forEach items="${pics}" var="pic">
                <img width="165" height="165" src="${pic}">
            </c:forEach>
    </dl>

    <dl>
        <dt style="background:#bdd2ff">联系人：</dt>
        <dd style="background: beige"><input type="text" value="${ret.adRecvName}"></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">地区：</dt>
        <dd style="background: beige">${ret.adProvince}${ret.adCity}${ret.adCounty}${ret.adTown}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">地址：</dt>
        <dd style="background: beige"><input type="text" value="${ret.adDetailAddr}"></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">手机：</dt>
        <dd style="background: beige"><input type="text" value="${ret.adMobile}"></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">电话：</dt>
        <dd style="background: beige"><input type="text" value="${ret.adPhone}"></dd>
    </dl>
</fieldset>
<br>
<br>
<hr>
<br>
<br>
<fieldset>
    <legend>退款详情</legend>
    <dl>
        <dt style="background:#bdd2ff">是否需要退款：</dt>
        <dd style="background: beige">是<input type="radio" name="retBack" <c:if test="${ret.retTotalAmount > 0}">checked="" </c:if>> 否 <input type="radio" name="retBack"  <c:if test="${ret.retTotalAmount <= 0}">checked="" </c:if>></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">商品退款金额：</dt>
        <dd style="background: beige"><input type="text" value="${ret.retPayAmount}">元</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">退运费金额:</dt>
        <dd style="background: beige"><input type="text" value="${ret.retPayFare}">元</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">补偿运费金额：</dt>
        <dd style="background: beige"><input type="text" value="${ret.retPayCompensate}">元</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">退款总金额：</dt>
        <dd style="background: beige">${ret.retTotalAmount}元</dd>
    </dl>
</fieldset>
<br>
<hr>
<br>
<fieldset>
    <legend>用户账号信息</legend>
    <dl>
        <dt style="background:#bdd2ff">用户ID：</dt>
        <dd style="background: beige">${user.userName}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">昵称：</dt>
        <dd style="background: beige">${user.nickName}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">邮箱:</dt>
        <dd style="background: beige">${user.email}<span style="color: red"><c:if test="${user.emailActive==1}">已</c:if><c:if test="${user.emailActive!=1}">未</c:if>验证</span>
        </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">手机号:</dt>
        <dd style="background: beige">${user.mobile}<span style="color: blue"><c:if test="${user.mobileActive==1}">已</c:if><c:if test="${user.mobileActive!=1}">未</c:if>验证</span>
        </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">创建类型：</dt>
        <dd style="background: beige"><c:if test="${ret.createType==0}">用户自建</c:if><c:if test="${ret.createType==1}">客服创建</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff"> 涉及订单：</dt>
        <dd style="background: beige">${ret.oldOrderId}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">支付方式：</dt>
        <dd style="background: beige"><c:if test="${ret.ordiList[0].orderType =='RCD'}">货到付款</c:if><c:if test="${ret.ordiList[0].orderType !='RCD'}">网上支付</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">是否开发票：</dt>
        <dd style="background: beige"><c:if test="${ord.isInvoice==1}">是</c:if><c:if test="${ord.isInvoice!=1}">否</c:if></dd>
    </dl>
</fieldset>

<br>
<hr>
<br>

<h2> 退换货处理流程记录 </h2>
<table class="table" width="900">
    <thead>
    <tr>
        <th>处理时间</th>
        <th>处理结果</th>
        <th>处理内容</th>
        <th>操作人</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="logs" items="${ret.retLogsList}">
        <tr>
            <td><fmt:formatDate value="${logs.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
            <td>${logs.optResult}</td>
            <td>${logs.optContent}</td>
            <td>${logs.optMan}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<br>
<hr>
<br>



</div>
<div class="formBar">
    <ul>
        <li>
            <div class="button">
                <div class="buttonContent">
                    <button class="close" type="button">关闭</button>
                </div>
            </div>
        </li>
        <li>
            <div class="button">
                <div class="buttonContent">
                    <button class="close" type="button">确定退款</button>
                </div>
            </div>
        </li>
        <li>
            <div class="button">
                <div class="buttonContent">
                    <button class="close" type="button">关闭</button>
                </div>
            </div>
        </li>
    </ul>
</div>
</form>
</div>