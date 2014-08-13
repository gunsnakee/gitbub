<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<script language="javascript" type="text/javascript" src="/js/flot/jquery.flot.js"></script>
<script language="javascript" type="text/javascript" src="/js/flot/jquery.flot.categories.js"></script>

<div id="requestCount">
<c:forEach var="e" varStatus="i" items="${list}">
	<input type="hidden" name="${e.name}" value="${e.count}">
</c:forEach>
</div>
<div class="demo-container">
	<div id="placeholder" class="demo-placeholder"></div>
</div>

<script type="text/javascript">

var data =[];
var len = $("#requestCount input").each(function(i){
	var temp=[];
	temp[0] = $(this).attr("name")+"("+$(this).val()+")";
	temp[1] = $(this).val();
	data[i]=temp;
});

//var data = [ ["DAO", 10], ["ICES", 8], ["ICEC", 4], ["INTR", 13] ];

$.plot("#placeholder", [ { label: "the count of request", data: data, color: "#5482FF" } ], {
	series: {
		bars: {
			show: true,
			barWidth: 0.6,
			align: "center"
		}
	},
	xaxis: {
		mode: "categories",
		tickLength: 0
	}
});

</script>
