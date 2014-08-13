<#import "/lib/account_lib.ftl" as accountLib/>
<!DOCTYPE html>
<html>
<head>
    <#assign mlw=JspTaglibs["/WEB-INF/tld/mlw-inc-form.tld"]>
    <@accountLib.wallet_user_list/>
    <!--cms 标签定义-->
    <@mlw.getAndSetIncAttribute incNameStr="topbar_990,inc_footer_990,uCenter_menu,header,nav_list_990,map_990,shop_list"/>
</head>
<body class="uCenter w990">
<!--topbar-->
${topbar_990}
<!--header-->
${header}
<!--导航-->
<div class="hideshoplist">
    ${shop_list}
</div>
${nav_list_990}
<!--breadcrumb-->
<div class="area">
    <div class="breadcrumb">
        <strong><a href="http://www.meiliwan.com/ucenter/index">我的美丽湾</a></strong>
        <span>&nbsp;&gt;&nbsp;美丽钱包</span>
    </div>
</div>
<!--uCenter-->
<div class="area">
    <div class="s1 fl uCenter-lnavs" >
       ${uCenter_menu}
    </div>
    <div class="s4">
        <div class="grid-account wallet">
            <div class="tit tit2"><b>美丽钱包</b></div>

            <#if walletVo.status == 1>
                <div class="ac-info">
                    <p class="list"><span>账户状态：</span><span class="msg-box verified">可用</span>
                    </p>
                    <p class="list"><span>账户余额：</span><b class="fn-rmb fc-red">¥${walletVo.mlwMoney?string('0.00')}</b><a href="javascript:void(0)" id="J_recharge"  class="anC8"><span>充值</span></a></p>
                </div>
            <#else>
                <!--账户被锁-->
                <div class="ac-info ac-info-locked">
                    <p class="list"><span>账户状态：</span><span class="msg-box locked">封锁</span></p>
                    <p class="list"><span>账户余额：</span><b class="fn-rmb fc-gray">¥${walletVo.mlwMoney?string('0.00')}</b></p>
                    <div class="warning">您可能进行一些不当的操作，账户已封锁，解除封锁后才可充值。请联系客服处理!</div>
                </div>
            </#if>

            <div class="grid-rechargeRecord">
                <div class="t gap-mb10"><b>交易记录</b></div>
                <div class="g-filter gap-mb10">
                    <dl class="clearfix">
                        <dt>日期：</dt>
                        <dd>
                            <a href="/ucenter/list?opt=${opt}&time=1&page=${page}" class="anC7   <#if time == 1>current</#if> "><span>最近一个月</span></a>
                            <a href="/ucenter/list?opt=${opt}&time=2&page=${page}" class="anC7 <#if time == 2>current</#if> " ><span>最近三个月</span></a>
                            <a href="/ucenter/list?opt=${opt}&time=3&page=${page}" class="anC7 <#if time == 3>current</#if> "><span>三个月之前</span></a>
                        </dd>
                    </dl>
                    <p class="border gap-mt10 gap-mb10"></p>
                    <dl class="clearfix">
                        <dt>类型：</dt>
                        <dd>
                            <a href="/ucenter/list?opt=1&time=${time}&page=${page}" class="anC7 <#if opt == 1>current</#if>"><span>全部</span></a>
                            <a href="/ucenter/list?opt=2&time=${time}&page=${page}" class="anC7 <#if opt == 2>current</#if>"><span>购物</span></a>
                            <a href="/ucenter/list?opt=3&time=${time}&page=${page}" class="anC7 <#if opt == 3>current</#if>"><span>充值</span></a>
                            <a href="/ucenter/list?opt=4&time=${time}&page=${page}" class="anC7 <#if opt == 4>current</#if>"><span>退款</span></a></dd>
                    </dl>
                </div>
                <table class="table-record">
                    <thead>
                    <tr>
                        <th class="r1">日期</th>
                        <th class="r2">类型</th>
                        <th class="r3">存入(元)</th>
                        <th class="r4">支出(元)</th>
                        <th class="r5">详情</th>
                    </tr>
                    </thead>
                    <tbody>
                      <#if pc.entityList ? exists>
                            <#list pc.entityList as list>
                                <tr>
                                    <td class="r1"><span class="fc-gray"> ${list.optTime ?string('yyyy-MM-dd HH:mm:ss')} </span></td>
                                    <td class="r2"><span>
                                        <#if  list.optType == 'REFUND_FROM_GATEWAY' || list.optType == 'REFUND_FROM_FREEZE'>
                                            <span >退款</span>
                                        </#if >

                                        <#if  list.optType == 'SUB_MONEY' || list.optType == 'FREEZE_MONEY'>
                                            <span >购物</span>
                                        </#if >

                                        <#if  list.optType == 'ADD_MONEY'>
                                            <span >充值</span>
                                        </#if >
                                    </span></td>
                                    <td class="r3">
                                        <#if  list.optType == 'ADD_MONEY'>
                                            <span class="fc-green">+ ${list.money ?string('0.00')}</span>
                                        </#if >
                                        <#if  list.optType == 'REFUND_FROM_GATEWAY' || list.optType == 'REFUND_FROM_FREEZE'>
                                            <span class="fc-green">+ ${list.money ?string('0.00')}</span>
                                        </#if >
                                    </td>
                                    <td class="r4">
                                        <#if  list.optType == 'SUB_MONEY' || list.optType == 'FREEZE_MONEY'>
                                            <span class="fc-red">- ${list.money ?string('0.00')}</span>
                                        </#if >
                                    </td>
                                    <td class="r5">
                                        <#if  list.optType == 'SUB_MONEY' || list.optType == 'FREEZE_MONEY'>
                                            <p>使用美丽湾钱包支付订单<a class="fc-blue" href="http://www.meiliwan.com/ucenter/order/detail?orderId=${list.orderId}">${list.orderId}</a></p>
                                            <p>支付流水号:${list.innerNum}</p>
                                        </#if >
                                        <#if  list.optType == 'REFUND_FROM_GATEWAY'>
                                             <p>退换货编号 <a class="fc-blue" href="http://www.meiliwan.com/ucenter/retorder/get?retId=${list.orderId}">${list.orderId}</a></p>
                                             <p>支付流水号 ${list.innerNum}</p>
                                        </#if >
                                        <#if  list.optType == 'REFUND_FROM_FREEZE'>
                                            <p>取消订单 <a class="fc-blue" href="http://www.meiliwan.com/ucenter/order/detail?orderId=${list.orderId}">${list.orderId}</a>返回余额</p>
                                            <p>支付流水号 ${list.innerNum}</p>
                                        </#if >
                                        <#if  list.optType == 'ADD_MONEY' && list.childType == 'ADD_MONEY_HB'>
                                            <p>注册马上有钱-送${list.money ?string('0.00')}元红包活动</p>
                                            <p>支付流水号：<#if list.outNum ??> ${list.outNum}<#else>${list.innerNum}</#if></p>
                                        </#if>
                                        <#if  list.optType == 'ADD_MONEY' && list.childType == 'ADD_MONEY_AWARD'>
                                            <p>抽奖获赠${list.money ?string('0.00')}元</p>
                                            <p>支付流水号：<#if list.outNum ??> ${list.outNum}<#else>${list.innerNum}</#if></p>
                                        </#if>
                                        <#if  list.optType == 'ADD_MONEY' && list.childType != 'ADD_MONEY_HB' && list.childType != 'ADD_MONEY_AWARD'>
                                            <p>使用 ${list.source}充值，充值单号${list.innerNum},支付流水号<#if list.outNum ??> ${list.outNum}<#else>${list.innerNum}</#if></p>
                                        </#if >
                                    </td>
                                </tr>
                            </#list>
                        <#else>
                               <tr style="padding: 10px;"><td colspan="5">您还没有充值记录！</td></tr>
                        </#if>
                    </tbody>
                </table>
                <div class="page">
                    <div class="inner">
                        <#if pc.pageInfo ? exists>
                            <#if 1!=pc.pageInfo.page><a href="/ucenter/list?opt=${opt}&time=${time}&page=1" >首页</a></#if>
                            <#if 0!=pc.pageInfo.isPrev><a href="/ucenter/list?opt=${opt}&time=${time}&page=${pc.pageInfo.isPrev}" class="prev first">上一页</a></#if>
                            <#list pc.pageInfo.listPages as row>
                                <#if row==pc.pageInfo.page><a class="on" href="/ucenter/list?opt=${opt}&time=${time}&page=${row}">${row}</a></#if>
                                <#if row!=pc.pageInfo.page><a href="/ucenter/list?opt=${opt}&time=${time}&page=${row}">${row}</a></#if>
                                <#if row_index gt 3><#break ></#if>
                            </#list>
                            <#if pc.pageInfo.allPage gt 5 && pc.pageInfo.allPage!=pc.pageInfo.page><#if pc.pageInfo.allPage-1 != pc.pageInfo.page><span class="slh">...</span></#if><a href="/ucenter/list?opt=${opt}&time=${time}&page=${pc.pageInfo.allPage}"">${pc.pageInfo.allPage}</a></#if>
                            <#if  0!=pc.pageInfo.isNext><a class="next" href="/ucenter/list?opt=${opt}&time=${time}&page=${pc.pageInfo.isNext}">下一页</a></#if>
                            <#if  pc.pageInfo.page<=pc.pageInfo.allPage><a class="last" href="/ucenter/list?opt=${opt}&time=${time}&page=${pc.pageInfo.allPage}">尾页</a></#if><i>到第</i><input type="text" id="jumpPage" autocomplete="off" name="page" class="jump-to" value="${pc.pageInfo.page}"><i>页</i>
                            <input type="button" value="确定" url="${urlEncoder}" class="jump" id="jump">
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--footer-->
${map_990}
${inc_footer_990}
<@accountLib.wallet_user_list_js/>
<script>
$(document).ready(function(){
    //跳到多少页
    $("#jump").click(function(){
        var p = $("#jumpPage").val();
        location.href="/ucenter/list?page="+p+"";
    });
    $("#J_recharge").click(function(){
        MLW.thirdPartLogin.handleLogin("https://passport.meiliwan.com/user/get-dialog?notRemindOpen=false",
                function(){
                    MLW.thirdPartLogin.handleDailog("https://passport.meiliwan.com/user/third-part-register?successUrl=http://account.meiliwan.com/ucenter/list",true)
                },
                function(){
                    MLW.thirdPartLogin.jumpUrl("http://account.meiliwan.com/ucenter/recharge")
                   })
        return false;
    });
});

</script>
</body>
</html>

