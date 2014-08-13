<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
<form method="post" action="/pms/product/add" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);" id="J-add-spu-all">
    <input type="hidden" name="step" value="3">
    <input type="hidden" name="firstLevel" value="${firstPc.categoryId}">
    <input type="hidden" name="secondLevel" value="${secondPc.categoryId}">
    <input type="hidden" name="thirdLevel" value="${thirdPc.categoryId}">
    <input type="hidden" name="imageUri" value="" id="pro-add-imageUri">
    <input type="hidden" name="isFalls" value="" id="pro-add-falls">
    <input type="hidden" name="fallsImageUri" value="" id="pro-add-fallsImageUri">
    <input type="hidden" name="imgHeight" value="0" id="imgHeight">
    <input type="hidden" name="state" value="0" id="pro-add-state">
    <div class="pageFormContent nowrap" layoutH="70">
        <fieldset>
            <legend><span style="color: #ff0000">当前已选商品类目</span></legend>
                   <div><span> ${firstPc.categoryName} &lt;  ${secondPc.categoryName}  &lt;  ${thirdPc.categoryName}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="background: #5c8069"><a title="修改类目" rel="73" target="navTab" href="/pms/product/add">返回修改类目</a></span> </div>
        </fieldset>
        <div class="divider"></div>
        <dl>
            <dt>商品标题：</dt>
            <dd>
                <input type="text" size="25" name="proName" class="required" value="" style="width: 300px" minlength="10" maxlength="100"/>
            </dd>
        </dl>
        <dl>
            <dt>商品短标题：</dt>
            <dd>
                <input type="text" size="25" name="shortName" class="required" value="" style="width: 300px" minlength="5" maxlength="40"/>
            </dd>
        </dl>
        <dl>
            <dt>宣传标题：</dt>
            <dd>
                <input type="text" size="25" name="advName" value="" style="width: 300px" minlength="5" maxlength="40"/>
            </dd>
        </dl>
        <dl>
            <dt>商品关键词：</dt>
            <dd>
                <input type="text" size="25" name="keyword" value="" style="width: 90px"/>
                <input type="text" size="25" name="keyword" value="" style="width: 90px"/>
                <input type="text" size="25" name="keyword" value="" style="width: 90px"/>
                <input type="text" size="25" name="keyword" value="" style="width: 90px"/>
                <input type="text" size="25" name="keyword" value="" style="width: 90px"/>
                <span>(至多5个)</span>
            </dd>
        </dl>
        <dl>
            <dt>是否支持货到付款：</dt>
            <dd>
                <input type="radio" name="isCod" value="1" checked="true"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="isCod" value="0"/>否
            </dd>
        </dl>
        <dl>
            <dt>品牌：</dt>
            <dd>
                <input type="hidden" id="brandId" name="brand.id" value="${brand.id}"/>
                <input type="text" class="required textInput" name="brand.name" value="" suggestFields="name" suggestUrl="/pms/brand/list?type=1" lookupGroup="brand"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a title="品牌管理" target="navTab" href="/pms/brand/list"><span style="color: #0000cc">[品牌管理]</span></a>
            </dd>
        </dl>
        <dl>
            <dt>产地：</dt>
            <dd>
                <select name="placeId" id="J-pro-add-place" class="required" style="width: 120px">
                    <option value='0'>请选择</option>
                    <c:forEach var="placeList" items="${places}">
                        <option value='${placeList.id}'>${placeList.placeName}</option>
                    </c:forEach>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <a title="产地管理" target="navTab" href="/pms/place/list"><span style="color: #0000cc">[产地管理]</span></a>
            </dd>
        </dl>
        <dl>
            <dt>供应商名称：</dt>
            <dd>
                <select name="supplierId" id="J-add-supplier" class="required" style="width: 120px">
                    <option value='0'>请选择</option>
                    <c:forEach var="supplier" items="${suppliers}">
                        <option value='${supplier.supplierId}'>${supplier.supplierName}</option>
                    </c:forEach>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <a title="供应商管理" target="navTab" href="/pms/supplier/list"><span style="color: #0000cc">[供应商管理]</span></a>
            </dd>
        </dl>
        <div class="divider"></div>
        <fieldset id="J-skuProps">
            <legend>规格 <span style="color: red">创建后规格不可修改(参数可以),请谨慎选择</span></legend>
            <c:if test="${empty skuProp}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无规格属性数据</font></td></tr>
            </c:if>
            <c:if test="${!empty skuProp}">
                <c:forEach var="e" varStatus="i" items="${skuProp}">
                    <dl>
                        <dt><input type="checkbox" name="skuProp" value="${e.proPropId}:${e.name}">${e.name}</dt>
                        <dt style="visibility:hidden;"><input type="checkbox" name="skuImage" value="${e.proPropId}">异图</dt>
                        <dd class="cbox">
                            <c:if test="${!empty e.proProValue}">
                                <c:forEach var="spv" varStatus="i" items="${e.proProValue}">
                                    <input type="checkbox" name="skuProv" value="${e.proPropId}:${spv.id}">${spv.name}&nbsp;&nbsp;
                                </c:forEach>
                            </c:if>
                            <span class="sku-item-show" style="display: none;color: #ff0000">选择${e.name}属性值已经到达15个</span>
                            <span class="item-show" style="display: none;color: #ff0000">请选择${e.name}属性</span>
                        </dd>
                    </dl>
                </c:forEach>
            </c:if>
        </fieldset>
        <fieldset class="goodsAttr">
            <legend><span style="color: #ff0000">填写商品参数</span></legend>
            <c:if test="${empty pps}">
                <div style="text-align: center;"><span style="color:#c31033; ">暂无相关属性</span></div>
            </c:if>
            <c:if test="${!empty pps}">
              <c:forEach var="pp" items="${pps}">
                  <c:if test="${pp.isSku == 0}">
                    <c:choose>
                        <c:when test="${pp.propertyType == 1}">
                            <dl>
                                <dt>${pp.name} :  </dt>
                                <dd>
                                    <select name="props" style="width: 100px;">
                                        <c:forEach var="ppv" items="${pp.proProValue}">
                                            <option value="${pp.id}:${ppv.id}:${pp.isFilter}">${ppv.name}</option>
                                        </c:forEach>
                                    </select>
                                </dd>
                            </dl>
                        </c:when>
                        <c:when test="${pp.propertyType == 2}">
                            <dl class="kv-item <c:if test="${pp.isRequired == 1}">req</c:if> cbox">
                                <dt>${pp.name} :  </dt>
                                <dd>
                                    <c:forEach var="ppv" items="${pp.proProValue}">
                                        <input type="checkbox" name="props" value="${pp.id}:${ppv.id}:${pp.isFilter}"/>${ppv.name}
                                    </c:forEach>
                                    <span class="item-show" style="display: none;color: #ff0000">请选择${pp.name}属性</span>
                                </dd>
                            </dl>
                        </c:when>
                        <c:otherwise>
                            <dl class="kv-item <c:if test="${pp.isRequired == 1}">req</c:if> text">
                                <input type="hidden" name="proPropId" value="${pp.id}">
                                <dt>${pp.name}：</dt>
                                <dd>
                                    <input name="proPropV" type="text" />
                                    <span class="item-show" style="display: none;color: #ff0000">请选择${pp.name}属性</span>
                                </dd>
                            </dl>
                        </c:otherwise>
                    </c:choose>
                  </c:if>
              </c:forEach>
            </c:if>
        </fieldset>
        <fieldset>
            <legend>商品自定义属性</legend>
            <div id="add-chideren"><a class="button" href="javascript:;"><span>添加自定义属性</span></a></div>
        </fieldset>
        <dl>
            <dt>商品小编推荐：</dt>
            <dd>
                <textarea name="editorRec" class="required textInput" cols="80" rows="4" minlength="15" maxlength="500"></textarea>
            </dd>
        </dl>
        <dl>
            <dt>商品详细描述锚点<span style="color: red">(最多25个锚点，每个锚点最多4个字)</span>：</dt>
            <dd>
                <script>
                    $(function() {
                        var editor = KindEditor.create('#pro-add-descp-menu',
                                {uploadJson:'http://imagecode.meiliwan.com/file/uploadImg?projectName=prodetail&upfileElemName=Filedata',
                                    filePostName:'Filedata',
                                    items:[
                                        'source'
                                    ],designMode:false});
                    });
                </script>
                <textarea id="pro-add-descp-menu" name="descpMenu" style="width:900px;height:100px;visibility:hidden;"></textarea>
            </dd>
        </dl>
        <dl>
            <dt>商品描述：</dt>
            <dd>
                <script>
                    $(function() {
                        var editor = KindEditor.create('#pro-add-descp',
                                {uploadJson:'http://imagecode.meiliwan.com/file/uploadImg?projectName=prodetail&upfileElemName=Filedata&sizeRule=320,480,640,800&ratio=T',
                                filePostName:'Filedata',
                                items:[
                                    'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                                    'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'multiimage',
                                    'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                                    'anchor', 'link', 'unlink', '|', 'about'
                                ]});
                    });
                </script>
                <textarea id="pro-add-descp" name="descp" style="width:900px;height:400px;visibility:hidden;"></textarea>
            </dd>
        </dl>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="return proAddOff()">下 一 步</button></div></div></li>
        </ul>
    </div>
</form>
<script type="text/javascript">
    $(document).ready(function () {
        $("#add-chideren").click(function(){
            $(this).before('<dl><input name="selfPropId" type="hidden" value="0"><dt><input name="selfPropName" type="text" style="width: 110px"></dt> <dd><input name="selfPropValue" type="text" style="width: 150px"></dd></dl>');
        });


        var _isDoubleChecked=false;
        $('input[name=skuProp]').click(function(){
            var $this=$(this);
            $this[0].checked===true?$this.parent().next().css({visibility:'visible'}).find('input').removeAttr('checked'):$this.parent().next().css({visibility:'hidden'}).find('input').removeAttr('checked');
            if($('input[name=skuProp]:checked').length==2){
                _isDoubleChecked=true;
                $('input[name=skuProp]').not(':checked').css({visibility:'hidden'});
                if($('input[name=skuImage]:checked').length == 0){
                    $this.parent().next().find('input[name=skuImage]').attr('checked','checked');
                }
                if(!$('input[name=skuImage]:checked')){
                    $this.parent().next().find('input[name=skuImage]').removeAttr('checked');
                }
            }else if($('input[name=skuProp]:checked').length==3){
                _isDoubleChecked=true;
            }
            else{
                _isDoubleChecked=false;
                $('input[name=skuProp]').not(':checked').css({visibility:'visible'});

            }
            $this.parent().next().next().find(".sku-item-show").hide();
            $this.parent().next().next().find(".item-show").hide();
            $this[0].checked===true?$this.parent().next().next().find('input').removeAttr('checked'):$this.parent().next().next().find('input').removeAttr('disabled');
            $this[0].checked===true?$this.parent().parent().attr('class','kv-item req'):$this.parent().parent().removeAttr("class");
        });

        $('input[name=skuImage]').click(function(e){

            console.log(_isDoubleChecked);
            if(_isDoubleChecked){
                if($('input[name=skuImage]:checked').length==0){
                    $(this).attr('checked','checked');
                    return;
                }
            }
            if($('input[name=skuImage]:checked').length>1){
                $('input[name=skuImage]').removeAttr('checked');
                $(this).attr('checked','checked');
            }
        });

        $('input[name=skuProv]').click(function(){
            var skuItem = $('input[name=skuProv]:checked');
            if(skuItem.length>2){
               $(this).find("sku-item-show").show();
            }
        });

        $('#J-skuProps dl dd').each(function(i,n){
            $(n).click(function(e){
                if($(n).find('input[name=skuProv]:checked').length==15){
                    if($(n).prev().prev().find('input')[0].checked===true){
                        $(n).find('span.sku-item-show').show();
                        $(n).find('input[name=skuProv]').not(':checked').attr('disabled','disabled');
                    }
                }else{
                    $(n).find('span.sku-item-show').hide();
                    $(n).find('input[name=skuProv]').removeAttr('disabled');

                }
            });

        });

    });

    //提交并且下架
    function proAddOff(){
        var $form = $("#J-add-spu-all");
        if (!$form.valid()) {
            return false;
        }
        KindEditor.sync('#pro-add-descp');
        KindEditor.sync('#pro-add-descp-menu');
        $("#pro-add-state").val(2);
        if(!validateParams()){
            return false;
        }
        if(event.keyCode == 13){
            return false;
        }
        navTab.rel(73);
    }
    function validateParams(){
        var place = $("#J-pro-add-place").val();
        if(place == 0){
            alertMsg.error("请选择商品产地");
            return false;
        }
        var brandId = $('#brandId').val();
        if(brandId ==''){
            alertMsg.error("暂无该品牌，请重新选择");
            return false;
        }
        if(!skuProperty()){
            alertMsg.error("请填写商品规格属性");
            return false;
        }
        if(!productProperty()){
            alertMsg.error("请填写商品相关必填属性");
            return false;
        }

        return true;
    }

    //验证商品属性必填属性是否已经填写
    function productProperty(){
        var validate = true;
        var items = $(".goodsAttr .kv-item");
        for (var i = 0; i < items.size(); i++) {
            var curItemIsValSuc = true;
            var item = items.eq(i);
            if (item.hasClass("req")) {
                if (item.hasClass("text")) {
                    if (item.find("input[type=text]").val() == "") {
                        curItemIsValSuc =false;
                    }
                }
                if (item.hasClass("cbox")) {
                    if (item.find("input[type=checkbox]:checked").size() == 0) {
                        curItemIsValSuc =false;
                    }
                }
            }
            if(curItemIsValSuc ==false){
                item.find(".item-show").show();
                validate =false;
            }
        }
        return validate;
    }
    //验证商品属性必填属性是否已经填写
    function skuProperty(){
        var validate = true;
        var items = $("#J-skuProps .kv-item");
        for (var i = 0; i < items.size(); i++) {
            var curItemIsValSuc = true;
            var item = items.eq(i);
            if (item.hasClass("req")) {
                if (item.find("input[name=skuProv]:checked").size() == 0) {
                    curItemIsValSuc =false;
                }
            }
            if(curItemIsValSuc ==false){
                item.find(".item-show").show();
                validate =false;
            }
        }
        return validate;
    }
</script>
