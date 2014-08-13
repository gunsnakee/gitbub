package com.meiliwan.emall.union.service;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.sf.SFOrder;
import com.sf.integration.expressservice.service.IService;

@Service
public class SFService {

	private final static MLWLogger logger = MLWLoggerFactory.getLogger(SFService.class);
	
	private static String security;
	private static String webServiceUrl;
	private static String custId;
	static{
		try {
			ConfigOnZk  configOnZk  = ConfigOnZk.getInstance();
			security = configOnZk.getValue("union-web/system.properties", "checkword.SF");
			webServiceUrl = configOnZk.getValue("union-web/system.properties", "webServiceUrl.SF");
			custId = configOnZk.getValue("union-web/system.properties", "custId.SF");
			
		} catch (BaseException e) {
			logger.error(e, security+webServiceUrl, null);
		}
		
	}
	
	public String bindOrderAndMailNo(String orderid,String mailno) {
		 JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
        svr.setServiceClass(IService.class);
        svr.setAddress(webServiceUrl);
        logger.debug(webServiceUrl);
        logger.debug(orderid+","+mailno);
        IService sfservice = (IService) svr.create();
        SFOrder order = new SFOrder(orderid,mailno);
        order.setCheckword(security);
        order.setCustCode(custId);
        String msg=order.toXml();
        logger.debug(msg);
        String result = sfservice.sfexpressService(msg);
        logger.debug(result);
        return result;
	}
	
	public static void main(String[] args) {
		SFService service  = new SFService();
		service.bindOrderAndMailNo("000004259962", "23421231412");
	}
}
