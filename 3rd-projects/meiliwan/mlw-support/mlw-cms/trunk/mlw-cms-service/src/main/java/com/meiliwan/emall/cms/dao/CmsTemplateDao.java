package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsTemplate;
import com.meiliwan.emall.core.db.IDao;

public interface CmsTemplateDao extends IDao<Integer,CmsTemplate> {

    /**
     * 通过模板ID获取模板
     * @param templateId
     * @return
     */
    CmsTemplate getTmByTmId(int templateId,boolean isManiDataSource);
}