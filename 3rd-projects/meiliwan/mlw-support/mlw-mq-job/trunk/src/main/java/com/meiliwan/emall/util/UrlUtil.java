package com.meiliwan.emall.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

public class UrlUtil {
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(UrlUtil.class);
	
	public static String encodeVal(String value){
		if(StringUtils.isBlank(value)){
			return null;
		}
		
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error(e, "encode value " + value + " error", null);
		}
		
		return value;
	}

}
