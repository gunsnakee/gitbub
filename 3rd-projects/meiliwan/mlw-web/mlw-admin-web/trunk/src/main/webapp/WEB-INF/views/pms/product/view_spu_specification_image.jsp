<%@ page language="java" pageEncoding="UTF-8" %>
<fieldset>
    <legend>规格图片</legend>
    <input type="hidden" name="imageUrlsSpu" id="imageUrlsSpu">

    <c:if test="${!empty unImageValueList && fn:length(unImageValueList)>0}">
        <fieldset>
            <legend><span style="color: red">系统检查到您的规则值有变化,请重新检查图片并保存</span></legend>
            <dl>
                <c:forEach var="unImageValue" items="${unImageValueList}">
                    <dd style="width: 10%">
                        <a target="dialog" class="button" height="350" width="1000" href="/pms/product/spu-view?viewFlag=1&proVid=${unImageValue.id}&spuId=${spu.spuId}&proPid=${unImageValue.proPropId}"><span>上传${unImageValue.name}图片</span> </a>
                    </dd>
                </c:forEach>
            </dl>
        </fieldset>
    </c:if>

    <span name="specification_image" class="view">
        <c:forEach var="proImage" items="${spu.imageses}">
            <fieldset style="width: 700px">
            <dl>
                <dd style="width: 600px;">
                    <img src="${proImage.defaultImageUri}100x100.jpg" name="imageUrls"   proPid="${proImage.proPropId}" proVid="${proImage.proProvId}" width="100" height="100" />

                <c:set var="imageUrls" value="${fn:split(proImage.imageUris,',')}"></c:set>

                <c:forEach var="imageUrl" items="${imageUrls}">
                    <c:if test="${imageUrl!=proImage.defaultImageUri}">
                            <img src="${imageUrl}100x100.jpg" name="imageUrls"   proPid="${proImage.proPropId}" proVid="${proImage.proProvId}" width="100" height="100" />
                    </c:if>
                </c:forEach>
                </dd>
                <dd style="width: 100px;">
                    <c:if test='${sessionScope.bkstage_user.menus["f6f03aa4654220af64642d5aa03965ed"]!=null}'>
                        <a target="dialog" class="button" height="350" width="1000" href="/pms/product/spu-view?viewFlag=1&proVid=${proImage.proProvId}&spuId=${spu.spuId}&proPid=${proImage.proPropId}"><span>修改</span> </a>
                    </c:if>
                </dd>

            </dl>
            </fieldset>
        </c:forEach>
    </span>
</fieldset>
<script>
    $(function(){
        $("#reCheck").click(function(e){
            e.preventDefault();
            return navTabSearch($("#updare-spu").get(0));
        });

    });
</script>