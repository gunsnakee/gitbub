<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<div class="pageContent" style="border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid">
<div class="panelBar">
		<ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["62"]!=null}'>
                <li><a class="add" href="/pms/category/create-all-category" target="ajaxTodo" mask="true"><span>生成前台全部类目页</span></a></li>
                <c:if test="${level == 1}"><li><a class="add" href="/pms/category/add?pid=${pid}&level=${level}" target="dialog" mask="true"><span>添加一级类目</span></a></li></c:if>
                <c:if test="${level == 2}"><li><a class="add" href="/pms/category/add?pid=${pid}&level=${level}" target="dialog" mask="true"><span>添加二级类目</span></a></li></c:if>
                <c:if test="${level == 3}"><li><a class="add" href="/pms/category/add?pid=${pid}&level=${level}" target="dialog" mask="true"><span>添加三级类目</span></a></li></c:if>
            </c:if>
        </ul>
	</div>
	<table class="table" width="99%" layoutH="120" rel="jbsxBox">
		<thead>
			<tr>
				<th width="100" >类目编号</th>
				<th width="120">类目名称</th>
                <th width="100">排序类型</th>
				<th width="100">父级ID</th>
				<th width="100">用户ID</th>
				<th width="100">状态</th>
                <th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
            <c:if test="${empty pcList}">
                <tr><td style="text-align: center"><span style="color: #ec3425">暂无类目数据</span></td></tr>
            </c:if>
            <c:if test="${!empty pcList}">
                <c:forEach var="list" items="${pcList}">
                <tr target="sid_obj" rel="${list.categoryId}">
                    <td>${list.categoryId}</td>
                    <td>${list.categoryName}</td>
                    <td>${list.sequence}</td>
                    <td>${list.parentId}</td>
                    <td>${list.adminId}</td>
                    <c:if test="${list.state == 0}">
                        <td>禁用</td>
                    </c:if>
                    <c:if test="${list.state == 1}">
                        <td>使用</td>
                    </c:if>
                    <td>
                        <c:if test='${sessionScope.bkstage_user.menus["63"]!=null}'>
                            <a title="修改"  target="dialog" href="/pms/category/update?categoryId=${list.categoryId}&level=${level}" class="btnEdit">修改</a>
                         </c:if>
                        <c:if test='${sessionScope.bkstage_user.menus["65"]!=null}'>
                            <a title="删除"  target="ajaxTodo" href="/pms/category/delete?categoryId=${list.categoryId}&level=${level}" class="btnDel">删除</a>
                         </c:if>
                    </td>
                </tr>
                </c:forEach>
        </c:if>
		</tbody>
	</table>
</div>