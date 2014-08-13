<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/base/config/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/mms/categoryIntegral/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="first">一级类目：:</label>
                    </td>
                    <td>
                        <select id="first" name="firstCategoryId">
                            <option value="-1">全部</option>
                            <c:forEach var="e" items="${categoryList}">
                                <option value="${e.categoryId}">${e.categoryName}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <label for="second">二级类目:</label>
                    </td>
                    <td>
                        <select id="second" name="secondCategoryId" >
                            <option value="-1">全部</option>
                        </select>
                    </td>
                    <td>
                        <label for="third">三级类目:</label>
                    </td>
                    <td>
                        <select name="categoryId" id="third" >
                            <option value="-1">全部</option>
                        </select>
                    </td>

                    <td>
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit">筛选</button>
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
    <div class="panelBar">
        <ul class="toolBar">
            <!-- 必须先设置默认积分才能设置 单个积分-->
           <c:if test="${defaultNum!=0}">
              <c:if test='${sessionScope.bkstage_user.menus["129"]!=null}'>
                 <li class="line">line</li>
                 <li><a class="edit" href="/mms/categoryIntegral/add" target="dialog"><span>添加</span></a></li>
               </c:if>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["133"]!=null}'>
                <li class="line">line</li>
                <li><a class="edit" href="/mms/categoryIntegral/defaultRatio" target="dialog"><span>默认积分比例设置</span></a></li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="900" layoutH="130" id="J-table">
        <thead>
        <tr>
           <!-- <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>   -->
            <th width="5%"> 三级类目</th>
            <th width="12%">类型</th>
            <th width="12%">积分比例</th>
            <th width="12%">状态</th>
            <th width="10%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="id" rel="${e.id}">
                    <td>${e.categoryName}</td>
                    <td>
                        <c:if test='${e.ruleType==1}'>默认</c:if>
                        <c:if test='${e.ruleType==0}'>单个</c:if>
                    </td>
                    <td>${e.ratio}</td>
                    <td>
                        <c:if test="${e.state!=0}">
                            启用
                        </c:if>
                        <c:if test="${e.state==0}">
                            停用
                        </c:if>
                    </td>

                    <td>
                        <c:if test='${e.ruleType==1}'>
                            <c:if test='${sessionScope.bkstage_user.menus["133"]!=null}'>
                                <a title="编辑默认积分" target="dialog" height="320"  class="btnEdit" href="/mms/categoryIntegral/defaultRatio"><span>编辑默认积分</span></a>
                            </c:if>
                        </c:if>
                        <c:if test='${e.ruleType==0}'>
                            <c:if test='${sessionScope.bkstage_user.menus["131"]!=null}'>
                                <a title="编辑类目积分" target="dialog" height="320"  class="btnEdit" href="/mms/categoryIntegral/edit?id=${e.id}"><span>编辑类目积分</span></a>
                            </c:if>
                        </c:if>

                        <c:if test="${e.state==0}">
                            <a title="启用" target="ajaxTodo" height="320"  style="float:left; margin-right:10px;color: #0000ff;" class="btnShow" href="/mms/categoryIntegral/updateState?id=${e.id}&toState=1">启用<span></span></a>
                        </c:if>
                        <c:if test="${e.state!=0 && e.ruleType!=1}">
                            <a title="停用" target="ajaxTodo" height="320"  style="float:left; margin-right:10px;color: #0000ff;" class="btnShow" href="/mms/categoryIntegral/updateState?id=${e.id}&toState=0">停用<span></span></a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>
    $(document).ready(function(){
        $("#first").change(function(){
            var level = $("#first").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#second").html('<option value="-1">全部</option>'+data);
            });
          //  $("#third").html('<option value="-1">全部</option>');
        });

        $("#second").change(function(){
            var level = $("#second").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#third").html('<option value="-1">全部</option>'+data);
            });
        });
    });
</script>
