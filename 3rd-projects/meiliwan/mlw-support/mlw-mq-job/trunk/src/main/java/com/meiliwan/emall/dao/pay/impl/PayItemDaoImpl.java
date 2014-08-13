package com.meiliwan.emall.dao.pay.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.dao.pay.PayItemDao;
import com.meiliwan.emall.pay.bean.PayItem;
@Repository
public class PayItemDaoImpl extends BaseDao<String, PayItem> implements PayItemDao  {

	@Override
	public String getMapperNameSpace() {
		return PayItemDao.class.getName();
	}

	@Override
	public List<PayItem> getPayItemByDateAndPayCode(String startDate,
			String endDate, String payCode) {
		Map<String,String> whereMap = new HashMap<String, String>();
		whereMap.put("startDate", startDate);
		whereMap.put("endDate", endDate);
		whereMap.put("payCode", payCode);
		try {
            return getSqlSession().selectList(
                    getMapperNameSpace() + ".getPayItemByDateAndPayCode",getShardParam(null, whereMap, false));
            
        } catch (Exception ex) {
            throw new ServiceException("PayItemDao-" + getMapperNameSpace()
                    + ".getPayItemByDateAndPayCode:", ex);
        }
	}

	@Override
	public PayItem getPayItemByOrderIdAndPayCode(String orderId, String payCode) {
		Map<String,String> whereMap = new HashMap<String, String>();
		whereMap.put("orderId", orderId);
		whereMap.put("payCode", payCode);
		PayItem payItem = null ;
		try{
			List<PayItem> list = getSqlSession().selectList(
					getMapperNameSpace() + ".getPayItemByOrderIdAndPayCode",getShardParam(null, whereMap, false));
			if(list !=null && list.size()>0){
				payItem = list.get(0);
			}
			
		}catch (Exception e) {
			throw new ServiceException("PayItemDao-" + getMapperNameSpace()
                    + ".getPayItemByOrderIdAndPayCode:", e);
		}
		
		return payItem;
	}

	

}
