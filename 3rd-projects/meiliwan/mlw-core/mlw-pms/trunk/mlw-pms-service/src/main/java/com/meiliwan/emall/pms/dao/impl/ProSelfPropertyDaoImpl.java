package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProSelfProperty;
import com.meiliwan.emall.pms.dao.ProSelfPropertyDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProSelfPropertyDaoImpl extends BaseDao<Integer,ProSelfProperty> implements ProSelfPropertyDao{
    @Override
    public String getMapperNameSpace() {
        return ProSelfPropertyDao.class.getName();
    }

    @Override
    public int insertByBatch(int spuId, List<ProSelfProperty> propertys) {
        List<ProSelfProperty> proProperties = new ArrayList<ProSelfProperty>();
        for (ProSelfProperty property:propertys){
            if (StringUtils.isNotEmpty(property.getSelfPropName())&&StringUtils.isNotEmpty(property.getSelfPropValue())){
                property.setSpuId(spuId);
                proProperties.add(property);
            }
        }
        if (proProperties!=null&&proProperties.size()>0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", proProperties);
            try {
                return getSqlSession().insert(getMapperNameSpace() + ".insertByBatch",
                        getShardParam(null, map, true));
            } catch (Exception e) {
                throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatch: {}", propertys.toString(), e);
            }
        }else {
            return 0;
        }
    }
}