package com.meiliwan.emall.monitor.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.bean.Ip2city;
import com.meiliwan.emall.monitor.dao.Ip2cityDao;
import com.meiliwan.emall.monitor.util.IP2Long;

public class Ip2cityDaoTest extends MonitorBaseTest{

	@Autowired
	private Ip2cityDao ip2cityDao;
	
	@Test
	public void getEntityByObj(){
		String ip="113.16.175.58";
		long longIp=0l;
			longIp = IP2Long.ip2long(ip);
		System.out.println(longIp);
		Ip2city ip2city = new Ip2city();
		ip2city.setStartIp(longIp);
		ip2city.setEndIp(longIp);
		Ip2city result = ip2cityDao.getEntityByObj(ip2city);
		System.out.println(result);
	}
	
	
}
