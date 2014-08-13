<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/bkstage/store/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageHeader">
    <%--<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/bkstage/store/list" method="post"--%>
          <%--id="queryForm">--%>
        <%--<div class="searchBar">--%>
            <%--<table class="searchContent pageFormContent">--%>
                <%--<tr>--%>
                    <%--<td>--%>
                        <%--<label for="adminName">店铺名称:(未开放)</label>--%>
                    <%--</td>--%>
                    <%--<td><input name="adminName" id="adminName" value="${adminName}"/></td>--%>
                    <%--<td>--%>
                        <%--<div class="subBar">--%>
                            <%--<div class="buttonActive">--%>
                                <%--<div class="buttonContent">--%>
                                    <%--<button type="submit">筛选</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</td>--%>
                <%--</tr>--%>
            <%--</table>--%>
        <%--</div>--%>
    <%--</form>--%>
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["10102"]!=null}' >
                <li><a class="add" href="/bkstage/store/add" target="navTab"><span>添加店铺</span></a></li>
                <li class="line">line</li>
            </c:if>
        </ul>
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["00e44af2c44abc9b874e347bc0225bde"]!=null}' >
                <li><a class="edit" href="/bkstage/store/editAll?storeId={storeId}" target="navTab"><span>编辑店铺</span></a></li>
                <li class="line">line</li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="100%" layoutH="130" >
        <thead>
        <tr>
            <th align="center" width="10%">店铺名称</th>
            <th align="center" width="10%">SEO 标题</th>
            <th align="center" width="20%">SEO 列表页title</th>
            <th align="center" width="15%">店铺SEO keyword</th>
            <th align="center" width="20%">店铺SEO 描述</th>
            <th align="center" width="20%">创建时间</th>
            <th align="center"  width="5%">状态</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="storeId" rel="${e.storeId}">
                    <td>${e.storeName}</td>
                    <td>${e.seoTitle}</td>
                    <td>${e.seoTitleList}</td>
                    <td>${e.seoKeyword}</td>
                    <td>${e.seoDescp}</td>
                    <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
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
