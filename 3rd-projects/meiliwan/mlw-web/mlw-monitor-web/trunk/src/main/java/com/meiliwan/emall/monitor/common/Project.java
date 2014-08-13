package com.meiliwan.emall.monitor.common;

/**
 * 此类只提供给监控后台下拉列表使用
 * @author rubi
 *
 */
public enum Project {

	Antispam("antispam-service"),
    Bkstage("bkstage-service"), 
    Monitor("monitor-service"), 
    Pay("pay-service"),     
    Sp("sp-service"),      
    Account("account-service"), 
    Base("base-service"),    
    Cms("cms-service"),     
    Mms("mms-service"),     
    Oms("oms-service"),     
    Pms("pms-service"),     
    Stock("stock-service"),
    Imeiliwan("web"),
    BkstageWeb("bkstage-web"),    
    MmsWeb("mms-web");  
    private String code;

	private Project(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
    
     
}
