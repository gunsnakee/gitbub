<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/mms/collect/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
    <input type="hidden" name="nickName" value="${nickName}" />
    <input type="hidden" name="proName" value="${proName}" />

</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/mms/collect/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="nickName">用户:</label>
                    </td>
                    <td><input name="nickName" id="nickName" value="${nickName}"/></td>
                    <td>
                        <label for="proName">商品名称:</label>
                    </td>
                    <td><input name="proName" id="proName" value="${proName}"/></td>
                    <td>
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit">筛选</button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <form action="/mms/collect/del" id="collectForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" id="proPackStatus" name="isDel" value="">
        <div class="panelBar">
        <ul class="toolBar">
            <%--<c:if test='${sessionScope.bkstage_user.menus["172"]!=null}'>
               <li><a title="批量删除收藏"  rel="ids" val="-1" class="delete"><span>删除收藏</span></a></li>
            </c:if>--%>
        </ul>
    </div>

    <table class="table" width="900" layoutH="130" id="J-table">
        <thead>
        <tr>
            <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
            <th width="5%"> ID</th>
            <th width="12%">用户</th>
            <th width="12%">商品id</th>
            <th width="12%">商品名称</th>
            <th width="12%" align="center"  orderField="create_time" class="<c:if test='${pc.pageInfo.orderField=="create_time"}' >
            ${pc.pageInfo.orderDirection}</c:if>">收藏时间</th>
            <th width="12%">是否删除</th>
            <th width="12%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="id" rel="${e.id}">
                    <td><input name="ids" value="${entity.id}" type="checkbox"></td>
                    <td>${e.id}</td>
                    <td>${e.nickName}</td>
                    <td>${e.proId}</td>
                    <td>${e.proName}</td>
                    <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <c:if test="${e.isDel==0}">未删</c:if>
                        <c:if test="${e.isDel!=0}">已删除</c:if>
                    </td>
                    <td>
                        <a title="查看收藏" target="dialog" height="320"  class="btnShow" href="/mms/collect/edit?id=${e.id}"><span>查看收藏</span></a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>
    $(document).ready(function(){
        $(".delete").click(function(){
            $("#proPackStatus").val($(this).attr("val"));
            alertMsg.confirm('确实要执行?' , {
                okCall: function(){
                    $("#proPackForm").submit();
                }
            });
        });
    });
</script>
