package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProPropertyValue;
import com.meiliwan.emall.pms.dao.ProPropertyValueDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProPropertyValueDaoImpl extends BaseDao<Integer,ProPropertyValue> implements ProPropertyValueDao{

    @Override
    public String getMapperNameSpace() {
        return ProPropertyValueDao.class.getName();
    }

    @Override
    public List<ProPropertyValue> getValuesByIds(String[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        List<ProPropertyValue> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getValuesByIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getValuesByIds: {}", ids.toString(), e);
        }
    }
}