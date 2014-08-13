package com.meiliwan.emall.base.dao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.base.BaseTest;
import com.meiliwan.emall.base.bean.AreaPayKey;
import com.meiliwan.emall.base.bean.BaseTransportAreaPay;
import com.meiliwan.emall.base.constant.Constants;

public class BaseTransportAreaPayDaoTest extends BaseTest {
	
	@Autowired
	private BaseTransportAreaPayDao baseTransportAreaPayDao;
	
	@Test
	public void testAdd(){
		
		BaseTransportAreaPay bean = new BaseTransportAreaPay();
		bean.setAreaCode("55");
		bean.setPrice(new BigDecimal(10.05));
		bean.setTransCompanyId(1);
		bean.setCashOnDelivery(1);
		bean.setPredictTimeMin(2);
		bean.setPredictTimeMax(1000);
		baseTransportAreaPayDao.insert(bean);
	}
	
	@Test
	public void testUpdate(){
		
		BaseTransportAreaPay bean = new BaseTransportAreaPay();
		bean.setAreaCode("55");
		bean.setPrice(new BigDecimal(10.05));
		bean.setTransCompanyId(1);
		bean.setCashOnDelivery(0);
		bean.setFullFreeSupport(new BigDecimal(10.05));
		bean.setPredictTimeMin(0);
		bean.setPredictTimeMax(1000);
		baseTransportAreaPayDao.update(bean);
	}
	
	@Test
	public void testUpdateForAreaCodes(){
		
		Set<String> set = new HashSet<String>();
		set.add("1000110112");
		set.add("1000110113");
		set.add("1000110114");
		set.add("1000110115");
		BaseTransportAreaPay pay = new BaseTransportAreaPay();
		pay.setPredictTimeMax(10);
		pay.setCashOnDelivery(0);
		pay.setFreeSupport(0);
		pay.setFullFreeSupport(new BigDecimal(10));
		pay.setPredictTimeMin(5);
		pay.setPrice(new BigDecimal(999));
		pay.setTransCompanyId(Constants.DEFAULT_TRANS_ID);
		baseTransportAreaPayDao.updateByAreaCodeBatch(pay, set);
	}
	
	@Test
	public void testDelete(){
		AreaPayKey pk = new AreaPayKey();
		pk.setAreaCode("55");
		pk.setTransCompanyId(1);
		baseTransportAreaPayDao.delete(pk);
	}
	
	@Test
	public void testGetIdExists(){
		String[] ids1={"1","2","3","4"};
		List<String> ids = Arrays.asList(ids1);
		
		List<BaseTransportAreaPay> list = baseTransportAreaPayDao.getAreaIdExists(999,ids);
		logger.info("result:"+list);
	}
	
}