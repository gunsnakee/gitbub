package com.meiliwan.emall.imeiliwan.web.controller;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.imeiliwan.bean.WeixinOrder;
import com.meiliwan.emall.imeiliwan.service.WeixinOrderService;
import com.meiliwan.emall.imeiliwan.util.CommonUtil;
import com.meiliwan.emall.imeiliwan.util.HttpClientUtils;
import com.meiliwan.emall.imeiliwan.util.SHA1Util;
import com.meiliwan.emall.imeiliwan.util.WXSafeTool;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.client.OrdClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WeixinOrderController {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(WeixinOrderController.class);
    @Autowired
    private WeixinOrderService weixinOrderService;


    /**
     *接收并保存从getaway 传递过来的微信订单信息
     * 用于给微信做 发货通知的数据准备
     *
     * @return
     */
    @RequestMapping("/saveWeixinOrd")
    public void controller(HttpServletRequest request, HttpServletResponse response){
        String openId = request.getParameter("openId");
        String outTradeNo = request.getParameter("outTradeNo");
        String transId = request.getParameter("transId");
        Boolean success = false;
        if(StringUtils.isNotBlank(openId)&& StringUtils.isNotBlank(outTradeNo)
                &&StringUtils.isNotBlank(transId)){
            try{
                Ord ord = OrdClient.getOrdByOrdId(outTradeNo);
                WeixinOrder weixinOrder = weixinOrderService.getWxOrdByOrdId(outTradeNo);
                if(ord!=null && weixinOrder==null){   //如果已经保存了订单就不处理
                    WeixinOrder wOrd = new WeixinOrder();
                    wOrd.setTransid(transId);
                    wOrd.setOpenId(openId);
                    wOrd.setOutTradeNo(outTradeNo);
                    Integer id = weixinOrderService.add(wOrd);
                    if(id!=null && id>0){
                        success = true;
                    }
                }
            }catch (Exception e){
                Map map = new HashMap();
                map.put("desc","添加微信订单信息异常");
                map.put("openId",openId);
                map.put("outTradeNo",outTradeNo);
                map.put("transId",transId);
                logger.error(e,map,null);
            }
        }
        Map map = new HashMap();
        map.put("success",success);
        WebUtils.writeJsonByObj(map,response);
    }

    /**
     * 发货通知
     * admim后台做发货处理时，需调用该接口给微信服务端做发货通知
     * @param request
     * @param response
     */
    @RequestMapping("/deliverNotify")
    public void deliverNotify(HttpServletRequest request, HttpServletResponse response) {
        String ordId = request.getParameter("ordId");//订单号
        if(StringUtils.isNotBlank(ordId)){
            Ord ord = OrdClient.getOrdByOrdId(ordId);
            if(ord!=null){
                WeixinOrder wxOrd = weixinOrderService.getWxOrdByOrdId(ordId);
                if(wxOrd!=null){
                    String appId = WXSafeTool.getWxParam("AppId");
                    String appKey = WXSafeTool.getWxParam("AppKey");
                    String accessToken = WXSafeTool.getAccessToken();

                    String openId = wxOrd.getOpenId();
                    String transId = wxOrd.getTransid();   // 交易单号
                    String outTradeNo = wxOrd.getOutTradeNo();  // 第三方订单号（美丽湾订单号）
                    String deliverTimestamp = Math.round(new Date().getTime()/1000)+"";  //是发货时间戳，这里指的是 Linux 时间戳；注意只有10位

                    String deliverStatus = "1"; // 是发货状态，1 表明成功，0 表明失败
                    String deliverMsg = "ok"; //   是发货状态信息
                    String signMethod ="sha1";  //  是签名方法

                    HashMap<String,String> paramters = new HashMap<String, String>();
                    paramters.put("appid",appId);
                    paramters.put("appkey",appKey);
                    paramters.put("openid",openId);
                    paramters.put("transid",transId);
                    paramters.put("out_trade_no",outTradeNo);
                    paramters.put("deliver_timestamp",deliverTimestamp);
                    paramters.put("deliver_status",deliverStatus);
                    paramters.put("deliver_msg",deliverMsg);

                    String  app_signature =getAppSignature(paramters);

                    JsonObject json = new JsonObject();
                    json.addProperty("appid",appId);
                    json.addProperty("openid",openId);
                    json.addProperty("transid",transId);
                    json.addProperty("out_trade_no",outTradeNo);
                    json.addProperty("deliver_timestamp",deliverTimestamp);
                    json.addProperty("deliver_status",deliverStatus);
                    json.addProperty("deliver_msg",deliverMsg);
                    json.addProperty("app_signature",app_signature);
                    json.addProperty("sign_method","sha1");
                    String postData = json.toString();
                    System.out.println("postData="+json);

                    try{
                        StringBuffer res = HttpClientUtils.doPost(postData, WebConstant.WX_DELIVER_NOTIFY_URL+"?access_token="+accessToken);
                        res.length();
                    }catch(Exception e){
                       logger.error(e,paramters,null);
                    }
                }
            }
        }
    }

    /**
     *  生成发货通签名
     *
     * 参加签名字段为：appid、appkey、openid、transid、out_trade_no、deliver_timestamp、deliver_status、
     deliver_msg；
     * @param map
     * @return
     */
    private String getAppSignature(HashMap<String, String> map){
        HashMap<String, String> parameters = new HashMap<String, String>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
                map.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            if (item.getKey() != "") {
                parameters.put(item.getKey().toLowerCase(), item.getValue());
            }
        }
        String bizString = CommonUtil.FormatBizQueryParaMap(parameters,
                false);
        return SHA1Util.Sha1(bizString);

    }
}
