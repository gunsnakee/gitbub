<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/card/batch/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}"/>
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/card/batch/list" method="post"id="batch_list_queryForm">
        <table class="searchContent pageFormContent" style="padding:0px;">
            <tr>
                <td>
                    <label>批次号:</label>
                </td>
                <td><input name="batchId" value="${batch.batchId}"/>

                <td>
                    <label>卡名称:</label>
                </td>
                <td><input name="cardName" value="${batch.cardName}"/>
                </td>

                <td>
                    <label>卡类型:</label>
                </td>
                <td>
                     <select name="cardType">
                         <option value="99" <c:if test="${batch.cardType == 99}"> selected="selected"</c:if>>全部</option>
                         <option value="1" <c:if test="${batch.cardType == 1}"> selected="selected"</c:if>>电子卡</option>
                         <option value="0" <c:if test="${batch.cardType == 0}"> selected="selected"</c:if>>实体卡</option>
                     </select>
                </td>
            </tr>
            <tr>
                <td>
                    <label>生成时间：</label>
                </td>
                <td>
                    <input type="text" name="createTimeMin" value="${batch.createTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    至
                    <input type="text" name="createTimeMax" value="${batch.createTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </td>
                <td>
                    <label>截止时间：</label>
                </td>
                <td>
                    <input type="text" name="endTimeMin" value="${batch.endTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    至
                    <input type="text" name="endTimeMax" value="${batch.endTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label>截止提前提醒时间：</label>
                </td>
                <td>
                    <input type="text" name="warnTimeMin" value="${batch.warnTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    至
                    <input type="text" name="warnTimeMax" value="${batch.warnTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </td>
                <td>
                    <label>导出状态:</label>
                </td>
                <td>
                    <select name="state">
                        <option value="99" <c:if test="${batch.state == 99}"> selected="selected"</c:if>>全部</option>
                        <option value="0" <c:if test="${batch.state == 0}"> selected="selected"</c:if>>未导出</option>
                        <option value="1" <c:if test="${batch.state == 1}"> selected="selected"</c:if>>已导出</option>
                    </select>
                </td>

                <td>
                    <div class="subBar">
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">筛   选</button>
                            </div>
                        </div>
                    </div>
                    <a class="button" href="javascript:void(0);"><span id="batchReset">重  置</span></a>
                </td>
            </tr>
        </table>
        <br/>
    </form>
</div>

<!-- ============================================== -->
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["55709978a5f820b085be3aace2699ccd"]!=null}'>
                <li><a title="按批次创建礼品卡" target="dialog" rel="ids" href="/card/batch/add" class="add" width="500"  height="400"><span>按批次创建礼品卡</span></a></li>
            </c:if>
        </ul>
    </div>
    <table class="table" width="80%" layoutH="205">
        <thead>
        <tr>
            <th align="center">批次号</th>
            <th align="center">卡类型</th>
            <th align="center">卡名称</th>
            <th align="center">面额(￥)</th>
            <th align="center">有效期(月)</th>
            <th align="center">数量</th>
            <th align="center">库存</th>
            <th align="center">截止提醒时间</th>
            <th align="center">生成时间</th>
            <th align="center">截止时间</th>
            <th align="center">礼品卡状态</th>
            <th align="center">是否导出</th>
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
                <tr <c:if test="${e.endTime < currentTime}">style="color: red" </c:if>>
                    <td>${e.batchId}</td>
                    <td><c:if test="${e.cardType == 0}">实体卡</c:if><c:if test="${e.cardType == 1}">电子卡</c:if></td>
                    <td>${e.cardName}</td>
                    <td>${e.cardPrice}</td>
                    <td>${e.validMonth}</td>
                    <td>${e.initNum}</td>
                    <td>${e.cardNum}</td>
                    <td><fmt:formatDate value="${e.warnTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td><fmt:formatDate value="${e.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td>
                        <c:choose>
                            <c:when test="${e.endTime >= currentTime }">
                                未过期
                            </c:when>
                            <c:otherwise>
                                已过期
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:if test="${e.cardType == 0}"><c:if test="${e.state == 0}">否</c:if><c:if test="${e.state == 1}">是</c:if></c:if><c:if test="${e.cardType == 1}">-</c:if></td>
                    <td align="center">
                        <c:if test='${sessionScope.bkstage_user.menus["fa5de1dce0d06ebb9e62b9c7644fd7f0"]!=null}'>
                            <span><a title="礼品卡批次详情" href="/card/batch/detail?batchId=${e.batchId}" target="navTab" style="color: #0000ff">查看详情</a></span>
                        </c:if>
                        <c:if test="${e.cardType == 0}">
                            <c:if test='${sessionScope.bkstage_user.menus["7111c665d0cea985795a0cd681f0acde"]!=null}'>
                                <span><a title="导出实体卡" href="/card/batch/batch-export?batchId=${e.batchId}" style="color: #0000ff">导出</a></span>
                            </c:if>
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
        $("#batchReset").click(function () {
            $("input[name=batchId]").val("");
            $("input[name=cardName]").val("");
            $("input[name=createTimeMin]").val("");
            $("input[name=createTimeMax]").val("");
            $("input[name=endTimeMin]").val("");
            $("input[name=endTimeMax]").val("");
            $("input[name=warnTimeMin]").val("");
            $("input[name=warnTimeMax]").val("");
            $("select[name=cardType]").val("");
            $("select[name=state]").val("");
        });
    });

</script>

