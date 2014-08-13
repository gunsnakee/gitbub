package com.meiliwan.emall.oms.dto.export;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sean on 13-10-30.
 */
public class SendGoodsListExcelEntity implements Serializable {

    private static final long serialVersionUID = 6639491948392305721L;
    /** 序列 */
    private int sequence;

    private Date orderCreateTime;
    /** 出库时间 */
    private Date storeHouseSendTime;

    private String orderId;

    /** 运单号 */
    private String waybillNum;
    /** 商品编号 */
    private String productNum;

    /** 商品名称 */
    private String productName;

    /** 数量 */
    private int count;

    private double price;

    /** 商品金额  单价 * 数量 */
    private double curProductTotal;


    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getStoreHouseSendTime() {
        return storeHouseSendTime;
    }

    public void setStoreHouseSendTime(Date storeHouseSendTime) {
        this.storeHouseSendTime = storeHouseSendTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getWaybillNum() {
        return waybillNum;
    }

    public void setWaybillNum(String waybillNum) {
        this.waybillNum = waybillNum;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCurProductTotal() {
        return curProductTotal;
    }

    public void setCurProductTotal(double curProductTotal) {
        this.curProductTotal = curProductTotal;
    }
}
