package com.meiliwan.emall.mms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserCollect;
import com.meiliwan.emall.mms.dao.UserCollectDao;

import org.springframework.stereotype.Repository;

/**
 * User: guangdetang
 * Date: 13-5-29
 * Time: 下午12:56
 */
@Repository
public class UserCollectDaoImpl extends BaseDao<Integer, UserCollect> implements UserCollectDao {
    public static final String updateProNameByProId = ".updateProNameByProId";
    @Override
    public String getMapperNameSpace() {
        return UserCollectDao.class.getName();
    }

    @Override
    public int updateProNameByProId(UserCollect entity) {
        try{
            return getSqlSession().update(getMapperNameSpace() + updateProNameByProId,
                    getShardParam(entity != null ? entity.getId() : null, entity, true));
        }catch (Exception e){
            throw new ServiceException("service-" + getMapperNameSpace() + updateProNameByProId+": {}", entity.toString(), e);
        }
    }
}
