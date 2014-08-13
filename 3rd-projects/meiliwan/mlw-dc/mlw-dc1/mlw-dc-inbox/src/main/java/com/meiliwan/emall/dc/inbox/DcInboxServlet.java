package com.meiliwan.emall.dc.inbox;


import com.meiliwan.emall.dc.inbox.util.WebUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class DcInboxServlet extends HttpServlet {

	private final static Logger log= Logger.getLogger(DcInboxServlet.class);
   
	TransferWork twork=null;
	
	public void init() throws ServletException {
		log.info("DcInbox init...");
		twork=new TransferWork();
		twork.start();
		log.info("DcInbox init finished");
	}

    public void doGet(HttpServletRequest request,
                  HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
        throws IOException, ServletException {
    	process(request, response);
    }
    	

    protected void process(HttpServletRequest request,
                       HttpServletResponse response)throws IOException, ServletException {    	
    	try{    		
    		//检验是否是来自3g的pv,ip数据 ,因为3g的数据需要通过urlEncode.encode->UTF-8,此处需要decode
    		//并且ip的值来自3gip
	    	boolean is3gRequest = false;
    		String url= WebUtil.getStringPara(request, "url");
    		if(url.indexOf('?')>0){
    			String chkUrl = url.substring(0, url.indexOf('?'));
    			if(chkUrl.contains("3g.meiliwan.com")){
    				is3gRequest = true;
    			}
    		}else{
    			if(url.contains("3g.meiliwan.com")){
    				is3gRequest = true;
    			}
    		}
    		if(is3gRequest){
    			return;
    		}
    		
	    	String ref=WebUtil.getStringPara(request, "ref");
	    	String lan=WebUtil.getStringPara(request, "lan");
	    	String screen=WebUtil.getStringPara(request, "screen");
	    	int uid=WebUtil.getIntPara(request, "uid");
	    	long uc=WebUtil.getLongPara(request,"uc");    	
	    	
	    	String ip = WebUtil.getStringPara(request, "ip");
	    	if(ip==null||ip.trim().isEmpty()){
	    		ip=request.getHeader("X-Real-IP");
		    	if(ip==null){
		    		ip=request.getHeader("X-Forwarded-For");
		    		if(ip==null){
		    			ip = request.getRemoteAddr();
		    			if(ip==null){
		    				ip="127.0.0.1";
		    			}
		    		}
		    	}	 
	    	}	    		    		   
	    	//c1,c2 -- 汉字 统一需要解码
	    	String c1 = URLDecoder.decode(WebUtil.getStringPara(request, "c1"),"UTF-8");
	    	String c2 = URLDecoder.decode(WebUtil.getStringPara(request, "c2"),"UTF-8");

	    	
	    	if(uid==0){
	    		//TODO 转换成自己的id
	    	}
	    	
	    	
	    	StringBuffer sb=new StringBuffer();
	    	sb.append("ip=").append(ip).append("&")
	    	  .append("url=").append(URLEncoder.encode(url,"UTF-8")).append("&")
	    	  .append("c1=").append(URLEncoder.encode(c1,"UTF-8")).append("&")
	    	  .append("c2=").append(URLEncoder.encode(c2,"UTF-8")).append("&")
	    	  .append("ref=").append(URLEncoder.encode(ref,"UTF-8")).append("&")
	    	  .append("uc=").append(uc).append("&")
	    	  .append("uid=").append(uid).append("&")
	    	  .append("time=").append(System.currentTimeMillis()+"")	    	  
	    	  ;
	    	
	    	log.info("DcInboxServlet["+sb.toString()+"]");
	    	DcInboxData.addData(sb.toString());
	    		
    	}
    	catch(Exception exp){
    		log.error("pv inbox collect msg error",exp);
    	}
    }
}
