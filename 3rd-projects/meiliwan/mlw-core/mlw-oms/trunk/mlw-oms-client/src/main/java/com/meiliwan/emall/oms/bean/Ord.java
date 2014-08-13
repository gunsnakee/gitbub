package com.meiliwan.emall.oms.bean;

import java.util.Date;
import java.util.List;

import com.meiliwan.emall.core.bean.BaseEntity;
import com.meiliwan.emall.oms.dto.OrdiDTO;

public class Ord extends BaseEntity{

    @Override
    public String toString() {
        return "Ord{" +
                "orderId='" + orderId + '\'' +
                ", orderComments='" + orderComments + '\'' +
                ", orderType='" + orderType + '\'' +
                ", realPayAmount=" + realPayAmount +
                ", orderSaleAmount=" + orderSaleAmount +
                ", orderPayAmount=" + orderPayAmount +
                ", favorableTotalAmount=" + favorableTotalAmount +
                ", transportFee=" + transportFee +
                ", isInvoice=" + isInvoice +
                ", totalItem=" + totalItem +
                ", billType=" + billType +
                ", clientId='" + clientId + '\'' +
                ", uid=" + uid +
                ", userName='" + userName + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", state=" + state +
                ", exceptionCode='" + exceptionCode + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", recvAddrId=" + recvAddrId +
                ", payTime=" + payTime +
                ", recvName='" + recvName + '\'' +
                ", remarkCount=" + remarkCount +
                ", printPickCount=" + printPickCount +
                ", printSendCount=" + printSendCount +
                ", stockTime=" + stockTime +
                ", listOrdi=" + listOrdi +
                ", listOrdiDTO=" + listOrdiDTO +
                '}';
    }

    /**
	 *
	 */
	private static final long serialVersionUID = 1725761133795608435L;

	private String orderId;

    private String orderComments;

    private String orderType;

    private Double realPayAmount;

    private Double orderSaleAmount;

    private Double orderPayAmount;
    private Double favorableTotalAmount;

    private Double transportFee;

    private Short isInvoice;

    private Short totalItem;

    private Short billType;

    private String clientId;

    private Integer uid;

    private String userName;

    private Date updateTime = new Date();

    private Date createTime = new Date();

    private Short state;
    private String exceptionCode;
    private String orderStatus;
    private Integer recvAddrId;
    private Date payTime;
    private String recvName;
    private Integer remarkCount;
    private Integer printPickCount;
    private Integer printSendCount;
    private Date stockTime;


    private List<Ordi> listOrdi;
    private List<OrdiDTO> listOrdiDTO;
    public String getRecvName() {
		return recvName;
	}

    public Double getFavorableTotalAmount() {
        return favorableTotalAmount;
    }

    public void setFavorableTotalAmount(Double favorableTotalAmount) {
        this.favorableTotalAmount = favorableTotalAmount;
    }

    public void setRecvName(String recvName) {
		this.recvName = recvName;
	}

    public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }
    public String getId() {
        return orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderComments() {
        return orderComments;
    }

    public void setOrderComments(String orderComments) {
        this.orderComments = orderComments == null ? null : orderComments.trim();
    }


    public Double getRealPayAmount() {
        return realPayAmount;
    }


    public Short getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Short isInvoice) {
        this.isInvoice = isInvoice;
    }

    public Short getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Short totalItem) {
        this.totalItem = totalItem;
    }

    public Short getBillType() {
        return billType;
    }

    public void setBillType(Short billType) {
        this.billType = billType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public Double getOrderSaleAmount() {
		return orderSaleAmount;
	}

	public void setOrderSaleAmount(Double orderSaleAmount) {
		this.orderSaleAmount = orderSaleAmount;
	}

	public Double getOrderPayAmount() {
		return orderPayAmount;
	}

	public void setOrderPayAmount(Double orderPayAmount) {
		this.orderPayAmount = orderPayAmount;
	}

	public Double getTransportFee() {
		return transportFee;
	}

	public void setTransportFee(Double transportFee) {
		this.transportFee = transportFee;
	}

	public Integer getRecvAddrId() {
		return recvAddrId;
	}

	public void setRecvAddrId(Integer recvAddrId) {
		this.recvAddrId = recvAddrId;
	}

	public void setRealPayAmount(Double realPayAmount) {
		this.realPayAmount = realPayAmount;
	}

    public List<Ordi> getListOrdi() {
        return listOrdi;
    }

    public void setListOrdi(List<Ordi> listOrdi) {
        this.listOrdi = listOrdi;
    }

    public List<OrdiDTO> getListOrdiDTO() {
        return listOrdiDTO;
    }

    public void setListOrdiDTO(List<OrdiDTO> listOrdiDTO) {
        this.listOrdiDTO = listOrdiDTO;
    }

    public Integer getRemarkCount() {
        return remarkCount;
    }

    public void setRemarkCount(Integer remarkCount) {
        this.remarkCount = remarkCount;
    }

    public Integer getPrintPickCount() {
        return printPickCount;
    }

    public void setPrintPickCount(Integer printPickCount) {
        this.printPickCount = printPickCount;
    }

    public Integer getPrintSendCount() {
        return printSendCount;
    }

    public void setPrintSendCount(Integer printSendCount) {
        this.printSendCount = printSendCount;
    }

    public Date getStockTime() {
        return stockTime;
    }

    public void setStockTime(Date stockTime) {
        this.stockTime = stockTime;
    }
}