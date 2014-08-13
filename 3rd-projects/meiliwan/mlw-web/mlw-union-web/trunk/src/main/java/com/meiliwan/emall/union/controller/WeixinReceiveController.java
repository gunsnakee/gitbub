package com.meiliwan.emall.union.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.weixin.VerifyUtils;

/**
 * 接收微信服务器推送URL controller类
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 14-3-25
 * Time: 下午5:27
 *
 */


@Controller
@RequestMapping("/wx")
public class WeixinReceiveController {

	private final static MLWLogger logger = MLWLoggerFactory.getLogger(WeixinReceiveController.class);
	

	
	/**
	 * 接收微信服务器推送URL
     * URL：是开发者用来接收微信服务器数据的接口URL
     *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/rcv")
	public void receive(HttpServletRequest request,
			HttpServletResponse response) {
        //获取微信服务器发送给我们的四个参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String  token = getToken();

        //验证URL有效性
        if(StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp)
                && StringUtils.isNotBlank(nonce) && StringUtils.isNotBlank(echostr) && StringUtils.isNotBlank(token)){
            try {
                //创建一个出处流, 用于向微信服务器发送数据
                PrintWriter out = response.getWriter();
                //如果校验通过, 向微信服务器发送echostr参数
                if(VerifyUtils.checkSignature(token,signature, timestamp, nonce)){
                    out.print(echostr);
                }
                //释放资源
                out.close();
                out = null;
            } catch (Exception e){
                Map errorMap = new HashMap();
                errorMap.put("errorMsg","向微信服务器发送数据失败！");
                errorMap.put("signature",signature);
                errorMap.put("timestamp",timestamp);
                errorMap.put("nonce",nonce);
                errorMap.put("echostr",echostr);
                logger.error(e,errorMap,WebUtils.getIpAddr(request));
            }
        }
    }
	
	/**
	 * 微信维权
	 */
	@RequestMapping("/weiquan")
	public void weiquan(){
		
	}
	
	/**
	 * 微信订单告警
	 */
	@RequestMapping("/warn")
	public void orderWarn(){
		
	}

    /**
     * 从zk获取token
     * @return
     */
    private String getToken(){
        String token = null;
        try {
            token = ConfigOnZk.getInstance().getValue("weixin-web/system.properties","weixin.token");
        } catch (BaseException e) {
            logger.error(e,"从zk获取微信token异常！",null);
        }
        return  token;
    }
}
