<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post"  onsubmit="return dialogSearch(this);" action="/sp/activity/showProList">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
    <input type="hidden" name="first_category_id" value="${bean.firstCategoryId}"/>
    <input type="hidden" name="second_category_id" value="${bean.secondCategoryId}"/>
    <input type="hidden" name="third_category_id" value="${bean.thirdCategoryId}"/>
    <input type="hidden" name="pro_id" value="${bean.proId}"/>
    <input type="hidden" name="pro_name" value="${bean.proName}"/>
    <input type="hidden" name="mlw_price_min" value="${bean.mlwPriceMin}"/>
    <input type="hidden" name="mlwPriceMax" value="${bean.mlwPriceMax}"/>
    <input type="hidden" name="_min" value="${bean.updateTimeMin}"/>
    <input type="hidden" name="update_time_max" value="${bean.updateTimeMax}"/>
    <input type="hidden" name="sell_type" value="${bean.sellType}"/>
    <input type="hidden" name="supplier_id" value="${bean.supplierId}"/>
    <input type="hidden" name="actId" value="${actId}"/>
</form>

<div class="pageHeader">
<form rel="pagerForm" class="required-validate" onsubmit="return dialogSearch(this);" action="/sp/activity/showProList" method="post" id="transportList_queryForm" name="transportList_queryForm">
    <div class="searchBar" id="product_list_searchBar">
        <input type="hidden" name="actId" value="${actId}"/>
        <table class="searchContent">
            <tr>
                <td >
                    <label for="first">一级类目：</label>
                    <select id="first" name="first_category_id">
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${categoryList}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.firstCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <label for="second">二级类目：</label>
                    <select id="second" name="second_category_id">
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${second_List}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.secondCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <label for="third">三级类目:</label>
                    <select name="third_category_id" id="third" >
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${third_List}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.thirdCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <label>商品ID：</label><input name="pro_id" value="${bean.proId}" class="digits"/>
                <td>
                    <label>商品标题：</label><input name="pro_name" value="${bean.proName}"/>
                </td>
                <td></td>
            </tr>
            <tr>
                <td>
                    <label >供应商名称：</label>
                    <select name="supplier_id">
                        <option value='0'>请选择</option>
                        <c:forEach var="e" items="${suppliers}">
                            <option value='${e.supplierId}'
                                    <c:if test="${e.supplierId == bean.supplierId}">selected="selected"</c:if>
                                    >${e.supplierName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <label>更新时间：</label>
                    <input type="text" name="update_time_min" size="16" value="<fmt:formatDate value="${bean.updateTimeMin}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true"/>
                    至
                    <input type="text" name="update_time_max" size="16" value="<fmt:formatDate value="${bean.updateTimeMax}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true"/>
                </td>
                <td></td>
            </tr>
            <tr>
                <td>
                    <label >经营方式：</label>
                    <select name="sell_type">
                        <c:forEach var="e" items="${sellTypeMap}">
                            <option value='${e.key}' <c:if test="${e.key == bean.sellType}">selected="selected"</c:if>>${e.value}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <label>美丽价：</label><input name="mlw_price_min" size="10" value="${bean.mlwPriceMin}"/>元 至 <input name="mlw_price_max" size="10" value="${bean.mlwPriceMax}"/>元
                </td>
                <td>
                    <div style="margin-left: 20px;" class="buttonActive"><div class="buttonContent"><button type="submit">查询</button></div></div>
                    <div style="margin-left: 20px;"  class="buttonActive"><div class="buttonContent" id="spListClean"><button type="button">重置</button></div></div>
                </td>
            </tr>
        </table>
    </div>
</form>
</div>

<!-- ============================================== -->

			
<div class="pageContent">
   <form action="/sp/activity/addActPro" id="addActProForm" method="post" class="pageForm" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
       <input type="hidden" name="actId" value="${actId}"/>
       <div class="panelBar">
           <ul class="toolBar">
               <li><a title="添加一批选中的商品" rel="ids" href="javascript:void(0);" class="add addActProduct"><span>添加活动商品</span></a></li>
               <li class="line">line</li>
           </ul>
       </div>

       <table class="table" width="100%" layoutH="190" >
           <thead>
           <tr>
               <th align="center">商品ID</th>
               <th align="center">商品标题</th>
               <th align="center" orderField="mlw_price" class="<c:if test='${pc.pageInfo.orderField=="mlw_price"}'>${pc.pageInfo.orderDirection}</c:if>">美丽价</th>
               <th align="center" orderField="sell_stock" class="<c:if test='${pc.pageInfo.orderField=="sell_stock"}'>${pc.pageInfo.orderDirection}</c:if>">可用库存</th>
               <th align="center">全选<input type="checkbox" group="ids" class="checkboxCtrl"></th>
           </tr>
           </thead>
           <tbody>
           <c:if test="${empty pc.entityList}">
               <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
           </c:if>
           <c:if test="${!empty pc.entityList}">
               <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                   <tr id="tr_${e.proId}" >
                       <td>${e.proId}</td>
                       <td><div id="proName_${e.proId}">${e.proName} &nbsp;${e.skuName}</div></td>
                       <td>${e.mlwPrice}</td>
                       <td><div id="stock_${e.proId}">${e.sellStock}</div></td>
                       <td>
                           <c:if test="${fn:contains(usedProIds, e.proId)}">
                               已添加
                           </c:if>
                           <c:if test="${!fn:contains(usedProIds, e.proId)}">
                               <span style="color: #0000ff">选择</span><input name="ids" class="addActProCheckbox" value="${e.proId}" type="checkbox">
                           </c:if>
                       </td>
                   </tr>
               </c:forEach>
           </c:if>
           </tbody>
       </table>
       <div class="panelBar">
           <div class="pages">
               <span>显示</span>
               <select class="combox" name="numPerPage" onchange="dialogPageBreak({numPerPage:this.value})">
                   <option value="10" <c:if test="${pc.pageInfo.pagesize == 10}" >selected="selected"</c:if>> 10</option>
                   <option value="20" <c:if test="${pc.pageInfo.pagesize == 20}" >selected="selected"</c:if>>20</option>
                   <option value="50" <c:if test="${pc.pageInfo.pagesize == 50}" >selected="selected"</c:if>>50</option>
                   <option value="100" <c:if test="${pc.pageInfo.pagesize == 100}" >selected="selected"</c:if>>100</option>
                   <option value="200" <c:if test="${pc.pageInfo.pagesize == 200}" >selected="selected"</c:if>>200</option>
               </select>
               <span>条，共${pc.pageInfo.totalCounts}条</span>
           </div>
           <div class="pagination" targetType="dialog" totalCount="${pc.pageInfo.totalCounts}" numPerPage="${pc.pageInfo.pagesize}" pageNumShown="10" currentPage="${pc.pageInfo.page}"></div>
       </div>
   </form>
</div>
			


<!-- ============================================== -->

<script type="text/javascript">
$(document).ready(function(){
	$("#first").change(function(){
		var level = $("#first").val();
		$.post("/common/pms/child-category",{level:level},function(data){
			$("#second").html('<option value="-1">全部</option>'+data);
		});
	});

	$("#second").change(function(){
		var level = $("#second").val();
		$.post("/common/pms/child-category",{level:level},function(data){
			$("#third").html('<option value="-1">全部</option>'+data);
		});
	});

    $(".addActProduct").click(function(){
        alertMsg.confirm('确实要添加选中的商品吗?', {
            okCall: function () {
                var i=0;
                $(".addActProCheckbox:checked").each(function(){
                    i=1;
                });
                if(i==0){
                    alert("请选择");
                }else{
                    $("#addActProForm").submit();
                    $(".addActProCheckbox:checked").each(function(){
                        $(this).parent().html("已添加");
                    });
                }
            }
        });
    });
    $("#spListClean").click(function(){
        $(".searchContent input").val('');
        $(".searchContent select").each(function(i){
            if(i==3 || i==4){
                $(this).html('<option value="0">全部</option>');
            }else{
                $(this).val(0)
            }
        });
    });
});
</script>
