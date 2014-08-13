package com.meiliwan.emall.commons.web;


import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

public class WebAppContextUtils
{
  public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = RequestContext.class.getName() + ".CONTEXT";

  public static WebApplicationContext getWebApplicationContext(HttpServletRequest request) {
    WebApplicationContext webApplicationContext = (WebApplicationContext)request.getAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    if (webApplicationContext == null) {
      webApplicationContext = RequestContextUtils.getWebApplicationContext(request, request.getSession().getServletContext());
    }
    return webApplicationContext;
  }

  public static Locale getRequestLocale(HttpServletRequest request)
  {
    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
    Locale tempLocale = null;
    if (localeResolver != null)
    {
      tempLocale = localeResolver.resolveLocale(request);
    }
    return tempLocale;
  }
}
