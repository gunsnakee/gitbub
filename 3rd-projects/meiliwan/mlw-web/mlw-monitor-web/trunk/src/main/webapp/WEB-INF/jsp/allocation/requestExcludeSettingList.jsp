<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<!DOCTYPE HTML>
<div class="pageHeader">
    <form id="pagerForm" onsubmit="return navTabSearch(this);" action="/allocation/requestExcludeSettingList" method="post">
        <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
        <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    </form>
</div>

<div>
    <a title="添加特殊案例" class="dialog" href="/allocation/requestExcludeSettingAddView">添加</a>
</div>
<div class="pageContent">
    <table class="widefat post fixed" style="border:1px;border-color:red;">
       <thead>
        <tr>
            <th style="text-align:center;"  width="10%">ID</th>  
            <th style="text-align:center" width="30%">类型</th>
            <th style="text-align:center" width="30%">名称</th>
            <th style="text-align:center" width="20%">时间</th>
            <th style="text-align:center" width="10%">操作</th>
        </tr>
       </thead>
       <tbody id="requestExcludeSettingList">
            <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" COLSPAN="8"><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="id"  rel="${e.rid}">
                    <td>${e.rid}</td>
                    <td>${e.type}</td>
                    <td>${e.name}</td>
                    <td>${e.resumeTime}</td>
                    <td><a class="delete" href="/allocation/requestExcludeSettingDelete?rid=${e.rid}">删除</a></td>
                </tr>
                </c:forEach>

            </c:if>

       </tbody>
      </table>
     <%@include file="/inc/per_page.jsp" %>

</div>
<script>            
$("#requestExcludeSettingList .delete").each(function(){

    $(this).click(function(e){

        var href = $(this).attr("href");
        $.mlwbox.ask('你确定要删除吗?',function(){ //'确定'
            
            $.ajax({
                type: "POST",
                url: href,
                success: function(msg){
                    
                    var $form = $("#pagerForm");
                    $form.submit();
                }
             });
        })
        e.preventDefault();

    });    

});

</script>     

