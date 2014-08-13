package com.meiliwan.emall.commons.constant.order;

import com.meiliwan.emall.commons.constant.ActType;

public enum BuyType {

    NORMAL("NML", "普通"),
    PACK("PAK","套餐"),
    GIFT("GIF","赠品"),
    ACT("ACT","活动"),
    ACT_DISCOUNT("ACT_"+ActType.zk.name(),"限时折扣"),
    ACT_FREE_POSTAGE("ACT_"+ActType.my.name(),"限时包邮"),
    ACT_FULL_DISCOUNT("ACT_"+ActType.mlj.name(),"满立减"),
    ACT_FREE_POSTAGE_DISCOUNT("ACT_"+ActType.my_zk.name(),"限时包邮并打折");
	//PAK' || e.buyType=='ACT' || e.buyType=='GIF' || e.buyType=='ACT_my'
	//|| e.buyType=='ACT_zk' || e.buyType=='ACT_my_zk'  || e.buyType=='ACT_mlj
	private String code;
	private String desc;
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	private BuyType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	
}
