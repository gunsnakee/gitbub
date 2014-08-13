package com.meiliwan.emall.commons.util;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-15
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
public class MailUtilsTest {
    @Test
    public void testSendMail() throws Exception {

        MailUtils.sendMail("jiawu.wu@meiliwan.com","gunsnakee@sina.com","测试邮件","测试邮件","美丽湾");

    }
}
