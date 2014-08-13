
    <div class="snav2">
        <div class="tit btop">
            <h3 class="titon">
                交易中心</h3>
            <div class="ui-arrow"> <em>◆</em> <span>◆</span> </div>
        </div>
        <div class="con">
            <ul>
                <li><a href="http://www.meiliwan.com/uCenter/order/list">我的订单</a></li>
                <li><a href="http://www.meiliwan.com/uCenter/userCollect/list">我的收藏</a></li>
                <li><a href="http://account.meiliwan.com/uCenter/list">美丽钱包</a></li>
                <li><a href="http://www.meiliwan.com/uCenter/userIntegral/list">我的积分</a></li>
            </ul>
        </div>
        <div class="tit">
            <h3 class="titon sever-ico">
                售后服务</h3>
            <div class="ui-arrow"> <em>◆</em> <span>◆</span> </div>
        </div>
        <div class="con">
            <ul>
                <li><a href="http://www.meiliwan.com/uCenter/user/consult">商品咨询</a></li>
                <li><a href="http://www.meiliwan.com/uCenter/proComment/list">商品评价</a></li>
                <li><a href="http://www.meiliwan.com/uCenter/retorder/applyList">退货/换货</a></li>
                <li><a href="http://www.meiliwan.com/uCenter/user/complaints">在线投诉</a></li>
            </ul>
        </div>
        <div class="tit">
            <h3 class="titon user-ico">
                帐号中心</h3>
            <div class="ui-arrow "> <em>◆</em> <span>◆</span> </div>
        </div>
        <div class="con">
            <ul>
                <li><a href="http://www.meiliwan.com/ucenter/profile">帐号信息</a></li>
                <li><a href="javascript:void(0)"id="J_account_safe">帐号安全</a></li>
                <li><a href="http://www.meiliwan.com/uCenter/address/list">收货地址</a></li>
                <li><a href="http://www.meiliwan.com/uCenter/userStationMsg/list">站内信<b class="fc-red gap-pl10" id="J_Left_Letter"></b></a></li>
            </ul>
        </div>
    </div>
    <script src="http://www.meiliwan.com/js/thirdPartLogin.js"></script>
    <script>
        $(function(){
             $.ajax({
                        url: 'https://passport.meiliwan.com/ucenter/userstationmsg/getunreadtotal',
                        dataType: 'jsonp',
                        jsonP: 'callback',
                        success: function(data){
                            $("#J_Left_Letter").html(data);
                            $("#J_station").html(data);


                            //处理帐号安全的第三方登录
                            $("#J_account_safe").click(function(){

                                MLW.thirdPartLogin.handleLogin("https://passport.meiliwan.com/user/get-dialog?notRemindOpen=false",
                                        function(){
                                            MLW.thirdPartLogin.handleDailog("https://passport.meiliwan.com/user/third-part-register?successUrl=http://www.meiliwan.com/ucenter/safe/index",true);
                                        },
                                        function(){
                                            MLW.thirdPartLogin.jumpUrl("http://www.meiliwan.com/ucenter/safe/index");
                                        });
                            });
                        }
                    });
        });
    </script>
