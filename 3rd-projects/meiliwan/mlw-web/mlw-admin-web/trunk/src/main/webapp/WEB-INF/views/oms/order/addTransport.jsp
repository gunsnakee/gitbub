<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/oms/order/ship" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>快递类型：</label>
				<input type="hidden" name="ids" value="${orderId}">
				<input type="hidden" name="handle" value="1">
                <input type="hidden" name="os" value="${os}">
                <!-- 
				<select name="company">
				<c:forEach var="c" items="${companies}">
					<option value="${c.code}">${c.desc}</option>
				</c:forEach>
				</select>
				 -->
				 <c:forEach var="c" items="${companies}">
					<input name="company" type="radio" value="${c.code}"><span>${c.desc}</span>
				</c:forEach>
			</p>
			<script>
				$(function(){
				var setCheck=function($obj){
					
					var $parent=$obj.parent(),$span=$parent.find('span');
						$span.css({color:'#000'});
						$obj.next().css({color:'#f00'});
						$('input[name=transportInfo]').val('').focus();
						$obj[0].checked=true;
				}
				var _type=sessionStorage.getItem('transportType');
					_type=_type===null||_type==='undefiend'?'EMS':_type;
					setCheck($('input[name=company][value='+_type+']'));
					
					$('input[name=company]').on('change',function(e){
						var $this=$(this);
						setCheck($this);
						sessionStorage.setItem('transportType',$this.val());
					});
				});
			</script>
			<p>
				<label>货运单号：</label>
				<input name="transportInfo" type="text" maxlength="20" class="required">
			</p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
