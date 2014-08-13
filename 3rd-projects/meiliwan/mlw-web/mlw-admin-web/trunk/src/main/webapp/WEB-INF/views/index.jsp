<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<html>
<head>
<title>欢迎进入美丽湾后台管理</title>
    <link href="themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
    <link href="uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
    <!--[if IE]>
    <link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->
    <script src="js/kindeditor/kindeditor-min.js" type="text/javascript"></script>
    <script src="js/kindeditor/lang/zh_CN.js" type="text/javascript"></script>
    <script src="js/kindeditor/jquery.js" type="text/javascript"></script>
    <script src="js/speedup.js" type="text/javascript"></script>
    <script src="js/jquery-1.7.2.js" type="text/javascript"></script>
    <script src="js/jquery.cookie.js" type="text/javascript"></script>
    <script src="js/jquery.validate.js" type="text/javascript"></script>
    <script src="js/jquery.bgiframe.js" type="text/javascript"></script>
    <script src="uploadify/scripts/swfobject.js" type="text/javascript"></script>
    <script src="uploadify/scripts/jquery.uploadify.v2.1.0.js" type="text/javascript"></script>
    <script src="projs/jquery.uploadify-3.1.min.js" type="text/javascript"></script>
    <!-- svg图表  supports Firefox 3.0+, Safari 3.0+, Chrome 5.0+, Opera 9.5+ and Internet Explorer 6.0+ -->
    <script type="text/javascript" src="chart/raphael.js"></script>
    <script type="text/javascript" src="chart/g.raphael.js"></script>
    <script type="text/javascript" src="chart/g.bar.js"></script>
    <script type="text/javascript" src="chart/g.line.js"></script>
    <script type="text/javascript" src="chart/g.pie.js"></script>
    <script type="text/javascript" src="chart/g.dot.js"></script>

    <script src="js/dwz.core.js" type="text/javascript"></script>
    <script src="js/dwz.util.date.js" type="text/javascript"></script>
    <script src="js/dwz.validate.method.js" type="text/javascript"></script>
    <script src="js/dwz.regional.zh.js" type="text/javascript"></script>
    <script src="js/dwz.barDrag.js" type="text/javascript"></script>
    <script src="js/dwz.drag.js" type="text/javascript"></script>
    <script src="js/dwz.tree.js" type="text/javascript"></script>
    <script src="js/dwz.accordion.js" type="text/javascript"></script>
    <script src="js/dwz.ui.js" type="text/javascript"></script>
    <script src="js/dwz.theme.js" type="text/javascript"></script>
    <script src="js/dwz.switchEnv.js" type="text/javascript"></script>
    <script src="js/dwz.alertMsg.js" type="text/javascript"></script>
    <script src="js/dwz.contextmenu.js" type="text/javascript"></script>
    <script src="js/dwz.navTab.js" type="text/javascript"></script>
    <script src="js/dwz.tab.js" type="text/javascript"></script>
    <script src="js/dwz.resize.js" type="text/javascript"></script>
    <script src="js/dwz.dialog.js" type="text/javascript"></script>
    <script src="js/dwz.dialogDrag.js" type="text/javascript"></script>
    <script src="js/dwz.sortDrag.js" type="text/javascript"></script>
    <script src="js/dwz.cssTable.js" type="text/javascript"></script>
    <script src="js/dwz.stable.js" type="text/javascript"></script>
    <script src="js/dwz.taskBar.js" type="text/javascript"></script>
    <script src="js/dwz.ajax.js" type="text/javascript"></script>
    <script src="js/dwz.pagination.js" type="text/javascript"></script>
    <script src="js/dwz.database.js" type="text/javascript"></script>
    <script src="js/dwz.datepicker.js" type="text/javascript"></script>
    <script src="js/dwz.effects.js" type="text/javascript"></script>
    <script src="js/dwz.panel.js" type="text/javascript"></script>
    <script src="js/dwz.checkbox.js" type="text/javascript"></script>
    <script src="js/dwz.history.js" type="text/javascript"></script>
    <script src="js/dwz.combox.js" type="text/javascript"></script>
    <script src="js/dwz.print.js" type="text/javascript"></script>
    <script src="js/mlw.js" type="text/javascript"></script>
    <!--
    <script src="bin/dwz.min.js" type="text/javascript"></script>
    -->
    <script src="js/dwz.regional.zh.js" type="text/javascript"></script>

    <script type="text/javascript">
        $(function(){
            DWZ.init("/js/dwz.frag.xml", {
                loginUrl:"/loginAjax", loginTitle:"登录",	// 弹出登录对话框
//		loginUrl:"login.html",	// 跳到登录页面
                statusCode:{ok:200, error:300, timeout:301}, //【可选】
                pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
                debug:false,	// 调试模式 【true|false】
                callback:function(){
                    initEnv();
                    $("#themeList").theme({themeBase:"themes"}); // themeBase 相对于index页面的主题base路径
                }
            });
        });

    </script>
</head>
<body scroll="no">
<div id="layout">
<div id="header">
    <div class="headerNav">
        <a class="logo" href="/index">标志</a>

        <ul class="nav">   <!--
            <li id="switchEnvBox"><a href="javascript:">（<span>北京</span>）切换城市</a>
                <ul>
                    <li><a href="sidebar_1.html">北京</a></li>
                    <li><a href="sidebar_2.html">上海</a></li>
                    <li><a href="sidebar_2.html">南京</a></li>
                    <li><a href="sidebar_2.html">深圳</a></li>
                    <li><a href="sidebar_2.html">广州</a></li>
                    <li><a href="sidebar_2.html">天津</a></li>
                    <li><a href="sidebar_2.html">杭州</a></li>
                </ul>
            </li>
            -->
            <li><a href="/bkstage/user/settingPwd" target="dialog" width="600">设置密码</a></li>
            <li><a href="/logout">退出</a></li>
        </ul>
        <ul class="themeList" id="themeList">
            <li theme="default"><div class="selected">蓝色</div></li>
            <li theme="green"><div>绿色</div></li>
            <!--<li theme="red"><div>红色</div></li>-->
            <li theme="purple"><div>紫色</div></li>
            <li theme="silver"><div>银色</div></li>
            <li theme="azure"><div>天蓝</div></li>
        </ul>
    </div>
    <!-- navMenu -->
</div>
<div id="leftside">
    <div id="sidebar_s">
        <div class="collapse">
            <div class="toggleCollapse"><div></div></div>
        </div>
    </div>
    <div id="sidebar" style="overflow:auto;">
        <div class="toggleCollapse"><h2>主菜单</h2><div>收缩</div></div>

        <div class="accordion" fillSpace="sidebar">
           ${menuHtml}
        </div>
    </div>
</div>
<div id="container">
    <div id="navTab" class="tabsPage">
        <div class="tabsPageHeader">
            <div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
                <ul class="navTab-tab">
                    <li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
                </ul>
            </div>
            <div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
            <div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
            <div class="tabsMore">more</div>
        </div>
        <ul class="tabsMoreList">
            <li><a href="javascript:;">我的主页</a></li>
        </ul>
        <div class="navTab-panel tabsPageContent layoutBox">
            <div class="page unitBox">
                <div class="pageFormContent" >
                    <div class="divider"></div>
                    <h2>商品相关 —> 商品下拉列表:</h2>
                    <div class="unit"><span style="color: #ff0000">商品管理</span>（包括商品上架、下架、删除、审核、翻译等功能）</div>
                    <div class="unit"><span style="color: #ff0000">商品添加</span>（根据类目添加商品）</div>
                    <div class="unit"><span style="color: #ff0000">咨询管理</span>（可以对买家和卖家的相关商品咨询进行管理）</div>
                    <div class="divider"></div>
                    <h2>订单管理：</h2>
                    <div class="unit"><span style="color: #ff0000">正向订单管理</span> （正常下单之后生成的订单，客服可查看列表、更改订单各状态、导出财务结算报表等功能。）</div>
                    <div class="unit"><span style="color: #ff0000">订单配送管理 </span>（主要是仓库对订单的管理，这里全部是查询可出库以上的订单，取消或删除的订单不展示。仓库人员可仓库、发货、收货、导出仓库需要的各种报表等功能）</div>
                    <div class="unit"><span style="color: #ff0000">逆向订单管理</span>（也就是退换货管理。在售后服务->退换货管理下进行管理，可查询不同状态的退换货列表，跟进退换货进度，修改退换货各种状态。包括客服审核、仓库收发货、财务退款等功能）</div>
                    <div class="divider"></div>
                    <h2>cms管理（拥有对全站静态文件进行管理和生成）:</h2>
                    <div class="unit"><span style="color: #ff0000">首页管理</span> （主要负责对美丽湾首页分区进行管理，可以进行分区编辑，然后生成首页静态文件）</div>
                    <div class="unit"><span style="color: #ff0000">类目页管理 </span>（主要负责对美丽湾三个级别类目进行管理，并且可以对每个类目的静态页进行编辑，然后生成类目页静态文件）</div>
                    <div class="unit"><span style="color: #ff0000">页面模板生成</span>（包括：1、生成首页类目模版和HTML；2、生成公共类目模版；3、生成所有类目Htm；4、生成所有商品Html；5、生成所有资讯Html；6、生成所有帮助Html；7、生成所有热销排行Html）</div>
                    <div class="divider"></div>
                    <h2>技术联系:</h2>
                    <table class="table" width="1000" layoutH="205" >
                        <thead>
                        <tr>
                            <th align="center">姓名</th>
                            <th align="center">电话号码</th>
                            <th align="center">邮箱</th>
                            <th align="center">负责模块</th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>别晓峰</td>
                                <td>18577841077</td>
                                <td>xiaofeng.bie@opi-corp.com</td>
                                <td>整个项目统一管理</td>
                            </tr>
                            <tr>
                                <td>玉雄</td>
                                <td>18677115139</td>
                                <td>xiong.yu@opi-corp.com</td>
                                <td>整个项目统一管理</td>
                            </tr>
                            <tr>
                                <td>刘尚风</td>
                                <td>15296481397</td>
                                <td>shangfeng.liu@opi-corp.com</td>
                                <td>支付支付流程、平台框架和缓存服务</td>
                            </tr>
                            <tr>
                                <td>肖斌</td>
                                <td>18677067820 18776946028</td>
                                <td>bin.xiao@opi-corp.com</td>
                                <td>订单退换货流程，购物车、管理后台店铺，后台权限</td>
                            </tr>
                            <tr>
                                <td>伍字鑫</td>
                                <td>15977479547</td>
                                <td>zixin.wu@opi-corp.com</td>
                                <td>商品管理、商品类目、商品属性、国家馆</td>
                            </tr>
                            <tr>
                                <td>彭文乐</td>
                                <td>18648904851</td>
                                <td>wenle.peng@opi-corp.com</td>
                                <td>前端店铺、统一cms管理</td>
                            </tr>
                            <tr>
                                <td>唐光德</td>
                                <td>15177940168</td>
                                <td>guangde.tang@opi-corp.com</td>
                                <td>订单管理、退货流程、咨询、投诉</td>
                            </tr>
                            <tr>
                                <td>吴家武</td>
                                <td>18176876269</td>
                                <td>jiawu.wu@opi-corp.com</td>
                                <td>passport,缓存设计</td>
                            </tr>
                            <tr>
                                <td>卓营高</td>
                                <td>18665311931</td>
                                <td>yinggao.zhuo@opi-corp.com</td>
                                <td>订单管理、运费相关、日志和监控系统</td>
                            </tr>
                            <tr>
                                <td>罗贻友</td>
                                <td>15278009126</td>
                                <td>yiyou.luo@opi-corp.com</td> 
                                <td>商品评论相关、搜索列表相关</td>
                            </tr>
                            <tr>
                                <td>吴玉敬</td> 
                                <td>18611685941</td>
                                <td>yujing.wu@opi-corp.com</td>
                                <td>店铺首页、cms管理、敏感词管理</td>
                            </tr>
                            <tr>
                                <td>梁庄龙</td>
                                <td>18642983809</td>
                                <td>zhuanglong.liang@opi-corp.com</td>
                                <td>商品详情页</td>
                            </tr>
                            <tr>
                                <td>梁国宁</td>
                                <td>13761713515</td>
                                <td>guoning.liang@opi-corp.com</td>
                                <td>搜索相关</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<div id="footer">Copyright &copy; 2013 <a href="demo_page2.html" target="dialog">美丽湾团队</a> Tel：+86771-5086886 Ext 6641</div>
</body>
</html>