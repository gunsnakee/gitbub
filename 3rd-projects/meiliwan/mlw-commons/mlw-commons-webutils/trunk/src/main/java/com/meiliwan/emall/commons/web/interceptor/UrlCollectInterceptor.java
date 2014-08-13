package com.meiliwan.emall.commons.web.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.meiliwan.emall.commons.kafka.KafkaSender;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 业务数据采集 发送给kafka
 * @author lzl
 */
public class UrlCollectInterceptor extends HandlerInterceptorAdapter {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
	
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
    	return true ;
    }

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		/**获得要搜集的业务信息*/
		//客户端ip
		String clientIp = WebUtils.getIpAddr(request);
		//Sun, 03 Jan 2016 05:49:21 GMT
		String _mJid = WebUtils.getCookieValue(request, "_mJid");
		//7位随机数+11位长度的用户ID（不够时前面补0）+4位随机数
		String _ml = WebUtils.getCookieValue(request, "_ml");
		//"mlw-" + System.currentTimeMillis() +6位随机数
		String _mc = WebUtils.getCookieValue(request, "_mc");
		String collectTime = DateUtil.getCurrentDateTimeStr() ;
    	String domain = request.getHeader("host");
    	String url = request.getRequestURI() ;
    	String parameter =  request.getQueryString();
    	
    	//对多参数进行处理 只有当“parameter”存在多个参数的时候才会进行处理
    	if(!StringUtils.isBlank(parameter) && !"null".equals(parameter.trim())){
    		if(parameter.split("&")!=null && parameter.split("&").length>0){
    			String[] paramStr = parameter.split("&") ;
    			String resulStr = "" ;
        		for(String str:paramStr){
        			if(str.length() > 51){
        				str = str.substring(0, 50);
        			}
        			resulStr = resulStr + (!StringUtils.isBlank(resulStr)? "&":"") + str ;
        		}
        		parameter = resulStr ;
        		
    		}else if(parameter.length()>51){
    			parameter = parameter.substring(0, 50);
    		}
    	}
    	
    	StringBuffer sb = new StringBuffer() ;
    	sb.append("ctime[#").append(collectTime) ;
    	sb.append("#]domain[#").append(domain);
    	sb.append("#]uri[#").append(url);
    	sb.append("#]param[#").append(parameter);
    	sb.append("#]clientIp[#").append(clientIp);
    	sb.append("#]_mJid[#").append(_mJid);
    	sb.append("#]_ml[#").append(_ml);
    	sb.append("#]_mc[#").append(_mc).append("#]");
    	
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>param:"+parameter);
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>clientIp:"+clientIp);
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>_mJid:"+_mJid);
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>_ml:"+_ml);
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>_mc:"+_mc);
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>sendToKafuka:"+sb.toString());
		//发送给kafka
    	KafkaSender.sendMsg(KafkaSender.KAFKA_TOPIC, sb.toString());
    	
    	logger.info("kafka业务数据采集发送结果", "sendMsg{"+sb.toString()+"}", WebUtils.getIpAddr(request));
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}


}
