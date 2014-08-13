package com.meiliwan.emall.cms.ff.dao.impl;

import com.meiliwan.emall.cms.ff.bean.Page;
import com.meiliwan.emall.cms.ff.dao.PageDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-18
 * Time: 下午6:30
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class PageDaoImpl extends BaseDao<Integer,Page> implements PageDao {
    @Override
    public String getMapperNameSpace() {
        return PageDao.class.getName();
    }
}
