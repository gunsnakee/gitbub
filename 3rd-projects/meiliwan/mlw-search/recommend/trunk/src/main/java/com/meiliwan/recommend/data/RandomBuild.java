package com.meiliwan.recommend.data;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONObject;

import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.util.BaseConfig;
import com.meiliwan.recommend.common.ItemItemRelations;
import com.meiliwan.recommend.core.CategoryRecommender;
import com.meiliwan.recommend.core.ProductRecommender;
import com.meiliwan.recommend.core.RecommendModule;
import com.meiliwan.search.common.Constants;


/**
 * 随机算法，根据数据库中此时刻有效商品，随机生成关系矩阵。包括列表页和详情页
 * @author lgn-mop
 *
 */
public class RandomBuild {

	static Random r = new Random();

	public void generate(String dirName, int numRelations) throws Exception{
		if (numRelations > 10 || numRelations <= 0)
			numRelations = 2;
		ResultSource rs = new ResultSource.DBSource();
		rs.open(BaseConfig.getValue("product.select"));
		List<Integer> idSet = new ArrayList<Integer>();
		Iterator<ItemResult> it = rs.getItemResultIterator();
		
		while(it.hasNext()){
			ItemResult ir = it.next();
			idSet.add(ir.getId());
		}
		rs.close();
		//----------------------
		String outdir = BaseConfig.getValue("remotedir")  + RecommendModule.getZkServicePath();
		String outProductDir = dirName + "_" + new Random().nextInt(1000);
		while(new File(outdir + "/" + outProductDir).exists()){
			outProductDir = dirName + "_" + new Random().nextInt(1000);
		}
		new File(outdir + "/" + outProductDir).mkdirs();
		ZKClient.get().createIfNotExist(RecommendModule.getZkServicePath());
		for(int n = 0 ; n < numRelations; n++){
			TIntObjectHashMap<long[]> recommender = new TIntObjectHashMap<long[]>();
			System.out.println("all items:  " + idSet.size() );
			for(int i = 0 ; i < idSet.size(); i++){
				HashSet<Integer> ids = new HashSet<Integer>();
				for(int j = 0 ; j < 12; j++){
					ids.add(idSet.get(r.nextInt(idSet.size())));
				}
				//不包含自己ID
				ids.remove(idSet.get(i)); 

				long[] a = new long[ids.size()];
				int j = 0;
				for(Integer id : ids){
					long x = 0;
					x = ((long)id.intValue()) ;
					x <<= 32;
					x |= Float.floatToIntBits(1.0f);
					a[j] = x;
					j += 1;
				}
				recommender.put(idSet.get(i), a);

			}
			System.out.println("---finish "+ n +"----");

			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
					outdir + "/" + outProductDir + "/" + ItemItemRelations.FILEPATH + "." + n)));
			recommender.writeExternal(oos);
			oos.close();
			System.out.println("---write down----");
		}
		
		JSONObject jo = new JSONObject();
		jo.put(Constants.REALCOLLECTION_KEY, outProductDir);
		JSONObject strgyA = new JSONObject();
		strgyA.put(Constants.ABTEST_ABBR, "random");
		strgyA.put(Constants.ABTEST_RATIO, 1.0);
		strgyA.put(Constants.ABTEST_PARAM, new HashMap<String, Double>());
		JSONObject ab = new JSONObject();
		ab.put("A", strgyA);
		jo.put(Constants.ABTEST_STRING, ab);
		if (dirName.equals("product"))
			jo.put("className", ProductRecommender.class.getCanonicalName());
		else if (dirName.equals("category"))
			jo.put("className", CategoryRecommender.class.getCanonicalName());
		String updateSource = BaseConfig.getValue("update.source");
		jo.put("update.source", updateSource);
		ZKClient.get().upsertNode(RecommendModule.getZkServicePath() + "/" + dirName,  jo.toString().getBytes());
		System.out.println("---inform zk----");
		//---------------------
	}

	
	public void generateCategory(String dirName, int numRelations) throws Exception{
		if (numRelations > 10 || numRelations <= 0)
			numRelations = 2;
		ResultSource rs = new ResultSource.DBSource();
		rs.open(BaseConfig.getValue("product.select"));
		List<Integer> idSet = new ArrayList<Integer>();
		
		Iterator<ItemResult> it = rs.getItemResultIterator();
		while(it.hasNext()){
			ItemResult ir = it.next();
			idSet.add(ir.getId());
		}
		rs.close();
		
		rs = new ResultSource.DBSource();
		rs.open(BaseConfig.getValue("product.category"));
		List<Integer> catIdSet = new ArrayList<Integer>();
		it = rs.getItemResultIterator();
		while(it.hasNext()){
			ItemResult ir = it.next();
			catIdSet.add(ir.getId());
		}
		rs.close();
		
		//----------------------
		String outdir = BaseConfig.getValue("remotedir")  + RecommendModule.getZkServicePath();
		String outProductDir = dirName + "_" + new Random().nextInt(1000);
		while(new File(outdir + "/" + outProductDir).exists()){
			outProductDir = dirName + "_" + new Random().nextInt(1000);
		}
		new File(outdir + "/" + outProductDir).mkdirs();
		ZKClient.get().createIfNotExist(RecommendModule.getZkServicePath());
		for(int n = 0 ; n < numRelations; n++){
			TIntObjectHashMap<long[]> recommender = new TIntObjectHashMap<long[]>();
			System.out.println("all cats :  " + catIdSet.size() );
			for(int i = 0 ; i < catIdSet.size(); i++){
				HashSet<Integer> ids = new HashSet<Integer>();
				for(int j = 0 ; j < 12; j++){
					ids.add(idSet.get(r.nextInt(idSet.size())));
				}
				//不包含自己ID

				long[] a = new long[ids.size()];
				int j = 0;
				for(Integer id : ids){
					long x = 0;
					x = ((long)id.intValue()) ;
					x <<= 32;
					x |= Float.floatToIntBits(1.0f);
					a[j] = x;
					j += 1;
				}
				recommender.put(catIdSet.get(i), a);

			}
			System.out.println("---finish "+ n +"----");

			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
					outdir + "/" + outProductDir + "/" + ItemItemRelations.FILEPATH + "." + n)));
			recommender.writeExternal(oos);
			oos.close();
			System.out.println("---write down----");
		}
		
		JSONObject jo = new JSONObject();
		jo.put(Constants.REALCOLLECTION_KEY, outProductDir);
		JSONObject strgyA = new JSONObject();
		strgyA.put(Constants.ABTEST_ABBR, "random");
		strgyA.put(Constants.ABTEST_RATIO, 1.0);
		strgyA.put(Constants.ABTEST_PARAM, new HashMap<String, Double>());
		JSONObject ab = new JSONObject();
		ab.put("A", strgyA);
		jo.put(Constants.ABTEST_STRING, ab);
		if (dirName.equals("product"))
			jo.put("className", ProductRecommender.class.getCanonicalName());
		else if (dirName.equals("category"))
			jo.put("className", CategoryRecommender.class.getCanonicalName());
		String updateSource = BaseConfig.getValue("update.source");
		jo.put("update.source", updateSource);
		ZKClient.get().upsertNode(RecommendModule.getZkServicePath() + "/" + dirName,  jo.toString().getBytes());
		System.out.println("---inform zk----");
		//---------------------
	}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		RandomBuild rb = new RandomBuild();
		assert new Integer(args[0]) >= 1;
		rb.generate("product", new Integer(args[0]));
		rb.generateCategory("category", new Integer(args[0]));
	}

}
