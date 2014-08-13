<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/ticket/batch/detail">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/ticket/batch/detail" method="post">
        <input type="hidden" name="batchId" value="${batch.batchId}"/>
        <input type="hidden" name="optType" value="${optType}"/>
        <div class="searchBar">
            <table class="searchContent pageFormContent" style="padding:0px;">
                <tr>
                    <td>
                        <label>批次号:</label>
                    </td>
                    <td>${batch.batchId}</td>
                    <td>
                        <label>面值:</label>
                    </td>
                    <td>${batch.ticketPrice}元</td>
                    <td><label>生成日期：</label></td>
                    <td>
                        <span><fmt:formatDate value="${batch.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></span>
                    </td>

                </tr>
                <tr>
                    <td>券类型:</td>
                    <td><c:if test="${batch.ticketType == 0}">通用券</c:if><c:if test="${batch.ticketType == 1}">品类券</c:if></td>

                    <td>总数量(参考值)</td>
                    <td>${batch.initNum}</td>
                    <td><label>有效期开始时间：</label></td>
                    <td>
                        <span><fmt:formatDate value="${batch.startTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></span>
                    </td>
                </tr>

                <tr>
                    <td><label>券名称：</label></td>
                    <td>${batch.ticketName}</td>
                    <td><label>使用范围说明：</label></td>
                    <td>${batch.descp}</td>
                    <td><label>有效期结束时间：</label></td>
                    <td>
                        <span><fmt:formatDate value="${batch.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></span>
                    </td>
                </tr>

                <tr>
                    <td><label>券批次状态：</label></td>
                    <td>
                        <c:choose>
                            <c:when test="${batch.state > 0}">
                                <c:if test="${batch.endTime >= currentTime && batch.startTime <= currentTime}">
                                    进行中<c:if test="${batch.disable == 1}">(已停用)</c:if>
                                </c:if>
                                <c:if test="${batch.endTime < currentTime}">
                                    已结束<c:if test="${batch.disable == 1}">(已停用)</c:if>
                                </c:if>
                                <c:if test="${batch.startTime > currentTime}">
                                    未开始<c:if test="${batch.disable == 1}">(已停用)</c:if>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                未上线
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><label>使用范围:</label></td>
                    <td><c:if test="${batch.ticketType == 0}">全站使用</c:if><c:if test="${batch.ticketType == 1}"> <span><a title="查看" href="/ticket/batch/list?batchId=${batch.batchId}&selType=1&viewType=1" target="dialog" width="800" height="550" style="color: #0000ff">查看</a></span></c:if></td>
                    <td><label>后台创建人:</label></td>
                    <td>${admin.adminName}</td>
                </tr>

                <tr>
                    <td>
                        <label>停用原因:</label>
                    </td>
                    <td><c:if test="${empty batch.disableDescp}">暂无</c:if><c:if test="${!empty batch.disableDescp}">${batch.disableDescp}</c:if></td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="tabs" currentIndex="<c:if test="${optType==1}">0</c:if><c:if test="${optType==0}">1</c:if>">
<%--tag状态栏--%>
<div class="tabsHeader">
    <div class="tabsHeaderContent">
        <ul>
            <li>
                <a title="批量发送记录" href="/ticket/batch/detail?batchId=${batch.batchId}&optType=1" target="navTab" rel="2972b1e24307bfd926d6f4dc9e4bda27">
                    <span>批量发送记录</span>
                </a>
            </li>
            <li>
                <a title="单个发送记录" href="/ticket/batch/detail?batchId=${batch.batchId}&optType=0" target="navTab" rel="2972b1e24307bfd926d6f4dc9e4bda27">
                    <span>单个发送记录</span>
                </a>
            </li>

        </ul>
    </div>
</div>
</div>
<!-- ============================================== -->
<c:if test="${optType == 1}">
    <div class="pageContent">
        <table class="table" width="80%" layoutH="205">
            <thead>
            <tr>
                <th align="center">发送时间</th>
                <th align="center">操作人</th>
                <th align="center">导入数量</th>
                <th align="center">发送成功数量</th>
                <th align="center">发送的美丽湾账户不存在-未发送数</th>
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
                        <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>${e.adminName}</td>
                        <td>${e.totalNum}</td>
                        <td>${e.sucNum}</td>
                        <td>${e.dismatchNun}</td>
                        <td>
                            <c:if test='${sessionScope.bkstage_user.menus["d6b33f2eed97522a480586064d3360af"]!=null}'>
                                <span><a title="查看明细" href="/ticket/batch/detail?importId=${e.id}&batchId=${e.batchId}&fileName=${e.fileName}&handle=1" target="navTab" style="color: #0000ff">查看明细</a></span>
                            </c:if>
                            <c:if test='${sessionScope.bkstage_user.menus["dce689184b97c2915586793df57ca200"]!=null}'>
                                <span><a title="下载导入模板" href="/ticket/batch/send-ticket?batchId=${e.batchId}&optType=1" target="dialog" style="color: #0000ff">下载导入文档</a></span>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
</c:if>
<c:if test="${optType == 0}">
    <div class="pageContent">
        <table class="table" width="80%" layoutH="205">
            <thead>
            <tr>
                <th align="center">优惠券号</th>
                <th align="center">发送时间</th>
                <th align="center">操作人</th>
                <th align="center">发送手机</th>
                <th align="center">发送邮箱</th>
                <th align="center">发送美丽湾账户</th>
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
                        <td>${e.detail.ticketId}</td>
                        <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>${e.adminName}</td>
                        <td><c:if test="${empty e.detail.buyerPhone}">--</c:if><c:if test="${!empty e.detail.buyerPhone}">${e.detail.buyerPhone}</c:if></td>
                        <td><c:if test="${empty e.detail.buyerEmail}">--</c:if><c:if test="${!empty e.detail.buyerEmail}">${e.detail.buyerEmail}</c:if></td>
                        <td><c:if test="${empty e.detail.userName}">--</c:if><c:if test="${!empty e.detail.userName}">${e.detail.userName}</c:if></td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
        <%@include file="/WEB-INF/inc/per_page.jsp" %>
    </div>
</c:if>

