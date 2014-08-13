<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>帮助中心-${content.infoName}</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/common.css">
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/module.css">
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/ucenter.css">
    <script src="http://www.meiliwan.com/js/jquery-1.8.3.min.js"></script>
    <script src="http://www.meiliwan.com/js/mlw.core.js"></script>
    <script src="http://www.meiliwan.com/js/jquery.mlw.plugin.js"></script>
    <script src="http://www.meiliwan.com/js/cart/cart.js"></script>
    <script src="http://www.meiliwan.com/js/mlw.dialogbox.js"></script>
</head>
<body class="uCenter w990">
<!--topbar-->
<div class="topbar" id="J-topbar" id="J_topbar">
</div>
<!--header-->
<div class="header" id="J_search">
</div>
<!--导航-->
<div class="navbar">
    <div class="cart"><a href="http://www.meiliwan.com/cat/allcategory.html" class="n_all">全部分类</a>
        <div class="snavP" id="J_navbar">
        </div>
    </div>
    <div class="nav-list" id="J_Nav_List">
    </div>
    <div class="cart-info" id="J_Cart">
    </div>
</div>

<!--uCenter-->
<div class="area gap-pt10">
    <div class="breadcrumb">
        <strong><a href="/base/content/notice?item=${item.infoItemId}">${item.infoItemName}</a></strong>
        <span>&nbsp;&gt;&nbsp;<a href="javascript:void(0); ">${content.infoName}</a></span>
    </div>
        <div class="help-detail">
            <div class="tit2"><b>${content.infoName}</b><p><fmt:formatDate value="${content.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></p></div>
            <div class="help-content">
                ${content.infoContent}
            </div>
        </div>

</div>
<!--footer-->
<div class="footer" id="J_Footer">
</div>
<div align="center" style="display: block; position: fixed; top: 100px; right: 20px; z-index: 8888; width: 30px; height: 190px; font-size: 16px; color: red;" id="saveBtn">
    <c:if test='${sessionScope.bkstage_user.menus["237"]!=null}'>
        <div style="background-color: gray;"><a  href="javascript:void(0);" id="pub-zixun" key="${content.infoId}"><br>发<br>布</a></div>
    </c:if>
    <div style="background-color: gray; height: 70px;"><a onclick="window.close();" href="javascript:void(0);"><br>关<br>闭</a></div>
</div>
</body>
<script>
    $(document).ready(function () {
        $("#pub-zixun").click(function () {
            var infoId = $(this).attr("key");
            $.post("/base/infocontent/publish", {infoId: infoId}, function (data) {
                if (data.statusCode == 200) {
                    alert('发布成功');
                } else {
                    alert('发布失败');
                }
            })
        });
        MLW.initIncResource();
   });
</script>
</html>