package com.meiliwan.search.common;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;

import com.meiliwan.search.util.HttpResponseUtil;
import com.meiliwan.search.util.http.Handler;
import com.meiliwan.search.util.http.NettyHttpRequest;

public class StatusHandler implements Handler{
//	static ESLogger log = Loggers.getLogger(StatusHandler.class);
	RequestProcessor core ;
	
	public StatusHandler(RequestProcessor core){
		this.core = core;
	}
	

	public String getPath() {
		// TODO Auto-generated method stub
		return "status";
	}

	public void handle(NettyHttpRequest req, DefaultHttpResponse resp) {
		HttpResponseUtil.setHttpResponseOkReturn(resp, core.showStatus());
	}

}
