package com.meiliwan.emall.pay.client;


import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pay.bean.PayHead;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.dto.PaymentDTO;
import com.meiliwan.emall.service.BaseService;

public class PayClient {
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(PayClient.class); 

	public static final String GETPAYID = "payService/getPayId";

	
	public static boolean log(PayHead payHead) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/log", payHead));

		return obj.get(BaseService.RESULT_OBJ).getAsBoolean();
	}

	/**
	 * 
	 * @param payment
	 * @param password
	 * @param clientIP
	 * @return
	 */
	public static PaymentDTO innerPay(PaymentDTO payment, String password,
			String clientIP) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/innerPay", payment, password,
						 clientIP));

		return new Gson().fromJson(obj.get("resultObj"), PaymentDTO.class);
	}
	
	/**
	 * 
	 * @param uid
	 * @param orderId
	 * @param clientIP
	 */
	public static void storeGoToPayOrderId(int uid, String orderId,String clientIP){
		try {
			ShardJedisTool.getInstance().sadd(JedisKey.pay$order$nocancel, uid, orderId);
		} catch (Exception e) {
			LOG.error(e, "storeGoToPayOrderId(uid:"+uid + ",orderId:" +orderId +")", clientIP);
		}
	}
	
	/**
	 * 
	 * @param uid
	 * @param clientIP
	 * @return
	 */
	public static Set<String> getGoToPayOrderIds(int uid, String clientIP){
		try {
			return ShardJedisTool.getInstance().smembers(JedisKey.pay$order$nocancel, uid);
		} catch (Exception e) {
			LOG.error(e, "getGoToPayOrderIds(uid:"+uid +")", clientIP);
		}
		
		return new HashSet<String>();
	}

	/**
	 * 
	 * @param orderId
	 * @param includeItems
	 * @param delBeforeSelect
	 *            是否在查询之前，把未支付成功的支付item删除掉，true为删除，false为不删除
	 * @return
	 */
	public static PayHead getPayHead(String orderId, boolean includeItems,
			boolean delBeforeSelect) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/getPayHead", orderId,
						includeItems, delBeforeSelect));

		return new Gson().fromJson(obj.get("resultObj"), PayHead.class);
	}

	/**
	 * 
	 * @param orderId
	 */
	public static void delPayInfoByPayId(String payId) {
		IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/delPayInfoByPayId", payId));
	}

	/**
	 * 订单退款，供取消订单使用
	 * 
	 * @param orderId
	 * @return
	 */
	public static boolean backMoney(String orderId) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/backMoney", orderId));

		return PayStatus.PAY_FINISHED.name().equals(
				obj.get("resultObj").getAsString());
	}

	/**
	 * 订单退款，供取消订单使用
	 * 
	 * @param orderId
	 * @return
	 */
	public static boolean backMoneyForAdmin(PayHead payHead) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/backMoneyForAdmin", payHead));

		return PayStatus.PAY_FINISHED.name().equals(
				obj.get("resultObj").getAsString());
	}

	/**
	 * 订单退款，供取消订单使用 新方法，需要传入取消操作人id
	 * 
	 * @param orderId
	 * @return
	 */
	public static boolean backMoneyForCancel(String orderId, String ip,
			Integer uid) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/backMoneyForCancel", orderId,
						ip, uid));

		return PayStatus.PAY_FINISHED.name().equals(
				obj.get("resultObj").getAsString());
	}

	/**
	 * 订单退款，仅供后台退款操作使用，不做冻结回退和取消订单使用
	 * 
	 * @param orderId
	 * @return
	 */
	public static boolean refundMoneyForAdmin(PayHead payHead, String ip,
			Integer uid) {
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.PAY_ICE_SERVICE,
				JSONTool.buildParams("payService/refundMoneyForAdmin", payHead,
						ip, uid));

		return PayStatus.PAY_FINISHED.name().equals(
				obj.get("resultObj").getAsString());
	}

}
