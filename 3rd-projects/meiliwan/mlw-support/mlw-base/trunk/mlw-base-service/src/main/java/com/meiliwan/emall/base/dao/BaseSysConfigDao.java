package com.meiliwan.emall.base.dao;

import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
/**
 * 系统基础配置项  dao接口
 * @author yiyou.luo
 *2013-06-03
 */
public interface BaseSysConfigDao extends IDao<Integer, BaseSysConfig>{
    /**
     * 通过 配置编码 获得配置值
     * @param sysConfigCode
     * @return
     */
    public String getSysValueSysConfigByCode(String sysConfigCode);

    public int delete(BaseSysConfig config);

    /**
     * SEO  分页查询
     * @param baseSysConfig
     * @param pageInfo
     * @return
     */
    PagerControl<BaseSysConfig> getSeoListByEntityAndPageInfo(BaseSysConfig baseSysConfig, PageInfo pageInfo);
}