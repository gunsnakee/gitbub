package com.meiliwan.emall.union.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;

import com.meiliwan.emall.commons.bean.ErrorPageCode;
import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.WebRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.CookieSessionUtil;
import com.meiliwan.emall.commons.web.UserCookieUtil;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserForeign;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserForeignClient;
import com.meiliwan.emall.mms.client.UserPassportClient;

public class LoginHelper {
	
	private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(LoginHelper.class);
	
	 /**
     * 获取当前用户uid
     *
     * @param request
     * @return
     */
    public static Integer getLoginUid(HttpServletRequest request, HttpServletResponse response){
        return UserPassportClient.getLoginUid(request, response);
    }
	
    /**
     * 检查登录 ,如果未登录，则返回登录登录页面URL
     * @param model
     * @param request
     */
    public static String loginCheck(Model model, HttpServletRequest request, HttpServletResponse response) {
        Integer uid = getLoginUid(request, response);
        if (uid == null) {
            model.addAttribute(WebConstant.SUCCESS_FLAG, false);
            model.addAttribute(WebConstant.COMMON_MSG, "未登录");
            return WebConstant.LOGIN_URL_PAGE;
        }
        
        UserForeign userForeign = UserForeignClient.getForeignByUid(uid, "qq");
        if (userForeign == null) {
        	model.addAttribute("not_association", uid);
			return "not_association";
		}
        return null;
    }
    
    /**
     * 记录登录数据
     *
     * @param response
     * @param loginType 登录类型
     * @param loginName 登录名称
     * @return
     */
    public static boolean writeLoginCookie(HttpServletRequest request, HttpServletResponse response, String loginType, String loginName, UserPassport loginedUser) {

        if(loginedUser!=null){
           UserCookieUtil.writeLoginCookie(request,response,loginType,loginName,loginedUser.getNickName(),loginedUser.getUid());
        }

        return true;
    }
    
    /**
     * 登录后的统一动作，更新登录时间，  写cookie, 写缓存，
     *
     * @param request
     * @param response
     * @param loginpassport
     * @return
     */
    public static String loginAfter(HttpServletRequest request,
                                    HttpServletResponse response, UserPassport loginpassport) {

        // 登录类型，登录名称，token串
        String loginType = WebConstant.LoginType.foreignLogin.toString();
        String loginName = (String) request.getAttribute(WebConstant.LOGIN_NAME);

      

        if (loginpassport == null) {
            return null;
        }

        // 更新登录时间
        UserPassport updateBean = new UserPassport();
        updateBean.setUid(loginpassport.getUid());
        updateBean.setLoginTime(new Date());
        UserPassportClient.update(updateBean);

        // 写Session
        try {
            //注意这里用的是 newSession ,登陆成功重新刷新cookieID和sessionID
            CookieSessionUtil.newSession(request, response).setUidAndNickname(loginpassport.getUid().toString(),loginpassport.getNickName()+"");
            UserLoginUtil.updateUserState(loginpassport.getUid(),loginpassport.getState()+"");
        } catch (JedisClientException e) {
            LOGGER.error(e,"用户登录后写缓存发生异常.user:"+loginpassport,WebUtils.getIpAddr(request));
            throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }

        writeLoginCookie(request,response,loginType,loginName,loginpassport);
        return WebConstant.SUCCESS_FLAG;
    }

}
