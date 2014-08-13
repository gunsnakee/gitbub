package com.meiliwan.emall.mms.client;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.util.RandomUtil;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.bean.UserPassportPara;
import com.meiliwan.emall.mms.bean.UserPassportSimple;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by jiawuwu on 13-8-26.
 */
public class UserPassportClientTest {

    private Random random = new Random();


    @DataProvider
    public Object[][] getUids(Method testMethod, ITestContext context) {
        int rel = 1;
        Object[][] resulet = new Object[rel][];
        for(int i=0;i<rel;i++){
            int leng = random.nextInt(20)+10;
            Integer[] uids = new Integer[leng];
            for(int j=0;j<leng;j++){
                uids[j]=random.nextInt(5000000)+100000000;
            }
            resulet[i] = new Object[]{uids};
        }
        return  resulet;
    }

    @DataProvider
    public Object[][] getUserNames(Method testMethod, ITestContext context) {
        PageInfo page = new PageInfo();
        page.setPagesize(100);
        PagerControl<UserPassport> passportList = UserPassportClient.getPassportList(new UserPassport(), page);
        List<UserPassport> users = passportList.getEntityList();
        int user_leng = users.size();
        int rel = user_leng+100;
        Object[][] resulet = new Object[rel][];
        for(int i=0;i<user_leng;i++){
            resulet[i] = new Object[]{users.get(i).getUserName()};
        }
        for(int i=user_leng;i<rel;i++){
            resulet[i] = new Object[]{RandomUtil.randomStrCode(10)+"@qq.com"};
        }
        return  resulet;
    }

    @DataProvider
    public Object[][] getNickNames(Method testMethod, ITestContext context) {
        PageInfo page = new PageInfo();
        page.setPagesize(100);
        PagerControl<UserPassport> passportList = UserPassportClient.getPassportList(new UserPassport(), page);
        List<UserPassport> users = passportList.getEntityList();
        int user_leng = users.size();
        int rel = user_leng+100;
        Object[][] resulet = new Object[rel][];
        for(int i=0;i<user_leng;i++){
            resulet[i] = new Object[]{users.get(i).getNickName()};
        }
        for(int i=user_leng;i<rel;i++){
            resulet[i] = new Object[]{RandomUtil.randomStrCode(10)};
        }
        return  resulet;
    }

    @DataProvider
    public Object[][] getMobiles(Method testMethod, ITestContext context) {
        PageInfo page = new PageInfo();
        page.setPagesize(100);
        PagerControl<UserPassport> passportList = UserPassportClient.getPassportList(new UserPassport(), page);
        List<UserPassport> users = passportList.getEntityList();
        int user_leng = users.size();
        int rel = user_leng+100;
        Object[][] resulet = new Object[rel][];
        for(int i=0;i<user_leng;i++){
            resulet[i] = new Object[]{users.get(i).getMobile()};
        }
        for(int i=user_leng;i<rel;i++){
            resulet[i] = new Object[]{RandomUtil.randomNumCode(11)};
        }
        return  resulet;
    }

    @DataProvider
    public Object[][] getEmails(Method testMethod, ITestContext context) {
        PageInfo page = new PageInfo();
        page.setPagesize(100);
        PagerControl<UserPassport> passportList = UserPassportClient.getPassportList(new UserPassport(), page);
        List<UserPassport> users = passportList.getEntityList();
        int user_leng = users.size();
        int rel = user_leng+100;
        Object[][] resulet = new Object[rel][];
        for(int i=0;i<user_leng;i++){
            resulet[i] = new Object[]{users.get(i).getEmail()};
        }
        for(int i=user_leng;i<rel;i++){
            resulet[i] = new Object[]{RandomUtil.randomNumCode(11)+"@11.com"};
        }
        return  resulet;
    }



    @Test
    public void testSave() throws Exception {
        UserPassport user = new UserPassport();
        String email = RandomUtil.randomStrCode(16) + "@mlw.com";
        user.setEmail(email);
        user.setNickName(email);
        user.setUserName("test_" + RandomUtil.randomNumCode(10));
        System.out.println(">>>" + UserPassportClient.save(user));
    }

    public void testUpdate() throws Exception {
        UserPassport user = new UserPassport();
        user.setUid(105000008);
        user.setNickName("testUpdate");
        user.setEmailActive((short) 1);
        System.out.println(">>>" + UserPassportClient.update(user));
    }

    public void testUpdateBatch() throws Exception {


        UserPassport u1 = new UserPassport();
        UserPassport u2 = new UserPassport();

        UserPassport[] users = new UserPassport[2];
        users[0] = u1;
        users[1] = u2;

        u1.setUid(1);
        u1.setNickName("ha");

        u2.setUid(2);
        u2.setUserName("22");

        System.out.println(UserPassportClient.updateBatch(users));

    }

    public void testSoftdel() throws Exception {
        System.out.println(">>>>>" + UserPassportClient.softdel(0));
    }

    public void testGetPassport() throws Exception {
        //UserPassport user = UserPassportClient.getPassport(null,null);
    }

    @Test(dataProvider="getUids" ,invocationCount = 500, threadPoolSize = 10)
    public void testGetPassportByUids(Integer[] uids) throws Exception {
        List<UserPassport> passportByUids = UserPassportClient.getPassportByUids(uids);
    }

    @Test(dataProvider="getUids",invocationCount = 1, threadPoolSize = 1)
    public void testGetPassportMapByUids(Integer[] uids) throws Exception {
        Map<Integer, UserPassport> passportMapByUids = UserPassportClient.getPassportMapByUids(uids);
    }

    @Test
    public void testGetPassportSimpleByUids() throws Exception {
        Integer[] uids = {1,2,3};
        List<UserPassportSimple> passportByUids = UserPassportClient.getPassportSimpleByUids(uids);
    }

    @Test
    public void testGetPassportSimpleMapByUids() throws Exception {
        Integer[] uids = {1,2,3};
        Map<Integer, UserPassportSimple> passportSimpleMapByUids = UserPassportClient.getPassportSimpleMapByUids(uids);
    }

    @Test
    public void testGetPassportByUidFromDb() throws Exception {
        UserPassport passportByUidFromDb = UserPassportClient.getPassportByUidFromDb(random.nextInt(150000000));
    }


    @Test(dataProvider="getUserNames",invocationCount = 500, threadPoolSize = 10)
    public void testGetPassportByUserName(String userName) throws Exception {
        UserPassport passportByUserName = UserPassportClient.getPassportByUserName(userName);
    }

    @Test(dataProvider="getNickNames",invocationCount = 500, threadPoolSize = 10)
    public void testGetPassportByNickName(String nickName) throws Exception {
        UserPassport test = UserPassportClient.getPassportByNickName(nickName);
    }

    @Test(dataProvider="getMobiles",invocationCount = 500, threadPoolSize = 10)
    public void testGetPassportByMobile(String mobile) throws Exception {
        UserPassport passportByMobile = UserPassportClient.getPassportByMobile(mobile);
    }

    @Test(dataProvider="getEmails",invocationCount = 5000, threadPoolSize = 200)
    public void testGetPassportByEmail(String email) throws Exception {
        UserPassport passportByEmail = UserPassportClient.getPassportByEmail(email);
    }

    @Test
    public void testGetPassportByEmail() throws Exception {
        UserPassport passportByEmail = UserPassportClient.getPassportByEmail(null);
    }

    @Test(invocationCount = 500, threadPoolSize = 10)
    public void testGetPassportList() throws Exception {
        PagerControl<UserPassport> passportList = UserPassportClient.getPassportList(null, new PageInfo());
    }

    public void testUpdateKeepUnique() throws Exception {
        UserPassport user = new UserPassport();
        user.setUid(3);
        user.setMobile("18176876269");
        HashMap<String, Object> stringObjectHashMap = UserPassportClient.updateKeepUnique(user);
    }

    @Test(invocationCount = 500, threadPoolSize = 10)
    public void testGetSearchList() throws Exception {
        PagerControl<UserPassportPara> searchList = UserPassportClient.getSearchList(null, new PageInfo());
    }
    @Test
    public void testGetRandomShamUser(){
        UserPassport u = UserPassportClient.getRandomShamUser();
        System.out.println(u!=null?u.getUid():"");
    }


}
