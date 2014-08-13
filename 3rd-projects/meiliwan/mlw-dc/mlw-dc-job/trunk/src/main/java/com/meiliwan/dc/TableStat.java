package com.meiliwan.dc;

import java.util.List;
import java.util.Map;

public interface TableStat {
	/**
	 * key must contain 3 part ->  type1::value::comment
	 * @param pvs
	 * @return
	 */
	public Map<String, int[]> compute(List<PageView> pvs);
	
	public String getSQLFormat();
	
	public void addStatWorker(StatWorker... workers);
}
