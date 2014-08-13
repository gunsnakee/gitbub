//package com.meiliwan.emall.monitor.web;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
//import com.meiliwan.emall.base.constant.Constants;
//import com.meiliwan.emall.commons.exception.BaseException;
//import com.meiliwan.emall.commons.log.MLWLogger;
//import com.meiliwan.emall.commons.log.MLWLoggerFactory;
//import com.meiliwan.emall.commons.util.ConfigOnZk;
//import com.meiliwan.emall.commons.web.ResponseUtil;
//import com.meiliwan.emall.commons.web.WebUtils;
//
///**
// * 短信发送http接口
// *
// * User: jiawuwu
// * Date: 13-11-12
// * Time: 下午4:24
// * To change this template use File | Settings | File Templates.
// */
//@Controller
//public class MessageController {
//
//    private static final MLWLogger logger = MLWLoggerFactory.getLogger(MessageController.class);
//
//
//    @RequestMapping(value = "/sendMsg")
//    public void sendMsg(HttpServletRequest request, Model model, HttpServletResponse response) {
//
//        String clientIP = WebUtils.getIpAddr(request);
//
//        String ck = null;
//        try {
//            ck = ConfigOnZk.getInstance().getValue(Constants.MSG_WHITELIST,clientIP);
//        } catch (BaseException e) {
//            logger.error(e, "ConfigOnZk.getInstance().getValue(PROPERTIES_ZK,clientIP): {PROPERTIES_ZK:" + Constants.MSG_WHITELIST + ",clientIP:" + clientIP + "}", clientIP);
//            model.addAttribute("success",false);
//            model.addAttribute("para","{clientIP:"+clientIP+",PROPERTIES_ZK:"+Constants.MSG_WHITELIST+"}");
//            model.addAttribute("error",e.getMessage());
//            ResponseUtil.writeJsonByObj(model.asMap(), request, response);
//            return;
//        }
//
//        if(!"true".equals(ck)){
//            model.addAttribute("success",false);
//            model.addAttribute("error","您的IP："+clientIP+" 不在白名单");
//            ResponseUtil.writeJsonByObj(model.asMap(), request, response);
//            return;
//        }
//
//
//
//        String mobiles = request.getParameter("mobiles");
//        String content = request.getParameter("content");
//
//        boolean result = false;
//        try {
//        	result = BaseMailAndMobileClient.sendMobile(mobiles, content);
//        } catch (Exception e) {
//            logger.error(e, "MessageUtils.sendMessage(mobiles,content): {mobiles:" + mobiles + ",content:" + content + "}", clientIP);
//            model.addAttribute("success",false);
//            model.addAttribute("para","{mobiles:"+mobiles+",content:"+content+"}");
//            model.addAttribute("error",e.getMessage());
//            ResponseUtil.writeJsonByObj(model.asMap(), request, response);
//            return;
//        }
//
//        if(result){
//            model.addAttribute("success",true);
//        }else{
//            model.addAttribute("success",false);
//            model.addAttribute("para","{mobiles:"+mobiles+",content:"+content+"}");
//            model.addAttribute("resultCode",result);
//            model.addAttribute("successCode",Constants.MOBILE_SUCESS_CODE);
//        }
//
//        ResponseUtil.writeJsonByObj(model.asMap(), request, response);
//    }
//
//
//}
