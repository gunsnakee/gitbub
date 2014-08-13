package com.meiliwan.emall.job.redis;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisExecutor;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ServerInfo;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.mongodb.MongoClient;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class RedisRecover {


    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(RedisRecover.class);

    private static final int MAX = 100; //  最大尝试次数


    private RedisRecover(){

    }

    private static void recover(JedisExecutor executor,JedisKey key,String id,String type,Object valObj) throws JedisClientException {

        if(JedisKey.STRING.equals(type)){
            executor.set(key,id,valObj.toString());
        }else if(JedisKey.HASH.equals(type)){
            executor.del(key,id);
            executor.hmset(key,id,(Map<String,String>)valObj);
        }else if(JedisKey.LIST.equals(type)){ // 未支持

        }else if(JedisKey.SET.equals(type)){
            executor.del(key,id);
            BasicDBList dblist = (BasicDBList)valObj;
            Iterator<Object> datas = dblist.iterator();
            while (datas.hasNext()){
                executor.sadd(key, id, datas.next().toString());
            }

        }else if(JedisKey.SORTED_SET.equals(type)){ // 未支持



        }

    }


    private static Long recover(JedisExecutor executor,Long lastTime) throws IOException, InterruptedException, KeeperException {
        DBCursor cursor = MongoClient.getInstance().getCursor(lastTime);
        Long newLastTime = null;
        int idx = 0;
        while(cursor.hasNext()){

            DBObject dbObj = cursor.next();

            if(idx==0){
                newLastTime = (Long)dbObj.get("upTime");
            }
            idx++;

            String objId = dbObj.get("_id").toString();
            Object valObj = dbObj.get("val");
            String type = (String)dbObj.get("type");
            Long expireTime = (Long)dbObj.get("expireTime");

            if(StringUtils.isNotBlank(type) && valObj!=null){
                String[] parts = objId.split(MongoClient.SPLITER);
                if(parts.length==2){
                    try {
                        JedisKey key = JedisKey.valueOf(parts[0]);
                        String id = parts[1];

                        if(expireTime!=null && expireTime>0 ){
                            long extime =  expireTime - System.currentTimeMillis() ;
                            if( extime>0 ){
                                recover(executor,key,id,type,valObj);
                                executor.expire(key,id,(int)(extime/1000));
                            }
                        }else{
                            recover(executor,key,id,type,valObj);
                        }

                    }catch (Exception e){
                        LOGGER.error(e,"objId:" + objId + ",value:" + valObj,null);
                    }

                }

            }
        }

        return newLastTime;

    }



	public static void main(String[] args) throws Exception {


        //args = new String[]{"10.249.15.196","6379"};

        if(args.length!=2){
            System.out.println("args length !=2 ");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        JedisExecutor executor = JedisExecutor.initInstance(new ServerInfo(ip,port));

        long start = System.currentTimeMillis();

        Long lasttime = recover(executor,null);

        if(lasttime!=null){
            for(int i=0;i<MAX;i++){
                lasttime = recover(executor,lasttime);
                if(lasttime==null){
                    break;
                }
           }
        }

        System.out.println("~~~~~ final useTime="+(System.currentTimeMillis()-start));

        System.exit(0);

	}

}
