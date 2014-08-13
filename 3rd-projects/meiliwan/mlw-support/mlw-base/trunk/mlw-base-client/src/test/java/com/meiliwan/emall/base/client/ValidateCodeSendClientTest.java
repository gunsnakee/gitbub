package com.meiliwan.emall.base.client;

import org.testng.annotations.Test;

import com.meiliwan.emall.base.dto.MsgTemplete;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiawuwu on 13-8-26.
 */
public class ValidateCodeSendClientTest  {
    @Test
    public void testMoblieValidSend() throws Exception {
        System.out.println(">>>" + ValidateCodeSendClient.moblieValidSend("12345678", "18176876269", 3, "jiawu.wu"));
    }

    @Test
    public void testMoblieValidSendByMap() throws Exception {
        Map<String,String> paraMap = new HashMap<String,String>() ;
        paraMap.put("sessionId","12345678");
        paraMap.put("uid","3");
        paraMap.put("mobile","18176876269");
        paraMap.put("code","3556676");
        paraMap.put("nickName","jiawu.wu");

        MsgTemplete msgTemplete = MsgTemplete.MOBILE_VALIDATE_CODE;

        System.out.println(">>>"+ValidateCodeSendClient.moblieValidSendByMap(paraMap,msgTemplete));

    }

    @Test
    public void testEmailValidSend() throws Exception {

    }

    @Test
    public void testCreateCodeStr() throws Exception {
       System.out.println(">>>"+ValidateCodeSendClient.createCodeStr("12345678",8));
    }

    @Test
    public void testCreateCodeNum() throws Exception {
       System.out.println(">>>"+ValidateCodeSendClient.createCodeNum("12345678",5));
    }

    @Test
    public void testEmailValidUrlSendInPaypassword() throws Exception {

    }
}
