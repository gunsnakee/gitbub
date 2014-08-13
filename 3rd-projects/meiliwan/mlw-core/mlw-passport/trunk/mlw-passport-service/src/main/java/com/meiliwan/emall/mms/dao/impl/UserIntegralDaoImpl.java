package com.meiliwan.emall.mms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserIntegral;
import com.meiliwan.emall.mms.dao.UserIntegralDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserIntegralDaoImpl extends BaseDao<Integer, UserIntegral> implements
        UserIntegralDao {

	@Override
	public String getMapperNameSpace() {
		return UserIntegralDao.class.getName();
	}

    @Override
    public int updateIntegralBalance(Integer id, Integer balance) {
        UserIntegral integral = new UserIntegral();
        try {
            integral.setId(id);
            integral.setIntegralBalance(balance);
            integral.setUpdateTime(new Date());
            return getSqlSession().update(getMapperNameSpace() + ".updateIntegralBalance",
                    getShardParam(integral != null ? integral.getId() : null, integral, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".update: {}", integral == null ? "" : integral.toString(), e);
        }
    }
}
