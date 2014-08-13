<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">基础配置项添加</h2>

<div class="pageContent">
    <form action="/base/config/add" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" value="1" name="handle">
    <div class="pageFormContent" layoutH="97">
    <fieldset>
        <legend>基础配置项添加</legend>

        <dl>
            <dt>编码名称：</dt>
            <dd><input name="sysConfigName" class="required" type="text" /></dd>
        </dl>
        <dl>
            <dt>编码：</dt>
            <dd><input id="new_sysConfigCode" class="required" name="sysConfigCode" type="text" /></dd>
        </dl>
        <dl>
            <dt>校验：</dt>
            <dd><button type="button" onclick="checkCode();">编码唯一性</button></dd>
        </dl>
        <dl>
            <dt>编码值：</dt>
            <dd><input name="sysConfigValue" type="text" class="required"/></dd>
        </dl>
        <dl>
            <dt>描述：</dt>
            <dd><input name="descp" type="text" class="required" /></dd>
        </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>
<script>
    function checkCode(){
        var  sysConfigCode = $("#new_sysConfigCode").val();
        $.post("/base/config/checkSysConfigCodeIsUsed",{sysConfigCode:sysConfigCode},function(data){
            var isUsed = data;
            if(data=="false")  {
                alert("该编码可以使用。");
            }else {
                alert("该编码被占用，请使用新的编码！");
            }
        });
    }
</script>