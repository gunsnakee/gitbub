package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsBlock;
import com.meiliwan.emall.cms.dao.CmsBlockDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pengwenle
 * Date: 13-6-18
 * Time: 下午3:47
 */
@Repository
public class CmsBlockDaoImpl extends BaseDao<Integer,CmsBlock> implements CmsBlockDao {
    @Override
    public String getMapperNameSpace() {
        return CmsBlockDao.class.getName();
    }

    @Override
    public List<CmsBlock> getByPageId(int pageId) {
        CmsBlock entity=new CmsBlock();
        entity.setPageId(pageId);
        return this.getListByObj(entity);
    }
}
