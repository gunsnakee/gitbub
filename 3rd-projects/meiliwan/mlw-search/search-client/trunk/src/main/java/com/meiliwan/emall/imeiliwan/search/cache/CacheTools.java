package com.meiliwan.emall.imeiliwan.search.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.pms.bean.ProBrand;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.ProPlace;
import com.meiliwan.emall.pms.bean.ProProperty;
import com.meiliwan.emall.pms.bean.ProPropertyValue;
import com.meiliwan.emall.pms.bean.ProStore;
import com.meiliwan.emall.pms.client.ProBrandClient;
import com.meiliwan.emall.pms.client.ProCategoryClient;
import com.meiliwan.emall.pms.client.ProPlaceClient;
import com.meiliwan.emall.pms.client.ProPropertyClient;
import com.meiliwan.emall.pms.client.ProStoreClient;
import com.meiliwan.emall.pms.dto.PropertyValueList;
import com.meiliwan.emall.search.common.ScheduledExecutor;

/**
 * JVM缓存，定期更新;
 * detail为string[]类型 {id, 名, 值}
 * 缓存找不到会直接调用client
 * 
 * @author lgn-mop
 *
 */
public class CacheTools implements Runnable{

	public final static int LOAD_INTERVAL = 60 * 1000;

	static MLWLogger logger = MLWLoggerFactory.getLogger(CacheTools.class);




	class ProBrandCache {

		private volatile Map<Integer, String[]> flatFormatPB = new HashMap<Integer, String[]>() ;
		public synchronized String refreshCache() {
			try{
				List<ProBrand> tempList = ProBrandClient.getAll();
				Map<Integer, String[]> tmpMap = new HashMap<Integer, String[]>();
				for(ProBrand pb : tempList){
					tmpMap.put(pb.getBrandId(), new String[]{pb.getBrandId() + "", "品牌",pb.getName()});
				}
				flatFormatPB = tmpMap;
				return "ok";
			}catch (Exception e){
				logger.error(e, "ProBrandCache", "");
				return "null";
			}
		}

	}

	class ProStoreCache {

		private volatile Map<String, String[]> enMap = new HashMap<String, String[]>() ;


		public synchronized String refreshCache() {
			try{
				PageInfo page = new PageInfo();
				page.setPagesize(100000);
				PagerControl<ProStore> proStoreByObjAndPage = ProStoreClient.getProStoreByObjAndPage(new ProStore(), page);
				List<ProStore> entityList = proStoreByObjAndPage.getEntityList();

				Map<String, String[]> tmp1 = new HashMap<String, String[]>() ;

				for(ProStore store : entityList){
					String[] detail = new String[]{store.getId() + "", store.getEnName(), store.getStoreName()};
					tmp1.put(detail[1], detail);
				}
				enMap = tmp1;
				return "ok";
			}catch(Exception e){
				logger.error(e, "ProStoreCache", "");;
				return "null";
			}
		}

	}


	class ProPlaceCache{

		private volatile Map<Integer, String[]> flatFormatPB =new HashMap<Integer, String[]>();
		public synchronized String refreshCache() {
			try{
				List<ProPlace> tempList = ProPlaceClient.getAllProPlaceList(new ProPlace());
				Map<Integer, String[]> tmpMap = new HashMap<Integer, String[]>();
				for(ProPlace pb : tempList){
					tmpMap.put(pb.getPlaceId(), new String[]{pb.getPlaceId() + "", "产地",pb.getPlaceName()});
				}
				flatFormatPB = tmpMap;
				return "ok";
			}catch (Exception e){
				logger.error(e, "ProPlaceCache", "");;
				return "null";
			}
		}

	}

	class ProCategoryCache {

		private volatile Map<Integer, String[]> flatFormatPC =new HashMap<Integer, String[]>();
		private volatile List<ProCategory> pcList = new ArrayList<ProCategory>();

		public synchronized String refreshCache(){
			try{
				List<ProCategory> tempList = ProCategoryClient.getAllCategory();
				Map<Integer, String[]> tmpMap = new HashMap<Integer, String[]>();
				for(ProCategory pcat : tempList){
					tmpMap.put(pcat.getId(), new String[]{pcat.getId()+"", "类目名", pcat.getCategoryName()});
				}
				flatFormatPC = tmpMap;
				pcList = tempList;
				return "ok";
			}catch (Exception e){
				logger.error(e, "ProCategoryCache", "");;
				return "null";
			}
		}


		public List<ProCategory>  getCategoryList(){
			return pcList;
		}

	}


	class ProPropertyCache{
		private volatile Map<Integer, String[]> flatFormatPB =new ConcurrentHashMap<Integer, String[]>();

		public synchronized String refreshCache() {
			try{
				PropertyValueList allPPVList = ProPropertyClient.getAllPPVList();
				Map<Integer, String[]> tmp = new HashMap<Integer, String[]>();
				String mid = makeTable(allPPVList, tmp);
				flatFormatPB = tmp;
				return mid;
			}catch (Exception e){
				logger.error(e, "ProPropertyCache", "");;
				return "null";
			}
		}

		private String makeTable(PropertyValueList allPPVList, Map<Integer, String[]> toFill){
			StringBuilder builder = new StringBuilder("property_names=>[[ ");
			HashMap<Integer, String> id2Key = new HashMap<Integer, String>();
			for(ProProperty pp : allPPVList.getProperties()){
				id2Key.put(pp.getId(), pp.getName());
			}
			builder.append(id2Key.toString()).append(" ]]").append(";property_values=>[[ ");
			for(ProPropertyValue pv: allPPVList.getPropertyValues()){
				Integer keyId = pv.getProPropId();
				String keyName = id2Key.get(keyId);
				builder.append("(k:" + keyId +" => v:" + pv.getId() + "=" + pv.getName() + "),");
				if (!StringUtils.isEmpty(keyName) && !StringUtils.isEmpty(pv.getName())){
					String[] detail = new String[]{pv.getId().toString(), keyName, pv.getName()};
					toFill.put(pv.getId(), detail);
				}
			}
			builder.append(" ]]");
			return builder.toString();
		}
	}




	private static CacheTools instance = new CacheTools();

	ProCategoryCache cc = new ProCategoryCache();
	ProPropertyCache pc = new ProPropertyCache();
	ProBrandCache pb = new ProBrandCache();
	ProPlaceCache ppc = new ProPlaceCache();
	ProStoreCache psc = new ProStoreCache();

	private CacheTools(){
		init();

	}

	private Map<String, String> refreshAll(){
		Map<String , String> result = new HashMap<String, String>();
		result.put("category_cache", cc.refreshCache());
		result.put("property_cache", pc.refreshCache());
		result.put("brand_cache", pb.refreshCache());
		result.put("place_cache", ppc.refreshCache());
		result.put("store_cache", psc.refreshCache());
		logger.debug("search cache refreshed!");
		return result;
	}

	private void init(){
		refreshAll();
		ScheduledExecutor.submit(this, LOAD_INTERVAL);
	}

	public static CacheTools get(){
		return instance;
	}


	public static Map<String, String> refreshCache(){
		return get().refreshAll();
	}

	public static String getAllCacheInfo(){
		JsonObject obj = new JsonObject();

		obj.addProperty("category_cache", new Gson().toJson(get().cc.flatFormatPC));
		obj.addProperty("property_cache", new Gson().toJson(get().pc.flatFormatPB));
		obj.addProperty("brand_cache", new Gson().toJson(get().pb.flatFormatPB));
		obj.addProperty("place_cache", new Gson().toJson(get().ppc.flatFormatPB));
		obj.addProperty("store_cache", new Gson().toJson(get().psc.enMap));
		return obj.toString();
	}


	public String getCategoryNameById(int id) {
		String[] detail =  getCategoryDetailById(id);
		return detail == null ? "" : detail[2];
	}

	/**
	 * cache找不到会直接调用client
	 * @param id
	 * @return
	 */
	public String[] getCategoryDetailById(int id) {
		String[] detail = get().cc.flatFormatPC.get(id);
		if (detail == null){
			ProCategory cat = ProCategoryClient.getProCategoryById(id);
			if (cat != null && cat.getId() != null)
				return new String[]{cat.getId() + "", "类目名", cat.getCategoryName()};
			else{
				return null;
			}
		}else{
			return detail;
		}
	}

	public String getBrandNameById(int id){
		String[] detail = getBrandDetailById(id);
		return detail == null ? "" : detail[2];
	}

	public String[] getBrandDetailById(int id){
		String[] detail = get().pb.flatFormatPB.get(id);
		if (detail == null){
			ProBrand brand = ProBrandClient.findById(id);
			if (brand != null && brand.getBrandId() != null)
				return new String[]{brand.getBrandId()+"", "品牌", brand.getName()};
			else{
				return null;
			}
		}else{
			return detail;
		}
	}

	public String getPlaceNameById(int id){
		String[] detail = getPlaceDetailById(id);
		return detail == null ? "" : detail[2];
	}
	public String[] getPlaceDetailById(int id){
		String[] detail = get().ppc.flatFormatPB.get(id);
		if (detail != null){
			ProPlace place = ProPlaceClient.getProPlaceById(id);
			if (place!=null && place.getId() != null)
				detail = new String[]{place.getId() + "", "产地", place.getPlaceName()};
			else {
				return null;
			}
		}
		return detail;

	}
	public String[] getPropDetailById(int id){
		String[] detail = get().pc.flatFormatPB.get(id);
		if (detail == null){
			ProPropertyValue proValue = ProPropertyClient.getPropertyValueById(id);
			if (proValue == null || proValue.getId() == null){
				return null;
			}
			ProProperty proKey = ProPropertyClient.getProPropertyById(proValue.getProPropId());
			if (proKey != null && proKey.getId() != null){
				detail = new String[]{proValue.getId() + "", proKey.getName(), proValue.getName()};
				get().pc.flatFormatPB.put(id, detail);
			}else {
				return null;
			}
		}
		return detail;
	}

	public String[] getStoreDetailByEn(String enName){
		String[] detail = get().psc.enMap.get(enName);
		if (detail == null){
			ProStore store = ProStoreClient.getStoreByEnName(enName);
			if (store != null && store.getId() != null ){
				
				detail = new String[]{store.getId() + "", store.getEnName(), store.getStoreName()};
			}else{
				return null;
			}
		}
		return detail;
	}

	public String getStoreCnNameByEn(String enName){
		String[] detail = getStoreDetailByEn(enName);
		return detail == null ? "" : detail[2];
	}

	@Override
	public void run() {
		
		refreshAll();
	}


}
