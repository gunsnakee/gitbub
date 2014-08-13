package com.meiliwan.emall.tongji.task.dao.oms.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.tongji.task.dao.oms.OrdDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrdDaoImpl extends BaseDao<String, Ord> implements OrdDao {

    private static final String SHARD_NAME = "OmsShard";

    @Override
    public String getShardName() {
        return SHARD_NAME;
    }

	@Override
	public String getMapperNameSpace() {
		return OrdDao.class.getName();
	}

}