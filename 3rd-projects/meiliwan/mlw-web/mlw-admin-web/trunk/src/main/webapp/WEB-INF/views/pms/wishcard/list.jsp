<%@ page language="java" pageEncoding="UTF-8" %>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" class="orderPagerInputForm" action="/pms/wishcard/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="length" value="${pc.pageInfo.totalCounts}"/>
</form>
<%--搜索条件区--%>
<div class="pageHeader">
    <form rel="pagerForm" class="required-validate" onsubmit="return navTabSearch(this);" action="/pms/wishcard/list"
          method="post" id="wish_card_list_form" name="wish_card_list_form">
        <div class="searchBar" id="product_list_searchBar">
            <table class="searchContent">
                <tr>
                    <td><label>提交时间</label>
                        <input type="text" name="createTimeMin" id="createTimeMin"
                               value='${createTimeMin}'
                               class="date" dateFmt="yyyy-MM-dd HH:mm:ss" style="float:none"/>至
                        <input type="text" name="createTimeMax" id="createTimeMax"
                               value='${createTimeMax}'
                               class="date" dateFmt="yyyy-MM-dd HH:mm:ss" style="float:none"/>
                    </td>
                    <td><label>产地</label>
                        <select name="sourceCountryName" id="sourceCountryName">
                            <option value="-1" selected>全部</option>
                            <c:forEach var="sc" items="${sourceCountryList}" varStatus="scstatus">
                                <option value="${sc.placeName}" <c:if test="${search.sourceCountryName == sc.placeName}">selected</c:if> >${sc.placeName}</option>
                            </c:forEach>
                        </select></td>
                    <td><label>是否备注:</label>
                        <select name="isRemark" id="isRemark">
                            <option value="-1" <c:if test="${empty isRemark}">selected</c:if>>全部</option>
                            <option value="1" <c:if test="${isRemark == '1'}">selected</c:if>>是</option>
                            <option value="0" <c:if test="${isRemark == '0'}">selected</c:if>>否</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label>商品名</label><input type="text" name="proName" id="proName" value="${search.proName}"/></td>
                    <td><label>提交用户</label><input type="text" name="commitUser" id="commitUser" value="${search.commitUser}"/></td>
                    <td><label>状态:</label>
                        <select name="status" id="status">
                            <option value="-1" <c:if test="${empty status}">selected</c:if>>全部</option>
                            <option value="0" <c:if test="${status == 0}">selected</c:if>>未读</option>
                            <option value="1" <c:if test="${status == 1}">selected</c:if>>已读</option>
                        </select>
                    </td>
                    <td>
                        <div style="margin-left: 60px;" class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">查 询</button>
                            </div>
                        </div>
                        <div style="margin-left: 20px;" class="buttonActive">
                            <div class="buttonContent" id="wish_card_list_reset">
                                <button type="button">重置</button>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>
<%--列表显示--%>
<div class="pageContent">
    <table class="list" width="100%" layoutH="165">
        <%--表头--%>
        <thead>
        <tr>
            <th align="center" style="width: 10%;">心愿单ID</th>
            <th align="center" style="width: 15%;">提交时间</th>
            <th align="center" style="width: 30">商品名</th>
            <th align="center" style="width: 10%;">产地</th>
            <th align="center" style="width: 20%;" >提交用户</th>
            <th align="center" style="width: 5%;" >备注</th>
            <th align="center" style="width: 5%;">状态</th>
            <th align="center" style="width: 5%;">操作</th>
        </tr>
        </thead>
        <tbody>
        <%--无数据--%>
        <c:if test="${empty pc.entityList}">
            <tr>
                <td style="text-align: center;" colspan="8"><font color="#dc143c">暂无数据</font></td>
            </tr>
        </c:if>
        <%--有数据--%>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="id" rel="${e.id}" <c:if test="${i.index%2==0}"> style="background: #ecf8ff"</c:if>>
                    <td align="center">${e.id}</td>
                    <td align="center"><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td align="center">${e.proName}</td>
                    <td align="center">${e.sourceCountryName}</td>
                    <td align="center">${e.commitUser}</td>
                    <td align="center"><c:if test="${empty e.remark}"><a
                            href="/pms/wishcard/readRemark?wishCardId=${e.id}&navTabId=10232"
                            target="dialog">添加备注</a></c:if><c:if test="${!empty e.remark}"><a
                            href="/pms/wishcard/readRemark?wishCardId=${e.id}&navTabId=10232"
                            target="dialog">查看备注</a></c:if></td>
                    <td align="center"><c:if test="${e.status ==0}">未读</c:if><c:if test="${e.status !=0}">已读</c:if></td>
                    <td align="center"><a href="/pms/wishcard/detail?wishCardId=${e.id}" target="navTab">查看</a></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%--底部--%>
    <div class="panelBar">
        <div class="pages">
            <span>显示</span>
            <select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="10" <c:if test="${pc.pageInfo.pagesize == 10}">selected="selected"</c:if>> 10</option>
                <option value="20" <c:if test="${pc.pageInfo.pagesize == 20}">selected="selected"</c:if>>20</option>
                <option value="50" <c:if test="${pc.pageInfo.pagesize == 50}">selected="selected"</c:if>>50</option>
                <option value="100" <c:if test="${pc.pageInfo.pagesize == 100}">selected="selected"</c:if>>100</option>
                <option value="200" <c:if test="${pc.pageInfo.pagesize == 200}">selected="selected"</c:if>>200</option>
            </select>
            <span>条，共${pc.pageInfo.totalCounts}条</span>
            <span style="padding: 0 0 0 10px;color:blue;">本页有 ${fn:length(pc.entityList)} 个心愿单</span>
        </div>
        <div id="pagemessage" class="pagination listTotalCount" targetType="navTab"
             totalCount="${pc.pageInfo.totalCounts}" numPerPage="${pc.pageInfo.pagesize}" pageNumShown="10"
             currentPage="${pc.pageInfo.page}"></div>
    </div>
</div>
<script type="text/javascript">
$(function(){
   $('#wish_card_list_reset').on('click',function(){
       $('#wish_card_list_form input,#wish_card_list_form select').val("");
       $('#wish_card_list_form select').val(-1);
   });
});
</script>
