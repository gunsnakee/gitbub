package com.meiliwan.emall.base.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.base.BaseTest;
import com.meiliwan.emall.base.dto.TransportPriceDTO;

public class BaseTransportPriceDaoTest extends BaseTest{
	
	@Autowired
	private BaseTransportPriceDao baseTransportPriceDao;
	
	@Test
	public void deleteByAreaCodes(){
		String[] areaCodes = new String[]{
				"1111","222"
		};
		
		int i = baseTransportPriceDao.deleteByAreaCodes(areaCodes);
		System.out.println(i);
	}
	
	@Test
	public void getListByParentCode(){
		
		List<TransportPriceDTO> i = baseTransportPriceDao.getListAreaFirstByParentCode(1000+"");
		System.out.println(i);
	}
	
}
