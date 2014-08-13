package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.imeiliwan.comments.msgstrategy.ReceiveMsgtpyeEnum;
import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;
import com.meiliwan.emall.imeiliwan.util.VerifyUtils;
import com.meiliwan.emall.imeiliwan.util.WXSafeTool;
import com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.IMsgtypeStrategy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.lang.StringUtils;
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
 * Date: 13-11-29
 * Time: 上午11:11
 * 接收微信推送控制器
 */

@Controller
@RequestMapping("/wx")
public class ReceiveController {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(ReceiveController.class);


    /**
     * 接收微信服务器推送消息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/rcv")
    public String rcv(HttpServletRequest request, HttpServletResponse response){
        //一、消息有消息验证
        //不是微信的请求将不做任何处理
        if(!WXSafeTool.isWxRequest(request, getToken())){
            logger.warn("非法请求：非微信服务访问微信接收消息接口！","",WebUtils.getIpAddr(request));
            return  null;
        }

        String echostr = request.getParameter("echostr");
        //判断是普通消息推送 or  接收微信服务器消息接收URL验证
        if(!StringUtils.isNotBlank(echostr)){
            //根据消息类型出来响应
            doReceiveMsg(request,response);
        } else{
            //接收微信服务器推送URL (目前用于验证url消息)
            doRcvUrlCheck(request,response);
        }
        return null;
    }

    /**
     * 根据消息类型出来响应
     * @param request
     * @param response
     */
    public void doReceiveMsg(HttpServletRequest request, HttpServletResponse response){
        // 根据消息类型出来响应
        IMsgtypeStrategy strategye = null;
        try {
            BaseMsgReceive msgReceive = getBaseMsgReceive(request);
            if(msgReceive!=null && StringUtils.isNotBlank(msgReceive.getMsgType())){
                //1、根据消息类型得到 策略实例。
                ReceiveMsgtpyeEnum msgtpyeEnum = ReceiveMsgtpyeEnum.getReceiveMsgtpyeEnum(msgReceive.getMsgType());
                Class aClass = Class.forName(msgtpyeEnum.getStrategyClass());
                strategye = (IMsgtypeStrategy)aClass.newInstance();
                //2、执行对应策略
                strategye.operate(request,response,msgReceive);
            } else{
                //todo。。。。。未知类型消息处理 ,调用固定消息回复用户
            }
        } catch (Exception  e) {
            //todo.....处理消息响应异常时，调用固定的消息回复用户
        }
    }

    /**
     * 接收微信服务器推送URL (目前用于验证url消息)
     * URL：是开发者用来接收微信服务器数据的接口URL
     *
     * @param request
     * @param response
     * @return
     */
    public void doRcvUrlCheck(HttpServletRequest request,
                        HttpServletResponse response) {
        //获取微信服务器发送给我们的四个参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String  token = getToken();
       System.out.println( "token校验进来ip："+WebUtils.getIpAddr(request));

        //验证URL有效性
        if(StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp)
                && StringUtils.isNotBlank(nonce) && StringUtils.isNotBlank(echostr) && StringUtils.isNotBlank(token)){
            try {
                //创建一个出处流, 用于向微信服务器发送数据
                PrintWriter out = response.getWriter();
                //如果校验通过, 向微信服务器发送echostr参数
                if(VerifyUtils.checkSignature(token, signature, timestamp, nonce)){
                    out.print(echostr);
                    logger.info("接收消息url验证：向微信服务器发送echostr参数",echostr,WebUtils.getIpAddr(request));
                }
                //释放资源
                out.close();
                out = null;
            } catch (Exception e){
                Map errorMap = new HashMap();
                errorMap.put("errorMsg","接收消息url验证：向微信服务器发送数据失败！");
                errorMap.put("signature",signature);
                errorMap.put("timestamp",timestamp);
                errorMap.put("nonce",nonce);
                errorMap.put("echostr",echostr);
                logger.error(e,errorMap, WebUtils.getIpAddr(request));
            }
        }
    }



    /**
     * 从zk获取token
     * @return
     */
    private String getToken(){
        String token = WXSafeTool.getWxParam("weixin.token");
        return  token;
    }

    /**
     * 获取微信推送消息
     * @param request
     * @return
     */
    private  BaseMsgReceive getBaseMsgReceive(HttpServletRequest request){
        BaseMsgReceive msgReceive = null;
        try{
            ServletInputStream in = request.getInputStream();
            XStream xs = new XStream(new DomDriver());
            xs.alias("xml", BaseMsgReceive.class);
            StringBuilder xmlMsg = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1;) {
                xmlMsg.append(new String(b, 0, n, "UTF-8"));
            }
            msgReceive = (BaseMsgReceive) xs.fromXML(xmlMsg.toString());
        }catch(IOException e){
            Map errorMap = new HashMap();
            errorMap.put("errorMsg","获取消息异常");
            logger.error(e,errorMap, WebUtils.getIpAddr(request));
        }
        return msgReceive;
    }


}
