package com.meiliwan.emall.pay.method;

import com.meiliwan.emall.account.client.AccountWalletClient;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.account.bean.WalletDto;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;

public class BalancePayMethod extends InnerPayMethod {
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(BalancePayMethod.class);

	@Override
	public PayStatus payMoney(PaymentDTO payment, PayParam payParam, String password, String clientIP) {
		WalletDto walletDto = new WalletDto();
		walletDto.setMoney(payParam.getAmount());
		walletDto.setuId(payment.getUid());
		walletDto.setOrderId(payment.getOrderId());
		double result = AccountWalletClient.subMoney(payment.getOrderId(), payment.getUid(), payParam.getAmount(), password, payParam.getPayId());
		
		PayStatus payStatus = NumberUtil.compareTo(result, 0) >= 0 ? PayStatus.PAY_FINISHED : PayStatus.PAY_FAILURE;
		payParam.setPayStatus(payStatus);
		
		LOG.info("balance pay","payResult:"+payStatus+ ", payParam:"+ payParam.allToString(), "");
		
		return payStatus;
	}


	@Override
	public PayStatus robackMoney(PaymentDTO payment, PayParam payParam) {
		WalletDto  walletDto = new WalletDto();
		walletDto.setOrderId(payment.getOrderId());
		walletDto.setuId(payment.getUid());
		walletDto.setMoney(payParam.getAmount());
		double result = AccountWalletClient.updateRefundFromGateway(walletDto);
		
		return NumberUtil.compareTo(result, 0) >= 0 ? PayStatus.PAY_FINISHED : PayStatus.PAY_FAILURE;
	}

}
