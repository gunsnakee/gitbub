package com.meiliwan.emall.union.dao;

import java.util.Date;
import java.util.List;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.Ord;
public interface SFUnionOrderDao extends IDao<Integer,Ord> {
	List<Ord> getSForderList(String uids,Date startTime,Date endTime,String orderStatus);

}
