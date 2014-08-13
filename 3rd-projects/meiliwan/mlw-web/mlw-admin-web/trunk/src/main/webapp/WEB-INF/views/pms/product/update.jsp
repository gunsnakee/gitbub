<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<script type="text/javascript" src="/js/ueditor1_2_5_0/editor_config.js"></script>
<script type="text/javascript" src="/js/ueditor1_2_5_0/editor_all_min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<link rel="stylesheet" type="text/css" href="/css/upload.css">

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
<form method="post" action="/pms/product/update" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
    <input type="hidden" name="step" value="3">
    <input type="hidden" name="proId" value="${product.proId}">
    <input type="hidden" name="firstLevel" value="${product.firstCategoryId}">
    <input type="hidden" name="secondLevel" value="${product.secondCategoryId}">
    <input type="hidden" name="thirdLevel" value="${product.thirdCategoryId}">
    <input type="hidden" name="imageUri" value="" id="pro-up-imageUri">
    <input type="hidden" name="isFalls" value="${product.isFalls}" id="pro-up-falls">
    <input type="hidden" name="fallsImageUri" value="${product.fallsImageUri}" id="pro-up-fallsImageUri">
    <input type="hidden" name="imgHeight" value="${product.fallsImgHeight}" id="imgHeight">
    <input type="hidden" name="state" value="0" id="pro-up-state">
    <div class="pageFormContent nowrap" layoutH="70">
        <div class="divider"></div>
        <dl>
            <dt>商品标题：</dt>
            <dd>
                <input type="text" size="25" name="proName" class="required" value="${product.proName}" style="width: 300px" minlength="10" maxlength="100"/>
            </dd>
        </dl>
        <dl>
            <dt>商品短标题：</dt>
            <dd>
                <input type="text" size="25" name="shortName" class="required" value="${product.shortName}" style="width: 300px" minlength="5" maxlength="40"/>
            </dd>
        </dl>
        <dl>
            <dt>宣传标题：</dt>
            <dd>
                <input type="text" size="25" name="advName" value="${product.advName}" style="width: 300px" minlength="5" maxlength="40"/>
            </dd>
        </dl>
        <dl>
            <dt>商品关键词：</dt>
            <dd>
                    <input type="text" size="25" name="keyword" value="${keyword[0]}" style="width: 90px"/>
                    <input type="text" size="25" name="keyword" value="${keyword[1]}" style="width: 90px"/>
                    <input type="text" size="25" name="keyword" value="${keyword[2]}" style="width: 90px"/>
                    <input type="text" size="25" name="keyword" value="${keyword[3]}" style="width: 90px"/>
                    <input type="text" size="25" name="keyword" value="${keyword[4]}" style="width: 90px"/>
                <span>(至多5个)</span>
            </dd>
        </dl>
        <dl>
            <dt>美丽价：</dt>
            <dd>
                <input type="text" size="25" id="up-mlwPrice" name="mlwPrice" class="number" value="<c:if test="${product.mlwPrice > 0}">${product.mlwPrice}</c:if>" min="0.01"/> 元
            </dd>
        </dl>
        <dl>
            <dt>市场价：</dt>
            <dd>
                <input type="text" size="25" id="up-marketPrice" name="marketPrice" class="number" value="<c:if test="${product.marketPrice > 0}">${product.marketPrice}</c:if>" min="0.01"/> 元
            </dd>
        </dl>
        <dl>
            <dt>进货价：</dt>
            <dd>
                <input type="text" size="25" id="up-tradePrice" name="tradePrice" class="number" value="<c:if test="${product.tradePrice > 0}">${product.tradePrice}</c:if>" min="0.01"/>  元
            </dd>
        </dl>
        <dl>
            <dt>品牌：</dt>
            <dd>
                <select name="brand.id" style="width: 120px">
                    <option value='0'>请选择</option>
                    <c:forEach var="brand" items="${brands}">
                        <option value='${brand.brandId}' <c:if test="${brand.brandId == product.brandId}">selected="selected"</c:if>>${brand.name}</option>
                    </c:forEach>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <a title="品牌管理" rel="73" target="navTab" href="/pms/brand/list"><span style="color: #0000cc">[品牌管理]</span></a>
            </dd>
        </dl>
        <dl>
            <dt>产地：</dt>
            <dd>
                <select name="placeId" id="J-pro-up-place" class="required" style="width: 120px">
                    <option value='0'>请选择</option>
                    <c:forEach var="placeList" items="${places}">
                        <option value='${placeList.id}' <c:if test="${placeList.placeId == product.placeId}">selected="selected"</c:if>>${placeList.placeName}</option>
                    </c:forEach>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <a title="产地管理" rel="73" target="navTab" href="/pms/place/list"><span style="color: #0000cc">[产地管理]</span></a>
            </dd>
        </dl>
        <dl>
            <dt>经营方式：</dt>
            <dd>
                <select name="sellType" class="required" style="width: 120px">
                    <option value='1' <c:if test="${product.sellType == 1}">selected="selected"</c:if>>购销</option>
                    <option value='2' <c:if test="${product.sellType == 2}">selected="selected"</c:if>>代销</option>
                    <option value='3' <c:if test="${product.sellType == 3}">selected="selected"</c:if>>联营</option>
                </select>
            </dd>
        </dl>
        <dl>
            <dt>供应商名称：</dt>
            <dd>
                <select name="supplierId" class="required" style="width: 120px">
                    <option value='0'>请选择</option>
                    <c:forEach var="supplier" items="${suppliers}">
                        <option value='${supplier.supplierId}' <c:if test="${supplier.supplierId == product.supplierId}">selected="selected"</c:if>>${supplier.supplierName}</option>
                    </c:forEach>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <a title="供应商管理" rel="73" target="navTab" href="/pms/supplier/list"><span style="color: #0000cc">[供应商管理]</span></a>
            </dd>
        </dl>
        <dl>
            <dt>商品条形码：</dt>
            <dd>
                <input type="text" size="25" name="barCode" value="${product.barCode}" class="required digit" minlength="9" maxlength="13"/>
            </dd>
        </dl>
        <dl>
            <dt>商品包装名：</dt>
            <dd>
                <input type="text" size="25" name="barName" value="${product.barName}" minlength="5" maxlength="40"/>
            </dd>
        </dl>
        <dl>
            <dt>毛重：</dt>
            <dd>
                <input type="text" size="25" name="weight" value="${product.detail.weight}"class="number"/>g
            </dd>
        </dl>
        <dl>
            <dt>规格：</dt>
            <dd>
                <input type="text" size="25" name="proSpec" value="${product.detail.proSpec}"/>
            </dd>
        </dl>
        <dl>
            <dt>是否支持货到付款：</dt>
            <dd>
                <input type="radio" name="isPay" value="1" <c:if test="${product.isCod == 1}">checked="true"</c:if>/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="isPay" value="0" <c:if test="${product.isCod == 0}">checked="true"</c:if>/>否
            </dd>
        </dl>
        <div class="divider"></div>
        <fieldset>
            <legend><span style="color: #ff0000">填写商品参数</span></legend>
            <fieldset id="pro-up-goodsAttr">
            <c:if test="${empty propertList}">
                <div style="text-align: center;"><span style="color:#c31033; ">暂无相关属性</span></div>
            </c:if>
            <c:if test="${!empty propertList}">
              <c:forEach var="pp" items="${propertList}">
                    <c:choose>
                        <c:when test="${pp.propertyType == 1}">
                            <dl>
                                <dt>${pp.name} :  </dt>
                                <dd>
                                    <select name="propSelect" style="width: 100px;">
                                        <c:forEach var="ppv" items="${pp.proProValue}">
                                            <option value="${pp.id}-${ppv.id}-${pp.isFilter}" <c:if test="${ppv.fill == 1}">selected="selected"</c:if>>${ppv.name}</option>
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
                                        <input type="checkbox" name="propCheck" value="${pp.id}-${ppv.id}-${pp.isFilter}" <c:if test="${ppv.fill == 1}">checked="true" </c:if>/>${ppv.name}
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
                                    <input name="proPropV" type="text" value="${pp.descp}"/>
                                    <span class="item-show" style="display: none;color: #ff0000">请选择${pp.name}属性</span>
                                </dd>
                            </dl>
                        </c:otherwise>
                    </c:choose>
              </c:forEach>
            </c:if>
            </fieldset>
            <fieldset>
                <legend>商品自定义属性</legend>
                <c:if test="${!empty product.psplist}">
                    <c:forEach var="psp" items="${product.psplist}">
                        <dl>
                            <input name="selfPropId" type="hidden" value="${psp.id}">
                            <dt><input name="selfPropName" type="text" style="width: 110px" value="${psp.selfPropName}"></dt>
                            <dd><input name="selfPropValue" type="text" style="width: 150px" value="${psp.selfPropValue}"></dd>
                        </dl>
                    </c:forEach>
                </c:if>
                <div  id="update-chideren"><a class="button" href="javascript:;"><span>添加自定义属性</span></a></div>
            </fieldset>

        </fieldset>
        <dl>
            <dt>上传商品图片</dt>
            <dd>
                <div class="wrap mlw">
                    <div class="hd">
                        <div class="post-btn" id="pro-up-uploadImg">上传图片</div>
                    </div>
                    <div class="bd">
                        <div id="pro-up-five-image" class="img-queue">
                        <c:if test="${!empty imageList}">
                            <c:forEach items="${imageList}" var="map">
                                <div class="uploadify-queue-item">
                                    <div class="cancel" style="top: 158px;">
                                        <a href="#">X</a>
                                    </div>
                                    <span class="fileName">3.jpg (1KB)</span><span class="data" style="color: rgb(17, 205, 241); display: none;">上传成功</span>
                                    <div class="uploadify-progress" style="display: none;">
                                        <div class="uploadify-progress-bar" style="width: 100%;"><!--Progress Bar--></div>
                                    </div>
                                    <img width="" height="" alt="" src="${map.value}" key="${map.value}" main-pic="<c:if test="${map.key == 0}">true</c:if><c:if test="${map.key != 0}">false</c:if>" class="imgCube">
                                    <div class="op-bar" style="bottom: -24px;">
                                        <div class="con"><span class="ico left-arrow"></span><span class="ico right-arrow">
                                        </span><span class="set-main"><c:if test="${map.key == 0}">主图</c:if><c:if test="${map.key != 0}">设为主图</c:if></span>
                                        </div>
                                        <div class="mask"></div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                        </div>
                    </div>
                </div>
            </dd>
        </dl>

        <fieldset>
            <dl>
                <dd><input type="checkbox" id="pro-up-isFalls" <c:if test="${product.isFalls == 1}">checked="true" </c:if>><span style="font: 20px">是否加入地方频道</span></dd>
            </dl>
            <dl>
                <dd>
                    <div class="wrap mlw">
                        <div class="hd">
                            <div class="post-btn" id="uploadyFalls">上传图片</div>
                        </div>
                        <div class="bd">
                            <div id="update-Falls" class="img-queue">
                                <c:if test="${product.isFalls == 1}">
                                    <div class="uploadify-queue-item">
                                        <div class="cancel" style="top: 158px;">
                                            <a href="#">X</a>
                                        </div>
                                        <span class="fileName">3.jpg (1KB)</span><span class="data" style="color: rgb(17, 205, 241); display: none;">上传成功</span>
                                        <div class="uploadify-progress" style="display: none;">
                                            <div class="uploadify-progress-bar" style="width: 100%;"><!--Progress Bar--></div>
                                        </div>
                                        <img width="" height="${product.fallsImgHeight}" alt="" src="${product.fallsImageUri}" key="${product.fallsImageUri}" main-pic="false" class="imgCube">
                                        <div class="op-bar" style="bottom: -24px;">
                                            <div class="con"><span class="ico left-arrow"></span><span class="ico right-arrow">
                                                </span><span class="set-main">设为主图</span>
                                            </div>
                                            <div class="mask"></div>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </dd>
            </dl>
        </fieldset>
        <dl>
            <dt>商品加工厂：</dt>
            <dd>
                <input type="text" size="25" name="factory" value="${product.detail.factory}" minlength="2" maxlength="10"/>
            </dd>
        </dl>
        <dl>
            <dt>商品小编推荐：</dt>
            <dd>
                <textarea name="editorRec" class="required textInput" cols="80" rows="4" minlength="15" maxlength="500">${product.detail.editorRec}</textarea>
            </dd>
        </dl>
        <dl>
            <dt>商品详细描述锚点<span style="color: red">(最多25个锚点，每个锚点最多4个字)</span>：</dt>
            <dd>
                <script>
                    $(function() {
                        var editor = KindEditor.create('#pro-up-descp-menu',
                                {uploadJson:'http://imagecode.meiliwan.com/file/uploadImg?projectName=prodetail&upfileElemName=Filedata',
                                    filePostName:'Filedata',
                                    items:[
                                        'source',
                                    ],designMode:false});
                    });
                </script>
                <textarea id="pro-up-descp-menu" name="descpMenu" style="width:900px;height:100px;visibility:hidden;">${product.detail.descpMenu}</textarea>
            </dd>
        </dl>
        <dl>
            <dt>商品描述：</dt>
            <dd>
                <script>
                    $(function() {
                        var editor = KindEditor.create('#pro-up-descp',
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
                <textarea id="pro-up-descp" name="descp" style="width:900px;height:400px;visibility:hidden;">${product.detail.descp}</textarea>
            </dd>
        </dl>
        <fieldset>
            <legend><span style="color: #ff0000">seo编辑相关</span></legend>
            <dl>
                <dt>seo标题：</dt>
                <dd>
                    <input name="seoTitle" type="text" style="width: 600px;" value="${product.detail.seoTitle}"/>
                </dd>
            </dl>
            <dl>
                <dt>seo关键词：</dt>
                <dd>
                    <textarea name="seoKeyword" cols="80" rows="2" maxlength="255">${product.detail.seoKeyword}</textarea>
                </dd>
            </dl>
            <dl>
                <dt>seo描述：</dt>
                <dd>
                    <textarea name="seoDescp" cols="80" rows="3" maxlength="255">${product.detail.seoDescp}</textarea>
                </dd>
            </dl>
        </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="return productSave();">保      存</button></div></div></li>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="return proAddOff()">提交并下架</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button type="button" class="close">取     消</button></div></div></li>
        </ul>
    </div>
</form>
<script type="text/javascript">
    $(document).ready(function () {
        $('.uploadify-queue-item div.cancel a').click(function(e)
        {
            e.preventDefault();
            $(this).parents('.uploadify-queue-item').remove();
        });
        $("#update-chideren").click(function(){
            $(this).before('<dl><input name="selfPropId" type="hidden" value="0"><dt><input name="selfPropName" type="text" style="width: 110px"></dt> <dd><input name="selfPropValue" type="text" style="width: 150px"></dd></dl>');
        });

        var SetMainPic = function (id) {
            var $imgItem = $('div.uploadify-queue-item');
            var $setMainBtn = $imgItem.find('span.set-main');
            (function () {
                $setMainBtn.click(function () {
                    var $self = $(this);
                    if ($.trim($self.text()) === '主图') {
                        return;
                    }
                    else {
                        var $imgQueue = $self.parents(id+'.img-queue');
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
                    _index == 0 ? false : $self.insertBefore($('#pro-up-five-image div.uploadify-queue-item').eq(_index - 1));
                });
                $rightArrow.unbind('click').bind('click', function () {
                    var _index = $self.index();
                    _index === $imgItem.length - 1 ? false : $self.insertAfter($('#pro-up-five-image div.uploadify-queue-item').eq(_index + 1));
                });
            });
        };
        SetMainPic("#pro-up-five-image");
        /*uploadify*/
        var imageRule = "40x40,50x50,60x60,80x80,100x100,120x120,160X160,180X180,200x200,450x450,800x800,320x320,640x640,65x65,90x90,145x145";
        var imageFalls = "230,149,354,504";
        $('#pro-up-uploadImg').uploadify({
            swf: '/swf/uploadify.swf',
            uploader: 'http://imagecode.meiliwan.com/file/uploadImg?projectName=product&upfileElemName=Filedata&sizeRule='+imageRule+"&showSize=100x100",
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
            queueID: 'pro-up-five-image',
            queueSizeLimit: 5,
            uploadLimit: 50,
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
                var _con = '<img class="imgCube" width="" height="" main-pic="false" src="' + data.url + '" alt="" key="'+data.persistUri+'">';
                _con += '<div class="op-bar"><div class="con"><span class="ico left-arrow"></span><span class="ico right-arrow"></span><span class="set-main">设为主图</span></div><div class="mask"></div></div>';
                $('#' + file.id + '').append(_con).find('.data').css('color', '#11CDF1').fadeOut(2000).end().find('.uploadify-progress').fadeOut(2500);
            },
            onQueueComplete: function (queueData) {
                SetMainPic("#pro-up-five-image");
                var $imgQueue=$('#pro-up-five-image div.uploadify-queue-item');
                if($imgQueue.length>5){
                    $imgQueue.slice(5).remove();
                }
            }
        });
        /*uploadify*/
        $('#uploadyFalls').uploadify({
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
            queueID: 'update-Falls',
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
                var _con = '<img class="imgCube" width="" height="' + data.h +'" main-pic="false" src="' + data.url + '" alt="" key="'+data.persistUri+'">';
                _con += '<div class="op-bar"><div class="con"><span class="ico left-arrow"></span><span class="ico right-arrow"></span><span class="set-main">设为主图</span></div><div class="mask"></div></div>';
                $('#' + file.id + '').append(_con).find('.data').css('color', '#11CDF1').fadeOut(2000).end().find('.uploadify-progress').fadeOut(2500);
            },
            onQueueComplete: function (queueData) {
                //SetMainPic("#update-Falls");
                var $imgQueue=$('#update-Falls div.uploadify-queue-item');
                if($imgQueue.length>1){
                    $imgQueue.slice(1).remove();
                }
            }
        });
    });

    //保存功能
    function productSave(){
        KindEditor.sync('#pro-up-descp');
        KindEditor.sync('#pro-up-descp-menu');
        $("#pro-up-state").val(0);
        if(!fiveImage()){
            return false;
        }
        if(!fallsImage()){
            return false;
        }
    }
    //提交并且上架
    function proAddOn(){
        KindEditor.sync('#pro-up-descp');
        KindEditor.sync('#pro-up-descp-menu');
        $("#pro-up-state").val(1);
        if(!validateParams()){
            return false;
        }
    }
    //提交并且下架
    function proAddOff(){
        KindEditor.sync('#pro-up-descp');
        KindEditor.sync('#pro-up-descp-menu');
        $("#pro-up-state").val(2);
        if(!validateParams()||!upvalidatePrice()){
            return false;
        }
    }
    function validateParams(){
        var place = $("#J-pro-up-place").val();
        if(place == 0){
            alertMsg.error("请选择商品产地");
            return false;
        }
        if(!productProperty()){
            alertMsg.error("请填写商品相关必填属性");
            return false;
        }
        if(!fiveImage()){
            return false;
        }
        if(!fallsImage()){
            return false;
        }
        return true;
    }

    //验证是否上传5张商品主图
    function fiveImage(){
        var url = "";
        var $imgs = $("#pro-up-five-image img.imgCube");
        if ($imgs.length == 0) { //如果图片为空
            alertMsg.error("请上传商品相关的主图");
            return false;
        }
        for (var i = 0, _len = $imgs.length; i < _len; i++) {
            if ($imgs.eq(i).attr('main-pic') == 'false') {
                url += $imgs.eq(i).attr('key') + ',';
            }
            else {
                url = $imgs.eq(i).attr('key') + ',' + url;//拼接到字符串前面
            }
        }
        $("#pro-up-imageUri").val(url);
        return true;
    }
    //验证是否加入图片频道
    function fallsImage(){
        var isFalls = $("#pro-up-isFalls").get(0).checked;
        if(isFalls){
            $("#pro-up-falls").val(1);
            var $imgs = $("#update-Falls img.imgCube");
            if($imgs.length == 0){
                alertMsg.error("请上传一张商品频道图片")
                return false;
            }else{
                var url = $imgs.attr('key');
                var imgHeight = $imgs.attr('height');
                $("#pro-up-fallsImageUri").val(url);
                $("#imgHeight").val(imgHeight);
                return true;
            }
        }else{
            $("#pro-up-falls").val(0);
            return true;
        }
    }
    //验证商品属性必填属性是否已经填写
    function productProperty(){
        var validate = true;
        var items = $("#pro-up-goodsAttr .kv-item");
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
    function upvalidatePrice(){
        var mlwPrice = $("#up-mlwPrice").val();
        var marketPrice = $("#up-marketPrice").val();
        var tradePrice = $("#up-tradePrice").val();
        if(mlwPrice!=''&&marketPrice!=''&&tradePrice!=''){
            return true;
        }else{
            alertMsg.error("请输入商品对应美丽价、市场价、进货价！");
            return false;
        }
    }
</script>
