<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/base/config/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <%--<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/base/app/version/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="sysConfigName">编码名称:</label>
                    </td>
                    <td><input name="sysConfigName" id="sysConfigName" value=""/></td>
                    <td>
                        <label for="sysConfigCode">编码:</label>
                    </td>
                    <td><input name="sysConfigCode" id="sysConfigCode" value=""/></td>
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
    </form>--%>
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["302c0729d15638669d0adb1cca4991ac"]!=null}'>
                <li class="line">line</li>
                <li><a class="edit" href="/base/app/version/add" target="navTab"><span>添加App版本号</span></a></li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="900" layoutH="130" id="J-table">
        <thead>
        <tr>
           <!-- <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>   -->
            <th width="5%"> 配置项ID</th>
            <th width="12%">编码名称</th>
            <th width="12%">编码(根据版本)</th>
            <th width="20%">编码值(版本码,版本名称,更新类型)</th>
            <th width="30%">版本更新内容描述</th>
            <th width="5%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="sysConfigId" rel="${e.sysConfigId}">
                    <td>${e.sysConfigId}</td>
                    <td>${e.sysConfigName}</td>
                    <td>${e.sysConfigCode}</td>
                    <td>${e.sysConfigValue}</td>
                    <td>${e.descp}</td>
                    <td>
                        <a title="编辑该配置项" target="dialog" width="800" height="320"  class="btnEdit" href="/base/app/version/edit?sysConfigId=${e.sysConfigId}"><span>编辑配置项</span></a>
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
