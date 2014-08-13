package com.meiliwan.emall.pms.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * User: guangdetang
 * Date: 13-6-28
 * Time: 上午11:55
 * 针对不同类型查询条数DTO
 */
public class QueryCountsDTO implements Serializable {

    private static final long serialVersionUID = 99415071439494692L;
    private Integer keyType;
    private Integer counts;

    public Integer getKeyType() {
        return keyType;
    }

    public void setKeyType(Integer keyType) {
        this.keyType = keyType;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
