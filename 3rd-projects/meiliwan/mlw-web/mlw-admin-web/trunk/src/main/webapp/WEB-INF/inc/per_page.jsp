<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="panelBar">
    <div class="pages">
        <span>显示</span>
        <select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
            <option value="10" <c:if test="${pc.pageInfo.pagesize == 10}" >selected="selected"</c:if>> 10</option>
            <option value="20" <c:if test="${pc.pageInfo.pagesize == 20}" >selected="selected"</c:if>>20</option>
            <option value="50" <c:if test="${pc.pageInfo.pagesize == 50}" >selected="selected"</c:if>>50</option>
            <option value="100" <c:if test="${pc.pageInfo.pagesize == 100}" >selected="selected"</c:if>>100</option>
            <option value="200" <c:if test="${pc.pageInfo.pagesize == 200}" >selected="selected"</c:if>>200</option>
        </select>
        <span>条，共${pc.pageInfo.totalCounts}条</span>
    </div>
    <div class="pagination" targetType="navTab" totalCount="${pc.pageInfo.totalCounts}" numPerPage="${pc.pageInfo.pagesize}" pageNumShown="10" currentPage="${pc.pageInfo.page}"></div>
</div>