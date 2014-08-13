package com.meiliwan.emall.sp2.bean;


import com.meiliwan.emall.sp2.bean.base.ActivityRuleBean;

import java.util.Date;

/**
 * 折扣规则bean
 */

public class ActivityDiscountRuleBean extends ActivityRuleBean {
    private static final long serialVersionUID = -1222062798294735592L;

    private Integer id;

    private String ruleName;   //规则名称

    private Boolean isLimitSale;  //是否限购

    private Integer limitSaleNum;  //限购数量

    private Date createTime;  //

    private Integer uid;     //创建人id

    private String admin;   //    创建人

    private Integer discount;  //折扣 七点五折用 75数字表示

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public Boolean getIsLimitSale() {
        return isLimitSale;
    }

    public void setIsLimitSale(Boolean isLimitSale) {
        this.isLimitSale = isLimitSale;
    }

    public Integer getLimitSaleNum() {
        return limitSaleNum;
    }

    public void setLimitSaleNum(Integer limitSaleNum) {
        this.limitSaleNum = limitSaleNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin == null ? null : admin.trim();
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

}