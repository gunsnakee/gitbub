package com.meiliwan.emall.stock.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class StockImportLog extends BaseEntity {
    private Integer id;

    private String batchNo;

    private String barCode;

    private Short state;

    private Integer originalStock;

    private Integer changeStock;

    private Integer nowStock;

    private String descp;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Integer getOriginalStock() {
        return originalStock;
    }

    public void setOriginalStock(Integer originalStock) {
        this.originalStock = originalStock;
    }

    public Integer getChangeStock() {
        return changeStock;
    }

    public void setChangeStock(Integer changeStock) {
        this.changeStock = changeStock;
    }

    public Integer getNowStock() {
        return nowStock;
    }

    public void setNowStock(Integer nowStock) {
        this.nowStock = nowStock;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}