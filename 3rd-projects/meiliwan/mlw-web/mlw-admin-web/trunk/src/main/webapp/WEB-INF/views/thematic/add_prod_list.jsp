<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/thematic/add-area-prod">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
	<input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return ThemeProNavTabSearch(this);" action="/thematic/add-area-prod" method="post"
          id="themeAdd_list_queryForm">
        <input type="hidden" name="pageId" value="${pageId}">
        <input type="hidden" name="pmId" value="${pmId}">
        <table class="searchContent pageFormContent" style="padding:0px;">
             <tr>
                <td colspan="4">
                    <label for="themefirst">一级类目:</label>
                    <select id="themefirst" name="first_category_id">
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${categoryList}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.firstCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                    <label for="themesecond">二级类目:</label>
                    <select id="themesecond" name="second_category_id">
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${second_List}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.secondCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                    <label for="themethird">三级类目:</label>
                    <select name="third_category_id" id="themethird" >
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${third_List}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.thirdCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                </td>
              </tr>
            <tr>
                <td>
                    <label>商品ID:</label>
                </td>
                <td><input name="pro_id" value="${bean.proId}"/>

                </td>
                 <td>
                    <label >商品标题:</label>
                </td>
                <td><input name="pro_name" value="${bean.proName}"/></td>
            </tr>
            <tr>
                <td>
                    <label>美丽价：</label>
                </td>
                <td><input name="mlw_price_min" value="${bean.mlwPriceMin}" style="float:none"/>至<input name="mlw_price_max" value="${bean.mlwPriceMax}" style="float:none"/></td>
                 <td>
                    <label>更新时间：</label>
                </td>
                <td>
                <input type="text" name="update_time_min" value="${bean.updateTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
            至
            <input type="text" name="update_time_max" value="${bean.updateTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
            </td>
            </tr>
            <tr>
                 <td>
                    <label >供应商名称:</label>
                </td>
                <td>
                <select name="supplier_id">
                <option value='0'>请选择</option>
                    <c:forEach var="e" items="${suppliers}">
                        <option value='${e.supplierId}'
                        <c:if test="${e.supplierId == bean.supplierId}">selected="selected"</c:if>
                         >${e.supplierName}</option>
                    </c:forEach>
                </select>
                </td>
                <td><label>优惠券批次号:</label></td>
                <td><input type="text" name="tkBatchId" value="${tkBatchId}"></td>
                <td>
                    <div class="subBar">
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">筛   选</button>
                            </div>
                        </div>
                    </div>
                    <a class="button" href="javascript:void(0);"><span id="theme_list_reset">重  置</span></a>
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 列表按钮操作 start-->
<div class="pageContent">
   <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["31adbbaf1dc5d89f7ee25787814e66bf"]!=null}'>
                <li><a title="确实要把商品加入专题页?" target="selectedTodo" rel="ids" href="/thematic/add-area-prod?pageId=${pageId}&pmId=${pmId}&handle=1" class="add"><span>添加商品到专题页</span></a></li>
            </c:if>
        </ul>
   </div>

    <table class="table" width="100%" layoutH="290" >
        <thead>
        <tr>
			<th align="center"><input type="checkbox" group="ids" class="checkboxCtrl">全选</th>
            <th align="center">商品ID</th>
            <th align="center">商品标题</th>
            <th align="center" orderField="mlw_price" class='<c:if test="${pc.pageInfo.orderField=='mlw_price'}" >${pc.pageInfo.orderDirection}</c:if>'>美丽价&nbsp;&nbsp;</th>
            <th align="center" orderField="stock" class='<c:if test="${pc.pageInfo.orderField=='sell_stock'}" >${pc.pageInfo.orderDirection}</c:if>'>可用库存&nbsp;&nbsp;</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${tkType == 'sp'}">
            <c:if test="${empty pc.entityList}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                    <tr target="proId"  rel="${e.proId}">
                        <td><c:if test="${e.isDel == 1}">已选择</c:if><c:if test="${e.isDel != 1}"><input name="ids" value="${e.proId}" type="checkbox"></c:if></td>
                        <td>${e.proId}</td>
                        <td>${e.proName}</td>
                        <td>${e.mlwPrice}</td>
                        <td>
                            <c:if test="${!empty e.sellStock}">
                                <span <c:if test="${e.sellStock <= e.safeStock}">style="color: red"</c:if>>${e.sellStock}</span>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </c:if>
        <c:if test="${tkType == 'tk'}">
            <c:if test="${empty pds}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            <c:if test="${!empty pds}">
                <c:forEach var="e" varStatus="i" items="${pds}">
                    <tr target="proId"  rel="${e.product.proId}">
                        <td><c:if test="${e.product.isCod == 1}">已选择</c:if><c:if test="${e.product.isCod != 1}"><input name="ids" value="${e.product.proId}" type="checkbox"></c:if></td>
                        <td>${e.product.proId}</td>
                        <td>${e.product.proName}</td>
                        <td>${e.product.mlwPrice}</td>
                        <td>
                            <c:if test="${!empty e.stock}">
                                <span <c:if test="${e.stock <= e.stock}">style="color: red"</c:if>>${e.stock}</span>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </c:if>
        </tbody>
    </table>

    <c:if test="${tkType == 'sp'}">
        <%@include file="/WEB-INF/inc/per_page.jsp" %>
    </c:if>
</div>
			


<!-- ============================================== -->

<script type="text/javascript">
//时间比较data1>data2 return true
function themeDateCompare(date1,date2){

	if(!date1) return true;
	if(!date2) return true; 
	date1 = date1.replace("-","/");//替换字符，变成标准格式  
	date2 = date2.replace("-","/");//替换字符，变成标准格式  

	if(date1>date2){  
		alert("时间查询后面必须大于前面");
	  return false;
	}  
	return true;
}
//价格比较
function themePriceCompare(p1,p2){
	if(!p1||!p2) return true;
	if(eval(p1)>eval(p2)){
		alert("价格查询后面必须大于前面");
	    return false;
	}  
	return true;
}
function ThemeProNavTabSearch(_this){
	var min = $(_this).find("input[name='update_time_min']").val();
	var max = $(_this).find("input[name='update_time_max']").val();
	var pmin = $(_this).find("input[name='mlw_price_min']").val();
	var pmax = $(_this).find("input[name='mlw_price_max']").val();


	if(themeDateCompare(min,max)&&themePriceCompare(pmin,pmax)){
		return navTabSearch(_this);
	}
	return false;
}
$(document).ready(function () {

    $("#themefirst").change(function () {
        var level = $("#themefirst").val();

        $.post("/common/pms/child-category", {level: level}, function (data) {
            $("#themesecond").html('<option value="0">全部</option>' + data);
            $("#themethird").html('<option value="0">全部</option>');
        });
    });

    $("#themesecond").change(function () {
        var level = $("#themesecond").val();
        if (level) {
            $.post("/common/pms/child-category", {level: level}, function (data) {
                $("#themethird").html('<option value="0">全部</option>' + data);
            });
        } else {
            $("#themethird").html('<option value="0">全部</option>');
        }

    });
    //重置
    $("#theme_list_reset").click(function (check) {
        $("#themeAdd_list_queryForm input").each(function (i) {
            var val = $(this).val("");
        });
        check.preventDefault();
    });
});
</script>
