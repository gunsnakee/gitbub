package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProLocation;
import com.meiliwan.emall.pms.dao.ProLocationDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProLocationDaoImpl extends BaseDao<Integer, ProLocation> implements ProLocationDao {
    @Override
    public String getMapperNameSpace() {
        return ProLocationDao.class.getName();
    }

    @Override
    public List<ProLocation> getListByBarCode(String[] codes) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", codes);
        List<ProLocation> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListByBarCode", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByBarCode: {}", codes.toString(), e);
        }
    }

    @Override
    public int updateLocationByBarcode(String barCode, String locationName) {
        Map<String,Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id",barCode);
        map.put("locationName", locationName);
        map.put("updateTime",updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateLocationByBarcode", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateLocationByBarcode: {},{}", new String[]{barCode,locationName}, e);
        }
    }

    @Override
    public ProLocation getLocationByBarCode(String barCode) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",barCode);
        ProLocation location = null;
        try {
            location = getSqlSession().selectOne(getMapperNameSpace() + ".getLocationByBarCode", getShardParam(null, map, false));
            return location;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getLocationByBarCode: {}", barCode, e);
        }
    }
}
