package com.meiliwan.emall.account.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.BaseTest;
import com.meiliwan.emall.account.bean.AccountCard;
import com.meiliwan.emall.account.bean.CardOptRecord;
import com.meiliwan.emall.account.dao.AccountCardDao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;


public class AccountCardServiceTest extends BaseTest {
	
	@Autowired
	protected AccountCardDao accountCardDao;
	
//	@Test
	public void testAdd(){
		AccountCard card = new AccountCard();
		card.setUid(105005046);
		accountCardService.add(new JsonObject(), card);
	}
	
//	@Test
	public void testUpdate(){
		AccountCard card = accountCardDao.getEntityById(105005046);
		card.setCardCoin(BigDecimal.valueOf(1234.33));
		card.setFreezeAmount(BigDecimal.valueOf(0.33));
		card.setStatus((short)1);
		accountCardService.update(new JsonObject(), card);
	}

	
//	@Test
	public void testAddOpt(){
		CardOptRecord cardOpt = new CardOptRecord() ;
		cardOpt.setInnerNum("123456");
		cardOpt.setCardNum("1110001110001100");
		cardOpt.setUid(105005046);
		cardOpt.setOptType("SUB_MONEY");
		cardOpt.setOrderId("1515151515");
		cardOpt.setSource("卡激活");
		cardOpt.setClientIp("10.249.15.227");
		cardOpt.setMoney(BigDecimal.valueOf(12.11));
		cardOpt.setAdminId(123);
		accountCardService.addOptRecord(new JsonObject(), cardOpt);
	}
	
	@Test
	public void testAddMoney(){
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 11.23, "卡激活", "XXXXXX1", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 12.23, "卡激活", "XXXXXX2", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 13.23, "卡激活", "XXXXXX3", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 14.23, "卡激活", "XXXXXX4", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 15.23, "卡激活", "XXXXXX5", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 11.23, "卡激活", "XXXXXX6", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 12.23, "卡激活", "XXXXXX7", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 13.23, "卡激活", "XXXXXX8", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 14.23, "卡激活", "XXXXXX9", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 15.23, "卡激活", "XXXXXX10", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 11.23, "卡激活", "XXXXXX11", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 12.23, "卡激活", "XXXXXX12", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 13.23, "卡激活", "XXXXXX13", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 14.23, "卡激活", "XXXXXX14", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 15.23, "卡激活", "XXXXXX15", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 11.23, "卡激活", "XXXXXX16", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 12.23, "卡激活", "XXXXXX17", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 13.23, "卡激活", "XXXXXX18", "10.249.15.227");
//		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 14.23, "卡激活", "XXXXXX19", "10.249.15.227");
		
		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 11.23, "卡激活", "XXXXXX26", "10.249.15.227");
		accountCardService.addMoneyWithIp(new JsonObject(), 105005046, 12.23, "卡激活", "XXXXXX27", "10.249.15.227");
		
	}
	
//	@Test
	public void testRefundFree(){
		accountCardService.updateRefundFromFreezeWithIp(new JsonObject(),"000004258562","10.249.15.227",1234) ;
	}
	
}
