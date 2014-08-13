<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">编辑修改供应商</h2>

<div class="pageContent">
    <form action="/pms/supplier/update?handle=1" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="92">
        <input name="id"  type="hidden" value="${entity.id}">
        <fieldset>
        <legend>编辑修改供应商</legend>
            <dl>
                <dt>供应商名称：</dt>
                <dd><input name="name"  class="required"  type="text" value="${entity.supplierName}"/></dd>
            </dl>
            <dl>
                <dt>联系人：</dt>
                <dd><input name="linkman"  type="text" value="${entity.supplierLinkman}"  /></dd>
            </dl>
            <dl>
                <dt>联系电话：</dt>
                <dd><input name="phone" class="phone" type="text" value="${entity.supplierPhone}" /></dd>
            </dl>
            <dl>
                <dt>其他电话：</dt>
                <dd><input name="otherPhone" type="text" class="phone" value="${entity.supplierOtherPhone}" /></dd>
            </dl>
            <dl>
                <dt>经营方式：</dt>
                <dd>
                    <select name="operateType" class="required">
                        <option value="1" <c:if test="${operateType==1}">selected="selected"</c:if>>购销</option>
                        <option value="2" <c:if test="${operateType==2}">selected="selected"</c:if>>代销</option>
                        <option value="3" <c:if test="${operateType==3}">selected="selected"</c:if>>联营</option>
                    </select>
                </dd>
            </dl>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存修改</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>