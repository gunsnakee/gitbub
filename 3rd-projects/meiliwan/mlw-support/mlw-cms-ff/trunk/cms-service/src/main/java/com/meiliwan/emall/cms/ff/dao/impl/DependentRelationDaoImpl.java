package com.meiliwan.emall.cms.ff.dao.impl;

import com.meiliwan.emall.cms.ff.bean.DependentRelation;
import com.meiliwan.emall.cms.ff.dao.DependentRelationDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-18
 * Time: 下午6:27
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DependentRelationDaoImpl extends BaseDao<Integer,DependentRelation> implements DependentRelationDao {
    @Override
    public String getMapperNameSpace() {
        return DependentRelationDao.class.getName();
    }
}
