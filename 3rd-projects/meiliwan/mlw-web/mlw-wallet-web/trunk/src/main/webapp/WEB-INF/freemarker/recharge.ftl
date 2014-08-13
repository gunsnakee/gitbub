<#escape x as(x)!>
<#import "/lib/account_lib.ftl" as accountLib/>
<!DOCTYPE html>
<html>
<head>
    <#assign mlw=JspTaglibs["/WEB-INF/tld/mlw-inc-form.tld"]>
    <@accountLib.recharge/>
    <!--cms 标签定义-->
    <@mlw.getAndSetIncAttribute incNameStr="topbar_990,inc_footer_990,map_990"/>
</head>
<body class="w990">
<!--topbar-->
${topbar_990}

<!--header-->
<div class="header">
    <div class="inn">
        <div class="logo">
            <a href="http://www.meiliwan.com" target="_self" title="美丽湾"><img src="http://www.meiliwan.com/images/logo.jpg" alt="美丽湾"></a>
        </div>
        <div class="steps2"></div>
    </div>
</div>

<!--P1-->
<div class="area recharge">
    <div class="box">
        <div class="hd">
            <h2>填写充值金额</h2>
        </div>
        <div class="bd">
            <div class="box1">
              <form action="/ucenter/rechargeway/add" method="POST" id="recharge_form" >
                <@mlw.noRepeatSubmit formName="recharge_form"/>
                  <p>账户昵称：<b class="fc-blue">${nickName}</b></p>
                  <p>账户余额：<b class="fn-rmb">&yen;${mlwCoin ?string('0.00')}</b></p>
                  <p>充值金额：<input type="text" id="h_money" name="rechargeMoney">&nbsp;元<span id="h_notice" style="padding-left: 15px;">(<em class="fc-red">请输入大于等于10，小于等于50000的整数金额</em>)</span></p>
                  <p><a href="#" id="next" class="anC6" style="margin-left:60px"><span>下一步</span></a></p>
              </form>
            </div>
            <div class="box2" style="border-bottom: none;">
                <p>温馨提示：</p>
                <p>1、充值成功后，余额可能存在稍微的延迟现象，如有问题，请咨询客服；</p>
                <p>2、充值金额的输入值必须是<span class="fc-red">10~50000的正整数</span>；</p>
                <p>3、充值完成后，您可以进入账号余额页面进行查看余额充值状态。</p>
            </div>
        </div>
    </div>
</div>

<!--footer-->
${map_990}
${inc_footer_990}
<@accountLib.recharge_js/>
<script>
    $(function () {
         //文本框只能输入数字，并屏蔽输入法和粘贴
         $.fn.numeral = function() {
             $(this).css("ime-mode", "disabled");
             this.bind("keypress",function(e) {
                 var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE
                 if(!$.browser.msie&&(e.keyCode==0x8))  //火狐下不能使用退格键
                 {
                     return ;
                 }
                 return code >= 48 && code<= 57;
             });
             this.bind("blur", function() {
                 if (this.value.lastIndexOf(".") == (this.value.length - 1)) {
                     this.value = this.value.substr(0, this.value.length - 1);
                 } else if (isNaN(this.value)) {
                     this.value = "";
                 }
                 if (parseInt(this.value) <10) {
                     $("#h_notice").html("(<em class='fc-red'>请输入大于等于10，小于等于50000的整数金额</em>)");
                     this.value = "";
                 } else if(parseInt(this.value)>50000){
                     $("#h_notice").html("(<em class='fc-red'>请输入大于等于10，小于等于50000的整数金额</em>)");
                     this.value = "";
                 } else if(parseInt(this.value)<=50000 && parseInt(this.value) >=10){
                     $("#h_notice").html("<em class='s_ico'></em>");
                 }
             });
             this.bind("paste", function() {
                 return false;
             });
             this.bind("dragenter", function() {
                 return false;
             });
             this.bind("keyup", function() {
                 if (/(^0+)/.test(this.value)) {
                     this.value = this.value.replace(/^0*/, '');
                 }
             });
         };
         //调用文本框的id
         $("#h_money").numeral();

         $("#next").click(function(){
             var money=parseInt($("#h_money").val());
             if( money >=10 && money <= 50000){
                 $.ajax({
                     type: "POST",
                     dataType: 'json',
                    // jsonp: 'callback',
                     url: "http://account.meiliwan.com/ucenter/account/get/isToLimitTop",
                     data: {
                         money:money
                     },
                     timeout: 20000,
                     success: function(msg){
                         if(msg.result == "true"){
                             $.mlwbox.remind("无法充值，充值后总金额超过上限");
                         }else{
                             $("#recharge_form").submit();
                         }
                     },
                     error:function(jqXHR, textStatus, errorThrown){
                     }
                 });
             }else{
                 $.mlwbox.remind("请输入充值金额");
             }
			 return false;
         });
        });
</script>
</body>
</html>
</#escape>