<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<!DOCTYPE HTML>

<style type="text/css">
.mlwTab {
    cursor: pointer;
    border: solid rgb(184, 220, 231) 2px;
    padding: 4px;
}
.temp {
    background: #eaf3fa;
}
tr td {
    align:center;
}
</style>

<div class="pageHeader">
    <form id="pagerForm" onsubmit="return navTabSearch(this);" action="/monitor/MLWLogList" method="post" >
        <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
        <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>

			<table class="searchContent pageFormContent">
				<tr>
                    <td><label>级别:</label></td>
                    <td>
                        <select name="level" id="MLWLogListLevel">
                                <option value="">全部</option>
                            <option value="ERROR" <c:if test="${ search.level=='ERROR'}">selected="selected"</c:if>>ERROR(错误)</option>
                             <option value="WARN" <c:if test="${ search.level=='WARN'}">selected="selected"</c:if>>WARN(警告)</option>
                             <option value="INFO" <c:if test="${ search.level=='INFO'}">selected="selected"</c:if>>INFO(信息)</option>
                             <option value="DEBUG" <c:if test="${ search.level=='DEBUG'}">selected="selected"</c:if>>DEBUG(调式)</option>
                        </select>
                    </td>
					
                    
                    <td><label>模块:</label></td>
                    <td>
                        <input type="text" name="module" value='${search.module}' style="float:none"/>
                    </td>
                    <td><label>应用:</label></td>
                    <td>
                        <input id="application_select_result" type="text" name="application" value='${search.application}' style="float:none"/>
                        
                        <select name="module_select" id="application_select" style="float:none">
                            <option value="">全部</option>
                            <c:forEach var="e" items="${app}">
                                <option value="${e.code}" <c:if test="${e.code == search.application}">selected="selected"</c:if>>${e.code}</option>
                            </c:forEach>
                        </select>

                    </td>
                </tr>
				<tr>                   
                    <td>服务端IP</td>
                     <td>
                       <input type="text" name="serverIp" value='${search.serverIp}' style="float:none"/>
                    </td>
                    <td>客户端IP</td>
                     <td>
                       <input type="text" name="clientIp" value='${search.clientIp}' style="float:none"/>
                    </td>
                    <td><label>创建时间:</label></td>
                    <td><input id="createTimeStart" type="text" name="createTimeStart" value='<fmt:formatDate value="${search.createTimeStart}" pattern="yyyy-MM-dd HH:mm:ss"/>'  class="date" style="float:none"/>至<input id="createTimeEnd" type="text" name="createTimeEnd" value='<fmt:formatDate value="${search.createTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>' class="date"/>
                    </td>
                </tr>
				
                <tr>
                    <td><label>ID:</label></td>
                    <td><input type="text" class="digits" name="id" value='${search.id}' style="float:none"/></td>
                     
                    <td><label></label></td>
                    <td><label></label></td>
                    <td><label></label></td>
                    <td>
                        <button  type="submit" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" aria-disabled="false"><span class="ui-button-text">筛选</span></button>

                        <button  type="reset" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only reset" role="button" aria-disabled="false"><span class="ui-button-text">重置</span></button>
                    </td>
                </tr>

			</table>
		    <div id="MLWLogTAB" >
                <span class="mlwTab">ERROR</span> 
                <span  class="mlwTab">WARN</span>
                <span  class="mlwTab">INFO</span></div>
		

    </form>
</div>


      <table class="widefat post fixed" style="border:1px;border-color:red;">
       <thead>
        <tr>
            <th style="text-align:center;"  width="2%"></th> 
            <th style="text-align:center;"  width="5%">ID</th>  
            <th style="text-align:center;border:1px" width="5%">级别</th>
            <th style="text-align:center" width="10%">模块</th>
            <th style="text-align:center" width="12%">应用</th>
            <th style="text-align:center" width="30%">标题</th>
            <th style="text-align:center" width="9%">服务端IP</th>
            <th style="text-align:center" width="9%">客户端IP</th>
            <th style="text-align:center" width="13%">创建时间</th>
            <th style="text-align:center" width="5%">操作</th>
        </tr>
       </thead>
       <tbody id="log_content">
            <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" COLSPAN="9"><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            
            <c:if test="${!empty pc.entityList}">
                
                <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                    <tr target="id"  rel="${e.id}">
                        <td class="click" uuid="${e.errorUuid}">+</td>
                        <td ><a target="_blank" href="/monitor/getMLWLogById?id=${e.id}">${e.id}</a></td>
                        <td  align="center">${e.level}</td>
                        <td  align="center">${e.module}</td>
                        <td  align="center">${e.application}</td>
                        <td  align="center"><a title="日志信息" class="dialog" href="/monitor/getMLWLogById?id=${e.id}">
                            <c:choose> 
                            <c:when test="${fn:length(e.title) > 90}"> 
                             <c:out value="${fn:substring(e.title, 0, 90)}" /> 
                            </c:when> 
                            <c:otherwise> 
                             <c:out value="${e.title}" /> 
                            </c:otherwise> 
                           </c:choose> </a>
                        </td>
                        <td>
                            ${e.serverIp}
                        </td>
                        <td>
                            ${e.clientIp}
                        </td>
                        <td  align="center"><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>

                        <td align="center"><a target="_blank" width="645" height="470" href="/monitor/getMLWLogById?id=${e.id}">查看</a></td>
                    </tr>
                </c:forEach>
            </c:if>

       </tbody>
      </table>
     <%@include file="/inc/per_page.jsp" %>

</div>
<script>   
$(function(){
var dateformat=function (value, type) {
    if (!type) {
        type = value;
        value = new Date();
    }
    if ($.type(value) !== "date") {
        value = new Date();
    }
    return type.replace(/yyyy/g, value.getFullYear()).replace(/mm/g, (value.getMonth() + 1) < 10 ? "0" + (value.getMonth() + 1) : value.getMonth() + 1).replace(/dd/g, value.getDate() < 10 ? "0" + value.getDate() : value.getDate()).replace(/HH/g, value.getHours() < 10 ? "0" + value.getHours() : value.getHours()).replace(/MM/g, value.getMinutes() < 10 ? "0" + value.getMinutes() : value.getMinutes()).replace(/SS/g, value.getSeconds() < 10 ? "0" + value.getSeconds() : value.getSeconds());
}

$(".click").click(function(){
    //alert($(this).parents("tr").next().html());
    var $this=$(this);
    var text = $this.text();
    if(text=="+"){
        
        var uuid= $(this).attr("uuid");
        var temp="";
        $.ajax({
            type: "POST",
            url: "/monitor/getByUuid",
            data: {"errorUuid":uuid},
            success: function(msg){
                if(msg){
                    for(var i=0;i<msg.length;i++){
                        var clientIp=msg[i].clientIp?msg[i].clientIp:"";
                        var dd = dateformat(msg[i].createTime,"yyyy-mm-dd HH:MM:SS");
                        temp+="<tr class='temp'><td></td><td>"+msg[i].id+"</td><td>"+msg[i].level+"</td><td>"+msg[i].module+"</td><td>"+msg[i].application+"</td><td>"+msg[i].title+"</td><td>"+msg[i].serverIp+"</td><td>"+clientIp+"</td><td>"+dd+"</td><td><a target='_blank' href='/monitor/getMLWLogById?id="+msg[i].id+"'>查看</a></td></tr>";

                    }
                    $this.parent().after(temp);
                }
            }
         });
        
        $this.text("-");
    }else{
        $("tr.temp").hide();
        $(this).text("+");
    }

    return false;
});


$("#MLWLogTAB span").each(function(){

    var $this = $(this);
    $(this).click(function(){
        $("#MLWLogListLevel").val($this.text());
        $("#pagerForm").submit();
    });

});


$("#application_select").change(function(){
    var val = $(this).val();
    
    $("#application_select_result").val(val);
});

});
</script>			

