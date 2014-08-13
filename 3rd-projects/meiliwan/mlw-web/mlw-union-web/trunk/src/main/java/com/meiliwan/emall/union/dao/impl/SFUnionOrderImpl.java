package com.meiliwan.emall.union.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.union.dao.SFUnionOrderDao;;

public class SFUnionOrderImpl extends BaseDao<Integer,Ord> implements SFUnionOrderDao{

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return SFUnionOrderDao.class.getName();
	}

	@Override
	public List<Ord> getSForderList(String uids,Date startTime,Date endTime,String orderStatus) {
		String method=".getSForderList";
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UidList", uids);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("orderStatus", orderStatus);
		return getSqlSession().selectList(getMapperNameSpace()+method, getShardParam(null, map, true));
	}

}
