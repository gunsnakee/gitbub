package com.meiliwan.emall.tongji.dto.oms;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OrdQueryDTO implements Serializable{

    private static final long serialVersionUID = -3147249267205855959L;
    /***************  Ord属性  **********************/
    private  Date createTimeMin;

    private Date createTimeMax;

    private Short billType;

    private List<String> orderStatusList;

    public Short getBillType() {
        return billType;
    }

    public void setBillType(Short billType) {
        this.billType = billType;
    }

    public List<String> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(String... orderStatusList) {
        this.orderStatusList = orderStatusList == null?null: Arrays.asList(orderStatusList);
    }

    public Date getCreateTimeMin() {
        return createTimeMin;
    }

    public void setCreateTimeMin(Date createTimeMin) {
        this.createTimeMin = createTimeMin;
    }

    public Date getCreateTimeMax() {
        return createTimeMax;
    }

    public void setCreateTimeMax(Date createTimeMax) {
        this.createTimeMax = createTimeMax;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}