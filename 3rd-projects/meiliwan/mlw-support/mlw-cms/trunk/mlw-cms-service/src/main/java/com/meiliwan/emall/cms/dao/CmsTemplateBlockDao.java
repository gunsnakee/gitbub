package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsTemplateBlock;
import com.meiliwan.emall.core.db.IDao;

import java.util.List;

public interface CmsTemplateBlockDao extends IDao<Integer,CmsTemplateBlock> {

    /**
     * 通过模板ID 获取列表
     * @param tmId
     * @return
     */
    List<CmsTemplateBlock> getListByTmId(int tmId,boolean isManiDataSource);
}