package com.meiliwan.emall.base.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.base.dao.BaseSysConfigDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

/**
 * 系统基础配置项 管理 server
 *
 * @author yiyou.luo
 *         2013-06-03
 */

@Service
public class BaseSysConfigService extends DefaultBaseServiceImpl {
    @Autowired
    private BaseSysConfigDao baseSysConfigDao;

    /**
     * 根据配置项ID获得配置项对象
     *
     * @param sysConfigId
     * @param resultObj
     */
    @IceServiceMethod
    public void getSysConfigById(JsonObject resultObj, Integer sysConfigId) {
            BaseSysConfig baseSysConfig = baseSysConfigDao.getEntityById(sysConfigId);
            addToResult(baseSysConfig, resultObj);
    }

    /**
     * 添加配置项
     *
     * @param baseSysConfig
     * @param resultObj
     */
    @IceServiceMethod
    public  void saveSysConfig(JsonObject resultObj, BaseSysConfig baseSysConfig) {
        if (baseSysConfig != null) {
            baseSysConfig.setSysConfigId(null);
            baseSysConfig.setCreateTime(new Date());
            baseSysConfig.setUpdateTime(new Date());
            baseSysConfig.setIsDel(0);
            baseSysConfigDao.insert(baseSysConfig);
            addToResult(baseSysConfig.getSysConfigId()>0 ? baseSysConfig.getSysConfigId():-1, resultObj);
        }else {
            addToResult(-1, resultObj);
        }
    }

    /**
     * 修改配置项
     *
     * @param resultObj
     * @param baseSysConfig
     */
    @IceServiceMethod
    public  void updateSysConfig(JsonObject resultObj, BaseSysConfig baseSysConfig) {
        if (baseSysConfig != null) {
            baseSysConfig.setUpdateTime(new Date());
            BaseSysConfig config = baseSysConfigDao.getEntityById(baseSysConfig.getSysConfigId(), true);
            if (config != null && config.getSysConfigId() != null) {
                addToResult(baseSysConfigDao.update(baseSysConfig)>0?true:false, resultObj);
            }
        }else{
            addToResult(false, resultObj);
        }
    }

    /**
     * 通过 配置编码 获得配置对象
     *
     * @param sysConfigCode
     * @param resultObj
     */
    @IceServiceMethod
    public void getSysConfigByCode(JsonObject resultObj,  String sysConfigCode) {
        if (sysConfigCode != null) {
            BaseSysConfig baseSysConfig = new BaseSysConfig();
            baseSysConfig.setSysConfigCode(sysConfigCode);
            addToResult(baseSysConfigDao.getListByObj(baseSysConfig, ""), resultObj);
        }
    }

    /**
     * 通过 配置编码 获得唯一对象
     *
     * @param sysConfigCode
     * @param resultObj
     */
    @IceServiceMethod
    public void getUniqueSysConfigByCode(JsonObject resultObj,  String sysConfigCode) {
        if (StringUtils.isBlank(sysConfigCode)) {
        		throw new ServiceException("getUniqueSysConfigByCode","sysConfigCode can not be null");
        }
        BaseSysConfig baseSysConfig = new BaseSysConfig();
        baseSysConfig.setSysConfigCode(sysConfigCode);
        List<BaseSysConfig> list = baseSysConfigDao.getListByObj(baseSysConfig, "");
        if(list!=null&&list.size()>0){
        	 	addToResult(list.get(0), resultObj);
        }else{
        		addToResult(new BaseSysConfig(), resultObj);
        }
    }
    
    /**
     * 通过配置项 实体参数获取对应的实体列表包含物理分页
     *
     * @param baseSysConfig
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getSysConfigPaperByObj(JsonObject resultObj, BaseSysConfig baseSysConfig,  PageInfo pageInfo) {
        if (baseSysConfig != null && pageInfo != null) {
            baseSysConfig.setIsDel(0);
            addToResult(baseSysConfigDao.getPagerByObj(baseSysConfig, pageInfo,"", " order by create_time desc "), resultObj);
        }
    }

    /**
     * 通过 配置编码 获得配置值
     * @param sysConfigCode
     * @return
     */
    public void getSysValueSysConfigByCode(JsonObject resultObj,String sysConfigCode){
        if (StringUtils.isBlank(sysConfigCode)) {
            throw new ServiceException("getUniqueSysConfigByCode","sysConfigCode can not be null");
        }
        String sysValue = baseSysConfigDao.getSysValueSysConfigByCode(sysConfigCode);
        if(sysValue!=null){
            //addToResult(baseSysConfigDao.getSysValueSysConfigByCode(sysConfigCode), resultObj);
            //没有必要再走一次  getSysValueSysConfigByCode modify by yuxiong 2013.11.15
            addToResult(sysValue, resultObj);
        } else{
            addToResult("", resultObj);
        }


    }

    /**
     * 通过配置项 实体参数获取对应的 SEO 相关的实体列表包含物理分页
     *
     * @param baseSysConfig
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getSeoListByEntityAndPageInfo(JsonObject resultObj, BaseSysConfig baseSysConfig,  PageInfo pageInfo) {
        if (baseSysConfig != null && pageInfo != null) {
            baseSysConfig.setIsDel((int) GlobalNames.STATE_VALID);
            addToResult(baseSysConfigDao.getSeoListByEntityAndPageInfo(baseSysConfig, pageInfo), resultObj);
        }
    }

    public void getListByCondition(JsonObject resultObj,BaseSysConfig baseSysConfig,PageInfo pageInfo,String whereSql,String orderSql){
        BaseSysConfig qureyParam = null;
        if(baseSysConfig == null){
            qureyParam = new BaseSysConfig();
        }else{
            qureyParam = baseSysConfig;
        }
        baseSysConfig.setIsDel((int) GlobalNames.STATE_VALID);
        addToResult(baseSysConfigDao.getPagerByObj(qureyParam,pageInfo,whereSql,orderSql),resultObj);
    }


}
