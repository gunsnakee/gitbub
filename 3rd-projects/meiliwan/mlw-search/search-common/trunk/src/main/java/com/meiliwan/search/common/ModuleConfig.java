package com.meiliwan.search.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

public class ModuleConfig {

	private static class ABTest{
		static Random r = new Random();
		double[] boundaries ;
		Stratagy[] stratagies ;
		
		public ABTest(JSONObject ab){
			boundaries = new double[ab.size()];
			stratagies = new Stratagy[ab.size()];
			int i = 0;
			double ratioSum = 0;
			for(Object strgy : ab.keySet()){
				JSONObject values = ab.getJSONObject(strgy.toString());
				ratioSum += values.getDouble(Constants.ABTEST_RATIO);
				JSONObject paraMap = values.getJSONObject(Constants.ABTEST_PARAM);
				Map<String, Number> tmpPara = new HashMap<String, Number>();
				for(Object paraKey : paraMap.keySet()){
					tmpPara.put(paraKey.toString(), (Number)paraMap.get(paraKey));
				}
				stratagies[i] = new Stratagy(strgy.toString(), values.getString(Constants.ABTEST_ABBR), tmpPara);
				boundaries[i] = values.getDouble("ratio");
			}
			for(int j = 0 ; j < boundaries.length; j++){
				boundaries[i] /= ratioSum;
			}
			boundaries[boundaries.length-1] += 1.0;
		}
		
		public Stratagy randomStratagy(){
			double d = r.nextDouble();
			for(int i = 0 ; i < boundaries.length -1; i++){
				if (d < boundaries[i]){
					return stratagies[i];
				}
			}
			return stratagies[stratagies.length-1];
		}
		
		public String toString(){
			JSONObject jo = new JSONObject();
			for(Stratagy s : stratagies){
				jo.put(s.stratagyName, s.simpleFormat);
			}
			return jo.toString();
		}
		
	}
	
	public static class Stratagy{
		
		String stratagyName;
		String simpleFormat;
		Map<String, Number> parameters;
		
		public Stratagy(String name, String simpleFormat, Map<String, Number> para){
			this.stratagyName = name;
			this.simpleFormat = simpleFormat;
			this.parameters = para;
		}

		public Number getValue(String key, Number defaultValue){
			Number value = parameters.get(key);
			if (value == null){
				return defaultValue;
			}else{
				return value;
			}
		}
		
		public String toString(){
			return String.format("{\"%s\":\"%s\"}", stratagyName, simpleFormat);
		}
		
	}
	
	private volatile Map<String, Object> basicConfig;
	private ABTest abTest;
	
	public boolean load(String json){
		JSONObject jo = JSONObject.fromObject(json);
		JSONObject ab = (JSONObject)jo.remove(Constants.ABTEST_STRING);
		Map<String, Object> tmp = new HashMap<String, Object>();
		for(Object name : jo.keySet()){
			tmp.put(name.toString(), jo.get(name));
		}
		basicConfig = tmp;
		
		ABTest abTmp = new ABTest(ab);
		abTest = abTmp;
		
		return false;
	}
	
	public Stratagy getStratagy(){
		return abTest.randomStratagy();
	}
	
	public Object getValue(String key, Object defaultValue){
		Object value = basicConfig.get(key);
		if (value == null){
			return defaultValue;
		}else{
			return value;
		}
	}
	
	public String toString(){
		JSONObject result = new JSONObject();
		result.putAll(basicConfig);
		result.put(Constants.ABTEST_STRING, abTest.toString());
		return result.toString();
	}
}
