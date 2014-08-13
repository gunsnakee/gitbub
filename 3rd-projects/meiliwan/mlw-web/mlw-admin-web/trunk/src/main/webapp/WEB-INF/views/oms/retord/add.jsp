<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<script src="js/area.js" type="text/javascript"></script>
<h2 class="contentTitle">客服发起退换货</h2>
<c:if test="${handle==-1}">
    <div id="upTurnDiv">
        <legend><span style="background:#bdd2ff">1、输入订单号 》</span> 2、填写退换货信息 》 3、提交成功</legend><br>
        <legend>请输入需要申请退换货的订单</legend><br>
        <form action="/oms/returnOrder/add" method="post" id="upForm" class="required-validate" onsubmit="return navTabSearch(this)">
            <input type="hidden" value="0" name="handle">
            <dl>
                <dd style='background: beige'><input type='text' name="ordId" id="ordIdVal" class="required digits" value="${oid}">&nbsp;&nbsp;&nbsp;<span style="color: red"><c:if test="${msg==0}">←该订单不存在或不符合申请条件！</c:if><c:if test="${msg==1}">←该订单存在正在处理的退换货申请，请等待上次的申请结束！</c:if><c:if test="${msg==2}">←请输入合法的订单号！</c:if></span></dd>
            </dl><br>
            <dl><dt><input type="submit" value="下一步" id="nextTurn"></dt></dl>
        </form>
    </div>
</c:if>
<c:if test="${handle==0}">
<div class="pageContent">
<form action="/oms/returnOrder/add" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
<input type="hidden" value="1" name="handle">
<input type="hidden" name="oldOrderId" value="${ord.orderId}">
<input type="hidden" value="${ord.orderId}" id="orderId">
<input type="hidden" value="${ord.uid}" name="uid">
<input type="hidden" value="1" name="createType">
<input type="hidden" value="${ord.userName}" name="userName">

<input type="hidden" name="adProvince" id="provinceVal">
<input type="hidden" name="adCity" id="cityVal">
<input type="hidden" name="adCounty" id="countyVal">

<input type="hidden" name="retTotalAmount" id="retTotalAmountIn_add">
<div class="pageFormContent" layoutH="95">
    <legend>1、输入订单号 》 <span style="background:#bdd2ff">2、填写退换货信息 》</span> 3、提交成功</legend><br>
    <fieldset>
        <legend>订单信息</legend>
        <dl>
            <dt style="background:#bdd2ff">订单编号：</dt>
            <dd style="background: beige">${ord.orderId}</dd>
        </dl>
        <dl>
            <dt style="background:#bdd2ff">下单时间</dt>
            <dd style="background: beige"><fmt:formatDate value="${ord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></dd>
        </dl>
        <dl>
            <dt style="background:#bdd2ff"><a  class="btn" rel="10156" href="/oms/returnOrder/add" target="navTab" title="客服添加退换货">修改订单</a> </dt>
        </dl>
    </fieldset>
    <br>
    <br>
    <hr>
    <br>
    <br>
    <h2> 请选择需要退货或换货的商品 </h2>
    <table class="table" width="100%">
        <thead>
        <tr>
            <th>已选商品</th>
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
        <tbody>
        <c:forEach items="${ordiList}" var="oi">
            <tr  class="add_ordi_tr">
                <td><input type="checkbox" value="${oi.orderItemId}" class="add_checkOrdi"></td>
                <td>${oi.proId}</td>
                <td>${oi.proBarCode}</td>
                <td>${oi.proName}</td>
                <td><input class="add_unitPrice" value="${oi.uintPrice}"></td>
                <td>${oi.saleNum}<input type="hidden" class="add_saleNum" value="${oi.saleNum}"/></td>
                <td><span class="add_thisPercent" data-form="0.00"><c:if test="${cardPayAmount>0}"><fmt:formatNumber type="number" value="${(oi.uintPrice*oi.saleNum)/ord.realPayAmount*cardPayAmount}" maxFractionDigits="2"></fmt:formatNumber></c:if><c:if test="${empty cardPayAmount||cardPayAmount<=0}">0</c:if></span></td>
                <td><span class="add_thisPercent" data-form="0.00"><c:if test="${mlwPayAmount>0}"><fmt:formatNumber type="number" value="${(oi.uintPrice*oi.saleNum)/ord.realPayAmount*mlwPayAmount}" maxFractionDigits="2"></fmt:formatNumber></c:if><c:if test="${empty mlwPayAmount||mlwPayAmount<=0}">0</c:if></span></td>
                <td><input type="text" value="1"  class="add_ordi_num required" min="1" max="${oi.saleNum}"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <input type="hidden" name="userSelect" value="" id="ordi_list_str_add">
    <br>
    <br>
    <hr>
    <br>
    <br>
    <fieldset  style="width: 250px;">
        <legend>申请退换货详情</legend>
        <dl>
            <dt style="background:#bdd2ff">服务类型：</dt>
            <dd style="background: beige"><input type="radio" name="retType" value="RET" checked>退货<input type="radio" name="retType" value="CHG">换货</dd>
        </dl>
        <%--
        <dl>
            <dt style="background:#bdd2ff">退款的付款方式：</dt>
            <dd style="background: beige"><input type="radio" name="retPayType" value="MLW_WALLET" checked id="mlw">美丽钱包<input type="radio" name="retPayType" value="THIRD_ALIPAY" id="alipay">支付宝<input type="radio" name="retPayType" value="THIRD_BANK" id="bank">支付银行</dd>
        </dl>
        <div id="bankHide" style="display: none">
            <dl>
                <dt style="background:#bdd2ff">银行名称:</dt>
                <dd style="background: beige"><input type="text" name="serviceRetBank"></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">银行账户名：</dt>
                <dd style="background: beige"><input type="text" name="serviceRetCardName"></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">银行卡号：</dt>
                <dd style="background: beige"><input type="text" name="serviceRetCardNum"></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">开户行：</dt>
                <dd style="background: beige"><input type="text" name="serviceRetOpenBank"></dd>
            </dl>
        </div>
        <div id="alipayHide" style="display: none">
            <dl>
                <dt style="background:#bdd2ff">支付宝账号:</dt>
                <dd style="background: beige"><input type="text" name="serviceRetAlipay"></dd>
            </dl>
        </div>
        --%>
        <dl style="  height: auto;">
            <dt style="background:#bdd2ff">问题描述：</dt>
            <dd style="background: beige; height: auto">
                <textarea name="applyComments" cols="28" rows="6" class="required"></textarea>
            </dd>
        </dl>
    </fieldset>
        <dl style="width: auto; height: auto;">
            <dt style="background:#bdd2ff">上传凭证：</dt>
            <dd style="background: beige;width: auto; height: auto;">
                <div class="wrap mlw">
                    <div class="hd">
                        <div class="post-btn" id="J-add-ret-uploadImg">上传图片</div>
                    </div>
                    <div class="bd">
                        <div id="J-add-five-image" class="img-queue"></div>
                    </div>
                </div>
                <div class="imageP" id="img-queue"></div>
            </dd>
        </dl>
    <fieldset style="width: 250px; clear: both ">
        <dl>
            <dt style="background:#bdd2ff">联系人：</dt>
            <dd style="background: beige"><input type="text" value="${addr.recvName}" name="adRecvName"></dd>
        </dl>
        <dl>
            <dt style="background:#bdd2ff">手机：</dt>
            <dd style="background: beige"><input type="text" value="${addr.mobile}" name="adMobile"></dd>
        </dl>
        <dl>
            <dt style="background:#bdd2ff">地址：</dt>
            <dd style="background: beige"><input type="text" value="${addr.detailAddr}" name="adDetailAddr"></dd>
        </dl>
        <dl>
            <dt style="background:#bdd2ff">电话：</dt>
            <dd style="background: beige"><input type="text" value="${addr.phone}" name="adPhone"></dd>
        </dl>
        <dl>
            <dt style="background:#bdd2ff">地区：</dt>
            <dd style="background: beige"><div id="mlw_area"></div></dd>
        </dl>
    </fieldset>
    <br>
    <br>
    <fieldset style="width: 550px; clear: both ">
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
            <dd style="background: beige"><span id="orderRealAmount">${ord.realPayAmount}</span>元</dd>
        </dl>
    </fieldset>

    <fieldset style="width: 550px; clear: both ">
        <legend>原订单支付详情</legend>
        <%--
        <dl>
            <dt style="background:#bdd2ff">使用<c:if test="${!empty thirdPayDesc}">[${thirdPayDesc}]</c:if><c:if test="${empty thirdPayDesc}">第三方</c:if>支付：</dt>
            <dd style="background: beige"><span><c:if test="${!empty thirdPayAmount}">${thirdPayAmount}</c:if><c:if test="${empty thirdPayAmount}">0.0</c:if></span>元</dd>
        </dl>
        --%>
        <dl>
            <dt style="background:#bdd2ff">使用第三方支付：</dt>
            <dd style="background: beige">
            	<span><c:if test="${!empty thirdPayAmount}">${thirdPayAmount}</c:if><c:if test="${empty thirdPayAmount}">0.0</c:if></span>元&nbsp;&nbsp;[${thirdPayDesc}]
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
            <dd style="background: beige"><span id="addCardPayAmount"><c:if test="${!empty cardPayAmount}">${cardPayAmount}</c:if><c:if test="${empty cardPayAmount}">0.0</c:if></span>元</dd>
        </dl>
    </fieldset>
    <hr>
    <br>
    <br>
    <fieldset id="retBackInput" style="width: 700px; clear: both ">
        <legend>退款详情</legend>
        <dl>
            <dt style="background:#bdd2ff">是否需要退款：</dt>
            <dd style="background: beige"><input type="radio" name="retBack" value="1" checked id="retBackY">是<input type="radio" name="retBack" value="0" id="retBackN">否</dd>
        </dl>
        <div id="retBackDiv_add" >
            <dl style="width: 700px">
                <dt style="background:#bdd2ff">商品退款金额：</dt>
                <dd style="background: beige; width: 500px;"><input type="text" value="0.00" id="retPayAmount_add" name="retPayAmount" class="number retPayPrice">元
                    &nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red">*</span><span style="color: #0000ff">申请退款的商品购买总金额：<span id="autoMoney" style="color: red">0.00</span>元</span></dd>
            </dl>
            <dl style="width: 700px">
                <dt style="background:#bdd2ff">退运费金额:</dt>
                <dd style="background: beige; width: 500px;"><input type="text" value="0.00" id="retPayFare_add" name="retPayFare"  class="number retPayPrice">元
                    &nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red">*</span><span style="color: #0000ff">订单运费：<span style="color: red"><b>${ord.transportFee}</b></span>元</span></dd>
            </dl>
            <dl style="width: 700px">
                <dt style="background:#bdd2ff">补偿运费金额：</dt>
                <dd style="background: beige; width: 500px;"><input type="text" value="0.00" id="retPayCompensate_add" name="retPayCompensate" class="number retPayPrice">元
                    &nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red">!</span><span style="color: orange">补偿运费标准：<span style="color: red"><b>8.00-15.00</b></span>元，请与顾客沟通确认</span></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">退款总金额：</dt>
                <dd style="background: beige" id="retTotalAmount_add">0.00 元</dd>
            </dl>
        </div>
    </fieldset>

    <br><br>
    <fieldset style="width: 700px; clear: both " id="retBackCardDiv_add">
        <legend>退款渠道详情</legend>
        <dl>
            <dt style="background:#bdd2ff">退款的付款方式：</dt>
            <dd style="background: beige;width: 250px;">
            	<input type="radio" name="retPayType" value="MLW_WALLET" checked id="mlw">不需要退至第三方平台
            	<input type="radio" name="retPayType" value="THIRD_ALIPAY" id="alipay">支付宝
            	<input type="radio" name="retPayType" value="THIRD_BANK" id="bank">银行
            </dd>
        </dl>
        <div style="clear: both;"></div>
        <div id="bankHide" style="display: none">
            <dl>
                <dt style="background:#bdd2ff">银行名称:</dt>
                <dd style="background: beige"><input type="text" name="serviceRetBank"></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">银行账户名：</dt>
                <dd style="background: beige"><input type="text" name="serviceRetCardName"></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">银行卡号：</dt>
                <dd style="background: beige"><input type="text" name="serviceRetCardNum"></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">开户行：</dt>
                <dd style="background: beige"><input type="text" name="serviceRetOpenBank"></dd>
            </dl>
        </div>
        <div id="alipayHide" style="display: none">
            <dl>
                <dt style="background:#bdd2ff">支付宝账号:</dt>
                <dd style="background: beige"><input type="text" name="serviceRetAlipay"></dd>
            </dl>
            <dl>
                <dt style="background:#bdd2ff">支付宝用户姓名:</dt>
                <dd style="background: beige"><input type="text" name="serviceRetAlipayName"></dd>
            </dl>
        </div>
        <div style="clear: both;"></div>
        <c:if test="${isUseCardPay!=null && isUseCardPay==1}">
        <dl>
            <dt style="background:#bdd2ff">退至礼品卡账户：</dt>
            <dd style="background: beige"><input type="text" id="retRealCardAmount_add" value="0.00" name="retRealCardAmount" max="<fmt:formatNumber type="number" value="${cardPayAmount-cardRetAmount}" maxFractionDigits="2"></fmt:formatNumber>" class="number retPayPrice required">元</dd>
        </dl>
        <span style="color: #0000ff;display: inline-block;height: 30px;line-height: 30px;">申请退换货的商品使用礼品卡支付占比金额：<span id="retToPercent">0.00</span>元</span>
        </c:if>
        
        <div class="ret-info-wallet" <c:if test="${empty ret.retPayType or ret.retPayType =='MLW_WALLET'}">style="display: none;"</c:if>>
				  <dl>
			        <dt style="background:#bdd2ff">退至美丽湾钱包：</dt>
			        <dd style="background: beige">
			            <input type="text" id="retRealMlwAmount" name="retRealMlwAmount" value="<c:if test="${empty ret.retRealMlwAmount}">0.00</c:if><c:if test="${!empty ret.retRealMlwAmount}">${ret.retRealMlwAmount}</c:if>" class="number retPayPrice required">元
			        </dd>
			    </dl>
			    <span style="color: #0000ff;display: inline-block;height: 30px;line-height: 30px;">申请退换货的商品使用美丽钱包支付占比金额：<span id="updRetWalletToPercent">0.00</span>元</span>
		    </div>
    
        <dl>
            <dt style="background:#bdd2ff">退至[<span id="retToType">美丽钱包</span>]：</dt>
            <dd style="background: beige"><span id="retToTypeAmount_add">0.00</span>元</dd>
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
            <dt style="background:#bdd2ff">售后回访分值：</dt>
            <dd style="background: beige"></dd>
        </dl>
    </fieldset>

    <br>
    <hr>
    <br>


</div>
<div class="formBar">
    <ul>
        <li>
            <div class="button">
                <div class="buttonContent">
                    <button type="submit" class="applyRet">提交</button>
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
<input type="hidden" name="provinceCode" value="${addr.provinceCode}">
<input type="hidden" name="cityCode" value="${addr.cityCode}">
<input type="hidden" name="countyCode" value="${addr.countyCode}">

<script>
    var provinceCode=$("input[name=provinceCode]").val();
    var cityCode=$("input[name=cityCode]").val();
    var countyCode=$("input[name=countyCode]").val();
    $("#mlw_area").mlwAreaPicker({provinceCode:provinceCode, cityCode:cityCode, countyCode:countyCode});
    //选中退换货的商品，自动计算购买金额
    $(document).on('click','.add_checkOrdi' ,function(){
        var total = 0;//申请退换货的商品总金额
        var cardPercent = 0;//申请退换货的商品占礼品卡支付金额的比例
        var _retWPercent=0;
        $("input.add_checkOrdi:checked").each(function(){
            total = total +  parseFloat($(this).parents(".add_ordi_tr").find(".add_unitPrice").val()*$(this).parents(".add_ordi_tr").find(".add_ordi_num").val());
            if($("#addCardPayAmount").html()>0){
                cardPercent = cardPercent +  parseFloat(($(this).parents(".add_ordi_tr").find(".add_unitPrice").val()*$(this).parents(".add_ordi_tr").find(".add_ordi_num").val())/$("#orderRealAmount").html()*$("#addCardPayAmount").html());
            }
            if($("#updWalletPayAmount").html()>0){
                _retWPercent = _retWPercent +  parseFloat(($(this).parents(".add_ordi_tr").find(".add_unitPrice").val()*$(this).parents(".add_ordi_tr").find(".add_ordi_num").val())/$("#orderRealAmount").html()*$("#updWalletPayAmount").html());
            }
        });
        if(total>0){//Object 0 has no method toFixed();
            $('#autoMoney').html(total.toFixed(2));
        } else {
            $('#autoMoney').html("0.00");
        }
        if(cardPercent>0){
            $('#retToPercent').html(cardPercent.toFixed(2));
        } else {
            $('#retToPercent').html("0.00");
        }
        if(_retWPercent>0){
            $('#updRetWalletToPercent').html(_retWPercent.toFixed(2));
        } else {
            $('#updRetWalletToPercent').html("0.00");
        }
    });
    //修改退换货商品数量，自动计算购买金额
    $(document).on('keyup','.add_ordi_num' ,function(){
        var retNum= $(this).val();
        var buyNum = $(this).attr("max");
        if(eval(retNum)<=eval(buyNum)){
            //======================计算单行礼品卡占金额比例
            //var this_percent = parseFloat($(this).parents(".add_ordi_tr").find(".add_unitPrice").val())*retNum/parseFloat($("#orderRealAmount").html())*parseFloat($("#addCardPayAmount").html());
            //$(this).parent().parent().parent().find('.add_thisPercent').html(this_percent.toFixed(2));
            //======================
            var total = 0;
            var cardPercent = 0;//申请退换货的商品占礼品卡支付金额的比例
            $("input.add_checkOrdi:checked").each(function(){
                total = total +  parseFloat($(this).parents(".add_ordi_tr").find(".add_unitPrice").val()*$(this).parents(".add_ordi_tr").find(".add_ordi_num").val());
                if($("#addCardPayAmount").html()>0){
                    cardPercent = cardPercent +  parseFloat(($(this).parents(".add_ordi_tr").find(".add_unitPrice").val()*$(this).parents(".add_ordi_tr").find(".add_ordi_num").val())/$("#orderRealAmount").html()*$("#addCardPayAmount").html());
                }
            });
            if(total>0){//Object 0 has no method toFixed();
                $('#autoMoney').html("<b>"+total.toFixed(2)+"</b>");
            }
            if(cardPercent>0){
                $('#retToPercent').html(cardPercent.toFixed(2));
            }
        }
    });
    //拼退换货的商品行串
    $(".applyRet").click(function(){
        var ordiStr ="";
        $(".add_checkOrdi:checked").each(function(){
            ordiStr = ordiStr +  $(this).val()+"_"+$(this).parents(".add_ordi_tr").find(".add_ordi_num").val()+",";
        });
        var province=$("#sel_province option:selected").text();
        var city=$("#sel_city option:selected").text();
        var county=$("#sel_county option:selected").text();
        $("#provinceVal").val(province);
        $("#cityVal").val(city);
        $("#countyVal").val(county);
        if(ordiStr==""){
            alert('请选择需要退换货的商品！');
            return false;
        }else{
            $("#ordi_list_str_add").val(ordiStr);
        }
        if($('#img-queue').html()==""){
            alert('请上传图片！');
            return false;
        }
    });
    //选择退款方式
    $("#mlw").click(function(){
        $("#bankHide").hide();
        $("#alipayHide").hide();
        $('.ret-info-wallet').hide();
        $("#bankHide  input").each(function(){
            $(this).val("");
        });
        $("#alipayHide  input").each(function(){
            $(this).val("");
        });
        //显示退至XX
        $("#retToType").html("美丽钱包");
        resetWalletInfo();
    });
    $("#bank").click(function(){
        $("#bankHide").show();
        $("#alipayHide").hide();
        $("#alipayHide  input").each(function(){
            $(this).val("");
        });
        //显示退至XX
        $("#retToType").html("银行");
        $('.ret-info-wallet').show();
    });
    $("#alipay").click(function(){
        $("#bankHide").hide();
        $("#bankHide  input").each(function(){
            $(this).val("");
        });
        $("#alipayHide").show();
        //显示退至XX
        $("#retToType").html("支付宝");
        $('.ret-info-wallet').show();
    });
    //选择是否退款
    $("#retBackY").click(function(){
        $("#retBackDiv_add").show();
        $("#retBackCardDiv_add").show();
    });
    $("#retBackN").click(function(){
        $("#retBackDiv_add").hide();
        $("#retBackCardDiv_add").hide();
        $("#retRealCardAmount_add").val("0.00");
        $("#retBackInput  input").each(function(){
            $(this).val("0.00");
            $("#retTotalAmount_add").html('0.00');
        });
        $("#retToTypeAmount_add").html('0.00');
    });
    $(".retPayPrice").change(function () {
        var retTotal = parseFloat($("#retPayAmount_add").val()) + parseFloat($("#retPayFare_add").val()) + parseFloat($("#retPayCompensate_add").val());
        var retCard = parseFloat($("#retRealCardAmount_add").val() == null ? 0 : $("#retRealCardAmount_add").val());
        var retWallet = parseFloat($("#retRealMlwAmount").val());
        var retTotal2 = retTotal;
        if(!isNaN(retCard) || !isNaN(retWallet)){
            retTotal2 = retTotal - retCard - retWallet;
        }
        $("#retTotalAmount_add").html(retTotal.toFixed(2)+"元");
        $("#retTotalAmountIn_add").val(retTotal.toFixed(2));
        $("#retToTypeAmount_add").html(retTotal2.toFixed(2));
    });
    
    var resetWalletInfo = function(){
        var retTotal = parseFloat($("#retPayAmount_add").val()) + parseFloat($("#retPayFare_add").val()) + parseFloat($("#retPayCompensate_add").val());
        var retCard = parseFloat($("#retRealCardAmount_add").val() == null ? 0 : $("#retRealCardAmount_add").val());
        var retWallet = parseFloat('0.00');
        var retTotal2 = retTotal;
        if(!isNaN(retCard) || !isNaN(retWallet)){
            retTotal2 = retTotal - retCard - retWallet;
        }
        $("#retTotalAmount_add").html(retTotal.toFixed(2)+"元");
        $("#retTotalAmountIn_add").val(retTotal.toFixed(2));
        $("#retToTypeAmount_add").html(retTotal2.toFixed(2));
        $("#retRealMlwAmount").val('0.0');
    }

    //上传
    $('#J-add-ret-uploadImg').uploadify({
        swf: '/swf/uploadify.swf',
        uploader:'http://imagecode.meiliwan.com/file/uploadImg?projectName=bkstage-web&upfileElemName=Filedata&showSize=80x80',
        height:25,
        width:70,
        buttonText:'上传图片',
        buttonCursor:'pointer',
        auto:true,
        fileSizeLimit:'1000KB',
        fileTypeDesc:'Image Files',
        fileTypeExts:'*.png; *.jpg; *.jpeg',
        uploadLimit: 30,
        formData:{},
        method:'post',
        multi:false,
        queueID:'img-queue',
        queueSizeLimit:1,
        removeCompleted:false,
        removeTimeout:3,
        requeueErrors:false,
        progressData:'speed',
        preventCaching:false,
        suceessTimeout:300,
        onDialogClose:function(){
            var $items=$('#img-queue div.uploadify-queue-item');
            if($items.length>5){
                $items.slice(5).remove();
                alert('已经上传了5张照片');
                return;
            }
        },onUploadSuccess:function (file, data1, response) {
            var data = $.parseJSON(data1);
            var _con='<a target="_blank" href="'+data.url+'"><img width="80" height="80" src="' + data.url + '" /></a>';
            var _con2='<input type="hidden" name="uploadPic" value="'+data.persistUri+'" >';
            $('#' + file.id + '').append(_con).find('.data').css('color', '#11CDF1').fadeOut(2000).end().find('.uploadify-progress').fadeOut(2500);
            $('#' + file.id + '').append(_con2);
        }
    });
</script>
</c:if>
