package com.meiliwan.emall.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.oms.bean.OrdPay;
import com.meiliwan.emall.oms.constant.OrderStatusType;


/**
 * 订单状态DIO
 * @author yuxiong
 *
 */
public class OrderStatusDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5231858595683386283L;
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 订单行号
	 */
	private String orderItemId;
	
	/**
	 * 旧的订单行号-逆向订单专用
	 */
	private String oldOrderItemId;
	
	/**
	 * 订单类型，正向/逆向
	 */
	private short billType;
	/**
	 * 状态类型
	 */
	private OrderStatusType statusType;
    /**
     * 订单类型
     */
    private String orderType;
    /**
	 * 状态Code
	 */
	private String statusCode;

	private int uid;
	private int adminId;
	//日志使用
	private String adminName;
	//日志使用,货运单号
	private String transportInfo;
	//日志使用,取消订单的备注
	private String cancelComment;
	
    private List<OrdPayDTO> ordPayDTOList;
    private List<OrdPay> ordPays;

    
    public String getCancelComment() {
		return cancelComment;
	}

	public void setCancelComment(String cancelComment) {
		this.cancelComment = cancelComment;
	}

	public List<OrdPay> getOrdPays() {
        return ordPays;
    }

    public void setOrdPays(List<OrdPay> ordPays) {
        this.ordPays = ordPays;
    }

    /**
	 * 是否做订单有效性的校验
	 */
	private boolean checkOrder = true;
	
	/**
	 * 逆向订单，是否需要退回商品,1:yew 0 no
	 */
	private short needRetpro;
	
	/**
	 * 逆向订单，需要退回多少运费
	 */
	private BigDecimal backTransportFee;
	
	/**
	 * 逆向订单，退换货原因
	 */
	private String returnReasonType;
	/** 逆向退货数量 **/
	private short retProCount;
	/** 逆向退款总数 单价*数量 未加运费和其他 **/
	private double retTotalAmount;
	/** 退还的地方 */
	private PayCode payCode;
	/** 退换货的协商说明 **/
	/*private String consultationComment;
	
    public String getConsultationComment() {
		return consultationComment;
	}

	public void setConsultationComment(String consultationComment) {
		this.consultationComment = consultationComment;
	}*/

	public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

	public String getReturnReasonType() {
		return returnReasonType;
	}

	public void setReturnReasonType(String returnReasonType) {
		this.returnReasonType = returnReasonType;
	}

	public short getNeedRetpro() {
		return needRetpro;
	}

	public void setNeedRetpro(short needRetpro) {
		this.needRetpro = needRetpro;
	}

	public BigDecimal getBackTransportFee() {
		return backTransportFee;
	}

	public void setBackTransportFee(BigDecimal backTransportFee) {
		this.backTransportFee = backTransportFee;
	}

	public String getOldOrderItemId() {
		return oldOrderItemId;
	}

	public void setOldOrderItemId(String oldOrderItemId) {
		this.oldOrderItemId = oldOrderItemId;
	}

	public int getAdminId() {
		return adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public List<OrdPayDTO> getOrdPayDTOList() {
        return ordPayDTOList;
    }

    public void setOrdPayDTOList(List<OrdPayDTO> ordPayDTOList) {
        this.ordPayDTOList = ordPayDTOList;
    }

    
	public String getTransportInfo() {
		return transportInfo;
	}

	public void setTransportInfo(String transportInfo) {
		this.transportInfo = transportInfo;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public boolean isAdmin() {
		return adminId>0&&uid==0?true:false;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public boolean isCheckOrder() {
		return checkOrder;
	}

	public void setCheckOrder(boolean checkOrder) {
		this.checkOrder = checkOrder;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public short getBillType() {
		return billType;
	}

	public void setBillType(short billType) {
		this.billType = billType;
	}

	public OrderStatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(OrderStatusType statusType) {
		this.statusType = statusType;
	}

	public short getRetProCount() {
		return retProCount;
	}

	public void setRetProCount(short retProCount) {
		this.retProCount = retProCount;
	}

	public double getRetTotalAmount() {
		return retTotalAmount;
	}

	public void setRetTotalAmount(double retTotalAmount) {
		this.retTotalAmount = retTotalAmount;
	}

	public PayCode getPayCode() {
		return payCode;
	}

	public void setPayCode(PayCode payCode) {
		this.payCode = payCode;
	}
	
	
}
