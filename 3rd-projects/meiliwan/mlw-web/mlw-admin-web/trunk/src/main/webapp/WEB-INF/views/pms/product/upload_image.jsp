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
<form method="post" action="" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
    <input type="hidden" id="provId" value="${provId}">
<div class="pageFormContent nowrap" layoutH="70">
    <dl>
        <dd>
            <div class="wrap mlw">
                <div class="hd">
                    <div class="post-btn" id="J-pro-add-uploadImg">上传图片</div>
                </div>
                <div class="bd">
                    <div id="J-add-five-image" class="img-queue"></div>
                </div>
            </div>
        </dd>
    </dl>
</div>
<div class="formBar">
    <ul>
        <li><div class="buttonActive"><div class="buttonContent"><button type="button" class="close" onclick="return fiveImage()">保  存</button></div></div></li>
    </ul>
</div>
</form>
<script type="text/javascript">
    $(document).ready(function () {
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
        var imageRule = "40x40,50x50,60x60,80x80,100x100,120x120,160X160,180X180,200x200,450x450,800x800,320x320,640x640,65x65,90x90,145x145";
        $('#J-pro-add-uploadImg').uploadify({
            swf: '/swf/uploadify.swf',
            uploader: 'http://imagecode.meiliwan.com/file/uploadImg?projectName=product&upfileElemName=Filedata&sizeRule=' + imageRule + "&showSize=100x100",
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
            queueID: 'J-add-five-image',
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
                var _con = '<img class="imgCube" width="" height="" main-pic="false" src="' + data.url + '" alt="" key="' + data.persistUri + '">';
                _con += '<div class="op-bar"><div class="con"><span class="ico left-arrow"></span><span class="ico right-arrow"></span><span class="set-main">设为主图</span></div><div class="mask"></div></div>';
                $('#' + file.id + '').append(_con).find('.data').css('color', '#11CDF1').fadeOut(2000).end().find('.uploadify-progress').fadeOut(2500);
            },
            onQueueComplete: function (queueData) {
                SetMainPic();
                var $imgQueue = $('#J-add-five-image div.uploadify-queue-item');
                if ($imgQueue.length > 5) {
                    $imgQueue.slice(5).remove();
                }
            }
        });
    });

    //验证是否上传5张商品主图
    function fiveImage() {
        var url = "";
        var provId = $("#provId").val();
        var $imgs = $("#J-add-five-image img.imgCube");
        var count = 0;
        if ($imgs.length > 0) { //如果图片为空
            for (var i = 0, _len = $imgs.length; i < _len; i++) {
                if ($imgs.eq(i).attr('main-pic') == 'false') {
                    url += $imgs.eq(i).attr('key') + ',';
                }
                else {
                    url = $imgs.eq(i).attr('key') + ',' + url;//拼接到字符串前面
                }
                count++;
            }
        }
        if(url != ''){
           $("#spv-image-"+provId).after('<span style="color: red" class="isComplete">(已上传'+count+'/5张)</span>');
        }
        url = provId + ";" + url;
        var vid = "image-" + provId;
        var $imageId = $("#" + vid);
        if ($imageId.length > 0) {
            $("#" + vid).val(url);
        } else {
            $("#J-imageUris").append('<input type="hidden" name="imageUrls"' + " id=" + vid + " value=" + url + ">");
        }
        return true;
    }

</script>
