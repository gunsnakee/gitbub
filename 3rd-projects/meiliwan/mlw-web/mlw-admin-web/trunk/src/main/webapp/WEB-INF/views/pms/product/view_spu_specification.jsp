<%@ page language="java" pageEncoding="UTF-8" %>
<fieldset>
    <legend>规格 <span style="color: red">公共信息一旦创建后规格不可修改(参数可以),请谨慎选择</span></legend>
             <span name="specification" class="view">
                <table border="1" width="80%">
                    <c:forEach var="proProperty" items="${proPropAllList}">
                        <c:if test="${proProperty.isSku==1}">
                            <tr>
                                <td width="10%">${proProperty.name}</td>
                                <td width="10%">
                                    <c:choose>
                                        <c:when test="${proProperty.isImage==1}">
                                            <input type="checkbox"  value="1" checked disabled/> 异图
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox"  value="1"  disabled/> 异图
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:forEach var="proProValue" items="${proProperty.proProValue}">
                                        <c:if test="${proProValue.fill==1}">
                                            ${proProValue.name}&nbsp;&nbsp;&nbsp;&nbsp;
                                        </c:if>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>

            </span>

            <span name="specification" class="input">
                <table border="1" width="80%">
                    <c:forEach var="proProperty" items="${proPropAllList}">
                        <c:if test="${proProperty.isSku==1}">

                            <tr>
                                <input type="hidden" name="specProIsSku" value="${proProperty.isSku}"/>
                                <input type="hidden" name="specProPropertyId" value="${proProperty.id}"/>
                                <input type="hidden" name="specProProValue" refId="${proProperty.id}"/>
                                <td width="10%">${proProperty.name}</td>
                                <td width="10%">
                                    <input type="hidden" name="specIsImage" value="${proProperty.isImage}"/>
                                    <c:choose>
                                        <c:when test="${proProperty.isImage==1}">
                                            <input type="checkbox"  checked disabled/> 异图
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox"  disabled/> 异图
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:forEach var="proProValue" items="${proProperty.proProValue}">
                                        <c:set var="checkedD" value=""/>
                                        <c:if test="${proProValue.fill==1}">
                                            <c:set var="checkedProVidsList" value="${checkedProVidsList}${proProValue.id}"/>
                                            <c:set var="checkedD" value="checked"/>
                                        </c:if>
                                        <input type="checkbox" name="specProProValueCheck" value="${proProValue.id}" refId="${proProperty.id}" ${checkedD} />
                                        ${proProValue.name}
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </span>

            <c:if test='${sessionScope.bkstage_user.menus["f6f03aa4654220af64642d5aa03965ed"]!=null}'>
                <span name="specification" class="view"><input  type="button"  name="modify" value="修改"  ref="specification"/></span>
            </c:if>
            <span name="specification" class="input">
                <input type="button" name="saveSubmit"  value="保存" submitType="specification"/>
                <input type="button"  name="cancel" value="取消" ref="specification"/>
            </span>
</fieldset>
<script>
    function specSubmit(){

        var checkedLengths = [];
        $("input[type='hidden'][name='specProProValue']").each(function(){
            var proId = $(this).attr("refId");
            var values = [];
            $("input[name='specProProValueCheck'][refId='"+proId+"']").each(function(){
                if($(this).is(':checked')){
                    values.push($(this).val());
                }
            });
            checkedLengths.push(values.length);
            $(this).val(values.join());
        });

        for(var i=0;i<checkedLengths.length;i++){
            if(checkedLengths[i]>15){
                alert("规则属性值不能超过15个");
                return false;
            }
            if(checkedLengths[i] < 1){
                alertMsg.info("规则属性值必须选择，不能出现不选sku属性值情况");
                return false;
            }
        }


        $("#submitType").val("specification");
        $("#updare-spu").submit();


    }
</script>