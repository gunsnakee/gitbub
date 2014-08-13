package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProBrand;
import com.meiliwan.emall.pms.dao.ProBrandDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProBrandDaoImpl extends BaseDao<Integer,ProBrand> implements ProBrandDao{

    @Override
    public String getMapperNameSpace() {
        return ProBrandDao.class.getName();
    }

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.pms$brand;
    }

    @Override
    public List<ProBrand> getListByName(String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name",name);
        List<ProBrand> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListByName", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByIdsAndName: {}", name, e);
        }
    }
}