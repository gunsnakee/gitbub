package com.meiliwan.emall.commons.web.interceptor;

import com.meiliwan.emall.commons.constant.GlobalNames;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.validate.FormConstans;
import com.meiliwan.emall.commons.web.validate.ValidateFormUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuxiong on 13-6-22.
 */
public class SubmitInterceptor extends HandlerInterceptorAdapter{

    private String friendlyPage;
    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(SubmitInterceptor.class);


    public String getFriendlyPage() {
        return friendlyPage;
    }

    public void setFriendlyPage(String friendlyPage) {
        this.friendlyPage = friendlyPage;
    }

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,Object handler)throws Exception{

        boolean isValidSubmit = ValidateFormUtil.validateRepeatSubmit(request, response);

        String ajax = request.getParameter(FormConstans.REQUEST_AJAX);
        LOG.debug("url check result : {}" , isValidSubmit);
        //如果是ajax提交， 暂时默认true吧
        if(!StringUtil.checkNull(ajax) && ajax.equals(GlobalNames.IS_AJAX)){
            isValidSubmit = true;
        }

        //验证当前form操作合法性
        if (!isValidSubmit) {
//            String noPassUrl = request.getParameter(FormConstans.REQUEST_VALI_NOPASS_URL);
//            if(!StringUtil.checkNull(noPassUrl)){
//              //  response.sendRedirect(friendlyPage+ URLEncoder.encode(noPassUrl, "UTF-8"));
//               // request.getRequestDispatcher("/friendlymsg/error.ftl").forward(request, response);
//                response.sendRedirect(friendlyPage + URLEncoder.encode(noPassUrl, "UTF-8"));
//            }
            //先临时给它默认跳到网站首页去吧 modify by yuxiong 2013-10-18
            if(StringUtil.checkNull(friendlyPage)) {
                friendlyPage =  "http://www.meiliwan.com";
            }
            response.sendRedirect(friendlyPage);
            return false;
        }

        //如果是一个ajax请求，则重新构造一个新的token
        if(!StringUtil.checkNull(ajax) && ajax.equals(GlobalNames.IS_AJAX)){
            String uuid = ValidateFormUtil.getNoRepeatSubmitCode(request, response, request.getParameter(FormConstans.REQUEST_CURR_FORMID));
            request.setAttribute(FormConstans.REQUEST_FORM_TOKEN, uuid);
        }
        return true;
    }

}
