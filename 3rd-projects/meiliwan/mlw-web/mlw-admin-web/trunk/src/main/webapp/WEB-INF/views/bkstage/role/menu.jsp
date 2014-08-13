<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">管理角色菜单-- 角色：<b style="color: #0000ff">${obj.name}</b> 描述:<b
        style="color: #0000ff">${obj.description}</b></h2>

<div class="pageContent">
    <form id="roleMenuForm" method="post" action="/bkstage/role/menu" class="pageForm "
          onsubmit="return validateCallback(this)">
        <input type="hidden" value="1" name="handle">
        <input type="hidden" value="${obj.roleId}" name="roleId" >
        <input type="hidden" value="" id="userSelectIds" name="userSelectIds">

        <div class="pageFormContent" layoutH="95">
            ${allMenu}
        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit" onclick="return handleValue();">保存</button>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button class="close" type="button">关闭</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </form>
</div>

<script type="text/javascript">
    function handleValue() {
        var str = "";
        $("#roleMenuForm .checked , #roleMenuForm .indeterminate").each(function (key, val) {
            str = str + $(val).find("input").val() + ",";
        });
        $("#userSelectIds").val(str);
        return true;
    }
</script>