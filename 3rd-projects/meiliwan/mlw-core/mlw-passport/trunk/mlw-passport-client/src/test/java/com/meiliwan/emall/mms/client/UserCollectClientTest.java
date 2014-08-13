package com.meiliwan.emall.mms.client;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.mms.bean.UserCollect;
import com.meiliwan.emall.mms.bean.UserPassport;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-4
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class UserCollectClientTest {

    //提供用户数据源
    @DataProvider
    public Object[][] userPassportdataProvider(Method testMethod, ITestContext context) {
        PagerControl pc = UserPassportClient.getPassportList(new UserPassport(),new PageInfo());
        List<UserPassport> uList = pc.getEntityList();
        Object[][] resulet = new Object[uList.size()][] ;
        for(int i=0; i<uList.size();i++){
            resulet[i] =  new Object[]{uList.get(i)};
        }
        return resulet;
    }

    @Test(dataProvider = "userPassportdataProvider", invocationCount = 10, threadPoolSize = 5)
    public void thisIsDataDrivenTest(UserPassport user) {
        System.out.println("userId="+user.getId());
        //System.out.println("loginName="+user.getLoginName());
        System.out.println("pd="+user.getPassword());
    }

    /**
     *  添加收藏
     * @throws Exception
     */
    @Test(invocationCount = 200, threadPoolSize = 50)
    public void saveUserCollect() throws Exception {
        UserCollect collect = new UserCollect();
       // collect.setUid(new Random().nextInt(4839350)+100000000);
        collect.setUid(new Random().nextInt(10)+100000000);
        collect.setNickName("JeckTestPersure");
        collect.setProId(new Random().nextInt(5011958)+10235100);
        collect.setProName("测试商品5000000条")   ;
        UserCollectClient.saveUserCollect(collect);
    }

    /**
     * 通过商品id 和 用户id获取用户收藏
     */
    //   @Test
   // @Test(invocationCount = 1000, threadPoolSize = 50)
    public void getUserCollectByUIdAndProId()  throws Exception{
        JsonObject json = new JsonObject();
        UserCollectClient.getUserCollectByUIdAndProId(123134435,342881320);
    }

    /**
     * 通过用户id获取最近前num条收藏
     */
   // @Test(invocationCount = 1000, threadPoolSize = 50)
    public void getListByUidAndNun()  throws Exception{
         UserCollectClient.getListByUidAndNun(1231421,5);
    }

    /**
     * 通过 用户id获取 用户收藏 list
     */
   // @Test (invocationCount = 1000, threadPoolSize = 50)
    public void getUserCollectByUId()  throws Exception{
        UserCollectClient.getUserCollectByUId(102495909);
    }
}

