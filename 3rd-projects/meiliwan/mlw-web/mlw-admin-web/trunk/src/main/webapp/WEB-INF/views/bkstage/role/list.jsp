<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/bkstage/role/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
    <input type="hidden" name="roleName" value="${roleName}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/bkstage/role/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="roleName">权限名称:</label>
                    </td>
                    <td><input name="roleName" id="roleName" value="${roleName}"/></td>
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
    <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["47"]!=null}'>
                <li><a class="edit" href="/bkstage/role/add?mid=47" target="navTab"><span>添加权限</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["69"]!=null}'>
                <li><a class="add" href="/bkstage/role/editState?mid=69&roleId={roleId}&state=0" target="ajaxTodo"
                       title="确定要启用该角色吗？" warn="请选择一个角色" ><span>启用</span></a></li>
                <li><a class="delete" href="/bkstage/role/editState?mid=69&roleId={roleId}&state=-1" target="ajaxTodo"
                       title="确定要停用该角色吗?"><span>停用</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["52"]!=null}'>
                <li><a class="edit" href="/bkstage/role/edit?roleId={roleId}"
                       target="navTab"><span>编辑角色</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["70"]!=null}'>
                <li><a class="edit" href="/bkstage/role/menu?roleId={roleId}"
                       target="navTab"><span>管理角色菜单</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["114"]!=null}'>
                <li><a class="delete" href="/bkstage/role/del?roleId={roleId}" target="ajaxTodo" title="确定要删除该角色吗?"><span>删除</span></a></li>
                <li class="line">line</li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="900" layoutH="130" id="J-table">
        <thead>
        <tr>
            <th align="center"   orderField="department" class="<c:if test='${pc.pageInfo.orderField=="department"}' >${pc.pageInfo.orderDirection}</c:if>">部门</th>
            <th align="center"   orderField="name" class="<c:if test='${pc.pageInfo.orderField=="name"}' >${pc.pageInfo.orderDirection}</c:if>">角色名称</th>
            <th align="center">角色描述</th>
            <th align="center"  orderField="state" class="<c:if test='${pc.pageInfo.orderField=="state"}' >${pc.pageInfo.orderDirection}</c:if>">状态</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="roleId" rel="${e.roleId}">
                    <td   <c:if test='${e.department =="美丽湾人员" }' >  style="background-color: #19D6FA" </c:if>
                            <c:if test='${e.department =="管理层" }' >  style="background-color: #97e651" </c:if>
                            <c:if test='${e.department =="编辑部" }' >  style="background-color: #e6db55" </c:if>
                            <c:if test='${e.department =="客服部" }' >  style="background-color: #ffff00" </c:if>
                            <c:if test='${e.department =="仓库物流部门" }' >  style="background-color: #F88D63" </c:if>
                            <c:if test='${e.department =="财务" }' >  style="background-color: #ddffdd" </c:if>
                            <c:if test='${e.department =="采购" }' >  style="background-color: #E29DE2" </c:if> >${e.department}</td>
                    <td>${e.name}</td>
                    <td>${e.description}</td>
                    <td <c:if test="${e.state==-1}">  style="background-color: #f82a02"  </c:if> > <c:if test="${e.state==0}">启用</c:if>
                        <c:if test="${e.state==-1}">停用</c:if></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>

</script>
