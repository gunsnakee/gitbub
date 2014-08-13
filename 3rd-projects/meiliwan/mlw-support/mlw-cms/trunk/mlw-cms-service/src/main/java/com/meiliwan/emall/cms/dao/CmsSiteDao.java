package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsSite;
import com.meiliwan.emall.core.db.IDao;

public interface CmsSiteDao extends IDao<Integer,CmsSite> {

    /**
     * 通过站点ID获取站点
     * @param siteId
     * @return
     */
    CmsSite getSiteBySId(int siteId,boolean isManiDataSource);

    int deleteBySiteId(int siteId);
}