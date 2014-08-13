<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/product/spu-list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
	<input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/pms/product/spu-list" method="post"
          id="spu_list_Form">
            <table class="searchContent pageFormContent" style="padding:0px;">
				 <tr>
				    <td colspan="4">
			    		<label for="spu_first">一级类目:</label>
			    		<select id="spu_first" name="spu_first_id">
                            <option value="0">全部</option>
                            <c:forEach var="e" items="${categoryList}">
                                <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.firstCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                            </c:forEach>
                        </select>
                        <label for="spu_second">二级类目:</label>
                        <select id="spu_second" name="spu_second_id">
                            <option value="0">全部</option>
                            <c:forEach var="e" items="${second_List}">
                                <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.secondCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                            </c:forEach>
                        </select>
                        <label for="spu_third">三级类目:</label>
                        <select name="spu_third_id" id="spu_third" >
                            <option value="0">全部</option>
							<c:forEach var="e" items="${third_List}">
                                <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.thirdCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                            </c:forEach>
                        </select>
				    </td>
				  </tr>
				<tr>
                    <td>
                        <label>SPU ID:</label>
                    </td>
                    <td><input name="spuId" value="${bean.spuId}"/>
					</td>
                     <td>
                        <label >商品标题:</label>
                    </td>
					<td><input name="proName" value="${bean.proName}"/></td>
                    <td><label>店铺名称:</label></td>
                    <td>
                        <select name="storeId">
                            <option value='0'>请选择</option>
                            <c:forEach var="e" items="${store}">
                                <option value='${e.storeId}'<c:if test="${e.storeId == bean.storeId}">selected="selected"</c:if>>${e.storeName}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
				<tr>
                    <td><label >供应商名称:</label></td>
					<td>
					<select name="supplierId">
					    <option value='0'>请选择</option>
						<c:forEach var="e" items="${suppliers}">
							<option value='${e.supplierId}'<c:if test="${e.supplierId == bean.supplierId}">selected="selected"</c:if>>${e.supplierName}</option>
						</c:forEach>
					    </select>
					</td>
                    <td>商品状态：</td>
                    <td>
                        <select name="state">
                            <option value='1' <c:if test="${1 == bean.state}">selected="selected"</c:if>>正常</option>
                            <option value='0' <c:if test="${0 == bean.state}">selected="selected"</c:if>>已删除</option>
                        </select>
					</td>
                    <td>
                    </td>
                    <td>
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit">筛   选</button>
                                </div>
                            </div>
                        </div>
                        <a class="button" href="javascript:void(0);"><span id="spu_list_reset">重  置</span></a>
                    </td>
                </tr>
            </table>
    </form>
</div>

<!-- ============================================== -->
<div class="pageContent">
   <div class="panelBar">
   </div>
    <table class="table" width="100%" layoutH="235" >
        <thead>
        <tr>
			<th align="center"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
			<th align="center">SPU ID</th>
            <th align="center">店铺名称</th>
            <th align="center">三级类目名称</th>
            <th align="center">商品标题</th>
            <th align="center">规格A</th>
            <th align="center">规格B</th>
            <th align="center">商品状态</th>
			<th align="center" orderField="create_time" class='<c:if test="${pc.pageInfo.orderField=='create_time'}" >${pc.pageInfo.orderDirection}</c:if>'>创建时间</th>
			<th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="spuId"  rel="${e.spuId}">
					<td><input name="ids" value="${e.spuId}" type="checkbox"></td>
					<td>${e.spuId}</td>
                    <td>${e.storeName}</td>
                    <td>${e.categoryName}</td>
                    <td>${e.proName}</td>
                    <td><c:if test="${empty e.skuPropA}">无</c:if><c:if test="${!empty e.skuPropA}">${e.skuPropA}</c:if></td>
                    <td><c:if test="${empty e.skuPropB}">无</c:if><c:if test="${!empty e.skuPropB}">${e.skuPropB}</c:if></td>
                    <td><c:if test="${e.state == 1}">正常</c:if><c:if test="${e.state == 0}">已删除</c:if></td>
                    <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td align="center">
                            <span><a title="查看" href="/pms/product/spu-view?spuId=${e.spuId}" target="navTab" style="color: #0000ff">查看</a></span>
                        <c:if test='${sessionScope.bkstage_user.menus["73"]!=null}'>
                            <c:if test="${e.isUploadImg == 0}">
                                <span><a title="上传图片" href="/pms/product/add?step=4&spuId=${e.spuId}" rel="73" target="navTab" style="color: #0000ff">上传图片</a></span>
                            </c:if>
                            <c:if test="${e.isUploadImg == 1}">
                                <span><a title="生成管理" href="/pms/product/add?step=7&spuId=${e.spuId}" target="navTab" style="color: #0000ff" rel="73">生成管理</a></span>
                            </c:if>
                        </c:if>
                        <c:if test='${sessionScope.bkstage_user.menus["df79a968ae166104724c7411308608df"]!=null}'>
                            <span><a title="删除操作会将该SPU生成的SKU全部删除，且无法恢复！ 请确认是否删除" href="/pms/product/spu-delete?spuId=${e.spuId}" target="ajaxTodo" style="color: #0000ff">删除</a></span>
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
$(document).ready(function () {
    $("#spu_first").change(function () {
        var level = $("#spu_first").val();
        $.post("/common/pms/child-category", {level: level}, function (data) {
            $("#spu_second").html('<option value="0">全部</option>' + data);
            $("#spu_third").html('<option value="0">全部</option>');
        });
    });

    $("#spu_second").change(function () {
        var level = $("#spu_second").val();
        if (level) {
            $.post("/common/pms/child-category", {level: level}, function (data) {
                $("#spu_third").html('<option value="0">全部</option>' + data);
            });
        } else {
            $("#spu_third").html('<option value="0">全部</option>');
        }
    });
    //重置
    $("#spu_list_reset").click(function (check) {
        $("#spu_list_Form input").each(function (i) {
            var val = $(this).val("");
        });
        $("#spu_list_Form select").each(function (i) {
            var $this = $(this);
            if ($this.attr("name") == "state") {
                $(this).val("1")
            } else {
                $(this).val("0")
            }
        });
        check.preventDefault();
    });
});
</script>
