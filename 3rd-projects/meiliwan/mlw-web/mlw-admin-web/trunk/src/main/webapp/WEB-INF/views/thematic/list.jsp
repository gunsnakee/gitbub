<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/thematic/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/thematic/list" method="post" id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        专题ID:
                        <input name="pageId" class="digits" value="${bean.pageId}">
                    </td>
                    <td>
                        专题名称:
                        <input name="pageName"  value="${bean.pageName}" >
                    </td>
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
            <c:if test='${sessionScope.bkstage_user.menus["2319ce2abe852441e0cbf8109abb0878"]!=null}'>
                <li><a title="添加专题" href="/thematic/add" width="600" height="400" rel="2319ce2abe852441e0cbf8109abb0878"  target="dialog" class="add"><span>添加专题</span></a></li>
            </c:if>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="235" id="J-table">
        <thead>
        <tr>
            
            <th align="center" width="5%">序号</th>
            <th align="center" width="10%">专题ID</th>
            <th align="center" width="10%">专题名称</th>
            <th align="center" width="15%">专题链接</th>
            <th align="center" width="15%">Html5链接(浏览器直接访问)</th>
            <th align="center" width="15%">Android链接(app专用)</th>
            <th align="center" width="5%">状态</th>
            <th align="center" width="14%">结束时间</th>
            <th align="center" width="8%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="adminId" rel="">
                    <td>${i.index + 1}</td>
                    <td>${e.pageId}</td>
                    <td>${e.pageName}</td>
                    <td><a href="${e.url}" target="_blank">${e.url}</a></td>
                    <td><a href="http://m.meiliwan.com/mopwan/${e.enName}" target="_blank">http://m.meiliwan.com/mopwan/${e.enName}</a></td>
                    <td><a href="http://m.meiliwan.com/mopwan/android/${e.enName}" target="_blank">http://m.meiliwan.com/mopwan/android/${e.enName}</a></td>
                    <td><c:if test="${e.state == 1}">正常</c:if><c:if test="${e.state == 0}">已删除</c:if></td>
                    <td><fmt:formatDate value="${e.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td>
                        <c:if test='${sessionScope.bkstage_user.menus["843fadd5128cb55f9802f6a41831daa3"]!=null}'>
                            <a title="编辑" target="dialog" href="/thematic/update?pageId=${e.pageId}" width="600" height="400" class="btnEdit">编辑</a>
                        </c:if>
                        <c:if test='${sessionScope.bkstage_user.menus["b1f540bbc840b3fd08007fc21435b81f"]!=null}'>
                            <a title="页面设置" target="navTab" href="/thematic/pageSet?pageId=${e.pageId}" class="btnInfo" rel="b1f540bbc840b3fd08007fc21435b81f">页面设置</a>
                        </c:if>
                        <c:if test='${sessionScope.bkstage_user.menus["7d7093a854e86c53cfd23dba09d7ba09"]!=null}'>
                            <a title="删除" target="ajaxTodo" href="/thematic/del?pageId=${e.pageId}" class="btnDel" >删除</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>
    $(document).ready(function(){
    });
</script>