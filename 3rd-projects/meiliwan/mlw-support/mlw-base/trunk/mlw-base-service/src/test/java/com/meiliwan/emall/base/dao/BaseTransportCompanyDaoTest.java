package com.meiliwan.emall.base.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.meiliwan.emall.base.BaseTest;
import com.meiliwan.emall.base.bean.BaseTransportCompany;
import com.meiliwan.emall.base.constant.Constants;

public class BaseTransportCompanyDaoTest extends BaseTest{
	
	@Autowired
	private BaseTransportCompanyDao baseTransportCompanyDao;
	@Test
	public void testAdd(){
		
		BaseTransportCompany bean = new BaseTransportCompany();
		bean.setAdminId(1100);
		bean.setIsDefault((byte) 0);
		bean.setName("顺丰快递2");
		bean.setSupportCash((short) 1);
		bean.setSupportPos((short) 1);
		bean.setSupportCheck((short) 1);
		int addKey = baseTransportCompanyDao.insert(bean);
		Assert.assertEquals(addKey, 1);
	}
	
	@Test
	public void testquery(){
		
		BaseTransportCompany bean = new BaseTransportCompany();
		bean.setIsDefault((byte) 1);
		String whereSql = "is_default=1";
		List<BaseTransportCompany> company = baseTransportCompanyDao.getListByObj(bean, whereSql);
		System.out.println(company);
	}
	
	@Test
	public void testGetEntityById(){
		
		BaseTransportCompany  company = baseTransportCompanyDao.getEntityById(1);
		Assert.assertNull(company);
	}
	
	@Test
	public void testGetEntityByIdNotNull(){
		
		BaseTransportCompany  company = baseTransportCompanyDao.getEntityById(Constants.DEFAULT_TRANS_ID);
		Assert.assertNotNull(company);
	}
	
	/**
	 * 根据条件得到总数
	 */
	@Test
	public void testCount(){
		BaseTransportCompany bean = new BaseTransportCompany();
		String whereSql = "is_default=1";
		int count = baseTransportCompanyDao.getCountByObj(bean, whereSql);
		System.out.println("默认物流总数:"+count);
	}
	
	/**
	 * 根据条件得到总数
	 */
	@Test
	public void testUpdate(){
		BaseTransportCompany bean = new BaseTransportCompany();
		
		bean.setId(0);
		bean.setAdminId(11000);
		bean.setIsDefault((byte) 0);
		bean.setName("韵达快递");
		
		baseTransportCompanyDao.update(bean);
	}
	
}