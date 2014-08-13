package com.meiliwan.emall.oms.dto;

import com.meiliwan.emall.pms.bean.ProSupplier;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * User: guangdetang
 * Date: 13-6-11
 * Time: 上午11:37
 */
public class SaleProOrdDTO implements Serializable {

    /**
     * 商品ID
     */
    private Integer proId;

    /**
     * 三级类目ID
     */
    private Integer proCateId;
    private String proCateName;

    /**
     * 商品名称
     */
    private String proName;

    /**
     * 某商品已卖出总数
     */
    private Integer saleNum;

    /**
     * 供应商ID
     */
    private Integer supplierId;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private Integer billType;
    private Integer operateType;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp payTime;
    private Integer orderItemStatus;
    private Integer state;
    private BigDecimal unitPrice;

    private ProSupplier supplier;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getProCateId() {
        return proCateId;
    }

    public void setProCateId(Integer proCateId) {
        this.proCateId = proCateId;
    }

    public String getProCateName() {
        return proCateName;
    }

    public void setProCateName(String proCateName) {
        this.proCateName = proCateName;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Integer getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(Integer orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ProSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProSupplier supplier) {
        this.supplier = supplier;
    }
}
