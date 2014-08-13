package com.meiliwan.emall.monitor.allocation;

import com.meiliwan.emall.monitor.bean.Player;

public class PlayerAppDTO extends Player{
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1481803094143718119L;
	private String appName;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

}