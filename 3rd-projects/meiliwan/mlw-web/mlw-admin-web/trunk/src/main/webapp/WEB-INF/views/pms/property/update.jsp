<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<div class="pageContent" style="border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid;">
    <form method="post" action="/pms/property/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" name="proPropId" value="${pc.proPropId}">
        <input type="hidden" name="handle" value="1">
        <div class="pageFormContent nowrap" layoutH="58" id="update-property">
            <input type="hidden" id="u-SkuFlag" value="<c:if test="${pc.isSku == 1}">1</c:if><c:if test="${pc.isSku == 0}">0</c:if>">
            <div class="unit">
                <label>属性名称：</label>
                <input type="text" name="propertyName" id="u-propertyName" value="${pc.name}" <c:if test="${pc.isSku == 1}">maxlength="4"</c:if>/>
            </div>
            <div class="unit">
                <label>排序类型：</label>
                <select class="combox" name="sequence">
                    <option value="10" <c:if test="${pc.sequence == 10}">selected="selected"</c:if>>10</option>
                    <option value="9" <c:if test="${pc.sequence == 9}">selected="selected"</c:if>>9</option>
                    <option value="8" <c:if test="${pc.sequence == 8}">selected="selected"</c:if>>8</option>
                    <option value="7" <c:if test="${pc.sequence == 7}">selected="selected"</c:if>>7</option>
                    <option value="6" <c:if test="${pc.sequence == 6}">selected="selected"</c:if>>6</option>
                    <option value="5" <c:if test="${pc.sequence == 5}">selected="selected"</c:if>>5</option>
                    <option value="4" <c:if test="${pc.sequence == 4}">selected="selected"</c:if>>4</option>
                    <option value="3" <c:if test="${pc.sequence == 3}">selected="selected"</c:if>>3</option>
                    <option value="2" <c:if test="${pc.sequence == 2}">selected="selected"</c:if>>2</option>
                    <option value="1" <c:if test="${pc.sequence == 1}">selected="selected"</c:if>>1</option>
                    <option value="0" <c:if test="${pc.sequence == 0}">selected="selected"</c:if>>0</option>
                    <option value="-1" <c:if test="${pc.sequence == -1}">selected="selected"</c:if>>-1</option>
                    <option value="-2" <c:if test="${pc.sequence == -2}">selected="selected"</c:if>>-2</option>
                    <option value="-3" <c:if test="${pc.sequence == -3}">selected="selected"</c:if>>-3</option>
                    <option value="-4" <c:if test="${pc.sequence == -4}">selected="selected"</c:if>>-4</option>
                    <option value="-5" <c:if test="${pc.sequence == -5}">selected="selected"</c:if>>-5</option>
                    <option value="-6" <c:if test="${pc.sequence == -6}">selected="selected"</c:if>>-6</option>
                    <option value="-7" <c:if test="${pc.sequence == -7}">selected="selected"</c:if>>-7</option>
                    <option value="-8" <c:if test="${pc.sequence == -8}">selected="selected"</c:if>>-8</option>
                    <option value="-9" <c:if test="${pc.sequence == -9}">selected="selected"</c:if>>-9</option>
                    <option value="-10" <c:if test="${pc.sequence == -10}">selected="selected"</c:if>>-10</option>
                </select>
            </div>
            <div class="unit">
                <label>属性类型：</label>
                <label><input type="radio" name="propertyType" value="1" id="u-select-input" <c:if test="${pc.isSku == 1}">disabled="disabled"</c:if> <c:if test="${pc.propertyType == 1}">checked="true"</c:if>/>下拉框选择</label>
                <label><input type="radio" name="propertyType" value="2" id="u-checkbox-input" <c:if test="${pc.isSku == 1}">disabled="disabled"</c:if> <c:if test="${pc.propertyType == 2}">checked="true"</c:if>/>可以多选</label>
                <label><input type="radio" name="propertyType" value="3" id="u-text-input" <c:if test="${pc.isSku == 1}">disabled="disabled"</c:if> <c:if test="${pc.propertyType == 3}">checked="true"</c:if>/>文本输入</label>
            </div>
            <div class="unit">
                <label>是否必填：</label>
                <label><input type="radio" name="isRequired" value="1" <c:if test="${pc.isSku == 1}">disabled="disabled"</c:if> <c:if test="${pc.isRequired == 1}">checked="true" </c:if> id="u-fillter1">是</label>
                <label><input type="radio" name="isRequired" value="0" <c:if test="${pc.isSku == 1}">disabled="disabled"</c:if> <c:if test="${pc.isRequired == 0}">checked="true" </c:if>>否</label>
            </div>
            <c:if test="${pc.propertyType != 3}">
                <div class="unit" id="filter-opt">
                    <label>是否筛选：</label>
                    <label><input type="radio" name="isFilter" value="1" <c:if test="${pc.isSku == 1}">disabled="disabled"</c:if> <c:if test="${pc.isFilter == 1}">checked="true" </c:if> id="u-isRequired1" class="pIsSelect">是</label>
                    <label><input type="radio" name="isFilter" value="0" <c:if test="${pc.isSku == 1}">disabled="disabled"</c:if> <c:if test="${pc.isFilter == 0}">checked="true" </c:if> class="pIsSelect" >否</label>
                </div>
                <div class="unit" id="J-children">
                    <fieldset>
                        <legend>子项值</legend>
                        <c:if test="${!empty pc.proProValue}">
                            <c:forEach var="list" items="${pc.proProValue}">
                                <dl>
                                    <dd><input type="hidden" name="valueIds" value="${list.id}"><input name="childValues" <c:if test="${pc.isSku == 1}">maxlength="4"</c:if> type="text" value="${list.name}"/></dd>
                                </dl>
                            </c:forEach>
                        </c:if>
                        <div  id="add-chideren"><a class="button" href="javascript:;"><span>添加子项值</span></a></div>
                    </fieldset>
                </div>
            </c:if>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
            </ul>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        $("#add-chideren").die("click").live("click",function(){
            var flag = $("#u-SkuFlag").val();
            if(flag == 1){
                $(this).before('<dl><dd><input type="hidden" name="valueIds" value="0"><input name="childValues" maxlength="4" type="text" /></dd></dl>');
            }else{
                $(this).before('<dl><dd><input type="hidden" name="valueIds" value="0"><input name="childValues" type="text" /></dd></dl>');
            }
        });

        var addHtml='<div class="unit" id="filter-opt"><label>是否筛选：</label><label><input type="radio" name="isFilter" value="0" checked="true"/>否</label><label><input type="radio" name="isFilter" value="1"/>是</label></div><div class="unit" id="J-children"><fieldset><legend>子项值</legend><div  id="add-chideren"><a class="button" href="javascript:;"><span>添加子项值</span></a></div></fieldset></div>';
        var index = 0;
        var isHide = 0;
        var type = ${pc.propertyType};
        $("#u-checkbox-input").die("click").live("click",function(){
            if(index <= 0 && type > 2){
                $("#update-property").append(addHtml);
                index++
            }
            if(isHide == 1){
                $("#filter-opt").show();
                $("#J-children").show();
            }
        });
        $("#u-select-input").die("click").live("click",function(){
            if(index <= 0 && type > 2){
                $("#update-property").append(addHtml);
                index++
            }
            if(isHide == 1){
                $("#filter-opt").show();
                $("#J-children").show();
            }
        });

        $("#u-text-input").die("click").live("click",function(){
            $("#filter-opt").hide();
            $("#J-children").hide();
            isHide = 1;
        });
    });
</script>