<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">导出平台相关报表</h2>

<div class="pageContent">
    <table>
        <tr>
            <td>
                <div class="pageFormContent" layoutH="97">
                    <form action="/account/export/getrechargerexcel" method="post" class="pageForm required-validate" id="rechargeForm" >
                        <fieldset style="height: 150px;">
                            <legend>充值流水导出</legend>
                            <dl style="width: 400px;">
                                <dt>起始日期:</dt>
                                <dd style="width: 260px;">
                                    <input type="text" id="J_recharge_start" name="recharge_start" class="date textInput readonly" readonly="true">
                                    <a class="inputDateButton" href="javascript:;">选择</a>
                                    <span class="info">yyyy-MM-dd</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt>结束日期:</dt>
                                <dd>
                                    <input type="text" id="J_recharge_end" name="recharge_end" class="date textInput readonly" readonly="true">
                                    <a class="inputDateButton" href="javascript:;">选择</a>
                                    <span class="info">yyyy-MM-dd</span>
                                </dd>
                            </dl>
                            <dl>
                               <dt>充值类型选择：</dt>
                               <dd>
                                   <select id="rechargeType" name="rechargeType">
                                       <option value="" selected>默认</option>
                                       <option value="ADD_MONEY_HB">红包</option>
                                   </select>
                               </dd>
                            </dl>
                            <dl>
                                <dd><button type="button" id="recharge_export">导出</button></dd>
                            </dl>
                        </fieldset>
                        </form>
                        
                        <!-- add by lzl 20140504 -->
				       	<form action="/oms/report/getretorderexcel" method="post" class="pageForm required-validate" id="retordForm" >
			               <fieldset>
			                   <legend>退换货记录导出</legend>
			                   <dl style="width: 400px;">
			                       <dt>起始日期:</dt>
			                       <dd style="width: 260px;">
			                           <input type="text" id="J_retord_start" name="retord_start" class="date textInput readonly" readonly="true">
			                           <a class="inputDateButton" href="javascript:;">选择</a>
			                           <span class="info">yyyy-MM-dd</span>
			                       </dd>
			                   </dl>
			                   <dl>
			                       <dt>结束日期:</dt>
			                       <dd>
			                           <input type="text" id="J_retord_end" name="retord_end" class="date textInput readonly" readonly="true">
			                           <a class="inputDateButton" href="javascript:;">选择</a>
			                           <span class="info">yyyy-MM-dd</span>
			                       </dd>
			                   </dl>
			                   
			                   <dl>
			                       <dd><button type="button" id="retord_export">导出</button></dd>
			                   </dl>
			               </fieldset>
				       </form>
                </div>
            </td>
            <td>
                <div class="pageFormContent" layoutH="97">
                    <form action="/account/export/getpayexcel" method="post" class="pageForm required-validate" id="payForm">
                        <fieldset style="height: 150px;">
                            <legend>钱包使用记录导出</legend>
                            <dl style="width: 400px;">
                                <dt>起始日期:</dt>
                                <dd style="width: 260px;">
                                    <input type="text" id="J_pay_start" name="pay_start" class="date textInput readonly" readonly="true">
                                    <a class="inputDateButton" href="javascript:;">选择</a>
                                    <span class="info">yyyy-MM-dd</span>
                                </dd>
                            </dl>
                            <dl>
                                <dt>结束日期:</dt>
                                <dd>
                                    <input type="text" id="J_pay_end" name="pay_end" class="date textInput readonly" readonly="true">
                                    <a class="inputDateButton" href="javascript:;">选择</a>
                                    <span class="info">yyyy-MM-dd</span>
                                </dd>
                            </dl>
                            <dl>
                                <dd><button type="button" id="pay_export">导出</button></dd>
                            </dl>
                        </fieldset>
                	</form>
                
	                <!-- add by lzl 20140610 spticket -->
			       	<form action="/sp/ticket/getspticketorderexcel" method="post" class="pageForm required-validate" id="spticketordForm" >
		               <fieldset>
		                   <legend>优惠券报表导出</legend>
		                   <dl style="width: 400px;">
		                       <dt>起始日期:</dt>
		                       <dd style="width: 260px;">
		                           <input type="text" id="J_spticket_start" name="spticket_start" class="date textInput readonly" readonly="true">
		                           <a class="inputDateButton" href="javascript:;">选择</a>
		                           <span class="info">yyyy-MM-dd</span>
		                       </dd>
		                   </dl>
		                   <dl>
		                       <dt>结束日期:</dt>
		                       <dd>
		                           <input type="text" id="J_spticket_end" name="spticket_end" class="date textInput readonly" readonly="true">
		                           <a class="inputDateButton" href="javascript:;">选择</a>
		                           <span class="info">yyyy-MM-dd</span>
		                       </dd>
		                   </dl>
		                   
		                   <dl>
		                       <dd><button type="button" id="spticket_export">导出</button></dd>
		                   </dl>
		               </fieldset>
			       </form>
		       </div>
            </td>
            <td>
                <form action="/account/export/getmoneyexcel" method="post" class="pageForm required-validate" id="moneyForm" >
                    <div class="pageFormContent" layoutH="97">
                        <fieldset style="height: 150px;">
                            <legend>余额总览表导出</legend>
                            <dl style="width: 400px;">
                                <dd><button id="money_export" type="button">导出</button></dd>
                            </dl>
                        </fieldset>
                    </div>
                </form>
            </td>
        </tr>
    </table>
</div>
<script>
    $('#recharge_export').click(function(){
        var start =$('#J_recharge_start').val();
        var end =$('#J_recharge_end').val();
        if(start == "" || end == ""){
           alert("起始时间和结束时间都不能为空!");
        }else{
          $("#rechargeForm").submit();
        }
    });

    $('#pay_export').click(function(){
        var start =$('#J_pay_start').val();
        var end =$('#J_pay_end').val();
        if(start == "" || end == ""){
            alert("起始时间和结束时间都不能为空!");
        }else{
            $("#payForm").submit();
        }
    });

    $('#money_export').click(function(){
        var start =$('#J_money_start').val();
        var end =$('#J_money_end').val();
        if(start == "" || end == ""){
            alert("起始时间和结束时间都不能为空!");
        }else{
            $("#moneyForm").submit();
        }
    });
    
    $('#retord_export').click(function(){
        var start =$('#J_retord_start').val();
        var end =$('#J_retord_end').val();
        if(start == "" || end == ""){
           alert("起始时间和结束时间都不能为空!");
        }else{
          $("#retordForm").submit();
        }
    });
    
    $('#spticket_export').click(function(){
        var start =$('#J_spticket_start').val();
        var end =$('#J_spticket_end').val();
        if(start == "" || end == ""){
           alert("起始时间和结束时间都不能为空!");
        }else{
          $("#spticketordForm").submit();
        }
    });
</script>