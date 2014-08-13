package com.meiliwan.emall.service.union.handler.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.bean.union.ChanetConstant;
import com.meiliwan.emall.commons.bean.CpsOrderVO;
import com.meiliwan.emall.commons.bean.CpsOrderVO.CpsProduct;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.EncryptTools;

public class ChannetHandler extends TwoDimCodeHandler  {
	
	private final static MLWLogger LOGGER = MLWLoggerFactory.getLogger(ChannetHandler.class.getName());
	
	

	/**
	 * @param thanksId Thanks id，由成果网提供，是广告主在成果网这边的编号，固定的
	 * @param  id 成果网唯一标识id ([SID])
	 * @param orderId
	 * @param key	加密key请向成果网索取。
	 * */
	private String encrySign(String thanksId,String id,String orderId,String key){
		if (StringUtils.isBlank(thanksId)||StringUtils.isBlank(id)||StringUtils.isBlank(orderId)||StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder  = new StringBuilder();
		stringBuilder.append("t=").append(thanksId).append("&");
		stringBuilder.append("id=").append(id).append("&");
		stringBuilder.append("i=").append(orderId).append("&");
		stringBuilder.append("key=").append(key);
		
		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	/**
	 * @param 获取value的值
	 * */
	private String getValueFromZookeeper(String key){
		
		String value = "";
		try {
			value = ConfigOnZk.getInstance().getValue(ChanetConstant.ZK_CONFIG_PATH, key, "");
		} catch (BaseException e) {
			LOGGER.error(e, "get config param fail from zookeeper", "");
		}
		
		return value;
	}
	
	
	@Override
	public String buildPushParam(CpsOrderVO cpsOrder) {
		int payMethod = 1;
		int orderStatus = 0;
		int orderPayStatus = 0;
		String thanksId = getValueFromZookeeper("channet.t");
		String id = cpsOrder.getSourceId();
		String orderId = cpsOrder.getOrderId();
		String key = getValueFromZookeeper("channet.key");
		String orderTime = DateUtil.getCurrentDateTimeStr();
		StringBuilder goods_info = new StringBuilder();
		List<CpsProduct> cpsProducts = cpsOrder.getProList();
		
		//goods/price/count/name:
		for (CpsProduct cpsProduct : cpsProducts) {
			goods_info.append("goods").append("/");
			goods_info.append(cpsProduct.getPrice()).append("/");
			goods_info.append(cpsProduct.getBuyNum()).append("/");
			goods_info.append(cpsProduct.getProId()).append(":");
		}
		
		goods_info.setLength(goods_info.length() - 1);
		
		StringBuilder pushUrl = new StringBuilder();
		pushUrl.append("t=").append(thanksId).append("&");
		pushUrl.append("id=").append(id).append("&");
		pushUrl.append("i=").append(orderId).append("&");
		String encryString = encrySign(thanksId, id, orderId, key);
		pushUrl.append("sign=").append(encryString).append("&");
		pushUrl.append("o=").append(goods_info.toString()).append("&");
		pushUrl.append("ot=").append(orderTime).append("&");
		pushUrl.append("pm=").append(payMethod).append("&");
		pushUrl.append("ps=").append(orderPayStatus).append("&");
		pushUrl.append("st=").append(orderStatus);
		return pushUrl.toString();
	}

	@Override
	public String buildQueryInfo(CpsOrderVO cpsOrder) {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		StringBuilder query_info = new StringBuilder();
		
		List<CpsProduct> cpsProducts = cpsOrder.getProList();
		
		// 构造订单字符串
		String order_time = currentTime.toString();
		String order_id = cpsOrder.getOrderId();
		String union_id = cpsOrder.getSourceId();
		double total_price = cpsOrder.getTotalAmount();
		// double shipping = cpsOrder.get  非必须参数
		//double coupon = 0.0; 非必须参数
		
		
		query_info.append("{").append("\t");
		query_info.append("\"order_time\":").append("\"").append(order_time).append("\",").append("\t");
		query_info.append("\"order_id\":").append("\"").append(order_id).append("\",").append("\t");
		query_info.append("\"union_id\":").append("\"").append(union_id).append("\",").append("\t");
		query_info.append("\"status\":").append("\"").append("_ORDER_STATUS_").append("\",").append("\t");
		query_info.append("\"payment\":").append("\"").append("_PAY_MENTHOD_").append("\",").append("\t");
		query_info.append("\"total_price\":").append("\"").append(total_price).append("\",").append("\t");
		query_info.append("\"paid\":").append("\"").append("_PAY_STATUS_").append("\",").append("\t");
		
		query_info.append("\"items\":[").append("\t");
		
		for (CpsProduct cpsProduct : cpsProducts) {
			query_info.append("{").append("\t");
			
			query_info.append("\"item_id\":").append("\"").append(cpsProduct.getProId()).append("\",").append("\t");
			query_info.append("\"item_name\":").append("\"").append(cpsProduct.getProName()).append("\",").append("\t");
			query_info.append("\"category\":").append("\"").append("goods").append("\",").append("\t");
			query_info.append("\"price\":").append("\"").append(cpsProduct.getPrice()).append("\",").append("\t");
			query_info.append("\"amount\":").append("\"").append(cpsProduct.getBuyNum()).append("\"").append("\t");
			
			query_info.append("}");
			query_info.append(",").append("\t");
		}
		
		query_info.deleteCharAt(query_info.length()-2);
		query_info.append("]");
		query_info.append("}");
		
		return query_info.toString();
	}

	@Override
	public String getPushUrl() {
		return"http://count.chanet.com.cn/add_action_ec_2.cgi";
	}

	@Override
	public boolean isStoped() {
		boolean isStoped = false;
		try {
			isStoped = Boolean.valueOf(ConfigOnZk.getInstance().getValue("commons/system.properties", "channet.isStoped", "false").trim());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return isStoped;
	}
}
