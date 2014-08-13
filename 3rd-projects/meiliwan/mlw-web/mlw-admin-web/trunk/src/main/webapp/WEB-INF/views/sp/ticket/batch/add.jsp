<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/ticket/batch/add" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="handle"  type="hidden" value="1"/>
            <p>
                <label>优惠券名称:</label>
                <input type="text" name="ticketName" value="" class="required" maxlength="50">
            </p>
            <p>
                <label>面  值:</label>
                <input type="text" name="ticketPrice" value="" class="required number" min="0" max="1000">
            </p>
            <p>
                <label>数量（参考值）:</label>
                <input type="text" name="initNum" value="" class="required digits">
            </p>
            <p>
                <label>有效期开始时间:</label>
                <input type="text" name="startTime" value="" class="required date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
            </p>
            <p>
                <label>有效期结束时间:</label>
                <input type="text" name="endTime" value="" class="required date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
            </p>
			<p>
				<label>优惠券类型:</label>
                <input type="radio" name="ticketType" value="1" id="pinlei" checked="true">品类券  <input type="radio" name="ticketType" id="tongyong" value="0">通用券
			</p>
            <p>
                <label>使用范围:</label>
                <label id="useRange">--</label>
            </p>

            <p>
                <label>使用范围说明:</label>
                <label><span id="rangeInfo1"><input type="text" id="descp" name="descp" value="" class="required" maxlength="30"><span class="info">30个字符以内</span></span><span hidden="true" id="rangeInfo2">全站通用</span></label>
            </p>

		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit" id="createTicketBt">创 建</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取 消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#pinlei").live("click",function(){
            $("#useRange").html("--");
            $("#rangeInfo1").show();
            $("#rangeInfo2").hide();
            $("#descp").addClass("required");
            $("#createTicketBt").html("下一步")
        });

        $("#tongyong").live("click",function(){
            $("#useRange").html("全站通用");
            $("#rangeInfo1").hide();
            $("#descp").removeClass("required");
            $("#rangeInfo2").show();
            $("#createTicketBt").html("创建优惠券并上线")
        });
    });

</script>