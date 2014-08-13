<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<div class="pageContent">
    <div class="pageFormContent" layoutH="56">
        <br>
        <div class="panelBar" style="height: 35px">
            <c:if test='${sessionScope.bkstage_user.menus["44aa1c8fa707be44ba4e1b12472c4e51"]!=null}'>
                <input id="importStock" type="file" name="file" uploaderOption="{
        swf:'uploadify/scripts/uploadify.swf',
        uploader:'/card/card/import',
        formData:{},
        buttonText:'导入购买信息',
        fileSizeLimit:'2048KB',
        fileTypeDesc:'*.xls;',
        fileTypeExts:'*.xls;',
        auto:true,
        multi:true,
        onUploadSuccess:importCardSuccess
         }"/>
            </c:if>

        </div><span style="color: red">(现阶段只支持.xls格式低版本)</span>
        <br><br><br>
        <p>
            <label><a href="/card/card/index?handle=2"><span style="color: #0000ff">下载导入购买信息模板</span></a></label>
        </p>

    </div>
</div>
<script type="text/javascript">
    function importCardSuccess (file, data, response) {
        var json = eval('(' + data + ')');
        if (json.statusCode == DWZ.statusCode.ok){
            if (json.navTabId){
                //把指定navTab页面标记为需要“重新载入”。注意navTabId不能是当前navTab页面的
                navTab.openTab(json.navTabId,json.forwardUrl,{title:"查看导入购买记录"});
            } else {
                //重新载入当前navTab页面
                navTabPageBreak();
            }
            if ("closeCurrent" == json.callbackType) {
                navTab.openTab(json.navTabId,json.forwardUrl,{title:"查看导入购买记录"});
            }

        }else{
            alert(json.message);
        }
    }
</script>