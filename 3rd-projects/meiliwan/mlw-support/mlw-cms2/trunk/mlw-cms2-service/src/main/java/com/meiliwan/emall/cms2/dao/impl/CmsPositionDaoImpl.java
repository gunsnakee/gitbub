package com.meiliwan.emall.cms2.dao.impl;

import com.meiliwan.emall.cms2.bean.CmsPosition;
import com.meiliwan.emall.cms2.dao.CmsPositionDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-24
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CmsPositionDaoImpl extends BaseDao<Integer,CmsPosition> implements CmsPositionDao {
    @Override
    public String getMapperNameSpace() {
        return CmsPositionDao.class.getName();
    }

    @Override
    public List<Integer> getAllGroupByBlockId(Integer blockId) {
        try{
            return this.getSqlSession().selectList(getMapperNameSpace() + ".getAllGroupByBlockId", getShardParam(blockId, blockId, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getAllGroupByBlockId: {}", blockId == null ? "" : blockId.toString());
        }
    }
}
