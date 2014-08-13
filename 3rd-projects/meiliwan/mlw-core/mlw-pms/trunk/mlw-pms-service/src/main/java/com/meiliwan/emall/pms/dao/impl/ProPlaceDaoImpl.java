package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProPlace;
import com.meiliwan.emall.pms.dao.ProPlaceDao;
import org.springframework.stereotype.Repository;

/**
 * User: guangdetang
 * Date: 13-6-3
 * Time: 下午1:45
 */
@Repository
public class ProPlaceDaoImpl extends BaseDao<Integer, ProPlace> implements ProPlaceDao {
    @Override
    public String getMapperNameSpace() {
        return ProPlaceDao.class.getName();
    }

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.pms$place;
    }
}
