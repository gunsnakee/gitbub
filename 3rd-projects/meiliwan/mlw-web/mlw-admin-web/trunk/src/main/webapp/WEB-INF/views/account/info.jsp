<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/account/info" >
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" id='uid' name="uid" value="${uid}"/>
    <input type="hidden" id='time' name="time" value="${time}"/>
    <input type="hidden" id='opt' name="opt" value="${opt}"/>
</form>
<div class="pageHeader">
<form rel="pagerForm" onsubmit="return dialogSearch(this);" action="/account/info" method="post" id="queryForm">
<input type="hidden" id='uid2' name="uid" value="${uid}"/>

<c:choose>

    <c:when test="${ob==null}">

        <h2>用户尚未开通美丽湾钱包！</h2>

    </c:when>

    <c:otherwise>

        <div class="searchBar">
            <table class="searchContent" width="60%">
                <tr>
                    <td>
                        美丽钱包 ：

                        <c:choose>

                            <c:when test="${ ob.mlwCoin != null}">
                                ${ob.mlwCoin}
                            </c:when>
                            <c:otherwise>
                                0
                            </c:otherwise>
                        </c:choose>


                    </td>
                </tr>
                <tr>
                    <td>日期：</td>
                    <td>
                        <input type="radio" name="time" value="1" <c:if test="${time eq 1}">checked</c:if> />     最近一个月
                        <input type="radio" name="time" value="2" <c:if test="${time eq 2}">checked</c:if>/>     最近三个月
                        <input type="radio" name="time" value="3" <c:if test="${time eq 3}">checked</c:if>/>     三个月之前
                    </td>


                </tr>
                <tr>
                    <td>类型：</td>
                    <td>
                            <input type="radio" name="opt" value="1" <c:if test="${opt eq 1}">checked</c:if>/>     全部
                            <input type="radio" name="opt" value="2" <c:if test="${opt eq 2}">checked</c:if> />     购物
                            <input type="radio" name="opt" value="3" <c:if test="${opt eq 3}">checked</c:if>/>     充值
                            <input type="radio" name="opt" value="4" <c:if test="${opt eq 4}">checked</c:if>/>     退款
                    </td>
                </tr>
                <tr>
                    <td>
                        <button type="submit">筛选</button>
                    </td>
                </tr>
            </table>
        </div>
        </form>
        </div>

        <div class="pageContent">
        <div class="panelBar">
        </div>
        <table class="table" width="100%" layoutH="180" id="J-table">
            <thead>
            <tr>
                <th align="center">日期</th>
                <th align="center">类型</th>
                <th align="center">存入(元)</th>
                <th align="center">支出(元)</th>
                <th align="center">详情</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty pc.entityList}">
                <tr>
                    <td style="text-align: center;"><font color="#dc143c">暂无数据</font></td>
                </tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="list" items="${pc.entityList}">
                    <tr>
                        <td><fmt:formatDate value="${list.optTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <span>
                                        <c:if  test="${list.optType == 'REFUND_FROM_GATEWAY' || list.optType == 'REFUND_FROM_FREEZE'}">
                                            <span >退款</span>
                                        </c:if >

                                        <c:if  test="${list.optType == 'SUB_MONEY' || list.optType == 'FREEZE_MONEY'}">
                                            <span >购物</span>
                                        </c:if>

                                        <c:if  test="${list.optType == 'ADD_MONEY'}">
                                            <span >充值</span>
                                        </c:if>
                             </span>
                        </td>
                        <td>
                            <c:if test="${list.optType == 'ADD_MONEY'}">
                                <span class="fc-green">+ ${list.money }</span>
                            </c:if >
                            <c:if  test ="${list.optType == 'REFUND_FROM_GATEWAY' || list.optType == 'REFUND_FROM_FREEZE'}">
                                <span class="fc-green">+ ${list.money }</span>
                            </c:if >
                        </td>

                        <td>
                            <c:if test = "${list.optType == 'SUB_MONEY' || list.optType == 'FREEZE_MONEY'}">
                                <span class="fc-red">- ${list.money }</span>
                            </c:if >
                        </td>
                        <td>
                            <c:if  test="${list.optType == 'SUB_MONEY' || list.optType == 'FREEZE_MONEY'}">
                                <p>使用美丽湾钱包支付订单<a class="fc-blue" href="http://www.meiliwan.com/uCenter/order/detail?orderId=${list.orderId}">${list.orderId}</a></p>
                                <p>支付流水号:${list.innerNum}</p>
                            </c:if >
                            <c:if  test="${list.optType == 'REFUND_FROM_GATEWAY'}">
                                <p>退换货编号 <a class="fc-blue" href="http://www.meiliwan.com/uCenter/retorder/detail?orderId=${list.orderId}">${list.orderId}</a></p>
                                <p>支付流水号 ${list.innerNum}></p>
                            </c:if >
                            <c:if  test ="${list.optType == 'REFUND_FROM_FREEZE'}">
                                <p>取消订单 <a class="fc-blue" href="http://www.meiliwan.com/uCenter/order/detail?orderId=${list.orderId}">${list.orderId}</a>返回余额</p>
                                <p>支付流水号 ${list.innerNum}</p>
                            </c:if >
                            <c:if  test="${list.optType == 'ADD_MONEY' && list.childType != 'ADD_MONEY_HB'}">
                                <p>使用 ${list.source}充值，充值单号${list.innerNum},支付流水号
                                     <c:choose>
                                         <c:when test="${list.outNum != null}">
                                             ${list.outNum}
                                         </c:when>
                                         <c:otherwise>
                                             ${list.innerNum}
                                         </c:otherwise>
                                     </c:choose>
                                </p>
                            </c:if >
                            <c:if  test="${list.optType == 'ADD_MONEY' && list.childType == 'ADD_MONEY_HB'}">
                            	<p>注册马上有钱-送 ${list.money } 元红包活动,支付流水号
                            			<c:choose>
                                       <c:when test="${list.outNum != null}">
                                           ${list.outNum}
                                       </c:when>
                                       <c:otherwise>
                                           ${list.innerNum}
                                       </c:otherwise>
                                   </c:choose>
                            	</p>
                            </c:if >
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
        <%@include file="/WEB-INF/inc/dialog_page.jsp" %>

    </c:otherwise>

</c:choose>


</div>


