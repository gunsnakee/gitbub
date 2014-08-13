package com.meiliwan.emall.base.dto;

import com.meiliwan.emall.base.bean.BaseTransportPrice;

/**
 * 根据父id 查找,顺便带出名字
 * @author rubi
 *
 */
public class TransportPriceDTO extends BaseTransportPrice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2359047263841056160L;
	
	private String areaName;

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	

}
