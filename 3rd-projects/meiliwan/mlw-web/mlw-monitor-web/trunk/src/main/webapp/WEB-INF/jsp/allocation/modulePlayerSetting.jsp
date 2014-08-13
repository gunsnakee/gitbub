<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div id="playerModuleSetting">
	<form method="post" action="/playerModule/add" class="pageForm required-validate" onsubmit="return dialogAjaxDone(this)">
		
		<input id="playerModuleSettingPid" type="hidden" name="pid" value="${pid}">
		<input type="text" name="moduleName" value="">
	    <button type="submit" onkeydown="if(13==event.keyCode){return false;}">添加</button>
    </form>

    <c:forEach var="e" varStatus="i" items="${modules}">
        <div style="width:190px;float: left;">
        	${e.moduleName} <span class="ui-icon ui-icon-close" style="float: left;cursor: pointer;" rel="${e.moduleName}"></span> 
		</div>
		<c:if test="${(i.index+1)%3==0}"></br></c:if>    
    </c:forEach>
</div>
<script>
$("#playerModuleSetting .ui-icon-close").each(function(){
	var $this=$(this);
	$this.click(function(){

    	var mName = $this.attr("rel");
    	var pid = $("#playerModuleSettingPid").val();
    	var data = {};
    	data.moduleName=mName;
    	data.pid=pid;
    	$.ajax({
          	type: "POST",
          	url: "/playerModule/delete",
          	data:data,
        }).done(function( msg ) {
			$this.parent().remove();
        });

    });
});
</script>   