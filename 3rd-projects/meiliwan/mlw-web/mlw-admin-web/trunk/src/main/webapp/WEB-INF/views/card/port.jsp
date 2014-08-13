<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">导出礼品卡相关报表</h2>

<div class="pageContent">
    <table>
        <tr>
            <td>
                <form action="/giftcard/export/getrechargerexcel" method="post" class="pageForm required-validate" id="rechargeForm" >
                    <div class="pageFormContent" layoutH="97">
                        <fieldset>
                            <legend>礼品卡充值流水导出</legend>
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
                                <dd><button type="button" id="recharge_export">导出</button></dd>
                            </dl>
                        </fieldset>
                    </div>
                </form>
            </td>
            <td>
                <form action="/giftcard/export/getpayexcel" method="post" class="pageForm required-validate" id="payForm">
                    <div class="pageFormContent" layoutH="97">
                        <fieldset>
                            <legend>礼品卡使用记录导出</legend>
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
                    </div>
                </form>
            </td>
            <td>
                <form action="/giftcard/export/getmoneyexcel" method="post" class="pageForm required-validate" id="moneyForm" >
                    <div class="pageFormContent" layoutH="97">
                        <fieldset>
                            <legend>礼品卡余额总览表导出</legend>
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
</script>