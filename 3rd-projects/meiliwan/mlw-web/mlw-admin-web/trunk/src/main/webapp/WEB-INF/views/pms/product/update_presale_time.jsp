<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/pms/product/updateskuPresale" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="proId"  type="hidden" value="${bean.proId}"/>
		    <input name="handle"  type="hidden" value="1"/>
            <input name="cacheState"  type="hidden" value="1"/>
            <fieldset>
                <legend>预售相关时间</legend>
                <p>
                    <label>预售结束时间：</label>
                    <input name="presaleEndTime" id="presaleEndTime" value="<fmt:formatDate value="${bean.presaleSendTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </p>
                <p>
                    <label>预计发货时间：</label>
                    <input name="presaleSendTime" id="presaleSendTime" value="<fmt:formatDate value="${bean.presaleSendTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </p>
            </fieldset>
         </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="return upateSaleTime()">保存</button></div></div></li>
                <li>
                    <div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
                </li>
            </ul>
        </div>
	</form>
</div>

<script type="text/javascript">
    function upateSaleTime(){
        var presaleEndTime = $("#presaleEndTime").val();
        var presaleSendTime = $("#presaleSendTime").val();
        if(presaleEndTime != ''){
            if(presaleSendTime == ''){
                alertMsg.error("请填写预售预计发货时间")
                return false;
            }
        }
    }
</script>
