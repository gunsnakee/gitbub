package com.meiliwan.emall.account.client;

import com.meiliwan.emall.account.bean.WalletDto;
import org.testng.annotations.Test;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-9-11
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public class AccountWalletClientNewTest {

    /**
     * 测试冻结，解冻流程
     */
    @Test
    public void testFreezeAndUnFreeze(){
        String orderId="test" + new Random().nextInt(100000000);
        WalletDto walletDto = new WalletDto();
        walletDto.setuId(26);
        walletDto.setOrderId(orderId);
        walletDto.setMoney(10);
        walletDto.setClientIp("127.0.1.1");
        AccountWalletClient.updateFreezeMoney(walletDto);


        //冻结回退
        //AccountWalletClient.updateRefundFromFreeze(walletDto.getOrderId());
        AccountWalletClient.updateRefundFromFreezeWithIp(walletDto.getOrderId(),"127.0.0.1",23);
    }


    /**
     * 测试冻结，消费，退款 流程
     */
   @Test
    public void testFreezePayRefund(){
       String orderId="test" + new Random().nextInt(100000000);
       WalletDto walletDto = new WalletDto();
       walletDto.setuId(26);
       walletDto.setOrderId(orderId);
       walletDto.setMoney(15);
       AccountWalletClient.updateFreezeMoney(walletDto);

       //支付
       AccountWalletClient.payMoney(walletDto);
       walletDto.setOrderId(orderId+"-"+"test"+new Random().nextInt());
       walletDto.setClientIp("192.168.1.1");
       walletDto.setAdminId(29);
       AccountWalletClient.updateRefundFromGateway(walletDto);
   }

    /**
     * 1    0.04    true
     * 2    0.00    true
     * 2    0   true
     * 5    999981.88   true
     * 11   700 true
     */

    @Test
    public void testFreezeMoney(){
        WalletDto walletDto = new WalletDto();
        walletDto.setuId(8);
        walletDto.setOrderId("0004255729");
        walletDto.setMoney(0.01);
        AccountWalletClient.updateFreezeMoney(walletDto);
    }

    @Test

    public void test(){
        WalletDto walletDto = new WalletDto();
        walletDto.setuId(105005036);
        walletDto.setOrderId("0004255823-0004255823");
        walletDto.setMoney(2789.99);

        AccountWalletClient.updateRefundFromGateway(walletDto);
    }

    @Test
    public void testBackMoney(){
        WalletDto walletDto = new WalletDto();
        walletDto.setOrderId("000004256139-000004256140");
        walletDto.setuId(105000020);
        walletDto.setMoney(15.2);
      AccountWalletClient.updateRefundFromGateway(walletDto);
    }

    @Test
    public void testIsPayPwdFreeze(){
        int result = AccountWalletClient.isPayPwdFreeze(25);
        System.out.println(result);
    }

    @Test
    public void testIsPayPwdRight(){
      // boolean flag = AccountWalletClient.isPayPasswordRight(5,"pengwenle%402139!%40%23");
      //  boolean flag = AccountWalletClient.isPayPasswordRight(10,"123456");
      // System.out.println(flag);

        System.out.print(AccountWalletClient.validatePwdAndGetMoney("pengwenle%402139!%40%23",5));
        System.out.print(AccountWalletClient.validatePwdAndGetMoney("123456",5));
    }

}
