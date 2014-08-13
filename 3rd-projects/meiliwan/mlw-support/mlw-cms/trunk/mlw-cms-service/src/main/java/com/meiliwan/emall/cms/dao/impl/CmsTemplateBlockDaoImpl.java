package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsTemplateBlock;
import com.meiliwan.emall.cms.dao.CmsTemplateBlockDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: wuzixin
 * Date: 13-6-18
 * Time: 下午4:09
 */
@Repository
public class CmsTemplateBlockDaoImpl extends BaseDao<Integer,CmsTemplateBlock> implements CmsTemplateBlockDao {
    @Override
    public String getMapperNameSpace() {
        return CmsTemplateBlockDao.class.getName();
    }

    @Override
    public List<CmsTemplateBlock> getListByTmId(int tmId,boolean isManiDataSource) {
        CmsTemplateBlock ctb = new CmsTemplateBlock();
        ctb.setTemplateId(tmId);
        return getListByObj(ctb,isManiDataSource);
    }
}
