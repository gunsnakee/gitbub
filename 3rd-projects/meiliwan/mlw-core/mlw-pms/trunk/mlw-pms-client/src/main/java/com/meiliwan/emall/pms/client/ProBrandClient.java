package com.meiliwan.emall.pms.client;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.JsonResult;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProBrand;
import com.meiliwan.emall.service.BaseService;

/**
 * 品牌的客户端
 *
 * @author yinggao.zhuo
 * @date 2013-6-4
 */
public class ProBrandClient {

	private static String method(String method) {
		String service = String.format("proBrandService/%s", method);
		return service;
	}

	public static void add(ProBrand brand) throws ServiceException{
		if(brand==null||!brand.isRequiredFieldNotNull()){
			throw new ServiceException("name FirstChar CategoryId can not be null");
		}
		IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
				JSONTool.buildParams(method("add"), brand));
	}

	public static JsonResult del(int id) throws ServiceException{
		if(id<=0){
			throw new ServiceException("id can not be less than 0");
		}
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
				JSONTool.buildParams(method("del"), id));

		return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), JsonResult.class);
	}

	public static void update(ProBrand brand) throws ServiceException{

		IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
				JSONTool.buildParams(method("update"), brand));
	}

	public static ProBrand findById(int brandId) {
		if (brandId <= 0) {
			return null;
		}
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
				JSONTool.buildParams(method("findByBrandId"), brandId));

		return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProBrand.class);

	}

	public static PagerControl<ProBrand> pageByBrand(ProBrand bean,PageInfo pageInfo) throws ServiceException{

		if(bean==null||pageInfo==null){
			return null;
		}
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
				JSONTool.buildParams(method("pageByBrand"), bean, pageInfo));

		return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ),
				new TypeToken<PagerControl<ProBrand>>() {
				}.getType());
	}

	public static List<ProBrand> getAll() throws ServiceException{
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
				JSONTool.buildParams(method("getAll")));
		return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ),
				new TypeToken<List<ProBrand>>() {
				}.getType());

	}

    /**
     * 通过品牌名称查找品牌名称
     *
     * @param brandName
     * @param flag 为true走like查询，false走精确查找
     * @return
     */
    public static List<ProBrand> getListByName(String brandName,boolean flag) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(method("getListByName"), brandName,flag));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ),
                new TypeToken<List<ProBrand>>() {
                }.getType());
    }

}
