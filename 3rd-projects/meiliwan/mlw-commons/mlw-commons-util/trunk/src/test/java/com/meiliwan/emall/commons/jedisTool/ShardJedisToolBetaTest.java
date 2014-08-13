package com.meiliwan.emall.commons.jedisTool;

import com.meiliwan.emall.commons.BaseTest;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RandomCode;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-9-28
 * Time: 下午12:51
 * To change this template use File | Settings | File Templates.
 */
public class ShardJedisToolBetaTest extends BaseTest{

    private MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    private Random random = new Random();



    @DataProvider
    public Object[][] getKeys(Method testMethod, ITestContext context) {
        int size = 100000;
        String prex = System.currentTimeMillis()+"_";
        Object[][] resulet = new Object[size][];
        for(int i=0;i<size;i++){
            resulet[i] = new Object[]{prex+random.nextInt(50)};
        }

        return  resulet;
    }




    @Test
    public void testConfigUtil() throws Exception {
        /*ShardJedisToolBeta.ConfigUtil.initList("/redis/cacheGroupInfo");
        String nodeName = "r192.168.1.1:8080";
        ServerInfo s = ShardJedisToolBeta.ConfigUtil.transTo(nodeName);
        ShardJedisToolBeta.ConfigUtil.addNoteToList(nodeName,s);
        ShardJedisToolBeta.ConfigUtil.removeNodeFromList(nodeName);*/
    }

    @Test
    public void testWatch() throws Exception {
       /* ShardJedisTool.getInstance().watchNode("/redis/cacheGroupInfo","r10.249.15.194:6379");*/
    }


    @Test(dataProvider="getKeys")
    public void testSet(String key) throws Exception {
        ShardJedisTool.getInstance().set(JedisKey.vu$test,key,"0");
    }

    @Test(dataProvider="getKeys")
    public void testHset(String key) throws Exception {
        int name = random.nextInt(5);
        String value = RandomCode.randomStrCode(5);
        logger.debug(" =====> name="+name+" value="+value);
        ShardJedisTool.getInstance().hset(JedisKey.vu$hash, key, name + "", value);
        name = random.nextInt(5);
        value = RandomCode.randomStrCode(5);
        logger.debug(" =====> name="+name+" value="+value);
        ShardJedisTool.getInstance().hset(JedisKey.vu$hash,key,name+"", value);
        name = random.nextInt(5);
        value = RandomCode.randomStrCode(5);
        logger.debug(" =====> name="+name+" value="+value);
        ShardJedisTool.getInstance().hset(JedisKey.vu$hash,key,name+"", value);
    }

    @Test(dataProvider="getKeys")
    public void testGet(String key) throws Exception {
        logger.debug(" ============> " + ShardJedisTool.getInstance().get(JedisKey.vu$test, key));
    }

    @Test(dataProvider="getKeys")
    public void testDel(String key) throws Exception {
        logger.debug(" ============> " + ShardJedisTool.getInstance().del(JedisKey.vu$test, key));
    }


    @Test(dataProvider="getKeys")
    public void randomTest(String key) throws Exception {
        int i = random.nextInt(2);
        if(i==0){
            logger.debug(" ============> " + ShardJedisTool.getInstance().set(JedisKey.vu$test, key, "0"));
        }else if(i==1){
            logger.debug(" ============> " + ShardJedisTool.getInstance().get(JedisKey.vu$test, key));
        }else if(i==2){
            //logger.debug(" ============> " + ShardJedisTool.getInstance().del(JedisKey.v$test, key));
        }

    }

    public static  void main(String[] args) throws Exception {
        int size = 100;
        String prx = System.currentTimeMillis()+"";
        for(int i=0;i<size;i++){
            System.out.println("------ "+i+" -----");
            ShardJedisTool.getInstance().set(JedisKey.vu$test,prx+"_"+i,"0");
            Thread.sleep(2000);
        }
        System.out.println("------finish-----");
    }


}
