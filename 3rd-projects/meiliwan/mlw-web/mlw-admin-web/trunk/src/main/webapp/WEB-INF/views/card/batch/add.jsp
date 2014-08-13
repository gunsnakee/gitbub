<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/card/batch/add" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<input name="handle"  type="hidden" value="1"/>
			<p>
				<label>卡类型:</label>
                <label><input type="radio" name="cardType" value="1" checked="true">电子卡  <input type="radio" name="cardType" value="0">实体卡</label>

			</p>
            <p>
                <label>卡名称:</label>
                <input type="text" name="cardName" value="" class="required" maxlength="50">
            </p>
            <p>
                <label>面  额:</label>
                <input type="text" name="cardPrice" value="" class="required number" min="0" max="1000">
            </p>
            <p>
                <label>最大使用数量:</label>
                <input type="text" name="actNum" value="" class="digits" min="1" max="9"><span class="info" style="color: red">(不填则不限制)</span>
            </p>
            <p>
                <label>有效期:</label>
                <input type="text" name="vaildNum" value="36" class="required digits" min="1">月 <span style="color: red">(默认36个月)</span>
            </p>
            <p>
                <label>数  量:</label>
                <input type="text" name="initNum" value="" class="required digits" min="1" max="10000">张 (<=10000)
            </p>
            <p>
                <label>截止日提醒:</label>
                <input type="text" name="preWarnDay" value="" class="required digits" min="1">天
            </p>
            <p>
                <label>截止日提醒邮箱:</label>
                <textarea id="adminEmail" name="adminEmail" class="required textInput" cols="40" rows="2"></textarea>
            </p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit" id="createCard">创 建</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取 消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
<script type="text/javascript">
   $(document).ready(function(){
       $('#adminEmail').keyup(function(){
           this.value=this.value.replace(/[；;，]/ig,',');
       });
   });
</script>