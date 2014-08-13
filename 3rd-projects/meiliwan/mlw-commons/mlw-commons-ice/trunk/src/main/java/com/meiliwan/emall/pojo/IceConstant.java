package com.meiliwan.emall.pojo;

import com.meiliwan.emall.commons.util.BaseConfig;
import com.meiliwan.emall.commons.util.IPUtil;

/**
 * 
 * @author lsf
 *
 */
public class IceConstant {
	
	public final static String LOCAL_IP = IPUtil.getLocalIp();
	
	public static String DEFAULT_MODE = "debug";
    // private static String connectMode = DEFAULT_MODE;
	public final static String iceBaseConfig = "iceintf/ice_base_config.xml";
	public final static String iceConfig = BaseConfig.getValue("projectName")
            + "/ice_config.xml";
	
	public final static String FILE_TRANS_PROTOCOL = "icef~";
	
	public final static String FILE_SPLITER = " ";

}
