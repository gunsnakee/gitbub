package com.meiliwan.emall.imeiliwan.web.controller;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.imeiliwan.bean.WeixinOrder;
import com.meiliwan.emall.imeiliwan.model.message.notify.WarnMsg;
import com.meiliwan.emall.imeiliwan.service.WeixinOrderService;
import com.meiliwan.emall.imeiliwan.util.CommonUtil;
import com.meiliwan.emall.imeiliwan.util.HttpClientUtils;
import com.meiliwan.emall.imeiliwan.util.SHA1Util;
import com.meiliwan.emall.imeiliwan.util.WXSafeTool;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.client.OrdClient;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 14-06-29
 * Time: 上午11:11
 * 微信维权 controller
 */

@Controller
@RequestMapping("/wx")
public class WxWeiquanController {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(WxWeiquanController.class);

    /**
     * 接收维权通知
     * 用户在公众号进行支付贩买行为出现异常时，通常会投诉到腾讯客服，因此微信侧需要
     即时了解公众号不用户交易的详情
     * @param request
     * @param response
     */
    @RequestMapping("/weiquan")
    public void weiquan(HttpServletRequest request, HttpServletResponse response) {
        String wqMsg = getWqMsg(request);
        logger.info("维权信息",wqMsg,WebUtils.getIpAddr(request));
        try{
            sendSuccessToWX(response);
        } catch(Exception e){
            logger.warn("维权通知success响应失败",wqMsg,WebUtils.getIpAddr(request));
        }

    }

    /**
     * 获取微信后台推送的 维权消息
     * 备注：后期美丽湾维权系统时，请注意做签名校验，以确认是微信后台推送的消息
     * @param request
     * @return
     */
    private String getWqMsg(HttpServletRequest request){
        String xmlStr = null;
        try{
            ServletInputStream in = request.getInputStream();
            XStream xs = new XStream(new DomDriver());
            xs.alias("xml", WarnMsg.class);
            StringBuilder xmlMsg = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1;) {
                xmlMsg.append(new String(b, 0, n, "UTF-8"));
            }
            xmlStr = xmlMsg.toString();
        }catch(Exception e){
            Map errorMap = new HashMap();
            errorMap.put("errorMsg","获取维权通知消息异常");
            logger.error(e,errorMap, WebUtils.getIpAddr(request));
        }
        return xmlStr;
    }

    /**
     * 商户收到维权通知后，需要成功返回 success
     * @throws java.io.IOException
     */
    public void sendSuccessToWX(HttpServletResponse response) throws IOException {
        String strHtml = "success";
        PrintWriter out = response.getWriter();
        out.println(strHtml);
        out.flush();
        out.close();
    }
}
