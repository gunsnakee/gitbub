package com.meiliwan.emall.base.client;


import org.testng.annotations.Test;

import com.meiliwan.emall.base.bean.BaseTransportCompany;



/**
 * 物流公司管理客户端
 * @author yinggao.zhuo
 * Date: 13-6-3
 * Time: 下午7:08
 */
public class BaseTransportCompanyClientTest {
    
	
	@Test
	public void testAdd(){
		
		BaseTransportCompany bean = new BaseTransportCompany();
		bean.setAdminId(1100);
		bean.setIsDefault((byte) 0);
		bean.setState((short) 1);
		bean.setName("顺丰快递2");
		 BaseTransportCompanyClient.add(bean);
		
	}
	
	
	/**
	 * 根据ID查找物流
	 */
	@Test
	public void testFindById(){
		
		
		BaseTransportCompany  company = BaseTransportCompanyClient.findById(1);
		System.out.println(company);
//		logger.info(list.getName());
	}
	
	
	/**
	 * 根据条件得到总数
	 */
	@Test
	public void testUpdate(){
		BaseTransportCompany bean = new BaseTransportCompany();
		
		bean.setId(1000);
		bean.setAdminId(11000);
		bean.setIsDefault((byte) 0);
		bean.setState((short) -1);
		bean.setName("韵达快递");
		bean.setSupportCash((short) 1);
		bean.setSupportCheck((short) 1);
		bean.setSupportPos((short) 1);
		bean.setAdminId(999);
		BaseTransportCompanyClient.update(bean);
	}
	
}
