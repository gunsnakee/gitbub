<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<link rel="stylesheet" href="js/kindeditor/themes/default/default.css" />
<div class="pageContent">
	<form method="post" action="/base/infocontent/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" name="handle" value="1">
        <input type="hidden" name="infoId" value="${content.infoId}">
        <div class="pageFormContent nowrap" layoutH="60">
			<div class="unit">
				<label>资讯类别：</label>
                <select name="itemId">
                    <c:forEach var="its" items="${items}">
                        <option value="${its.infoItemId}" <c:if test="${its.infoItemId == content.infoItemId}">selected="selected" </c:if>>${its.infoItemName}</option>
                    </c:forEach>
                </select>
			</div>
            <div class="unit">
                <label>SEO 标题：</label>
                <input type="text" name="seoTitle" class="required" style="width: 400px" maxlength="400" value="${content.seoTitle}">
            </div>
            <div class="unit">
                <label>SEO 关键字：</label>
                <input type="text" name="seoKeywords" class="required" style="width: 400px" maxlength="400" value="${content.seoKeywords}">
            </div>
            <div class="unit">
                <label>SEO 描述：</label>
                <input type="text" name="seoDescp" class="required" style="width: 400px" maxlength="400" value="${content.seoDescp}">
            </div>
            <div class="unit">
                <label>资讯标题：</label>
                <input type="text" name="infoName" class="required" style="width: 400px" value="${content.infoName}">
            </div>
            <dl>
                <dt>资讯内容：</dt>
                <dd>
                    <script>
                        $(function() {
                            var editor = KindEditor.create('#update-infoContent',
                                    {uploadJson:'http://imagecode.meiliwan.com/file/uploadImg?projectName=bkstage-web&upfileElemName=Filedata',
                                        filePostName:'Filedata',
                                        items:[
                                            'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                                            'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                                            'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                                            'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                                            'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                                            'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'multiimage',
                                            'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                                            'anchor', 'link', 'unlink', '|', 'about'
                                        ]});
                        });
                    </script>
                    <textarea id="update-infoContent" name="infoContent" style="width:900px;height:400px;visibility:hidden;">${content.infoContent}</textarea>
                </dd>
            </dl>
            <div class="unit">
                <label>排序类型：</label>
                <select class="combox" name="sequence">
                    <option value="-1" <c:if test="${content.sequence == -1}">selected="selected"</c:if>>-1</option>
                    <option value="-2" <c:if test="${content.sequence == -2}">selected="selected"</c:if>>-2</option>
                    <option value="-3" <c:if test="${content.sequence == -3}">selected="selected"</c:if>>-3</option>
                    <option value="-4" <c:if test="${content.sequence == -4}">selected="selected"</c:if>>-4</option>
                    <option value="-5" <c:if test="${content.sequence == -5}">selected="selected"</c:if>>-5</option>
                    <option value="-6" <c:if test="${content.sequence == -6}">selected="selected"</c:if>>-6</option>
                    <option value="-7" <c:if test="${content.sequence == -7}">selected="selected"</c:if>>-7</option>
                    <option value="-8" <c:if test="${content.sequence == -8}">selected="selected"</c:if>>-8</option>
                    <option value="-9" <c:if test="${content.sequence == -9}">selected="selected"</c:if>>-9</option>
                    <option value="-10" <c:if test="${content.sequence == -10}">selected="selected"</c:if>>-10</option>
                    <option value="0"  <c:if test="${content.sequence == 0}">selected="selected"</c:if>>0</option>
                    <option value="1" <c:if test="${content.sequence == 1}">selected="selected"</c:if>>1</option>
                    <option value="2" <c:if test="${content.sequence == 2}">selected="selected"</c:if>>2</option>
                    <option value="3" <c:if test="${content.sequence == 3}">selected="selected"</c:if>>3</option>
                    <option value="4" <c:if test="${content.sequence == 4}">selected="selected"</c:if>>4</option>
                    <option value="5" <c:if test="${content.sequence == 5}">selected="selected"</c:if>>5</option>
                    <option value="6" <c:if test="${content.sequence == 6}">selected="selected"</c:if>>6</option>
                    <option value="7" <c:if test="${content.sequence == 7}">selected="selected"</c:if>>7</option>
                    <option value="8" <c:if test="${content.sequence == 8}">selected="selected"</c:if>>8</option>
                    <option value="9" <c:if test="${content.sequence == 9}">selected="selected"</c:if>>9</option>
                    <option value="10" <c:if test="${content.sequence == 10}">selected="selected"</c:if>>10</option>
                </select>
            </div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button onclick="KindEditor.sync('#update-infoContent')" type="submit">提交</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
</div>

