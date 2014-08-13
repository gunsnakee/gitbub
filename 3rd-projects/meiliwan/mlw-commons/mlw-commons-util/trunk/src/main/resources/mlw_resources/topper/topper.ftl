<div class="topbar" >
    <div class="inn">
        <div class="fl" userName=${uid}>
            <ul>
                <li class="bar-item"><a href="javascript:void(0)" id="J_add_mlw" class="ico top-collect">收藏美丽湾</a></li>

                <!--未登录状态-->
                <li class="bar-item gap-ml10" id="J_welcomeMLW"  >欢迎来到<a href="http://www.meiliwan.com">美丽湾</a>！</li>
                <li class="bar-item" id="J_loginIn"><a href="javascript:void(0)" class="t-login">登录</a><a href="javascript:void(0)" class="t-register">注册</a></li>


                <!--登录状态-->
                <li class="bar-item" id="J_NickName"  style="display:none" >欢迎您！<a href="http://www.meiliwan.com/ucenter/index" class="top-name" ></a></li>
                <li class="bar-item" id="J_loginOut"  style="display:none" ><a href="javascript:void(0)" class="t-out" >退出</a></li>

            </ul>
        </div>
        <div class="fr">
            <ul>
                <li class="bar-item"><a href="http://www.meiliwan.com/uCenter/order/list" class="ico top-order">我的订单</a></li>
                <li class="bar-item bar-user"><a href="javascript:void(0)" class="ico top-home" id="J_person_center"><span>个人中心</span><i class="ico"></i></a>
                    <ul class="list">
                        <!-- <li><a href="#" class="ico my-coupon">我的优惠券</a></li>-->
                        <li><i></i><a href="http://www.meiliwan.com/uCenter/userCollect/list" class="ico my-collect">我的收藏</a></li>
                        <li><i></i><a href="http://www.meiliwan.com/uCenter/userStationMsg/list" class="ico letter">站内信<b class="fc-red gap-pl10" id="J_station"></b></a></li>
                    </ul>
                </li>
                <li class="bar-item bar-service"><a href="#" class="ico top-sever"><span>客户服务</span><i class="ico"></i></a>
                    <ul class="list">
                        <li><i></i><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1513483808&site=qq&menu=yes" class="ico qqserver">QQ在线客服</a></li>
                        <li><i></i><a href="http://www.meiliwan.com/uCenter/retorder/list" class="ico payment">在线退换货</a></li>
                        <li><i></i><a href="http://www.meiliwan.com/uCenter/user/complaints" class="ico complaint">在线投诉</a></li>
                    </ul>
                </li>
                <li class="bar-item"><a href="http://www.meiliwan.com/enterprisePurchase/register" class="ico top-market">企业采购</a></li>
                <li class="bar-item ico bar-hotline">客服热线：<b class="fc-orange">400-600-4040</b></li>
            </ul>
        </div>
        <script src="http://www.meiliwan.com/js/thirdPartLogin.js"></script>
        <script>
            $(function(){
                $(function(){
                                passport.bindTopAll();
                                $('.t-login').click(function(e){
                                    e.preventDefault();
                                    window.location.href="https://passport.meiliwan.com/user/login?targetUrl="+window.location.href;
                                });
                                $('.t-out').click(function(e){
                                    e.preventDefault();
                                    user.loginOut(function(){
                                        window.location.href='http://www.meiliwan.com/';
                                    });
                                });
                                $('.t-register').click(function(e){
                                    e.preventDefault();
                                    window.location.href="https://passport.meiliwan.com/user/reg";
                                });
                                $("div.inn li.bar-item").Jdropdown();
                                $("#J_add_mlw").click(function(){
                                    MLW.SetFavorite();
                                });

                                $("#J_person_center").click(function(){
                                    $.ajax({
                                        url:"https://passport.meiliwan.com/user/get-dialog?notRemindOpen=true",
                                        dataType:'jsonp',
                                        data:'',
                                        jsonp:'callback',
                                        success:function(result) {
                                            if(result == 1){
                                                MLW.thirdPartLogin.handleDailog("https://passport.meiliwan.com/user/third-part-register?isHas=1&successUrl=https://passport.meiliwan.com/ucenter/index",true);
                                            }else
                                            {
                                                window.location.href="http://www.meiliwan.com/ucenter/index";
                                            }
                                        }
                                    });
                                });
                });
            });
        </script>
    </div>
</div>
