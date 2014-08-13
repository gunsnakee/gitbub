package com.meiliwan.recommend.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;
import com.meiliwan.search.common.Constants;

import gnu.trove.map.hash.TIntFloatHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntFloatProcedure;

/**
 * 可同时维持N个关系矩阵， 下标从0开始
 * @author lgn-mop
 *
 */
public class ItemItemRelations {
	
	public final static String FILEPATH = "itemitem.relations";
	
	private static class CompactSort implements Comparator<Long>{

		public int compare(Long o1, Long o2) {
			float w1 = extractWeight(o1);
			float w2 = extractWeight(o2);
			if (w2 > w1){
				return -1;
			}else if (w2 < w1){
				return 1;
			}
			return 0;
		}
		
	}
	
	List<TIntObjectHashMap<long[]>> itemRelateds = null;
	
	int typeMax = 0;
	
	public ItemItemRelations(String dir) throws IOException, ClassNotFoundException{
		if (dir.startsWith(Constants.INDEX_DIR))
			this.loadFromDir(dir);
		else {
			this.loadFromDir(Constants.INDEX_DIR + "/" + dir);
		}
	}
	
	
	private void loadFromDir(String absDir) throws IOException, ClassNotFoundException{
		File[] blocks = new File(absDir).listFiles();

		for(File f : blocks){
			String fileName = f.getName();
			if (fileName.replaceAll(FILEPATH + "\\.\\d+$", "").length() < fileName.length()){
				String[] parts = fileName.split("\\.");
				int current =  Integer.parseInt(parts[parts.length-1]);
				typeMax = (typeMax > current)? typeMax : current;
			}
		}
		
		//fills null
		itemRelateds = new ArrayList<TIntObjectHashMap<long[]>>(typeMax + 1);
		for(int i = 0 ; i < typeMax + 1; i++){
			itemRelateds.add(null);
		}
		
		for(File f : blocks){
			String fileName = f.getName();
			if (fileName.replaceFirst(FILEPATH + "\\.\\d+$", "").length() < fileName.length()){
				String[] parts = fileName.split("\\.");
				int current =  Integer.parseInt(parts[parts.length-1]);
				TIntObjectHashMap<long[]> itemRelated = new TIntObjectHashMap<long[]>();
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
				itemRelated.readExternal(ois);
				ois.close();
				itemRelateds.set(current, itemRelated);
			}
		}

	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<Long> sumWeightByItems(List<Integer> ids, int idx, int topK){
		if (ids == null || ids.isEmpty() || 
			idx > typeMax || itemRelateds.get(idx) == null){
			return null;
		}
		// only one
		if (ids.size() == 1){
			return this.fetchItemsById(ids.get(0), idx, topK);
		}
		
		float noEntryValue = -10000000f;
		TIntFloatHashMap tmpMap = new TIntFloatHashMap(20, 0.5f , -1, noEntryValue);
		for(Integer id : ids){
			long[] mostRelatedOfTheId = itemRelateds.get(idx).get(id.intValue());
			if (mostRelatedOfTheId != null){
				for(int r = 0 ; r <mostRelatedOfTheId.length; r++){
					int rid = extractId(mostRelatedOfTheId[r]);
					float rweight = extractWeight(mostRelatedOfTheId[r]);
					
					float existWeightSum = tmpMap.get(rid);
					if (existWeightSum == noEntryValue){
						tmpMap.put(rid, rweight);
					}else{
						tmpMap.put(rid, existWeightSum + rweight);
					}
					
				}
			}
		}
		
		final MinMaxPriorityQueue<Long> queue = MinMaxPriorityQueue.orderedBy(new CompactSort()).maximumSize(topK).create();
		
		tmpMap.forEachEntry(new TIntFloatProcedure() {
			
			public boolean execute(int a, float b) {
				long x = ((long)a);
				x <<= 32;
				x |= (long)Float.floatToIntBits(b);
				queue.add(x);
				return true;
			}
		});
		if (queue.isEmpty()){
			return Collections.emptyList();
		}
		ArrayList<Long> result = new ArrayList<Long>(queue.size());
		while(!queue.isEmpty()){
			result.add(queue.pollFirst());		
		}
		return result;
	}
	
	private List<Long> fetchItemsById(int id, int idx, int topK){
		long[] mostRelatedOfTheId = itemRelateds.get(idx).get(id);
		if (mostRelatedOfTheId == null){
			return Collections.emptyList();
		}
		int resultSize = Math.min(mostRelatedOfTheId.length, topK);
		ArrayList<Long> result = new ArrayList<Long>(Math.min(mostRelatedOfTheId.length, topK));
		for(int i = 0 ; i < resultSize; i++){
			result.add(mostRelatedOfTheId[i]);
		}
		return result;
	}
	
	
	public static int extractId(long compact){
		long x = compact & 0xffffffff00000000l;
		x = x >> 32;
		return (int)(x & 0xffffffffl);
	}
	
	public static float extractWeight(long compact){
		long x = compact & 0xffffffffl;
		int f = (int)x;
		return Float.intBitsToFloat(f);
	}
	
	
}
