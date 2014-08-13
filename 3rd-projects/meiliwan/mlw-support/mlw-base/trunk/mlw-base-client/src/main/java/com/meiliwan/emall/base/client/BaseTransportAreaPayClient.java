package com.meiliwan.emall.base.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.AreaGrade;
import com.meiliwan.emall.base.bean.AreaPayKey;
import com.meiliwan.emall.base.bean.BaseTransportAreaPay;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.base.dto.TransportAreaDTO;
import com.meiliwan.emall.base.dto.TransportLimit;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserRecvAddr;
import com.meiliwan.emall.service.BaseService;


/**
 * 物流区域服务状况
 * @author yinggao.zhuo
 * @date 2013-6-4
 */
public class BaseTransportAreaPayClient {
	
	public static final String TRANS_COMPANY_FINDBYID = "baseTransportCompanyService/findById";
	public static final String TRANS_COMPANY_ADD = "baseTransportCompanyService/add";
	public static final String TRANS_COMPANY_DEL = "baseTransportCompanyService/del";
	public static final String TRANS_COMPANY_UPDATE = "baseTransportCompanyService/update";
	public static final String TRANS_COMPANY_PAGE = "baseTransportCompanyService/page";

	public static final String TRANS_AREA_PAY_FINDBYID = "baseTransportAreaPayService/findById";
	public static final String TRANS_AREA_PAY_ADD = "baseTransportAreaPayService/add";
	public static final String TRANS_AREA_PAY_DEL = "baseTransportAreaPayService/delete";
	public static final String TRANS_AREA_PAY_UPDATE = "baseTransportAreaPayService/update";
	public static final String TRANS_AREA_PAY_GETIDEXISTS = "baseTransportAreaPayService/getAreaIdExists";
	public static final String TRANS_AREA_PAY_ADDORUPDATE = "baseTransportAreaPayService/addOrUpdate";
	public static final String TRANS_AREA_PAY_GETAREASERVICELIST = "baseTransportAreaPayService/pageTransportAreaServe";	//默认物流的ID

	
	private static final String GETPRICEBYTRANSPORTDTO = "baseTransportAreaPayService/getTransportPrice";
	private static final String GETTANSPORTAREABYPID = "baseTransportAreaPayService/getTansportAreaByPid";
	private static final String GETAREABYPID = "baseTransportAreaPayService/getAreaByPid";
	private static final String GETFOURAREACODBYPID = "baseTransportAreaPayService/getFourAreaCODByPid";
	private static final String FINDBYPK = "baseTransportAreaPayService/findByPK";
	private static final String GETALLAREAPAYDETAIL = "baseTransportAreaPayService/getAllAreaPayDetail";
	private static final String SELECTBYAREACODE = "baseTransportAreaPayService/selectByareaCode";
	
	private static String TRANSPORT_PRICE_MIN="transport_price_min";
	private static String TRANSPORT_PRICE_MAX="transport_price_max";
	
	/**
     * 取得运费范围
     * @return
     */
    public static TransportLimit getTransportLimit() throws ServiceException{
    		
    		String min = BaseSysConfigServiceClient.getSysValueSysConfigByCode(TRANSPORT_PRICE_MIN);
    		String max = BaseSysConfigServiceClient.getSysValueSysConfigByCode(TRANSPORT_PRICE_MAX);
    		TransportLimit limit = new TransportLimit();
    		if (StringUtils.isEmpty(min)) {
			throw new ServiceException("can not get the min price");
		} else {
			limit.setPriceMin(Double.parseDouble(min));
		}
		if (StringUtils.isEmpty(max)) {
            throw new ServiceException("can not get the max price");
        }else{
        		limit.setPriceMax(Double.parseDouble(max));
    		}
    		return limit;
    }
    
	public static boolean isSupportCOD(String countyCode){
		if(StringUtils.isEmpty(countyCode)){
			return false;
		}
		BaseTransportAreaPay pay = new BaseTransportAreaPay();
		pay.setAreaCode(countyCode);
		pay.setCashOnDelivery(Constants.COD_VALID);
		pay.setState(Constants.STATE_VALID);
		List<BaseTransportAreaPay>  list = BaseTransportAreaPayClient.selectByareaCode(pay);
		if(list.size()>0){
			return true;
		}
		return false;
	}
	
	public static boolean isSupportCOD(UserRecvAddr addr) {
		if (addr == null) {
			return false;
		}
		BaseTransportAreaPay pay = new BaseTransportAreaPay();
		pay.setAreaCode(addr.getCountyCode());
		pay.setCashOnDelivery(Constants.COD_VALID);
		List<BaseTransportAreaPay> list = BaseTransportAreaPayClient
				.selectByareaCode(pay);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 品牌增加
	 * @throws Exception 
	 */
    public static void add(BaseTransportAreaPay bean) {
    	
		IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
		        JSONTool.buildParams(TRANS_AREA_PAY_ADD,  bean));
    }
    
    public static void delete(AreaPayKey key) {
    	
		IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
		        JSONTool.buildParams(TRANS_AREA_PAY_DEL,key));
    }

    public static void update(BaseTransportAreaPay bean) {
    	
		IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
		            JSONTool.buildParams(TRANS_AREA_PAY_UPDATE,  bean));
    }

	public static BaseTransportAreaPay findById(int id)  {
		// TODO Auto-generated method stub
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
		        JSONTool.buildParams(TRANS_AREA_PAY_FINDBYID,  id));
		JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();
		
		return new Gson().fromJson(jbean, BaseTransportAreaPay.class);
	}
	
	public static List<Integer> getAreaIdExists(List<Integer> ids){
		
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
		        JSONTool.buildParams(TRANS_AREA_PAY_GETIDEXISTS,  ids));
		
		return new Gson().fromJson(obj, new TypeToken<List<Integer>>() {
        }.getType());
	}

	/**
	 * 后台运费和货到付款设置
	 * @param allId
	 * @param bean
	 */
	public static void addOrUpdate(String[] allId, BaseTransportAreaPay bean) {
		// TODO Auto-generated method stub
		IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams(TRANS_AREA_PAY_ADDORUPDATE, allId,bean));
	}
	
	public static PagerControl<TransportAreaDTO> pageTransportAreaServe(PageInfo pageInfo){
		
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams(TRANS_AREA_PAY_GETAREASERVICELIST,  pageInfo));
		
		JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();
		
		return new Gson().fromJson(jbean, new TypeToken<PagerControl<TransportAreaDTO>>() {
        }.getType());
		
	}


	
	
	
	/**
	 * 根据区域父ID取得列表,左链接,返回已设置的区域
	 * @param dto
	 * @return
	 */
	public static List<TransportAreaDTO> getTansportAreaByPid(TransportAreaDTO dto){
		
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams(GETTANSPORTAREABYPID,  dto));
		
		JsonElement array = obj.get(BaseService.RESULT_OBJ);
		
		return new Gson().fromJson(array, new TypeToken<List<TransportAreaDTO>>() {
        }.getType());
	}
	
	/**
	 * 根据区域父ID取得列表,返回所有区域,已设置的区域,对象里含有是否支持货到付款
	 * @param dto
	 * @return
	 */
	public static List<TransportAreaDTO> getAreaByPid(TransportAreaDTO dto){
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams(GETAREABYPID,  dto));
		
		JsonElement array = obj.get(BaseService.RESULT_OBJ);
		
		return new Gson().fromJson(array, new TypeToken<List<TransportAreaDTO>>() {
        }.getType());
	}
	
	/**
	 * 一次取得所有的省市县
	 * 分为下面三个对象
	 *  List<TransportAreaDTO> province = new ArrayList<TransportAreaDTO>();
	 *		Map<String,List<TransportAreaDTO>> city = new HashMap<String,List<TransportAreaDTO>>();
	 *		Map<String,List<TransportAreaDTO>> county = new HashMap<String,List<TransportAreaDTO>>();
	 *		
	 * @return
	 */
	public static AreaGrade getAllArea(){
		
		TransportAreaDTO dto  = new TransportAreaDTO();
		dto.setTransCompanyId(Constants.DEFAULT_TRANS_ID);
		
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams(GETTANSPORTAREABYPID,  dto));
		
		JsonElement array = obj.get(BaseService.RESULT_OBJ);
		
		List<TransportAreaDTO> pages = new Gson().fromJson(array, new TypeToken<List<TransportAreaDTO>>() {
        }.getType());
		
		List<TransportAreaDTO> province = new ArrayList<TransportAreaDTO>();
		Map<String,List<TransportAreaDTO>> city = new HashMap<String,List<TransportAreaDTO>>();
		Map<String,List<TransportAreaDTO>> county = new HashMap<String,List<TransportAreaDTO>>();
		
		for (TransportAreaDTO bean : pages) {
			if(bean.getAreaGrade().equals(3)){
				List<TransportAreaDTO> countys = county.get(bean.getParentCode());
				if(countys==null){
					List<TransportAreaDTO> cl = new ArrayList<TransportAreaDTO>();
					cl.add(bean);
					county.put(bean.getParentCode(), cl);
				}else{
					countys.add(bean);
				}
				continue;
			}
			if(bean.getAreaGrade().equals(2)){
				List<TransportAreaDTO> citys = city.get(bean.getParentCode());
				if(citys==null){
					List<TransportAreaDTO> cl = new ArrayList<TransportAreaDTO>();
					cl.add(bean);
					city.put(bean.getParentCode(), cl);
				}else{
					citys.add(bean);
				}
				continue;
			}
			if(bean.getAreaGrade().equals(1)){
				province.add(bean);
				continue;
			}
		}
		
		AreaGrade areaGrade = new AreaGrade();
		areaGrade.setProvince(province);
		areaGrade.setCity(city);
		areaGrade.setCounty(county);
		return areaGrade;
	}
	
	
	
	/**
	 * 根据区域父ID取得列表,返回所有区域,已设置的区域,对象里含有是否支持货到付款
	 * @param dto
	 * @return
	 */
	public static List<TransportAreaDTO> getAllAreaPayDetail(TransportAreaDTO dto){
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams(GETALLAREAPAYDETAIL,  dto));
		
		JsonElement array = obj.get(BaseService.RESULT_OBJ);
		
		return new Gson().fromJson(array, new TypeToken<List<TransportAreaDTO>>() {
		}.getType());
	}

	public static List<TransportAreaDTO> getFourAreaCODByPid(
			TransportAreaDTO dto) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams(GETFOURAREACODBYPID,  dto));
		
		JsonElement array = obj.get(BaseService.RESULT_OBJ);
		
		return new Gson().fromJson(array, new TypeToken<List<TransportAreaDTO>>() {
        }.getType());
	}
	
	public static List<BaseTransportAreaPay> selectByareaCode(
			BaseTransportAreaPay pay) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams(SELECTBYAREACODE,  pay));
		
		JsonElement array = obj.get(BaseService.RESULT_OBJ);
		
		return new Gson().fromJson(array, new TypeToken<List<BaseTransportAreaPay>>(){}.getType());
	}
	
	public static BaseTransportAreaPay getByBean(BaseTransportAreaPay bean){
		
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams(SELECTBYAREACODE,  bean));
		
		JsonElement array = obj.get(BaseService.RESULT_OBJ);
		
		return new Gson().fromJson(array, new TypeToken<List<BaseTransportAreaPay>>(){}.getType());
		
	}
}
