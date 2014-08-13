-- MySQL dump 10.13  Distrib 5.5.30, for Linux (x86_64)
--
-- Host: 10.249.15.141    Database: mlw_cms
-- ------------------------------------------------------
-- Server version	5.5.30-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `thematic_template`
--

DROP TABLE IF EXISTS `thematic_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thematic_template` (
  `template_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_name` varchar(45) NOT NULL COMMENT '模板名称',
  `template_ftl` text COMMENT '用来存储模板页面ftl的,也可以空着，存到项目的web文件中',
  PRIMARY KEY (`template_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thematic_template`
--

LOCK TABLES `thematic_template` WRITE;
/*!40000 ALTER TABLE `thematic_template` DISABLE KEYS */;
INSERT INTO `thematic_template` VALUES (3,'简版','<!DOCTYPE html>\n<html>\n<head>\n    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">\n    <title>美丽湾-${title}</title>\n    <meta content=\"${keyword}\" name=\"keywords\">\n    <meta content=\"${desc}\" name=\"description\">\n    <link href=\"http://www.meiliwan.com/css/com.css\" rel=\"stylesheet\">\n    <link href=\"http://www.meiliwan.com/css/thematic/style.css\" rel=\"stylesheet\">\n    <style type=\"text/css\">\n\n    </style>\n    <script>\n\n    </script>\n</head>\n<body>\n<!--容器 开始-->\n<div class=\"container\">\n<input type=\"hidden\" id=\"pageState\" name=\"pageState\" value=\"${pageState}\">\n<!--头部 开始-->\n${topbar}\n<!--头部 结束-->\n<!--主体 开始-->\n<div class=\"main mlra overflow\" style=\"background:#${pageParam.bgColor} url(${pageParam.headImg}) no-repeat center 0 ;\">\n    <div class=\"mlra overflow w990\">\n    ${areaContent}\n    </div>\n    <div class=\"clear h60\"></div>\n</div>\n<!--主体 结束-->\n<!--尾部 开始-->\n    ${footer}\n<!--尾部 结束-->\n    <div class=\"floatSide\">\n        <#list anchorList as list>\n            <#if list_index == 0>\n                 <img src=\"${list}\" alt=\"\"><br>\n                <#else>\n                 <a href=\"#${list_index}\"><img src=\"${list}\" alt=\"\"></a><br>\n            </#if>\n        </#list>\n    </div>\n</div>\n<!--容器 结束-->\n\n<div class=\"pop_tip overflow\" style=\"display:none; z-index:99999;\">\n    <div class=\"pop_tip_title h30 lh28\" style=\"background:#fe642c;\">\n        <!--a href=\"#\" class=\"bold fr fs14 mr10 white\">X</a-->\n        <span class=\"fs14 ml10 white yahei\">温馨提示</span>\n    </div>\n    <div class=\"pop_content bcfff\">\n        <img src=\"http://www.meiliwan.com/images/thematic/img175x135.png\" alt=\"\" class=\"fl ml30 mr25\" height=\"135\" style=\"margin-bottom:45px;margin-top:45px;\" width=\"175\">\n        <p class=\"c333 lh24\" style=\"padding-top:100px;\"><strong class=\"fs14\">这个活动已经结束啦，不过美丽湾还有更多精彩活动哦！</strong></p>\n        <p class=\"lh24\"><a href=\"http://www.meiliwan.com/\" class=\"fc235f9d underline\">如果您的浏览器没有自动跳转，请点击链接</a></p>\n        <div class=\"clear\"></div>\n    </div>\n</div>\n\n<script src=\"http://www.meiliwan.com/js/jquery-1.8.3.min.js?v=5.1.4.2\"></script>\n<script src=\"http://www.meiliwan.com/js/mlw.core.js?v=5.1.4.2\"></script>\n<script src=\"http://www.meiliwan.com/js/com.js?v=5.1.4.2\"></script>\n<script src=\"http://www.meiliwan.com/js/mlw.dialogbox.js?v=6.0.0\"></script>\n<script src=\"http://www.meiliwan.com/js/cart/cart.js?v=6.0.0\"></script>\n<!--[if IE 6]>\n<script src=\"http://www.meiliwan.com/js/DD_belatedPNG_0.0.8a.js\"></script>\n<script>\n    DD_belatedPNG.fix(\'body *\');//图标\n</script>\n<![endif]-->\n<script>\n    $(\".section1 li\").add(\".section2 li\").hover(function(){\n        $(this).addClass(\"hover\");\n    },function(){\n        $(this).removeClass(\"hover\");\n    });\n    /**\n     *加入购物车\n     */\n    $(\"a.btn-addToCart1\").add(\"a.btn-addToCart2\").not(\".disabled\").on(\'click\', function(e) {\n        e.preventDefault();\n        var proId = $(this).attr(\'name\');\n        $.post(\"http://www.meiliwan.com/product/checkStock\", {\n            proId : proId\n        }, function(data) {\n            if (data > 0) {\n                imlw.cart.addCartOpt(\"product\", proId, 1, imlw.cart.addDefaultCallback);\n            } else {\n                $.mlwbox.remind(\'库存不足！\', null, {\n                    title : \'提示\',\n                    bar : true\n                });\n            }\n        }, \"html\");\n        return false;\n    });\n    $(\"a.disabled\").on(\"click\",function(){\n        return false;\n    });\n    $(\".floatSide2\").hide();\n$(\".floatSide\").find(\"a:last\").gotop();\n    /*时间到期后调用弹出框方法*/\nvar PoP=function(){\n    $.pop({\n        bullet: \'.pop_tip\',\n        mask: \'<div class=\"mask\"></div>\'\n    });\n    setTimeout(function(){\n        location.href=\"http://www.meiliwan.com/\";\n    },3000);\n};\nvar end = $(\"#pageState\").val();\nif(end!=1){\n    PoP();\n}\n</script>\n<script>\n    var _bdhmProtocol = ((\"https:\" == document.location.protocol) ? \" https://\" : \" http://\");\n    document.write(unescape(\"%3Cscript src=\'\" + _bdhmProtocol + \"hm.baidu.com/h.js%3F3b8970e87794fecc64ecdf684350400d\' type=\'text/javascript\'%3E%3C/script%3E\"));\n</script>\n</body>\n\n</html>\n');
/*!40000 ALTER TABLE `thematic_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-28 18:31:06
