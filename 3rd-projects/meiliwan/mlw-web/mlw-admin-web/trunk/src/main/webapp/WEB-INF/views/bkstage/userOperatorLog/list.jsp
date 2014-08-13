<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/bkstage/userOperatorLog/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
    <input type="hidden" name="loginName" value="${obj.loginName}" />
    <input type="hidden" name="loginId" value="${obj.loginId}" />
    <input type="hidden" name="startTime" value="<fmt:formatDate value="${obj.startTime}" pattern="yyyy-MM-dd HH:mm:ss"  />" />
    <input type="hidden" name="entTime" value="<fmt:formatDate value="${obj.endTime}" pattern="yyyy-MM-dd HH:mm:ss"  />" />
    <input type="hidden" name="menuId" value="${obj.menuId}" />
    <input type="hidden" name="menuName" value="${obj.menuName}" />
    <input type="hidden" name="roleKey" value="${obj.roleKey}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/bkstage/userOperatorLog/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td  >
                        <label  style="width: 50px;" for="loginName">登录名称:</label>
                    </td>
                    <td><input  style="width: 80px;" name="loginName" id="loginName" value="${obj.loginName}"/></td>
                    <td>
                        <label  style="width: 50px;" for="loginId">登录Id:</label>
                    </td>
                    <td><input  style="width: 80px;" name="loginId" id="loginId" value="${obj.loginId}"/></td>
                    <td style="width: 80px;">
                         时间范围:
                    </td>
                    <td style="width: 400px;">
                        <input type="text"  name="startTime" value="<fmt:formatDate value="${obj.startTime}" pattern="yyyy-MM-dd HH:mm:ss"  />" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true"/>
                        <a class="inputDateButton" href="javascript:;">选择</a>
                        <span style="float: left">至</span>
                        <input type="text"   name="endTime" value="<fmt:formatDate value="${obj.endTime}" pattern="yyyy-MM-dd HH:mm:ss"  />" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true"/>
                        <a class="inputDateButton" href="javascript:;">选择</a>

                    </td>
                </tr>
                <tr>
                    <td  >
                        <label  style="width: 50px;" for="menuId">menuId:</label>
                    </td>
                    <td><input  style="width: 80px;" name="menuId" id="menuId" value="${obj.menuId}"/></td>
                    <td  >
                        <label  style="width: 50px;" for="menuName">menuName:</label>
                    </td>
                    <td><input  style="width: 80px;" name="menuName" id="menuName" value="${obj.menuName}"/></td>
                    <td  >
                        <label  style="width: 50px;" for="roleKey">roleKey:</label>
                    </td>
                    <td><input  style="width: 150px;" name="roleKey" id="roleKey" value="${obj.roleKey}"/></td>
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
            <c:if test='${sessionScope.bkstage_user.menus["47"]!=null}' >
                <li><a class="delete" href="/bkstage/admin/editState?adminId={adminId}&state=-1" target="ajaxTodo" title="确定要停用该用户吗?"><span>停用该用户</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["92"]!=null}'>
                <li><a class="edit" href="/bkstage/admin/role?adminId={adminId}" target="navTab"><span>修改操作人权限</span></a></li>
                <li class="line">line</li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["a9d36d8484bdf4971ec53a6495defaf1"]!=null}'>
                <li><a class="edit" href="/bkstage/userOperatorLog/list?loginId={adminId}&menuId=${obj.menuId}&menuName=${obj.menuName}&startTime=<fmt:formatDate value="${obj.startTime}" pattern="yyyy-MM-dd HH:mm:ss"  />&endTime=<fmt:formatDate value="${obj.endTime}" pattern="yyyy-MM-dd HH:mm:ss"  />" target="navTab"><span>查看该用户操作</span></a></li>
                <li class="line">line</li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="100%" layoutH="160" >
        <thead>
        <tr>
            <th align="center"  width="100" >操作时间</th>
            <th align="center"   width="100" >操作人</th>
            <th align="center"   width="120" >操作功能</th>
            <th align="center"    >操作链接</th>
            <th align="center"    >操作参数</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}" varStatus="status">
                <tr  target="adminId" rel="${e.loginId}"  >
                    <td><fmt:formatDate value="${e.createTime}" pattern="MM-dd HH:mm:ss"  /></td>
                    <td>${e.loginName}(id:${e.loginId},IP:${e.ip})</td>
                    <td>${e.menuName}</td>
                    <td>${e.operateUrl}</td>
                    <td style="text-align: left" class="viewOptParameter"   > <span style="cursor: pointer" title='${e.operateParameter}'>查看</span>  </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script >
</script>
