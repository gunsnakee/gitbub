package com.meiliwan.emall.imeiliwan.web.controller;

import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.imeiliwan.util.HttpClientUtils;
import com.meiliwan.emall.imeiliwan.util.WXSafeTool;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 上午11:11
 * 微信订单 controller
 */

@Controller
@RequestMapping("/wx")
public class WeixinMenuController {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(WeixinMenuController.class);


    /**
     *创建自定义菜单
     *
     * @return
     */
    @RequestMapping("/rebuildMenu")
    public void controller(HttpServletRequest request, HttpServletResponse response){
        String postData = getMenuData();
        String accessToken = WXSafeTool.getAccessToken();
        Map resultMap = new HashMap();
        try{
            StringBuffer getOldRes = HttpClientUtils.doPost("", "https://api.weixin.qq.com/cgi-bin/menu/get"+"?access_token="+accessToken);
            resultMap.put("old menus",getOldRes);
            //1、先删除原有的菜单
            StringBuffer delRes = HttpClientUtils.doPost("", WebConstant.WX_MENU_DELETE_URL+"?access_token="+accessToken);
            resultMap.put("delRes",delRes);

            //2、然后在生成新菜单
            StringBuffer addRes = HttpClientUtils.doPost(postData, WebConstant.WX_MENU_CREATE_URL+"?access_token="+accessToken);
            resultMap.put("addRes",addRes);

            StringBuffer getNewRes = HttpClientUtils.doPost("", "https://api.weixin.qq.com/cgi-bin/menu/get"+"?access_token="+accessToken);
            resultMap.put("new menus",getNewRes);
            System.out.println("postData="+postData);
        }catch(Exception e){
            logger.error(e,postData,null);
        }
        WebUtils.writeJsonByObj(resultMap,response);
    }

    /**
     * 获取订单接口数据
     * @return
     */
    private String  getMenuData(){
      /*  String menuJson = "{\"button\":[{\"name\":\"购物专区\",\"sub_button\":[{\"type\":\"view\",\"name\":\"商品馆\",\"url\":\"http://m.meiliwan.com/html/commodity.html?store=snack\"},{\"type\":\"view\",\"name\":\"国家馆\",\"url\":\"http://m.meiliwan.com/html/country.html?place=thailand\"}]},\n" +
                "{\"name\":\"最新活动\",\"sub_button\":[{\"type\":\"view\",\"name\":\"激情世界杯\",\"url\":\"http://m.meiliwan.com/mopwan/worldcup\"},{\"type\":\"view\",\"name\":\"暑期专享\",\"url\":\"http://m.meiliwan.com/mopwan/sqfq\"},{\"type\":\"view\",\"name\":\"年中大促\",\"url\":\"http://m.meiliwan.com/mopwan/linqicuxiao\"}]}," +
                "{\"name\":\"自助服务\",\"sub_button\":[{\"type\":\"click\",\"name\":\"我的订单\",\"key\":\"my_order\"},{\"type\":\"click\",\"name\":\"物流查询\",\"key\":\"logistics_query\"},{\"type\":\"view\",\"name\":\"下载app\",\"url\":\"http://app.qq.com/#id=detail&appid=100491211\"},{\"type\":\"click\",\"name\":\"维权\",\"key\":\"weiquan\"}]}]}";
       */
       /* String menuJson = "{\"button\":[{\"name\":\"购物专区\",\"sub_button\":[{\"type\":\"view\",\"name\":\"商品馆\",\"url\":\"http://m.meiliwan.com/html/commodity.html?store=snack\"},{\"type\":\"view\",\"name\":\"国家馆\",\"url\":\"http://m.meiliwan.com/html/country.html?place=thailand\"}]},\n" +
                "{\"name\":\"最新活动\",\"sub_button\":[{\"type\":\"view\",\"name\":\"红菇3折\",\"url\":\"http://m.meiliwan.com/mopwan/honggu\"},{\"type\":\"view\",\"name\":\"特色调料\",\"url\":\"http://m.meiliwan.com/mopwan/tiaoweiliao\"},{\"type\":\"view\",\"name\":\"品茗养生\",\"url\":\"http://m.meiliwan.com/mopwan/pmys\"}]}," +
                "{\"name\":\"自助服务\",\"sub_button\":[{\"type\":\"click\",\"name\":\"我的订单\",\"key\":\"my_order\"},{\"type\":\"click\",\"name\":\"物流查询\",\"key\":\"logistics_query\"},{\"type\":\"view\",\"name\":\"下载app\",\"url\":\"http://app.qq.com/#id=detail&appid=100491211\"},{\"type\":\"click\",\"name\":\"维权\",\"key\":\"weiquan\"}]}]}";
*/
       /* String menuJson = "{\"button\":[{\"name\":\"购物专区\",\"sub_button\":[{\"type\":\"view\",\"name\":\"商品馆\",\"url\":\"http://m.meiliwan.com/html/commodity.html?store=snack\"},{\"type\":\"view\",\"name\":\"国家馆\",\"url\":\"http://m.meiliwan.com/html/country.html?place=thailand\"}]},\n" +
                "{\"name\":\"最新活动\",\"sub_button\":[{\"type\":\"view\",\"name\":\"浪漫七夕\",\"url\":\"http://m.meiliwan.com/mopwan/qixi\"},{\"type\":\"view\",\"name\":\"特色调料\",\"url\":\"http://m.meiliwan.com/mopwan/tiaoweiliao\"},{\"type\":\"view\",\"name\":\"品茗养生\",\"url\":\"http://m.meiliwan.com/mopwan/pmys\"}]}," +
                "{\"name\":\"自助服务\",\"sub_button\":[{\"type\":\"click\",\"name\":\"我的订单\",\"key\":\"my_order\"},{\"type\":\"click\",\"name\":\"物流查询\",\"key\":\"logistics_query\"},{\"type\":\"view\",\"name\":\"下载app\",\"url\":\"http://app.qq.com/#id=detail&appid=100491211\"},{\"type\":\"click\",\"name\":\"维权\",\"key\":\"weiquan\"}]}]}";
       */
        String menuJson = "{\"button\":[{\"name\":\"购物专区\",\"sub_button\":[{\"type\":\"view\",\"name\":\"商品馆\",\"url\":\"http://m.meiliwan.com/html/commodity.html?store=snack\"},{\"type\":\"view\",\"name\":\"国家馆\",\"url\":\"http://m.meiliwan.com/html/country.html?place=thailand\"}]},\n" +
                "{\"name\":\"最新活动\",\"sub_button\":[{\"type\":\"view\",\"name\":\"中秋豪礼\",\"url\":\"http://m.meiliwan.com/mopwan/moon\"},{\"type\":\"view\",\"name\":\"特色调料\",\"url\":\"http://m.meiliwan.com/mopwan/tiaoweiliao\"},{\"type\":\"view\",\"name\":\"品茗养生\",\"url\":\"http://m.meiliwan.com/mopwan/pmys\"}]}," +
                "{\"name\":\"自助服务\",\"sub_button\":[{\"type\":\"click\",\"name\":\"我的订单\",\"key\":\"my_order\"},{\"type\":\"click\",\"name\":\"物流查询\",\"key\":\"logistics_query\"},{\"type\":\"view\",\"name\":\"下载app\",\"url\":\"http://app.qq.com/#id=detail&appid=100491211\"},{\"type\":\"click\",\"name\":\"维权\",\"key\":\"weiquan\"}]}]}";

        return menuJson;
    }

    /**
     *创建自定义菜单
     *
     * @return
     */
    @RequestMapping("/rbMenuByParam")
    public void rbMenuByParam(HttpServletRequest request, HttpServletResponse response){
       // String postData = getMenuData();
        String postData = request.getParameter("menuStr");
        String sign = request.getParameter("sign");//请求签名
        Map resultMap = new HashMap();
        if(StringUtils.isNotBlank(postData) && StringUtils.isNotBlank(sign)){
            String accessToken = WXSafeTool.getAccessToken();

            try{
                StringBuffer getOldRes = HttpClientUtils.doPost("", "https://api.weixin.qq.com/cgi-bin/menu/get"+"?access_token="+accessToken);
                resultMap.put("old menus",getOldRes);
                //1、先删除原有的菜单
                StringBuffer delRes = HttpClientUtils.doPost("", WebConstant.WX_MENU_DELETE_URL+"?access_token="+accessToken);
                resultMap.put("delRes",delRes);

                //2、然后在生成新菜单
                StringBuffer addRes = HttpClientUtils.doPost(postData, WebConstant.WX_MENU_CREATE_URL+"?access_token="+accessToken);
                resultMap.put("addRes",addRes);

                StringBuffer getNewRes = HttpClientUtils.doPost("", "https://api.weixin.qq.com/cgi-bin/menu/get"+"?access_token="+accessToken);
                resultMap.put("new menus",getNewRes);
                System.out.println("postData="+postData);
            }catch(Exception e){
                logger.error(e,postData,null);
            }
        }
        WebUtils.writeJsonByObj(resultMap,response);
    }
}
