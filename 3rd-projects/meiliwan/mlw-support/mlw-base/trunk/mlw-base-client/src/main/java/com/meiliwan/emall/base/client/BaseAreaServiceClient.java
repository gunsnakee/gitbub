package com.meiliwan.emall.base.client;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.BaseAreaManagement;
import com.meiliwan.emall.base.dto.IPLocation;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.HttpClientUtil;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地域管理 ICE Client
 *
 * @author yiyou.luo
 *         2013-06-04
 */
public class BaseAreaServiceClient {

	private static MLWLogger logger = MLWLoggerFactory.getLogger(BaseAreaServiceClient.class);
    private static Map<String,String> proMap = new HashMap<String, String>();   //省份map<省名称，地区对象>
    private static Map<String,String> cityMap = new HashMap<String, String>() ;  //城市map <城市名称，地区对象>

    public static void main(String args[]) {
        //getAreasByParentCode("0");
        //1、创建数据
      /*  BaseAreaManagement area = new BaseAreaManagement();
    	area.setAreaCode("12313213");
    	area.setAreaGrade(1);
    	area.setAreaName("广西");
    	area.setIsDel(0);
    	area.setIsLastGrade(-1);
    	area.setParentCode("0");
    	Integer newId = saveArea(area);
    	System.out.println("地域的主键id="+newId);*/

        //2、根据id拿到数据
    //    BaseAreaManagement area2 = getAreaById(7021);
     //   System.out.println("getbyId name=" + area2.getAreaName() + "  code=" + area2.getAreaCode());

        //3、修改地域
     //   area2.setAreaName(area2.getAreaName() + "修改地域");
      //  updateArea(area2);

        //4、根据上级编码获取地域
       // System.out.println("getAreasByParentCode=" + getAreasByParentCode("0").size());

        //5、根据地域级别获取编码
       // System.out.println("getAreasByGrade=" + getAreasByGrade("1").size());
       //     getAreaPaperByObj(new BaseAreaManagement(),new PageInfo());
      //  getProAndCityByNames("广西", "南宁");
      //  getProAndCityByNames("广东","广州");
        getProAndCityByNames("北", "北");
        getProAndCityByNames("北", "");
        getProAndCityByNames(null, null);
    }

    /**
     * 根据地域ID获得产地对象
     *
     * @param areaId
     */
    public static BaseAreaManagement getAreaById (int areaId) {
        JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
			        JSONTool.buildParams("baseAreaManagementService/getAreaById", areaId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<BaseAreaManagement>() {
        }.getType());
    }

    /**
     * 添加地域
     *
     * @param baseAreaManagement
     */
    public static Integer saveArea(BaseAreaManagement baseAreaManagement) {
        JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
			        JSONTool.buildParams("baseAreaManagementService/saveArea", baseAreaManagement));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }

    /**
     * 修改地域
     *
     * @param baseAreaManagement
     */
    public static boolean updateArea(BaseAreaManagement baseAreaManagement) {
        JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
			        JSONTool.buildParams("baseAreaManagementService/updateArea", baseAreaManagement));
        return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
    }

    /**
     * 通过上级编码获取未删除的地域
     *
     * @param parentCode
     */
    public static List<BaseAreaManagement> getAreasByParentCode(String parentCode) {
        JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                    JSONTool.buildParams("baseAreaManagementService/getAreasByParentCode", parentCode));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BaseAreaManagement>>() {
        }.getType());
    }

    /**
     * 通过地区级别获取地域（未逻辑删除）
     *
     * @param grade
     */
    public static List<BaseAreaManagement> getAreasByGrade(String grade) {
        JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
			        JSONTool.buildParams("baseAreaManagementService/getAreasByGrade",grade));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BaseAreaManagement>>() {
        }.getType());
    }

    /**
     * 通过地域 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     */
    public static PagerControl<BaseAreaManagement> getAreaPaperByObj(BaseAreaManagement baseAreaManagement, PageInfo pageInfo) {
        JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
			        JSONTool.buildParams("baseAreaManagementService/getAreaPaperByObj",baseAreaManagement,pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BaseAreaManagement>>() {
        }.getType());
    }

    /**
     * 通过省名称和城市名称返回相应JSON编码串
     * @param province  省名
     * @param city 城市名
     * @return String {provinceCode:10234, cityCode:20333, townCode:34566}
     */
    public static String getProAndCityByNames(String province, String city ){
    	JsonObject resultObj = new JsonObject();
        if(proMap.size()==0 || cityMap.size()==0){
            initAreaMap();
        }
        String provinceCode = "100045";  //默认广西
        String cityCode = "10004501";    //默认南宁
        if(!Strings.isNullOrEmpty(province) && !Strings.isNullOrEmpty(city) && province.length()>1  && city.length()>1){
            provinceCode = proMap.get(province.substring(0,2));
            cityCode =  cityMap.get(city.substring(0,2));
        }
        resultObj.addProperty("provinceCode", provinceCode);
        resultObj.addProperty("cityCode", cityCode);
        resultObj.addProperty("townCode", 0);

        return resultObj.toString();
    }
    
    /**
     * 通过IP 获得省名称和城市名称返回相应JSON编码串
     * @param ipInfo IP地址
     * @return String {provinceCode:10234, cityCode:20333, townCode:34566}
     */
    public static String getProAndCityByIp(String ipInfo){
    	String result = "" ;
        try{
        	result = ShardJedisTool.getInstance().hget(JedisKey.base$ipinfo, "", ipInfo);
			
		}catch (JedisClientException e) {
			logger.error(e, "JedisKey:" + JedisKey.base$ipinfo +",ipInfo:" + ipInfo, null);
		}
		
    	String province = "" ;
        String city = "" ;
		// 如果缓存为空或者Jedis已经当机，则需要重新查询数据库然后放到缓存中
		if (Strings.isNullOrEmpty(result)) {
//	        try{
//		    	String uri = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=java&ip=" + ipInfo ;
//		        String backResult = HttpClientUtil.getInstance().doGet(uri);
//		        logger.debug("sina iplookup return:" + backResult);
//		        
//		        String[] parts = backResult.split("\t");
//			       
//		        if(parts!=null && parts.length >5){
//		        	province = parts[4];
//		        	city = parts[5];
//		        }
//		        
//	        }catch (Exception e) {
//				logger.error(e, "ipInfo:"+ipInfo, null);
//			}
			
			IPLocation loc = BaseIPParseClient.getIPLocation(ipInfo);
			if(loc != null){
				province = loc.getProvince();
				city = loc.getCity();
			}
        
	    	JsonObject resultObj = new JsonObject();
	        if(proMap.size()==0 || cityMap.size()==0){
	            initAreaMap();
	        }
	        String provinceCode = "100045";  //默认广西
	        String cityCode = "10004501";    //默认南宁
	        if(!Strings.isNullOrEmpty(province) && !Strings.isNullOrEmpty(city) && province.length()>1  && city.length()>1){
	            provinceCode = proMap.get(province.substring(0,2));
	            cityCode =  cityMap.get(city.substring(0,2));
	        }
	        resultObj.addProperty("provinceCode", provinceCode);
	        resultObj.addProperty("cityCode", cityCode);
	        resultObj.addProperty("townCode", 0);
	        result = resultObj.toString();
	        try{
	        	ShardJedisTool.getInstance().hset(JedisKey.base$ipinfo, "", ipInfo, result);
			}catch (JedisClientException e) {
				logger.error(e, "JedisKey:" + JedisKey.base$ipinfo +",ipInfo:" + ipInfo, null);
			}
		}

        return result;
    }

    private static void initAreaMap(){
        Map<String,BaseAreaManagement> proMapLS = new HashMap<String, BaseAreaManagement>();// 临时map
        List<BaseAreaManagement> proList = getAreasByGrade("1") ;
        List<BaseAreaManagement> cityList = getAreasByGrade("2") ;
        for(BaseAreaManagement area : proList){
            proMap.put(area.getAreaName().substring(0, 2),area.getAreaCode());
            proMapLS.put(area.getAreaCode(),area);
        }
        for(BaseAreaManagement area : cityList){
            if("市辖区".equals(area.getAreaName())) {
                BaseAreaManagement parentArea = proMapLS.get(area.getParentCode()) ;
                cityMap.put(parentArea.getAreaName().substring(0,2),area.getAreaCode());
            }else if(!"县".equals(area.getAreaName())){
                cityMap.put(area.getAreaName().substring(0,2),area.getAreaCode());
            }

        }


    }

    /**
     * 根据地域名称和地域级别获取地址，以及同级别同parentid的地址
     * @param name
     * @param grade
     */
    public static String getAreasByNameAndGrade(String name,int grade) {
        String result = "";
        try{
            JsonObject obj=null;
            obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                    JSONTool.buildParams("baseAreaManagementService/getAreasByNameAndGrade",name,grade));
            result = obj.get("resultObj").toString();
        }catch(Exception e){
            logger.error(e,"根据地域名称和地域级别获取地址以及同级别同parentid的地址异常",null);
        }
        return result;
    }


}
