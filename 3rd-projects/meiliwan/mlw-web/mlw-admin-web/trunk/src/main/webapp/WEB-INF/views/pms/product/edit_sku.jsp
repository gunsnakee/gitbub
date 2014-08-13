<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<link rel="stylesheet" type="text/css" href="/css/upload.css">
<link rel="stylesheet" href="js/kindeditor/themes/default/default.css" />
<style>
    <style>
        .mlw .uploadify-queue-item .cancel {
        top: 158px; /*133px*/
        right: 72px;
    }
    .uploadify-queue-item .op-bar {
        position: absolute;
        left: 0;
        bottom: -24px;
        width: 100%;
        height: 24px;
        line-height: 24px;
    }
    .uploadify-queue-item .op-bar .mask {
        position: absolute;
        z-index: 1;
        width: 100%;
        height: 24px;
        background-color: #000;
        opacity: 0.3;
        filter: alpha(opacity = 30);
    }
    .uploadify-queue-item .op-bar .con {
        position: absolute;
        z-index: 10;
    }
    .uploadify-queue-item .op-bar .con .ico {
        position: absolute;
        width: 16px;
        height: 16px;
        overflow: hidden;
        cursor: pointer;
    }
    .uploadify-queue-item .op-bar .con .left-arrow,
    .uploadify-queue-item .op-bar .con .left-arrow-hover {
        left: 18px;
        top: 8px;
    }
    .uploadify-queue-item .op-bar .con .right-arrow,
    .uploadify-queue-item .op-bar .con .right-arrow-hover {
        left: 40px;
        top: 8px;
    }
    .uploadify-queue-item .op-bar .con .left-arrow { background: url(/images/arrow-left.png) 0px 0px no-repeat; }
    .uploadify-queue-item .op-bar .con .left-arrow-hover { background: url(/images/arrow-left_hover.png) 0px 0px no-repeat; }
    .uploadify-queue-item .op-bar .con .right-arrow { background: url(/images/arrow-right.png) 0px 0px no-repeat; }
    .uploadify-queue-item .op-bar .con .right-arrow-hover { background: url(/images/arrow-right_hover.png) 0px 0px no-repeat; }
    .uploadify-queue-item .op-bar .con .set-main {
        position: absolute;
        left: 80px;
        width: 55px;
        color: #FFF;
        cursor: pointer;
    }
</style>
<form method="post" id="updare-sku" action="/pms/product/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone());" >
    <input type="hidden" value="" id="pro-add-falls">
    <input type="hidden" value="" id="pro-add-fallsImageUri">
    <input type="hidden" value="0" id="imgHeight">
    <input type="hidden" value="${product.proId}" id="edit-proId">
    <input type="hidden" name="navFlag" value="${navFlag}" id="navFlag">
    <div class="pageFormContent nowrap" layoutH="70">
    <fieldset>
        <legend><span style="color: #ff0000">公共信息</span></legend>
        <dl>
            <dt>SPU ID:</dt>
            <dd>${product.spuId}</dd>
        </dl>
        <dl>
            <dt>类目:</dt>
            <dd><span>${firstPc.categoryName} &lt;  ${secondPc.categoryName}  &lt;  ${thirdPc.categoryName}</span></dd>
        </dl>
        <dl>
            <dt>商品标题:</dt>
            <dd>${product.proName} ${product.skuName}</dd>
        </dl>
    </fieldset>
    <fieldset>
        <legend><span style="color: #ff0000">规格信息</span></legend>
        <c:if test="${empty skuDetail}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无规格属性数据</font></td></tr>
        </c:if>
        <c:if test="${!empty skuDetail}">
                <dl>
                    <dt>规格信息：</dt>
                    <dd>
                        ${skuDetail};
                    </dd>
                </dl>
        </c:if>
    </fieldset>
    <fieldset>
        <legend>商品条形码</legend>
        <dl>
            <dt>商品条形码：</dt>
            <dd>
                <input type="hidden" id="barCode" value="${product.barCode}">
                <label>${product.barCode}</label>
            </dd>
        </dl>
    </fieldset>
    <fieldset>
        <legend>商品相关价格</legend>
        <dl>
            <dt>美丽价：</dt>
            <dd>
                <input type="text" id="mlwPrice" size="25" name="mlwPrice" class="required number" value="" min="0.01"/> 元
            </dd>
        </dl>
        <dl>
            <dt>市场价：</dt>
            <dd>
                <input type="text" id="marketPrice" size="25" name="marketPrice" class="required number" value="" min="0.01"/> 元
            </dd>
        </dl>
        <dl>
            <dt>进货价：</dt>
            <dd>
                <input type="text" id="tradePrice" size="25" name="tradePrice" class="required number" value="" min="0.01"/>  元
            </dd>
        </dl>
    </fieldset>
    <fieldset>
        <legend>商品相关库存</legend>
        <dl>
            <dt>库存：</dt>
            <dd>
                <input type="text" size="25" name="stock" id="stock" class="required digits" value="" min="1" max="99999"/>
            </dd>
        </dl>
        <dl>
            <dt>安全库存：</dt>
            <dd>
                <input type="text" size="25" name="safeStock" id="safeStock" class="required digits" value="" min="0" max="99999"/>
            </dd>
        </dl>
    </fieldset>
        <fieldset>
            <legend>预售相关</legend>
            <dl>
                <dt>预售结束时间：</dt>
                <dd>
                    <input type="text" size="25" name="presaleEndTime" id="presaleEndTime" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </dd>
            </dl>
            <dl>
                <dt>预计发货时间：</dt>
                <dd>
                    <input type="text" size="25" name="presaleSendTime" id="presaleSendTime" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </dd>
            </dl>
        </fieldset>
    <fieldset>
        <legend>商品自定义属性</legend>
        <div  id="add-sku-chideren"><a class="button" href="javascript:;"><span>添加自定义属性</span></a></div>
    </fieldset>
    <fieldset>
        <legend>地方频道</legend>
        <dl>
            <dd><input type="checkbox" id="J-pro-add-isFalls"><span style="font: 20px">是否加入地方频道</span></dd>
        </dl>
        <dl>
            <dd>
                <div class="wrap mlw">
                    <div class="hd">
                        <div class="post-btn" id="J-pro-add-uploadyFalls">上传图片</div>
                    </div>
                    <div class="bd">
                        <div id="J-add-img-Falls" class="img-queue"></div>
                    </div>
                </div>
            </dd>
        </dl>
    </fieldset>
    <fieldset>
        <legend><span style="color: #ff0000">统计数值</span></legend>
        <dl>
            <dt>显示销量：</dt>
            <dd>
                <input name="showSellNum" id="showSellNum" type="text" class="digits" value="0"/>
            </dd>
        </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <%--<li><div class="button"><div class="buttonContent"><button type="button" onclick="return validate();">保      存</button></div></div></li>--%>
            <li><div class="button"><div class="buttonContent"><button type="button" onclick="return skuAddOff()">下架并预览</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button type="button" id="J-update-cacle" class="close">取     消</button></div></div></li>
        </ul>
    </div>
</form>
<script type="text/javascript">
    $(document).ready(function () {
        $("#add-sku-chideren").click(function(){
            $(this).before('<dl><input name="selfPropId" type="hidden" value="0"><dt><input name="selfPropName" type="text" style="width: 110px"></dt> <dd><input name="selfPropValue" type="text" style="width: 150px"></dd></dl>');
        });

        var SetMainPic = function () {
            var $imgItem = $('div.uploadify-queue-item');
            var $setMainBtn = $imgItem.find('span.set-main');
            (function () {
                $setMainBtn.click(function () {
                    var $self = $(this);
                    if ($.trim($self.text()) === '主图') {
                        return;
                    }
                    else {
                        var $imgQueue = $self.parents('.img-queue');
                        $imgQueue.find('img.imgCube').attr('main-pic', 'false').end().find('div.op-bar span.set-main').html('设为主图');
                        $self.html('主图').parents('.op-bar').prev('img.imgCube').attr('main-pic', 'true');
                    }
                });
            })();
            $imgItem.each(function (index) {
                var $self = $(this);
                var $opBar = $self.find('div.op-bar');
                var $leftArrow = $opBar.find('span.left-arrow');
                var $rightArrow = $opBar.find('span.right-arrow');
                /*鼠标经过效果*/
                $self.hover(function () {
                    $opBar.css('bottom', 0);
                    $self.find('div.cancel').css('top', '133px');
                }, function () {
                    $opBar.css('bottom', '-24px');
                    $self.find('div.cancel').css('top', '158px');
                });
                $opBar.mouseenter(function () {
                    $(this).css('bottom', 0);
                });
                $leftArrow.hover(function () {
                    $self.index() === 0 ? $(this).removeClass('left-arrow-hover') : $(this).addClass('left-arrow-hover');
                }, function () {
                    $(this).removeClass('left-arrow-hover');
                });
                $rightArrow.hover(function () {
                    $self.index() == $imgItem.length - 1 ? $(this).removeClass('right-arrow-hover') : $(this).addClass('right-arrow-hover');
                }, function () {
                    $(this).removeClass('right-arrow-hover');
                });
                /*图片位置切换*/
                $leftArrow.unbind('click').bind('click', function () {//左移
                    var _index = $self.index();
                    _index == 0 ? false : $self.insertBefore($('#J-add-five-image div.uploadify-queue-item').eq(_index - 1));
                });
                $rightArrow.unbind('click').bind('click', function () {
                    var _index = $self.index();
                    _index === $imgItem.length - 1 ? false : $self.insertAfter($('#J-add-five-image div.uploadify-queue-item').eq(_index + 1));
                });
            });
        };
        /*uploadify*/
        var imageFalls = "230,149,354,504";
        $('#J-pro-add-uploadyFalls').uploadify({
            swf: '/swf/uploadify.swf',
            uploader: 'http://imagecode.meiliwan.com/file/uploadImg?projectName=profalls&upfileElemName=Filedata&sizeRule='+imageFalls+"&showSize=230&ratio=T",
            height: 30,
            width: 120,
            buttonText: '上传图片',
            buttonCursor: 'pointer',
            auto: true,
            fileSizeLimit: '5000KB',
            fileTypeDesc: 'Image Files',
            fileTypeExts: '*.gif; *.jpg; *.png',
            formData: {},
            method: 'post',
            multi: true,
            queueID: 'J-add-img-Falls',
            queueSizeLimit: 1,
            uploadLimit: 25,
            removeCompleted: false,
            removeTimeout: 3,
            requeueErrors: false,
            progressData: 'speed',
            preventCaching: false,
            suceessTimeout: 300,
            onDialogOpen: function () {
            },
            onUploadSuccess: function (file, data, response) {
                data = $.parseJSON(data);//把json字符串转换成json对象
                var _con = '<img class="imgCube" width="" height="'+data.h+'" src="' + data.url + '" key="'+data.persistUri+'" alt="">';
                _con += '<div class="op-bar"><div class="con"><span class="ico left-arrow"></span><span class="ico right-arrow"></span><span class="set-main">设为主图</span></div><div class="mask"></div></div>';
                $('#' + file.id + '').append(_con).find('.data').css('color', '#11CDF1').fadeOut(2000).end().find('.uploadify-progress').fadeOut(2500);
            },
            onQueueComplete: function (queueData) {
                var $imgQueue=$('#J-add-img-Falls div.uploadify-queue-item');
                if($imgQueue.length>1){
                    $imgQueue.slice(1).remove();
                }
            }
        });
    });

    //保存功能
    function skuSave(){
        if(!fallsImage()){
            return false;
        }
        if(!validatePrice()){
            return false;
        }
    }

    //提交并且下架

    function skuAddOff(){
        var $form = $("#updare-sku");
        if (!$form.valid()) {
            return false;
        }
        if(!fallsImage()){
            return false;
        }
        var presaleEndTime = $("#presaleEndTime").val();
        var presaleSendTime = $("#presaleSendTime").val();
        if(presaleEndTime != ''){
            if(presaleSendTime == ''){
                alertMsg.error("请填写预售预计送达时间")
                return false;
            }
        }
        var proId = $('#edit-proId').val();
        var isFalls = $('#pro-add-falls').val();
        var fallsImageUri = $('#pro-add-fallsImageUri').val();
        var imgHeight = $('#imgHeight').val();
        var mlwPrice = $("#mlwPrice").val();
        var marketPrice = $("#marketPrice").val();
        var tradePrice = $("#tradePrice").val();
        var stock = $("#stock").val();
        var safeStock = $("#safeStock").val();
        var showSellNum = $("#showSellNum").val();
        //sku固有属性名称
        var $selfPropId = $("input[name=selfPropId]");
        var arrselfPropId=[];
        if($selfPropId.length>0){
            for(var i=0;i<$selfPropId.length;i++){
                arrselfPropId.push($selfPropId.eq(i).val());
            }
        }
        //sku固有属性名称
        var $selfPropName = $("input[name=selfPropName]");
        var arrPropNameStr=[];
        if($selfPropName.length>0){
           for(var i=0;i<$selfPropName.length;i++){
               arrPropNameStr.push($selfPropName.eq(i).val());
           }
        }
        //sku固有属值
        var $selfPropValue = $("input[name=selfPropValue]");
        var arrselfPropValue=[];
        if($selfPropValue.length>0){
            for(var i=0;i<$selfPropValue.length;i++){
                arrselfPropValue.push($selfPropValue.eq(i).val());
            }
        }
        var barCode = $("#barCode").val();
        var navFlag = $('#navFlag').val();
        $.post("/pms/product/update",
                {ustep: 2, navFlag:navFlag, proId: proId, isFalls: isFalls, fallsImageUri: fallsImageUri,imgHeight: imgHeight, mlwPrice: mlwPrice, marketPrice: marketPrice, tradePrice: tradePrice, stock: stock, safeStock: safeStock, showSellNum: showSellNum,state:2,selfPropId:arrselfPropId,selfPropName:arrPropNameStr,selfPropValue:arrselfPropValue,barCode:barCode,presaleEndTime:presaleEndTime,presaleSendTime:presaleSendTime},
                function (data) {
                    if (data.suc == '0') {
                        alertMsg.error("生成对应规格失败");
                        return false;
                    } else {
                        if(navFlag == 0){
                            $('#'+data.proId).prev().prev().attr('href',"javascript:;").addClass('viewDetail').attr("data-key","/pms/product/product-detail?proId="+data.proId).removeAttr("target").removeAttr("width").removeAttr("height");
                            $('#'+data.proId).html("当前状态:"+data.stateStr);
                            var $clone=$('#'+data.proId).prev().prev().clone();
                            $clone.insertAfter($('#'+data.proId).prev().prev());
                            $clone.prev().hide();
                            $clone[0].addEventListener('click',function(e){
                                e.preventDefault();
                                var href = $(this).attr('data-key');
                                navTab.openTab("0f55cb6caaf2b3de6bebda90d26d5c43",href,{title:'查看详情'});
                            });
                        }
                        navTab.openTab("ba693a664d734b3f418ee03ac078c7a5","/pms/product/viewAndOn?navFlag="+navFlag+"&proId="+data.proId,{title:'预览'});
                        $('#J-update-cacle').click();
                    }
                });

    }
    function validatePrice(){
        var mlwPrice = $("#mlwPrice").val();
        var marketPrice = $("#marketPrice").val();
        var tradePrice = $("#tradePrice").val();
        var re  = /^\d+(\.\d+)?$/;
        if(mlwPrice!=''&&marketPrice!=''&&tradePrice!=''){
            if(!re.test(mlwPrice)){
                alertMsg.error("请输入美丽价正确格式");
                return false;
            }
            if(!re.test(marketPrice)){
                alertMsg.error("请输入市场价正确格式");
                return false;
            }
            if(!re.test(tradePrice)){
                alertMsg.error("请输入进货价正确格式");
                return false;
            }
            return true;
        }else{
            alertMsg.error("请输入商品对应美丽价、市场价、进货价！");
            return false;
        }
    }

    function validateStock(){
        var stock = $("#stock").val();
        var safeStock = $("#safeStock").val();
        var strP=/^\d+$/;
        if(stock!=''){
            if(stock > 0&&stock<=99999&&strP.test(stock)){
            }else{
                alertMsg.error("请输入正确的库存");
                return false;
            }
        }
        if(safeStock!=''){
            if(safeStock > 0 && safeStock<=99999 && strP.test(safeStock)){
            }else{
                alertMsg.error("请输入正确的安全库存");
                return false;
            }
        }
        return true;
    }

    //验证是否加入图片频道
    function fallsImage(){
        var isFalls = $("#J-pro-add-isFalls").get(0).checked;
        if(isFalls){
            $("#pro-add-falls").val(1);
            var $imgs = $("#J-add-img-Falls img.imgCube");
            if($imgs.length == 0){
                alertMsg.error("请上传一张商品频道图片")
                return false;
            }else{
                var url = $imgs.attr('key');
                var imgHeight = $imgs.attr('height');
                $("#pro-add-fallsImageUri").val(url);
                $("#imgHeight").val(imgHeight);
                return true;
            }
        }else{
            $("#pro-add-falls").val(0);
            $("#pro-add-fallsImageUri").val("")
            return true;
        }
    }

</script>
