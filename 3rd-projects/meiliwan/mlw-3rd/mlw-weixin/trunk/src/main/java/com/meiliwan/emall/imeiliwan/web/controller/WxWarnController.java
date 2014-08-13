package com.meiliwan.emall.imeiliwan.web.controller;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.imeiliwan.model.message.notify.WarnMsg;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 14-06-29
 * Time: 上午11:11
 * 告警 controller
 */

@Controller
@RequestMapping("/wx")
public class WxWarnController {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(WxWarnController.class);

    /**
     *告警通知
     * 为了及时通知商户异常，提高商户在微信平台的服务质量。微信后台会向商户推送告警
     通知，包括发货延迟、调用失败、通知失败等情况
     本接口用于接收此类的问题
     * 处理逻辑：
     * 1、获取通知消息，并保存
     * 2、给微信后台返回 success
     * @param request
     * @param response
     */
    @RequestMapping("/warn")
    public void warn(HttpServletRequest request, HttpServletResponse response) {
        WarnMsg warnMsg = getWarnMsg(request);
        if(warnMsg!=null){
            logger.info(" 微信告警消息",warnMsg,WebUtils.getIpAddr(request));
            try {
                sendSuccessToWX(response);
            } catch (Exception e){
               logger.warn("告警通知success响应失败",warnMsg,WebUtils.getIpAddr(request));
            }
        }
    }
    /**
     * 获取微信后台推送的 告警消息
     * 备注：后期做美丽湾维权系统时，请注意做签名校验，以确认是微信后台推送的消息
     * @param request
     * @return
     */
    private WarnMsg getWarnMsg(HttpServletRequest request){
        WarnMsg warnMsg = null;
        try{
            ServletInputStream in = request.getInputStream();
            XStream xs = new XStream(new DomDriver());
            xs.alias("xml", WarnMsg.class);
            StringBuilder xmlMsg = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1;) {
                xmlMsg.append(new String(b, 0, n, "UTF-8"));
            }
            warnMsg = (WarnMsg) xs.fromXML(xmlMsg.toString());
        }catch(Exception e){
            Map errorMap = new HashMap();
            errorMap.put("errorMsg","获取告警通知消息异常");
            logger.error(e,errorMap, WebUtils.getIpAddr(request));
        }
        return warnMsg;
    }

    /**
     * 商户收到告警通知后，需要成功返回 success
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
