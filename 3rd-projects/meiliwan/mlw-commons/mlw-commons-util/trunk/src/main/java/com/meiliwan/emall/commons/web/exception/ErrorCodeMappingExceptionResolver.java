package com.meiliwan.emall.commons.web.exception;


import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.exception.UserPassportException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.commons.web.exception.util.WebAppContextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.RedirectView;

public class ErrorCodeMappingExceptionResolver extends SimpleMappingExceptionResolver
{
  private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
  private Properties exceptionMappings;
  private Properties errorCodeMappings;
  private String defaultErrorView;
  private boolean redirect=false;
  private static final String PAGE_LOGIN = "https://passport.meiliwan.com/user/login";

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
  {
      BaseRuntimeException bex = null;

      //如果抛出的异常是用户未登录的异常
      if (ex instanceof UserPassportException) {
          logger.error(ex, request.getParameterMap(), WebUtils.getIpAddr(request));
          //如果抛出的异常是用户未登录的异常，则url redirect跳转去前台登录页
          return new ModelAndView(new RedirectView(PAGE_LOGIN));
      }else if (!(ex instanceof BaseRuntimeException)) {
        logger.error(ex, request.getParameterMap(), WebUtils.getIpAddr(request));
        bex = new BaseRuntimeException(ex);
     }else {
        bex = (BaseRuntimeException)ex;
     }

    String viewName = determineViewName(bex, request);
    if (viewName != null)
    {
      Integer statusCode = determineStatusCode(request, viewName);
      if (statusCode != null) {
        applyStatusCodeIfPossible(request, response, statusCode.intValue());
      }
        if(redirect){
            //request.g
            String fromUrl = WebUtils.getCurrentURL(request);
            String url = viewName;
            StringBuffer sb = new StringBuffer();
            if(!StringUtil.checkNull(bex.getCode())){
                sb.append("code=").append(bex.getCode());
            }
            if(!StringUtil.checkNull(fromUrl)){
                if(!StringUtil.checkNull(sb.toString())) {
                    sb.append("&");
                }
                try {
                    sb.append("fromUrl=").append(URLEncoder.encode(fromUrl, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Map map = new HashMap();
                    map.put("fromUrl", fromUrl);
                    map.put("code", bex.getCode());
                    logger.error(e, map, WebUtils.getIpAddr(request));
                }
            }
            String param = sb.toString();
            url = url + (!StringUtil.checkNull(param)?"?"+param:"");
            return new ModelAndView(new RedirectView(url));
        }
      return getModelAndView(viewName, bex, request);
    }

    return null;
  }

  protected String determineViewName(Exception ex, HttpServletRequest request)
  {
    String viewName = null;
    String codeViewName = null;
    String classViewName = null;

    if (this.exceptionMappings != null) {
      classViewName = findMatchingViewName(this.exceptionMappings, ex);
    }

    if (this.errorCodeMappings != null) {
      codeViewName = findMatchingViewNameByErrorCode(this.errorCodeMappings, ex);
    }

    if (null != classViewName)
      viewName = classViewName;
    else if (null != codeViewName) {
      viewName = codeViewName;
    }

    if ((viewName == null) && (this.defaultErrorView != null)) {
      viewName = this.defaultErrorView;
    }

    fillFriendlyExceptionMessage(ex, request);

    return viewName;
  }

  protected int getDepth(String exceptionMapping, Exception ex)
  {
    Exception cause = ex;
    if ((ex instanceof BaseRuntimeException)) {
      cause = (Exception)((BaseRuntimeException)ex).getMostSpecificCause();
    }
    return super.getDepth(exceptionMapping, cause);
  }

  protected String findMatchingViewNameByErrorCode(Properties errorCodeMappings, Exception ex)
  {
    if ((null == ex) || (!(ex instanceof BaseRuntimeException))) {
      return null;
    }

    String errorCode = ((BaseRuntimeException)ex).getCode();
    if (null == errorCode) {
      return null;
    }

    for (Enumeration names = errorCodeMappings.propertyNames(); names.hasMoreElements(); ) {
      String codes = (String)names.nextElement();
      String viewName = errorCodeMappings.getProperty(codes);

      String[] codeArray = codes.split(",");
      for (String code : codeArray) {
        if (code.trim().equalsIgnoreCase(errorCode.trim())) {
          return viewName;
        }
      }
    }
    return null;
  }

  protected void fillFriendlyExceptionMessage(Exception ex, HttpServletRequest request)
  {
    Locale locale = LocaleContextHolder.getLocale();
    BaseRuntimeException bex = null;
    if ((ex instanceof BaseRuntimeException))
      bex = (BaseRuntimeException)ex;
    else {
      bex = new BaseRuntimeException(ex);
    }

    String friendlyExceptionMsg = getFriendlyExceptionMessage(bex, locale, request);
    bex.setFriendlyMessage(friendlyExceptionMsg);
  }

  protected String getFriendlyExceptionMessage(BaseRuntimeException ex, Locale locale, HttpServletRequest request)
  {
    WebApplicationContext webAppContext = WebAppContextUtils.getWebApplicationContext(request);

    String defaultGlobalMessage = webAppContext.getMessage("exception.defaultMessage", null, "", locale);
    String message = defaultGlobalMessage;

    String code = ex.getCode();
    if (StringUtils.hasText(code)) {
      Object[] args = ex.getMessageArgs();
      String userDefaultMessage = ex.getDefaultFriendlyMessage();
      
      if (StringUtils.hasText(userDefaultMessage))
        message = webAppContext.getMessage(code, args, userDefaultMessage, locale);
      else {
        message = webAppContext.getMessage(code, args, defaultGlobalMessage, locale);
      }

    }
    else
    {
      Throwable tr = ex.getMostSpecificCause();
      String exClassName = tr.getClass().getName();
      message = webAppContext.getMessage(exClassName, null, defaultGlobalMessage, locale);
    }
    return message;
  }

  public void setErrorCodeMappings(Properties errorCodeMappings)
  {
    this.errorCodeMappings = errorCodeMappings;
  }

  public void setExceptionMappings(Properties exceptionMappings) {
    this.exceptionMappings = exceptionMappings;
  }

  public void setDefaultErrorView(String defaultErrorView) {
    this.defaultErrorView = defaultErrorView;
  }
}