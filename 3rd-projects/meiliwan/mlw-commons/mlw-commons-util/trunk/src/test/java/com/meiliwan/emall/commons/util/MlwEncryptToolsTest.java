package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.BaseTest;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-23
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
public class MlwEncryptToolsTest extends BaseTest{


    @Test
    public void testEncryptLoginPwd() throws Exception {

        System.out.println("~~~ "+MlwEncryptTools.encryptLoginPwd("123456"));

    }

    @Test
    public void testEncryptAccountPwd() throws Exception {

        System.out.println("~~~ "+MlwEncryptTools.encryptAccountPwd("123456"));

    }

    @Test
    public void testEncrypt() throws Exception {
        System.out.println("~~~ "+MlwEncryptTools.encrypt("123456","md5key","shakey"));
    }


}
