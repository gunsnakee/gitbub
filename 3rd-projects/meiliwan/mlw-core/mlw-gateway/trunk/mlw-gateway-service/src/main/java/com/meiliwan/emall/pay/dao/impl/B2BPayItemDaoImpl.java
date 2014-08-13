package com.meiliwan.emall.pay.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pay.bean.PayItem;
import com.meiliwan.emall.pay.dao.B2BPayItemDao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class B2BPayItemDaoImpl extends BaseDao<String, PayItem> implements
		B2BPayItemDao {

	@Override
	public String getMapperNameSpace() {
		return B2BPayItemDaoImpl.class.getName();
	}

	@Override
	public List<PayItem> getPayItemByOrderId(String orderId) {
		return getPayItemByOrderId(orderId, false);
	}

    @Override
    public List<PayItem> getPayItemByOrderId(String orderId, boolean isMainDS) {
        try {
            return getSqlSession().selectList(
                    getMapperNameSpace() + ".getPayItemByOrderId", getShardParam(orderId, orderId, isMainDS));
        } catch (Exception ex) {
            throw new ServiceException("service-" + getMapperNameSpace()
                    + ".getPayItemByOrderId:", ex);
        }
    }

	@Override
	public int delFailPayItemByOrderId(String orderId) {
		try {
			return getSqlSession().delete(
					getMapperNameSpace() + ".delFailPayItemByOrderId", getShardParam(orderId, orderId, true));
		} catch (Exception ex) {
			throw new ServiceException("service-" + getMapperNameSpace()
					+ ".delFailPayItemByOrderId:", ex);
		}
	}

}
