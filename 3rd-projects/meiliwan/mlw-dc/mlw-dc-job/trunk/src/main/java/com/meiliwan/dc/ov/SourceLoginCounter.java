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
import com.meiliwan.emall.commons.web.UserLoginUtil;

public class SourceLoginCounter implements StatWorker{

	SourceChecker checker;
	public SourceLoginCounter(){
		checker = new SourceChecker();
	}
	
	public Map<String, Integer> stat(List<PageView> pvs) {
		SourceInfo source = checker.getSource(pvs);
		Map<String, Integer> result = new HashMap<String, Integer>();
		String lastC = "-";
		int numLogin = 0;
		Set<Integer> userSet = new HashSet<Integer>();
		
		for(int i = source.indexOfList; i < pvs.size(); i++){
			PageView pv = pvs.get(i);
			String ml = pv.getMl();
			// user stat 
			if (!StringUtils.isEmpty(ml) && !ml.endsWith(lastC)) {
				Integer uid = UserLoginUtil.mlToUid(ml);
				if (uid != null) {
					if (!userSet.contains(uid)) {
						numLogin += 1;
						userSet.add(uid);
					}
				}
			}
			
		}
		//finding source
		result.put(source.sourceSite, numLogin);
		return result;
	}

}
