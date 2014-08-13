package com.meiliwan.emall.mms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;
/**
 * 商品额外赠分
 * @author yiyou.luo
 *2013-06-03
 */

public class UserProExtraIntegral extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6435234908836687878L;

	private Integer id;

    private Integer proId;

    private Integer value;

    private Date createTime;

    private Date endTime;

    private Integer isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

        public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}