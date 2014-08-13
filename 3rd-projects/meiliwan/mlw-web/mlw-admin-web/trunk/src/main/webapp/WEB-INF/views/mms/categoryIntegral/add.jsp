<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">类目积分添加</h2>

<div class="pageContent">
    <form id="categoryIntegralform"  action="/mms/categoryIntegral/add" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" value="1" name="handle">
    <input id="categoryName" type="hidden" name="categoryName">
    <div class="pageFormContent" layoutH="97">
    <fieldset>
        <legend>类目积分添加</legend>

        <dl>
            <dt>一级类目：</dt>
            <dd>
                <select id="add_first" name="firstCategoryId">
                <option value="0">全部</option>
                <c:forEach var="e" items="${categoryList}">
                    <option value="${e.categoryId}">${e.categoryName}</option>
                </c:forEach>
                </select>
            </dd>
        </dl>
        <dl>
            <dt>二级类目：</dt>
            <dd>
                <select id="add_second" name="secondCategoryId" style="width: 120px;">
                    <option value="-1">全部</option>
                </select>
            </dd>
        </dl>
        <dl>
            <dt>三级类目：</dt>
            <dd>
                <select id="add_third" name="categoryId" style="width: 120px;">
                    <option value="-1">全部</option>
                </select>
            </dd>
        </dl>
        <dl>
            <dt>积分：</dt>
            <dd><input name="ratio" type="text" class="required" min="1" max="100" />%</dd>
        </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button id="save" type="button">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>

<script>
    $(document).ready(function(){
        $("#add_first").change(function(){
            var level = $("#add_first").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#add_second").html('<option value="-1">全部</option>'+data);
            });
            $("#add_third").html('<option value="-1">全部</option>');
        });

        $("#add_second").change(function(){
            var level = $("#add_second").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#add_third").html('<option value="-1">全部</option>'+data);
            });
        });

        $("#save").click(function(){
            var third = $("#add_third").val();
            var thirdValue = $("#add_third").find('option:selected').text();
            if(third < 0){
                alert("请选择三级类目！");
                return;
            }else{
                var categoryId = $("#add_third").val();
                $.post("/mms/categoryIntegral/categoryIsExisted",{categoryId:categoryId},function(data){
                    isUsed = data;
                    if(data=="true")  {
                        alert("该三级类目已经设置积分！");
                    }else{
                        $("#categoryName").val(thirdValue) ;
                        $("#categoryIntegralform").submit();
                    }
                });
            }
        });
    });

</script>