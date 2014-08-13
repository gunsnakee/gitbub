<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/sp/lottery/result">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>

<div class="pageContent">


    <table class="table" width="100%" layoutH="130" id="J-table">
        <thead>
        <tr>
			<th align="center">id</th>
            <th align="center">用户id</th>
            <th align="center">用户名</th>
            <th align="center">奖品</th>
             <th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr  target="id" rel="${e.id}">
					<td>${e.id}</td>
					<td>${e.uid}</td>
					<td>${e.userName}</td>
					<td>${e.lotteryName}</td>
					<td><a target="navTab" href="/sp/lottery/getAddress?id=${e.id}">查看收货地址</a></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>

</script>
