package com.meiliwan.dc.ov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.dc.PageView;
import com.meiliwan.dc.StatWorker;
import com.meiliwan.dc.TableStat;

public class SourceStat implements TableStat{

	List<StatWorker> statWorkers = new ArrayList<StatWorker>();
	 
	public Map<String, int[]> compute(List<PageView> pvs) {
		HashMap<String, int[]> map = new HashMap<String, int[]>();
		
		for(int i = 0; i < statWorkers.size(); i++){
			Map<String, Integer> stat = statWorkers.get(i).stat(pvs);
			for(Entry<String, Integer> entry : stat.entrySet()){
				int[] vs = map.get(entry.getKey());
				if (vs == null){
					vs = new int[statWorkers.size()];
					map.put(entry.getKey(), vs);
				}
				vs[i] += entry.getValue();
			}
		}
		return map;
	}

	public String getSQLFormat() {
		List<Integer> params = new ArrayList<Integer>(statWorkers.size());
		for(int i = 0 ; i < statWorkers.size(); i++){
			params.add(i);
		}
		if (params.isEmpty()){
			throw new RuntimeException("no stat worker yet");
		}
		return "insert into tj_general values('%s','%s','%s',$" + StringUtils.join(params, ", $") + ")"; 
	}

	/**
	 * orders must ,
	 */
	public void addStatWorker(StatWorker... workers) {
		for(StatWorker sw : workers){
			statWorkers.add(sw);
		}
	}

}
