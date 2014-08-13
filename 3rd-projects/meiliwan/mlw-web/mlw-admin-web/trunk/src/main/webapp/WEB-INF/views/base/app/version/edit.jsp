<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">基础配置项编辑</h2>

<div class="pageContent">
    <form action="/base/app/version/edit" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <input type="hidden" value="${config.sysConfigId}" name="sysConfigId">
        <input type="hidden" value="${config.createTime}" name="createTime">
        <input type="hidden" value="${config.updateTime}" name="updateTime">
        <input type="hidden" value="${config.isDel}" name="isDel">

        <input id="old_sysConfigCode" type="hidden" value="${config.sysConfigCode}" name="old_sysConfigCode">
    <div class="pageFormContent" layoutH="92">
    <fieldset>
        <legend>基础配置项编辑</legend>
        <dl>
            <dt>编码名称：</dt>
            <dd><input name="sysConfigName" class="required" type="text" value="${config.sysConfigName}" /></dd>
        </dl>
        <dl style="display: none">
            <dt>版本码(必须为数字)：</dt>
            <dd><input name="sysConfigCode" class="required" type="text" value="${config.sysConfigCode}" /></dd>
        </dl>
        <dl>
            <dt>版本名称：</dt>
            <dd><input name="versionName" class="required" type="text" value="${versionName}" /></dd>
        </dl>
        <dl>
            <dt>更新类型：</dt>
            <dd>
                <select name = "updateType" id="J_updateType" >
                    <option value="0" selected>普通更新</option>
                    <option value="1" selected>强制更新</option>
                </select>
            </dd>
        </dl>
       <%-- <dl>
            <dt>编码：</dt>
            <dd><input class="required" id="new_sysConfigCode" name="sysConfigCode" type="text" value="${config.sysConfigCode}" />
            </dd>
        </dl>--%>
       <%-- <dl>
            <dt>校验：</dt>
            <dd><button type="button" onclick="checkCode();">编码唯一性</button></dd>
        </dl>--%>
       <%-- <dl class="nowrap">
            <dt>编码值(版本码,版本名称,更新类型(0普通更新1强制更新))</dt>
            <textarea name="sysConfigValue" class="required textInput" cols="80" rows="2">${config.sysConfigValue}</textarea>
        </dl>--%>
        <dl class="nowrap">
            <dt>版本更新内容描述</dt>
            <dd>
                <textarea name="descp" cols="80" rows="2" class="textInput">${config.descp}</textarea>
            </dd>
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
    jQuery(document).ready(function(){
        $("#J_updateType").val('${updateType}');
    });
   /* function checkCode(){
        var  sysConfigCode = $("#new_sysConfigCode").val();
        var old_sysConfigCode = $("#old_sysConfigCode").val();
        if(sysConfigCode==old_sysConfigCode){
            alert("编码未改变，可以继续使用！");
        }else{
            $.post("/base/app/version/checkSysConfigCodeIsUsed",{sysConfigCode:sysConfigCode},function(data){
                var isUsed = data;
                if(data=="false")  {
                    alert("该编码可以使用。");
                }else {
                    alert("该编码被占用，请使用新的编码！");
                }
            });
        }
    }*/
</script>