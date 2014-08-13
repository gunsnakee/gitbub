<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/card/card/import-list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/card/card/import-list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="batchNo">导入批次:</label>
                    </td>
                    <td><input id="batchNo" name="batchNo" value="${result.batchId}"/></td>
                    <td>
                        <label>导入时间：</label>
                    </td>
                    <td>
                        <input type="text" name="createTimeMin" value="${result.createTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                        至
                        <input type="text" name="createTimeMax" value="${result.createTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
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
            <th style="text-align: center">导入时间</th>
            <th style="text-align: center">操作人</th>
            <th style="text-align: center">礼品卡类型</th>
            <th style="text-align: center">导入总数</th>
            <th style="text-align: center">不匹配数量</th>
            <th style="text-align: center">发送成功数</th>
            <th style="text-align: center">发送失败数</th>
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
                    <td style="text-align: center">${e.batchId}</td>
                    <td style="text-align: center">${e.fileName}</td>
                    <td style="text-align: center"><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                    <td style="text-align: center">${e.adminName}</td>
                    <td style="text-align: center"><c:if test="${e.cardType == 0}">实体卡</c:if><c:if test="${e.cardType == 1}">电子卡</c:if></td>
                    <td style="text-align: center">${e.totalNum}</td>
                    <td style="text-align: center">${e.dismatchNum}</td>
                    <td style="text-align: center"><c:choose><c:when test="${e.cardType == 1}">${e.sendSucNum}</c:when><c:otherwise>--</c:otherwise></c:choose></td>
                    <td style="text-align: center"><c:choose><c:when test="${e.cardType == 1}">${e.sendErrorNum}</c:when><c:otherwise>--</c:otherwise></c:choose></td>
                    <td style="text-align: center"><a href="/card/card/index?handle=1&batchNo=${e.batchId}" target="navTab"><span style="color: #0000ff">查看明细</span></a></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
