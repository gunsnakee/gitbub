<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/update-seo" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${detail.proId}"/>
			<input name="handle"  type="hidden" value="1"/>
            <fieldset style="width: 600">
                <legend>商品SEO相关信息</legend>
                <dl class="nowrap">
                    <dt>seo标题：</dt>
                    <dd><input name="seoTitle" type="text" size="80" value="${detail.seoTitle}">
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>seo关键词：</dt>
                    <dd>
                        <textarea name="seoKeyword" cols="65" rows="2">${detail.seoKeyword}</textarea>
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>seo描述：</dt>
                    <dd><textarea name="seoDescp" cols="65" rows="2">${detail.seoDescp}</textarea></dd>
                </dl>
            </fieldset>
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
