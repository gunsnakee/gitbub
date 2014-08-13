package com.meiliwan.emall.cms.ff.dao.impl;

import com.meiliwan.emall.cms.ff.bean.Fragment;
import com.meiliwan.emall.cms.ff.dao.FragmentDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-18
 * Time: 下午6:28
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class FragmentDaoImpl extends BaseDao<Integer,Fragment> implements FragmentDao {
    @Override
    public String getMapperNameSpace() {
        return FragmentDao.class.getName();
    }
}
