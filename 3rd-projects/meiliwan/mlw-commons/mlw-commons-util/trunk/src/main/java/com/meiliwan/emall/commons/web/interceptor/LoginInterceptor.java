package com.meiliwan.emall.commons.web.interceptor;

import com.meiliwan.emall.commons.constant.user.UserCommon;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.UserCookieUtil;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆验证拦截器
 * Created by jiawu.wu on 13-6-28.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());


    public void setHandle(String handle) {
        this.handle = handle;
    }

    private String handle;




    private boolean checkLogin(HttpServletRequest request, HttpServletResponse response){
        Integer uid = null;
        uid = UserLoginUtil.getLoginUid(request, response);
        if (uid == null) {
            request.setAttribute(UserCommon.IS_LOGINED, UserCommon.FALSE);
            UserCookieUtil.updateNickName(request, response, "");
            WebUtils.deleteCookie(request, response, "/", "_ml");
            return false;
        } else {
            request.setAttribute(UserCommon.IS_LOGINED, UserCommon.TRUE);
            request.setAttribute(UserCommon.UID, uid);
            return true;
        }
    }


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if("post".equals(handle)){
            return true;
        }

        if(checkLogin(request,response)){
            return true;
        }else{
            String p = request.getQueryString();
            p = (p == null || p.length() == 0) ? "?g="+System.currentTimeMillis() : "?" + p+"&g="+System.currentTimeMillis();
            String targetUrl =  request.getRequestURL() + p;

            boolean idxTg = "gateway.meiliwan.com".equals(request.getServerName()) || ("passport.meiliwan.com".equals(request.getServerName()) && "/user/reg/success".equals(request.getRequestURI())) ;

            if(idxTg){
                targetUrl="http://www.meiliwan.com?g="+System.currentTimeMillis();
            }
            response.sendRedirect(response.encodeRedirectURL(UserCommon.LOGIN_URL_VALUE + "?targetUrl=" +targetUrl));
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        if("post".equals(handle)){
            if (!checkLogin(request, response)) {
                response.sendRedirect(UserCommon.LOGIN_URL_VALUE + "?targetUrl=http://www.meiliwan.com?g="+System.currentTimeMillis());
                return;
            }
        }


    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }


}
