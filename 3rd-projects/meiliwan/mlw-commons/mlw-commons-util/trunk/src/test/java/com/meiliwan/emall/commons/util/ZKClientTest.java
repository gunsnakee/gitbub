package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.BaseTest;
import org.apache.zookeeper.KeeperException;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-9-27
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
public class ZKClientTest extends BaseTest{


    private  class RedisChildWatcher extends ZKClient.ChildrenWatcher {

        private String path;

        private RedisChildWatcher(String path) {
            this.path = path;
        }

        @Override
        public void nodeRemoved(String node) {
            System.out.println("remove ...."+node+" ~~~"+path);
        }

        @Override
        public void nodeAdded(String node) {
            System.out.println("add ...."+node+" ~~~"+path);
        }
    }




    /**
     * 服务节点状态监控器
     */
    private class RedisStringValueWatcher implements ZKClient.StringValueWatcher {

        @Override
        public void valueChaned(String l) {
           System.out.println(l+"");
        }
    }

    @Test
    public void testRedisChildWatcher() throws KeeperException, InterruptedException {
        String path = "/redis/cacheGroupInfo";
        RedisChildWatcher sw = new RedisChildWatcher(path);
        ZKClient.get().watchChildren(path, sw);
    }

    @Test
    public void testRedisStringValueWatcher() throws KeeperException, InterruptedException {
        String path = "/redis/cacheGroupInfo";
        String nodeName = "r10.249.15.194:6379";
        //RedisStringValueWatcher sw = new RedisStringValueWatcher();
        ZKClient.StringValueWatcher watcher = new ZKClient.StringValueWatcher(){

            @Override
            public void valueChaned(String l) {
              System.out.println("sdfdsfs"+l);
            }
        };
        ZKClient.get().watchStrValueNode(path, watcher);
    }



    @Test
    public void testGetChildren() throws KeeperException, InterruptedException {
        List<String> nodeList = ZKClient.get().getChildren("/redis/cacheGroupInfo");
        System.out.println(nodeList);
    }

    @Test
    public void testStringChange() throws InterruptedException {

        ZKClient.StringValueWatcher sw = new ZKClient.StringValueWatcher(){

            @Override
            public void valueChaned(String l) {
                System.out.println("............ "+l);
            }
        };

        ZKClient.get().watchStrValueNode("/redis/cacheGroupInfo/r10.249.15.194:6379", sw);

        while (true);
    }

    @Test
    public void testDirChange() {

        ZKClient.ChildrenWatcher cw = new ZKClient.ChildrenWatcher() {


            private String a;



            @Override
            public void nodeRemoved(String node) {
               System.out.println("....remove "+node);
            }

            @Override
            public void nodeAdded(String node) {
                System.out.println("....add "+node);

            }
        };
        ZKClient.get().watchChildren("/redis/cacheGroupInfo", cw);
    }



    public static void main(String[] args) throws InterruptedException, KeeperException {
        new ZKClientTest().testRedisStringValueWatcher();
    }


}
