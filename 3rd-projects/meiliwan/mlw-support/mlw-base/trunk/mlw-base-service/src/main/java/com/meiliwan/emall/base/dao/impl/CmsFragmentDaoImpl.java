package com.meiliwan.emall.base.dao.impl;

import com.meiliwan.emall.base.bean.CmsFragment;
import com.meiliwan.emall.base.dao.CmsFragmentDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by wenlepeng on 13-8-22.
 */
@Repository
public class CmsFragmentDaoImpl extends BaseDao<Integer,CmsFragment> implements CmsFragmentDao{
    @Override
    public String getMapperNameSpace() {
        return CmsFragmentDao.class.getName();
    }

    /**
     * 分库分表名称
     *
     * @return
     */
    @Override
    public String getShardName() {
        return "CmsShard";
    }
}
