<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<div class="page" >
   <div class="left">共${pc.pageInfo.totalCounts}条</div>
    <div class="inner" id="pagination">
    
    <c:if test="${1!=pc.pageInfo.page}"><a href="javascript:void(0);" rel="1">首页</a></c:if>

    <c:if test="${0==pc.pageInfo.isPrev}"><span class="prev first" href="javascript:void(0);" rel="${pc.pageInfo.isPrev}">上一页</span></c:if>
    <c:if test="${0!=pc.pageInfo.isPrev}"><a class="prev" href="javascript:void(0);" rel="${pc.pageInfo.isPrev}">上一页</a></c:if>

    <c:forEach var="row" varStatus="index" items="${pc.pageInfo.listPages}">
        <c:if test="${row==pc.pageInfo.page}"><a class="on" href="javascript:void(0);" rel="${row}">${row}</a></c:if>
        <c:if test="${row!=pc.pageInfo.page}"><a href="javascript:void(0);" rel="${row}">${row}</a></c:if>
        <c:if test="${row_index gt 3}"></c:if>
    </c:forEach>
   <c:if test="${pc.pageInfo.allPage gt 5 && pc.pageInfo.allPage!=pc.pageInfo.page}">
        <c:if test="${pc.pageInfo.allPage-1 != pc.pageInfo.page}"><span class="slh">...</span></c:if>
        <a href="javascript:void(0);" rel="${pc.pageInfo.allPage}">${pc.pageInfo.allPage}</a>
    </c:if>

    <c:if  test="${0==pc.pageInfo.isNext}"><span class="next end" href="javascript:void(0);" rel="${pc.pageInfo.isNext}">下一页</span></c:if>
    <c:if  test="${0!=pc.pageInfo.isNext}"><a class="next" href="javascript:void(0);" rel="${pc.pageInfo.isNext}">下一页</a></c:if>

    <c:if  test="${pc.pageInfo.page lt pc.pageInfo.allPage}"><a class="last" href="javascript:void(0);" rel="${pc.pageInfo.allPage}">尾页</a></c:if>

    </div>
</div>


<script>  

$("a.dialog").each(function(i){


    $(this).click(function(e){

        var href = $(this).attr("href");
        var title = $(this).attr("title");

        $.mlwdialog.open(href,title, {});
        
        e.preventDefault();
    });
    
});

$('.date').datetimepicker({
    timeFormat: "HH:mm:ss",
    dateFormat: "yy-mm-dd"
});

$("#pagerForm .reset").click(function(check){
    $("#pagerForm input").each(function(i){
        var val = $(this).val("");
            
    });
    
    $("#pagerForm select").each(function(i){
        $(this).val("")
    });
    check.preventDefault();
});

/**
 * 处理navTab上的查询, 会重新载入当前navTab
 * @param {Object} form
 */
function navTabSearch(form){
    
    var $form = $(form);

    //alert(form[pageNum]);
    if (form.pageNum){
        form.pageNum.value = 1;
    }
    MLWpageBreak($form.attr('action'),$form.serializeArray());
    return false;
}

function dialogAjaxDone(form){
    var $form = $(form);
    $.ajax({
        type: "POST",
        url: $form.attr('action'),
        data:$form.serializeArray(),
    }).done(function( msg ) {
        $.mlwdialog.close();
    });
    return false;
}
function MLWpageBreak(url,data){

   $.ajax({
      type: "POST",
      url: url,
      data:data,
    }).done(function( msg ) {
      
      $("#content").html(msg);

    });

}
$("#pagination a").each(function(index) {
        
    var $this = $(this);
    $this.click(function(check){
        var $form = $("#pagerForm");
        var form = $form[0];

        var page = $(this).attr("rel");
        form.pageNum.value = page;
        //var data={"pageNum":page};
        MLWpageBreak($form.attr('action'),$form.serializeArray());
        check.preventDefault();
    });
});



</script>

