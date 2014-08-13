package com.meiliwan.emall.oms.bean;


import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;
/**
 * 订单优惠/折扣上下限控制对象
 * @author lzl
 */
public class OrdFavourable extends BaseEntity{

	private static final long serialVersionUID = 5713838533313745424L;
	
	private Integer id ;
	//优惠方式名称
	private String name ;
	//优惠方式类型
	private String type ;
	//上限折扣(八五折用整数85表示)
	private Integer upperDiscount ;
	//下限折扣(八五折用整数85表示)
	private Integer lowerDiscount ;
	//操作人ID
	private Integer opteratorId ;
	//用户名
	private String opteratorName ;
	//操作人类型,AD管理员,US用户
	private String opteratorType ;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	
	@SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getUpperDiscount() {
		return upperDiscount;
	}


	public void setUpperDiscount(Integer upperDiscount) {
		this.upperDiscount = upperDiscount;
	}


	public Integer getLowerDiscount() {
		return lowerDiscount;
	}


	public void setLowerDiscount(Integer lowerDiscount) {
		this.lowerDiscount = lowerDiscount;
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


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public void setId(Integer id) {
		this.id = id;
	}
}
