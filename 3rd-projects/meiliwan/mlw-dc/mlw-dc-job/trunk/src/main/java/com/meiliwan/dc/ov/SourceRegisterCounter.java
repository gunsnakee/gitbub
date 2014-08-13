package com.meiliwan.dc.ov;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.dc.PageView;
import com.meiliwan.dc.StatWorker;
import com.meiliwan.dc.ov.SourceChecker.SourceInfo;

public class SourceRegisterCounter implements StatWorker{

	SourceChecker checker;
	public SourceRegisterCounter(){
		checker = new SourceChecker();
	}
	
	public Map<String, Integer> stat(List<PageView> pvs) {
		SourceInfo source = checker.getSource(pvs);
		Map<String, Integer> result = new HashMap<String, Integer>();
		int numReg = 0;

		for(int i = source.indexOfList; i < pvs.size(); i++){
			if (pvs.get(i).getVisitPage().startsWith("https://passport.meiliwan.com/user/reg/success")){
				numReg += 1;
			}
		}
		//finding source
		result.put(source.sourceSite, numReg);
		return result;
	}

}
