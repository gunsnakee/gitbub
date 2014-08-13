<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/base/infoitem/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="itemType" value="${itemType}"/>
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["149"]!=null}'>
                <li><a class="edit" href="/base/infoitem/add" target="dialog"><span>添加资讯类别</span></a></li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="900" layoutH="100" id="J-table">
        <thead>
        <tr>
            <th width="5%">资讯编码</th>
            <th width="12%">资讯名称</th>
            <th width="12%">更新时间</th>
            <th width="5%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="list" items="${pc.entityList}">
                <tr>
                    <td>${list.infoItemId}</td>
                    <td>${list.infoItemName}</td>
                    <td><fmt:formatDate value="${list.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td>
                        <c:if test='${sessionScope.bkstage_user.menus["150"]!=null}'>
                            <a title="编辑" target="dialog" height="320"  class="btnEdit" href="/base/infoitem/update?itemId=${list.infoItemId}"><span>编辑</span></a>
                        </c:if>
                       <%-- <c:if test='${sessionScope.bkstage_user.menus["151"]!=null}'>
                            <a title="删除" target="ajaxTodo" height="320"  class="btnDel" href="/base/infoitem/delete?itemId=${list.infoItemId}"><span>编辑</span></a>
                        </c:if>--%>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>

</script>
