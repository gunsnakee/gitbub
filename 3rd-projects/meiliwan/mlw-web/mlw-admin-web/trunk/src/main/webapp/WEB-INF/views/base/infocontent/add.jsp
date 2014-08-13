<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<link rel="stylesheet" href="js/kindeditor/themes/default/default.css" />
<div class="pageContent">
	<form method="post" action="/base/infocontent/add" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
        <input type="hidden" name="handle" value="1">
        <div class="pageFormContent nowrap" layoutH="58">
			<div class="unit">
				<label>资讯类别：</label>
                <select name="itemId">
                    <c:forEach var="its" items="${items}">
                        <option value="${its.infoItemId}">${its.infoItemName}</option>
                    </c:forEach>
                </select>
			</div>
            <div class="unit">
                <label>SEO 标题：</label>
                <input type="text" name="seoTitle" class="required" style="width: 1000px" maxlength="400">
            </div>
            <div class="unit">
                <label>SEO 关键字：</label>
                <input type="text" name="seoKeywords" class="required" style="width: 1000px" maxlength="400">
            </div>
            <div class="unit">
                <label>SEO 描述：</label>
                <input type="text" name="seoDescp" class="required" style="width: 1000px" maxlength="400">
            </div>
            <div class="unit">
                <label>资讯标题：</label>
                <input type="text" name="infoName" class="required" style="width: 1000px" maxlength="100">
            </div>
            <dl>
                <dt>资讯内容：</dt>
                <dd>
                    <script>
                        $(function() {
                            var editor = KindEditor.create('#add-infoContent',
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
                    <textarea id="add-infoContent" name="infoContent" style="width:900px;height:400px;visibility:hidden;"></textarea>
                </dd>
            </dl>
            <div class="unit">
                <label>排序类型：</label>
                <select class="combox" name="sequence">
                    <option value="-1">-1</option>
                    <option value="-2">-2</option>
                    <option value="-3">-3</option>
                    <option value="-4">-4</option>
                    <option value="-5">-5</option>
                    <option value="-6">-6</option>
                    <option value="-7">-7</option>
                    <option value="-8">-8</option>
                    <option value="-9">-9</option>
                    <option value="-10">-10</option>
                    <option value="0"  selected="selected">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button onclick="KindEditor.sync('#add-infoContent')" type="submit">提交</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
</div>

