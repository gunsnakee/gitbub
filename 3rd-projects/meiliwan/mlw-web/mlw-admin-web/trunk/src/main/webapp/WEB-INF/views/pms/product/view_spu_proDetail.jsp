<%@ page language="java" pageEncoding="UTF-8" %>
<fieldset>
    <legend>商品锚点和详情</legend>
    <dl>
        <dt>商品详细描述锚点<span style="color: red">(最多25个锚点，每个锚点最多4个字)</span>：</dt>
        <dd>
            <input type="hidden" id="pro-up-descp-menu-init" value="${spu.detail.descpMenu}" />
            <script>
                var editorDescpMenu;
                $(function() {
                    editorDescpMenu = KindEditor.create('#pro-up-descp-menu',
                            {uploadJson:'http://imagecode.meiliwan.com/file/uploadImg?projectName=prodetail&upfileElemName=Filedata',
                                filePostName:'Filedata',
                                items:[
                                    'source'
                                ],designMode:false});
                    editorDescpMenu.readonly(true);
                });
            </script>
            <textarea id="pro-up-descp-menu" name="descpMenu" style="width:900px;height:100px;visibility:hidden;">${spu.detail.descpMenu}</textarea>


        </dd>
    </dl>
    <dl>
        <dt>商品描述：</dt>
        <dd>
            <script>
                var editorDescp;
                $(function() {
                    editorDescp = KindEditor.create('#pro-up-descp',
                            {uploadJson:'http://imagecode.meiliwan.com/file/uploadImg?projectName=prodetail&upfileElemName=Filedata&sizeRule=320,480,640,800&ratio=T',
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
                    editorDescp.readonly(true);
                });
            </script>
            <textarea id="pro-up-descp" name="descp" style="width:900px;height:400px;visibility:hidden;">${spu.detail.descp}</textarea>
        </dd>

    </dl>
    <c:if test='${sessionScope.bkstage_user.menus["f6f03aa4654220af64642d5aa03965ed"]!=null}'>
        <span name="proDetail" class="view"><input  type="button"  name="modify" value="修改"  ref="proDetail"/></span>
    </c:if>
                <span name="proDetail" class="input">
                    <input type="button" name="saveSubmit"  value="保存" submitType="proDetail"/>
                    <input type="button"  name="cancel" value="取消" ref="proDetail"/>
                </span>
</fieldset>