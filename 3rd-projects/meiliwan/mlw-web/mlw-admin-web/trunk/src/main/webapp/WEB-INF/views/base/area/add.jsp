<%--
  Created by Sean on 13-6-5.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">地域管理添加</h2>

<div class="pageContent">
    <form id="addForm" action="/base/area/add" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" value="1" name="handle">
    <input type="hidden" value="1" id="addParentCode" name="parentCode">
    <input type="hidden" value="1" id="isLastGrade" name="isLastGrade">
    <div class="pageFormContent" layoutH="97">
    <fieldset>
        <legend>地域添加</legend>
        <dl>
            <dt>地区级别：</dt>
            <dd>
                <select id="add_areaGrade" name="areaGrade">
                    <option value="0">国家</option>
                    <option value="1">省/直辖市</option>
                    <option value="2">市</option>
                    <option value="3">区/县</option>
                    <option value="4">街道/乡镇</option>
                </select>
            </dd>
        </dl>
        <dl id="del_country" style="display:none">
            <dt>上级国家：</dt>
            <dd>
                <select id="sel_country" name="sel_country">
                    <option value="0">请选国家</option>
                    <c:forEach var="e" items="${countryList}">
                        <option value="${e.areaCode}">${e.areaName}</option>
                    </c:forEach>
                </select>
            </dd>
        </dl>
        <dl id="del_province" style="display:none">
            <dt>上级省：</dt>
            <dd>
                <select id="sel_province" name="sel_province">
                    <option value="0">请选省</option>
                </select>
            </dd>
        </dl>
        <dl id="del_city" style="display:none">
            <dt>上级市：</dt>
            <dd>
                <select id="sel_city" name="sel_city">
                    <option value="0">请选市</option>
                </select>
            </dd>
        </dl>
        <dl id="del_county" style="display:none">
            <dt>上级区/县：</dt>
            <dd>
                <select id="sel_county" name="sel_county">
                    <option value="0">请选区/县</option>
                </select>
            </dd>
        </dl>
        <%--<dl>
            <dt>地区编码：</dt>
            <dd>
                <input name="areaCode"  class="required"  type="text" />
            </dd>
        </dl>--%>
        <%--<dl>
            <dt>上级编码：</dt>
            <dd><input name="parentCode" class="required" type="text" /></dd>
        </dl>--%>
        <dl>
            <dt>地区名称：</dt>
            <dd><input class="required" name="areaName" type="text" /></dd>
        </dl>
        <%--<dl>
            <dt>是否末级：</dt>
            <dd><input name="isLastGrade" type="text" /></dd>
        </dl>--%>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="setParentCode();">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>
<script>
    $(document).ready(function(){
            $("#sel_country").change(function(){
                // var level = $("#sel_country").val();
                var parentCode = $("#sel_country").val();
                if(parentCode!="0"){
                    $.post("/base/area/getAreaByParentCode",{parentCode:parentCode},function(data){
                        $("#sel_province").html("<option value='0'>请选择省</option>"+data);
                    });
                }
            });
        $("#sel_province").change(function(){
            var parentCode = $("#sel_province").val();
            if(parentCode!="0"){
                $.post("/base/area/getAreaByParentCode",{parentCode:parentCode},function(data){
                    $("#sel_city").html("<option value='0'>请选择市</option>"+data);
                });
            }
        });

        $("#sel_city").change(function(){
            var parentCode = $("#sel_city").val();
            if(parentCode!="0"){
                $.post("/base/area/getAreaByParentCode",{parentCode:parentCode},function(data){
                    $("#sel_county").html("<option value='0'>请选择区/县</option>"+data);
                });
            }
        });

        //根据地域级别控制（国家 省 市 区/县下拉的显示）
        $("#add_areaGrade").change(function(){
            var areaGrade = $("#add_areaGrade").val();
            if(areaGrade=="0"){
                $("#del_country").hide();
                $("#del_province").hide();
                $("#del_city").hide();
                $("#del_county").hide();
            }
            if(areaGrade=="1"){
                $("#del_country").show();
                $("#del_province").hide();
                $("#del_city").hide();
                $("#del_county").hide();
            }
            if(areaGrade=="2"){
                $("#del_country").show();
                $("#del_province").show();
                $("#del_city").hide();
                $("#del_county").hide();
            }
            if(areaGrade=="3"){
                $("#del_country").show();
                $("#del_province").show();
                $("#del_city").show();
                $("#del_county").hide();
            }
            if(areaGrade=="4"){
                $("#del_country").show();
                $("#del_province").show();
                $("#del_city").show();
                $("#del_county").show();
            }
        });
    });

    /*
     设置上级编码
     */
    function setParentCode(){
        var areaGrade = $("#add_areaGrade").val();
        var parentCode=0;
        if(areaGrade=="0"){
            parentCode = 10;
        }
        if(areaGrade=="1"){
            parentCode =  $("#sel_country").val();
        }
        if(areaGrade=="2"){
            parentCode =  $("#sel_province").val();
        }
        if(areaGrade=="3"){
            parentCode =  $("#sel_city").val();
        }
        if(areaGrade=="4"){
            parentCode =  $("#sel_county").val();
        }

        if(parentCode==0){
            alert("请选择相应上级地地域");
        }else{
            $("#addParentCode").val(parentCode) ;
            $("#addForm").submit();
        }
    }
</script>