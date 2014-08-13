<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/base/area/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="areaCode" value="${areaCode}" />
    <input type="hidden" name="parentCode" value="${parentCode}" />
    <input type="hidden" name="areaName" value="${areaName}" />
    <input type="hidden" name="areaGrade" value="${areaGrade}" />
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/base/area/list" method="post"
          id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label for="areaCode">地区编码:</label>
                    </td>
                    <td><input name="areaCode" id="areaCode" value="${areaCode}"/></td>
                    <td>
                        <label for="areaName">地区名称:</label>
                    </td>
                    <td><input name="areaName" id="areaName" value="${areaName}"/></td>

                </tr>
                <tr>
                    <td>
                        <label for="parentCode">上级编码:</label>
                    </td>
                    <td><input name="parentCode" id="parentCode" value="${parentCode}"/></td>
                    <td>
                        <label for="areaCode">地区级别:</label>
                    </td>
                    <td>
                        <select id="areaGrade" name="areaGrade">
                            <option value="-1" <c:if test="${areaGrade==-1}">selected</c:if>>全部</option>
                            <option value="0" <c:if test="${areaGrade==0}">selected</c:if> >国家</option>
                            <option value="1" <c:if test="${areaGrade==1}">selected</c:if> >省/直辖市</option>
                            <option value="2" <c:if test="${areaGrade==2}">selected</c:if> >市</option>
                            <option value="3" <c:if test="${areaGrade==3}">selected</c:if> >区/县</option>
                            <option value="4" <c:if test="${areaGrade==4}">selected</c:if> >街道/乡镇</option>
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
            <c:if test='${sessionScope.bkstage_user.menus["68"]!=null}'>
                <li class="line">line</li>
                <li><a class="edit" href="/base/area/add" target="dialog"><span>添加</span></a></li>
            </c:if>
        </ul>
    </div>

    <table class="table" width="900" layoutH="155" id="J-table">
        <thead>
        <tr>
           <!-- <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>   -->
            <th width="5%"> 地区ID</th>
            <th width="12%">地区编码</th>
            <th width="12%">上级编码</th>
            <th width="12%">地区名称</th>
            <th width="12%">地区级别</th>
            <th width="5%">是否删除</th>
            <th width="5%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" items="${pc.entityList}">
                <tr  target="areaId" rel="${e.areaId}">
                    <td>${e.areaId}</td>
                    <td>${e.areaCode}</td>
                    <td>${e.parentCode}</td>
                    <td>${e.areaName}</td>
                    <td>
                        <c:if test="${e.areaGrade==0}">国家</c:if>
                        <c:if test="${e.areaGrade==1}">省</c:if>
                        <c:if test="${e.areaGrade==2}">市</c:if>
                        <c:if test="${e.areaGrade==3}">区/县</c:if>
                        <c:if test="${e.areaGrade==4}">街道/乡镇</c:if>
                    </td>
                    <td><c:if test="${e.isDel==0}">否</c:if>
                        <c:if test="${e.isDel==-1}">是</c:if>
                    </td>
                    <td>
                        <a title="编辑该地区" target="dialog"  class="btnEdit" href="/base/area/edit?areaId=${e.areaId}"><span>编辑该地区</span></a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>

</script>
