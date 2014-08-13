<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/product/listSelect">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
	<input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return ProNavTabSearch(this);" action="/pms/product/listSelect" method="post"
          id="p_kf_list_queryForm">
	
            <table class="searchContent pageFormContent" style="padding:0px;">
				 <tr>
				    <td colspan="4">
			    		<label>一级类目:</label>
			    		<select id="p_kf_first" name="first_category_id">
                            <option value="0">全部</option>
                            <c:forEach var="e" items="${categoryList}">
                                <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.firstCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                            </c:forEach>
                        </select>
                        <label>二级类目:</label>
                        <select id="p_kf_second" name="second_category_id">
                            <option value="0">全部</option>
                            <c:forEach var="e" items="${second_List}">
                                <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.secondCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                            </c:forEach>
                        </select>
                        <label>三级类目:</label>
                        <select id="p_kf_third" name="third_category_id"  >
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
                    <td>
                        <label >商品条形码:</label>
                    </td>
                    <td><input name="bar_code" class="alphanumeric" value="${bean.barCode}"/></td>
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
                        <label >SPU ID：</label>
                    </td>
                    <td>
                        <input name="spu_id" class="alphanumeric" value="${bean.spuId}"/>
                    </td>
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
                </tr>
				<tr>
                    <td>
					商品状态：
                    </td>
                    <td><select  name="state">

					 <c:forEach var="e" items="${stateList}" begin="0" end="1">
                        <option value='${e.code}' <c:if test="${e.code == bean.state}">selected="selected"</c:if>>${e.desc}</option>
                    </c:forEach>
					
					</td>
                     <td>
					 <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
					<input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
	
                    </td>
					<td>
                        <button type="submit">筛选</button>
						<button id="p_kf_list_reset">重置</button>
						
                    </td>
                </tr>
				
            </table>
        
    </form>
</div>

<!-- ============================================== -->
<div class="pageContent">
   

    <table class="table" width="100%" layoutH="290" >
        <thead>
        <tr>
			<th align="center"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
			<th align="center">序号</th>
			<th align="center">商品编码</th>
			<th align="center">条形码</th>
            <th align="center">商品标题</th>
            <th align="center">规格</th>
            <th align="center" orderField="mlw_price" class='<c:if test="${pc.pageInfo.orderField=='mlw_price'}" >${pc.pageInfo.orderDirection}</c:if>'>美丽价&nbsp;&nbsp;</th>
			<th align="center" orderField="stock" class='<c:if test="${pc.pageInfo.orderField=='sell_stock'}" >${pc.pageInfo.orderDirection}</c:if>'>可用库存&nbsp;&nbsp;</th>
            <th align="center">商品销量</th>
			<th align="center" orderField="update_time" class='<c:if test="${pc.pageInfo.orderField=='update_time'}" >${pc.pageInfo.orderDirection}</c:if>'>更新时间</th>
			<th align="center">商品状态</th>
			<th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="proId"  rel="${e.proId}">
					<td><input name="ids" value="${e.proId}" type="checkbox"></td>
					<td>${i.index+1}</td>
					<td>${e.proId}</td>
					<td>${e.barCode}</td>
                    <td><a href="http://www.meiliwan.com/${e.proId}.html" class="J-proView" key="${e.proId}" style="color: #0000ff" target="_blank">${e.proName}</a></td>
                    <td>${e.skuName}</td>
                    <td>${e.mlwPrice}
					</td>
                    <td>${e.sellStock}
                    </td>
                    <td>${e.realSaleNum}
                    </td>

					<td>
					<fmt:formatDate value="${e.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
					</td>
					<td>
						<c:forEach var="o" items="${stateList}">
							 <c:if test="${e.state == o.code}">${o.desc}</c:if>
						</c:forEach>
					</td>
					<td align="center">
                        <span><a title="商品预览" href="http://www.meiliwan.com/${e.proId}.html" class="J-proView" key="${e.proId}" style="color: #0000ff" target="_blank">预览</a></span>
                        <c:if test='${sessionScope.bkstage_user.menus["0f55cb6caaf2b3de6bebda90d26d5c43"]!=null}'>
                            <span><a title="查看详情" href="/pms/product/product-detail?proId=${e.proId}" target="navTab" style="color: #0000ff">查看详情</a></span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
			


<!-- ============================================== -->

<script type="text/javascript">
//时间比较data1>data2 return true
function dateCompare(date1,date2){

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
function PriceCompare(p1,p2){
	if(!p1||!p2) return true;
	if(eval(p1)>eval(p2)){
		alert("价格查询后面必须大于前面");
	  return false;
	}  
	return true;
}

function ProNavTabSearch(_this){
	var min = $(_this).find("input[name='update_time_min']").val();
	var max = $(_this).find("input[name='update_time_max']").val();
	var pmin = $(_this).find("input[name='mlw_price_min']").val();
	var pmax = $(_this).find("input[name='mlw_price_max']").val();
	
	
	if(dateCompare(min,max)&&PriceCompare(pmin,pmax)){
		return navTabSearch(_this);
	}
	return false;
}
$(document).ready(function(){
    
	$("#p_kf_first").change(function(){
		var level = $("#p_kf_first").val();
		
			$.post("/common/pms/child-category",{level:level},function(data){
				$("#p_kf_second").html('<option value="0">全部</option>'+data);
				$("#p_kf_third").html('<option value="0">全部</option>');
			});
		
		
	  //  $("#third").html('<option value="-1">全部</option>');
	});

	$("#p_kf_second").change(function(){
		var level = $("#p_kf_second").val();
		if(level){
			$.post("/common/pms/child-category",{level:level},function(data){
				$("#p_kf_third").html('<option value="0">全部</option>'+data);
			});
		}else{
			$("#p_kf_third").html('<option value="0">全部</option>');
		}
		
	});
//重置	
$("#p_kf_list_reset").click(function(check){
	$("#p_kf_list_queryForm input").each(function(i){
		var val = $(this).val("");
			
	});
	$("#p_kf_list_queryForm input[name='safeStock']").each(function(){
	   $(this).attr("checked",false);
	});  
	$("#p_kf_list_queryForm select").each(function(i){
		var $this = $(this);
		if($this.attr("name")=="state"){
			$(this).val("1")
		}else{
			$(this).val("0")
		}
		
	});
	check.preventDefault();
});



});
</script>
