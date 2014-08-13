package com.meiliwan.emall.account.bo;

public class AccountCommon {
	/** 业务ID */
	public static final String WALLET_ID = "123456789";
	/** 余额不足 */
	public static final int NOT_ENOUGH = -888;
	/** 钱包被封锁 */
	public static final int STATE_LOCK = -999;
    /** 操作失败**/
    public static final int OPT_FAILURE=-777;
    /**密码验证不通过**/
    public static final int PASSWORD_ERROR=-666;
    /**非法回滚,不符合回滚要求**/
    public static final int ROLL_BACK_IN_VALID=-555;
    /** 参数非法 **/
    public static final int PARAM_IN_VALID=-444;

    /** 不存在冻结记录**/
    public static final int RECORD_NOT_EXIT = -333;

    /** 充值超过上限**/
    public static final int MONEY_TOO_LARGER = -222;

    /** 钱包被冻结 **/
    public static final int STATE_FREEZE = -111;
    
    /** 成功 **/
    public static final int OPT_SUCCESS = 1;
}
