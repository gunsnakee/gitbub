package com.meiliwan.emall.mms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;
/**
 * 类目积分规则
 * @author yiyou.luo
 *2013-06-03
 */

public class UserCategoryIntegralRule extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4304173009925130388L;

	private Integer id;

    private String categoryName;

    private Integer categoryId;

    private Double ratio;

    private Date createTime;

    private Date updateTime;

    private Integer isDel;

    private Integer ruleType; //  类型

    private Integer state; //状态（启用 停用）

    private Integer firstCategoryId;

    private Integer secondCategoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
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

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(Integer firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public Integer getSecondCategoryId() {
        return secondCategoryId;
    }

    public void setSecondCategoryId(Integer secondCategoryId) {
        this.secondCategoryId = secondCategoryId;
    }
}