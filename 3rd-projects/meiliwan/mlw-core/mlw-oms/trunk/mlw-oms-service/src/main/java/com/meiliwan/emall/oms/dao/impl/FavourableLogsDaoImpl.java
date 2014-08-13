package com.meiliwan.emall.oms.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdFavourableLogs;
import com.meiliwan.emall.oms.dao.FavourableLogsDao;
@Repository
public class FavourableLogsDaoImpl extends BaseDao<Integer, OrdFavourableLogs> implements FavourableLogsDao {

	@Override
	public String getMapperNameSpace() {
		return FavourableLogsDao.class.getName() ;
	}

	@Override
	public List<OrdFavourableLogs> getEntityByOrderId(String orderId) {
		Map<String,String> whereMap = new HashMap<String, String>();
		whereMap.put("orderId", orderId);
		try {
            return getSqlSession().selectList(
                    getMapperNameSpace() + ".getEntityByOrderId",getShardParam(null, whereMap, false));
            
        } catch (Exception ex) {
            throw new ServiceException("FavourableLogsDao-" + getMapperNameSpace()
                    + ".getEntityByOrderId:", ex);
        }
	}

}
