package com.meiliwan.emall.commons.web;

import com.meiliwan.emall.commons.bean.ErrorPageCode;
import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.exception.WebRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 用户cookie工具类
 * @author jiawu.wu
 *
 */
public class UserCookieUtil {

    private static  final MLWLogger LOGGER = MLWLoggerFactory.getLogger(UserCookieUtil.class);
    private static  final String MU="_mu";

    private UserCookieUtil(){

    }

    /**
     * 登陆后写cookie
     *
     * @param response
     * @param loginType 登录类型
     * @param loginName 登录名称
     * @return
     */
    public static boolean writeLoginCookie(HttpServletRequest request, HttpServletResponse response, String loginType, String loginName, String nickName,Integer uid) {


        loginType = loginType==null?"":loginType;
        loginName = loginName==null?"":loginName;
        nickName = nickName==null?"":nickName;

        boolean isNewUser = StringUtils.isNotBlank(request.getParameter("RegisterName")) || request.getAttribute("reg") != null;
        WebUtils.setCookieValue(response,"_ml", UserLoginUtil.uidToMl(uid, isNewUser),"/",WebConstant.MLW_DOMAIN,-1);
        
        WebUtils.deleteCookie(request, response, "/", "_mlf");
        if(request.getAttribute("_mlf") != null){
        	WebUtils.setCookieValue(response,"_mlf", request.getAttribute("_mlf").toString(),"/",WebConstant.MLW_DOMAIN,-1);
        }

        String[] cs = new String[]{nickName,loginName,loginType};

        String mu= StringUtils.join(cs,"|");


        try {
            mu = URLEncoder.encode(mu, WebConstant.CHARSET_ENCODE);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e, e.getMessage() + " mu:" + mu, WebUtils.getIpAddr(request));
            throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }

        mu = mu.replaceAll("\\+","%20");

        try {
            WebUtils.setCookieValue(response,MU, mu,"/",WebConstant.MLW_DOMAIN,WebConstant.MU_TIIME);
        }catch (Exception e){
            LOGGER.error(e, e.getMessage() + " mu:" + mu, WebUtils.getIpAddr(request));
            throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }
        
        return true;
    }

    /**
     * 更新用户信息时，同时更新cookie中的昵称
     * @param request
     * @param response
     * @param nickName
     */
    public static void updateNickName(HttpServletRequest request, HttpServletResponse response, String nickName) {
        nickName = nickName==null?"":nickName;
        String mu = WebUtils.getCookieValue(request,MU);
        if(StringUtils.isNotBlank(mu)){
            String newVal = "";
            try {
                mu = URLDecoder.decode(mu,WebConstant.CHARSET_ENCODE);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e, e.getMessage() + " mu:" + mu, WebUtils.getIpAddr(request));
                throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
            }
            String[] muArr = mu.split("\\|",-1);
            muArr[0]=nickName;
            newVal = StringUtils.join(muArr, "|");
            try {
                newVal = URLEncoder.encode(newVal, WebConstant.CHARSET_ENCODE);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e, e.getMessage() + " newVal:" + newVal, WebUtils.getIpAddr(request));
                throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
            }
            try {
                if(StringUtils.isNotBlank(newVal)){
                    newVal = newVal.replaceAll("\\+","%20");
                    WebUtils.setCookieValue(response, MU, newVal, "/", WebConstant.MLW_DOMAIN, WebConstant.MU_TIIME);
                }
            }catch (Exception e){
                LOGGER.error(e, e.getMessage() + " newVal:" + newVal, WebUtils.getIpAddr(request));
                throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
            }
        }
    }

		/**
		 * 从cookie获取用户昵称
		 * @param request
		 * @return
		 */
		public static String getNickName(HttpServletRequest request) {
            String mu =  WebUtils.getCookieValue(request, MU);
            String nickName = "";
            if(StringUtils.isNotBlank(mu)){
                try {
                    mu = URLDecoder.decode(mu,WebConstant.CHARSET_ENCODE);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e, e.getMessage() + " mu:" + mu, WebUtils.getIpAddr(request));
                    throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
                }
                String[] arr = mu.split("\\|",-1);
                nickName = arr[0];

            }

          return nickName;
		}
		/**
		 * 从cookie中获取登录名称
		 * @param request
		 * @return
		 */
		public static String getLoginName(HttpServletRequest request){
            String mu =  WebUtils.getCookieValue(request, MU);
            String loginName = "";
            if(StringUtils.isNotBlank(mu)){
                try {
                    mu = URLDecoder.decode(mu,WebConstant.CHARSET_ENCODE);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e, e.getMessage() + " mu:" + mu, WebUtils.getIpAddr(request));
                    throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
                }
                String[] arr = mu.split("\\|",-1);
                if(arr.length>1){
                    loginName = arr[1];
                }
            }
            return  loginName;
		}
		/**
		 * 从cookie中获取登录类型
		 * @param request
		 * @return
		 */
		public static String getLoginType(HttpServletRequest request){
            String mu =  WebUtils.getCookieValue(request, MU);
            String loginType = "";
            if(StringUtils.isNotBlank(mu)){
                try {
                    mu = URLDecoder.decode(mu,WebConstant.CHARSET_ENCODE);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e, e.getMessage() + " mu:" + mu, WebUtils.getIpAddr(request));
                    throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
                }
                String[] arr = mu.split("\\|",-1);
                if(arr.length>2){
                    loginType = arr[2];
                }
            }
            return  loginType;
		}


}
