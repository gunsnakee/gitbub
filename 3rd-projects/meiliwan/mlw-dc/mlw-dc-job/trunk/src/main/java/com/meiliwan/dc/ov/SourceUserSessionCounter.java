package com.meiliwan.dc.ov;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.dc.PageView;
import com.meiliwan.dc.StatWorker;
import com.meiliwan.dc.ov.SourceChecker.SourceInfo;

public class SourceUserSessionCounter implements StatWorker{

	SourceChecker checker;
	public SourceUserSessionCounter(){
		checker = new SourceChecker();
	}
	
	public Map<String, Integer> stat(List<PageView> pvs) {
		SourceInfo source = checker.getSource(pvs);
		Map<String, Integer> result = new HashMap<String, Integer>();
		String lastC = "-";
		int numSession = 0;
		Set<String> sessionSet = new HashSet<String>();
		
		for(int i = source.indexOfList; i < pvs.size(); i++){
			PageView pv = pvs.get(i);
			
			// user session stat
			String mc = pv.getMc();
			if (!StringUtils.isEmpty(mc) && !pvs.get(i).getMc().endsWith(lastC)){
				if (!sessionSet.contains(mc)) {
					numSession += 1;
					sessionSet.add(mc);
				}
			}
		}
		//finding source
		result.put(source.sourceSite, numSession);
		return result;
	}

}
