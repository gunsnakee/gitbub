package com.meiliwan.emall.commons.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;

public class ExtBeanUtils extends BeanUtils {
	
	private static final BigDecimal BIGDECIMAL_ZERO = new BigDecimal("0");
	static {
		ConvertUtils.register(new DateConvert(), java.util.Date.class);
		ConvertUtils.register(new DateConvert(), java.sql.Date.class);
		ConvertUtils.register(new DateConvert(), java.sql.Timestamp.class);
		
		// 这里一定要注册默认值，使用null也可以  
	    BigDecimalConverter bd = new BigDecimalConverter(BIGDECIMAL_ZERO);  
	    ConvertUtils.register(bd, BigDecimal.class);
	}
	 
	public static void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		}
	}
}
