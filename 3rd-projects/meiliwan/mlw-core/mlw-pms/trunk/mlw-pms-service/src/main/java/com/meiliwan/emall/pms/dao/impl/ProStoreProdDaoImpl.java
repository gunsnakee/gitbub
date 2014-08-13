package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProStoreProd;
import com.meiliwan.emall.pms.dao.ProStoreProdDao;
import org.springframework.stereotype.Repository;

/**
 * User: yiyou.luo
 * Date: 13-9-23
 */
@Repository
public class ProStoreProdDaoImpl extends BaseDao<Integer, ProStoreProd> implements ProStoreProdDao {
    @Override
    public String getMapperNameSpace() {
        return ProStoreProdDao.class.getName();
    }


}
