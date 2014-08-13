package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsBlock;
import com.meiliwan.emall.core.db.IDao;

import java.util.List;

public interface CmsBlockDao extends IDao<Integer,CmsBlock> {
    //获取页面对应的区块列表
    List<CmsBlock> getByPageId(int pageId);
}