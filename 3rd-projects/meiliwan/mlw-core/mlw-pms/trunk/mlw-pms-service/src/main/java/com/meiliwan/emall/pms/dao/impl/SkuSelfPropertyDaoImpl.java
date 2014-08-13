package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.SkuSelfProperty;
import com.meiliwan.emall.pms.dao.SkuSelfPropertyDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-2-20
 * Time: 下午5:26
 */
@Repository
public class SkuSelfPropertyDaoImpl extends BaseDao<Integer,SkuSelfProperty> implements SkuSelfPropertyDao{
    @Override
    public String getMapperNameSpace() {
        return SkuSelfPropertyDao.class.getName();
    }

    @Override
    public int insertByBatch(int proId, List<SkuSelfProperty> properties) {
        List<SkuSelfProperty> skuSelfProperties = new ArrayList<SkuSelfProperty>();
        for (SkuSelfProperty property:properties){
            if (StringUtils.isNotEmpty(property.getSelfPropName())&&StringUtils.isNotEmpty(property.getSelfPropValue())){
                property.setProId(proId);
                skuSelfProperties.add(property);
            }
        }
        if (skuSelfProperties!=null&&skuSelfProperties.size()>0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", skuSelfProperties);
            try {
                return getSqlSession().insert(getMapperNameSpace() + ".insertByBatch",
                        getShardParam(null, map, true));
            } catch (Exception e) {
                throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatch: {}", properties.toString(), e);
            }
        }else {
            return 0;
        }
    }
}
