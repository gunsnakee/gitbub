package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.BaseTest;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiawuwu on 13-8-23.
 */
public class MobileMsgUtilShanghTest extends BaseTest {

    @Test
    public void testSend() throws Exception {
       MobileMsgUtilShangh.send("亲爱的 15177776304： 您的新密码是65391184,登录后建议您到【我的美丽湾-账户安全】中修改登录密码，谢谢您对美丽湾的支持！","18176876269",null);
       System.out.println("--------->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
