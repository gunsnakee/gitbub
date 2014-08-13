package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsTemplateLink;
import com.meiliwan.emall.core.db.IDao;

import java.util.List;

public interface CmsTemplateLinkDao extends IDao<Integer, CmsTemplateLink> {

    /**
     * 通过模板ID 获取列表
     * @param tmId
     * @return
     */
    List<CmsTemplateLink> getListByTmId(int tmId,boolean isManiDataSource);
}