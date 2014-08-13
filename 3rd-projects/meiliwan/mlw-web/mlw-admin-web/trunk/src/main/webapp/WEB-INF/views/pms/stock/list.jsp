<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/stock/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/pms/stock/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="batchNo">导入批次:</label>
                    </td>
                    <td><input id="batchNo" name="batchNo" value="${bean.batchNo}"/></td>
                    <td>
                        <label>更新时间：</label>
                    </td>
                    <td>
                        <input type="text" name="updateTimeMin" value="${bean.updateTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                        至
                        <input type="text" name="updateTimeMax" value="${bean.updateTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    </td>
                    <td>
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit">筛   选</button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <table class="table" width="1200" layoutH="130" id="J-table">
        <thead>
        <tr>
            <th style="text-align: center">导入批次号</th>
            <th style="text-align: center">文件名称</th>
            <th style="text-align: center">导入库存时间</th>
            <th style="text-align: center">操作人</th>
            <th style="text-align: center">导入总数</th>
            <th style="text-align: center">库存不足数</th>
            <th style="text-align: center">不匹配数量</th>
            <th style="text-align: center">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr>
                    <td style="text-align: center">${e.batchNo}</td>
                    <td style="text-align: center">${e.fileName}</td>
                    <td style="text-align: center"><fmt:formatDate value="${e.importTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                    <td style="text-align: center">${e.adminName}</td>
                    <td style="text-align: center">${e.totalNum}</td>
                    <td style="text-align: center">${e.stockShortNum}</td>
                    <td style="text-align: center">${e.mismatchNum}</td>
                    <td style="text-align: center"><a href="/pms/stock/index?handle=1&batchNo=${e.batchNo}" target="navTab"><span style="color: #0000ff">查看库存导入明细</span></a></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
