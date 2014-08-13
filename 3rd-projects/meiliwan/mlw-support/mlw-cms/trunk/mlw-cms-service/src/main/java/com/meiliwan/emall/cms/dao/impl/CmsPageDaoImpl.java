package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsPage;
import com.meiliwan.emall.cms.dao.CmsPageDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: pengwenle
 * Date: 13-6-18
 * Time: 下午3:54
 */
@Repository
public class CmsPageDaoImpl extends BaseDao<Integer,CmsPage> implements CmsPageDao {
    @Override
    public String getMapperNameSpace() {
        return CmsPageDao.class.getName();
    }

    @Override
    public CmsPage getCmsPageById(int cmsPageId,boolean isManiDataSource) {
        CmsPage entity=new CmsPage();
        entity.setPageId(cmsPageId);
        List<CmsPage> list=this.getListByObj(entity,new PageInfo(1,1),null,isManiDataSource);
        if(list!=null && list.size()>0)return list.get(0);
        return null;
    }
}
