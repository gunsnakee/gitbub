package com.meiliwan.emall.union.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.union.bean.UnionOrder;
import com.meiliwan.emall.union.dao.UnionOrderDao;

/**
 * 
 * @author lsf
 *
 */
public class UnionOrderService {
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(UnionOrderService.class);
	private UnionOrderDao unionOrderDao;
	
	private static Map<String, String> payMethodMap  = new HashMap<String, String>();
	private static Map<String, Integer> orderStatusMap = new HashMap<String, Integer>();
	private static Map<String, Integer>  payStatusMap = new HashMap<String, Integer>();
	
	static{
		payMethodMap.put("OFF_COD",new String("1"));
		payMethodMap.put("OFF_POS", new String("1"));
		payMethodMap.put("ALIPAY",new String("2"));
		payMethodMap.put("other", new String("9"));
		
		orderStatusMap.put(new String("10"), new Integer("0"));
		orderStatusMap.put(new String("30"), new Integer("0"));
		orderStatusMap.put(new String("40"), new Integer("2"));
		orderStatusMap.put(new String("50"), new Integer("3"));
		orderStatusMap.put(new String("80"), new Integer("4"));
		orderStatusMap.put(new String("60"), new Integer("6"));
		orderStatusMap.put(new String("11"), new Integer("9"));
		
		payStatusMap.put(new String("10"), new Integer("0"));
		payStatusMap.put(new String("30"), new Integer("1"));
		payStatusMap.put(new String("0"), new Integer("2"));
	}
	
	public UnionOrderDao getUnionOrderDao() {
		return unionOrderDao;
	}
	public void setUnionOrderDao(UnionOrderDao unionOrderDao) {
		this.unionOrderDao = unionOrderDao;
	}
	/**
	 * 类型映射
	 * @param type
	 * @param key
	 * @return
	 */
	private String getMapCode(String type,String key){
		if ("payMethod".equalsIgnoreCase(type)) {
			if (payMethodMap.containsKey(key)) {
				return payMethodMap.get(key);
			}
			return payMethodMap.get("other");
		} else if ("orderstatus".equalsIgnoreCase(type)) {
			if (orderStatusMap.containsKey(key)) {
				return String.valueOf(orderStatusMap.get(key).intValue());
			}
			return String.valueOf("10");
		} else if ("payStatus".equalsIgnoreCase(type)) {
			if (payStatusMap.containsKey(key)) {
				return String.valueOf(payStatusMap.get(key).intValue());
			}
			return String.valueOf("0");
		}
		
		return 	"-1";
	}
	private String replace(String str ,int orderStatus,int paystatus,String payMethod ){
		str = StringUtils.replace(str, "_ORDER_STATUS_", getMapCode("orderstatus", String.valueOf(orderStatus)), 1);
		str = StringUtils.replace(str, "_PAY_MENTHOD_", getMapCode("payMethod", payMethod), 1);
		str = StringUtils.replace(str, "_PAY_STATUS_", getMapCode("payStatus", String.valueOf(paystatus)), 1);
		return str;
	}

	/**
	 * 
	 * @param dateStr
	 * @param cpsType
	 * @return
	 */
	public String queryForLinkTech(String dateStr, String cpsType){
		Date queryDate = DateUtil.parseToDate(dateStr, "yyyyMMdd");
		List<UnionOrder> orderInfoList = unionOrderDao.queryForCps(DateUtil.getDateStr(queryDate), cpsType);
		
	    StringBuilder infoBuilder = new StringBuilder();
	    for(UnionOrder orderInfo : orderInfoList){
	    	infoBuilder.append(orderInfo.getQueryInfo());
	    }
	    
	    return infoBuilder.toString();
	}
	
	
	public List<String> queryOrderList(String cpsType, String startTime,String endTime){
		List<UnionOrder> oList = unionOrderDao.queryOrderList(cpsType, DateUtil.parseDate(startTime), DateUtil.parseDate(endTime));
		List<String> strList = new ArrayList<String>();
		
		for (UnionOrder unOrder : oList) {
			strList.add(replace(unOrder.getQueryInfo(), unOrder.getOrderStatus(),unOrder.getPayStatus(), unOrder.getPayMethod()));
		}
		return strList;
	}
	
	public List<String>  queryOrderByOrderStatus(String cpsType, int orderStatus){
		List<UnionOrder>  oList = unionOrderDao.queryOrderListByStatus(cpsType, orderStatus);
		List<String> strList = new ArrayList<String>();
		
		for (UnionOrder unOrder : oList) {
			strList.add(replace(unOrder.getQueryInfo(), unOrder.getOrderStatus(),unOrder.getPayStatus(), unOrder.getPayMethod()));
		}
		return strList;
	}
	
	public String queryOrderById(String cpsType,String orderId){
		UnionOrder unOrder = unionOrderDao.queryOrderById(cpsType, orderId);
		return replace(unOrder.getQueryInfo(), unOrder.getOrderStatus(),unOrder.getPayStatus(), unOrder.getPayMethod());
	}
	
	public List<UnionOrder> get2wCodeListBySource(String sources, String sTime, String eTime, Integer orderStatus){
		Date startTime = StringUtils.isNotBlank(sTime) ? DateUtil.parseDateTime(sTime) : null;
		Date endTime = StringUtils.isNotBlank(eTime) ? DateUtil.parseDateTime(eTime) : null;
		
		return unionOrderDao.get2wCodeListBySource(sources, startTime, endTime, orderStatus);
	}
	
}
