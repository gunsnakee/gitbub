package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProCommentClick;
import com.meiliwan.emall.pms.dao.ProCommentClickDao;
import org.springframework.stereotype.Repository;

/**
 * User: guangdetang
 * Date: 13-5-29
 * Time: 下午4:20
 */
@Repository
public class ProCommentClickDaoImpl extends BaseDao<Integer, ProCommentClick> implements ProCommentClickDao {
    @Override
    public String getMapperNameSpace() {
        return ProCommentClickDao.class.getName();
    }
}
