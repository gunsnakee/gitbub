<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>监控后台</title>
    
    <link href="../css/index.css" rel="stylesheet" type="text/css" >
    <link href="../css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet" type="text/css" />
    <link href="../css/pagination.css" rel="stylesheet" type="text/css" />
    <link href="../css/ie.css" rel="stylesheet" type="text/css" />
    <link href="../css/load-style.css" rel="stylesheet" type="text/css" />
    <link href="../css/colors-classic.css" rel="stylesheet" type="text/css" />
    <link href="../css/thickbox.css" rel="stylesheet" type="text/css" />
    <link href="../css/jquery-ui-timepicker-addon.css" rel="stylesheet" type="text/css" />

    <script type="text/javascript" src="/js/jquery-1.8.3.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/jquery-ui-1.10.3.custom.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/jquery.form.js"></script>
    <script type="text/javascript" src="../js/demo-core.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/demo-view.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/jquery-ui-timepicker-addon.js" ></script> 
    <script type="text/javascript" src="../js/jquery-ui-timepicker-zh-CN.js"  charset="utf-8"></script>
    <script type="text/javascript" src="../js/mlw.core.js" ></script>
    <script type="text/javascript" src="../js/mlw.dialog.js" ></script>
    <script type="text/javascript" src="../js/mlw.dialogbox.js" ></script>
</head>
<body class="wp-admin no-js  index-php">
    <div id="wpwrap">
        <div id="wpcontent">
            <div id="wphead">
                <img id="header-logo" src="../images/blank.gif" alt="" width="32" height="32" /> 
                <h1 id="site-heading" > 
                    <a href="/" title="查看网站"><span id="site-title">监控后台</span></a> 
                </h1>
                <div id="wphead-info"> 
                    <div id="user_info"> 
                        <!-- <p>您好，<a href="javascript:void(0)" >admin</a> | <a href="javascript:void(0)" title="注销">注销</a></p> -->
                    </div> 
                </div>
            </div>
            <div id="wpbody">
                <ul id="adminmenu">
                    <li class="wp-first-item current menu-top menu-top-first menu-icon-dashboard " id="menu-dashboard">
                        <div class='wp-menu-image'>
                            <a href='/'><br/></a>
                        </div>
                        <div class="wp-menu-toggle">
                            <br/>
                        </div>
                        <a href='javascript:void(0)' class="wp-first-item current menu-top menu-top-first menu-icon-dashboard menu-top-last" tabindex="1">系统首页</a>
                    </li>
                    <li class="wp-menu-separator">
                        <a class="separator" href="javascript:void(0)" ><br/></a>
                    </li>
    
                    <li class="wp-has-submenu menu-top menu-icon-tools menu-top-first  wp-menu-open" id="menu-posts">
                        
                        <div class='wp-menu-image'>
                            <a href='javascript:void(0)'><br/></a>
                        </div>
                        <div class="wp-menu-toggle">
                            <br/>
                        </div>
                        <a href='javascript:void(0)' class="wp-has-submenu open-if-no-js menu-top menu-icon-tools menu-top-first" tabindex="1">请求和Logger</a>
                        
                        <div class='wp-submenu'>
                            <ul>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='/monitor/list' name="/monitor/list" tabindex="1">请求监控</a>
                                </li>
                                <li>
                                    <a href='/monitor/MLWLogList' name="" tabindex="1">日志监控</a>
                                </li>
                            </ul>

                        </div>
                    </li>
                    
                    <li class="wp-has-submenu menu-top menu-icon-appearance" id="menu-media">
                        <div class='wp-menu-image'>
                            <a href='javascript:void(0)'><br/></a>
                        </div>
                        <div class="wp-menu-toggle">
                            <br/>
                        </div>
                        <a href='javascript:void(0)' class="wp-has-submenu menu-top menu-icon-appearance" tabindex="1">监控分配</a>
                        <div class='wp-submenu'>

                            <ul>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='player/list' tabindex="1">参与人员</a>
                                </li>
                                <li>
                                    <a href='allocation/requestSettingList' name="a" tabindex="1">(请求)分配设置</a>
                                </li>
                                <li>
                                    <a href='allocation/requestExcludeSettingList' name="a" tabindex="1">(请求)分配特殊设置</a>
                                </li>
                                <li>
                                    <a href='playerRequest/list' name="a" tabindex="1">(请求)分配参与人员</a>
                                </li>
                                <li>
                                    <a href='player/appPlayerList' name="a" tabindex="1">(日志)应用分配设置</a>
                                </li>
                               <li>
                                    <a href='player/modulePlayerList' name="a" tabindex="1">(日志)模块分配设置</a>
                                </li>
                            </ul>
                        </div>
                    </li>

                    <li class="wp-has-submenu menu-top menu-icon-appearance" id="menu-media">
                        <div class='wp-menu-image'>
                            <a href='javascript:void(0)'><br/></a>
                        </div>
                        <div class="wp-menu-toggle">
                            <br/>
                        </div>
                        <a href='javascript:void(0)' class="wp-has-submenu menu-top menu-icon-appearance" tabindex="1">监控统计</a>
                        <div class='wp-submenu'>

                            <ul>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='statistics/requestTypeCount' name="" tabindex="1">请求总数统计(类型)</a>
                                </li>
                                <li>
                                    <a class="wp-first-item" href='statistics/requestHourCount' name="" tabindex="1">请求总数统计(小时)</a>
                                </li>
                                <li>
                                    <a class="wp-first-item" href='statistics/requestMap' name="" tabindex="1">请求地图分布</a>
                                </li>
                                <li>
                                    <a class="wp-first-item" href='statistics/order' name="" tabindex="1">订单统计</a>
                                </li>
                                <li>
                                    <a class="wp-first-item" href='statistics/orderExcel' name="" tabindex="1">导出订单</a>
                                </li>
                                <li>
                                    <a class="wp-first-item" href='nuomi/list' name="" tabindex="1">sku转化</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li class="wp-has-submenu menu-top menu-icon-appearance" id="menu-media">
                        <div class='wp-menu-image'>
                            <a href='javascript:void(0)'><br/></a>
                        </div>
                        <div class="wp-menu-toggle">
                            <br/>
                        </div>
                        <a href='javascript:void(0)' class="wp-has-submenu menu-top menu-icon-appearance" tabindex="1">Redis监控</a>
                        <div class='wp-submenu'>

                            <ul>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='redis/index' name="" tabindex="1">Redis状态监控</a>
                                </li>
                                <li>
                                    <a class="wp-first-item" href='redis/keys' name="" tabindex="1">Redis Keys</a>
                                </li>
                               
                            </ul>
                        </div>
                    </li>

                    <li class="wp-has-submenu menu-top menu-icon-appearance" id="menu-media">
                        <div class='wp-menu-image'>
                            <a href='javascript:void(0)'><br/></a>
                        </div>
                        <div class="wp-menu-toggle">
                            <br/>
                        </div>
                        <a href='javascript:void(0)' class="wp-has-submenu menu-top menu-icon-appearance" tabindex="1">指标统计</a>
                        <div class='wp-submenu'>
                            <ul>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='tjgeneral/dayReport' name="dayReport" tabindex="1">综合统计</a>
                                </li>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='tjgeneral/list?indexCode=pv' name="pv" tabindex="1">PV统计</a>
                                </li>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='tjgeneral/list?indexCode=uv' name="uv" tabindex="1">UV统计</a>
                                </li>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='tjgeneral/list?indexCode=login' name="login" tabindex="1">登录统计</a>
                                </li>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='tjgeneral/list?indexCode=register' name="register" tabindex="1">注册统计</a>
                                </li>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='tjgeneral/list?indexCode=ord' name="ord" tabindex="1">订单统计</a>
                                </li>
                                <li class="wp-first-item">
                                    <a class="wp-first-item" href='/union/query' name="sf" tabindex="1">顺丰订单</a>
                                </li>
                                <%--<li>
                                    <a class="wp-first-item" href='redis/keys' name="" tabindex="1">Redis Keys</a>
                                </li>--%>

                            </ul>
                        </div>
                    </li>

                    <li class="wp-menu-separator-last">
                        <a class="separator" href="javascript:void(0)"><br/></a>
                    </li>
                </ul>
                <div id="wpbody-content"> 
                    <div id="screen-meta"> 
                        <div id="contextual-help-wrap" class="hidden"> 
                            <div class="metabox-prefs">
                                <p>您好！ 欢迎访问帮助页面<p/>

                            </div> 
                        </div> 
                        <!-- <div id="screen-meta-links"> 
                            <div id="contextual-help-link-wrap" class="screen-meta-toggle"> 
                                <a href="#contextual-help" id="contextual-help-link" class="show-settings">帮助</a> 
                            </div>
                        </div>  -->
                    </div> 
 
                    <div class="wrap"> 
                        <div id="icon-index" class="icon32">
                            <br/>
                        </div> 
                        <h2 id="module-title">管理首页</h2>
                        <div id="dashboard-widgets-wrap">
                            <!--  此处显示自定义内容 -->
                            <div id="content" >
                                
                            </div>
                            
                        </div>
                            <!--  此处显示自定义内容 结束 -->
                            <div class="clear"></div> 
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="clear"></div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
    <div id="footer">
        <p id="footer-left" class="alignleft"> 感谢您使用，更多有趣功能，请参见帮助！</p>
        <p id="footer-upgrade" class="alignright">版本 1.0</p>
        <div class="clear"></div>
    </div>
    <script type="text/javascript">
    $(document).ready(function(){


        $(".wp-submenu  a").click(function(e){
            var href = $(this).attr("href");
            var title = $(this).text();
            var target = $(this).attr("target");
            if(target=='_blank'){
                return ;
            }
            $("#module-title").text(title);
            $.ajax({
              type: "POST",
              url: href
            }).done(function( msg ) {
              
              $("#content").html(msg);

            });
            e.preventDefault();
        });
    });
    </script>
</body>
</html>
