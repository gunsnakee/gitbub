<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>


发送结果：


<c:choose>

    <c:when test="${success==true}">

        成功

    </c:when>

    <c:otherwise>

        失败

    </c:otherwise>


</c:choose>
