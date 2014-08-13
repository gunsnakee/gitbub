package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.mms.BaseTest;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.bean.UserPassportPara;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;

/**
 * Created by jiawuwu on 13-8-16.
 */
public class UserPassportServiceTest extends BaseTest{

    @Autowired
    public UserPassportService userPassportService;
    
    @Autowired
    public UserStationMsgService userStationMsgService;
    
    // 演示测试代码
    @Test(invocationCount = 100, threadPoolSize = 20)
   public void testPassportService(){
    	JsonObject resultObj = new JsonObject();
    	//userStationMsgService.getStationMsg(resultObj);
    }
    
    
    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {
        JsonObject resultObj = new JsonObject();
        UserPassport entity = new UserPassport();
        entity.setUid(3);
        entity.setState((short)1);
        userPassportService.update(resultObj,entity);
    }

    @Test
    public void testUpdateBatch() throws Exception {
        JsonObject resultObj = new JsonObject();
        UserPassport[] entitys = new UserPassport[2];

        UserPassport entity1 = new UserPassport();
        entity1.setUid(3);
        entity1.setState((short)1);

        UserPassport entity2 = new UserPassport();
        entity2.setUid(4);
        entity2.setState((short)1);

        entitys[0]= entity1;
        entitys[1]= entity2;

        userPassportService.updateBatch(resultObj,entitys);

    }

    @Test
    public void testDelete() throws Exception {
        JsonObject resultObj = new JsonObject();
        Integer uid = 3;
        userPassportService.delete(resultObj,uid);
    }

    @Test
    public void testGetPassport() throws Exception {
        JsonObject resultObj = new JsonObject();
        String userinput="18176876269";
        userPassportService.getPassport(resultObj,userinput);
    }

    @Test
    public void testGetPassportByUids() throws Exception {
        JsonObject resultObj = new JsonObject();
        Integer[] uids = {1,2,3,4};
        userPassportService.getPassportByUids(resultObj,uids);
    }


    @Test
    public void getPassportSimpleByUids() throws Exception {
        JsonObject resultObj = new JsonObject();
        Integer[] uids = new Integer[101];
        userPassportService.getPassportSimpleByUids(resultObj,uids);
    }

    @Test
    public void testGetPassportByUid() throws Exception {
        JsonObject resultObj = new JsonObject();
        Integer uid = 1;
        userPassportService.getPassportByUid(resultObj,uid);
    }

    @Test
    public void testGetPassportByEmailOrMobile() throws Exception {
        JsonObject resultObj = new JsonObject();
        String emailOrMobile = "314976779@qq.com";
        userPassportService.getPassportByEmailOrMobile(resultObj,emailOrMobile);
    }

    @Test
    public void testGetPassportByNickName() throws Exception {
        JsonObject resultObj = new JsonObject();
        String nickName = "test";
        userPassportService.getPassportByNickName(resultObj,nickName);
    }

    @Test
    public void testGetPassportByUserName() throws Exception {
        JsonObject resultObj = new JsonObject();
        String userName = "mlw_0000000001";
        userPassportService.getPassportByUserName(resultObj,userName);
    }

    @Test
    public void testGetPassportByMobile() throws Exception {
        JsonObject resultObj = new JsonObject();
        String mobile = "18176876269";
        userPassportService.getPassportByMobile(resultObj,mobile);
    }

    @Test
    public void testGetPassportByEmail() throws Exception {
        JsonObject resultObj = new JsonObject();
        String email = "314976779@qq.com";
        userPassportService.getPassportByEmail(resultObj, email);
    }

    @Test
    public void testGetPassportList() throws Exception {
        JsonObject resultObj = new JsonObject();

        UserPassport entity = new UserPassport();
        entity.setState((short)1);
        entity.setEmail("3149");
        entity.setEmailActive((short)1);
        entity.setMobile("181");
        entity.setMobileActive((short)1);
        entity.setNickName("哈哈");
        entity.setUserName("mlw");


        PageInfo pageInfo = new PageInfo();
        pageInfo.setPagesize(20);
        pageInfo.setStartIndex(0);

        userPassportService.getPassportList(resultObj,entity,pageInfo);

    }

    @Test
    public void testUpdateKeepUnique() throws Exception {
        JsonObject resultObj = new JsonObject();
        UserPassport user = new UserPassport();
        user.setUid(3);
        //user.setNickName("888");
        //user.setMobile("18176876269");
        user.setEmail("314976779@qq.com");
        userPassportService.updateKeepUnique(resultObj,user);
    }

    @Test
    public void testGetSearchList() throws Exception {
    	long t1 = System.currentTimeMillis();
    	
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd") ;
        JsonObject resultObj = new JsonObject();
        UserPassportPara entity = new  UserPassportPara();
        entity.setStateSearch((short)-1);;
//        entity.setUserNameSearch("mlw");
//        entity.setNickNameSearch("2");
//        entity.setEmail("1");
//        entity.setMobile("18");
//        entity.setCreateTimeBegin(df.parse("2013-01-01"));
//        entity.setCreateTimeEnd(df.parse("2013-12-30"));
//        entity.setBirthdayBegin(df.parse("1988-08-08"));
//        entity.setBirthdayEnd(df.parse("2010-01-01"));

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPagesize(20);
        pageInfo.setStartIndex(0);

        userPassportService.getSearchList(resultObj,entity,pageInfo);

        long t2 = System.currentTimeMillis();
        
        System.out.println("cost times : " + (t2 - t1));
    }
    
    @Test
    public void testGetSeqId(){
       JsonObject resultObj = new JsonObject();
       userPassportService.getSeqId(resultObj);
    }

    @Test
    public void testGetRandomShamUser() throws JedisClientException {
        JsonObject resultObj = new JsonObject();
        userPassportService.getRandomShamUser(resultObj);
    }
}
