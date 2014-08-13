<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <form action="/thematic/update" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
        <input type="hidden" name="handle" value="1">
        <input type="hidden" name="pageId" value="${bean.pageId}">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <dl>
                    <dt>专题名称：</dt>
                    <dd><input name="pageName"  class="required" value="${bean.pageName}"  type="text"></dd>
                </dl>
                <dl>
                    <dt>英文名称：</dt>
                    <dd><input name="enName"  class="required lettersonly" type="text" value="${bean.enName}"></dd>
                </dl>
                <dl>
                    <dt>结束时间：</dt>
                    <dd><input name="endTime" type="text" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${bean.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"></dd>
                </dl>
                <dl>
                    <dt>模板：</dt>
                    <dd>
                        <select name="templateId">
                            <c:forEach var="e" items="${templates}">
                                <option value="${e.templateId}" <c:if test="${e.templateId == bean.templateId}">selected="selected"</c:if>>${e.templateName}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt>SEO标题：</dt>
                    <dd><input name="seoTitle" type="text" style="width: 250px" value="${bean.title}"></dd>
                </dl>
                <dl>
                    <dt>SEO描述：</dt>
                    <dd><input name="seoDescp" type="text" style="width: 300px" value="${bean.descp}"></dd>
                </dl>
                <dl>
                    <dt>SEO关键字：</dt>
                    <dd><input name="seoKey" type="text" style="width: 300px" value="${bean.keyword}"></dd>
                    <dd><span class="info" style="color: red">(多个关键字间请用半角逗号","隔开)</span></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">取消</button></div></div></li>
            </ul>
        </div>
    </form>
</div>