package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsFragment;
import com.meiliwan.emall.cms.dao.CmsFragmentDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by wenlepeng on 13-8-22.
 */
@Repository
public class CmsFragmentDaoImpl extends BaseDao<Integer,CmsFragment> implements CmsFragmentDao {
    @Override
    public String getMapperNameSpace() {
        return CmsFragmentDao.class.getName();
    }

}
