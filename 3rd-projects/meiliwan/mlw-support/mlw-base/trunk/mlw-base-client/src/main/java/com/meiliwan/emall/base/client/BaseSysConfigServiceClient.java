package com.meiliwan.emall.base.client;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

/**
 * 系统基础配置项管理 ICE Client
 * Created by yiyou.luo on 13-6-7.
 */
public class BaseSysConfigServiceClient {
	
	
    public final  static void main(String args[]){
       /* BaseSysConfig config = new BaseSysConfig();
        PageInfo pageInfo = new PageInfo();
        for(int i=0; i<10000;i++){
            PagerControl pc = getSysConfigPaperByObj(config,pageInfo) ;
            for(int j=0; j<pc.getEntityList().size();j++) {
                System.out.println(i+":"+j);

                if ("com.meiliwan.emall.base.bean.BaseSysConfig".equals(pc.getEntityList().get(j).getClass().getName())!=true)    {
                    System.out.println(i+":"+j+"=:"+pc.getEntityList().get(j).getClass().getName());
                    return;
                }

            }
        }*/
        getSysValueSysConfigByCode("transport_price_min");

    }

    /**
     * 根据配置项ID获得配置项对象
     *
     * @param sysConfigId
     */
    public static BaseSysConfig getSysConfigById(Integer sysConfigId) {
        JsonObject obj=null;
	    obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseSysConfigService/getSysConfigById", sysConfigId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<BaseSysConfig>() {
        }.getType());
    }

    /**
     * 添加配置项
     *
     * @param baseSysConfig
     */
    public static Integer saveSysConfig(BaseSysConfig baseSysConfig) {
        JsonObject obj=null;
		obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseSysConfigService/saveSysConfig", baseSysConfig));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }

    /**
     * 修改配置项
     *
     * @param baseSysConfig
     */
    public static boolean updateSysConfig(BaseSysConfig baseSysConfig) {
        JsonObject obj=null;
		obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseSysConfigService/updateSysConfig", baseSysConfig));
        return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
    }

    /**
     * 通过 配置编码 获得配置对象list
     *
     * @param sysConfigCode
     */
    @IceServiceMethod
    public static List<BaseSysConfig> getSysConfigByCode(String sysConfigCode) {
        JsonObject obj=null;
		obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseSysConfigService/getSysConfigByCode",sysConfigCode));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BaseSysConfig>>() {
        }.getType());
    }

    /**
     * 通过 配置编码 获得唯一对象
     * @param sysConfigCode
     * @return
     */
    public static BaseSysConfig getUniqueSysConfigByCode(String sysConfigCode) {
        JsonObject obj=  IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseSysConfigService/getUniqueSysConfigByCode",sysConfigCode));
        return new Gson().fromJson(obj.get("resultObj"), BaseSysConfig.class);
    }
    
    
    
    /**
     * 通过配置项 实体参数获取对应的实体列表包含物理分页
     *
     * @param baseSysConfig
     * @param pageInfo
     */
    public static PagerControl getSysConfigPaperByObj(BaseSysConfig baseSysConfig, PageInfo pageInfo) {


        JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
			        JSONTool.buildParams("baseSysConfigService/getSysConfigPaperByObj", baseSysConfig, pageInfo ));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BaseSysConfig>>() {
        }.getType());

    }

    /**
     * 通过 配置编码 获得配置值
     * @param sysConfigCode
     * @return
     */
    public static String getSysValueSysConfigByCode(String sysConfigCode){
        JsonObject obj=  IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseSysConfigService/getSysValueSysConfigByCode",sysConfigCode));
        System.out.println(obj.toString());
        return obj.get("resultObj").equals("") ? null : obj.get("resultObj").getAsString();
    }

    /**
     * 通过配置项 实体参数获取对应的 SEO 相关实体列表包含物理分页
     *
     * @param baseSysConfig
     * @param pageInfo
     */
    public static PagerControl getSeoListByEntityAndPageInfo(BaseSysConfig baseSysConfig, PageInfo pageInfo) {


        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseSysConfigService/getSeoListByEntityAndPageInfo", baseSysConfig, pageInfo ));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BaseSysConfig>>() {
        }.getType());

    }

    public  static PagerControl getListByConditon(BaseSysConfig baseSysConfig,PageInfo pageInfo,String whereSql,String orderSql){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,JSONTool.buildParams("baseSysConfigService/getListByCondition",baseSysConfig,pageInfo,whereSql,orderSql));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BaseSysConfig>>() {}.getType());
    }


}
