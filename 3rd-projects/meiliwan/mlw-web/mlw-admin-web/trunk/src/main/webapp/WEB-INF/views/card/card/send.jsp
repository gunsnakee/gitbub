<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/card/card/send" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="cardId"  type="hidden" value="${card.cardId}"/>
			<input name="handle"  type="hidden" value="1"/>
			<p>
				<label><input name="type" value="0" <c:if test="${flag == 1}">checked="true"</c:if> type="radio">使用邮箱发送</label>
                <input type="text" name="email" id="email" value="${card.buyerEmail}" <c:if test="${!empty card.buyerEmail}">readonly="true"</c:if> class="email">
			</p>
            <p>
                <label><input name="type" value="1" <c:if test="${flag == 2}">checked="true"</c:if> type="radio">使用手机发送</label>
                <input type="text" name="phone" id="phone" value="${card.buyerPhone}" <c:if test="${!empty card.buyerPhone}">readonly="true"</c:if> class="phone">
            </p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button id="sendCard" type="submit">发  送</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取  消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        $("#sendCard").click(function(){
            var item = $("input[name=type]:checked").val();
            if(item == 0){
                var email = $("#email").val();
                if(email == ''||email == null){
                    alert("请填写邮箱")
                    return false;
                }
            }else{
                var phone = $("#phone").val();
                if(phone == ''||phone == null){
                    alert("请填写手机")
                    return false;
                }
            }
        });
    });
</script>