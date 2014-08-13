<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">系统角色添加</h2>

<div class="pageContent">
    <form action="/bkstage/role/add" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>系统角色添加</legend>
                <dl>
                    <dt>角色名称：</dt>
                    <dd><input name="roleName"  class="required"  type="text" /></dd>
                </dl>
                <dl>
                    <dt>角色对应部门：</dt>
                    <dd><select name="department">
                        <option value="美丽湾人员"  <c:if test='${obj.department =="美丽湾人员" }' > selected="selected" </c:if> >美丽湾人员</option>
                        <option value="管理层"  <c:if test='${obj.department =="管理层" }' > selected="selected" </c:if> >管理层</option>
                        <option value="编辑部"  <c:if test='${obj.department =="编辑部" }' > selected="selected" </c:if> >编辑部</option>
                        <option value="客服部"  <c:if test='${obj.department =="客服部" }' > selected="selected" </c:if> >客服部</option>
                        <option value="仓库物流部门"  <c:if test='${obj.department =="仓库物流部门" }' > selected="selected" </c:if> >仓库物流部门</option>
                        <option value="财务"  <c:if test='${obj.department =="财务" }' > selected="selected" </c:if> >财务</option>
                        <option value="采购"  <c:if test='${obj.department =="采购" }' > selected="selected" </c:if> >采购</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>角色描述：</dt>
                    <dd><input name="roleDesc" class="required" type="text" /></dd>
                </dl>
                <dl>
                    <dt>使用状态：</dt>
                    <dd><select name="state">
                        <option value="0">启用</option>
                        <option value="-1">停用</option>
                    </select></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
            </ul>
        </div>
    </form>
</div>