package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProProductProperty;
import com.meiliwan.emall.pms.bean.ProProductPropertyKey;
import com.meiliwan.emall.pms.bean.ProSelfProperty;
import com.meiliwan.emall.pms.dao.ProProductPropertyDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProProductPropertyDaoImpl extends BaseDao<ProProductPropertyKey,ProProductProperty> implements ProProductPropertyDao{
    @Override
    public String getMapperNameSpace() {
        return ProProductPropertyDao.class.getName();
    }

    @Override
    public int insertByBatch(int spuId, List<ProProductProperty> properties) {
        for (ProProductProperty property:properties){
            property.setSpuId(spuId);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", properties);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertByBatch",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatch: {}", properties.toString(), e);
        }
    }

    @Override
    public int updateBySpuAndPropId(int spuId, int propId, String valueId,String value) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("spuId",spuId);
        map.put("propId",propId);
        if(valueId!=null){
            map.put("valueId",valueId);
        }
        if(value!=null){
            map.put("value",value);
        }

        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateBySpuAndPropId", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateBySpuAndPropId: {}", map.toString(), e);
        }
    }
}