<%@ page language="java" pageEncoding="UTF-8" %>
<fieldset>
    <legend>类目属性</legend>
            <span name="categoryPro" class="view">
                <table border="1" width="80%">
                    <c:forEach var="pp" items="${proPropAllList}">
                        <c:if test="${pp.isSku!=1 && pp.checkSku!=1}">
                            <tr>
                                <td width="10%">${pp.name}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${pp.propertyType == 1}">
                                            <c:forEach var="proProValue" items="${pp.proProValue}">
                                                <c:if test="${proProValue.fill==1}">
                                                    ${proProValue.name}&nbsp;&nbsp;&nbsp;&nbsp;
                                                </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${pp.propertyType == 2}">
                                            <c:forEach var="proProValue" items="${pp.proProValue}">
                                                <c:if test="${proProValue.fill==1}">
                                                    ${proProValue.name}&nbsp;&nbsp;&nbsp;&nbsp;
                                                </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            ${pp.descp}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>

            </span>

            <span name="categoryPro" class="input">
                <table border="1" width="80%">
                    <c:forEach var="pp" items="${proPropAllList}">
                        <c:if test="${pp.isSku!=1 && pp.checkSku!=1}">
                            <tr>
                                <input type="hidden" name="cateProIsSku" value="${pp.isSku}"/>
                                <input type="hidden" name="cateIsImage" value="${pp.isSku}"/>
                                <input type="hidden" name="cateProPropertyId" value="${pp.id}"/>
                                <input type="hidden" name="cateProPropertyType" value="${pp.propertyType}"/>
                                <input type="hidden" name="cateProProValue" refId="${pp.id}" refType="${pp.propertyType}"/>

                                <td width="10%">${pp.name}</td>
                                <td>

                                    <c:choose>
                                        <c:when test="${pp.propertyType == 1}">
                                            <select name="cateProProValueCheck" refId="${pp.id}">
                                                <c:forEach var="proProValue" items="${pp.proProValue}">
                                                    <option value="${proProValue.id}"   <c:if test="${proProValue.fill==1}">selected="selected" </c:if>>${proProValue.name}</option>
                                                </c:forEach>
                                            </select>
                                        </c:when>
                                        <c:when test="${pp.propertyType == 2}">
                                            <c:forEach var="proProValue" items="${pp.proProValue}">
                                                <input type="checkbox" name="cateProProValueCheck" value="${proProValue.id}" refId="${pp.id}" <c:if test="${proProValue.fill==1}">checked</c:if> />
                                                ${proProValue.name}
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" name="cateProProValueCheck" refId="${pp.id}" value="${pp.descp}" size="90" >
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </span>

    <c:if test='${sessionScope.bkstage_user.menus["f6f03aa4654220af64642d5aa03965ed"]!=null}'>
        <span name="categoryPro" class="view"><input  type="button"  name="modify" value="修改"  ref="categoryPro"/></span>
    </c:if>
            <span name="categoryPro" class="input">
                <input type="button" name="saveSubmit"  value="保存" submitType="categoryPro"/>
                <input type="button"  name="cancel" value="取消" ref="categoryPro"/>
            </span>
</fieldset>
<script>
    function buildCateProProValue(){
        $("input[type='hidden'][name='cateProProValue']").each(function(){
            var proId = $(this).attr("refId");
            var proType = $(this).attr("refType");

            if( proType == 1){
                console.log("1111 === "+$("select[name='cateProProValueCheck'][refId='"+proId+"'] option:selected").val());
                $(this).val($("select[name='cateProProValueCheck'][refId='"+proId+"'] option:selected").val());
            }else if(proType == 2){
                var values = [];
                var idx=0;
                $("input[type='checkbox'][name='cateProProValueCheck'][refId='"+proId+"']").each(function(){
                    if($(this).is(':checked')){
                        values[idx++]=$(this).val();
                    }
                });
                $(this).val(values.join());
            }else{
                $(this).val($("input[type='text'][name='cateProProValueCheck'][refId='"+proId+"']").val());
            }
        });
    }
</script>