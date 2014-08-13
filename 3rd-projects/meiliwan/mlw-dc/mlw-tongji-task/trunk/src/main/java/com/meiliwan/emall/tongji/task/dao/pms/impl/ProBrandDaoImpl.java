package com.meiliwan.emall.tongji.task.dao.pms.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProBrand;
import com.meiliwan.emall.tongji.task.dao.pms.ProBrandDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProBrandDaoImpl extends BaseDao<Integer,ProBrand> implements ProBrandDao {

    @Override
    public String getMapperNameSpace() {
        return ProBrandDao.class.getName();
    }

//    @Override
//    public JedisKey getUseJedisCacheKey() {
//        return JedisKey.pms$brand;
//    }

    private static final String SHARD_NAME = "PmsShard";

    @Override
    public String getShardName() {
        return SHARD_NAME;
    }
    
}