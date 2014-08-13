package com.meiliwan.emall.pay.method;

import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;

public abstract class InnerPayMethod {

	/**
	 * 为给定的支付参数进行支付
	 * @param payment 支付的基本参数对象 
	 * @param payParam 支付金额信息对象
	 * @param password 密码
	 * @param clientIP 客户端IP
	 * @return
	 */
	public abstract PayStatus payMoney(PaymentDTO payment, PayParam payParam, String password, String clientIP);
	
	/**
	 * 该支付方式的退款接口
	 * @param payment 支付的基本参数对象 
	 * @param payParam 支付金额信息对象
	 */
	public abstract PayStatus robackMoney(PaymentDTO payment, PayParam payParam);
	
}
