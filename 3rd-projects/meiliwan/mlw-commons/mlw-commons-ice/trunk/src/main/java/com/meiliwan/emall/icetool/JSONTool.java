package com.meiliwan.emall.icetool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.meiliwan.emall.service.BaseService;

public class JSONTool {

//	private final static Logger LOGGER = LoggerFactory
//			.getLogger(JSONTool.class);

	public static String buildArrayParams(String action, Object[] objs ){
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("action", action);

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		if (objs != null && objs.length > 0) {
			JsonArray params = new JsonArray();
			params.add(gson.toJsonTree(objs));
			jsonObj.add("params", gson.toJsonTree(params));
		}

		return jsonObj.toString();
	}

	/**
	 * 需要传多个参数，可以使用该方法，该方法参数objs必须至少有一个参数值，否则会抛异常
	 * 注意：带有泛型的参数解析不正常，（例如：List<HelloWorld>将被解析成List<LinkedHashMap>， List<Integer>将被解析成List<String>），所以请将参数转换成数组，并使用buildArrayParams方法进行数据编码
	 * @param action
	 * @param objs
	 * @return
	 */
	public static String buildParams(String action, Object... objs) {
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("action", action);

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		if (objs != null && objs.length > 0) {
			JsonArray params = new JsonArray();
			for (Object value : objs) {
				params.add(gson.toJsonTree(value));
			}
			jsonObj.add("params", gson.toJsonTree(params));
		}

		return jsonObj.toString();

	}

	/**
	 * 
	 * @param code
	 *            错误代码
	 * @param msg
	 *            错误消息
	 * @param resultObj
	 *            包含返回结果的json对象
	 */
	public static void addToResultStatus(String code, String msg,
			JsonObject resultObj) {
		resultObj.addProperty(BaseService.CODE, code);
		resultObj.addProperty(BaseService.FAIL_MESSAGE_NAME, msg);
	}

	/**
	 * 
	 * @param resultMap
	 *            返回值，以 "resultObj" 作为json的键返回
	 * @param resultObj
	 *            包含返回结果的json对象
	 */
	@SuppressWarnings("rawtypes")
	public static void addToResultMap(Map resultMap, JsonObject resultObj) {
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		resultObj.add(BaseService.RESULT_OBJ, gson.toJsonTree(resultMap));
	}
	
	@SuppressWarnings("rawtypes")
	public static void addToResult(Set values, JsonObject resultObj){
		resultObj.add(BaseService.RESULT_OBJ, new Gson().toJsonTree(values));
	}
	
	/**
	 * 
	 * @param result
	 *            返回值，以 "resultObj" 作为json的键返回
	 * @param resultObj
	 *            包含返回结果的json对象
	 */
	public static void addToResult(Serializable result, JsonObject resultObj) {
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		resultObj.add(BaseService.RESULT_OBJ, gson.toJsonTree(result));
	}

    /**
     *
     * @param valueObj
     *            返回值，以 "resultObj" 作为json的键返回,给缓存使用
     * @param resultObj
     *            包含返回结果的json对象
     */
    public static void addToResult(JsonObject valueObj, JsonObject resultObj){
        resultObj.add(BaseService.RESULT_OBJ, valueObj);
    }

    /**
     *
     * @param valueObj
     *            返回值，以 "resultObj" 作为json的键返回,这个是查询缓存list列表时使用
     * @param resultObj
     *            包含返回结果的json对象
     */
    public static void addToResult(JsonArray valueObj, JsonObject resultObj){
        resultObj.add(BaseService.RESULT_OBJ, valueObj);
    }

	/**
     *
     */
	public static Object getResult(JsonObject resultObj) {
		return resultObj.get(BaseService.RESULT_OBJ);
	}

	/**
	 * 
	 * @param listObj
	 *            返回值，以 "resultObj" 作为json的键返回
	 * @param resultObj
	 *            包含返回结果的json对象
	 */
	@SuppressWarnings("rawtypes")
	public static void addToResult(List listObj, JsonObject resultObj) {
		resultObj.add(BaseService.RESULT_OBJ, new Gson().toJsonTree(listObj));
	}

}
