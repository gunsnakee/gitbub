<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/supplier/sellList">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="firstLevelId" value="${firstLevelId}" />
    <input type="hidden" name="secondLevelId" value="${secondLevelId}" />
    <input type="hidden" name="thirdLevelId" value="${thirdLevelId}" />
    <input type="hidden" name="proName" value="${proName}"/>
    <input type="hidden" name="proId" value="${proId}"/>
    <input type="hidden" name="operateType" value="${operateType}"/>
    <input type="hidden" name="supplierName" value="${supplierName}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/pms/supplier/sellList" method="post" id="queryFormSellPro">
        <div class="searchBar">
            <input type="hidden" value="${firstLevel}" id="firstCategory">
            <input type="hidden" value="${supplierHtml}" id="supplierHtml">
            <table class="searchContent ">
                <tr>
                    <td>
                        <label>供应商名称</label>
                        <select name="supplierId" id="supplierIdSaleProduct">
                            <option value="-1">全部</option>
                            ${supplierHtml}
                        </select>
                    </td>
                    <td>
                        <label>商品ID</label>
                        <input name="proId" id="proIdSaleProduct" size="14" <c:if test="${!empty proId}">value="${proId}"</c:if> />
                    </td>
                    <td>
                        <label>商品标题</label>
                        <input name="proName" id="proNameSaleProduct" <c:if test="${!empty proName}">value="${proName}"</c:if> />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>一级类目</label>
                        <select name="firstLevelId" id="firstLevelSupplierSaleProduct">
                            <option value="-1">全部</option>
                            ${firstLevel}
                        </select>
                    </td>
                    <td>
                        <label>二级类目</label>
                        <select name="secondLevelId" id="secondLevelSupplierSaleProduct">
                            <option value="-1">全部</option>
                            ${secondLevel}
                        </select>
                    </td>
                    <td>
                        <label>三级类目</label>
                        <select name="thirdLevelId" id="thirdLevelSupplierSaleProduct">
                            <option value="-1">全部</option>
                            ${thirdLevel}
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>美丽价</label>
                        <input name="startPrice" size="14" id="startPriceSaleProduct" <c:if test="${!empty startPrice}">value="${startPrice}"</c:if> />
                        至<input name="endPrice" size="14" id="endPriceSaleProduct" <c:if test="${!empty endPrice}">value="${endPrice}"</c:if> />
                    </td>
                    <td>
                        <label>成交时间</label>
                        <input name="startTime" size="14" class="date" dateFmt="yyyy-MM-dd HH:mm:ss"  id="startTimeSaleProduct" <c:if test="${!empty startTime}">value="${startTime}"</c:if> />至
                        <input name="endTime" size="14" class="date" dateFmt="yyyy-MM-dd HH:mm:ss"  id="endTimeSaleProduct" <c:if test="${!empty endTime}">value="${endTime}"</c:if> />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                    </td>
                    <td><label> &nbsp;</label>
                        <div class="subBar">
                            <div class="buttonActive"><div class="buttonContent"><button type="button" id="bt_SellPro" onclick="check();">筛选</button></div></div>
                            <div class="buttonActive"><div class="buttonContent" id="revertSelectSelled"><button type="button">重置</button></div></div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <table class="table" width="100%" layoutH="158" id="J-table">
        <thead>
        <tr>
            <th align="center" width="8%">商品ID</th>
            <th align="center" width="8%">三级类目</th>
            <th align="center" width="24%">商品标题</th>
            <th align="center" width="8%">数量</th>
            <th align="center" width="8%">成交价</th>
            <th align="center" width="24%">供应商名称</th>
            <th align="center" width="8%">经营方式</th>
            <th align="center" width="12%">成交时间</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">没有结果或未选择供应商！</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="pc" items="${pc.entityList}">
                <tr  target="adminId" rel="">
                    <td>${pc.proId}</td>
                    <td>${pc.proCateName}</td>
                    <td align="center" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${pc.proName}</td>
                    <td>${pc.saleNum}</td>
                    <td>${pc.unitPrice}</td>
                    <td align="center" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${pc.supplier.supplierName}</td>
                    <td>
                        <c:if test="${pc.supplier.operateType==1}">购销</c:if>
                        <c:if test="${pc.supplier.operateType==2}">代销</c:if>
                        <c:if test="${pc.supplier.operateType==3}">联营</c:if>
                    </td>
                    <td><fmt:formatDate value="${pc.payTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>
    $(document).ready(function(){
        $("#bt_SellPro").click(function(){
            var firstLV = $("#firstLevelSupplierSaleProduct").val();
            var thirdLV = $("#thirdLevelSupplierSaleProduct").val();
            if(firstLV!='-1' && thirdLV=='-1'){
                alert("请选择三级类目查询!");
            }else{
                $("#queryFormSellPro").submit();
            }
        });

        $("#firstLevelSupplierSaleProduct").change(function(){
            var level = $("#firstLevelSupplierSaleProduct").val();
            $("#thirdLevelSupplierSaleProduct").html("<option value='-1'>全部</option>");
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#secondLevelSupplierSaleProduct").html("<option value='-1'>全部</option>"+data);
            });
        });

        $("#secondLevelSupplierSaleProduct").change(function(){
            var level = $("#secondLevelSupplierSaleProduct").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#thirdLevelSupplierSaleProduct").html("<option value='-1'>全部</option>"+data);
            });
        });

        $("#revertSelectSelled").click(function(){
            $("#proIdSaleProduct").val("");
            $("#proNameSaleProduct").val("");
            $("#startPriceSaleProduct").val("");
            $("#endPriceSaleProduct").val("");
            var str1=$("#supplierHtml").val().replace(/selected/g,"");
            $("#supplierIdSaleProduct").html("<option value='-1' selected>全部</option>"+str1);
            var str=$("#firstCategory").val().replace(/selected/g,"");
            $("#firstLevelSupplierSaleProduct").html("<option value='-1' selected>全部</option>"+str);
            $("#secondLevelSupplierSaleProduct").html("<option value='-1'>全部</option>");
            $("#thirdLevelSupplierSaleProduct").html("<option value='-1'>全部</option>");
            $("#startTimeSaleProduct").val("");
            $("#endTimeSaleProduct").val("");
            $("#operateTypeSaleProduct").html("<option value='1'>购销</option><option value='2'>代销</option><option value='3'>联营</option>");
        });
    });
</script>