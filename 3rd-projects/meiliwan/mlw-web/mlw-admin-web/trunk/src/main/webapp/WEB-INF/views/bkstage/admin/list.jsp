<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/bkstage/admin/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
    <input type="hidden" name="adminName" value="${adminName}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/bkstage/admin/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="adminName">账号/名称/手机/部门/邮件:</label>
                    </td>
                    <td><input name="adminName" id="adminName" value="${adminName}"/></td>
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
            <c:if test='${sessionScope.bkstage_user.menus["49"]!=null}' >
                <li><a class="add" href="/bkstage/admin/add" target="navTab"><span>添加后台人员</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["47"]!=null}' >
                <li><a class="add" href="/bkstage/admin/editState?adminId={adminId}&state=0" target="ajaxTodo" title="确定要启用该用户吗？" warn="请选择一个用户" target="navTab"><span>启用</span></a></li>
                <li><a class="delete" href="/bkstage/admin/editState?adminId={adminId}&state=-1" target="ajaxTodo" title="确定要停用该用户吗?"><span>停用</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["91"]!=null}'>
                <li><a class="edit" href="/bkstage/admin/edit?adminId={adminId}" target="navTab"><span>编辑</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["a9d36d8484bdf4971ec53a6495defaf1"]!=null}'>
                <li><a class="edit" href="/bkstage/userOperatorLog/list?loginId={adminId}" target="navTab"><span>用户操作记录</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["92"]!=null}'>
                <li><a class="edit" href="/bkstage/admin/role?adminId={adminId}" target="navTab"><span>修改权限</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["107"]!=null}'>
                <li><a class="edit" href="/bkstage/admin/resetPwd?adminId={adminId}" target="navTab"><span>设置用户密码</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["113"]!=null}'>
                <li><a class="delete" href="/bkstage/admin/del?adminId={adminId}" target="ajaxTodo" title="确定要删除该用户吗?"><span>删除</span></a></li>
                <li class="line">line</li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="900" layoutH="130" >
        <thead>
        <tr>
            <th align="center"  orderField="login_name"  class="<c:if test='${pc.pageInfo.orderField=="login_name"}' >${pc.pageInfo.orderDirection}</c:if>">管理员账号</th>
            <th align="center"  orderField="admin_name"  class="<c:if test='${pc.pageInfo.orderField=="admin_name"}' >${pc.pageInfo.orderDirection}</c:if>">名称</th>
            <th align="center"  orderField="admin_phone" class="<c:if test='${pc.pageInfo.orderField=="admin_phone"}' >${pc.pageInfo.orderDirection}</c:if>">手机</th>
            <th align="center"  orderField="department"  class="<c:if test='${pc.pageInfo.orderField=="department"}' >${pc.pageInfo.orderDirection}</c:if>">部门</th>
            <th align="center"  orderField="admin_email" class="<c:if test='${pc.pageInfo.orderField=="admin_email"}' >${pc.pageInfo.orderDirection}</c:if>">邮件</th>
            <th align="center"  orderField="create_time" class="<c:if test='${pc.pageInfo.orderField=="create_time"}' >${pc.pageInfo.orderDirection}</c:if>">创建时间</th>
            <th align="center"  orderField="update_time" class="<c:if test='${pc.pageInfo.orderField=="update_time"}' >${pc.pageInfo.orderDirection}</c:if>">更新时间</th>
            <th align="center"  orderField="state"       class="<c:if test='${pc.pageInfo.orderField=="state"}' >${pc.pageInfo.orderDirection}</c:if>">状态</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="adminId" rel="${e.adminId}">
                    <td>${e.loginName}</td>
                    <td>${e.adminName}</td>
                    <td>${e.adminPhone}</td>
                    <td>${e.department}</td>
                    <td>${e.adminEmail}</td>
                    <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                    <td><fmt:formatDate value="${e.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                    <td><c:if test="${e.state==0}">启用</c:if>
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
