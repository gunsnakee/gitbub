package com.meiliwan.emall.account.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 礼品卡账户交易记录实体
 * 礼品卡账户操作日志表
 * @author lzl
 *
 */
public class CardOptRecord extends BaseEntity implements Cloneable {

	private static final long serialVersionUID = 1961606695272805003L;
	
	/**内部流水号*/
    private String innerNum;

    /**礼品卡卡号 只有激活的时候会有，相当于给礼品卡账户充值一样，类似第三方流水号*/
    private String cardNum;

    /**用户Id*/
    private Integer uid;

    /**礼品卡账户金额*/
    private BigDecimal cardCoin;

    /**类型*/
    private String optType;

    /**操作时间*/
    private Date optTime;

    /**金额*/
    private BigDecimal money;

    /**操作结果*/
    private Integer successFlag;

    /**渠道*/
    private String source;

    /**涉及的订单id*/
    private String orderId;

    /**管理员id 后台退款的时候用到*/
    private Integer adminId;

    /**客户端ip*/
    private String clientIp;


    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInnerNum() {
        return innerNum;
    }

    public void setInnerNum(String innerNum) {
        this.innerNum = innerNum;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType == null ? null : optType.trim();
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Integer successFlag) {
        this.successFlag = successFlag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    @SuppressWarnings("unchecked")
	@Override
    public String getId() {
        return innerNum;
    }


    @Override
    public String toString() {
        return new StringBuilder("uId: ").append(uid).append(" innerNum:").append(innerNum).append(" optType: ").append(optType).append(" money:").append(money).append(" orderId: ").append(orderId).toString();
    }

	public BigDecimal getCardCoin() {
		return cardCoin;
	}

	public void setCardCoin(BigDecimal cardCoin) {
		this.cardCoin = cardCoin;
	}
}