package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsTemplate;
import com.meiliwan.emall.cms.dao.CmsTemplateDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * User: wuzixin
 * Date: 13-6-18
 * Time: 下午4:07
 */
@Repository
public class CmsTemplateDaoImpl extends BaseDao<Integer,CmsTemplate> implements CmsTemplateDao {
    @Override
    public String getMapperNameSpace() {
        return CmsTemplateDao.class.getName();
    }

    @Override
    public CmsTemplate getTmByTmId(int templateId,boolean isManiDataSource) {
        CmsTemplate ct = new CmsTemplate();
        ct.setTemplateId(templateId);
        return getEntityByObj(ct,isManiDataSource);
    }
}
