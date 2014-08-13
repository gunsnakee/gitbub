package com.meiliwan.emall.job.redis;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisExecutor;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ServerInfo;
import com.meiliwan.emall.commons.mongodb.MongoClient;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA. User: jiawuwu Date: 13-10-12 Time: 上午11:35 To
 * change this template use File | Settings | File Templates.
 */
public class MongoCleanData {

    private MongoCleanData(){

    }

	public static final void main(String[] args) throws IOException,
			JedisClientException, InterruptedException, KeeperException {

		args = new String[] { "10.249.15.195", "6379" };

		if (args.length != 2) {
			System.out.println("args length !=2 ");
			return;
		}

		String ip = args[0];
		int port = Integer.parseInt(args[1]);

		JedisExecutor executor = JedisExecutor.initInstance(new ServerInfo(ip,port));

		DBCursor cursor = MongoClient.getInstance().getCursor(null);
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			String objId = dbObj.get("_id").toString();

			String[] parts = objId.split(MongoClient.SPLITER);
			if (parts.length == 2) {
				JedisKey key = JedisKey.valueOf(parts[0]);
				String id = parts[1];
				System.out.println("===> remove from mongo. key=" + key
						+ " id=" + id);
                if(!executor.exists(key,id).getOperRs()){
                    cursor.getCollection().remove(dbObj);
                }

			}
		}
	}

}
