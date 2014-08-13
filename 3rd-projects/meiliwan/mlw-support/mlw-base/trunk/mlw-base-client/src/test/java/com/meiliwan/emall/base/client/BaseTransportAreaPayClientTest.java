package com.meiliwan.emall.base.client;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.meiliwan.emall.base.bean.AreaPayKey;
import com.meiliwan.emall.base.bean.BaseTransportAreaPay;
import com.meiliwan.emall.base.capability.AbstractWorker;
import com.meiliwan.emall.base.capability.CapcityRunner;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.base.dto.TransportAreaDTO;
import com.meiliwan.emall.base.dto.TransportLimit;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;



/**
 * 物流区域服务状况,Capacity为性能测试
 * @author yinggao.zhuo
 * @date 2013-6-4
 */
public class BaseTransportAreaPayClientTest {
	
	private Random random = new Random();
	private Long [] thirdCodes = new Long[]{
			1000110105019l,
			1000110105018l,
			1000110105017l,
			1000110105016l,
			1000110105015l,
			1000110105014l,
			1000110105013l,
			1000110105012l,
			1000110105011l,
			1000110105010l,
			1000110105009l,
			1000110105008l,
			1000110105007l,
			1000110105006l,
			1000110105005l,
			1000110105004l,
			1000110105003l,
			1000110105002l,
			1000110105001l,
	};
	
	private int pCode[] = new int[]{
			10001101,
			10001102,
			10001201,
			10001202,
			10001301,
			10001302,
			10001303,
			10001304,
			10001305,
			10001306,
			10001307,
			10001308,
			10001309,
			10001310,
			10001311,
			10001401,
			10001402,
			10001403,
			10001404,
			10001405,
			10001406,
			10001407,
			10001408,
			10001409,
			10001410,
			10001411,
			10001501,
			10001502,
			10001503,
			10001504,
			10001505,
			10001506,
			10001529
	};
	


	@Test
	public void testAdd(){
		float r = new Random().nextFloat();
		BaseTransportAreaPay beanRef = new BaseTransportAreaPay();
		beanRef.setTransCompanyId(1);
		beanRef.setAreaCode("161");
		beanRef.setPrice(new BigDecimal(11.00));
		beanRef.setFreeSupport(1);
		beanRef.setFullFreeSupport(new BigDecimal(r));
		try {
			BaseTransportAreaPayClient.add(beanRef);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void isSupportCOD(){

		boolean bb = BaseTransportAreaPayClient.isSupportCOD(1000110101+"");
		System.out.println(bb);
		Assert.assertTrue(bb);
	}

	@Test
	public void testUpdate(){
		float r = new Random().nextFloat();
		BaseTransportAreaPay beanRef = new BaseTransportAreaPay();
		beanRef.setTransCompanyId(1);
		beanRef.setAreaCode("2");
		beanRef.setPrice(new BigDecimal(13.00));
		beanRef.setFreeSupport(1);
		beanRef.setFullFreeSupport(new BigDecimal(r));
		try {
			BaseTransportAreaPayClient.update(beanRef);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddOrUpdate(){
	
		float r = new Random().nextFloat();
		BaseTransportAreaPay b = new BaseTransportAreaPay();
		b.setTransCompanyId(1);
		b.setPrice(new BigDecimal(15.00));
		b.setFreeSupport(1);
		b.setFullFreeSupport(new BigDecimal(r));
		b.setCashOnDelivery(1);
		String[] ids={"1117","1111","1112"};
		BaseTransportAreaPayClient.addOrUpdate(ids, b);
	}
	
	
	@Test
	public void testPageTranAreaServe(){
		PageInfo pi = new PageInfo();
		pi.setPage(1);
		PagerControl<TransportAreaDTO>  pages = BaseTransportAreaPayClient.pageTransportAreaServe(pi);
		
		System.out.println(pages);
		
	}
	
	// base$transport`@~AreaPayKey [transCompanyId=999, areaCode=1000450102]
	@Test
	public void testCachePrice(){
	       AreaPayKey key = new AreaPayKey(999,"1000450102");
	       
	       try {
			String val = ShardJedisTool.getInstance().get(JedisKey.base$transport, key);
			System.out.println(val);
			
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGetPriceCapcity(){
		CapcityRunner runner = new CapcityRunner(20);
		
		runner.setWorker(new AbstractWorker(){

			@Override
			public void runTask() {
				// TODO Auto-generated method stub
				//new BaseTransportAreaPayClientTest().testGetPrice();
			}
		});
		runner.setRequestSize(100);
		runner.startThread();
		System.exit(1);
	}
	
	@Test
	public void testGetAreaByPid(){
		TransportAreaDTO dto = new TransportAreaDTO();
		dto.setTransCompanyId(999);
		dto.setParentCode(1000110101+"");
		List<TransportAreaDTO> list = BaseTransportAreaPayClient.getAreaByPid(dto);
		System.out.println(list);
	}
	
	@Test
	public void testGetTransportAreaByPid(){
		TransportAreaDTO dto = new TransportAreaDTO();
		dto.setTransCompanyId(999);
		dto.setParentCode(1000+"");
		dto.setState((short) 1);
		List<TransportAreaDTO> list = BaseTransportAreaPayClient.getTansportAreaByPid(dto);
		for (TransportAreaDTO transportAreaDTO : list) {
			System.out.println(transportAreaDTO.getAreaCode());
		}
		System.out.println(list);
	}
	
	@Test
	public void testGetAreaByPidCapacity(){
		
		CapcityRunner runner = new CapcityRunner(10);
		
		runner.setWorker(new AbstractWorker(){

			@Override
			public void runTask() {
				// TODO Auto-generated method stub
				new BaseTransportAreaPayClientTest().testGetAreaByPid();
			}
		});
		runner.setRequestSize(100);
		runner.startThread();
		System.exit(1);
	}
	
	
	
	
	@Test
	public void testGetAreasByParentCodeCapacity(){
		CapcityRunner runner = new CapcityRunner(10);
		
		runner.setWorker(new AbstractWorker(){

			@Override
			public void runTask() {
				// TODO Auto-generated method stub
				//new BaseTransportAreaPayClientTest().testGetObjByAraeCode();
			}
		});
		runner.setRequestSize(100);
		runner.startThread();
		System.exit(1);
	}
	
	@Test
	public void testGetTransportLimit(){
		 TransportLimit limit = BaseTransportAreaPayClient.getTransportLimit();
		 System.out.println("limit:"+limit );
		 double total = 1000;
		 if(total<limit.getPriceMax()|| total>limit.getPriceMin()){
     		System.out.println(true);
		 }else{
			 System.out.println(false);
		 }
	}
	@Test
	public void testSelectByAreaCode(){
		
		BaseTransportAreaPay pay = new BaseTransportAreaPay();
		pay.setAreaCode(1000110101+"");
		pay.setCashOnDelivery(Constants.COD_VALID);
		pay.setState(Constants.STATE_VALID);
		
		//BaseTransportAreaPayClient.selectByareaCode(pay);
		List<BaseTransportAreaPay> list = BaseTransportAreaPayClient.selectByareaCode(pay);
		System.out.println(list);
	}
	
	@Test
	public void testetDTOByAraeCode(){
		
		
		//TransportAreaDTO dto = BaseTransportAreaPayClient.getDTOByAraeCode("1000450105");
		//System.out.println(dto.toString());
	}
	
	@Test
	public void testSelectByAreaCodeCapacity(){
		
		CapcityRunner runner = new CapcityRunner(10);
		
		runner.setWorker(new AbstractWorker(){

			@Override
			public void runTask() {
				// TODO Auto-generated method stub
				new BaseTransportAreaPayClientTest().testSelectByAreaCode();
			}
		});
		runner.setRequestSize(100);
		runner.startThread();
		System.exit(1);
	}
	
	public static void main(String[] args) {
		System.out.println(new Date(1381804583456L));
	}
}
