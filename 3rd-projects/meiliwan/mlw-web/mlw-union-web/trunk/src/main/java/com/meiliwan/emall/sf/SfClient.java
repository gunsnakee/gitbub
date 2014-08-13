package com.meiliwan.emall.sf;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.sf.integration.expressservice.service.IService;

public class SfClient {

	public static void main(String[] args) {
		 JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
         svr.setServiceClass(IService.class);
         //测试
         //svr.setAddress("http://219.134.187.132:9090/bsp-ois/ws/expressService");
         //线上
         //svr.setAddress("http://bsp-oisp.sf-express.com/bsp-oisp/ws/expressService");
         IService sfservice = (IService) svr.create();
         
         String msg=getXml();
         String result = sfservice.sfexpressService(msg);
         System.out.println(result);
	}
	
	public static String getXml(){
		SFOrder order = new SFOrder("000000000004","903114021930");
		System.out.println(order.toXml());
		return order.toXml();
				
	}
}
