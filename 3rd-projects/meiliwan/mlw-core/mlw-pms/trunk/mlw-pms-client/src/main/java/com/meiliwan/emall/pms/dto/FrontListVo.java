package com.meiliwan.emall.pms.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * User: wuzixin
 * Date: 14-3-13
 * Time: 下午4:28
 */
public class FrontListVo implements Serializable{

    private static final long serialVersionUID = 5209046151802757752L;
    private Integer proId;

    private String skuName;

    private String proName;

    private String shortName;

    private BigDecimal mlwPrice;

    private Integer comNum;

    private Double score;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public BigDecimal getMlwPrice() {
        return mlwPrice;
    }

    public void setMlwPrice(BigDecimal mlwPrice) {
        this.mlwPrice = mlwPrice;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
