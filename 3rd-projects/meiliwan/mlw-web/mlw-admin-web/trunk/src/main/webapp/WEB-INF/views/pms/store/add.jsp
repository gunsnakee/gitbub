<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">店铺添加</h2>

<div class="pageContent">
    <form id="addStoreDivForm" onkeydown="if(event.keyCode==13){return false;}" action="/bkstage/store/add" method="post" class="pageForm required-validate"
          onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" value="1" name="handle">

        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>店铺添加</legend>
                <dl>
                    <dt>店铺名称：</dt>
                    <dd><input name="storeName" value=""  class="required" type="text"/></dd>
                </dl>
                <dl>
                    <dt>英文名(不能修改)：</dt>
                    <dd><input name="enName" value=""  class="required" type="text"/></dd>
                </dl>
                <dl>
                    <dt>SEO Title：</dt>
                    <dd><input name="seoTitle" value=""  type="text"/></dd>
                </dl>
                <dl>
                    <dt>SEO Title List：</dt>
                    <dd><input name="seoTitleList" value=""  type="text"/></dd>
                </dl>
                <dl>
                    <dt>SEO keyword：</dt>
                    <dd><input name="seoKeyword" value=""  type="text"/></dd>
                </dl>

                <dl>
                    <dt>SEO description：</dt>
                    <dd><input name="seoDescp" value="" type="text"/></dd>
                </dl>
            </fieldset>
            <div>
            ${proCategoryTree}
            </div>
        </div>



        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit" onclick="return handleValue();" >保存</button>
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
        // var str = "";
        $("#addStoreDivForm .checked ").each(function (key, val) {
            // str = str +
            $(val).find("input").attr("checked","true");
        });
        // $("#userSelectIds").val(str);
        return true;
    }
</script>