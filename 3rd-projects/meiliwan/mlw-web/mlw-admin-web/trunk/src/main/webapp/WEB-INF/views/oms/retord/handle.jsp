<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<style type="text/css">
.waikuang900 {
    width: 900px;
    clear: both;
}
.waikuang600 {
    width: 600px;
    clear: both;
}
</style>
<h2 class="contentTitle">查看退换货详情</h2>

<div class="pageContent">
<form action="${handleUrl}" method="post" class="pageForm required-validate"
      onsubmit="return validateCallback(this, navTabAjaxDone)">
<input type="hidden" value="1" name="handle">
<input type="hidden" value="${ret.retordOrderId}" name="retordOrderId">
<input type="hidden" value="" id="ordi_list_str" name="userSelect">
<input id="retOptUserId"  name="optUserId"  type="hidden" value="${adminObj.adminId}">

<div class="pageFormContent" layoutH="97">
<fieldset style="float: left;">
    <legend>退换货信息</legend>
    <dl>
        <dt style="background:#bdd2ff">退换货编号：</dt>
        <dd style="background: beige">${ret.retordOrderId}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">退换货类型:</dt>
        <dd style="background: beige"><c:if test="${ret.retType=='RET'}">退货</c:if><c:if test="${ret.retType=='CHG' }">换货</c:if></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">退换货状态:</dt>
        <dd style="background: beige">
            <c:forEach items="${retStatus}" var="rs">
                <c:if test="${rs.code==ret.retStatus}">${rs.desc}</c:if>
            </c:forEach>
        </dd>
    </dl>
    <div class="button" style="float: left;">
        <div class="buttonContent">
            <a href="/oms/printer/viewer?order_id=${ret.retordOrderId}&type=RET_STOCK_WAITCHECK" target="_blank">&nbsp;打印&nbsp;</a>
        </div>
    </div>
    <div style="clear: both;"></div>
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
<div style="clear: both;"></div>
<br>
<br>
<hr>
<br>
<br>
<h2> 申请退换货的商品 </h2>
<table class="table" width="100%">
    <thead>
    <tr>
        <c:if test="${isEditMode ==1}"> <th>已选商品</th>     </c:if>
        <th>商品编号</th>
        <th>商品条形码</th>
        <th>商品标题</th>
        <th>商品购买价</th>
        <th>购买数量</th>
        <th>使用礼品卡支付占比金额(元)</th>
        <th>使用钱包支付占比金额(元)</th>
        <th>申请退换货数量</th>
    </tr>
    </thead>
    <tbody id="J-retordProduct">
    <c:forEach items="${ret.ordiList}" var="oi" varStatus="os">
        <c:if test="${isEditMode ==0 && oi.state==1 && oi.saleNum>0}">
            <tr class="ordi_tr">
                <td>${oi.proId}</td>
                <td>${oi.proBarCode}</td>
                <td>${oi.proName}</td>
                <td>${oi.uintPrice}</td>
                <td>${oi.proCateId}</td>
                <td><c:if test="${cardPayAmount>0}"><fmt:formatNumber type="number" value="${(oi.uintPrice*oi.proCateId)/ord.realPayAmount*cardPayAmount}" maxFractionDigits="2"></fmt:formatNumber></c:if><c:if test="${empty cardPayAmount||cardPayAmount<=0}">0</c:if></td>
                <td><c:if test="${mlwPayAmount>0}"><fmt:formatNumber type="number" value="${(oi.uintPrice*oi.proCateId)/ord.realPayAmount*mlwPayAmount}" maxFractionDigits="2"></fmt:formatNumber></c:if><c:if test="${empty mlwPayAmount||mlwPayAmount<=0}">0</c:if></td>
                <td>${oi.saleNum}</td>
            </tr>
        </c:if>
        <c:if test="${isEditMode ==1}">
            <tr class="ordi_tr">
                <td><input type="checkbox" value="${oi.orderItemId}" class="checkOrdi" id="input_${os.index}" <c:if test="${oi.state==1}">checked="checked"</c:if>></td>
                <td>${oi.proId}</td>
                <td>${oi.proBarCode}</td>
                <td>${oi.proName}</td>
                <td id="price_${os.index}" class="thisPrice">${oi.uintPrice}</td>
                <td>${oi.proCateId}</td>
                <td class="thisPercent"><c:if test="${cardPayAmount>0}"><fmt:formatNumber type="number" value="${(oi.uintPrice*oi.proCateId)/ord.realPayAmount*cardPayAmount}" maxFractionDigits="2"></fmt:formatNumber></c:if><c:if test="${empty cardPayAmount||cardPayAmount<=0}">0</c:if></td>
                <td class="thisPercent"><c:if test="${mlwPayAmount>0}"><fmt:formatNumber type="number" value="${(oi.uintPrice*oi.proCateId)/ord.realPayAmount*mlwPayAmount}" maxFractionDigits="2"></fmt:formatNumber></c:if><c:if test="${empty mlwPayAmount||mlwPayAmount<=0}">0</c:if></td>
                <td><input id="ordi_num_${os.index}" buy-num="${oi.proCateId}" type="text" class="digits ordi_num" value="${oi.saleNum}" min="0" max="${oi.proCateId}"></td>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>
<br>
<br>
<hr>
<br>
<br>
<fieldset class="waikuang600">
    <legend>用户退换货详情</legend>
    <dl>
        <dt style="background:#bdd2ff">服务类型：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==0}"><c:if test="${ret.retType=='RET'}">退货</c:if><c:if test="${ret.retType=='CHG'  }">换货</c:if></c:if>
            <c:if test="${isEditMode ==1}">
                <input type="radio" name="retType" value="RET" <c:if test="${ret.retType=='RET'}"> checked="checked" </c:if> >退货
                <input type="radio" name="retType" value="CHG" <c:if test="${ret.retType=='CHG'}"> checked="checked" </c:if>  >换货
            </c:if>
        </dd>
    </dl>
    <%--
    <dl>
        <dt style="background:#bdd2ff;">退款的付款方式：</dt>
        <dd style="background: beige; width: 250px;" id="J-select-wallet">
            <c:if test="${isEditMode ==0}">
                <c:if test="${ret.retPayType=='MLW_WALLET'}">美丽钱包</c:if>
                <c:if test="${ret.retPayType=='THIRD_ALIPAY'}">支付宝</c:if>
                <c:if test="${ret.retPayType=='THIRD_BANK'}">银行</c:if>
            </c:if>
            <c:if test="${isEditMode ==1}">
                <input type="radio" name="retPayType" value="MLW_WALLET" <c:if test="${ret.retPayType=='MLW_WALLET'}"> checked="checked" </c:if> >美丽钱包
                <input type="radio" name="retPayType" value="THIRD_ALIPAY" <c:if test="${ret.retPayType=='THIRD_ALIPAY'}"> checked="checked" </c:if>  >支付宝
                <input type="radio" name="retPayType" value="THIRD_BANK" <c:if test="${ret.retPayType=='THIRD_BANK'}"> checked="checked" </c:if>  >银行
            </c:if>
        </dd>
    </dl>
    <dl class="ret-info-alipay" <c:if test="${ret.retPayType!='THIRD_ALIPAY'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">支付宝账户:</dt>
        <dd style="background: beige"><c:if test="${isEditMode ==1}"><input type="text" name="serviceRetAlipay" value="${ret.serviceRetAlipay}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetAlipay}</c:if></dd>
    </dl>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">银行名称:</dt>
        <dd style="background: beige"><c:if test="${isEditMode ==1}"><input type="text" name="serviceRetBank" value="${ret.serviceRetBank}"></c:if>
                                      <c:if test="${isEditMode ==0}">${ret.serviceRetBank}</c:if></dd>
    </dl>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">银行账户名：</dt>
        <dd style="background: beige"><c:if test="${isEditMode ==1}"><input type="text" name="serviceRetCardName" value="${ret.serviceRetCardName}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetCardName}</c:if></dd>
    </dl>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">银行卡号：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="serviceRetCardNum" value="${ret.serviceRetCardNum}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetCardNum}</c:if>
        </dd>
    </dl>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">开户行：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="serviceRetOpenBank" value="${ret.serviceRetOpenBank}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetOpenBank}</c:if>
        </dd>
    </dl>
    --%>
    <dl style="  height: auto;">
        <dt style="background:#bdd2ff">问题描述：</dt>
        <dd style="background: beige; height: auto; word-break: break-all; word-wrap: break-word;">
            ${ret.applyComments}
        </dd>
    </dl>
</fieldset>
    <dl style="width: auto; height: auto;">
        <dt style="background:#bdd2ff">上传凭证：</dt>
        <dd style="background: beige;width: auto; height: auto;">
            <c:forEach items="${pics}" var="pic">
               <a target="_blank" href="${pic}">点击查看图片 </a><br>
            </c:forEach>
    </dl>
<fieldset class="waikuang600">
    <dl >
        <dt style="background:#bdd2ff">联系人：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="adRecvName" value="${ret.adRecvName}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.adRecvName}</c:if>
        </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">地区：</dt>
        <dd style="background: beige">${ret.adProvince}${ret.adCity}${ret.adCounty}${ret.adTown}</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">地址：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="adDetailAddr" value="${ret.adDetailAddr}"></c:if>
            <c:if test="${isEditMode ==0}"><input type="text" title="${ret.adDetailAddr}" value="${ret.adDetailAddr}" readonly></c:if>
        </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">手机：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="adMobile" value="${ret.adMobile}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.adMobile}</c:if> </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">电话：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input name="adPhone" type="text" value="${ret.adPhone}"></c:if>
            <c:if test="${isEditMode ==0}"> ${ret.adPhone}</c:if>
        </dd>
    </dl>
</fieldset>
<br>
<br>
<hr>

<fieldset class="waikuang600">
    <legend>原订单结算详情</legend>
    <dl>
        <dt style="background:#bdd2ff">商品金额总计：</dt>
        <dd style="background: beige"><span>${ord.orderSaleAmount}</span>元</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">运费：</dt>
        <dd style="background: beige"><span>${ord.transportFee}</span>元</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">订单总计：</dt>
        <dd style="background: beige"><span id="updOrderRealAmount">${ord.realPayAmount}</span>元</dd>
    </dl>
</fieldset>
<br>
<fieldset class="waikuang600">
    <legend>原订单支付详情</legend>
    <dl style="width:480px;">
    	<%--
        <dt style="background:#bdd2ff;<c:if test='${fn:length(thirdPayDesc) > 3}'>width:170px</c:if>">
        	使用<span id="autoPayType">
        			<c:if test="${!empty thirdPayDesc}">[${thirdPayDesc}]</c:if>
        			<c:if test="${empty thirdPayDesc}">第三方</c:if>
        		</span>支付：
        	
		</dt>
		--%>
		<dt style="background:#bdd2ff;">
			使用第三方支付：
		</dt>
        <dd style="background: beige">
        	<span>
        		<c:if test="${!empty thirdPayAmount}">${thirdPayAmount}</c:if>
        		<c:if test="${empty thirdPayAmount}">0.0</c:if>
        	</span>元<c:if test="${!empty thirdPayDesc}">&nbsp;&nbsp;[${thirdPayDesc}]</c:if>
        </dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">使用货到付款支付：</dt>
        <dd style="background: beige"><span><c:if test="${!empty isCOD}">${ord.realPayAmount}</c:if><c:if test="${empty isCOD}">0.0</c:if></span>元</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">使用美丽钱包支付：</dt>
        <dd style="background: beige"><span id="updWalletPayAmount"><c:if test="${!empty mlwPayAmount}">${mlwPayAmount}</c:if><c:if test="${empty mlwPayAmount}">0.0</c:if></span>元</dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">使用礼品卡支付：</dt>
        <dd style="background: beige"><span id="updCardPayAmount"><c:if test="${!empty cardPayAmount}">${cardPayAmount}</c:if><c:if test="${empty cardPayAmount}">0.0</c:if></span>元</dd>
    </dl>
</fieldset>

<br>
<br>
<fieldset class="waikuang900">
    <legend>退款详情<c:if test="${isEditMode ==1}">(如需退款，请直接输入退款金额，否则将金额全部置为零)</c:if></legend>

    <dl style="display: none;">
        <dt style="background:#bdd2ff">是否需要退款：</dt>
        <dd style="background: beige">是<input type="radio" disabled="disabled" <c:if test="${ret.retTotalAmount > 0}">checked="" </c:if>> 否 <input  disabled="disabled"  type="radio" name="retBack"  <c:if test="${ret.retTotalAmount <= 0}">checked="" </c:if>></dd>
    </dl>
    <dl>
        <dt style="background:#bdd2ff">商品退款金额：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text"  id="retPayAmount"   name="retPayAmount" class="retPayPrice number required" value="${ret.retPayAmount}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.retPayAmount} </c:if>
            元</dd>
    </dl>
    <c:if test="${isEditMode ==1}">
    <span style="color: #0000ff;display: inline-block;height: 30px;line-height: 30px;">申请退款的商品购买总金额：<span id="retProductAmount">${iniRetProdAmount}</span>元</span>
    </c:if>
    <div style="clear: both;"></div>
    <dl>
        <dt style="background:#bdd2ff">退运费金额:</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="retPayFare" id="retPayFare"  class="number retPayPrice required" value="${ret.retPayFare}"></c:if>
            <c:if test="${isEditMode ==0}"> ${ret.retPayFare}  </c:if>
            元</dd>
    </dl>
    <c:if test="${isEditMode ==1}">
    <span style="color: #0000ff;display: inline-block;height: 30px;line-height: 30px;">订单运费：${ord.transportFee}元</span>
    </c:if>
    <div style="clear: both;"></div>
    <dl>
        <dt style="background:#bdd2ff">补偿运费金额：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" id="retPayCompensate" value="${ret.retPayCompensate}" name="retPayCompensate" class="number retPayPrice required"></c:if>
            <c:if test="${isEditMode ==0}">${ret.retPayCompensate}</c:if>
            元</dd>
    </dl>
    <c:if test="${isEditMode ==1}">
    <span style="color:burlywood;display: inline-block;height: 30px;line-height: 20px;">！补偿运费标准：8.00-15.00元，请与顾客沟通确</span>
    </c:if>
    <div style="clear: both;"></div>
    <dl>
        <dt style="background:#bdd2ff">退款总金额：</dt>
        <dd style="background: beige"><span id="retTotalAmount">${ret.retTotalAmount}</span> 元</dd>
    </dl>
    <div style="clear: both;"></div>
</fieldset>
<br>
<fieldset class="waikuang900">
    <legend>退款渠道详情</legend>
    <dl>
        <dt style="background:#bdd2ff;">退款的付款方式：</dt>
        <dd style="background: beige; width: 250px;" id="J-select-wallet">
            <c:if test="${isEditMode ==0}">
                <c:if test="${ret.retPayType=='THIRD_ALIPAY'}">支付宝</c:if>
                <c:if test="${ret.retPayType=='THIRD_BANK'}">银行</c:if>
                <c:if test="${ret.retPayType=='MLW_WALLET'}">不需要退至第三方平台</c:if>
            </c:if>
            <c:if test="${isEditMode ==1}">
                <input type="radio" name="retPayType" value="THIRD_ALIPAY" <c:if test="${ret.retPayType=='THIRD_ALIPAY'}"> checked="checked" </c:if>  >支付宝
                <input type="radio" name="retPayType" value="THIRD_BANK" <c:if test="${ret.retPayType=='THIRD_BANK'}"> checked="checked" </c:if>  >银行
                <input type="radio" name="retPayType" value="MLW_WALLET" <c:if test="${ret.retPayType=='MLW_WALLET' or (ret.retPayType !='THIRD_ALIPAY' and ret.retPayType !='THIRD_BANK')}"> checked="checked" </c:if> >不需要退至第三方平台
            </c:if>
        </dd>
    </dl>
    <div style="clear: both;"></div>
    <dl class="ret-info-alipay" <c:if test="${ret.retPayType!='THIRD_ALIPAY'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">支付宝账户:</dt>
        <dd style="background: beige"><c:if test="${isEditMode ==1}"><input type="text" name="serviceRetAlipay" value="${ret.serviceRetAlipay}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetAlipay}</c:if></dd>
        <dt style="background:#bdd2ff">支付宝用户姓名:</dt>
        <dd style="background: beige"><c:if test="${isEditMode ==1}"><input type="text" name="serviceRetAlipayName" value="${ret.serviceRetAlipayName}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetAlipayName}</c:if></dd>

    </dl>
    <div style="clear: both;"></div>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">银行名称:</dt>
        <dd style="background: beige"><c:if test="${isEditMode ==1}"><input type="text" name="serviceRetBank" value="${ret.serviceRetBank}"></c:if>
                                      <c:if test="${isEditMode ==0}">${ret.serviceRetBank}</c:if></dd>
    </dl>
    <div style="clear: both;"></div>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">银行账户名：</dt>
        <dd style="background: beige"><c:if test="${isEditMode ==1}"><input type="text" name="serviceRetCardName" value="${ret.serviceRetCardName}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetCardName}</c:if></dd>
    </dl>
    <div style="clear: both;"></div>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">银行卡号：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="serviceRetCardNum" value="${ret.serviceRetCardNum}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetCardNum}</c:if>
        </dd>
    </dl>
    <div style="clear: both;"></div>
    <dl class="ret-info-bank" <c:if test="${ret.retPayType!='THIRD_BANK'}">style="display: none;"</c:if>>
        <dt style="background:#bdd2ff">开户行：</dt>
        <dd style="background: beige">
            <c:if test="${isEditMode ==1}"><input type="text" name="serviceRetOpenBank" value="${ret.serviceRetOpenBank}"></c:if>
            <c:if test="${isEditMode ==0}">${ret.serviceRetOpenBank}</c:if>
        </dd>
    </dl>
    <div style="clear: both;"></div>
    <legend>退款金额：</legend>
    <div style="clear: both;"></div>
    <c:if test="${!empty isUseCardPay}">
        <dl>
            <dt style="background:#bdd2ff">退至礼品卡账户：</dt>
            <dd style="background: beige">
                <c:if test="${isEditMode ==1}">
                	<input type="text" id="retRealCardAmount" value="<c:if test="${empty ret.retRealCardAmount}">0.00</c:if><c:if test="${!empty ret.retRealCardAmount}">${ret.retRealCardAmount}</c:if>" max="<c:if test="${!empty cardCanRetAmount}">${cardCanRetAmount}</c:if><c:if test="${empty cardCanRetAmount}">0</c:if>" name="retRealCardAmount" class="number retPayPrice required">
                </c:if>
                <c:if test="${isEditMode ==0}">
                	<c:if test="${!empty ret.retRealCardAmount}">${ret.retRealCardAmount}</c:if><c:if test="${empty ret.retRealCardAmount}">0.00</c:if>
                </c:if>&nbsp;元
            </dd>
        </dl>
        <span style="color: #0000ff;display: inline-block;height: 30px;line-height: 30px;">申请退换货的商品使用礼品卡支付占比金额：<span id="updRetToPercent"><fmt:formatNumber type="number" value="${iniRetCartPayAmount}" maxFractionDigits="2"></fmt:formatNumber></span>元</span>
    </c:if>
    
    <div style="clear: both;"></div>
    <div class="ret-info-wallet" <c:if test="${empty ret.retPayType or ret.retPayType =='MLW_WALLET'}">style="display: none;"</c:if>>
		  <dl>
	        <dt style="background:#bdd2ff">退至美丽湾钱包：</dt>
	        <dd style="background: beige">
	            <c:if test="${isEditMode ==1}">
	            	<input type="text" id="retRealMlwAmount" name="retRealMlwAmount" value="<c:if test="${empty ret.retRealMlwAmount}">0.00</c:if><c:if test="${!empty ret.retRealMlwAmount}">${ret.retRealMlwAmount}</c:if>" class="number retPayPrice required">
	            </c:if>
	            <c:if test="${isEditMode ==0}">
	            	<c:if test="${!empty ret.retRealMlwAmount}">${ret.retRealMlwAmount}</c:if>
	            	<c:if test="${empty ret.retRealMlwAmount}">0.00</c:if>
	            </c:if>&nbsp;元
	        </dd>
	    </dl>
	    <span style="color: #0000ff;display: inline-block;height: 30px;line-height: 30px;">申请退换货的商品使用美丽钱包支付占比金额：<span id="updRetWalletToPercent"><fmt:formatNumber type="number" value="${iniRetWalletPayAmount}" maxFractionDigits="2"></fmt:formatNumber></span>元</span>
    </div>
    
    <div style="clear: both;"></div>
    <dl>
        <dt style="background:#bdd2ff">
        	退至[<span id="updRetToType">
        		<c:if test="${empty ret.retPayType}">美丽钱包</c:if>
        		<c:if test="${ret.retPayType=='MLW_WALLET'}">美丽钱包</c:if>
        		<c:if test="${ret.retPayType=='THIRD_ALIPAY'}">支付宝</c:if>
        		<c:if test="${ret.retPayType=='THIRD_BANK'}">银行</c:if>
        	</span>]：
        </dt>
        <dd style="background: beige">
        	<span id="updRetToTypeAmount">
        		<c:if test="${!empty ret.retTotalAmount}">
        			<fmt:formatNumber type="number" value="${ret.retTotalAmount-ret.retRealCardAmount-ret.retRealMlwAmount}" maxFractionDigits="2"></fmt:formatNumber>
        		</c:if>
        		<c:if test="${empty ret.retTotalAmount}">0.00</c:if>
        	</span> 元
        </dd>
    </dl>
</fieldset>
<c:if test="${isEditMode ==1}">
    <script>
        $(document).ready(function () {
            //改变值的时候
            $(".retPayPrice").change(function () {
                var retTotal = parseFloat($("#retPayAmount").val()) + parseFloat($("#retPayFare").val()) + parseFloat($("#retPayCompensate").val());
                var retCard = parseFloat($("#retRealCardAmount").val() == null ? 0 : $("#retRealCardAmount").val());
                var retWallet = parseFloat($("#retRealMlwAmount").val());
                var retTotal2 = retTotal;
                if(!isNaN(retCard) || !isNaN(retWallet)){
                    retTotal2 = retTotal - retCard - retWallet;
                }
                $("#retTotalAmount").html(retTotal.toFixed(2));
                $("#updRetToTypeAmount").html(retTotal2.toFixed(2));
            });

            //核算退货商品总金额
            var sumPrice=function(obj){
                var _len=$('#J-retordProduct .ordi_tr').length;
                //改变单行的值==========================
                /*if($(obj).attr('buy-num') >= $(obj).val()){
                    var this_percent = parseFloat($.trim($(obj).parent().parent().parent().find('.thisPrice').html().replace("<div>","").replace("</div>","")))*parseInt($(obj).val())/parseFloat($("#updOrderRealAmount").html())*parseFloat($("#updCardPayAmount").html());
                    $(obj).parent().parent().parent().find('.thisPercent').html(this_percent.toFixed(1));
                }*/
                //==========================
                var _totalPrice=0;
                var _retPercent=0;
                var _retWPercent=0;
                for(var i= 0;i<_len;i++){
                    if($('#input_'+i)[0].checked){
                        if(eval($('#ordi_num_'+i).attr('buy-num')) >= eval($('#ordi_num_'+i).val())){
                            _totalPrice+=parseFloat($.trim($('#price_'+i).find('div').text()))*parseInt($('#ordi_num_'+i).val());
                            if($("#updCardPayAmount").html()>0){
                                _retPercent = _retPercent +  parseFloat(($.trim($('#price_'+i).find('div').text()))*parseInt($('#ordi_num_'+i).val())/$("#updOrderRealAmount").html()*$("#updCardPayAmount").html());
                            }
                            if($("#updWalletPayAmount").html()>0){
                                _retWPercent = _retWPercent +  parseFloat(($.trim($('#price_'+i).find('div').text()))*parseInt($('#ordi_num_'+i).val())/$("#updOrderRealAmount").html()*$("#updWalletPayAmount").html());
                            }
                        } else {
                            break;//循环列表，如果发现不满足退货数量条件的，直接跳出循环
                        }
                    }
                }
                if(_totalPrice>0){//Object 0 has no method toFixed();
                    $('#retProductAmount').html(_totalPrice.toFixed(2));
                } else {
                    $('#retProductAmount').html("0.00");
                }
                if(_retPercent>0){
                    $('#updRetToPercent').html(_retPercent.toFixed(2));
                } else {
                    $('#updRetToPercent').html("0.00");
                }
                if(_retWPercent>0){
                    $('#updRetWalletToPercent').html(_retWPercent.toFixed(2));
                } else {
                    $('#updRetWalletToPercent').html("0.00");
                }
            };
            $(document).on('click','input.checkOrdi',function(){sumPrice();});  //勾选改变
            $(document).on('keyup','input.ordi_num',function(){sumPrice(this);});   //输入改变
            //选择退款账户
            $('#J-select-wallet').find('input').change(function(){
                if($(this).val() == 'THIRD_ALIPAY'){
                    resetBankInfo();
                    $('.ret-info-alipay').show();
                    $('.ret-info-wallet').show();
                    $("#updRetToType").html("支付宝");
                }else if($(this).val() == 'THIRD_BANK'){
                    resetAlipayInfo();
                    $('.ret-info-bank').show();
                    $('.ret-info-wallet').show();
                    $("#updRetToType").html("银行");
                }else{
                    resetAlipayInfo();
                    resetBankInfo();
                    resetWalletInfo();
                    $("#updRetToType").html("美丽钱包");
                }
            });

            var resetAlipayInfo = function(){
                $('.ret-info-alipay').hide();
                $('.ret-info-wallet').hide();
                $("input[name='serviceRetAlipay']").val('');
                $("input[name='serviceRetAlipayName']").val('');
            }

            var resetBankInfo = function(){
                $('.ret-info-bank').hide();
                $('.ret-info-wallet').hide();
                $('.ret-info-bank').find('input').val('');
            }
            
            var resetWalletInfo = function(){
                var retTotal = parseFloat($("#retPayAmount").val()) + parseFloat($("#retPayFare").val()) + parseFloat($("#retPayCompensate").val());
                var retCard = parseFloat($("#retRealCardAmount").val() == null ? 0 : $("#retRealCardAmount").val());
                var retWallet = parseFloat('0.00');
                var retTotal2 = retTotal;
                if(!isNaN(retCard) || !isNaN(retWallet)){
                    retTotal2 = retTotal - retCard - retWallet;
                }
                $("#retTotalAmount").html(retTotal.toFixed(2));
                $("#updRetToTypeAmount").html(retTotal2.toFixed(2));
                $("#retRealMlwAmount").val('0.0');
            }
        });
    </script>
</c:if>
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
</fieldset>

<br>
<hr>
<br>

<h2> 退换货处理流程记录 </h2>
<table class="table" width="1080">
    <thead>
    <tr>
        <th width="12%" align="center">处理时间</th>
        <th width="10%" align="center">处理结果</th>
        <th width="73%" align="center">处理内容</th>
        <th width="5%" align="center">操作人</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="logs" items="${ret.retLogsList}">
        <tr>
            <td><fmt:formatDate value="${logs.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
            <td>${logs.optResult}</td>
            <td title="${logs.optContent}">${fn:substring(logs.optContent, 0, 67)}<c:if test="${fn:length(logs.optContent)>67}"><br>${fn:substring(logs.optContent, 67, 134)}</c:if><c:if test="${fn:length(logs.optContent)>134}"><br>${fn:substring(logs.optContent, 134, 200)}</c:if></td>
            <td>${logs.optMan}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<br>
<hr>
<br>



<!-- 管理员操作区域 -->
<div>
    <%--<fieldset>--%>
        <%--<legend>打印发货单</legend>--%>
        <%--<dl style="width: 700px;"><a target="_blank" href="/oms/printer/viewer?order_id=${ret.retordOrderId}&type=RET_DELIVER_GOODS">打印</a></dl>--%>
    <%--</fieldset>--%>
    <c:if test="${flow ==10}">
        <fieldset>
            <legend>客服审核意见</legend>
            <dl style="width: 700px;">
                <dt style="background:#bdd2ff">处理结果：</dt>
                <dd style="background: beige"><input type="radio" name="nextStatus" value="15" class="required">同意 <input type="radio" name="nextStatus" value="12" class="required">拒绝</dd>
            </dl>
            <dl style="width: 700px;">
                <dt style="background:#bdd2ff">昵称：</dt>
                <dd style="background: beige">${adminObj.adminName}<input name="optMan" type="hidden" value="${adminObj.adminName}"></dd>
            </dl>
            <dl style="width: 700px; height: auto">
                <dt style="background:#bdd2ff">处理说明：</dt>
                <dd style="background: beige"><textarea  maxlength="200" style="width: 400px; height: 70px;"  name="optContent"></textarea></dd>
            </dl>
        </fieldset>
    </c:if>

    <c:if test="${flow ==85}">
    <fieldset>
        <legend>客服取消退换货审核意见</legend>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">处理结果：</dt>
            <dd style="background: beige"><input type="radio" name="nextStatus"  checked="checked" value="85">取消退换货申请</dd>
        </dl>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">昵称：</dt>
            <dd style="background: beige">${adminObj.adminName}<input name="optMan" type="hidden" value="${adminObj.adminName}"></dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">给顾客填写取消原因：</dt>
            <dd style="background: beige"><textarea  maxlength="200" style="width: 400px; height: 70px;"  name="optContent"></textarea></dd>
        </dl>
    </fieldset>
    </c:if>

    <c:if test="${flow ==18}">
        <fieldset>
            <legend>修改申请处理</legend>
            <dl style="width: 700px;">
                <dt style="background:#bdd2ff">处理后结果：</dt>
                <dd style="background: beige ; width: 500px;"><input type="radio" class="required"  checked="checked" name="nextStatus" value="18" >修改操作完成 </dd>
            </dl>
            <dl style="width: 700px;">
                <dt style="background:#bdd2ff">操作人：</dt>
                <dd style="background: beige">${adminObj.adminName}<input name="optMan" type="hidden" value="${adminObj.adminName}"></dd>
            </dl>
            <dl style="width: 700px; height: auto">
                <dt style="background:#bdd2ff">修改说明：</dt>
                <dd style="background: beige"><textarea  maxlength="200" style="width: 400px; height: 70px;" name="optContent"></textarea></dd>
            </dl>
        </fieldset>
    </c:if>

    <c:if test="${flow ==20}">
    <fieldset>
        <legend>仓库收货处理</legend>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">处理结果：</dt>
            <dd style="background: beige ; width: 500px;"><input type="radio"  name="nextStatus" value="40" >确认收货 <input type="radio"  name="nextStatus" value="41" >拒收 <span style="color: #FF0000">如果拒收，请与快递人员当面拒收</span></dd>
        </dl>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">操作人：</dt>
            <dd style="background: beige">${adminObj.adminName}<input name="optMan" type="hidden" value="${adminObj.adminName}"></dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">处理说明：</dt>
            <dd style="background: beige"><textarea  maxlength="200" style="width: 400px; height: 70px;" name="optContent"></textarea></dd>
        </dl>
    </fieldset>
    </c:if>

    <c:if test="${flow ==60}">
        <fieldset>
            <legend>确认买家收货处理</legend>
            <dl style="width: 700px;">
                <dt style="background:#bdd2ff">处理后结果：</dt>
                <dd style="background: beige ; width: 500px;"><input type="radio"  name="nextStatus" value="60" >买家收货确认 </dd>
            </dl>
            <dl style="width: 700px;">
                <dt style="background:#bdd2ff">操作人：</dt>
                <dd style="background: beige">${adminObj.adminName}<input name="optMan" type="hidden" value="${adminObj.adminName}"></dd>
            </dl>
            <dl style="width: 700px; height: auto">
                <dt style="background:#bdd2ff">处理说明：</dt>
                <dd style="background: beige"><textarea maxlength="200" style="width: 400px; height: 70px;" name="optContent">客户已收到换货</textarea></dd>
            </dl>
        </fieldset>
    </c:if>


    <c:if test="${flow ==48}">
    <fieldset>
        <legend>仓库发货</legend>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">处理后结果：</dt>
            <dd style="background: beige ; width: 500px;"><input type="radio" checked="checked"  name="nextStatus" value="48" >仓库已发货  <span style="color: red">请先填写货运单号再打印发货单 务必确认已发货后在提交</span> </dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">快递公司：</dt>
            <dd style="background: beige">
                <select name="kdCp" class="required kdCp">
                    <c:forEach items="${transportCompany}" var="tc">
                        <option value="${tc.code}">${tc.desc}</option>
                    </c:forEach>
                </select>
            </dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">货运单号：</dt>
            <dd style="background: beige"><input type="text" name="kdNum"  class="required kdNum" maxlength="20"></dd>
        </dl>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">操作人：</dt>
            <dd style="background: beige">${adminObj.adminName}<input name="optMan" type="hidden" value="${adminObj.adminName}"></dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">操作：</dt>
            <dd style="background: beige"><a target="_blank" href="/oms/printer/viewer?order_id=${ret.retordOrderId}&type=RET_STOCK_WAITCHECK"> 打印发货单</a> <input type="button" value="打印配送单(未开放)"></dd>
        </dl>
    </fieldset>
    <input type="hidden" name="optContent"  id="sendFlowOptContent" />
    </c:if>


    <c:if test="${flow ==70}">
    <fieldset>
        <legend>财务退款情况(如果退入支付宝或银行，则走线下流程退款后，这里做记录用)</legend>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">处理后结果：</dt>
            <dd style="background: beige ; width: 500px;"><input type="radio" checked="checked" class="required" name="nextStatus" value="75"> 已退款</dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">退款方式：</dt>
            <dd style="background: beige">    <c:if test="${ret.retPayType=='MLW_WALLET'}">美丽钱包</c:if>
                <c:if test="${ret.retPayType=='THIRD_ALIPAY'}">退钱回支付宝</c:if>
                <c:if test="${ret.retPayType=='THIRD_BANK'}">银行</c:if></dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">输入退款密码<span style="color: red">!如果没有财务密码，则先设置财务密码</span>：</dt>
            <dd style="background: beige;width: 450px;"><input id="retordPwd" name="optPwd" class="required" type="password"><span style="color: red">需要此密码才能改变订单退款状态</span>  </dd>
        </dl>
        <dl style="width: 700px;">
            <dt style="background:#bdd2ff">操作人：</dt>
            <dd style="background: beige">${adminObj.adminName}<input name="optMan" type="hidden" value="${adminObj.adminName}">
            </dd>
        </dl>
        <dl style="width: 700px; height: auto">
            <dt style="background:#bdd2ff">处理说明：</dt>
            <dd style="background: beige"><textarea maxlength="200" style="width: 400px; height: 70px;" name="optContent"></textarea></dd>
        </dl>
    </fieldset>

        <div style="display: none" id="retordParams" ></div>
    </c:if>
</div>
</div>
<c:if test="${isEditMode ==1}">
<script>
    $(document).ready(function(){
        $(".curEditMode").click(function(){
            var ordiStr ="";
            $(".checkOrdi:checked").each(function(){
                ordiStr = ordiStr +  $(this).val()+"_"+$(this).parents(".ordi_tr").find(".ordi_num").val()+",";
            });
            if(ordiStr==""){
                alert('请选择需要退换货的商品！');
                return false;
            }else{
                $("#ordi_list_str").val(ordiStr);
            }
        });

    });
</script>
</c:if>

<div class="formBar">
    <ul>
        <c:if test="${flow ==10}">
        <li>
            <div class="button">
                <div class="buttonContent">
                    <button class="button curEditMode"  type="submit">提交审核意见</button>
                </div>
            </div>
        </li>


        </c:if>

        <c:if test="${flow ==18}">
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button class="button curEditMode" type="submit">修改确认</button>
                    </div>
                </div>
            </li>
        </c:if>

        <c:if test="${flow ==20}">
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button class="button" type="submit">确认收货</button>
                    </div>
                </div>
            </li>
        </c:if>
        <c:if test="${flow ==85}">
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button class="button" type="submit">确认取消退换货申请</button>
                    </div>
                </div>
            </li>
        </c:if>
        <c:if test="${flow ==48}">
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button class="button" id="flowBtn48" type="submit">确认发货</button>
                    </div>
                </div>
            </li>
            <script>
                $(document).ready(function () {
                    $("#flowBtn48").click(function () {
                        var flow48Content = "美丽湾已发货，" + $(".kdCp option:selected").text() + "，货运单号:" + $(".kdNum").val();
                        $("#sendFlowOptContent").val(flow48Content);
                    });
                });
            </script>
        </c:if>

        <c:if test="${flow ==60}">
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button class="button" type="submit">买家收货确认</button>
                    </div>
                </div>
            </li>
        </c:if>

        <c:if test="${flow ==70}">
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button class="button" id="flow70" type="submit">确认退款</button>
                    </div>
                </div>
                <script>
                    $(document).ready(function () {
                        $("#flow70").click(function () {
                            var flow70params =
                                    ' <input type="hidden" name="retordParams"  value="adminId='+$("#retOptUserId").val()+'"  />'  +
                                    ' <input type="hidden" name="retordParams"  value="flowPwd='+$("#retordPwd").val()+'"  />' ;
                            $("#retordParams").html(flow70params);
                        });
                    });
                </script>
            </li>
        </c:if>

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