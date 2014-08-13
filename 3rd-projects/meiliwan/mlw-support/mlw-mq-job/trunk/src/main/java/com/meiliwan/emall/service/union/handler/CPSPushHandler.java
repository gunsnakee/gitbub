package com.meiliwan.emall.service.union.handler;

import com.meiliwan.emall.commons.bean.CpsOrderVO;
public interface CPSPushHandler {
	
	String buildPushParam(CpsOrderVO cpsOrder);
	
	String buildQueryInfo(CpsOrderVO cpsOrder);
	
	String buildMlwQueryInfo(CpsOrderVO cpsOrder);
	
	String getPushUrl();
	
	boolean isStoped();
}
