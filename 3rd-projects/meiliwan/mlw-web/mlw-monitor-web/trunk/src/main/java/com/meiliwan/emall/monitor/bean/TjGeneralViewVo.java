package com.meiliwan.emall.monitor.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yyluo
 * Date: 14-4-9
 * Time: 下午5:50
 * tjgeneral 报表视图vo
 *
 */
public class TjGeneralViewVo implements Serializable {
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    private String host;  //来源
    private List<Integer> dateIndexValue;//每天的某个指标量
    private List<TjGeneral> dateIndexValues;//每天的所有指标量

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<Integer> getDateIndexValue() {
        return dateIndexValue;
    }

    public void setDateIndexValue(List<Integer> dateIndexValue) {
        this.dateIndexValue = dateIndexValue;
    }

    public List<TjGeneral> getDateIndexValues() {
        return dateIndexValues;
    }

    public void setDateIndexValues(List<TjGeneral> dateIndexValues) {
        this.dateIndexValues = dateIndexValues;
    }
}
