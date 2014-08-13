package com.meiliwan.emall.base.bean;

import java.math.BigDecimal;

import com.meiliwan.emall.core.bean.BaseEntity;

public class BaseStationFare extends BaseEntity{
	
	public static final int DEFAULT_ID=1;
	public static final String fixed="fixed";
	public static final String full="full";
	public static final String free="free";
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3192861442865254336L;

	private Integer id=DEFAULT_ID;

    private String type;

    private BigDecimal fixedPrice;

    private BigDecimal fullFreeLimit;

    private BigDecimal notFullFreePrice;

    private String message;
    
	public boolean isFixed(){
    		if(type.equals(fixed)){
    			return true;
    		}
    		return false;
    }
    
    public boolean isFull(){
		if(type.equals(full)){
			return true;
		}
		return false;
    }
    
    public boolean isFree(){
		if(type.equals(free)){
			return true;
		}
		return false;
    }
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public BigDecimal getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(BigDecimal fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public BigDecimal getFullFreeLimit() {
        return fullFreeLimit;
    }

    public void setFullFreeLimit(BigDecimal fullFreeLimit) {
        this.fullFreeLimit = fullFreeLimit;
    }

    public BigDecimal getNotFullFreePrice() {
        return notFullFreePrice;
    }

    public void setNotFullFreePrice(BigDecimal notFullFreePrice) {
        this.notFullFreePrice = notFullFreePrice;
    }
}