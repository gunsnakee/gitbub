<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>帮助中心-<#if item?exists>${item.infoItemName}</#if></title>
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/com.css">
    <#--<link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/module.css">-->
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/ucenter.css">
    <#include "/WEB-INF/freemarker/inc/site_pub_js.ftl">
   <#-- <script src="http://www.meiliwan.com/js/mlw.dialogbox.js"></script>-->
</head>
<body class="uCenter w990">
<!--topbar-->
<#include "topper/topper.ftl">
<#assign mlw=JspTaglibs["/WEB-INF/tld/mlw-web-resource.tld"]>
<!--header-->
<div class="header"  data-value="${keyword}" id="J_search">
    <@mlw.getCmsContent pageName="search"/>
</div>
<!--导航-->
<#--<div class="navbar">-->
<#--<div class="cart"><a href="http://www.meiliwan.com/cat/allcategory.html" class="n_all">全部分类</a>-->
<#--<div class="snavP" >-->
<#--<@mlw.getCmsContent pageName="category"/>-->
<#--</div>-->
<#--</div>-->
<#--<#include "nav_list/nav_list.ftl">-->
<#--<#include "cart_div/cart.ftl">-->
<#--</div>-->
<!--breadcrumb-->
<div class="area">
    <div class="breadcrumb">
        <#if item?exists>
            <strong><a href="http://www.meiliwan.com/base/content/notice?itemId=${item.infoItemId?c}">${item.infoItemName}</a></strong>
        </#if>
    </div>
</div>
<!--uCenter-->
<div class="area">
        <div class="help-sales">
            <#if pc.entityList?exists>
                <ul class="hdashed">
                    <#list pc.entityList as list>
                        <#if (list_index%2 == 0)>
                            <li><span class="fr">${list.publishTime?string('yyyy-MM-dd HH:mm:ss')}</span><a href="http://www.meiliwan.com/${item.fileName}/${list.infoId}.html">${list.infoName}</a></li>
                        </#if>
                    </#list>
                </ul>
                <ul>
                    <#list pc.entityList as list>
                        <#if (list_index%2 == 1)>
                            <li><span class="fr">${list.publishTime?string('yyyy-MM-dd HH:mm:ss')}</span><a href="http://www.meiliwan.com/${item.fileName}/${list.infoId?c}.html">${list.infoName}</a></li>
                        </#if>
                    </#list>
                </ul>
            <#else >
                <div style="text-align: center">暂无信息</div>
            </#if>
        </div>
</div>

<#if item?exists>
    <div class="area">
        <div class="page">
            <div class="inner">
                <#if 1!=pc.pageInfo.page><a href="http://www.meiliwan.com/base/content/notice?itemId=${item.infoItemId}&pageNum=1">首页</a></#if>
                <#if 0!=pc.pageInfo.isPrev><a href="http://www.meiliwan.com/base/content/notice?itemId=${item.infoItemId}&pageNum=${pc.pageInfo.isPrev}">上一页</a></#if>
                <#list pc.pageInfo.listPages as row>
                    <#if row==pc.pageInfo.page><a class="on" href="http://www.meiliwan.com/base/content/notice?itemId=${item.infoItemId}&pageNum=${row}">${row}</a></#if>
                    <#if row!=pc.pageInfo.page><a href="http://www.meiliwan.com/base/content/notice?itemId=${item.infoItemId}&pageNum=${row}">${row}</a></#if>
                    <#if row_index gt 3>
                        <#break >
                    </#if>
                </#list>
                <#if pc.pageInfo.allPage gt 5 && pc.pageInfo.allPage!=pc.pageInfo.page>
                    <#if pc.pageInfo.allPage-1 != pc.pageInfo.page>
                        <span class="slh">...</span>
                    </#if>
                    <a href="http://www.meiliwan.com/base/content/notice?itemId=${item.infoItemId}&pageNum=${pc.pageInfo.allPage}">${pc.pageInfo.allPage}</a>
                </#if>
                <#if  0!=pc.pageInfo.isNext><a class="next" href="http://www.meiliwan.com/base/content/notice?itemId=${item.infoItemId}&pageNum=${pc.pageInfo.isNext}">下一页</a></#if>
                <i>到第</i><input type="text" class="jump-to" id="jump-to" value="${pc.pageInfo.page}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><i>页</i>
                <input type="submit" value="确定" class="jump" id="gotoPage" totalPage="${pc.pageInfo.allPage}" item="${item.infoItemId}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
            </div>
        </div>
    </div>
</#if>
<!--footer-->
<#include "footer/footer.ftl">
</body>
<script type="text/javascript">
   $(document).ready(function(){
       //MLW.initIncResource();

       $("#gotoPage").click(function(){
           var page = $("#jump-to").val();
           var totalPage = $(this).attr("totalPage");
           var itemId = $(this).attr("item");
           if($.trim(page)==""){
               page=1;
           }
           if(eval(page)>eval(totalPage)){
               page = totalPage;
           }
           if(eval(page)<=0){
               page=1;
           }
           window.location.href= "http://www.meiliwan.com/base/content/notice?itemId="+itemId+"&pageNum="+page;
       });
   });
</script>
</html>