package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsLink;
import com.meiliwan.emall.cms.dao.CmsLinkDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pengwenle
 * Date: 13-6-18
 * Time: 下午3:51
 */
@Repository
public class CmsLinkDaoImpl extends BaseDao<Integer,CmsLink> implements CmsLinkDao {
    @Override
    public String getMapperNameSpace() {
        return CmsLinkDao.class.getName();
    }

    @Override
    public CmsLink get(int linkId, int pageId,boolean isManiDataSource) {
        CmsLink entity=new CmsLink();
        entity.setId(linkId);
        entity.setPageId(pageId);

        PageInfo pageInfo=new PageInfo(1,1);
        pageInfo.setOrderField("modify_time");
        pageInfo.setOrderDirection("desc");

        List<CmsLink> linkList=this.getListByObj(entity,pageInfo,null,isManiDataSource);
        if(linkList!=null && linkList.size()>0) return  linkList.get(0);
        return null;
    }

    @Override
    public int update(int linkId, int pageId, String linkDesc) {
        Date now=new Date();
        CmsLink entity=new CmsLink();
        entity.setLinkDesc(linkDesc);
        entity.setPageId(pageId);
        entity.setId(linkId);
        entity.setEndTime(DateUtil.convert(now));
        entity.setModifyTime(DateUtil.convert(now));

        try {
            return this.getSqlSession().update(getMapperNameSpace() + ".updateByCondition", getShardParam(entity, entity, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateByCondition: {}", entity == null ? "" : entity.toString());
        }
    }

    @Override
    public int del(int linkId, int pageId) {
        CmsLink entity=new CmsLink();
        entity.setId(linkId);
        entity.setPageId(pageId);

        try {
            return this.getSqlSession().update(getMapperNameSpace() + ".deleteByCondition", getShardParam(entity, getMapParams(entity,null,null,null), true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteByCondition: {}", entity == null ? "" : entity.toString());
        }
    }

    @Override
    public CmsLink getByPositionId(int positionId, int pageId,boolean isManiDataSource) {
        CmsLink entity=new CmsLink();
        entity.setPageId(pageId);
        entity.setPositionId(positionId);

        List<CmsLink> linkList=this.getListByObjSortByMultiField(entity, new PageInfo(1, 1), null, " order by begin_time ,modify_time desc",isManiDataSource);
        if(linkList!=null && linkList.size()>0) return  linkList.get(0);
        return null;

    }

    @Override
    public CmsLink getByPositionId(int positionId, Date time, int pageId,boolean isManiDataSource) {

        CmsLink entity=new CmsLink();
        entity.setPositionId(positionId);
        entity.setPageId(pageId);

        String dataStr= DateUtil.getDateStr(time);

        StringBuilder whereSqlSB=new StringBuilder();
        whereSqlSB.append(" begin_time<=").append("'").append(dataStr).append("'");
        List<CmsLink> linkList=this.getListByObjSortByMultiField(entity, new PageInfo(1, 1), whereSqlSB.toString(), " order by begin_time, modify_time desc",isManiDataSource);
        if(linkList!=null && linkList.size()>0) return linkList.get(0);
        return  null;
    }

    @Override
    public int updateStatus(int linkId, int pageId) {
        CmsLink cmsLink=new CmsLink();
        cmsLink.setLinkId(linkId);
        cmsLink.setPageId(pageId);
        cmsLink.setStatus(1);
        return this.update(cmsLink);
    }

    @Override
    public int updatePos(CmsLink cmsLink, int pageId) {
        if(cmsLink!=null){
          cmsLink.setPageId(pageId);
          cmsLink.setModifyTime(DateUtil.convert(new Date()));
            try {
                return this.getSqlSession().update(getMapperNameSpace() + ".updateByCondition", getShardParam(cmsLink, cmsLink, true) );
            } catch (Exception e) {
                throw new ServiceException("service-" + getMapperNameSpace() + ".updateByCondition: {}", cmsLink == null ? "" : cmsLink.toString());
            }
        }
        return 0;
    }

    @Override
    public List<CmsLink> getCarouse(int positionId, Date time, int pageId,boolean isManiDataSource) {

        CmsLink entity=new CmsLink();
        entity.setPositionId(positionId);
        entity.setPageId(pageId);

        StringBuilder whereSqlSB=new StringBuilder();
        whereSqlSB.append(" end_time>=").append(time);
        return this.getListByObjSortByMultiField(entity, null, whereSqlSB.toString(), " order by begin_time , modify_time desc",isManiDataSource);

    }

    @Override
    public CmsLink getFixByPositionId(int positionId, int pageId,boolean isManiDataSource) {
        CmsLink entity=new CmsLink();
        //entity.setFixFlag(1);
        entity.setPageId(pageId);
        entity.setPositionId(positionId);

        List<CmsLink> linkList=this.getListByObj(entity,new PageInfo(1,1),null," order by modify_time desc",isManiDataSource);
        if(linkList!=null && linkList.size()>0) return linkList.get(0);
        return  null;
    }

    @Override
    public List<CmsLink> getListByPositionId(int positionId, int pageId,String time,boolean isManiDataSource) {
        CmsLink entity=new CmsLink();
        entity.setPositionId(positionId);
        entity.setPageId(pageId);

        /* StringBuilder whereSqlSB=new StringBuilder();
        whereSqlSB.append(" end_time>=").append(time);*/
        return this.getListByObjSortByMultiField(entity, null, null, " order by begin_time ,modify_time desc",isManiDataSource);
    }

    @Override
    public List<CmsLink> getListByPageId(String time, int pageId,boolean isManiDataSource) {
        CmsLink entity=new CmsLink();
        entity.setPageId(pageId);

        StringBuilder whereSqlSB=new StringBuilder();
        whereSqlSB.append(" begin_time <= ").append("'").append(time).append("'");
        return this.getListByObjSortByMultiField(entity, null, whereSqlSB.toString(), " order by begin_time desc ,modify_time desc",isManiDataSource);
    }

    @Override
    public int update(CmsLink entity) {
        try {
            return this.getSqlSession().update(getMapperNameSpace() + ".updateByCondition", getShardParam(entity != null ? entity.getId() : null, getMapParams(entity,null,null,null), true) );
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateByCondition: {}", entity == null ? "" : entity.toString());
        }
    }
}
