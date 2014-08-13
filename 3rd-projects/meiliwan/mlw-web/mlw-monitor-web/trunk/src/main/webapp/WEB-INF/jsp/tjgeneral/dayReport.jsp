<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<!DOCTYPE HTML>
<style> tbody tr td:nth-child(n*6){ border-right: 2px solid #333; } </style>
<div class="pageHeader" style="width:100%;">
    <form id="pagerForm" onsubmit="return navTabSearch(this);" action="/tjgeneral/dayReport" method="post"
          name="order_wait_queryForm">

        <input type="hidden" name="startTimeDesc" value="${search.startTimeDesc}"/>
        <input type="hidden" name="timeConsumeDesc" value="${search.timeConsumeDesc}"/>

        <div class="searchBar" id="product_list_searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td><label>类型:</label></td>
                    <td>

                    </td>

                    <td></td>
                    <td></td>
                    <td><label>开始时间:</label></td>
                    <td><input type="text" name="startTime" value='<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd 00:00:00"/>' class="date" dateFmt="yyyy-MM-dd HH:mm:ss"   style="float:none"/>
                        至<input type="text" name="endTime" value='<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd 00:00:00"/>' class="date" dateFmt="yyyy-MM-dd HH:mm:ss"   style="float:none"/></td>
                </tr>

                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>
                        <button  type="submit" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" aria-disabled="false"><span class="ui-button-text">筛选</span></button>

                        <%--<button  type="reset" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only reset" role="button" aria-disabled="false">
                            <span class="ui-button-text">重置</span></button>--%>
                    </td>

                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent" style="width:100%;">
    <table class="widefat post fixed" style="border:1px;border-color:red;">
        <thead>
        <tr>
            <th style="text-align:center;border-right: 2px solid #333;" width="160"></th>
            <c:if test="${!empty dateList}">
                <c:forEach var="e" varStatus="i" items="${dateList}">
                    <th colspan="8" style="text-align:center;border-right: 2px solid #333;" width="520">${e}</th>
                </c:forEach>
            </c:if>
        </tr>
        <tr>
            <th style="text-align:center;border-right: 2px solid #333;"  width="">渠道</th>
            <c:if test="${!empty dateList}">
                <c:forEach var="e" varStatus="i" items="${dateList}">
                    <th style="text-align:center;" width="">下单</br>金额(元)</th>
                    <th style="text-align:center;" width="">下单数</th>
                    <th style="text-align:center;" width="">登录</th>
                    <th style="text-align:center;" width="">注册</th>
                    <th style="text-align:center;" width="">UV</th>
                    <th style="text-align:center;" width="" >PV</th>
                    <th style="text-align:center;" width="">客单价(元)</th>
                    <th style="text-align:center;border-right: 2px solid #333;" width="" >下单</br>转化率(‰)</th>

                </c:forEach>
            </c:if>
        </tr>
        </thead>
        <tbody id="log_content">
        <c:if test="${empty tjgvvMap}">
            <tr><td style="text-align: center;" COLSPAN="8"><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>

        <c:if test="${!empty tjgvTotal}">
            <tr target="id"  rel="${tjgvTotal.host}">
                <th style="border-right: 2px solid #333;text-align:center;">${tjgvTotal.host}</th>
                <c:if test="${!empty tjgvTotal.dateIndexValues}">
                    <c:forEach var="tjgTotal" varStatus="i" items="${tjgvTotal.dateIndexValues}">
                        <th  style="text-align:center;" ><fmt:formatNumber value="${tjgTotal.ordSaleAmountYuan}" pattern="#,##0"/></th>
                        <th  style="text-align:center;<c:if test="${tjgTotal.ord!='0'}"> color:red;</c:if>"><fmt:formatNumber value="${tjgTotal.ord}" pattern="#,##0"/></th>
                        <th  style="text-align:center;<c:if test="${tjgTotal.login!='0'}"> color:red;</c:if>"><fmt:formatNumber value="${tjgTotal.login}" pattern="#,##0"/></th>
                        <th  style="text-align:center;<c:if test="${tjgTotal.register!='0'}"> color:red;</c:if>"><fmt:formatNumber value="${tjgTotal.register}" pattern="#,##0"/></th>
                        <th  style="text-align:center;"><fmt:formatNumber value="${tjgTotal.uv}" pattern="#,##0"/></th>
                        <th  style="text-align:center;"><fmt:formatNumber value="${tjgTotal.pv}" pattern="#,##0"/></th>
                        <th  style="text-align:center;">${tjgTotal.perOrdPrice}</th>    <!-- 客单价 -->
                        <th  align="center" style="text-align:center;border-right: 2px solid #333;"><fmt:formatNumber value="${tjgTotal.ordConversionRate}" pattern="#,##0.##"/></th>   <!-- 下单转化率 -->
                    </c:forEach>
                </c:if>
            </tr>
        </c:if>

        <%--<c:if test="${!empty tjgvTotal}">
            <tr target="id"  rel="${tjgvTotal.host}">
                <th  align="center" style="border-right: 2px solid #333;">${tjgvTotal.host}</th>
                <c:if test="${!empty tjgvTotal.dateIndexValues}">
                    <c:forEach var="tjgTotal" varStatus="i" items="${tjgvTotal.dateIndexValues}">
                        <th  align="center" ><fmt:formatNumber value="${tjgTotal.ordSaleAmountYuan}" pattern="#,##0"/></th>
                        <th  align="center"><fmt:formatNumber value="${tjgTotal.ord}" pattern="#,##0"/></th>
                        <th  align="center"><fmt:formatNumber value="${tjgTotal.login}" pattern="#,##0"/></th>
                        <th  align="center"><fmt:formatNumber value="${tjgTotal.register}" pattern="#,##0"/></th>
                        <th  align="center"><fmt:formatNumber value="${tjgTotal.uv}" pattern="#,##0"/></th>
                        <th  align="center"><fmt:formatNumber value="${tjgTotal.pv}" pattern="#,##0"/></th>
                        <th  align="center">${tjgTotal.perOrdPrice}</th>    <!-- 客单价 -->
                        <th  align="center" style="border-right: 2px solid #333;"><fmt:formatNumber value="${tjgTotal.ordConversionRate}" pattern="#,##0.##"/></th>   <!-- 下单转化率 -->
                    </c:forEach>
                </c:if>
            </tr>
        </c:if>--%>



        <c:if test="${!empty tjgvvMap}">

            <c:forEach var="e" varStatus="i" items="${tjgvvMap}">
                <tr target="id"  rel="${e.host}">
                    <td  align="center" style="border-right: 2px solid #333;">${e.host}</td>
                    <c:if test="${!empty e.dateIndexValues}">
                        <c:forEach var="tjg" varStatus="i" items="${e.dateIndexValues}">
                            <td  align="center" ><fmt:formatNumber value="${tjg.ordSaleAmountYuan}" pattern="#,##0"/></td>
                            <td  align="center" <c:if test="${tjg.ord!='0'}">style='color:red;'</c:if>><fmt:formatNumber value="${tjg.ord}" pattern="#,##0"/></td>
                            <td  align="center" <c:if test="${tjg.login!='0'}">style='color:red;'</c:if>><fmt:formatNumber value="${tjg.login}" pattern="#,##0"/></td>
                            <td  align="center" <c:if test="${tjg.register!='0'}">style='color:red;'</c:if>><fmt:formatNumber value="${tjg.register}" pattern="#,##0"/></td>
                            <td  align="center"><fmt:formatNumber value="${tjg.uv}" pattern="#,##0"/></td>
                            <td  align="center"><fmt:formatNumber value="${tjg.pv}" pattern="#,##0"/></td>
                            <td  align="center">${tjg.perOrdPrice}</td>    <!-- 客单价 -->
                            <td  align="center" style="border-right: 2px solid #333;"><fmt:formatNumber value="${tjg.ordConversionRate}" pattern="#,##0.##"/></td>   <!-- 下单转化率 -->
                        </c:forEach>
                    </c:if>
                </tr>
            </c:forEach>
        </c:if>

        </tbody>
    </table>

</div>
<script>
    //初始化时间控件
    $('.date').datetimepicker({
        timeFormat: "00:00:00",
        dateFormat: "yy-mm-dd"
    });
</script>