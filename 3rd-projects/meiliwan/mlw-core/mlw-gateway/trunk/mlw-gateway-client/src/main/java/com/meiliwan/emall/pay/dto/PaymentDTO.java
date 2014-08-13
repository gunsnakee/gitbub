package com.meiliwan.emall.pay.dto;


import java.io.Serializable;
import java.util.Arrays;










import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.bean.ValidateItem;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.constant.PayType;

public class PaymentDTO implements Serializable, ValidateItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1416912783526596558L;
	
	private String subject;
	/**
	 * 需要支付的总金额
	 */
	private Double totalAmount;
	
	/**
	 * 当前执行支付操作的用户ID
	 */
	private Integer uid;

	/**
	 * 当前支付的目标用户ID（即为谁支付），默认为当前用户的uid
	 */
	private Integer targetUid;

	/**
	 * 订单号，如果payType为 CHARGE , 则订单号可以为空
	 */
	private String orderId;

	/**
	 * 用于支付的目的, 目前有购买商品、账户充值、退款
	 */
	private PayType payType;
	
	/**
	 * 请求的时间戳，以毫秒为单位
	 */
	private long timeMillis;

	/**
	 * 支付结果的状态
	 */
//	private PayStatus payStatus;
	
	/**
	 * 支付方式的组合
	 */
	private PayParam[] payParams;
	
	public PayParam[] getPayParams() {
		return payParams;
	}

	public void setPayParams(PayParam[] payParams) {
		this.payParams = payParams;
	}
	
	public String getSubject() {
		return 
				(StringUtils.isBlank(subject) ? "账号充值" : (subject.startsWith("美丽传说") ? subject : "美丽传说 " + subject));
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Double getTotalAmount() {
		return totalAmount == null ? 0.0 : NumberUtil.formatToDouble(totalAmount, "0.00");
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getTargetUid() {
		return targetUid == null || targetUid <= 0 ? uid : targetUid;
	}

	public void setTargetUid(Integer targetUid) {
		this.targetUid = targetUid;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public long getTimeMillis() {
		return timeMillis;
	}

	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
	}

	public PayStatus getPayStatus() {
		PayStatus payStatus = PayStatus.PAY_WAIT;
		if(payParams != null && payParams.length > 0){
			double successAmount = 0.0;
			boolean hasThirdPayParam = false;
			for(PayParam payParam : payParams){
				if(PayStatus.PAY_FINISHED.equals(payParam.getPayStatus())){
					successAmount += payParam.getAmount();
				}
				//判断其中是否有第三方支付方式
				if(!hasThirdPayParam && !PayCode.MLW_C.equals(payParam.getPayCode()) && !PayCode.MLW_W.equals(payParam.getPayCode())){
					hasThirdPayParam = true;
				}
			} 
			
			long sucAmt = Math.round(successAmount*100);
			long totalAmt = Math.round(this.getTotalAmount()*100);
			if(sucAmt > 0){
				if(sucAmt < totalAmt){
					if(hasThirdPayParam){
						payStatus = PayStatus.PAY_PARTIAL;
					}else{
						payStatus = PayStatus.PAY_FAILURE;
					}
				}else if(sucAmt == totalAmt){
					payStatus = PayStatus.PAY_FINISHED;
				}
			}
		}
		return payStatus;
	}

	@Override
	public String toString() {
        if(payParams!=null && payParams.length>0) {
            return "(orderId:" + orderId + ",subject:" + getSubject() + ",uid:" + uid + ",targetUid:" + getTargetUid()
                    + ",payType:" + payType + ",totalAmount:" + getTotalAmount()
                    + ",payParams:" + Arrays.asList(payParams) + ")";
        } else{
            return "(orderId:" + orderId + ",subject:" +  getSubject()  + ",uid:" + uid + ",targetUid:" + getTargetUid()
                    + ",payType:" + payType + ",totalAmount:" +  getTotalAmount()
                    +  ")";
        }
	}
	
	public String allToString() {
		if(payParams!=null && payParams.length>0) {
			StringBuffer sb = new StringBuffer() ;
			for(PayParam para:payParams){
				if(para !=null ){
				sb.append(para.allToString());
				sb.append(", ");
				}
			}
			return "(orderId:" + orderId + ",subject:" + getSubject() + ",uid:" + uid + ",targetUid:" + getTargetUid()
	        + ",payType:" + payType + ",totalAmount:" + getTotalAmount()
	        + ",payParams:" + sb.toString() + ")";
		}else{
			return "(orderId:" + orderId + ",subject:" + getSubject() + ",uid:" + uid + ",targetUid:" + getTargetUid()
	        + ",payType:" + payType + ",totalAmount:" + getTotalAmount() ;
		}
	}

}
