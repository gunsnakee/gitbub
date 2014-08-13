<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/card/card/del" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="idStr" id="idStr" type="hidden" value="${cardId}"/>
			<input name="handle"  type="hidden" value="1"/>
			<p>
				<label>作废礼品卡原因：</label>
                <textarea name="descp" class="required textInput" cols="50" rows="5"></textarea>
			</p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button <c:if test="${type == 'batch'}">id="deleteCard"</c:if> type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        $("#deleteCard").click(function(){
            var selectItems =  $("#zuofeiTable input[name=ids]:checked");
            if(selectItems.size() ==0){
                alert("请选择礼品卡信息")
                return false;
            }
            var str="";
            var first = true;
            selectItems.each(function(i,o){
                if(first){
                    str=str+$(o).val();
                    first=false;
                }else{
                    str=str+","+$(o).val();
                }
            });
            $("#idStr").val();
            $("#idStr").val(str);
        });
    });
</script>
