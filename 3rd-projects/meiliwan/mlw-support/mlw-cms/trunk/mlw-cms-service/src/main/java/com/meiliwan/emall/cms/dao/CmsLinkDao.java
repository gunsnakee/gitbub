package com.meiliwan.emall.cms.dao;

import com.meiliwan.emall.cms.bean.CmsLink;
import com.meiliwan.emall.core.db.IDao;

import java.util.Date;
import java.util.List;

public interface CmsLinkDao extends IDao<Integer,CmsLink> {
    
    CmsLink get(int linkId, int pageId,boolean isManiDataSource);

    int update(int linkId, int pageId, String linkDesc);

    int del(int linkId, int pageId);

    CmsLink getByPositionId(int positionId, int pageId,boolean isManiDataSource);

    CmsLink getByPositionId(int positionId, Date time, int pageId,boolean isManiDataSource);

    int updateStatus(int linkId, int pageId);

    int updatePos(CmsLink CmsLink, int pageId);

    List<CmsLink> getCarouse(int positionId, Date time, int pageId,boolean isManiDataSource);

    CmsLink getFixByPositionId(int positionId, int pageId,boolean isManiDataSource);

    List<CmsLink> getListByPositionId(int positionId, int pageId, String time,boolean isManiDataSource);

    List<CmsLink> getListByPageId(String time, int pageId,boolean isManiDataSource);
}