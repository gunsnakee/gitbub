package com.meiliwan.emall.base.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.meiliwan.emall.base.bean.BaseStationFare;
import com.meiliwan.emall.base.bean.BaseTransportPrice;
import com.meiliwan.emall.base.client.StationFareClient;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.base.dao.BaseStationFareDao;
import com.meiliwan.emall.base.dao.BaseTransportAreaPayDao;
import com.meiliwan.emall.base.dao.BaseTransportPriceDao;
import com.meiliwan.emall.base.dto.TransportAreaAndCOD;
import com.meiliwan.emall.base.dto.TransportAreaDTO;
import com.meiliwan.emall.base.dto.TransportPriceDTO;
import com.meiliwan.emall.base.dto.TransportPriceTip;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class BaseTransportPriceService extends DefaultBaseServiceImpl{

	@Autowired
	private BaseTransportPriceDao baseTransportPriceDao;
	@Autowired
	private BaseStationFareDao baseStationFareDao;
	@Autowired
	private BaseTransportAreaPayDao baseTransportAreaPayDao;
	
	/**
	 * 关联全局运费
	 * @param resultObj
	 * @param areaCode
	 */
	public void findUnionStationFareByAreaCode(JsonObject resultObj,String areaCode){
		
		if(StringUtils.isBlank(areaCode)){
			throw new ServiceException("areaCode can not be null");
		}
		BaseTransportPrice price = baseTransportPriceDao.getEntityById(areaCode);
		if(price==null){
			JSONTool.addToResult(new JsonObject(), resultObj);
			return ;
		}
		TransportPriceTip tip = new TransportPriceTip();//TransportPriceClient..getDTOByAraeCode(addr.getCountyCode());
		BaseStationFare unified =  baseStationFareDao.getEntityById(BaseStationFare.DEFAULT_ID);
	    if(price.unifiedTrue()){//全局
	    		tip.setAreaCode(price.getAreaCode());
	    		tip.setPredictTimeMin(price.getPredictTimeMin());
	    		tip.setPredictTimeMax(price.getPredictTimeMax());
	    	 	tip.setBaseStationFare(unified);
	    	 	tip.setMessage(unified.getMessage());
	    }else{
		    	tip.setBaseTransportPrice(price);
		    	tip.setMessage(unified.getMessage());
	    }
		JSONTool.addToResult(tip, resultObj);
	}
	
	/**
	 * 地区优先
	 * @param resultObj
	 * @param parentCode
	 */
	public void getListAreaFirstByParentCode(JsonObject resultObj,String parentCode){
		List<TransportPriceDTO> list = new ArrayList<TransportPriceDTO>();
		if(StringUtils.isBlank(parentCode)){
			JSONTool.addToResult(list, resultObj);
			return ;
		}
		list = baseTransportPriceDao.getListAreaFirstByParentCode(parentCode);
		JSONTool.addToResult(list, resultObj);
	}
	
	public void page(JsonObject resultObj,PageInfo pageInfo){
		if(pageInfo==null){
			throw new ServiceException("pageinfo can not be null");
		}
		PagerControl<TransportPriceTip> pc = baseTransportPriceDao.page(pageInfo);
		JSONTool.addToResult(pc, resultObj);
	}
	
	public void delAddPrice(JsonObject resultObj,String[] areaCodes,BaseTransportPrice bean){

		if (areaCodes == null || areaCodes.length == 0) {
			throw new ServiceException("the areaCode  is null");
		}
		if (bean == null) {
			throw new ServiceException(
					"the BaseTransportPrice  is null");
		}
		
		baseTransportPriceDao.deleteByAreaCodes(areaCodes);
		for (String areaCode : areaCodes) {
			bean.setAreaCode(areaCode);
			baseTransportPriceDao.insert(bean);
		}
		
	}
	
	/**
	 * 
	 * @param resultObj
	 * @param pageInfo
	 */
	public void getTransportPrice(JsonObject resultObj, String countyCode,double orderSaleAmount) {
		if(StringUtils.isBlank(countyCode)){
			JSONTool.addToResult(new BigDecimal(-1), resultObj);
			return;
		}

		BaseTransportPrice transPrice = baseTransportPriceDao.getEntityById(countyCode);
		BigDecimal price = null;
		if (transPrice == null){
			JSONTool.addToResult(new BigDecimal(-1), resultObj);
			return;
		}
		if(!transPrice.unifiedTrue()) {
			price = getIndependentPrice(transPrice,orderSaleAmount);
		}else{
			price = getUnifiedPrice(orderSaleAmount);
		}
		JSONTool.addToResult(price, resultObj);
	}
	
	/**
	 * 全局
	 * @param totalAmout
	 * @return
	 */
	private BigDecimal getUnifiedPrice(double totalAmout) {
		BaseStationFare fare = baseStationFareDao.getEntityById(BaseStationFare.DEFAULT_ID);
		if(fare.isFull()){
			//如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
			int result = NumberUtil.compareTo(fare.getFullFreeLimit().doubleValue(), totalAmout);
			if(result<=0){
				return new BigDecimal(0);
			}else{
				return fare.getNotFullFreePrice();
			}
			
		}
		if(fare.isFixed()){
			return fare.getFixedPrice();
		}
		if(fare.isFree()){
			return new BigDecimal(0);
		}
		return new BigDecimal(-1);
	}
	
	private BigDecimal getIndependentPrice(BaseTransportPrice fare,double totalAmout){
		
		if(fare.isFull()){
			//如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
			int result = NumberUtil.compareTo(fare.getFullFreeLimit().doubleValue(), totalAmout);
			if(result<=0){
				return new BigDecimal(0);
			}else{
				return fare.getNotFullFreePrice() ;
			}
			
		}
		if(fare.isFixed()){
			return fare.getFixedPrice();
		}
		if(fare.isFree()){
			return new BigDecimal(0);
		}
		return new BigDecimal(-1);
	}
	
	/**
	 * 先取得配送地区，在根据配送地区判断是否支持货到付款
	 * 
	 * @param resultObj
	 * @param dto
	 */
	public void getTransportAreaAndCODByPid(JsonObject resultObj,
			String parentCode) {
		if (StringUtils.isEmpty(parentCode)) {
			throw new ServiceException("parentCode can not be null");
		}

		List<TransportPriceDTO> list = baseTransportPriceDao.getListPriceFirstByParentCode(parentCode);
		
		TransportAreaDTO dto = new TransportAreaDTO();
		// 再查询货到付款区域
		dto.setParentCode(parentCode);
		dto.setCashOnDelivery(Constants.BASE_CASH_ON_DELIVER_YES);
		dto.setState(Constants.STATE_VALID);
		// 查询货到付款地区
		List<TransportAreaDTO> listCod = baseTransportAreaPayDao
				.getTansportAreaByPid(dto);
		
		List<TransportAreaAndCOD> result = new ArrayList<TransportAreaAndCOD>();
		for (TransportPriceDTO transArea : list) {
			TransportAreaAndCOD transcod = new TransportAreaAndCOD();
			transcod.setBaseTransportPrice(transArea);
			transcod.setAreaName(transArea.getAreaName());
			for (int j=0;j< listCod.size();j++) {
				if (transArea.getAreaCode().equals(listCod.get(j).getAreaCode())) {
					transcod.setCODYes();
					listCod.remove(j);
				}
			}
			result.add(transcod);
		}
		JSONTool.addToResult(result, resultObj);
	}
	
	public void del(JsonObject resultObj, String areaCode) {
		if(StringUtils.isBlank(areaCode)){
			throw new ServiceException("areaCode can not be null");
		}
		baseTransportPriceDao.delete(areaCode);
		
	}
}
