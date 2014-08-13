<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/comment/list_dv">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}">
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}">
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageHeader">
</div>
<div class="pageContent">
    <form action="/pms/comment/add_dv?handle=0" id="complaintsForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" name="handle" id="complaintsState" value=""/>
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="创建评论缺省值" height="400" href="/pms/comment/add_dv?handle=0" rel="ids" class="add" target="dialog"><span>创建评论缺省值</span></a></li>
            </ul>
        </div>
        <table class="table" width="100%"  layoutH="85">
            <thead>
            <tr>
                <th align="center" width="5%"  orderField="id" class="<c:if test='${pc.pageInfo.orderField=="id"}'> ${pc.pageInfo.orderDirection}</c:if>">评论缺省值ID</th>
                <th align="center" width="8%">对应评分</th>
                <th align="center" width="25%">评论内容</th>
                <th align="center" width="12%" orderField="create_time" class="<c:if test='${pc.pageInfo.orderField=="create_time"}'> ${pc.pageInfo.orderDirection}</c:if>">创建时间</th>
                <th align="center" width="8%">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty pc.entityList}">
                <tr>
                    <td style="text-align: center;"><font color="#dc153c">暂无数据</font></td>
                </tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="entity" items="${pc.entityList}">
                    <tr align="center">
                        <td>${entity.id}</td>
                        <td>${entity.score}</td>
                        <td title="${entity.content}">${entity.content}</td>
                        <td><fmt:formatDate value="${entity.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>
                            <a title="编辑" target="dialog" height="400" href="/pms/comment/add_dv?handle=0&id=${entity.id}" class="btnEdit" >编辑</a>
                            <a title="删除" target="ajaxTodo" href="/pms/comment/add_dv?handle=3&id=${entity.id}" class="btnDel" >删除</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
