package com.meiliwan.emall.pms.service;

import java.util.List;

import com.meiliwan.emall.commons.bean.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProBrand;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.ProBrandDao;
import com.meiliwan.emall.pms.dao.ProProductDao;
import com.meiliwan.emall.pms.dto.ProductDTO;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

/**
 * 品牌Service
 * 
 * @author yinggao.zhuo
 * 
 */
@Service
public class ProBrandService extends DefaultBaseServiceImpl {
	
	private MLWLogger logger = MLWLoggerFactory.getLogger(getClass());
	@Autowired
	private ProBrandDao proBrandDao;
	@Autowired
	private ProProductDao proProductDao;
	
	public void findByBrandId(JsonObject resultObj,  int brandId) {
		
		if(brandId<=0){
			return ; 
		}
		ProBrand bean = proBrandDao.getEntityById(brandId);
		JSONTool.addToResult(bean, resultObj);
		return ;
	}

	public int update(JsonObject resultObj,ProBrand proBrand) {
		
		deleteCacheAllBrand();
		int result = proBrandDao.update(proBrand);
		return result;

	}

	/**
	 * 删除全部品牌列表缓存
	 */
	public void deleteCacheAllBrand(){
		try {
			ShardJedisTool.getInstance().del(JedisKey.pms$allbrand, "");
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, "", null);
		}
	}
	/**
	 * 软删除.删除订单
	 * @param resultObj
	 * @return RELEVANCE 已关联到商品 FAIL 更新失败
	 */
	public void del(JsonObject resultObj, int brandId) {
		
		deleteCacheAllBrand();
		ProductDTO b = new ProductDTO();
    		b.setBrandId(brandId);
        int count = proProductDao.getCountByObj(b,null,true);
        if(count>0){
        		JSONTool.addToResult(new JsonResult("RELEVANCE", "the brand relevance to the product", false), resultObj);
        		return ;
        }
		ProBrand brand = new ProBrand();
		brand.setBrandId(brandId);
		brand.setState(Constant.STATE_DEL);
		proBrandDao.update(brand);
		
	}

	/**
	 * 增加商品品牌
	 * 
	 * @return i,成功为1,其他不成功
	 */
	public int add(JsonObject resultObj,ProBrand proBrand) {
		if(proBrand==null||!proBrand.isRequiredFieldNotNull()){
			throw new ServiceException("name FirstChar CategoryId can not be null");
		}
		deleteCacheAllBrand();
		proBrand.setState(Constant.STATE_VALID);
		int result = proBrandDao.insert(proBrand);
		return result;
	}

	public void pageByBrand(JsonObject resultObj,ProBrand brand, PageInfo pageInfo) {
		String order = " order by brand_id desc" ;
		PagerControl<ProBrand> pages = proBrandDao.getPagerByObj(brand, pageInfo, null, order);
		
		JSONTool.addToResult(pages, resultObj);
		
	}

	public void getAll(JsonObject resultObj) {

		ProBrand brand = new ProBrand();
		brand.setState(Constant.STATE_VALID);
		String listss = null;
		try {
			listss = ShardJedisTool.getInstance().get(JedisKey.pms$allbrand, "");
			if(listss!=null){
				List<ProBrand> brands = new Gson().fromJson(listss, new TypeToken<List<ProBrand>>() {
		        }.getType());
				JSONTool.addToResult(brands, resultObj);
				return ;
			}
		} catch (JedisClientException e1) {
			// TODO Auto-generated catch block
			logger.error(e1, listss, null);
			
		}
		
		List<ProBrand> list = proBrandDao.getListByObj(brand);
		
		String strs =new Gson().toJson(list);
		try {
			
			ShardJedisTool.getInstance().set(JedisKey.pms$allbrand, "", strs);
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, list, null);
		}
		JSONTool.addToResult(list, resultObj);
	}

    /**
     * 通过品牌名称查找品牌名称
     * @param resultObj
     * @param brandName
     * @param flag 为true走like查询，false走精确查找
     */
    public void getListByName(JsonObject resultObj,String brandName,boolean flag){
        List<ProBrand> list = null;
        if (flag){
            ProBrand brand = new ProBrand();
            if (StringUtils.isNotEmpty(brandName)){
                brand.setName("%" + brandName.trim() + "%");
            }
            brand.setState((short)1);
            list = proBrandDao.getListByObj(brand);
        }else {
            list = proBrandDao.getListByName(brandName);
        }
        JSONTool.addToResult(list, resultObj);
    }

}