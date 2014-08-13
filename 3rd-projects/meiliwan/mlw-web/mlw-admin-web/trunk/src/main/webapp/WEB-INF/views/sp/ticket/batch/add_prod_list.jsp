<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/ticket/batch/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}"/>
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/ticket/batch/list" method="post">
        <div class="searchBar">
            <table class="searchContent pageFormContent" style="padding:0px;">
                <input type="hidden" name="batchId" value="${batch.batchId}">
                <tr>
                    <td><label>优惠券名称:</label></td>
                    <td>${batch.ticketName}</td>
                    <td><label>批次号:</label></td>
                    <td><c:if test="${batch.ticketType == 1}">品类券</c:if><c:if test="${batch.ticketType == 0}">通用券</c:if></td>
                    <td><label>优惠券批次号:</label></td>
                    <td>${batch.batchId}</td>
                </tr>

                <tr>
                    <td><label>面值：</label></td>
                    <td>${batch.ticketPrice}元</td>
                    <td><label>有效期开始时间:</label></td>
                    <td><fmt:formatDate value="${batch.startTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td><label>使用范围说明:</label></td>
                    <td>${batch.descp}</td>
                </tr>
                <tr>
                    <td><label>数量:</label></td>
                    <td>${batch.initNum}</td>
                    <td><label>有效期结束时间:</label></td>
                    <td><fmt:formatDate value="${batch.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["f6fe27ac3682777d56b508f1c7ed863c"]!=null}'>
                <li><a title="创建优惠券并上线?" target="ajaxTodo" href="/ticket/batch/get-on?batchId=${batch.batchId}" class="add" ><span>创建优惠券并上线</span></a></li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["174c3995a38084e785352ccdeb84e42c"]!=null}'>
                <li><a title="添加商品参与优惠券" target="navTab" rel="ids" href="/ticket/batch/get-prod-list?batchId=${batch.batchId}" class="add" ><span>添加商品</span></a></li>
            </c:if>
        </ul>
        <ul>
            <li>&nbsp;&nbsp;&nbsp;&nbsp;<span>已选择商品数量：<span style="color: red">${size}</span></span></li>
        </ul>
    </div>
    <table class="table" width="80%" layoutH="205">
        <thead>
        <tr>
            <th align="center">商品ID</th>
            <th align="center">商品标题</th>
            <th align="center">美丽价</th>
            <th align="center">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr>
                <td style="text-align: center;"><font color="#dc143c">暂无数据</font></td>
            </tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr>
                    <td>${e.proId}</td>
                    <td>${e.proName}</td>
                    <td>${e.mlwPrice}</td>
                    <td>
                        <c:if test='${sessionScope.bkstage_user.menus["174c3995a38084e785352ccdeb84e42c"]!=null}'>
                            <span><a title="确实要把商品删除吗?" target="ajaxTodo" href="/ticket/batch/del-prod?proId=${e.proId}&batchId=${batch.batchId}"class="delete" style="color: #0000ff">删除</a></span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script type="text/javascript">
    $(document).ready(function () {

    });

</script>

