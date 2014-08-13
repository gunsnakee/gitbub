package com.meiliwan.emall.commons.web;

/**
 * Created by wenlepeng on 13-10-22.
 */
public enum PayPwdValidateResult {
    right("right","支付密码正确"),
    three_chance("thirdChance","还有三次试错机会"),
    two_chance("twoChance","还有两次试错机会"),
    one_chance("oneChance","还有一次试错机会"),
    zero_chance("zeroChance","帐号冻结24小时");

    private String code ;
    private String desc;

    private PayPwdValidateResult(String code,String desc){
       this.code = code;
       this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
