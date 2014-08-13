package com.meiliwan.emall.account.client;

import java.math.BigDecimal;
import java.util.Random;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.commons.bean.PayCode;

/**
 * Created by wenlepeng on 13-8-29.
 */
public class AccountWalletClientTest {

    @Test
    public void testGetAccountWalletByUid(){
       Assert.assertNotNull(AccountWalletClient.getAccountWalletByUid(11));
        Assert.assertNull(AccountWalletClient.getAccountWalletByUid(-1));
    }


    //需要配合用户的增加一次编写测试
    @Test
    public void testAdd(){

        /*AccountWallet accountWallet = new AccountWallet();
        accountWallet.setUid(100000000);
        accountWallet.setMlwCoin(new BigDecimal(0));
        accountWallet.setState((short)1);
        Assert.assertTrue(AccountWalletClient.add(accountWallet) > 0);*/
    }

   @Test
   public void testUpdate(){

       AccountWallet wallet = AccountWalletClient.getAccountWalletByUid(11);
       BigDecimal old= wallet.getMlwCoin();

       BigDecimal   newBig =new BigDecimal(100);
       wallet.setMlwCoin(newBig);

       AccountWalletClient.update(wallet);
       wallet =AccountWalletClient.getAccountWalletByUid(11);
       Assert.assertTrue(newBig.compareTo(wallet.getMlwCoin()) == 0);

       wallet.setMlwCoin(old);
       AccountWalletClient.update(wallet);
   }


    //测试充值 和 消费
    @Test
    public void testAddMoney(){
        AccountWallet wallet = AccountWalletClient.getAccountWalletByUid(11);
        BigDecimal ordinal = wallet.getMlwCoin();
        BigDecimal result = new BigDecimal(100).add(ordinal);

        AccountWalletClient.addMoney(wallet.getUid(),100,"test"+new Random().nextInt(100000000), PayCode.CMB_ABC,null);

        wallet = AccountWalletClient.getAccountWalletByUid(11);
        Assert.assertTrue(wallet.getMlwCoin().compareTo(result) == 0);

        AccountWalletClient.subMoney("02399239",11,100,"234535","test"+ new Random().nextInt(1000000000));

        wallet = AccountWalletClient.getAccountWalletByUid(11);
        Assert.assertTrue(ordinal.compareTo(wallet.getMlwCoin()) == 0);
    }

    @Test
    public void testUpdateRefund(){
        AccountWallet wallet = AccountWalletClient.getAccountWalletByUid(11);
        BigDecimal ordinal = wallet.getMlwCoin();

        BigDecimal result = new BigDecimal(100).add(ordinal);

        AccountWalletClient.updateRefund(11,"test"+new Random().nextInt(1000000),100,PayCode.MLW_W);

        wallet = AccountWalletClient.getAccountWalletByUid(11);
        Assert.assertTrue(result.compareTo(wallet.getMlwCoin()) == 0);
    }


}
