package com.meiliwan.emall.sp2.bean.base;

import com.meiliwan.emall.core.bean.BaseEntity;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-25
 * Time: 下午5:05
 * 活动规则bean 父类
 * 所有活动规则bean 都必须继承该类
 */
public abstract class ActivityRuleBean extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2172595271411501440L;
	protected int actId;

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	
}
