package com.meiliwan.emall.commons.web;

import com.meiliwan.emall.commons.BaseTest;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-11-6
 * Time: 下午5:58
 * To change this template use File | Settings | File Templates.
 */
public class UserLoginUtilTest extends BaseTest {

    @Test
    public void testUidToMl() throws Exception {
        Integer uid = 1234567891;
        System.out.println("~~~~"+UserLoginUtil.uidToMl(uid));

    }

    @Test
    public void testMlToUid() throws Exception {
        String ml = "252857400000000003868";
        System.out.println("~~~~"+UserLoginUtil.mlToUid(ml));
    }
}
