package com.meiliwan.emall.pay.util;

import java.math.BigDecimal;
import java.util.Arrays;

import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.pay.bean.PayHead;
import com.meiliwan.emall.pay.bean.PayItem;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.constant.PayType;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;

/**
 * 
 * @author lsf
 *
 */
public class ParamConvertUtil {

	public static PaymentDTO convert2Payment(PayHead payHead){
		PaymentDTO payment = new PaymentDTO();
		payment.setOrderId(payHead.getOrderId());
		payment.setSubject(payHead.getSubject());
		payment.setPayType(PayType.valueOf(payHead.getPayType()));
		payment.setUid(payHead.getUid());
		payment.setTargetUid(payHead.getTargetUid());
		payment.setTotalAmount(payHead.getTotalAmount().doubleValue());
		
		if(payHead.getPayItems() != null && payHead.getPayItems().size() > 0){
			PayParam[] payParams = new PayParam[payHead.getPayItems().size()];
			payment.setPayParams(payParams);
			
			int index = 0;
			for(PayItem payItem : payHead.getPayItems()){
				PayParam payParam = new PayParam();
				payParam.setPayId(payItem.getPayId());
				payParam.setPayCode(PayCode.valueOf(payItem.getPayCode()));
				payParam.setAmount(payItem.getPayAmount().doubleValue());
				payParam.setPayStatus(PayStatus.valueOf(payItem.getPayStatus()));
				
				payParams[index++] = payParam;
			}
		}
		
		return payment;
	}
	
	public static PayHead convert2PayHead(PaymentDTO payment){
		PayHead payHead = new PayHead();
		payHead.setUid(payment.getUid());
		payHead.setTargetUid(payment.getTargetUid());
		payHead.setOrderId(payment.getOrderId());
		payHead.setSubject(payment.getSubject());
		payHead.setPayType(payment.getPayType().name());
		payHead.setTotalAmount(new BigDecimal(payment.getTotalAmount()));
		payHead.setPayStatus(payment.getPayStatus().name());
		
		PayItem[] payItems = new PayItem[payment.getPayParams().length];
		payHead.setPayItems(Arrays.asList(payItems));
		int index = 0;
		for (PayParam payParam : payment.getPayParams()) {
			PayItem payItem = new PayItem();
			payItem.setPayId(payParam.getPayId());
			payItem.setPayAmount(new BigDecimal(payParam.getAmount()));
			payItem.setPayCode(payParam.getPayCode().name());
			payItem.setPayStatus(payParam.getPayStatus().name());
			payItem.setOrderId(payment.getOrderId());
			
			payItems[index++] = payItem;
		}
		
		return payHead;
	}
	
}
