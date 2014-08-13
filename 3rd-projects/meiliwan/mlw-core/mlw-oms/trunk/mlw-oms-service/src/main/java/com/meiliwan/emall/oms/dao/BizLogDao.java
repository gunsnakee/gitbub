package com.meiliwan.emall.oms.dao;

import java.util.List;

import com.meiliwan.emall.core.db.ILogDao;
import com.meiliwan.emall.oms.bean.OmsBizLog;

public interface BizLogDao extends ILogDao<Integer, OmsBizLog> {

	List<OmsBizLog> getListByOrderId(String oid);

}
