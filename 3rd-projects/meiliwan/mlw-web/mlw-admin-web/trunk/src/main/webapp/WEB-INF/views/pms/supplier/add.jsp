<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">添加供应商</h2>

<div class="pageContent">
    <form action="/pms/supplier/add" method="post" id="addSupplierFrom" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="92">
        <fieldset>
        <legend>添加供应商</legend>
        <dl>
            <dt>供应商名称：</dt>
            <dd><input name="name" id="supplierName"  class="required"  type="text" minlength="1" maxlength="50" /></dd>
        </dl>
        <dl>
            <dt>联系人：</dt>
            <dd><input name="linkman" type="text"  /></dd>
        </dl>
        <dl>
            <dt>联系电话：</dt>
            <dd><input name="phone"  class="phone" type="text"  maxlength="17"/></dd>
        </dl>
        <dl>
            <dt>其他电话：</dt>
            <dd><input name="otherPhone" class="phone" type="text"  maxlength="17"/></dd>
        </dl>
        <dl>
            <dt>经营方式：</dt>
            <dd>
                <select name="operateType" class="required">
                    <option value="1">购销</option>
                    <option value="2">代销</option>
                    <option value="3">联营</option>
                </select>
            </dd>
        </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="button" id="addBtn">添加</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>
<script>
    $(document).ready(function(){
        $("#addBtn").click(function(){
            var name = $("#supplierName").val();
            $.post("/pms/supplier/add-re",{name:name},function(data){
                if (data=='1'){
                    alert("存在名字相同的供应商，请标异！");
                }else{
                    $("#addSupplierFrom").submit();
                }
            });
        });
    });
</script>