package com.meiliwan.emall.commons.web.interceptor;

import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.web.CookieSessionUtil;
import com.meiliwan.emall.commons.web.UserCookieUtil;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 校验session 的有效性
 *
 */
public class SessionCheckInterceptor extends HandlerInterceptorAdapter {

//    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());


    public void setHandle(String handle) {
        this.handle = handle;
    }

    private String handle;

    private boolean isSessionValidate(HttpServletRequest request, HttpServletResponse response){
        int uid = WebConstant.UID_NULL;
        try {
            uid = CookieSessionUtil.getSession(request, response).getUid();
        } catch (JedisClientException e) {
            e.printStackTrace();
        }
        if(uid != WebConstant.UID_NULL)return true;
        return false;
    }


    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
       if(!isSessionValidate(request,response)){
           WebUtils.deleteCookie(request, response, "/", "_ml");
       }
       return true;

    }


}
