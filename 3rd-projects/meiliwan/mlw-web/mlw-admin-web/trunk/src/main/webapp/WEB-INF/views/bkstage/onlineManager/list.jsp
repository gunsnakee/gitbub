<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
    <input type="hidden" name="adminName" value="${adminName}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="adminName">账号/名称/手机/部门/邮件:</label>
                    </td>
                    <td><input name="adminName" id="adminName" value="当前还不能使用"/></td>
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
            <c:if test='${sessionScope.bkstage_user.menus["124"]!=null}' >
                <li><a class="delete" href="/bkstage/onlineManager/helpLogout?adminId={adminId}" target="ajaxTodo" title="确定要踢出该用户吗?"><span>强制退出</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["47"]!=null}' >
                <li><a class="delete" href="/bkstage/admin/editState?adminId={adminId}&state=-1" target="ajaxTodo" title="确定要停用该用户吗?"><span>停用该用户</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["a9d36d8484bdf4971ec53a6495defaf1"]!=null}'>
                <li><a class="edit" href="/bkstage/userOperatorLog/list?loginId={adminId}" target="navTab"><span>查看该用户操作</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["92"]!=null}'>
                <li><a class="edit" href="/bkstage/admin/role?adminId={adminId}" target="navTab"><span>修改权限</span></a></li>
                <li class="line">line</li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="900" layoutH="130" id="J-table">
        <thead>
        <tr>
            <th align="center"  orderField="login_name"  class="<c:if test='${pc.pageInfo.orderField=="login_name"}' >${pc.pageInfo.orderDirection}</c:if>">登录账号</th>
            <th align="center"  orderField="admin_name"  class="<c:if test='${pc.pageInfo.orderField=="admin_name"}' >${pc.pageInfo.orderDirection}</c:if>">登录名字</th>
            <th align="center"  orderField="admin_phone" class="<c:if test='${pc.pageInfo.orderField=="admin_phone"}' >${pc.pageInfo.orderDirection}</c:if>">部门</th>
            <th align="center"  orderField="admin_phone" class="<c:if test='${pc.pageInfo.orderField=="admin_phone"}' >${pc.pageInfo.orderDirection}</c:if>">手机</th>
            <th align="center"  >本次登录操作次数</th>
            <th align="center"  orderField="department"  class="<c:if test='${pc.pageInfo.orderField=="department"}' >${pc.pageInfo.orderDirection}</c:if>">登录时间</th>
            <th align="center"  orderField="admin_email" class="<c:if test='${pc.pageInfo.orderField=="admin_email"}' >${pc.pageInfo.orderDirection}</c:if>">最后操作时间</th>
            <th align="center"  orderField="create_time" class="<c:if test='${pc.pageInfo.orderField=="create_time"}' >${pc.pageInfo.orderDirection}</c:if>">最后操作URL</th>
            <th align="center"  orderField="update_time" class="<c:if test='${pc.pageInfo.orderField=="update_time"}' >${pc.pageInfo.orderDirection}</c:if>">登录IP</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty  onlineUser}">
            <tr><td colspan="7" style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty  onlineUser}">
            <c:forEach var="e" items="${onlineUser}">
                <tr  target="adminId" rel="${e.bksAdmin.adminId}">
                    <td>${e.bksAdmin.loginName}</td>
                    <td>${e.bksAdmin.adminName}</td>
                    <td>${e.bksAdmin.department}</td>
                    <td>${e.bksAdmin.adminPhone}</td>
                    <td>${e.curLoginOptCount}</td>
                    <td><fmt:formatDate value="${e.loginTime}" pattern="MM-dd HH:mm:ss"  /></td>
                    <td><fmt:formatDate value="${e.lastOperatorTime}" pattern="MM-dd HH:mm:ss"  /></td>
                    <td>${e.lastRequestURI}</td>

                    <td>${e.loginIp}</td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>

</script>
