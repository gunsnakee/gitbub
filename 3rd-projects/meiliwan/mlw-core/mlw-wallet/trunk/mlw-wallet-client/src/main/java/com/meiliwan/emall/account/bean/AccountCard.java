package com.meiliwan.emall.account.bean;

import java.math.BigDecimal;
import com.meiliwan.emall.core.bean.BaseEntity;
/**
 * 礼品卡账户实体 不同于礼品卡实体，礼品卡实体在pms中定义
 * @author lzl
 *
 */
public class AccountCard extends BaseEntity implements Cloneable{
	
	private static final long serialVersionUID = 1961606695272805003L;
	
	/**用户ID，对应passport的uid*/
	private Integer uid;
	
	/**账户余额*/
    private BigDecimal cardCoin;
    
    /**账户冻结金额*/
    private BigDecimal freezeAmount;
    
    /**状态 ；1：正常，-1：异常*/
    private Short status;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public BigDecimal getCardCoin() {
		return cardCoin;
	}

	public void setCardCoin(BigDecimal cardCoin) {
		this.cardCoin = cardCoin;
	}

	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}

	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getId() {
		return uid;
	}
}
