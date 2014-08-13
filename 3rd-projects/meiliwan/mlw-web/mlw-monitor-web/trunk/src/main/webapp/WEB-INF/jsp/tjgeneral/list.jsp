<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<!DOCTYPE HTML>
<div class="pageHeader">
    <form id="pagerForm" onsubmit="return navTabSearch(this);" action="/tjgeneral/list" method="post"
           name="order_wait_queryForm">
          <input type="hidden" name="indexCode" value="${indexCode}"/>

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

<div class="pageContent">
    <table class="widefat post fixed" style="border:1px;border-color:red;">
       <thead>
        <tr>
            <th style="text-align:center;"  width="12%">来源\时间</th>
            <c:if test="${!empty dateList}">
               <c:forEach var="e" varStatus="i" items="${dateList}">
                   <th style="text-align:center;border:1px" width="5%">${e}</th>
               </c:forEach>
            </c:if>
        </tr>
       </thead>
       <tbody id="log_content">
            <c:if test="${empty tjgvvMap}">
            <tr><td style="text-align: center;" COLSPAN="8"><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            
            <c:if test="${!empty tjgvvMap}">

                <c:forEach var="e" varStatus="i" items="${tjgvvMap}">
                <tr target="id"  rel="${e.host}">
                    <td  align="center">${e.host}</td>
                    <c:if test="${!empty e.dateIndexValue}">
                        <c:forEach var="indexValue" varStatus="i" items="${e.dateIndexValue}">
                            <td  align="center">
                                <span <c:if test="${indexCode=='register'}"> <c:if test="${indexValue!='0'}"> style='color:red'</c:if></c:if>
                                        <c:if test="${indexCode=='ord'}"> <c:if test="${indexValue!='0'}"> style='color:red'</c:if></c:if>
                                    ><fmt:formatNumber value="${indexValue}" pattern="#,##0"/></span></td>
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

