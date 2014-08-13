package com.meiliwan.emall.commons.web.validate;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: yuxiong
 * Date: 13-7-21
 * Time: 下午8:11
 * To change this template use File | Settings | File Templates.
 */
public class ValidatePay implements ValidateItem{

    private int adminId;

    private int targetUid;

    private double money;

    private String[] payIds;

    private String orderId;
    
    private String orderItemId;

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

	public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(int targetUid) {
        this.targetUid = targetUid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String[] getPayIds() {
        return payIds;
    }

    public void setPayIds(String[] payIds) {
        this.payIds = payIds;
        if (this.payIds!=null && this.payIds.length>1){
            Arrays.sort(this.payIds);
        }
    }

    public ValidatePay(){
    }

    public ValidatePay(int adminId, int targetUid, double money, String[] payIds) {
        this.adminId = adminId;
        this.targetUid = targetUid;
        this.money = money;
        setPayIds(payIds);
    }
    
    public ValidatePay(int adminId, int targetUid, double money,
			String[] payIds, String orderId, String orderItemId) {
		this.adminId = adminId;
		this.targetUid = targetUid;
		this.money = money;
		this.orderId = orderId;
		this.orderItemId = orderItemId;
		setPayIds(payIds);
	}
    
    @Override
	public String toString() {
        if (this.payIds!=null && this.payIds.length>0){
            return "[adminId=" + adminId + ", targetUid=" + targetUid
                    + ", payIds=" + Arrays.toString(payIds)
                    + ", orderId=" + orderId + ", orderItemId=" + orderItemId + "]";
        }else{
            return "[adminId=" + adminId + ", targetUid=" + targetUid
                    + ", orderId=" + orderId + ", orderItemId=" + orderItemId + "]";
        }
	}

}
