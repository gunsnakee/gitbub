package com.meiliwan.emall.search.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.meiliwan.emall.commons.exception.BaseException;



/**
 * search中使用了同质架构的
 * @author lgn-mop
 *
 */
public class SearchComponents {
	private static Map<String, SearchComponentHttpClient> clients = new ConcurrentHashMap<String, SearchComponentHttpClient>();
	
	public enum Type{
		spell, suggest
	}
	
	public static SearchComponentHttpClient getByComponentTypeName(Type typeName) throws BaseException{
		SearchComponentHttpClient client = clients.get(typeName.toString());
		if (client == null){
			synchronized(SearchComponents.class){
				if (client == null){
					client = new SearchComponentHttpClient(typeName.toString());
					clients.put(typeName.toString(), client);
				}
			}
		}
		if (client.isServing()){
			return client;
		}else { //非法客户端
			synchronized(SearchComponents.class){
				client.openClient();
				if (client.isServing()){
					return client;
				}else{
					throw new BaseException("client not serving yet!!!!!");
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws BaseException {
		SearchComponents.getByComponentTypeName(Type.spell);
		SearchComponents.getByComponentTypeName(Type.spell);
		SearchComponents.getByComponentTypeName(Type.spell);
	}
}
