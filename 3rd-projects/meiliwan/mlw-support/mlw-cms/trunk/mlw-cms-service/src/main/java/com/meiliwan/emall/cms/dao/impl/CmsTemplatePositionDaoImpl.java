package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsTemplatePosition;
import com.meiliwan.emall.cms.dao.CmsTemplatePositionDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: wuzixin
 * Date: 13-6-18
 * Time: 下午3:37
 */
@Repository
public class CmsTemplatePositionDaoImpl extends BaseDao<Integer,CmsTemplatePosition> implements CmsTemplatePositionDao {
    @Override
    public String getMapperNameSpace() {
        return CmsTemplatePositionDao.class.getName();
    }

    @Override
    public List<CmsTemplatePosition> getListByTmId(int tmId,boolean isManiDataSource) {
        CmsTemplatePosition ctp = new CmsTemplatePosition();
        ctp.setTemplateId(tmId);
        return getListByObj(ctp,isManiDataSource);
    }
}
