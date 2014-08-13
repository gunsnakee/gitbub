<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<form id="pagerForm" method="post" action="/mms/sms/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/mms/sms/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label>发送时间：</label>
                        <input type="text" name="startTime" value='<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>&nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" name="endTime" value='<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
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
                    <td>
                    	<a style="color: #0000FF" href="/mms/sms/export?fileName=demo.txt">下载批量发送格式表</a>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <form action="/mms/sms/list" id="smsForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="给一个或多个用户发送短信！" href="/mms/sms/tosend"  target="dialog" class="add"><span>编辑短信</span></a></li>
            </ul>
        </div>

    <table class="table" width="1400" layoutH="180" id="J-table">
        <thead>
        	<tr>
            <th width="5%"> ID</th>
            <th width="8%">发送者</th>
            <th width="8%">接收号码</th>
            <th width="12%">发送时间</th>
            <th width="50%">短信内容</th>
            <th width="6%">状态(成功/总数)</th>
        	</tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr target="id" rel="${e.id}">
                    <td>${e.id}</td>
                    <td>${e.adminName}</td>
                    <c:if test="${fn:length(e.smsItemList) > 1}">
                    	<td><a style="color: #0000FF" href="/mms/sms/export?fileName=${e.fileName}">批量号码：<c:out value="${fn:length(e.smsItemList)}"></c:out> 个</a></td>
                    </c:if>
                    <c:if test="${fn:length(e.smsItemList) == 1}">
                    	<td>${e.smsItemList[0].mobile}</td>
                    </c:if>
                    <td><fmt:formatDate value="${e.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td style="word-break:break-all;" title="${e.content}">${e.content}</td>
                    <td title="${e.content}">${e.goodCount}/${fn:length(e.smsItemList)}</td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>