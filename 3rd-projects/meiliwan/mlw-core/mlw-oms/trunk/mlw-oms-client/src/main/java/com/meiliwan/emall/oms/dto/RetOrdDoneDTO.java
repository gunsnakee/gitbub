package com.meiliwan.emall.oms.dto;

import java.io.Serializable;

/**
 * 用来记录已经退换货了的商品的退换货记录
 * Created by guangdetang on 13-10-11.
 */
public class RetOrdDoneDTO implements Serializable{

    private static final long serialVersionUID = 1516956672608139687L;
    private int proId;
    /** 退换货类型，存储形式：退货 & 换货 */
    private String retType;
    /** 已经退换货的数量，存储形式：1 & 2 */
    private String retCount;

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType;
    }

    public String getRetCount() {
        return retCount;
    }

    public void setRetCount(String retCount) {
        this.retCount = retCount;
    }
}
