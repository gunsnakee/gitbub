/**
 *
 */
package com.meiliwan.emall.stock.base;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

/**
 * 单元测试基类，spring配置文件为stock-integration-test.xml，该文件包含现有所有的bean配置文件， 可根据自己的需要去修改bean的配置文件。
 * 
 */
@ContextConfiguration(locations = { "classpath:conf/spring/stock-integration-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class BaseTest extends AbstractTestNGSpringContextTests {

    
}
