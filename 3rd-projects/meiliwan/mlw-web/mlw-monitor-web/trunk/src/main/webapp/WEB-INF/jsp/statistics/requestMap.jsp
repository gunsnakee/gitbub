<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<script src="/js/asset/js/esl/esl.js"></script>
<script src="/js/asset/js/codemirror.js"></script>

<script src="/js/echarts-original-map.js"></script>

<div id="requestMapData">
    <c:forEach var="e" varStatus="i" items="${map}">
        <input type="hidden" name="${e.key}" value="${e.value}">
    </c:forEach>
</div>
<input id="requestMapMax" type="hidden" name="max" value="${max}">

<div id="main" class="main" style="height: 530px;"></div>
       

<script src="/js/asset/js/bootstrap-transition.js"></script>

<script src="/js/asset/js/bootstrap-modal.js"></script>
<script src="/js/asset/js/bootstrap-dropdown.js"></script>
<script src="/js/asset/js/bootstrap-scrollspy.js"></script>

<script src="/js/asset/js/bootstrap-tooltip.js"></script>
<script src="/js/asset/js/bootstrap-popover.js"></script>

<script src="/js/asset/js/bootstrap-collapse.js"></script>
<script src="/js/asset/js/bootstrap-carousel.js"></script>
<script src="/js/asset/js/bootstrap-typeahead.js"></script>
<script src="/js/asset/js/echartsExample.js"></script>
