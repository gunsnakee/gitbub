package com.meiliwan.emall.monitor.dao;

import java.util.Date;
import java.util.List;

import com.meiliwan.emall.monitor.bean.UnionOrder;


public interface UnionOrderDao  {

	List<UnionOrder> get2wCodeListBySourceId(String prefixSourceId, Date sTime, Date eTime, Integer orderStatus);
	
}