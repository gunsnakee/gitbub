package com.meiliwan.emall.cms.ff.dao.impl;

import com.meiliwan.emall.cms.ff.bean.Block;
import com.meiliwan.emall.cms.ff.dao.BlockDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-18
 * Time: 下午6:25
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BlockDaoImpl extends BaseDao<Integer,Block> implements BlockDao {
    @Override
    public String getMapperNameSpace() {
       return BlockDao.class.getName();
    }
}
