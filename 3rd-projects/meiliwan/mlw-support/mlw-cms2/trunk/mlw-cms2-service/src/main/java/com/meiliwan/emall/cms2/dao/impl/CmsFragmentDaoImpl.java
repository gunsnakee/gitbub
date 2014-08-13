package com.meiliwan.emall.cms2.dao.impl;

import com.meiliwan.emall.cms2.bean.CmsFragment;
import com.meiliwan.emall.cms2.dao.CmsFragmentDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-24
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CmsFragmentDaoImpl extends BaseDao<Integer,CmsFragment> implements CmsFragmentDao {
    @Override
    public String getMapperNameSpace() {
        return CmsFragmentDao.class.getName();
    }
}
