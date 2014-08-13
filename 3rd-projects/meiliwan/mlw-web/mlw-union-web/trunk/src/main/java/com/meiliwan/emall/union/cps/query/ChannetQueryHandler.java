package com.meiliwan.emall.union.cps.query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.EncryptTools;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.cps.CPSQueryHandler;
import com.meiliwan.emall.union.cps.cookie.ChanetConstant;
import com.meiliwan.emall.union.service.UnionOrderService;

public class ChannetQueryHandler implements CPSQueryHandler {
	
	private final static MLWLogger LOGGER = MLWLoggerFactory.getLogger(ChannetQueryHandler.class.getName());
	
	@Autowired
	private  UnionOrderService unionOrderService ;

	
	private static Map<String, String> payMethodMap  = new HashMap<String, String>();
	private static Map<String, Integer> orderStatusMap = new HashMap<String, Integer>();
	private static Map<String, Integer>  payStatusMap = new HashMap<String, Integer>();
	
	static{
		payMethodMap.put("1",new String("OFF_COD"));
		//payMethodMap.put("1", new String("OFF_POS"));
		payMethodMap.put("2",new String("ALIPAY"));
		payMethodMap.put("9", new String("other"));
		
		orderStatusMap.put(new String("0"), new Integer("10"));
		//orderStatusMap.put(new String("0"), new Integer("30"));
		orderStatusMap.put(new String("2"), new Integer("40"));
		orderStatusMap.put(new String("3"), new Integer("50"));
		orderStatusMap.put(new String("4"), new Integer("80"));
		orderStatusMap.put(new String("6"), new Integer("60"));
		orderStatusMap.put(new String("9"), new Integer("11"));
		
		payStatusMap.put(new String("0"), new Integer("10"));
		payStatusMap.put(new String("1"), new Integer("30"));
		payStatusMap.put(new String("2"), new Integer("0"));
	}

	
	public UnionOrderService getUnionOrderService() {
		return unionOrderService;
	}

	public void setUnionOrderService(UnionOrderService unionOrderService) {
		this.unionOrderService = unionOrderService;
	}

	//	private int getMapCode(String type,String key){
//		if ("payMethod".equalsIgnoreCase(type)) {
//			if (payMethodMap.containsKey(key)) {
//				return payMethodMap.get(key)
//			}
//			return payMethodMap.get("other").intValue();
//		} else if ("orderstatus".equalsIgnoreCase(type)) {
//			if (orderStatusMap.containsKey(key)) {
//				return orderStatusMap.get(key).intValue();
//			}
//			return 0;
//		} else if ("payStatus".equalsIgnoreCase(type)) {
//			if (payStatusMap.containsKey(key)) {
//				return payStatusMap.get(key).intValue();
//			}
//			return 0;
//		}
//		
//		return 	-1;
//	}
	/**
	 * 订单加密MD5方法
	 * sig=md5(user=***&start=***&end=***&orderid=**&unixtime=***&key=***)
	 * */
	private String getEncrySignForOrder(String user, String start,String end,String orderId,long unixTime,String key){
		if (StringUtils.isBlank(user)||StringUtils.isBlank(start)||StringUtils.isBlank(end) || StringUtils.isBlank(orderId)||StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();	
		stringBuilder.append("user=").append(user).append("&");
		stringBuilder.append("start=").append(start).append("&");
		stringBuilder.append("end=").append(end).append("&");
		stringBuilder.append("orderid=").append(orderId).append("&");
		stringBuilder.append("unixtime=").append(unixTime).append("&");
		stringBuilder.append("key=").append(key);

		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	/**
	 * 订单状态MD5加密方法
	 * sig=md5(user=***&start=***&end=***&orderstatus=***&unixtime=***&key=***)
	 * */
	private String getEncrySignForOrderStatus(String user, String start,String end,int orderStatus,long unixTime,String key){
		if (StringUtils.isBlank(user)||StringUtils.isBlank(start)||StringUtils.isBlank(end) || StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();	
		stringBuilder.append("user=").append(user).append("&");
		stringBuilder.append("start=").append(start).append("&");
		stringBuilder.append("end=").append(end).append("&");
		stringBuilder.append("orderstatus=").append(orderStatus).append("&");
		stringBuilder.append("unixtime=").append(unixTime).append("&");
		stringBuilder.append("key=").append(key);

		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	/**
	 * 时间范围查询md5查询信息加密方法
	 * 
	 * */
	private String getEncrySign(String user, String start,String end,long unixTime,String key){
		if (StringUtils.isBlank(user) || StringUtils.isBlank(start) || StringUtils.isBlank(end) || StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("user=").append(user).append("&");
		stringBuilder.append("start=").append(start).append("&");
		stringBuilder.append("end=").append(end).append("&");
		stringBuilder.append("unixtime=").append(unixTime).append("&");
		stringBuilder.append("key=").append(key);

		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	/**
	 * combine list order to json format
	 * */
	private String combineToString(List<String> list,String startTime,String endTime,int totalPage,int currentPage){
		
		if (list == null || list.size() == 0) {
			return "没有请求数据";
		}
		
		StringBuilder orderJson = new StringBuilder();
		StringBuilder queryBuilder = new StringBuilder();
		
		//builder  result
		orderJson.append("[").append("\t");
		for (String string : list) {
			orderJson.append(string).append(",").append("\t");
		}
		orderJson.deleteCharAt(orderJson.length()-2);
		orderJson.append("]");
		
		//build json
		String resultString = orderJson.toString();
		queryBuilder.append("{").append("\t");
		
		queryBuilder.append("\"start_time\":").append("\"").append(startTime).append("\",").append("\t");
		queryBuilder.append("\"end_time\":").append("\"").append(endTime).append("\",").append("\t");
		queryBuilder.append("\"total_page\":").append("\"").append(totalPage).append("\",").append("\t");
		queryBuilder.append("\"current_page\":").append("\"").append(currentPage).append("\",").append("\t");
		queryBuilder.append("\"results\":").append(resultString).append("\t");
		queryBuilder.append("}");
		
		return queryBuilder.toString();
	}
	
	/**
	 * yyyymmddhhmmss to yyyy-MM-dd HH:mm:ss
	 * */
	private String convertToFormat(String time){
		if (StringUtils.isBlank(time) || time.length() != 14) {
			return time;
		}
		StringBuilder date = new StringBuilder(time);
		
		date.insert(12, ":");
		date.insert(10, ":");
		date.insert(8, " ");
		date.insert(6, "-");
		date.insert(4, "-");
		return date.toString();
	}
	
	/**
	 * date  validation 
	 * */
	private boolean isRealFormat(String time){
		if (StringUtils.isBlank(time)) {
			return false;
		}
		
		Timestamp timestamp = DateUtil.parseTimestamp(time);
		if (timestamp == null) {
			return false;
		}
		
		return true;
	}

	/**
	 * url params  validation  
	 * md5  encry method
	 * time range,orderid order_Status
	 * */
	private boolean isValidatetionUrl(HttpServletRequest request){
		String user = request.getParameter("user");
		String startTime = request.getParameter("start");
		String endTime = request.getParameter("end");
		String orderid = request.getParameter("orderid");
		String orderstatus = request.getParameter("orderstatus");
		String unixtime = request.getParameter("unixtime");
		String sig = request.getParameter("sig");
		
		if (StringUtils.isBlank(user) || !isRealFormat(convertToFormat(startTime)) || !isRealFormat(convertToFormat(endTime)) || StringUtils.isBlank(unixtime)) {
			return false;
		}
		
		String key = "";
		
		try {
			key = ConfigOnZk.getInstance().getValue(ChanetConstant.ZK_CONFIG_PATH, "channet.key", "");
			
			if (StringUtils.isBlank(key)) {
				return false;
			}
			
			String _sign = "_sign";
			//几种验证方式
			if (StringUtils.isBlank(orderid) && StringUtils.isBlank(orderstatus)) {
				_sign = getEncrySign(user, startTime, endTime, Long.parseLong(unixtime), key);
			} else if (! StringUtils.isBlank(orderid)) {
				_sign = getEncrySignForOrder(user, startTime, endTime, orderid, Long.parseLong(unixtime), key);
			} else if ( !StringUtils.isBlank(orderstatus)) {
				_sign = getEncrySignForOrderStatus(user, startTime, endTime, Integer.parseInt(orderstatus), Long.parseLong(unixtime), key);
			}
			
			if (! _sign.equalsIgnoreCase(sig)) {
				return false;
			}
			
		} catch (BaseException e) {
			LOGGER.error(e, "get params error from zookeeper", WebUtils.getClientIp(request));
		}
		
		return true; 
	}
	
	/**
	 * query by time range
	 * */
	private String queryOrderList(String cpsType, String startTime,String endTime) {
		if (StringUtils.isBlank(cpsType) || !isRealFormat(startTime) || !isRealFormat(endTime)) {
			return null;
		}
		
		List<String> odList = unionOrderService.queryOrderList(cpsType, startTime, endTime);
		String orderString = combineToString(odList, startTime, endTime, 1, 1);
		return orderString;
	}

	/**
	 * query by id
	 * */
	private String queryOrderById(String cpsType,String orderId) {
		if (StringUtils.isBlank(cpsType) || StringUtils.isBlank(orderId)) {
			return null;
		}
		
		List<String>  olist = new ArrayList<String>();
		String orderInfo = unionOrderService.queryOrderById(cpsType, orderId);
		
		if (! StringUtils.isBlank(orderInfo)) {
			olist.add(orderInfo);
		}
		
		String currentTime = DateUtil.getCurrentDateTimeStr();
		String result = combineToString(olist, currentTime, currentTime, 1, 1);
		
		return result;
	}

	
	/**
	 * query by order status
	 * */
	private String queryOrderByOrderStatus(String cpsType, int orderStatus) {
		if (StringUtils.isBlank(cpsType)) {
			return null;
		}
		
		String time = DateUtil.getCurrentDateTimeStr();
		List<String> orderList = unionOrderService.queryOrderByOrderStatus(cpsType, orderStatus);
		String result = combineToString(orderList, time, time, 1, 1);
		return result;
	}


	/**
	 * validation
	 * */
	@Override
	public boolean validation(HttpServletRequest request) {
		if (! isValidatetionUrl(request)) {
			return false;
		}
		
		return true;
		/*String clientIp = WebUtils.getClientIp(request);
		try {
			String allowIp = ConfigOnZk.getInstance().getValue(ChanetConstant.ZK_CONFIG_PATH, ChanetConstant.CHANNET_ALLOW_IP_KEY, "10.249.9.160");
			if (!StringUtils.isBlank(clientIp) && allowIp.contains(clientIp.split(",")[0])) {
				return true;
			}
		} catch (BaseException e) {
 			LOGGER.error(e, "get param on zookeeper error", WebUtils.getClientIp(request));
		}
		return false;*/
	}


	/**
	 * query handler
	 * */
	@Override
	public String query(HttpServletRequest request,HttpServletResponse response) {
		
		String user = request.getParameter("user");
		String startTime = convertToFormat(request.getParameter("start"));
		String endTime = convertToFormat(request.getParameter("end"));
		String orderid = request.getParameter("orderid");
		String orderstatus = request.getParameter("orderstatus");
		String unixtime = request.getParameter("unixtime");
		
		String queryResult  = "";
		
		if (StringUtils.isBlank(orderid) && StringUtils.isBlank(orderstatus)) {
			queryResult = queryOrderList(user, startTime, endTime);
		}else if (!StringUtils.isBlank(orderid)) {
			queryResult = queryOrderById(user, orderid);
		} else if (!StringUtils.isBlank(orderstatus)) {
			Integer oStatus = orderStatusMap.get(orderstatus);
			if (oStatus == null) {
				oStatus = new Integer(60);
			}
			queryResult = queryOrderByOrderStatus(user, oStatus);
		} else {
			queryResult = "illegal url  request";
		}
		
		long localServerTime = DateUtil.parseDateTime(DateUtil.getCurrentDateTimeStr()).getTime();
		long _unixTime = Long.parseLong(unixtime);
		long differ = localServerTime - _unixTime;
		if (differ > 600*1000) {
			queryResult = "query time out,default value: 600s";
		}
		
		return queryResult;
	}

}
