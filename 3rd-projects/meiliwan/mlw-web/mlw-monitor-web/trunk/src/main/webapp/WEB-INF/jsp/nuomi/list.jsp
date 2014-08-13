<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<!DOCTYPE HTML>
<div class="pageHeader">
    <form id="pagerForm" onsubmit="return navTabSearch(this);" action="/nuomi/list" method="post"
           name="order_wait_queryForm">
        <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
        <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    </form>
</div>
<pre>
    要转变的值
    糯米 
    1.单规:直接填商品ID
    2.多规:填入商品ID＋商品名称＋规格
    窝窝团
    1.没有款式,填入"货品编码"
    2.有款式  ,填入"货品编码"+"款式"
</pre>
<div>
    <a title="添加" class="dialog"  href="/nuomi/addView">添加</a>
</div>
<div class="pageContent">
    <table class="widefat post fixed" style="border:1px;border-color:red;">
       <thead>
        <tr>
            <th style="text-align:center;"  width="10%">ID</th>  
            <th style="text-align:center" width="40%">要转变的值</th>
            <th style="text-align:center" width="40%">对应的美丽湾的SKU</th>
            <th style="text-align:center" width="40%">类型</th>
            <th style="text-align:center" width="10%">操作</th>
        </tr>
       </thead>
       <tbody id="player_list_content">
            <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" COLSPAN="8"><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            
            <c:if test="${!empty pc.entityList}">
                

                <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="id"  rel="${e.id}">
                    <td>${e.id}</td>
                    <td>${e.code}</td>
                    <td>${e.value}</td>
                    <td>${e.type}</td>
                    <td><a href="" class="delete" rel="${e.id}">删除</a></td>
                </tr>
                </c:forEach>

            </c:if>

       </tbody>
      </table>
     <%@include file="/inc/per_page.jsp" %>

</div>
<script>    

$("#player_list_content .delete").each(function(){

    $(this).click(function(e){

        var id = $(this).attr("rel");
        $.mlwbox.ask('你确定要删除吗?',function(){ //'确定'
            
            $.ajax({
                type: "POST",
                url: "/nuomi/delete",
                data: {"id":id},
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

