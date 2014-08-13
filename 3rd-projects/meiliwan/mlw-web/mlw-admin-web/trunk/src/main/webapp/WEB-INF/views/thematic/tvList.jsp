<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/tv/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>

<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="/tv/editCode" method="post" >
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td style="width: 400px;">
                        下载手机二维码文案:
                        <textarea name="name" class="required textInput" maxlength="100" cols="80" rows="2">${conf.sysConfigName}</textarea>
                    </td>
                    <td >
                        二维码:<input id="AppDimensionalCode" name="img" type="hidden" value="${conf.sysConfigValue}">
                        <div id="AppDimensionalCodeImg">
                            <img  src="${conf.sysConfigValue}" width="80" height="80">
                        </div>
                    </td>
                    <td>
                        上传APP下载二维码:
                        <div id="upload-app-code"></div>
                    </td>
                    <td>
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit" >保存</button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>


<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/tv/list" method="post" id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        专题ID:
                        <input name="id" class="digits" value="${bean.id}">
                    </td>
                    <td>
                        专题名称:
                        <input name="pageName"  value="${bean.pageName}" >
                    </td>
                    <td>
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit">筛选</button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li><a title="添加专题" href="/tv/add" width="600" height="400"  target="dialog" class="add"><span>添加专题</span></a></li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="235" id="J-table">
        <thead>
        <tr>
            <th align="center" width="5%">序号</th>
            <th align="center" width="10%">专题ID</th>
            <th align="center" width="10%">专题名称</th>
            <th align="center" width="8%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty pc.entityList}">
            <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
        </c:if>
        <c:if test="${!empty pc.entityList}">
            <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                <tr target="adminId" rel="">
                    <td>${i.index + 1}</td>
                    <td>${e.id}</td>
                    <td>${e.pageName}</td>
                    <td>
                        <c:if test="${e.state>=0}">
                        <a title="编辑" target="dialog" href="/tv/update?id=${e.id}" width="600" height="400" class="btnEdit">编辑</a>
                        <a title="页面设置" target="navTab" href="/tv/pageSet?id=${e.id}" class="btnInfo">页面设置</a>
                        </c:if>

                        <!--<a title="删除" target="ajaxTodo" href="/tv/del?id=${e.id}" class="btnDel" >删除</a>-->
                        
                        
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>
$(document).ready(function(){
    //上传锚点头图
        $('#upload-app-code').uploadify({
            swf: '/swf/uploadify.swf',
            uploader: 'http://imagecode.meiliwan.com/file/uploadImg?upfileElemName=Filedata',
            height: 30,
            width: 120,
            buttonText: '上传图片',
            buttonCursor: 'pointer',
            auto: true,
            fileSizeLimit: '5000KB',
            fileTypeDesc: 'Image Files',
            fileTypeExts: '*.gif; *.jpg; *.png',
            formData: {},
            method: 'post',
            multi: true,
            queueSizeLimit: 1,
            uploadLimit: 50,
            removeCompleted: false,
            removeTimeout: 3,
            requeueErrors: false,
            progressData: 'speed',
            preventCaching: false,
            suceessTimeout: 300,
            onUploadSuccess: function (file, data, response) {
                data = $.parseJSON(data);//把json字符串转换成json对象
                $("#upload-app-code-queue").empty();
                $("#AppDimensionalCodeImg").html('<img src="'+data.url+'" width="100" height="100">');
                $("#AppDimensionalCode").val(data.persistUri);
            }
        });
});
</script>