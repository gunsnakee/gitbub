<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<form id="pagerForm" method="post" action="/ticket/batch/get-prod-list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>

<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/ticket/batch/get-prod-list" method="post"
          id="ticketGetProdsForm">
        <table class="searchContent pageFormContent" style="padding:0px;">
            <input type="hidden" name="batchId" value="${batchId}">
            <tr>
                <td colspan="4">
                    <label for="ticket_first">一级类目:</label>
                    <select id="ticket_first" name="first_category_id">
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${categoryList}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.firstCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                    <label for="ticket_second">二级类目:</label>
                    <select id="ticket_second" name="second_category_id">
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${second_List}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.secondCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                    <label for="ticket_third">三级类目:</label>
                    <select name="third_category_id" id="ticket_third" >
                        <option value="0">全部</option>
                        <c:forEach var="e" items="${third_List}">
                            <option value="${e.categoryId}" <c:if test="${e.categoryId == bean.thirdCategoryId}">selected="selected"</c:if>>${e.categoryName}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>商品ID:</label></td>
                <td><input name="pro_id" value="${bean.proId}"></td>
                <td><label >商品标题:</label></td>
                <td><input name="pro_name" value="${bean.proName}" style="width: 200px"/></td>
                <td>
                    <label>美丽价：</label>
                </td>
                <td><input name="mlw_price_min" value="${bean.mlwPriceMin}" style="float:none"/>至<input name="mlw_price_max" value="${bean.mlwPriceMax}" style="float:none"/></td>
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
                <td>
                    <label>更新时间：</label>
                </td>
                <td>
                    <input type="text" name="update_time_min" value="${bean.updateTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    至
                    <input type="text" name="update_time_max" value="${bean.updateTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </td>
                <td>  </td>
                <td>
                    <div class="subBar">
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">筛   选</button>
                            </div>
                        </div>
                    </div>
                    <a class="button" href="javascript:void(0);"><span id="ticket_product_list_reset">重  置</span></a>
                </td>
            </tr>
        </table>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="/ticket/batch/get-prod-list?handle=1&batchId=${batchId}" rel="ids" target="selectedTodo" title="确实这些商品参与优惠券?"><span>添加商品</span></a></li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="205">
    <thead>
        <tr>
            <th align="center" width="50"><input type="checkbox" group="ids" class="checkboxCtrl">全选</th>
            <th align="center" width="80">商品ID</th>
            <th align="center" width="250">商品标题</th>
            <th align="center" width="100">参加活动</th>
            <th align="center" width="150">参加品类券</th>
            <th align="center" width="80">美丽价</th>
            <th align="center" width="80">可用库存</th>
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
                <td>${e.proId}</td>
                <td>${e.proName}</td>
                <td>
                    <select>
                        <c:if test="${!empty e.seoKeyword}">
                            <c:set var="actNmaes" value="${fn:split(e.seoKeyword,'/')}"></c:set>
                            <c:forEach var="actN" items="${actNmaes}" varStatus="varState">
                                <option>${actN}</option>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty e.seoKeyword}">
                            <option>暂无参加活动</option>
                        </c:if>
                    </select>
                </td>
                <td>
                    <select>
                        <c:if test="${!empty e.searchKeyword}">
                            <c:set var="tkNames" value="${fn:split(e.searchKeyword,'/')}"></c:set>
                            <c:forEach var="tkN" items="${tkNames}" varStatus="varState">
                                <option>${tkN}</option>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty e.searchKeyword}">
                            <option>暂无參加优惠券</option>
                        </c:if>
                    </select>
                </td>
                <td>${e.mlwPrice}</td>
                <td>${e.sellStock}</td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
    </table>
    <div class="panelBar">
        <div class="pages">
            <span>显示</span>
            <select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="20" <c:if test="${pc.pageInfo.pagesize == 20}" >selected="selected"</c:if>>20</option>
            </select>
            <span>条，共${pc.pageInfo.totalCounts}条</span>
        </div>
        <div class="pagination" targetType="navTab" totalCount="${pc.pageInfo.totalCounts}" numPerPage="${pc.pageInfo.pagesize}" pageNumShown="10" currentPage="${pc.pageInfo.page}"></div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#ticket_first").change(function () {
            var level = $("#ticket_first").val();
            $.post("/common/pms/child-category", {level: level}, function (data) {
                $("#ticket_second").html('<option value="0">全部</option>' + data);
                $("#ticket_third").html('<option value="0">全部</option>');
            });
        });
        $("#ticket_second").change(function () {
            var level = $("#ticket_second").val();
            if (level) {
                $.post("/common/pms/child-category", {level: level}, function (data) {
                    $("#ticket_third").html('<option value="0">全部</option>' + data);
                });
            } else {
                $("#ticket_third").html('<option value="0">全部</option>');
            }
        });

        //重置
        $("#ticket_product_list_reset").click(function (check) {
            $("#ticketGetProdsForm input").each(function (i) {
                var val = $(this).val("");
            });
            $("#ticketGetProdsForm select").each(function (i) {
                $(this).val("");
            });
            check.preventDefault();
        });
    });

</script>
