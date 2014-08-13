package com.meiliwan.emall.oms.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdFavourable;
import com.meiliwan.emall.oms.bean.OrdFavourableLogs;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.constant.BizCode;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.dao.FavourableLogsDao;
import com.meiliwan.emall.oms.dao.OrdFavourableDao;
import com.meiliwan.emall.oms.dao.OrdiDao;

@Service
public class OrdFavourableService extends BaseOrderService{
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(OrdFavourableService.class);
	@Autowired
	private OrdFavourableDao ofDao ;
	@Autowired
	private FavourableLogsDao flogDao ;
	@Autowired
    protected OrdiDao ordiDao;
	
	public void allList(JsonObject resultObj){
		List<OrdFavourable> list = ofDao.getAllEntityObj();
		addToResult(list, resultObj);
	}
	
	public void getById(JsonObject resultObj, String id){
		OrdFavourable of = ofDao.getEntityById(Integer.parseInt(id));
		addToResult(of, resultObj);
	}
	
	public void updateByEntity(JsonObject resultObj, OrdFavourable of){
		int result = ofDao.update(of) ;
		if(result >0 ){
			addToResult(true, resultObj);
		}else{
			addToResult(false, resultObj);
		}
	}
	
	public void getFavLogsByOrderId(JsonObject resultObj, String orderId){
		List<OrdFavourableLogs> list = flogDao.getEntityByOrderId(orderId);
		addToResult(list, resultObj);
	}
	
	public void addFavLog(JsonObject resultObj, OrdFavourableLogs log){
		int result = flogDao.insert(log);
		if(result >0 ){
			addToResult(true, resultObj);
		}else{
			addToResult(false, resultObj);
		}
	}
	
	
	public void updateOrdFavForAdmin(JsonObject resultObj, String orderId, String discountValue){
    	if (orderId == null || StringUtils.isBlank(orderId) || StringUtils.isBlank(discountValue)) {
            logger.warn("OMS-OrdFavourableService-updateOrdFavForAdmin {}", "ord is null", null);
            addToResult(false, resultObj);
            return ;
        }
        //查询订单信息
    	Ord dbOrd = ordDao.getEntityById(orderId, true);
    	if (dbOrd == null) {
        	logger.warn("OMS-OrdFavourableService-updateOrdFavForAdmin {}", orderId, null);
            addToResult(false, resultObj);
            return ;
        }
    	
    	try{
    		Ord neword = new Ord();
	    	double favorableTotalAmount = NumberUtil.formatToDouble((dbOrd.getOrderSaleAmount()*100 - (dbOrd.getOrderSaleAmount() * Double.valueOf(discountValue)))/100) ;
	    	neword.setOrderId(orderId);
	    	neword.setFavorableTotalAmount(favorableTotalAmount);
	    	neword.setUpdateTime(new Date());
	    	int result = ordDao.update(neword);
	    	if(result<=0){
	    		addToResult(false, resultObj);
	    		return ;
	    	}
	    	List<Ordi> list = ordiDao.getOrdIListByOrdId(orderId, true);
	    	double favAmount = 0.00 ;
	    	int forNum = 0 ;
	    	for(Ordi odi:list){
	    		if(odi != null){
	        		double favorableAmount = NumberUtil.formatToDouble(((odi.getTotalAmount()*100 - (odi.getTotalAmount() * Double.valueOf(discountValue)))/100)/odi.getSaleNum()) ;
	        		if(forNum>0 && forNum == (list.size()-1) ){
	        			odi.setFavorableAmount(NumberUtil.formatToDouble((NumberUtil.add(neword.getFavorableTotalAmount(), -(favAmount)))/odi.getSaleNum()));
	        		}else{
	        			odi.setFavorableAmount(favorableAmount);
	        		}
	        		odi.setUpdateTime(new Date());
	        		ordiDao.update(odi);
	        		favAmount = favAmount + NumberUtil.formatToDouble(favorableAmount * odi.getSaleNum()) ;
	        		forNum ++ ;
	        	}
	    	}
	    	addToResult(true, resultObj);
	    	
    	}catch (ServiceException e) {
            saveOrdException(orderId, "OMS-OrdFavourableService-updateOrdFavForAdmin", e.getMessage(), dbOrd.getUid());
            addToResult(false, resultObj);
        }
    	
    }
	
	/**
     * 保存一个订单头的异常订单
     * @param ordId
     * @param errorCode
     * @param errorMsg
     */
    protected void saveOrdException(String ordId, String errorCode, String errorMsg, Integer uid) {
        saveOrdException(BizCode.ORD.name(), ordId, Constant.ORDER_BILL_TYPE_FORWARD, errorCode, errorMsg, IS.getType(),
                ORDI_COMMITTED.getCode(), GlobalNames.STATE_VALID, uid);
    }
}
