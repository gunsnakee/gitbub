<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>



<div class="page unitBox">
    <div class="pageFormContent nowrap">
        <div class="divider"></div>
        <fieldset>
            <legend><span style="color: #ff0000">公共信息</span></legend>
            <dl>
                <input type="hidden" id="J-spuId" value="${spu.spuId}">
                <dt>SPU ID:</dt>
                <dd>${spu.spuId}</dd>
            </dl>
            <dl>
                <dt>类目:</dt>
                <dd><span>${firstPc.categoryName} &lt;  ${secondPc.categoryName}  &lt;  ${thirdPc.categoryName}</span></dd>
            </dl>
            <dl>
                <dt>商品标题:</dt>
                <dd>${spu.proName}</dd>
            </dl>
        </fieldset>
        <div class="divider"></div>
        <fieldset class="goodsAttr">
            <legend>规格</legend>
            <c:if test="${empty properties}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无规格属性数据</font></td></tr>
            </c:if>
            <c:if test="${!empty properties}">
                <c:forEach var="e" varStatus="i" items="${properties}">
                    <dl>
                        <dt>${e.name}<c:if test="${e.isImage == 1}">(异图)</c:if></dt>
                        <dd>
                            <c:if test="${!empty e.proProValue}">
                                <c:forEach var="spv" varStatus="i" items="${e.proProValue}">
                                    ${spv.name};
                                </c:forEach>
                            </c:if>
                        </dd>
                    </dl>
                </c:forEach>
            </c:if>
        </fieldset>
        <div class="divider"></div>
        <c:choose>
            <c:when test="${skuFlag == 0}">
                <fieldset>
                    <legend>填写参数表，就是生成商品</legend>
                    <dl>
                        <dt>生成商品</dt>
                        <c:if test="${empty sku}">
                            <dd>
                                <a href="/pms/product/add?step=8&spuId=${spu.spuId}" id="sku-add-0-0" target="dialog" width="550" height="200"><span style="color: #1727ff">生成商品</span></a>
                            </dd>
                        </c:if>
                        <c:if test="${!empty sku}">
                            <dd>
                                <c:if test="${sku.state == 0}">
                                    <a href="/pms/product/update?proId=${sku.proId}" target="dialog" width="900" height="600">
                                        <span style="color: #1727ff">商品ID:${sku.proId}</span>
                                    </a>
                                    <br><span id="${sku.proId}">当前状态:未编辑完成</span>
                                </c:if>
                                <c:if test="${sku.state != 0}">
                                    <a href="javascript:void(0);" class="viewDetail" data-key="/pms/product/product-detail?proId=${sku.proId}">
                                        <span style="color: #1727ff">商品ID:${sku.proId}</span>
                                    </a>
                                    <br><span id="${sku.proId}">当前状态:<c:if test="${sku.state == -1}">已删除</c:if><c:if test="${sku.state == 1}">上架</c:if><c:if test="${sku.state == 2}">下架</c:if></span>
                                </c:if>
                            </dd>
                        </c:if>
                    </dl>
                </fieldset>
            </c:when>
            <c:when test="${skuFlag == 1}">
                <fieldset>
                    <legend>填写参数表，就是生成商品</legend>
                    <table class="table spuTable" width="1000" layoutH="420">
                        <c:forEach var="pp" varStatus="i" items="${properties}">
                            <thead>
                            <tr>
                                <th align="center">${pp.name}/无</th>
                                <th align="center">无</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="ppv" varStatus="j" items="${pp.proProValue}">
                                <tr>
                                    <td align="center">${ppv.name}</td>
                                    <c:set var="propKey" value="0${ppv.id}"/>
                                    <c:choose>
                                        <c:when test="${!empty skuMap}">
                                            <c:choose>
                                                <c:when test="${!empty skuMap[propKey]}">
                                                    <td align="center">
                                                        <c:if test="${skuMap[propKey].state != 0}">
                                                            <a href="javascript:void(0);" class="viewDetail" data-key="/pms/product/product-detail?proId=${skuMap[propKey].proId}">
                                                                <span style="color: #1727ff">商品ID:${skuMap[propKey].proId}</span>
                                                            </a>
                                                            <br/><span id="${skuMap[propKey].proId}">当前状态:</span><c:if test="${skuMap[propKey].state == -1}">已删除</c:if><c:if test="${skuMap[propKey].state == 1}">上架</c:if><c:if test="${skuMap[propKey].state == 2}">下架</c:if></span>
                                                        </c:if>
                                                        <c:if test="${skuMap[propKey].state == 0}">
                                                            <a href="/pms/product/update?proId=${skuMap[propKey].proId}&skuDetail=${pp.name}:${ppv.name}" target="dialog" width="900" height="600">
                                                                <span style="color: #1727ff">商品ID:${skuMap[propKey].proId}</span>
                                                            </a>
                                                            <br><span id="${skuMap[propKey].proId}">当前状态:未编辑完成</span>
                                                        </c:if>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td align="center"><a href="/pms/product/add?step=8&spuId=${spu.spuId}&skuProp2=${ppv.proPropId}&skuProv2=${ppv.id}&skuName=${ppv.name}&skuDetail=${pp.name}:${ppv.name}" id="sku-add-0-${ppv.id}" target="dialog" width="550" height="200"><span style="color: #1727ff">生成商品</span></a></td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <td align="center"><a href="/pms/product/add?step=8&spuId=${spu.spuId}&skuProp2=${ppv.proPropId}&skuProv2=${ppv.id}&skuName=${ppv.name}&skuDetail=${pp.name}:${ppv.name}" id="sku-add-0-${ppv.id}" target="dialog" width="550" height="200"><span style="color: #1727ff">生成商品</span></a></td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </c:forEach>
                    </table>
                </fieldset>
            </c:when>
            <c:otherwise>
                <fieldset>
                    <legend>填写参数表，就是生成商品</legend>
                    <table class="table" width="1000" layoutH="420">
                        <thead>
                            <tr>
                                <th align="center">${properties[0].name}/${properties[1].name}</th>
                                <c:forEach var="ppv" varStatus="i" items="${properties[0].proProValue}">
                                    <th align="center">${ppv.name}</th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ppa" varStatus="j" items="${properties[1].proProValue}">
                                <tr>
                                    <td style="text-align: center">${ppa.name}</td>
                                    <c:forEach var="ppb" varStatus="k" items="${properties[0].proProValue}">
                                        <c:set var="propKey" value="${ppb.id}${ppa.id}"/>
                                        <c:set var="propKeyb" value="${ppa.id}${ppb.id}"/>
                                        <c:choose>
                                            <c:when test="${!empty skuMap}">
                                                <c:choose>
                                                    <c:when test="${!empty skuMap[propKey]}">
                                                        <td align="center">
                                                            <c:if test="${skuMap[propKey].state != 0}">
                                                                <a href="javascript:void(0);" class="viewDetail"  data-key="/pms/product/product-detail?proId=${skuMap[propKey].proId}">
                                                                    <span style="color: #1727ff">商品ID:${skuMap[propKey].proId}</span>
                                                                </a>
                                                                <br/><span id="${skuMap[propKey].proId}">当前状态:</span><c:if test="${skuMap[propKey].state == -1}">已删除</c:if><c:if test="${skuMap[propKey].state == 1}">上架</c:if><c:if test="${skuMap[propKey].state == 2}">下架</c:if></span>
                                                            </c:if>
                                                            <c:if test="${skuMap[propKey].state == 0}">
                                                                <a href="/pms/product/update?proId=${skuMap[propKey].proId}&skuDetail=${properties[0].name}:${ppb.name},${properties[1].name}:${ppa.name}" target="dialog" width="900" height="600">
                                                                    <span style="color: #1727ff">商品ID:${skuMap[propKey].proId}</span>
                                                                </a>
                                                                <br/><span id="${skuMap[propKey].proId}">当前状态:<c:if test="${skuMap[propKey].state == 0}">未编辑完成</c:if></span>
                                                            </c:if>
                                                            <br>
                                                        </td>
                                                    </c:when>
                                                    <c:when test="${!empty skuMap[propKeyb]}">
                                                        <td align="center">
                                                            <c:if test="${skuMap[propKeyb].state != 0}">
                                                                <a href="javascript:void(0);" class="viewDetail"  data-key="/pms/product/product-detail?proId=${skuMap[propKeyb].proId}">
                                                                    <span style="color: #1727ff">商品ID:${skuMap[propKeyb].proId}</span>
                                                                </a>
                                                            </c:if>
                                                            <c:if test="${skuMap[propKeyb].state == 0}">
                                                                <a href="/pms/product/update?proId=${skuMap[propKeyb].proId}&skuDetail=${properties[0].name}:${ppb.name},${properties[1].name}:${ppa.name}" target="dialog" width="900" height="600">
                                                                    <span style="color: #1727ff">商品ID:${skuMap[propKeyb].proId}</span>
                                                                </a>
                                                            </c:if>
                                                            <br><span id="${skuMap[propKeyb].proId}">当前状态:<c:if test="${skuMap[propKeyb].state == 0}">未编辑完成</c:if><c:if test="${skuMap[propKeyb].state == -1}">已删除</c:if><c:if test="${skuMap[propKeyb].state == 1}">上架</c:if><c:if test="${skuMap[propKeyb].state == 2}">下架</c:if></span>
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td style="text-align: center"><a href="/pms/product/add?step=8&spuId=${spu.spuId}&skuProp1=${ppb.proPropId}&skuProv1=${ppb.id}&skuProp2=${ppa.proPropId}&skuProv2=${ppa.id}&skuName=${ppb.name},${ppa.name}&skuDetail=${properties[0].name}:${ppb.name},${properties[1].name}:${ppa.name}" id="sku-add-${ppb.id}-${ppa.id}" target="dialog" width="550" height="200"><span style="color: #1727ff">生成商品</span></a></td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <td style="text-align: center"><a href="/pms/product/add?step=8&spuId=${spu.spuId}&skuProp1=${ppb.proPropId}&skuProv1=${ppb.id}&skuProp2=${ppa.proPropId}&skuProv2=${ppa.id}&skuName=${ppb.name},${ppa.name}&skuDetail=${properties[0].name}:${ppb.name},${properties[1].name}:${ppa.name}" id="sku-add-${ppb.id}-${ppa.id}" target="dialog" width="550" height="200"><span style="color: #1727ff">生成商品</span></a></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </fieldset>
            </c:otherwise>
        </c:choose>
        <div class="divider"></div>
        <c:if test='${sessionScope.bkstage_user.menus["a5a97d90f4d5f6c7a96c4b80eea315e3"]!=null}'>
            <div>
                <div class="button"><div class="buttonContent"><button type="button" id="allProToOff">将所有上架商品下架</button></div></div>
            </div>
        </c:if>
    </div>
    <div class="formBar">
        <ul>
            <li>
                <div class="button"><div class="buttonContent"><button type="button" id="offToall-cancle" class="close">关  闭</button></div></div>
            </li>
        </ul>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function(){

        $('a.viewDetail').live('click',function(e){
            var href = $(this).attr('data-key');
            navTab.openTab("0f55cb6caaf2b3de6bebda90d26d5c43",href,{title:'查看详情'});
        });

        $("#allProToOff").click(function(){
           var spuId = $("#J-spuId").val();
            $.post("/pms/product/updatesku-down",{cacheState:1,spuId:spuId,offFlag:1},function(data){
                if(data == 'ok'){
                    $("#offToall-cancle").click();
                }
            });
        });
    });
</script>