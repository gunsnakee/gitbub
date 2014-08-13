package com.meiliwan.emall.oms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;


public class OrdTransport extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3626104520234648960L;

	private Integer id;

    private String orderId;

    private String custDataId;

    private String info;

    private String logisticsCompany;

    private String logisticsNumber;

    private Date transportTime;
    private Long transportTimeLong;

    private Date createTime;
    private Long createTimeLong;
    
    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getCustDataId() {
        return custDataId;
    }

    public void setCustDataId(String custDataId) {
        this.custDataId = custDataId == null ? null : custDataId.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany == null ? null : logisticsCompany.trim();
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber == null ? null : logisticsNumber.trim();
    }

    public Date getTransportTime() {
        return transportTime;
    }

    public void setTransportTime(Date transportTime) {
        this.transportTime = transportTime;
        if(transportTime!=null)
        		setTransportTimeLong(transportTime.getTime());
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        if(createTime!=null)
        		setCreateTimeLong(createTime.getTime());
    }

	public Long getTransportTimeLong() {
		return transportTimeLong;
	}

	public void setTransportTimeLong(Long transportTimeLong) {
		this.transportTimeLong = transportTimeLong;

	}

	public Long getCreateTimeLong() {
		return createTimeLong;
	}

	public void setCreateTimeLong(Long createTimeLong) {
		this.createTimeLong = createTimeLong;
	}
    
    
}