package com.meiliwan.emall.commons.log;

public  enum BizLogObjOrder implements BizLogObj{
	
	ORDER;
	
	@Override
	public String getBizLogObjName() {
		return this.name();
	}
	
}
