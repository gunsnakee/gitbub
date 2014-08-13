package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsTemplatePosition;
import com.meiliwan.emall.core.db.IDao;

import java.util.List;

public interface CmsTemplatePositionDao extends IDao<Integer,CmsTemplatePosition> {

    /**
     * 通过模板ID获取模板
     * @param tmId
     * @return
     */
    List<CmsTemplatePosition> getListByTmId(int tmId,boolean isManiDataSource);
}