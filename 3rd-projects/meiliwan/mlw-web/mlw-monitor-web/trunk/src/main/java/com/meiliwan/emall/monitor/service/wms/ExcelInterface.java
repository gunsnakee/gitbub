package com.meiliwan.emall.monitor.service.wms;


public interface ExcelInterface<Ord,Pro> {

	void change(int num,String value, OrdSheet<Ord,Pro> sheet);
	/**
	 * 额外处理
	 * @param wmsSheet
	 */
	void extra(OrdSheet<Ord,Pro> wmsSheet);
}
