<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<div class="pageContent" style="border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid">
    <div class="panelBar">
		<ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["83"]!=null}'>
                <c:if test="${level > 3}">
                    <li><a class="add" href="/pms/property/add?categoryId=${categoryId}" target="dialog" mask="true" width="600" height="400"><span>添加类目属性</span></a><li>
                </c:if>
            </c:if>
        </ul>
	</div>
	<table class="table" width="99%" layoutH="120" rel="jbsxBox">
		<thead>
			<tr>
                <th width="80">属性编号</th>
                <th width="80" style="color: red">可用于规格</th>
				<th width="100">属性名称</th>
                <th width="180">子项值</th>
				<th width="80">属性类型</th>
                <th width="80">必填项</th>
                <th width="80">筛选属性</th>
                <th width="80">状态</th>
                <th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
            <c:if test="${empty pc}">
                <tr><td style="text-align: center"><span style="color: #ec3425">暂无属性数据</span></td></tr>
            </c:if>
            <c:if test="${!empty pc}">
                <c:forEach var="entity" items="${pc}">
                    <tr target="sid_obj" rel="${entity.proPropId}">
                        <td>${entity.proPropId}</td>
                        <td style="color: red"><c:if test="${entity.isSku == 0}">否</c:if><c:if test="${entity.isSku == 1}">是</c:if></td>
                        <td>${entity.name}</td>
                        <td><c:if test="${!empty entity.proProValue}"><c:forEach var="entityList" items="${entity.proProValue}">${entityList.name},</c:forEach></c:if><c:if test="${empty entity.proProValue}">无信息</c:if></td>
                        <td><c:if test="${entity.propertyType == 1}">单选</c:if><c:if test="${entity.propertyType == 2}">多选</c:if><c:if test="${entity.propertyType == 3}">文本输入</c:if></td>
                        <td><c:if test="${entity.isRequired == 0}">否</c:if><c:if test="${entity.isRequired == 1}">是</c:if></td>
                        <td><c:if test="${entity.isFilter == 0}">否</c:if><c:if test="${entity.isFilter == 1}">是</c:if></td>
                        <td><c:if test="${entity.state == 1}">使用</c:if><c:if test="${entity.state == 0}">禁用</c:if></td>
                        <td>
                            <c:if test='${sessionScope.bkstage_user.menus["86"]!=null}'>
                                <a title="修改" width="600" height="400"  target="dialog" href="/pms/property/update?proPropId=${entity.proPropId}" class="btnInfo">修改</a>
                             </c:if>
                            <c:if test='${sessionScope.bkstage_user.menus["85"]!=null}'>
                                <c:if test="${entity.state == 1}">
                                    <a title="禁用"  target="ajaxTodo" href="/pms/property/reset?proPropId=${entity.proPropId}&state=0" class="btnDel">禁用</a>
                                </c:if>
                                <c:if test="${entity.state == 0}">
                                    <a title="启用"  target="ajaxTodo" href="/pms/property/reset?proPropId=${entity.proPropId}&state=1" class="btnSelect">启用</a>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
        </c:if>
		</tbody>
	</table>
</div>