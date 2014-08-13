<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/location/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56" >
			<input name="locationId"  type="hidden" value="${locationId}"/>
            <input name="barCode"  type="hidden" value="${location.barCode}"/>
            <input name="handle"  type="hidden" value="1"/>
            <p>
                <label>原存储信息：</label>
                <label><c:if test="${empty location.locationName}">暂无</c:if><c:if test="${!empty location.locationName}">${location.locationName}</c:if></label>
            </p>
            <p>
                <label>新存储信息：</label>
                <input name="locationName" class="required" type="text"  value=""/></span>
            </p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
