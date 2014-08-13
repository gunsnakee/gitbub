package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProAction;
import com.meiliwan.emall.pms.dao.ProActionDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户对商品的操作行为表 比如点击喜欢等
 * User: wuzixin
 * Date: 13-7-8
 * Time: 下午10:21
 */
@Repository
public class ProActionDaoImpl extends BaseDao<Integer,ProAction> implements ProActionDao{
    @Override
    public String getMapperNameSpace() {
        return ProActionDao.class.getName();
    }

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.pms$action;
    }

    @Override
    public int updateActionByOpt(int id, String param) {
        deleteCacheByPk(id);
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id", id);
        map.put("param", param);
        map.put("updateTime",updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateActionByOpt", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateActionByOpt: {},{}", new String[]{String.valueOf(id), param.toString()}, e);
        }
    }

    @Override
    public int updateProSale(int id, int num) {
        deleteCacheByPk(id);
        Map<String,Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id",id);
        map.put("num",num);
        map.put("updateTime",updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateProSale", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateProSale: {},{}", new String[]{String.valueOf(id), String.valueOf(num)}, e);
        }
    }

    @Override
    public int updateProScan(int id, int num) {
        deleteCacheByPk(id);
        Map<String,Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id",id);
        map.put("num",num);
        map.put("updateTime",updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateProScan", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateProScan: {},{}", new String[]{String.valueOf(id), String.valueOf(num)}, e);
        }
    }

    @Override
    public int updateCommentByDelete(int id, String param) {
        deleteCacheByPk(id);
        Map<String,Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id",id);
        map.put("param", param);
        map.put("updateTime",updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateCommentByDelete", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateCommentByDelete: {},{}", new String[]{String.valueOf(id), param}, e);
        }
    }

    @Override
    public int updateCommentIdById(int proId, int commentId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", proId);
        map.put("commentId", commentId);
        map.put("updateTime", DateUtil.getCurrentDateTimeStr());
        try {
            int count = getSqlSession().update(getMapperNameSpace() + ".updateCommentIdById", getShardParam(null, map, true));
            if (count>0){
                deleteCacheByPk(proId);
            }
            return count;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateCommentIdById: {},{}", map.toString(), e);
        }
    }
}
