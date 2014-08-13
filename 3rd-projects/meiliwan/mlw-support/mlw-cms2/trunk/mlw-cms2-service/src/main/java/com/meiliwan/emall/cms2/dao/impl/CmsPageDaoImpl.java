package com.meiliwan.emall.cms2.dao.impl;

import com.google.common.base.Strings;
import com.meiliwan.emall.cms2.bean.CmsPage;
import com.meiliwan.emall.cms2.dao.CmsPageDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-24
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CmsPageDaoImpl extends BaseDao<Integer,CmsPage> implements CmsPageDao {
    @Override
    public String getMapperNameSpace() {
        return CmsPageDao.class.getName();
    }

    @Override
    public List<CmsPage> blurSeachByPageName(String pageName) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".blurSeachByPageName",getShardParam(pageName != null ? pageName : null,pageName,false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".blurSeachByPageName: {}",
                    new String[]{pageName == null ? "" : pageName.toString()}, e);
        }
    }
}
