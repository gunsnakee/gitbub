<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
	<form method="post" action="/oms/favourable/discount" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56" >
			<input name="handle"  type="hidden" value="1"/>
			<input name="needToPay" type="hidden" value="${needToPay}" />
			<input name="realPayAmount" type="hidden" value="${order.orderSaleAmount}" />
			<input name="orderId"  type="hidden" value="${order.orderId}"/>
			<p>
				<label>应付的商品总金额：</label>
				<label>¥ <fmt:formatNumber type="number" value="${order.orderSaleAmount}" minFractionDigits="2" maxFractionDigits="2"/></label>
			</p>
            <p>
                <label>优惠方式：</label>
                <label>
                	<c:forEach var="f" items="${favList}">
						<input name="favName" type="radio" ftype="${f.type}" upper="${f.upperDiscount}" lower="${f.lowerDiscount}" value="${f.id}" ><span>${f.name}</span>
					</c:forEach>
                </label>
            </p>
            <p  style="width: 540px;height:30px;">
                <label>优惠值：</label>
                <label style="width: 400px;">
                	<input name="discountValue" style="width: 100px;" class="required" type="text"  value=""/><br/>
                	<span style="color: red">&nbsp;(<span id="discount_cyc"> ? </span>)</span>
                </label>
            </p>
            <p style="width: 540px;">
            	<label>优惠后的金额总计：</label>
            	<label style="width: 400px;">
            		<span id="end_payment" style="color: red"> ? </span>
            		<a target="navTab" href="/oms/favourable/list" style="color:#0000ff;">修改订单金额范围设置>></a>
            	</label>
            </p>
            <p>
            	<label>填写原因：</label>
            	<label>
            		<textarea name="content" cols="80" rows="2" class="textInput required" style="margin: 0px; width: 250px; height: 70px;" maxlength="140"></textarea>
            	</label>
            </p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">确认修改</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
	<script>
		$(function(){
			var setCheck=function($obj){
				var upper = $obj.attr("upper") ;
				var lower = $obj.attr("lower") ;
				var uValue = $('input[name=discountValue]')[0].value ;
				var rValue = $('input[name=realPayAmount]')[0].value ;
				var nValue = $('input[name=needToPay]')[0].value ;
				$obj[0].checked=true ;
				
				if($obj.attr("ftype") == "ORD_MINUS"){
					//$("#discount_cyc").text("减金额范围：¥ " + ((lower*rValue)/100).toFixed(2) + " - ¥ " +((upper*rValue)/100).toFixed(2)) ;
					$("#discount_cyc").text("减金额范围：¥ " + Math.ceil(((lower*rValue)/100)*100)/100 + " - ¥ " +((upper*rValue)/100).toFixed(2)) ;
				}else{
					$("#discount_cyc").text("折扣范围：" + lower+"% - "+upper+"%" + "，例如：0.5折填写05，七五折填写为75" ) ;
				}
				
				if(uValue != ""){
					if($obj.attr("ftype") == "ORD_MINUS"){
						var upperValue = parseFloat((rValue*upper)/100) ;
						var lowerValue = parseFloat((rValue*lower)/100) ;
						uValue =  parseFloat(uValue);
						nValue = parseFloat(nValue);
						if(lowerValue <= uValue && uValue <= upperValue ){
							var endValue = (((rValue*100)-(uValue*100))/100).toFixed(2) ;
							if(nValue <= uValue){
								alertMsg.info('订单中还需支付金额需要>0，商品价格才可修改成功！');
								$('input[name=discountValue]').val('').focus();
								return;
							}else{
								$("#end_payment").text("¥ "+ endValue) ;
							}
						}else{
							alertMsg.info('优惠值不在限制范围内，请重新输入！');
							$('input[name=discountValue]').val('').focus();
							return;
						}
					
					}else{
						lower =  parseFloat(lower);
						upper =  parseFloat(upper);
						uValue =  parseFloat(uValue);
						nValue = parseFloat(nValue);
						if( lower <= uValue && uValue <= upper ){
							var endValue =(((rValue*100)*uValue)/10000).toFixed(2) ;
							var unValue = rValue - endValue ;
							//alert("nValue:"+nValue+"-uValue:"+uValue);
							if(nValue <= unValue){
								alertMsg.info('订单中还需支付金额需要>0，商品价格才可修改成功！');
								$('input[name=discountValue]').val('').focus();
								return;
							}else{
								$("#end_payment").text("¥ "+ endValue) ;
							}
							
						}else{
							alertMsg.info('优惠值不在限制范围内，请重新输入！');
							$('input[name=discountValue]').val('').focus();
							return;
						}
					}
					
				}else{
					$("#end_payment").text("¥ "+ rValue) ;
				}
			}
			
			setCheck($('input[name=favName][value=1]'));
			
			$('input[name=favName]').on('change',function(e){
				var $this=$(this);
				setCheck($this);
			});
			$("input[name=discountValue]").on("blur",function(){
				setCheck($('input[name=favName]:checked'));
			});
		});
		
		//去掉回车事件
		$(function (){
		  $("*").each(function () {
		   $(this).keypress(function (e) {
		    var key = window.event ? e.keyCode : e.which;
		    if (key.toString() == "13") {
		     	return false;
		    }
		   });
		  });
		});
				
	</script>
</div>

