package com.meiliwan.emall.base.dao.impl;

import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.base.dao.BaseSysConfigDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.core.db.BaseDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统基础配置项 dao接口实现类
 * @author yiyou.luo
 * date 2013-06-03
 */

@Repository
public class BaseSysConfigDaoImpl extends BaseDao<Integer, BaseSysConfig> implements BaseSysConfigDao{
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return BaseSysConfigDao.class.getName();
	}

    @Override
    public String getSysValueSysConfigByCode(String sysConfigCode) {
        //1、先根据缓存获取，
       //2、根据缓存无法获取，则根据数据库查，然后把获取到的数据放缓存里

        BaseSysConfig returnObj = null;
        //jedis 服务使用状态
        boolean jedisUseStatus = true;
        String sysValue = null;
        try {
            sysValue = getShardJedisTool().get(JedisKey.base$sys, sysConfigCode);
        } catch (JedisClientException e) {
            logger.error(e, sysConfigCode, "");
            jedisUseStatus = false;
        } catch (Exception e) {
            logger.error(e, sysConfigCode, "");
            jedisUseStatus = false;
        }

        if(sysValue == null){
            BaseSysConfig baseSysConfig = new BaseSysConfig();
            baseSysConfig.setSysConfigCode(sysConfigCode);
            List<BaseSysConfig> list = getListByObj(baseSysConfig, "");
            if(list!=null&&list.size()>0){
                sysValue = list.get(0).getSysConfigValue();
                //如果jedis正常，则加入缓存
                if(jedisUseStatus){
                    try {
                        boolean isInsertCache =  getShardJedisTool().set(JedisKey.base$sys, sysConfigCode, sysValue);
                        if (isInsertCache)
                            logger.debug("insert jedis " + getMapperNameSpace() + " key:" + sysConfigCode + " value: " + baseSysConfig.getSysConfigValue());
                    } catch (JedisClientException e) {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("sysConfigCode", sysConfigCode);
                        map.put("sysValue", sysValue);
                        logger.error(e, map, "");
                    }
                }
            }
    }
        return sysValue;
    }

    private ShardJedisTool getShardJedisTool() throws JedisClientException {
        //一般不会出现
        return ShardJedisTool.getInstance();
    }

    @Override
    //更新时 根据 配置编码删除缓存
    public int update(BaseSysConfig entity) {
        //DAO的更新动作里，不应该包含查询 modify by yuxiong 2013.11.16
        //BaseSysConfig config = this.getEntityById(entity.getSysConfigId());
        //if (config != null && config.getSysConfigId() != null) {
            try {
                boolean isDelCache = getShardJedisTool().del(JedisKey.base$sys, entity.getSysConfigCode());
                return getSqlSession().update(getMapperNameSpace() + updateSelective,
                        getShardParam(entity != null ? entity.getId() : null, entity, true));
            } catch (JedisClientException e) {
                logger.error(e, entity, "");
            } catch (Exception e) {
                throw new ServiceException("service-" + getMapperNameSpace() + ".update: {}", entity == null ? "" : entity.toString(), e);
            }
        //}
        return 0;
    }

    //删除时要清空缓存
//    @Override
//    public int delete(Integer pk) {
//        BaseSysConfig config = this.getEntityById(pk);
//        if (config != null && config.getSysConfigId() != null) {
//            try {
//                getShardJedisTool().del(JedisKey.base$sys, config.getSysConfigCode());
//                return getSqlSession().delete(getMapperNameSpace() + deleteByPrimaryKey,
//                        getShardParam(pk, pk, true));
//            } catch (JedisClientException e) {
//                Map<String,Integer> map = new HashMap<String,Integer>();
//                map.put("pk", pk);
//                logger.error(e, map, "");
//            }catch (Exception e) {
//                throw new ServiceException("service-" + getMapperNameSpace() + ".delete: {}", pk.toString(), e);
//            }
//        }
//        return 0;
//    }

    /**
     * DAO的删除动作里，不应该包含查询 modify by yuxiong 2013.11.16
     * @param config
     * @return
     */
    //删除时要清空缓存
    @Override
    public int delete(BaseSysConfig config) {
//        BaseSysConfig config = this.getEntityById(pk);
//        if (config != null && config.getSysConfigId() != null) {
            try {
                getShardJedisTool().del(JedisKey.base$sys, config.getSysConfigCode());
                return getSqlSession().delete(getMapperNameSpace() + deleteByPrimaryKey,
                        getShardParam(config.getSysConfigId(), config.getSysConfigId(), true));
            } catch (JedisClientException e) {
                Map<String,Integer> map = new HashMap<String,Integer>();
                map.put("pk", config.getSysConfigId());
                logger.error(e, map, "");
            }catch (Exception e) {
                throw new ServiceException("service-" + getMapperNameSpace() + ".delete: {}", config.getSysConfigId().toString(), e);
            }
      //  }
        return 0;
    }

    @Override
    public PagerControl<BaseSysConfig> getSeoListByEntityAndPageInfo(BaseSysConfig baseSysConfig, PageInfo pageInfo) {
        PagerControl<BaseSysConfig> pagerControl = new PagerControl<BaseSysConfig>();
        pageInfo.startTime();
        List<BaseSysConfig> list = null;
        int total = 0;
        list = getSqlSession().selectList(getMapperNameSpace() + ".getSeoListByEntityAndPageInfo",
                getShardParam(null, getMapParams(baseSysConfig, pageInfo, getOrderBySql(pageInfo)), false));
        Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getSeoTotalByEntity" ,
                getShardParam(null, getMapParams(baseSysConfig, null, null), false));
        total = (Integer) selectOne;

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    private Map<String, Object> getMapParams(BaseSysConfig entity, PageInfo pageInfo, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != entity) map.put("entity", entity);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }
}
