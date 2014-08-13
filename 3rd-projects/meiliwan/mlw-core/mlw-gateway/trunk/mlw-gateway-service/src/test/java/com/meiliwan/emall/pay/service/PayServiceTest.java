package com.meiliwan.emall.pay.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.account.bean.WalletDto;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.pay.bean.PayHead;
import com.meiliwan.emall.pay.bean.PayItem;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.constant.PayType;
import com.meiliwan.emall.pay.dao.PayItemDao;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;

public class PayServiceTest extends BaseTest{

	@Autowired
	private PayService payService;
	@Autowired
	private PayItemDao payItemDao ;
	
//	5102ms
//	@Test(invocationCount = 100, threadPoolSize = 20)
	public void testGetPayId(){
//		JsonObject resultObj = new JsonObject();
//		payService.getPayId(resultObj);
//		System.out.println(resultObj);
	}
	
//	803963ms insertPayHead-6000次 insertPayItem-12000次
//	@Test 
	public void testLog(){
		
		for(int i = 0; i < 6000; i++){
			PayHead payHead = new PayHead();
			payHead.setOrderId("000123456000"+i);
			payHead.setUid(105005046);
			payHead.setTargetUid(105005046);
			payHead.setSubject("压测商品买卖");
			payHead.setPayType(PayType.BUY_PRO.name());
			payHead.setPayStatus(PayStatus.PAY_FINISHED.name());
			payHead.setCreateTime(new Date());
			payHead.setTotalAmount(new BigDecimal(63.05));
			
			PayItem payItem1 = new PayItem();
			payItem1.setPayId(String.valueOf(i));
			payItem1.setOrderId("000123456000"+i);
			payItem1.setPayAmount(new BigDecimal(50.05));
			payItem1.setPayCode(PayCode.ALIPAY.name());
			payItem1.setPayStatus(PayStatus.PAY_FAILURE.name());
			payItem1.setThirdPayUid("105005046");
			payItem1.setThirdTradeNo("105005046");
			
			PayItem payItem2 = new PayItem();
			payItem2.setPayId(String.valueOf(i+6000));
			payItem2.setOrderId("000123456000"+i);
			payItem2.setPayAmount(new BigDecimal(13.00));
			payItem2.setPayCode(PayCode.MLW_W.name());
			payItem2.setPayStatus(PayStatus.PAY_FAILURE.name());
			payItem2.setThirdPayUid("105005046");
			payItem2.setThirdTradeNo("105005046");
			
			List<PayItem> payItems = new ArrayList<PayItem>();
			payItems.add(payItem1);
			payItems.add(payItem2);
			
			payHead.setPayItems(payItems);
			JsonObject resultObj = new JsonObject();
			PaymentDTO payment = new PaymentDTO();
			payment.setOrderId("000123456000");
			payment.setPayType(PayType.BUY_PRO);
			payment.setUid(105005046);
			payment.setTargetUid(105005046);
			payment.setTotalAmount(1.23);
			PayParam payParam1 = new PayParam() ;
			payParam1.setAmount(0.23);
			payParam1.setPayCode(PayCode.ALIPAY);
			PayParam payParam2 = new PayParam() ;
			payParam2.setAmount(1.00);
			payParam2.setPayCode(PayCode.MLW_C);
			PayParam[] payParams = {payParam1, payParam2} ;
			payment.setPayParams(payParams);
			payService.innerPay(resultObj, payment, "111111", "10.249.15.227");
//			payService.log(resultObj, payHead);
//			
//			System.out.println(resultObj);
		}
		
	}
	
//	6478ms
//	@Test(invocationCount = 100, threadPoolSize = 20)
	public void testGetPayMethodResultByPayId(){
		JsonObject resultObj = new JsonObject();
		payService.getPayHead(resultObj, "00233644", false, true);
		
		System.out.println(resultObj);
	}
	
//	6920ms
//	@Test(invocationCount = 100, threadPoolSize = 20)
	public void testGetPayLogByOrderId(){
		JsonObject resultObj = new JsonObject();
		payService.getPayHead(resultObj, "00233644", true, false);
		
		System.out.println(resultObj);
	}
	
//	5355ms
//	@Test(invocationCount = 100, threadPoolSize = 20)
	public void testGetCmbPayId(){
//		JsonObject resultObj = new JsonObject();
//		payService.getCmbPayId(resultObj);
//		
//		System.out.println(resultObj);
	}
	
//	@Test
	public void testToA(){
//		payService.backMoneyForCancel(new JsonObject(), "000004258654", "10.249.15.227", 105005046);
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String, Object> payResult = new HashMap<String, Object>() ;
		payResult.put("payment", new PaymentDTO());
		payResult.put("thirdPayCodes", PayCode.getThirdPayCodes());
		map.put("data", payResult);
		String json = new Gson().toJson(map);
		System.out.println(json);
	}
	
	@Test
	public void test(){
//		String testUrl = "actionType=action&uid=1234&uu=uustring&ff=flag&adfasdfsdfasdf%%$$cc" ;
//		String[] testStr = testUrl.split("&");
//		System.out.println(testStr.length);
//		for(String str:testStr){
//			System.out.println(str+"=="+str.length());
//			System.out.println(str.substring(0, 3));
//		}
		System.out.println(payItemDao.getPayItemByOrderId("000004259996"));
	}
	
	
}
