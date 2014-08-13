<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <div class="pageFormContent" layoutH="56">
    <c:if test="${!empty printList}">
        <span>已打印：${fn:length(printList)} 次</span>
        <table style="width:500px;">
        <c:forEach var="printLog" items="${printList}" begin="0" varStatus="rs">
            <tr style="height: 30px;">
                <th><fmt:formatDate value="${printLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></th>
                <td>${printLog.optUname}</td>
                <td>打印了1次</td>
            </tr>
            </c:forEach>
        </table>
    </c:if>
    </div>
    <div class="buttonContent" style="float: right;"><button type="button" class="close">关闭</button></div>
</div>