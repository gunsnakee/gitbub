package com.meiliwan.emall.base.client;

import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;

import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-8-7
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
 public class BaseMailAndMobileClientTest {



    //@Test
    public void testSendMail() throws JedisClientException {
        System.out.println("--"+this.getClass().getSimpleName());
    }

    @Test
    public void testSendMobile() throws JedisClientException {
        String mobile = "18290067035";
        String msg = "测试";
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("abc","12133344");
        String cacheId = "123";
        JedisKey jedisKey = JedisKey.mms$mobile;
        boolean b = BaseMailAndMobileClient.sendMobile(mobile, msg, cacheMap, cacheId, jedisKey);

    }

    //@Test
    public void testSendMailFindPwd() throws JedisClientException {

       /* Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("nickName","happy baby");
        dataMap.put("pwd","232432432");
        dataMap.put("lastUrl","http://www.meiliwan.com");
*/

        MailEntity mailEntity = new MailEntity();
        mailEntity.setTitle("美丽湾").setReceivers("446529842@qq.com").setContent("drtert");

       /* Map<String,Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put("uid","3");
        cacheMap.put("email","314976779@qq.com");

        String cacheId = "123456";*/

        BaseMailAndMobileClient.sendMail(mailEntity);

        //System.out.println("~~ "+ShardJedisTool.getInstance().hgetAll(JedisKey.mms$emailRSPwd,cacheId));


    }


    //@Test
    public void testSendMailVI() throws JedisClientException {

        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("nickName","happy baby");
        dataMap.put("lastUrl","http://www.meiliwan.com");


        MailEntity mailEntity = new MailEntity();
        mailEntity.setTitle("美丽湾").setReceivers("314976779@qq.com");

        Map<String,Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put("uid","3");
        cacheMap.put("email","314976779@qq.com");

        String cacheId = "123456";

        BaseMailAndMobileClient.sendMailVI(dataMap,mailEntity,cacheMap,cacheId,JedisKey.mms$emailRSPwd);

        System.out.println("~~ "+ShardJedisTool.getInstance().hgetAll(JedisKey.mms$emailRSPwd,cacheId));


    }


    //@Test
    public void testSendMailChange() throws JedisClientException {

        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("nickName","happy baby");
        dataMap.put("lastUrl","http://www.meiliwan.com");


        MailEntity mailEntity = new MailEntity();
        mailEntity.setTitle("美丽湾").setReceivers("jiawu.wu@opi-corp.com");

        Map<String,Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put("uid","3");
        cacheMap.put("email","jiawu.wu@opi-corp.com");

        String cacheId = "123456";

        BaseMailAndMobileClient.sendMailChange(dataMap,mailEntity,cacheMap,cacheId,JedisKey.mms$emailRSPwd);

        System.out.println("~~ "+ShardJedisTool.getInstance().hgetAll(JedisKey.mms$emailRSPwd,cacheId));


    }


    //@Test
    public void testSendMailPayPwd() throws JedisClientException {

        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("nickName","happy baby");
        dataMap.put("lastUrl","http://www.meiliwan.com");


        MailEntity mailEntity = new MailEntity();
        mailEntity.setTitle("美丽湾").setReceivers("527576701@qq.com,314976779@qq.com");

        Map<String,Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put("uid","3");
        cacheMap.put("email","527576701@qq.com,314976779@qq.com");

        String cacheId = "123456";

        BaseMailAndMobileClient.sendMailPayPwd(dataMap,mailEntity,cacheMap,cacheId,JedisKey.mms$emailRSPwd);

        System.out.println("~~ "+ShardJedisTool.getInstance().hgetAll(JedisKey.mms$emailRSPwd,cacheId));


    }
    
    @Test
    public void sendMailWithAttachment(){
    	MailEntity mailEntity = new MailEntity();
    	
    	mailEntity.setTitle("attachment test");
    	mailEntity.setReceivers("446529842@qq.com");
    	mailEntity.setSenderName("yuzhe");
    	mailEntity.setContent("asdfadfadfasdf");
    	
    	Map<String, Object> cacheMap = new HashMap<String, Object>();
    	
    	cacheMap.put("uid", "446529842");
    	String cacheId = "123456";
    	
    	BaseMailAndMobileClient.sendMail(mailEntity, cacheMap, cacheId, JedisKey.mms$email, false, "/home/yuzhe/qqconnect.log");
    }
    

    
}
