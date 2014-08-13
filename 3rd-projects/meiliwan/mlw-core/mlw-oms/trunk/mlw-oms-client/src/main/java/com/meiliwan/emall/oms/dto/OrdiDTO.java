package com.meiliwan.emall.oms.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 前台显示
 *
 */
public class OrdiDTO implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8252144109307492624L;
	
	private String orderId;

	private String orderItemId;

    private Integer proId;

    private Integer proCateId;

    private String proName;

    private short proSellType;
    private String proPic;
    private double uintPrice;

    private Integer supplierId;

    private Integer brandId;

    private double transportFee;

    private double totalAmount;
    private double favorableAmount;
    private Integer saleNum;

    private String saleUnit;

    private Integer uid;

    private String userName;

    private Short billType;

    private Date createTime;

    private Date updateTime;
    private short retFlag;
    private short state;
    private double orderRealPayAmount;
    private String remark;
    private String orderItemStatus;
    private String statusType;
    private String statusCode;
    private int recvAddrId;
    private String recvName;
    private String buyType;
    private String payCode;
    private String payName;
    private Date payTime;
    //	商品条形码
    private String proBarCode;
    private Short subStockFlag;

    private Integer actSingleId;
    private Integer actMultiId;
    /**  分摊至商品 的 运费 Transportation Expenses */
    private double avgProTransExp;
    /** 分摊运费 + 商品单价*商品数量  */
    private double proPriceAndTransExp;

    public Integer getActSingleId() {
        return actSingleId;
    }

    public void setActSingleId(Integer actSingleId) {
        this.actSingleId = actSingleId;
    }

    public Integer getActMultiId() {
        return actMultiId;
    }

    public void setActMultiId(Integer actMultiId) {
        this.actMultiId = actMultiId;
    }

    public double getAvgProTransExp() {
        return avgProTransExp;
    }

    public void setAvgProTransExp(double avgProTransExp) {
        this.avgProTransExp = avgProTransExp;
    }

    public double getProPriceAndTransExp() {
        return proPriceAndTransExp;
    }

    public void setProPriceAndTransExp(double proPriceAndTransExp) {
        this.proPriceAndTransExp = proPriceAndTransExp;
    }

    public Short getSubStockFlag() {
		return subStockFlag;
	}

	public void setSubStockFlag(Short subStockFlag) {
		this.subStockFlag = subStockFlag;
	}

	public String getProBarCode() {
		return proBarCode;
	}

	public void setProBarCode(String proBarCode) {
		this.proBarCode = proBarCode;
	}

    public double getFavorableAmount() {
        return favorableAmount;
    }

    public void setFavorableAmount(double favorableAmount) {
        this.favorableAmount = favorableAmount;
    }

    public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public double getOrderRealPayAmount() {
        return orderRealPayAmount;
    }

    public short getRetFlag() {
        return retFlag;
    }

    public void setRetFlag(short retFlag) {
        this.retFlag = retFlag;
    }

    public void setOrderRealPayAmount(double orderRealPayAmount) {
        this.orderRealPayAmount = orderRealPayAmount;
    }

    public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public short getProSellType() {
		return proSellType;
	}

	public void setProSellType(short proSellType) {
		this.proSellType = proSellType;
	}

	public int getRecvAddrId() {
		return recvAddrId;
	}

	public void setRecvAddrId(int recvAddrId) {
		this.recvAddrId = recvAddrId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getId() {
        return orderItemId;
    }
    private String orderType;
    public String getOrderType() {
        return orderType;
    }
    
    public String getOrderItemStatus() {
		return orderItemStatus;
	}

	public void setOrderItemStatus(String orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}

	public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId == null ? null : orderItemId.trim();
    }

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

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public double getUintPrice() {
        return uintPrice;
    }

    public void setUintPrice(double uintPrice) {
        this.uintPrice = uintPrice;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public double getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(double transportFee) {
        this.transportFee = transportFee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit == null ? null : saleUnit.trim();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Short getBillType() {
        return billType;
    }

    public void setBillType(Short billType) {
        this.billType = billType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getProPic() {
		return proPic;
	}

	public void setProPic(String proPic) {
		this.proPic = proPic;
	}


    @Override
    public String toString() {
        return "OrdiDTO{" +
                "orderId='" + orderId + '\'' +
                ", orderItemId='" + orderItemId + '\'' +
                ", proId=" + proId +
                ", proCateId=" + proCateId +
                ", proName='" + proName + '\'' +
                ", proSellType=" + proSellType +
                ", proPic='" + proPic + '\'' +
                ", uintPrice=" + uintPrice +
                ", supplierId=" + supplierId +
                ", brandId=" + brandId +
                ", transportFee=" + transportFee +
                ", totalAmount=" + totalAmount +
                ", saleNum=" + saleNum +
                ", saleUnit='" + saleUnit + '\'' +
                ", uid=" + uid +
                ", userName='" + userName + '\'' +
                ", billType=" + billType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", state=" + state +
                ", orderRealPayAmount=" + orderRealPayAmount +
                ", remark='" + remark + '\'' +
                ", orderItemStatus='" + orderItemStatus + '\'' +
                ", recvAddrId=" + recvAddrId +
                ", recvName='" + recvName + '\'' +
                ", buyType='" + buyType + '\'' +
                ", payCode='" + payCode + '\'' +
                ", payName='" + payName + '\'' +
                ", payTime=" + payTime +
                ", orderType='" + orderType + '\'' +
                '}';
    }
    
    
}