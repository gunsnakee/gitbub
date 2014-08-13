package com.meiliwan.emall.account.dao.impl;

import com.meiliwan.emall.account.bean.AccountCard;
import com.meiliwan.emall.account.dao.AccountCardDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountCardDaoImpl extends BaseDao<Integer, AccountCard>
		implements AccountCardDao {

	@Override
	public String getMapperNameSpace() {
		return AccountCardDao.class.getName();
	}


    @Override
    public int freezeMoney(int uid, double freezeMoney) {
        try {
            return getSqlSession().update(getMapperNameSpace() + ".freezeMoney", getShardParam(uid,getMapParamsForAccount(uid,freezeMoney), true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".freezeMoney: freezeMoney:"+freezeMoney+" uid: "+String.valueOf(uid), e);
        }
    }

    @Override
    public int unFreezeMoney(int uid, double freezeMoney) {
        try {
            return getSqlSession().update(getMapperNameSpace() + ".rollBackFromFreeze", getShardParam(uid,getMapParamsForAccount(uid,freezeMoney), true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".rollBackFromFreeze: freezeMoney:"+freezeMoney+" uid: "+String.valueOf(uid), String.valueOf(uid), e);
        }
    }

    private Map<String, Object> getMapParamsForAccount(int uId,double money) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid",uId);
        map.put("money",money);
        return map;
    }

}
