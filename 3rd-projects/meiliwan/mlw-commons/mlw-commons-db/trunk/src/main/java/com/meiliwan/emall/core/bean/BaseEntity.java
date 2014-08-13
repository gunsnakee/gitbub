package com.meiliwan.emall.core.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 所有实体的公共父类
 * @author xiong.yu
 *
 */
public abstract class BaseEntity implements Serializable{

	public abstract <T extends Serializable> T getId();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
