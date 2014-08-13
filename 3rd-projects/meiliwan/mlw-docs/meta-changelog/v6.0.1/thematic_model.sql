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
-- Table structure for table `thematic_model`
--

DROP TABLE IF EXISTS `thematic_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thematic_model` (
  `model_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '专题页对应区模块',
  `model_name` varchar(45) NOT NULL COMMENT '块名称，唯一',
  `model_ftl` text NOT NULL COMMENT '块的结构内容',
  `descp` varchar(45) DEFAULT NULL COMMENT '块的中文描述',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '模块类别(图片区 or 商品区)',
  `pronum_line` int(11) NOT NULL DEFAULT '0' COMMENT '一行的列数，决定样式',
  PRIMARY KEY (`model_id`),
  KEY `ind_type` (`type`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thematic_model`
--

LOCK TABLES `thematic_model` WRITE;
/*!40000 ALTER TABLE `thematic_model` DISABLE KEYS */;
INSERT INTO `thematic_model` VALUES (2,'productArea_2','<div class=\"mt20 overflow section3\" style=\"background:#${productAreaJsonVo.bgColor!};\" id=\"${anchorId}\">\n    <img src=\"${productAreaJsonVo.bannerPic!}\" alt=\"\" height=\"95\" width=\"990\">\n    <ul class=\"ml10 mt10 overflow\">\n    <#if productAreaJsonVo?? && productAreaJsonVo.proList??>\n        <#list productAreaJsonVo.proList as list>\n            <li class=\"bcfff fl mb10 mr10 pos-r\">\n                <input name=\"state\" value=\"${list.state}\" type=\"hidden\">\n                <input name=\"stock\" value=\"${list.stock}\" type=\"hidden\">\n                <span class=\"${list.mark!} l-0 pos-a t-0\"></span>\n                <a href=\"http://www.meiliwan.com/${list.proId}.html\" title=\"${list.proName}\"><img src=\"${list.picUrl}\" alt=\"${list.proName}\" class=\"fl ml20 mr20 mt20\" height=\"200\" width=\"200\"></a>\n                <p class=\"fcf74e20 fs16 mt20\">¥<strong class=\"fs30\">${list.mlwPrice?string(\'0.00\')}</strong></p>\n                <p class=\"c999 lh24 lineThrough\">参考价:￥${list.marketPrice?string(\'0.00\')}</p>\n                <p class=\"c333 fs14 lh24\"><a href=\"http://www.meiliwan.com/${list.proId}.html\" class=\"\" title=\"${list.proName}\">${list.proName}</a></p>\n                <p class=\"fcf74e20 lh24\">${list.advName!}</p>\n                <p class=\"mt10\"><a href=\"#\" class=\"btn-addToCart2\" name=\"${list.proId}\"></a></p>\n            </li>\n        </#list>\n    </#if>\n    </ul>\n</div>','商品区一行两列',0,2),(3,'productArea_4','<div class=\"mt20 overflow section2\" style=\"background:#${productAreaJsonVo.bgColor!};\" id=\"${anchorId}\">\n    <img src=\"${productAreaJsonVo.bannerPic!}\" alt=\"\" height=\"95\" width=\"990\">\n    <ul class=\"ml10 mt10 overflow\">\n        <#if productAreaJsonVo?? && productAreaJsonVo.proList??>\n            <#list productAreaJsonVo.proList as list>\n                <li class=\"bcfff fl mb10 mr10 pos-r\">\n                    <input name=\"state\" value=\"${list.state}\" type=\"hidden\">\n                    <input name=\"stock\" value=\"${list.stock}\" type=\"hidden\">\n                    <span class=\"${list.mark!} l-0 pos-a t-0\"></span>\n                    <p class=\"mt20 txtcenter\"><a href=\"http://www.meiliwan.com/${list.proId}.html\" target=\"_blank\" title=\"${list.proName}\"><img src=\"${list.picUrl}\" alt=\"${list.proName}\" height=\"200\" width=\"200\"></a></p>\n                    <p class=\"fcf74e20 fs16 lh22 ml5 mr5 mt10\">¥<strong class=\"fs24\">${list.mlwPrice?string(\'0.00\')}</strong></p>\n                    <p class=\"c999 lh20 lineThrough ml5 mr5\">参考价:${list.marketPrice?string(\'0.00\')}</p>\n                    <p class=\"h40 lh20 ml5 mr5 overflow\"><a href=\"http://www.meiliwan.com/${list.proId}.html\" class=\"fs14\" title=\"${list.proName}\">${list.proName}</a></p>\n                    <p class=\"mt15 txtcenter\"><a href=\"#\" class=\"btn-addToCart2\" name=\"${list.proId}\"></a></p>\n                </li>\n            </#list>\n        </#if>\n    </ul>\n</div>','商品区一行四列',0,4),(4,'productArea_5','<div class=\"mt20 overflow section1\" style=\"background:#${productAreaJsonVo.bgColor!};\" id=\"${anchorId}\">\n    <img src=\"${productAreaJsonVo.bannerPic!}\" alt=\"\" height=\"95\" width=\"990\">\n    <ul class=\"ml10 mt10 overflow\">\n       <#if productAreaJsonVo?? && productAreaJsonVo.proList??>\n           <#list productAreaJsonVo.proList as list>\n               <li class=\"bcfff fl mb10 mr10 pos-r\">\n                   <input name=\"state\" value=\"${list.state}\" type=\"hidden\">\n                   <input name=\"stock\" value=\"${list.stock}\" type=\"hidden\">\n                   <span class=\"${list.mark!} l-0 pos-a t-0\"></span>\n                   <p class=\"mt20 txtcenter\"><a href=\"http://www.meiliwan.com/${list.proId}.html\" target=\"_blank\" title=\"${list.proName}\"><img src=\"${list.picUrl}\" alt=\"${list.proName}\" height=\"160\" width=\"160\"></a></p>\n                   <p class=\"fcf74e20 fs16 lh22 ml5 mr5 mt10\">¥<strong class=\"fs24\">${list.mlwPrice?string(\'0.00\')}</strong></p>\n                   <p class=\"c999 lh20 lineThrough ml5 mr5\">参考价:￥${list.marketPrice?string(\'0.00\')}</p>\n                   <p class=\"h40 lh20 ml5 mr5 overflow\"><a href=\"http://www.meiliwan.com/${list.proId}.html\" class=\"fs14\" title=\"${list.proName}\">${list.proName}</a></p>\n                   <p class=\"mt15 txtcenter\"><a href=\"#\" class=\"btn-addToCart1\" name=\"${list.proId}\"></a></p>\n               </li>\n           </#list>\n       </#if>\n    </ul>\n</div>','商品区一行5列',0,5),(5,'picArea_1','<div class=\"mt20 overflow\" id=\"${anchorId}\">\n    <ul class=\"ml10\">\n    <#if picAreaJsonVo?? && picAreaJsonVo.picList??>\n        <#list picAreaJsonVo.picList as list>\n            <li class=\"fl mr10\">\n                <#if list.linkUrl?? && list.linkUrl!=\"\">\n                <a href=\"${list.linkUrl}\" title=\"\">\n                </#if>\n                <img src=\"${list.picUrl}\" alt=\"\" height=\"340\" width=\"990\">\n                <#if list.linkUrl?? && list.linkUrl!=\"\">\n                </a>\n                </#if>\n            </li>\n        </#list>\n    </#if>\n    </ul>\n</div>','图片区一行一列',1,1),(6,'picArea_2','<div class=\"mt20 overflow txtcenter w990\" id=\"${anchorId}\">\n    <ul class=\"inlineBlock overflow w1000\"> \n    <#if picAreaJsonVo?? && picAreaJsonVo.picList??>\n        <#list picAreaJsonVo.picList as list>\n            <li class=\"fl mr10\">\n                <#if list.linkUrl?? && list.linkUrl!=\"\">\n                <a href=\"${list.linkUrl}\" title=\"\">\n                </#if>\n                <img src=\"${list.picUrl}\" alt=\"\" height=\"340\" width=\"490\">\n                <#if list.linkUrl?? && list.linkUrl!=\"\">\n                </a>\n                </#if>\n            </li>\n        </#list>\n    </#if>\n    </ul>\n</div>\n','图片区一行两列',1,2),(7,'picArea_4','<div class=\"mt20 overflow\" id=\"${anchorId}\">\n    <ul class=\"ml10\">\n    <#if picAreaJsonVo?? && picAreaJsonVo.picList??>\n        <#list picAreaJsonVo.picList as list>\n            <li class=\"fl mr10\">\n                <#if list.linkUrl?? && list.linkUrl!=\"\">\n                <a href=\"${list.linkUrl}\" title=\"\">\n                </#if>\n                    <img src=\"${list.picUrl}\" alt=\"\" height=\"340\" width=\"235\">\n                <#if list.linkUrl?? && list.linkUrl!=\"\">\n                </a>\n                </#if>\n            </li>\n        </#list>\n    </#if>\n    </ul>\n</div>','图片区一行四列',1,4),(8,'picArea_5','<div class=\"mt20 overflow\" id=\"${anchorId}\">\n    <ul class=\"ml10\">\n        <#if picAreaJsonVo?? && picAreaJsonVo.picList??>\n            <#list picAreaJsonVo.picList as list>\n                <li class=\"fl mr10\">\n                    <#if list.linkUrl?? && list.linkUrl!=\"\">\n                        <a href=\"${list.linkUrl}\" title=\"\">\n                    </#if>\n                        <img src=\"${list.picUrl}\" alt=\"\" height=\"340\" width=\"186\">\n                    <#if list.linkUrl?? && list.linkUrl!=\"\">\n                        </a>\n                    </#if>\n                </li>\n            </#list>\n        </#if>\n    </ul>\n</div>','图片区一行五列',1,5);
/*!40000 ALTER TABLE `thematic_model` ENABLE KEYS */;
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
