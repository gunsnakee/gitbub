<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageFormContent" layoutH="97">
    <div class="panelBar" style="border: none;">
        <ul class="toolBar">
            <c:if test='${sessionScope.bkstage_user.menus["50"]!=null}'>
                <li><a class="add menuListBtn" href="" target="navTab" title="添加子节点"
                       warn="添加子节点" target="navTab"><span>添加子节点</span></a></li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["69"]!=null}'>
                <li class="line">line</li>
                <li><a class="delete menuListBtn" href=""  target="ajaxTodo" title="确定要删除当前菜单?"><span>删除</span></a></li>
            </c:if>
            <c:if test='${sessionScope.bkstage_user.menus["82"]!=null}'>
                <li class="line">line</li>
                <li><a class="edit menuListBtn" href="" target="navTab" title="编辑菜单"><span>编辑</span></a>
                </li>
            </c:if>
        </ul>
    </div>
    <div id="menuListTreeDiv"
         style=" float:left; margin:10px;  width:90%; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
        ${allMenu}
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var add = "/bkstage/menu/add?parentId=";
        var edit = "/bkstage/menu/edit?menuId=";
        var del = "/bkstage/menu/del?menuId=";
        $(".menuListBtn").click(function () {
            var url = "";
            var aTag = $("#menuListTreeDiv .selected a");
            var id = "0";
            if (aTag.size() > 0) {
                id = aTag.attr("tvalue");
            }
            if ($(this).hasClass("add")) {
                url = add + id;
            }
            else if ($(this).hasClass("delete")) {
                url = del + id;
            }

            else if ($(this).hasClass("edit")) {
                url = edit + id;
            }
            $(this).attr("href", url);
        });
    });
</script>



