<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<html>
<head>
<title>登录注册页</title>
<link href="themes/css/login.css" rel="stylesheet" type="text/css" />
<script src="http://www.meiliwan.com/js/jquery-1.8.3.min.js"></script>
<script src="http://www.meiliwan.com/js/user/user.js"></script>
</head>
<body>
<div id="login">
    <div id="login_header">
        <h1 class="login_logo">
            <a href="/"><img src="/themes/default/images/logo.png" /></a>
        </h1>
        <div class="login_headerContent">
            <div class="navList">
                <ul>
                    <li><a href="#">设为首页</a></li>
                    <li><a href="#">反馈</a></li>
                    <li><a href="#" target="_blank">帮助</a></li>
                </ul>
            </div>
            <h2 class="login_title"><img src="themes/default/images/login_title.png" /></h2>
        </div>
    </div>
    <div id="login_content">
        <div class="loginForm">
            <div>
                <c:if test="${loginResult eq 1}">
                    <p style="text-align: center;color: red;">
                        用户名或者密码不正确
                    </p>
                </c:if>
                <c:if test="${loginResult eq 2}">
                    <p style="text-align: center;color: red;">
                        验证码不正确
                    </p>
                </c:if>
            </div>
            <form action="/login" method="post" id="J_Login_Form">
                <p>
                    <label>用户名：</label>
                    <input type="text" name="username" size="20" class="login_input" />
                </p>
                <p>
                    <label>密码：</label>
                    <input type="password" name="password" size="20" class="login_input" />
                </p>
                <p>
                    <label>验证码：</label>
                   <%-- <input class="login_input" id="J_verCode_login" name="J_verCode_login" type="password">--%>
                    <input class="login_input" id="J_verCode_login" name="J_verCode_login" type="password">
                </p>
                <p>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img id="captchaImg" src="http://imagecode.meiliwan.com/captcha/getCaptcha" width="80" height="30">
                    <a id="changeCaptcha" href="javascript:;" >看不清楚，换一张</a>
                </p>

               <%-- <p>
                    <label>验证码：</label>
                    <input class="code" type="text" size="5" />
                    <span><img src="themes/default/images/header_bg.png" alt="" width="75" height="24" /></span>
                </p>--%>
                <div class="login_bar">
                    <input class="sub" type="submit" value=" " />
                </div>
            </form>
        </div>
        <div class="login_banner"><img src="themes/default/images/login_banner.jpg" /></div>
        <div class="login_main">
            <ul class="helpList">
                <li><a href="#">下载驱动程序</a></li>
                <li><a href="#">如何安装密钥驱动程序？</a></li>
                <li><a href="#">忘记密码怎么办？</a></li>
                <li><a href="#">为什么登录失败？</a></li>
            </ul>
            <div class="login_inner">
                <p>您可以使用 网易网盘 ，随时存，随地取</p>
                <p>您还可以使用 闪电邮 在桌面随时提醒邮件到达，快速收发邮件。</p>
                <p>在 百宝箱 里您可以查星座，订机票，看小说，学做菜…</p>
            </div>
        </div>
    </div>
    <div id="login_footer">
        <div id="footer">Copyright &copy; 2013 <a href="#" target="dialog">美丽湾团队</a> Tel：+86771-5086886 Ext 6641</div>
    </div>
</div>
<script>
    $(function(){
        var srchref="http://imagecode.meiliwan.com/captcha/getCaptcha?r="+new Date().getTime();
        $('#captchaImg').attr("src",srchref);
        user.refleshImgCode($("#changeCaptcha"), $("#captchaImg"));

        $('#captchaImg').click(function(){
            $("#changeCaptcha").click();
        });


        $(document).keypress(function(e){
            if(e.keyCode==13){
                e.preventDefault();
                $('#J_Login_Form').submit();
            }
        });
    });
</script>
</body>
</html>
