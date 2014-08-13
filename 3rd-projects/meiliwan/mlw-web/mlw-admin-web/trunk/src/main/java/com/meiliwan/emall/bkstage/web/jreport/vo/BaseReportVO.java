package com.meiliwan.emall.bkstage.web.jreport.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class BaseReportVO implements Serializable{

	public abstract <T extends Serializable> T getId();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
