package com.meiliwan.emall.cms2.dao.impl;

import com.meiliwan.emall.cms2.bean.ThematicPage;
import com.meiliwan.emall.cms2.dao.ThematicPageDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * User: wenlepeng
 * Date: 14-4-9
 * Time: 下午4:02
 */
@Repository
public class ThematicPageDaoImpl extends BaseDao<Integer,ThematicPage> implements ThematicPageDao {
    @Override
    public String getMapperNameSpace() {
        return ThematicPageDao.class.getName();
    }

    @Override
    public int deletePage(int pageId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id", pageId);
        map.put("updateTime", updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".deletePage", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deletePage: {},{}", new String[]{pageId+""}, e);
        }
    }

    @Override
    public int updataPagdJsonData(int pageId, String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id", pageId);
        map.put("json",json);
        map.put("updateTime", updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updataPagdJsonData", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updataPagdJsonData: {},{}", new String[]{pageId+"",json}, e);
        }
    }

    @Override
    public int updataPageProIds(int pageId, String ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id", pageId);
        map.put("ids",ids);
        map.put("updateTime", updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updataPageProIds", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updataPageProIds: {},{}", new String[]{pageId+"",ids}, e);
        }
    }
}
