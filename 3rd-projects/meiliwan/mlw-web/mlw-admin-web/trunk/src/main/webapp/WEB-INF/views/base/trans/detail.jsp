<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/base/transRef/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>

<div class="pageContent" id="trans_detail">
<div style=" float:left; display:block; margin:10px; overflow:auto; width:500px; height:450px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
	    
<c:forEach var="e" varStatus="i" items="${list}">
	<p style="padding: 5px;">
	<span style="padding: 0px 10px;width:40px;display:inline-block;">${e.areaName}</span>
	<span style="padding: 0px 10px;width:50px;display:inline-block;">${e.areaName1}</span>
	<span style="padding: 0px 10px;width:60px;display:inline-block;">${e.areaName2}</span>
	<span style="padding: 0px 10px;width:100px;display:inline-block;">${e.areaName3}</span>
	</p>
</c:forEach>
</div>
</div>
<script>
var ptemp="";
var ctemp="";
$("#trans_detail p span:first-child").each(function(k,v){
	var province = v.innerHTML;
	if(ptemp==province){
		v.innerHTML="";
	}else{
		ptemp=province;
	}
});
$("#trans_detail p span:nth-child(2)").each(function(k,v){
	var province = v.innerHTML;
	if(ctemp==province){
		v.innerHTML="";
	}else{
		ctemp=province;
	}
});
</script>
