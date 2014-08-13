package com.meiliwan.emall.cms2.dao.impl;

import com.meiliwan.emall.cms2.bean.CmsLink;
import com.meiliwan.emall.cms2.dao.CmsLinkDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-24
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CmsLinkDaoImpl extends BaseDao<Integer,CmsLink> implements CmsLinkDao {
    @Override
    public String getMapperNameSpace() {
        return CmsLinkDao.class.getName();
    }
}
