package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsSite;
import com.meiliwan.emall.cms.dao.CmsSiteDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: pengwenle
 * Date: 13-6-18
 * Time: 下午3:58
 */
@Repository
public class CmsSiteDaoImpl extends BaseDao<Integer,CmsSite> implements CmsSiteDao {
    public static final String deleteBySiteId = ".deleteBySiteId";
    @Override
    public String getMapperNameSpace() {
        return CmsSiteDao.class.getName();
    }

    @Override
    public CmsSite getSiteBySId(int siteId,boolean isManiDataSource) {
        CmsSite cs = new CmsSite();
        cs.setSiteId(siteId);
        return getEntityByObj(cs,isManiDataSource);
    }

    @Override
    public int deleteBySiteId(int siteId) {
        try {
            return getSqlSession().delete(getMapperNameSpace() + deleteBySiteId,
                    getShardParam(siteId, siteId, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteBySiteId: {}", ""+siteId, e);
        }
    }
}
