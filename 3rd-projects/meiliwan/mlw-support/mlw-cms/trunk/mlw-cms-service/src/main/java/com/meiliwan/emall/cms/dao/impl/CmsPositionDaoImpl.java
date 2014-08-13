package com.meiliwan.emall.cms.dao.impl;

import com.meiliwan.emall.cms.bean.CmsPosition;
import com.meiliwan.emall.cms.dao.CmsPositionDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pengwenle
 * Date: 13-6-18
 * Time: 下午3:56
 */
@Repository
public class CmsPositionDaoImpl extends BaseDao<Integer,CmsPosition> implements CmsPositionDao {

    @Override
    public String getMapperNameSpace() {
        return CmsPositionDao.class.getName();
    }

    @Override
    public List<CmsPosition> getByBlockId(int blockId, int pageId,boolean isManiDataSource) {
        CmsPosition entity=new CmsPosition();
        entity.setBlockId(blockId);
        entity.setPageId(pageId);

        return this.getListByObj(entity,null,null," order by block_id",isManiDataSource);
    }
}
