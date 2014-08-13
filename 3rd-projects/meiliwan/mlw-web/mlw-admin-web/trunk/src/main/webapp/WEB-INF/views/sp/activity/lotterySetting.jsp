<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">

    <table class="table" width="100%" layoutH="130" id="J-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>奖项名称</th>
            <th>奖品名称</th>
            <th>奖品数量</th>
            <th>可能中奖数量</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty list}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty list}">
            <c:forEach var="e" varStatus="i" items="${list}">
                <tr  target="id" rel="${e.id}">
                    <td>${e.id}</td>
                    <td>${e.lotteryName}</td>
                    <td>${e.lotteryProduct}</td>
                    <td>${e.total}</td>
                    <td>${e.possibility}   (<fmt:formatNumber value="${e.rate}" pattern="##.######"  minFractionDigits="6"/>%)</td>
                    <td>
                        <a target="navTab" href="/sp/lottery/addView?id=${e.id}">编辑</a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>
<script>

</script>
