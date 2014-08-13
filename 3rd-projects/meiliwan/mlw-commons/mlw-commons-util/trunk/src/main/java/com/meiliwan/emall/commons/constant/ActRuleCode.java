package com.meiliwan.emall.commons.constant;

/**
 * 活动规则的code
 * @author lsf
 *
 */
public enum ActRuleCode {
	
	//result_code
	DISCOUNT("折扣"),
	MINUS("立减"),
	BAOYOU("包邮"),
	CAN_REPEAT("商品是否可以重复参加满减活动"),
	
	//cond_code
	OVER("满"),
	ONE_PRO_BUY_NUM("单种商品购买数量")
	;
	

	private String descp;
	private ActRuleCode(String descp){
		this.descp = descp;
	}
	public String getDescp() {
		return descp;
	}
	
}
