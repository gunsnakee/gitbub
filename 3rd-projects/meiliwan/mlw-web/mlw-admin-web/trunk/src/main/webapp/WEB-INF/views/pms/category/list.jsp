<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<style type="text/css">
    ul.rightTools {float:right; display:block;}
    ul.rightTools li{float:left; display:block; margin-left:5px}
</style>

<div class="pageContent" style="padding:5px">
    <div class="tabs">
        <div class="tabsContent">
            <div>
                ${stringHtml}
                <div id="add-category" class="unitBox">
                    <%@include file="template_list.jsp"%>
                </div>

            </div>
        </div>
        <div class="tabsFooter">
            <div class="tabsFooterContent"></div>
        </div>
    </div>

</div>


	

