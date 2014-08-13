/**
 *
 */
package com.meiliwan.emall.account;

import com.meiliwan.emall.account.dao.AccountWalletDao;
import com.meiliwan.emall.account.dao.WalletOptRecordDao;
import com.meiliwan.emall.account.service.GiftCardService;
import com.meiliwan.emall.account.service.AccountWalletService;
import com.meiliwan.emall.account.service.WalletCheckLogsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 单元测试基类，spring配置文件为oms-integration-test.xml，该文件包含现有所有的bean配置文件， 可根据自己的需要去修改bean的配置文件。
 * 
 */
@ContextConfiguration(locations = {"classpath*:conf/spring/account-service-integration.xml"})
@TransactionConfiguration(defaultRollback = false)
public class BaseTest extends AbstractTestNGSpringContextTests {
     @Autowired
     protected AccountWalletService accountWalletService;
    @Autowired
    protected AccountWalletDao accountWalletDao;
    @Autowired
    protected WalletOptRecordDao walletOptRecordDao;
    @Autowired
    protected WalletCheckLogsService walletCheckLogsService;
    @Autowired
    protected GiftCardService accountCardService;
}
