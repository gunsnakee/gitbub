<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<!DOCTYPE HTML>
<div class="pageHeader">
    <form id="pagerForm" onsubmit="return navTabSearch(this);" action="/player/modulePlayerList" method="post"
           name="order_wait_queryForm">
        <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
        <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    </form>
</div>
<div class="pageContent">
    <table class="widefat post fixed" style="border:1px;border-color:red;">
       <thead>
        <tr>
            <th style="text-align:center;"  width="10%">ID</th>  
            <th style="text-align:center" width="30%">姓名</th>
            <th style="text-align:center" width="20%">电话</th>
            <th style="text-align:center" width="30%">邮件</th>
            <th style="text-align:center" width="10%">操作</th>
        </tr>
       </thead>
       <tbody id="modulePlayerList">
            <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" COLSPAN="8"><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            
            <c:if test="${!empty pc.entityList}">
                
                <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="id"  rel="${e.pid}">
                    <td>${e.pid}</td>
                    <td>${e.name}</td>
                    <td>${e.mobile}</td>
                    <td>${e.email}</td>
                    <td><a title="分配" class="dialog" href="/playerModule/modulePlayerSetting?pid=${e.pid}">分配</a></td>
                </tr>
                </c:forEach>

            </c:if>

       </tbody>
      </table>
     <%@include file="/inc/per_page.jsp" %>

</div>
<script>

</script>          

