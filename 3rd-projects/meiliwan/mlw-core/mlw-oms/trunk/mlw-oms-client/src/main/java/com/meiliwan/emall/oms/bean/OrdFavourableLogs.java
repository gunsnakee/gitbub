package com.meiliwan.emall.oms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;
/**
 * 订单优惠/折扣操作日志对象
 * @author lzl
 */
public class OrdFavourableLogs extends BaseEntity{

	private static final long serialVersionUID = 5713838533313745424L;
	
	private Integer id ;
	//订单头ID
	private String orderId ; 
	//优惠方式id
	private Integer favourableId ;
	//优惠方式名称
	private String favourableName ;
	//优惠数值
	private String favourableValue ;
	//操作描述
	private String content ;
	//操作人ID
	private Integer opteratorId ;
	//用户名
	private String opteratorName ;
	//操作人类型,AD管理员,US用户
	private String opteratorType ;
	//创建时间
	private Date createTime;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getFavourableId() {
		return favourableId;
	}

	public void setFavourableId(Integer favourableId) {
		this.favourableId = favourableId;
	}

	public String getFavourableName() {
		return favourableName;
	}

	public void setFavourableName(String favourableName) {
		this.favourableName = favourableName;
	}

	public String getFavourableValue() {
		return favourableValue;
	}

	public void setFavourableValue(String favourableValue) {
		this.favourableValue = favourableValue;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getOpteratorId() {
		return opteratorId;
	}

	public void setOpteratorId(Integer opteratorId) {
		this.opteratorId = opteratorId;
	}

	public String getOpteratorName() {
		return opteratorName;
	}

	public void setOpteratorName(String opteratorName) {
		this.opteratorName = opteratorName;
	}

	public String getOpteratorType() {
		return opteratorType;
	}

	public void setOpteratorType(String opteratorType) {
		this.opteratorType = opteratorType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }
	
}
