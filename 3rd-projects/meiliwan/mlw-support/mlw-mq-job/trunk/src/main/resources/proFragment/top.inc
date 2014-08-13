<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Pragma" content="no-cache" >
    <meta http-equiv="Expires" content="Mon, 12 May 2001 00:20:00 GMT" >
    <meta http-equiv="Cache-Control" content="no-cache" >
    
    <title>#proName</title>
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/common.css">
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/module.css">
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/detail.css">
    <link rel="stylesheet" type="text/css" href="http://www.meiliwan.com/css/jquery.jqzoom.css">
    <script type="text/javascript" src="http://www.meiliwan.com/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="http://www.meiliwan.com/js/mlw.core.js"></script>
    <script src="http://www.meiliwan.com/js/jquery.Jdropdown.js"></script>
    <script src="http://www.meiliwan.com/js/jquery.mlw.plugin.js"></script>
    <script src="http://www.meiliwan.com/js/mlw.dialogbox.js"></script>
    <script src="http://www.meiliwan.com/js/jquery.SuperSlide.source.js"></script>
    <script src="http://www.meiliwan.com/js/jquery.jqzoom-core.js"></script>
    <script src="http://www.meiliwan.com/js/collect/collect.js"></script>
    <script src="http://www.meiliwan.com/js/user/passport.js"></script>
    

    <script>
        $(function(){
            var inviteCode= MLW.cookie.getCookie('i_code');
            if(inviteCode ==undefined || inviteCode == null){
                window.location.href="http://www.meiliwan.com/invite/invite_show?url="+window.location.href;
            }
        });
    </script>

</head>
<body class="p-detail">