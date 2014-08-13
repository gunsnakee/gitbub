<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="page unitBox" style="display: block;">
    <h2 class="contentTitle">礼品卡批次详情</h2>
    <div class="pageFormContent" layouth="60" style="height: 249px; overflow: auto;">
        <c:if test="${!empty card}">
            <fieldset>
                <legend>礼品卡批次详情</legend>
                <dl>
                    <dt>卡批次号：</dt>
                    <dd>${card.batch.batchId}</dd>
                </dl>

                <dl>
                    <dt>礼品卡类型：</dt>
                    <dd><c:if test="${card.batch.cardType == 0}">实体卡</c:if><c:if test="${card.batch.cardType == 1}">电子卡</c:if></dd>
                </dl>
                <dl>
                    <dt>卡名称：</dt>
                    <dd>${card.batch.cardName}</dd>
                </dl>
                <dl>
                    <dt>面  额：</dt>
                    <dd>￥${card.batch.cardPrice}</dd>
                </dl>
                <dl>
                    <dt>有效期：</dt>
                    <dd>${card.batch.validMonth}个月</dd>
                </dl>
                <dl>
                    <dt>数  量：</dt>
                    <dd>${card.batch.initNum}</dd>
                </dl>
                <dl>
                    <dt>生成时间：</dt>
                    <dd><fmt:formatDate value="${card.batch.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></dd>
                </dl>
                <dl>
                    <dt>截止日提醒时间：</dt>
                    <dd><fmt:formatDate value="${card.batch.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></dd>
                </dl>
                <dl class="nowrap">
                    <dt>截止日提醒邮箱：</dt>
                    <dd>${card.batch.adminEmail}</dd>
                </dl>
            </fieldset>
            <fieldset>
                <legend>礼品卡详情</legend>
                <dl>
                    <dt>礼品卡卡号：</dt>
                    <dd>${card.cardId}</dd>
                </dl>
                <dl>
                    <dt>有效期开始时间：</dt>
                    <dd><fmt:formatDate value="${card.batch.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></dd>
                </dl>
                <dl>
                    <dt>有效期截止时间：</dt>
                    <dd><fmt:formatDate value="${card.batch.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></dd>
                </dl>
                <dl>
                    <dt>用户激活时间：</dt>
                    <dd><c:choose><c:when test="${card.state == 1 }"><fmt:formatDate value="${card.activeTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></c:when><c:otherwise>-</c:otherwise></c:choose></dd>
                </dl>
                <dl>
                    <dt>绑定人：</dt>
                    <dd><c:choose><c:when test="${card.state == 1 }">${card.userName}</c:when><c:otherwise>暂无</c:otherwise></c:choose></dd>
                </dl>
                <dl>
                    <dt>冻结状态：</dt>
                    <dd><c:if test="${card.isFreeze == 0}">正常</c:if><c:if test="${card.isFreeze == 1}">已冻结</c:if></dd>
                </dl>
                <dl>
                    <dt>激活状态：</dt>
                    <dd><c:if test="${card.state == 0}">未激活</c:if><c:if test="${card.state == 1}">已激活</c:if></dd>
                </dl>
                <dl>
                    <dt>销售状态：</dt>
                    <dd><c:if test="${card.isSell == 0}">未销售</c:if><c:if test="${card.isSell == 1}">已销售</c:if></dd>
                </dl>
            </fieldset>
            <fieldset>
                <legend>礼品卡操作记录</legend>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">操作时间</th>
                        <th style="text-align: center">事件描述</th>
                        <th style="text-align: center">操作人</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty card.logs}">
                        <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty card.logs}">
                        <c:forEach var="e" items="${card.logs}">
                            <tr>
                                <td style="text-align: center"><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                <td style="text-align: center">${e.descp}</td>
                                <td style="text-align: center">${e.userName}</td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </fieldset>
        </c:if>
    </div>
</div>
