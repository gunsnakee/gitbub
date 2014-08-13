package com.meiliwan.emall.pay.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.bo.AccountCommon;
import com.meiliwan.emall.account.client.AccountWalletClient;
import com.meiliwan.emall.account.client.GiftCardClient;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.account.bean.WalletDto;
import com.meiliwan.emall.base.client.IdGenClient;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pay.bean.PayHead;
import com.meiliwan.emall.pay.bean.PayItem;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.constant.PayType;
import com.meiliwan.emall.pay.dao.PayHeadDao;
import com.meiliwan.emall.pay.dao.PayItemDao;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;
import com.meiliwan.emall.pay.method.BalancePayMethod;
import com.meiliwan.emall.pay.method.GiftCardPayMethod;
import com.meiliwan.emall.pay.method.InnerPayMethod;
import com.meiliwan.emall.pay.util.ParamConvertUtil;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiong on 13-7-11.
 */
@Service
public class PayService extends DefaultBaseServiceImpl {

	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(PayService.class);
	
	@Autowired
	private PayHeadDao payHeadDao;

	@Autowired
	private PayItemDao payItemDao;
	
	private static final Map<String, InnerPayMethod> payMethodMap = new HashMap<String, InnerPayMethod>();

	static {
		payMethodMap.put(PayCode.MLW_W.getPayHandler(), new BalancePayMethod());
		payMethodMap
				.put(PayCode.MLW_C.getPayHandler(), new GiftCardPayMethod());
	}


	/**
	 * 记录支付日志
	 * 
	 * @param resultObj
	 * @param payHead
	 * @param payItems
	 */
	public void log(JsonObject resultObj, PayHead payHead) {
		JSONTool.addToResult(logPayInfo(payHead), resultObj);
	}
	
	/**
	 * 
	 * @param resultObj
	 * @param payment
	 * @param password
	 * @param clientIP
	 */
	public void innerPay(JsonObject resultObj, PaymentDTO payment, String password,
			String clientIP){
		PayParam[] payParams = payment.getPayParams();
		PayStatus resultCode = PayStatus.NO_INNER_PAYMETHOD;
		for (PayParam payParam : payParams) {
			InnerPayMethod payMethod = payMethodMap.get(payParam.getPayCode()
					.getPayHandler());
			// 异常 - 支付方式不存在
			if (payMethod == null && PayCode.getPayCodeByCode(payParam.getPayCode().getCode()) == null) {
				payParam.setPayStatus(PayStatus.PAY_FAILURE);
				LOG.warn("checkResult:" + PayStatus.PAY_METHOD_NOT_EXISTS,
						payment.allToString(), clientIP);
			} else {
				// 正常 - 支付方式存在
				// 检查支付方式种是否有payId，如果没有的话，将由系统自动分配一个唯一的payId
				checkPayId(payParam);

				if (payMethod instanceof InnerPayMethod) {
					if (PayStatus.PAY_FINISHED.equals(payParam.getPayStatus())) {
						LOG.warn("already pay finished payment",
								payment.allToString(), clientIP);
						continue;
					}
					
					resultCode = payMethod.payMoney(payment, payParam, password, clientIP);

					LOG.info("checkResult:" + resultCode,
							"payMethod.payMoney result:"+resultCode.getCode()+payment.allToString(), clientIP);
					payParam.setPayStatus(resultCode);

					if (!PayStatus.PAY_FINISHED.equals(resultCode)) {
						// 如果某种支付方式执行失败，则跳出循环
						LOG.warn("checkResult:" + resultCode,
								payment.allToString(), clientIP);

					} 
				}
			}
		}
		
		PayHead payHead = ParamConvertUtil.convert2PayHead(payment);
		
		try {
			logPayInfo(payHead);
		} catch (Exception e) {
			LOG.error(e, payHead, clientIP);
		}
		
		JSONTool.addToResult(payment, resultObj);
	}
	
	private boolean logPayInfo(PayHead payHead){
		int result = 0;
		try {
			PayHead persistHead = payHeadDao
					.getEntityById(payHead.getOrderId(), true);
			if (persistHead == null) {
				result = payHeadDao.insert(payHead);
			} else {
				result = payHeadDao.update(payHead);
			}
		} catch (Exception e) {
			throw new BaseRuntimeException("log-payHead-error", e);
		}

		List<PayItem> payItems = payHead.getPayItems();
		if (payItems != null) {
			List<PayItem> persistPayItems = payItemDao
					.getPayItemByOrderId(payHead.getOrderId(), true);
			Map<String, PayItem> persistPayIdMap = buildPayItemMapWithPayId(persistPayItems);
			Map<String, PayItem> persistPayCodeMap = buildPayItemMapWithPayCode(persistPayItems);
			for (PayItem payItem : payItems) {
				try {
					payItem.setOrderId(payHead.getOrderId());
					PayItem persistPayItem = persistPayCodeMap.get(payItem.getPayCode());
					if((persistPayItem != null && PayStatus.PAY_FINISHED.name().equals(persistPayItem.getPayStatus()))){
						LOG.info("exists payInfo", payItem, "");
						continue;
					}
					//如果支付成功则更新结账成功时间
					if(payItem.getPayStatus().equals(PayStatus.PAY_FINISHED.name())){
						payItem.setSuccessTime(new Date());
					}
					if (persistPayIdMap.containsKey(payItem.getPayId()) 
							) {
						payItemDao.update(payItem);
					} else {
						payItemDao.insert(payItem);
					}
				} catch (Exception e) {
					LOG.error(e, "payItem(" + payItem + "), payHead(" + payHead + ")", null);
					throw new BaseRuntimeException("log-payItem-error", e);
				}
			}
		}
		
		return result > 0;
	}
	
	/**
	 * 目前只有招商银行的支付方式需要使用
	 * @param resultObj
	 * @param orderId
	 */
	public void delPayInfoByPayId(JsonObject resultObj, String payId){
		PayItem payItem = payItemDao.getEntityById(payId, true);
		if(payItem != null){
			int result = payItemDao.delFailPayItemByOrderId(payItem.getOrderId());
			if(result > 0){
				payHeadDao.delete(payItem.getOrderId());
			}
		}
	}

	/**
	 * 根据订单号获取支付日志信息
	 * 
	 * @param resultObj
	 * @param orderId
	 * @param includeItems
	 *            是否需要一并查出payItems信息。true表示需要，false表示不需要
	 */
	public void getPayHead(JsonObject resultObj, String orderId,
			boolean includeItems, boolean delBeforeSelect) {
		PayHead payHead = payHeadDao.getEntityById(orderId, true);

		if (delBeforeSelect) {
			payItemDao.delFailPayItemByOrderId(orderId);
		}

		if (payHead != null && includeItems) {
			List<PayItem> payItems = payItemDao.getPayItemByOrderId(orderId, true);

			if (payItems != null && payItems.size() > 0) {
				payHead.setPayItems(payItems);
			}
		}

		JSONTool.addToResult(payHead, resultObj);
	}

	/**
	 * 退款服务，供后台使用
	 * payHead.getOrderId() 原订单id-退换货id
	 * @param resultObj
	 * @param payHead
	 */
	public void backMoneyForAdmin(JsonObject resultObj, PayHead payHead) {
		String orderId = payHead.getOrderId();
		String[] parts = orderId.split("-");
		payHead.setExtraInfo("{orderId:"+parts[0]+"}");
		String reOrderId = parts[1];
		
		PayHead rePayHead = payHeadDao.getEntityById(reOrderId, true);
		if(rePayHead!= null && PayStatus.PAY_FINISHED.name().equals(rePayHead.getPayStatus())){
			LOG.info("repeat backMoneyForAdmin","orderId " + payHead.getOrderId(), "");
			JSONTool.addToResult(PayStatus.PAY_FINISHED, resultObj);
			return;
		}
		
		payHead.setOrderId(reOrderId);

		List<PayItem> payItems = payHead.getPayItems();
		PayItem walletPayItem = null;
		if (payItems != null && payItems.size() > 0) {
			for (PayItem payItem : payItems) {
				if (PayCode.MLW_W.name().equals(
						payItem.getPayCode())) {
					walletPayItem = payItem;
				}else{
					payItem.setPayStatus(PayStatus.PAY_FINISHED.name());
				}
				payItem.setOrderId(reOrderId);
				checkPayId(payItem);
			}
		}

		PayStatus resultCode = PayStatus.PAY_FAILURE;
		if (walletPayItem != null) {
			WalletDto walletDto = new WalletDto();
			walletDto.setOrderId(orderId);
			walletDto.setuId(payHead.getTargetUid());
			walletDto.setMoney(walletPayItem.getPayAmount().doubleValue());
			
			double result = AccountWalletClient
					.updateRefundFromGateway(walletDto);
			resultCode = NumberUtil.compareTo(result, 0) >= 0 ? PayStatus.PAY_FINISHED
					: PayStatus.PAY_FAILURE;
			
			walletPayItem.setPayStatus(resultCode.name());
		} else {
			resultCode = PayStatus.PAY_FINISHED;
		}
		
		payHead.setPayStatus(resultCode.name());

		LOG.info("backMoneyForAdmin:" , "payHead:" + payHead, resultObj.get(CLIENT_IP_STR).getAsString());
		logPayInfo(payHead);

		JSONTool.addToResult(resultCode, resultObj);
	}
	
	/**
	 * 
	 * @param resultObj
	 * @param orderId
	 */
	@Deprecated
	public void backMoney(JsonObject resultObj, String orderId) {
		String reOrderId = "r" + orderId;
		PayHead rePayHead = payHeadDao.getEntityById(reOrderId, true);
		if(rePayHead!= null && PayStatus.PAY_FINISHED.name().equals(rePayHead.getPayStatus())){
			LOG.info("repeat backMoney" , "orderId " + orderId, resultObj.get(CLIENT_IP_STR).getAsString());
			JSONTool.addToResult(PayStatus.PAY_FINISHED, resultObj);
			return;
		}
		
		List<PayItem> payItems = payItemDao.getPayItemByOrderId(orderId, true);
		PayItem payItem = null;
		if (payItems != null && payItems.size() > 0) {
			for (PayItem currPayItem : payItems) {
				if (PayCode.MLW_W.name().equals(
						currPayItem.getPayCode())
						&& PayStatus.PAY_FINISHED.name().equals(
								currPayItem.getPayStatus())) {
					payItem = currPayItem;
				}else{
					currPayItem.setPayStatus(PayStatus.PAY_FINISHED.name());
				}
				
				currPayItem.setOrderId(reOrderId);
				checkPayId(currPayItem);
			}

			if (payItem != null) {
				PayHead payHead = payHeadDao.getEntityById(orderId, true);
				
				WalletDto walletDto = new WalletDto();
				walletDto.setOrderId(orderId + "-" + orderId);
				walletDto.setuId(payHead.getUid());
				walletDto.setMoney(payItem.getPayAmount().doubleValue());
				double result = AccountWalletClient
						.updateRefundFromGateway(walletDto);
//				double result = AccountWalletClient.updateRefundFromFreeze(orderId);

				PayStatus resultCode = NumberUtil.compareTo(result, 0) > 0 ? PayStatus.PAY_FINISHED
						: PayStatus.PAY_FAILURE;
				JSONTool.addToResult(resultCode, resultObj);

				payHead.setOrderId(reOrderId);
				payHead.setSubject("订单退货");
				payHead.setPayType(PayType.BACK_FUND.name());
				payHead.setTotalAmount(payItem.getPayAmount());
				payHead.setPayStatus(resultCode.name());
				payHead.setPayItems(Arrays.asList(payItem));

				LOG.info("backMoney" , payHead, resultObj.get(CLIENT_IP_STR).getAsString());
				logPayInfo(payHead);
			} 
		} 
		
		if (payItem == null) {
			//如果没有检测到有钱包支付记录，则调用该接口，以确保将用户的冻结款退回,如果没有冻结款记录，则什么都不做就返回成功
			double result = AccountWalletClient.updateRefundFromFreeze(orderId);
			JSONTool.addToResult(result > 0 || NumberUtil.compareTo(result, AccountCommon.RECORD_NOT_EXIT) == 0 ? PayStatus.PAY_FINISHED : PayStatus.PAY_FAILURE, resultObj);
		}
		

	}
	
	private static void checkPayId(PayParam payParam) {
		String payId = payParam.getPayId();
		if (StringUtils.isBlank(payId)) {
			payId = IdGenClient.getPayId();
			payParam.setPayId(payId);
		}
	}

	private void checkPayId(PayItem payItem) {
		String payId = IdGenClient.getPayId();
		payItem.setPayId(payId);
	}

	private Map<String, PayItem> buildPayItemMapWithPayCode(List<PayItem> payItems) {
		Map<String, PayItem> payItemMap = new HashMap<String, PayItem>();
		if (payItems != null && payItems.size() > 0) {
			for (PayItem payItem : payItems) {
				payItemMap.put(payItem.getPayCode(), payItem);
			}
		}

		return payItemMap;
	} 
	
	private Map<String, PayItem> buildPayItemMapWithPayId(List<PayItem> payItems) {
		Map<String, PayItem> payItemMap = new HashMap<String, PayItem>();
		if (payItems != null && payItems.size() > 0) {
			for (PayItem payItem : payItems) {
				payItemMap.put(payItem.getPayId(), payItem);
			}
		}

		return payItemMap;
	}
	
	/**
	 * 退款服务，供后台使用 新 支持钱包和礼品卡组合
	 * payHead.getOrderId() 原订单id-退换货id
	 * @param resultObj
	 * @param payHead
	 */
	public void refundMoneyForAdmin(JsonObject resultObj, PayHead payHead, String ip, Integer uid) {
		String orderId = payHead.getOrderId();
		String[] parts = orderId.split("-");
		payHead.setExtraInfo("{orderId:"+parts[0]+"}");
		String reOrderId = parts[1];
		
		PayHead rePayHead = payHeadDao.getEntityById(reOrderId, true);
		if(rePayHead!= null && PayStatus.PAY_FINISHED.name().equals(rePayHead.getPayStatus())){
			LOG.info("repeat backMoneyForAdmin","orderId " + payHead.getOrderId(), "");
			JSONTool.addToResult(PayStatus.PAY_FINISHED, resultObj);
			return;
		}
		
		payHead.setOrderId(reOrderId);

		List<PayItem> payItems = payHead.getPayItems();
		PayItem walletPayItem = null;
		PayItem cardPayItem = null;
		if (payItems != null && payItems.size() > 0) {
			for (PayItem payItem : payItems) {
				if (PayCode.MLW_W.name().equals(payItem.getPayCode())) {
					walletPayItem = payItem;
					
				}else if(PayCode.MLW_C.name().equals(payItem.getPayCode())){
					cardPayItem = payItem;
					
				}else{
					payItem.setPayStatus(PayStatus.PAY_FINISHED.name());
				}
				payItem.setOrderId(reOrderId);
				checkPayId(payItem);
			}
		}

		PayStatus resultCode = PayStatus.PAY_FAILURE;
		List<WalletDto> walletDtos = new ArrayList<WalletDto>(); //修改walletDtos空指针的报错 modify by yuxiong 2014-1-6
		
		if(walletPayItem != null){
			WalletDto walletDto = new WalletDto();
			walletDto.setPayCode(PayCode.MLW_W);
			walletDto.setOrderId(orderId);
			walletDto.setuId(payHead.getTargetUid());
			walletDto.setMoney(walletPayItem.getPayAmount().doubleValue());
			walletDto.setClientIp(ip);
			walletDto.setAdminId(uid);
			walletDtos.add(walletDto);
		}
		if(cardPayItem != null){
			WalletDto walletDto = new WalletDto();
			walletDto.setPayCode(PayCode.MLW_C);
			walletDto.setOrderId(orderId);
			walletDto.setuId(payHead.getTargetUid());
            //这里应该拿cardPayItem来设置金额 modify by yuxiong 2014-1-6
			walletDto.setMoney(cardPayItem.getPayAmount().doubleValue());
			walletDto.setClientIp(ip);
			walletDto.setAdminId(uid);
			walletDtos.add(walletDto);
		}
		
		if(walletDtos != null && walletDtos.size() > 0){
			double result = GiftCardClient.updateRefundFromGateway(walletDtos);
			resultCode = NumberUtil.compareTo(result, 0) >= 0 ? PayStatus.PAY_FINISHED
					: PayStatus.PAY_FAILURE;
            //这里需要设置支付的结果 modify by yuxiong 2014-1-6
            if(walletPayItem!=null){
                walletPayItem.setPayStatus(resultCode.name());
            }
            if(cardPayItem!=null){
                cardPayItem.setPayStatus(resultCode.name());
            }
		}else{
			resultCode = PayStatus.PAY_FINISHED;
		}
		
		payHead.setPayStatus(resultCode.name());
		LOG.info("backMoneyForAdmin:" , "payHead:" + payHead, resultObj.get(CLIENT_IP_STR).getAsString());
		logPayInfo(payHead);

		JSONTool.addToResult(resultCode, resultObj);
	}
	
	/**
	 * 这个是钱包或礼品卡取消回退的新方法 后面请使用这里
	 * @param resultObj
	 * @param orderId
	 */
	public void backMoneyForCancel(JsonObject resultObj, String orderId, String ip, Integer adminId) {
		String reOrderId = "r" + orderId;
		PayHead rePayHead = payHeadDao.getEntityById(reOrderId, true);
		PayHead payHead = payHeadDao.getEntityById(orderId, true);
//		if(rePayHead == null){
//			LOG.info("backMoneyForCancel no PayHead" , "orderId: " + orderId, resultObj.get(CLIENT_IP_STR).getAsString());
//			JSONTool.addToResult(PayStatus.PAY_FAILURE, resultObj);
//			return;
//		}
		if(rePayHead!= null && PayStatus.PAY_FINISHED.name().equals(rePayHead.getPayStatus())){
			LOG.info("repeat backMoneyForCancel" , "orderId " + orderId, resultObj.get(CLIENT_IP_STR).getAsString());
			JSONTool.addToResult(PayStatus.PAY_FINISHED, resultObj);
			return;
		}
		
		List<PayItem> payItems = payItemDao.getPayItemByOrderId(orderId, true);
		if (payItems == null || payItems.size() <= 0 || payHead == null) {
			//如果没有检测到有钱包支付记录，则调用该接口，以确保将用户的冻结款退回,如果没有冻结款记录，则什么都不做就返回成功
//			double result = AccountWalletClient.updateRefundFromFreeze(orderId);
			//钱包和礼品卡的回退都可以走这里
			double result = GiftCardClient.updateRefundFromFreezeWithIp(orderId, ip, adminId);
			JSONTool.addToResult(result > 0 || NumberUtil.compareTo(result, AccountCommon.RECORD_NOT_EXIT) == 0 ? PayStatus.PAY_FINISHED : PayStatus.PAY_FAILURE, resultObj);
		}
		
		PayItem walletPayItem = null;
		PayItem cardPayItem = null;
		List<PayItem> newPayItems = new ArrayList<PayItem>() ;
		if(payItems != null && payItems.size() > 0){
			for (PayItem currPayItem : payItems) {
				if (PayCode.MLW_W.name().equals(currPayItem.getPayCode()) && PayStatus.PAY_FINISHED.name().equals(currPayItem.getPayStatus())) {
					LOG.info("订单涉及钱包退款", "orderId:"+orderId, ip);
					walletPayItem = currPayItem;
					newPayItems.add(walletPayItem);
					
				}else if(PayCode.MLW_C.name().equals(currPayItem.getPayCode()) && PayStatus.PAY_FINISHED.name().equals(currPayItem.getPayStatus())){
					LOG.info("订单涉及礼品卡退款", "orderId:"+orderId, ip);
					cardPayItem = currPayItem;
					newPayItems.add(cardPayItem);
					
				}else{
					LOG.info("退款方式", "paycode:"+ currPayItem.getPayCode()+",orderId:"+orderId, ip);
					currPayItem.setPayStatus(PayStatus.PAY_FINISHED.name());
				}
				
				currPayItem.setOrderId(reOrderId);
				checkPayId(currPayItem);
			}
			
			PayStatus resultCode = PayStatus.PAY_FAILURE;
			List<WalletDto> walletDtos = new ArrayList<WalletDto>() ;
			if(walletPayItem != null){
				WalletDto walletDto = new WalletDto();
				walletDto.setPayCode(PayCode.MLW_W);
				walletDto.setOrderId(orderId+"-"+orderId);
				walletDto.setuId(payHead.getUid());
				walletDto.setMoney(walletPayItem.getPayAmount().doubleValue());
				walletDto.setClientIp(ip);
				walletDto.setAdminId(adminId);
				walletDtos.add(walletDto);
			}
			
			if(cardPayItem != null){
				WalletDto walletDto = new WalletDto();
				walletDto.setPayCode(PayCode.MLW_C);
				walletDto.setOrderId(orderId+"-"+orderId);
				walletDto.setuId(payHead.getUid());
				walletDto.setMoney(cardPayItem.getPayAmount().doubleValue());
				walletDto.setClientIp(ip);
				walletDto.setAdminId(adminId);
				walletDtos.add(walletDto);
			}
			
			if(walletDtos != null && walletDtos.size() > 0){
				LOG.info("GiftCardClient.updateRefundFromGateway", "walletDtos.size:"+walletDtos.size(), ip);
				double result = GiftCardClient.updateRefundFromGateway(walletDtos);
				resultCode = NumberUtil.compareTo(result, 0) > 0 ? PayStatus.PAY_FINISHED
						: PayStatus.PAY_FAILURE;
				
			}else{
				resultCode = PayStatus.PAY_FINISHED;
			}
			
			JSONTool.addToResult(resultCode, resultObj);
			
			payHead.setOrderId(reOrderId);
			payHead.setSubject("订单退货");
			payHead.setPayType(PayType.BACK_FUND.name());
			payHead.setPayStatus(resultCode.name());
			payHead.setPayItems(newPayItems);

			LOG.info("backMoneyForCancel" , payHead, ip);
			logPayInfo(payHead);
		}
	}
}
