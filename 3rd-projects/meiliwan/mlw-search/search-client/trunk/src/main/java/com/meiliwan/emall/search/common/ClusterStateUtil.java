package com.meiliwan.emall.search.common;

import java.util.Map.Entry;

import org.apache.zookeeper.KeeperException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;

public class ClusterStateUtil {
	/**
	 * 每次从ZK获取再解析
	 * @param collectionFull
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static String getLeaderFromZK(String collectionFull) throws KeeperException, InterruptedException{
		JsonParser jp = new JsonParser();
		String clusterstatejson = ZKClient.get().getStringData(Constants.SOLR_CHROOT + "/clusterstate.json");
		JsonObject clusterstate = jp.parse(clusterstatejson).getAsJsonObject();
		return getLeaderFromObject(clusterstate, collectionFull);
	}
	
	public static String getLeaderFromObject(JsonObject clusterstate, String collectionFull){
		JsonObject collectionstate = clusterstate.getAsJsonObject(collectionFull);
		if (collectionstate == null){
			return null;
		}
		JsonObject replicas = collectionstate.getAsJsonObject("shards").getAsJsonObject("shard1").getAsJsonObject("replicas");
		for(Entry<String, JsonElement> kv: replicas.entrySet()){
			JsonObject node = kv.getValue().getAsJsonObject();
			if (node.get("leader") != null && node.get("leader").getAsString().equalsIgnoreCase("true") ){
				return node.get("base_url").getAsString() ;
			}
		}
		return null;
	}
	
}
