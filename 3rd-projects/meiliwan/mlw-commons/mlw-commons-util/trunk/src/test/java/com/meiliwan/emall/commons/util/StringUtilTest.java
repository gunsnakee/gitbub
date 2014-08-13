package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.BaseTest;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-11-26
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public class StringUtilTest extends BaseTest{
    @Test
    public void testCheckPhone() throws Exception {
        String phone = "13412341234";
        System.out.println("~~~ "+StringUtil.checkPhone(phone));
    }
}
