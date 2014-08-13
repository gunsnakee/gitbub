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
<form id="nation_image_form" method="post" action="/pms/product/updatesku-nationimage" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
    <input type="hidden" name="proId" value="${proId}">
    <input name="handle"  type="hidden" value="1"/>
    <input name="cacheState"  type="hidden" value="1"/>
    <input type="hidden" name="fallsImageUri" value="" id="pro-add-fallsImageUri">
    <input type="hidden" name="height" value="" id="imgHeight">
    <div class="pageFormContent" layoutH="80" height="280">
            <fieldset>
                <dl>
                    <dd><input name="isFalls" type="checkbox" id="sku-J-pro-add-isFalls" <c:if test="${isFalls == 1}">checked="true" </c:if>><span style="font: 20px">是否加入地方频道</span></dd>
                </dl>
                <dl>
                    <dd>
                        <div class="wrap mlw">
                            <div class="hd">
                                <div class="post-btn" id="J-pro-add-uploadyFalls">上传图片</div>
                            </div>
                            <div class="bd">
                                <div id="J-add-img-Falls" class="img-queue">
                                    <c:if test="${isFalls == 1}">
                                        <div id="old_image" class="uploadify-queue-item">
                                            <img width="" height="${height}" alt="" src="${fallsImageUri}" key="${fallsImageUri}" main-pic="false" class="imgCube">
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </dd>
                </dl>
            </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="button" class="close" onclick="return save()">保  存</button></div></div></li>
        </ul>
    </div>
</form>

<script type="text/javascript">
    $(document).ready(function () {
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
                try{
                    $( '#old_image' ).remove(); //删除原来的旧图片
                }catch(e){}
                data = $.parseJSON(data);//把json字符串转换成json对象
                var _con = '<img class="imgCube" width="" height="'+data.h+'" src="' + data.url + '" key="'+data.persistUri+'" alt="">';
               // _con += '<div class="op-bar"><div class="con"><span class="ico left-arrow"></span><span class="ico right-arrow"></span><span class="set-main">设为主图</span></div><div class="mask"></div></div>';
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

    //验证是否加入图片频道
    function fallsImage(){
        var isFalls = $("#sku-J-pro-add-isFalls").get(0).checked;
        if(isFalls){
            $("#sku-J-pro-add-isFalls").val(1);
            var $imgs = $("#J-add-img-Falls img.imgCube");
            if($imgs.length == 0){
                alert("请上传一张商品频道图片")
                return false;
            }else{
                var url = $imgs.attr('key');
                var imgHeight = $imgs.attr('height');
                $("#pro-add-fallsImageUri").val(url);
                $("#imgHeight").val(imgHeight);
                return true;
            }
        }else{
            $("sku-J-pro-add-isFalls").val(0);
            $("#pro-add-fallsImageUri").val("")
            return true;
        }
    }

    //保存
    function save(){

        if(!fallsImage()){
          return false;
        }
        var $niForm= $("#nation_image_form");
        $niForm.submit();
    }

</script>
