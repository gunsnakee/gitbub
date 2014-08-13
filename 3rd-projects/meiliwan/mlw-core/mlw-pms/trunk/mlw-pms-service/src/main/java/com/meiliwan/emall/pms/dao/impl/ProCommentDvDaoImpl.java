package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProCommentDv;
import com.meiliwan.emall.pms.dao.ProCommentDvDao;
import org.springframework.stereotype.Repository;

/**
 * Created by guangdetang on 13-12-23.
 */
@Repository
public class ProCommentDvDaoImpl  extends BaseDao<Integer, ProCommentDv> implements ProCommentDvDao {
    @Override
    public String getMapperNameSpace() {
        return ProCommentDvDao.class.getName();
    }

}
