package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRuleParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动商品 bean
 */
public class ActivityProductBean extends BaseEntity implements ActivityRuleParam {
    private static final long serialVersionUID = -1222062798904735592L;

    private Integer id;

    private Integer actId;

    private Integer proId;

    private String proName;   //商品名

    private Integer actNum;

    private Short isPriceDown; //是否直降

    private BigDecimal actPrice;  //活动价（折扣价）

    private Integer thirdCatId;  //三级类目id

    private Date createTime;

    private Integer uid;

    private String admin;

    private Integer discount;  //折扣 如：八五折用 整数85表示

    private BigDecimal discounts;  //折扣力度（折扣额）

    private BigDecimal mlwPrice;   //美丽价

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public Integer getActNum() {
        return actNum;
    }

    public void setActNum(Integer actNum) {
        this.actNum = actNum;
    }

    public Short getIsPriceDown() {
        return isPriceDown;
    }

    public void setIsPriceDown(Short isPriceDown) {
        this.isPriceDown = isPriceDown;
    }

    public BigDecimal getActPrice() {
        return actPrice;
    }

    public void setActPrice(BigDecimal actPrice) {
        this.actPrice = actPrice;
    }

    public Integer getThirdCatId() {
        return thirdCatId;
    }

    public void setThirdCatId(Integer thirdCatId) {
        this.thirdCatId = thirdCatId;
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

    public BigDecimal getDiscounts() {
        return discounts;
    }

    public void setDiscounts(BigDecimal discounts) {
        this.discounts = discounts;
    }

    public BigDecimal getMlwPrice() {
        return mlwPrice;
    }

    public void setMlwPrice(BigDecimal mlwPrice) {
        this.mlwPrice = mlwPrice;
    }
}