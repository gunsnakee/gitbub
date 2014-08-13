package com.meiliwan.emall.oms.dao;

import java.util.List;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.Retord;
import com.meiliwan.emall.oms.dto.export.RetOrdReportRow;

public interface RetordDao extends IDao<String, Retord> {
	/**
	 * 获取导出退换货列表需要的数据 
	 * @param starTime  起始时间
	 * @param endTime   截止时间
	 * @return
	 */
	List<RetOrdReportRow> getRetOrderExcel(String starTime, String endTime);
}