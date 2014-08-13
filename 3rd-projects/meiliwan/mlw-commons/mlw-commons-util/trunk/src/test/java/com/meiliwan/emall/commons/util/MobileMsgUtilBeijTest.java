package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.BaseTest;
import org.testng.annotations.Test;
import sun.misc.BASE64Encoder;

/**
 * Created by jiawuwu on 13-8-22.
 */
public class MobileMsgUtilBeijTest extends BaseTest {
    @Test
    public void testSend() throws Exception {
       MobileMsgUtilBeij.send("13978862367",null, null, "haha", "123");
    }

    @Test
    public void testBase64(){
        BASE64Encoder base64=new BASE64Encoder();
        System.out.println(base64.encode("mlcs2013".getBytes()));
    }
}
