<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/base/fare/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["d84f080a7ad4ef7a92778b9acfdacf6c"]!=null}'>
			<li><a class="edit" href="base/stationFare/view" target="navTab"><span>全局运费设置</span></a></li>
            </c:if>

			<li class="line">line</li>

            <c:if test='${sessionScope.bkstage_user.menus["75"]!=null}'>
            <li><a class="edit" href="base/fare/priceSetting" target="navTab"><span>运费设置</span></a></li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="100%" layoutH="130" id="J-table">
        <thead>
        <tr>
			<th align="center">序号</th>
            <th align="center">省</th>
            <th align="center">市</th>
            <th align="center">配送地区</th>
            <th align="center">类型</th>
			<th align="center">固定运费</th>
			<th align="center">未满包邮时的运费</th>
			<th align="center">满包邮时的运费</th>
            <th align="center">全局运费</th>
            <th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr>
					<td>${i.index+1}</td>
					<td>${e.province}</td>
					<td>${e.city}</td>
					<td>${e.country}</td>
                    <td>
                        <c:choose>
                               <c:when test="${e.type == 'full'}">
                                      满包邮
                               </c:when>
                               <c:when test="${e.type == 'fixed'}">
                                      固定运费
                               </c:when>
                               <c:when test="${e.type == 'free'}">
                                      免邮
                               </c:when>
                               <c:otherwise>
                                      无
                               </c:otherwise>
                        </c:choose>
                    </td>
					<td>${e.fixedPrice}</td>
					<td>${e.notFullFreePrice}</td>
					<td>${e.fullFreeLimit}</td>
                    <td>
                        <c:if test="${e.unified==1}">
                            是
                        </c:if>
                        <c:if test="${e.unified==0}">
                            否
                        </c:if>
                    </td>
                    <td><span><a title="确实要删除吗?" target="ajaxTodo" href="/base/fare/del?areaCode=${e.countryCode}" style="color: #0000ff">删除</a></span></td>
                    
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>

</script>
