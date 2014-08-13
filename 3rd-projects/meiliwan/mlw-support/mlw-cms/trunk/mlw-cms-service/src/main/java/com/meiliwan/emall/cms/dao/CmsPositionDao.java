package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsPosition;
import com.meiliwan.emall.core.db.IDao;

import java.util.List;

public interface CmsPositionDao extends IDao<Integer,CmsPosition> {
    List<CmsPosition> getByBlockId(int blockId, int pageId,boolean isManiDataSource);
}