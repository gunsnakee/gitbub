package com.meiliwan.dc;

import java.util.List;
import java.util.Map;

public interface StatWorker {
	/**
	 * whatever return an entry
	 * @param pvs
	 * @return
	 */
	Map<String, Integer> stat(List<PageView>  pvs);
	
	
}
