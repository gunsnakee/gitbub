<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/antispam/keyword/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/antispam/keyword/list" method="post" id="queryForm">
        	<label for="word">敏感词:</label>
        	<input name="word" id="word" value="${word}"/><button type="submit">搜索</button>
    </form>
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
        		<li><a title="确实批量删除吗?" target="selectedTodo" rel="ids" href="/antispam/keyword/del" class="delete"><span>批量删除</span></a></li>
        		<li class="line">line</li>
			<li><a class="add" href="/antispam/keyword/add" target="dialog"><span>添加敏感词</span></a></li>
			<li class="line">line</li>
			<li ><a id="reload" class="edit"><span>重载敏感词</span></a></li>
        </ul>
    </div>

    <table class="table" width="900" layoutH="155" id="J-table">
        <thead>
        <tr>
            <th><input id="check_all" type="checkbox" group="ids" class="checkboxCtrl"></th>
            <th>ID</th>
            <th>敏感词</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="id" rel="${e.id}">
                    <td>
                    		<input name="ids" class="is_delete" type="checkbox" value="${e.id}">
                    </td>
                    <td>${e.id}</td>
                    <td>${e.word}</td>
                    <td>
                        <a class="delete" style="color: #0000ff" href="/antispam/keyword/del?ids=${e.id}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>
	$(function(){
		$("#reload").click(function(){
			alertMsg.confirm("确定重载吗？", {
				okCall: function(){
					$.post("/antispam/keyword/reload", {}, DWZ.ajaxDone, "json");
				}
			});
		});
	});
</script>