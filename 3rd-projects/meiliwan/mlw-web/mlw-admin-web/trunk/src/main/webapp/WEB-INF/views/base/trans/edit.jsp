<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">系统角色添加</h2>

<div class="pageContent">
    <form action="/bkstage/role/edit" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>系统角色编辑</legend>
                <dl>
                    <dt>角色名称：</dt>
                    <dd><input name="roleName"  class="required"  type="text"  value="${obj.name}"/></dd>
                </dl>
                <dl>
                    <dt>角色描述：</dt>
                    <dd><input name="roleDescription" class="required" type="text" value="${obj.description}" /></dd>
                </dl>
                <dl>
                    <dt>使用状态：</dt>
                    <dd><select name="state">
                        <option value="1"  <c:if test="${obj.state ==1 }" > selected="selected" </c:if> >启用</option>
                        <option value="-1" <c:if test="${obj.state ==-1 }" > selected="selected" </c:if> >停用</option>
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