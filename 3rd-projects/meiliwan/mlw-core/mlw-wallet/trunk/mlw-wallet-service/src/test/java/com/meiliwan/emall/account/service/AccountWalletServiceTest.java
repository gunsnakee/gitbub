package com.meiliwan.emall.account.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.BaseTest;
import com.meiliwan.emall.account.bean.WalletDto;
import com.meiliwan.emall.commons.bean.PayCode;

import org.testng.annotations.Test;

import java.util.Random;

/**
 * Created by wenlepeng on 13-8-19.
 */
public class AccountWalletServiceTest extends BaseTest {

//    @Test
    public void testSubMoney(){
        System.out.println("----------------- subMoney ------------------");
//        accountWalletService.subMoney(new JsonObject(),5,0.01,"123456","133365565658");
    }

//    @Test
    public void testRefund(){
        System.out.println("---------------------refund--------------------");
        accountWalletService.updateRefund(new JsonObject(),5,"092533525",1, PayCode.MLW_W);
    }

//    @Test
    public void testFreezePayRollback(){
       String orderId = "xyzhs"+new Random().nextInt(100000);
       WalletDto walletDto = new WalletDto();
       walletDto.setOrderId(orderId);
       walletDto.setMoney(15);
       walletDto.setuId(15);
       accountWalletService.updateFreezeMoney(new JsonObject(),walletDto);
       accountWalletService.payMoney(new JsonObject(),walletDto);
       walletDto.setOrderId(orderId+"-"+orderId+"01");
       accountWalletService.updateRefundFromGateway(new JsonObject(),walletDto);
    }

//    @Test
    public void testById(){
        accountWalletService.getAccountWalletByUid(new JsonObject(),11);
    }
    
//    @Test
//    public void testAutoWalletCheck(){
//    	walletCheckLogsService.autoWalletCheck() ;
//    }

}
