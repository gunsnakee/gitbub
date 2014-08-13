package com.meiliwan.emall.oms.constant;

/**
 * 退换货申请创建方式 枚举
 * Created by Sean on 13-10-4.
 */
public enum RetordCreateType {
    CREATE_TYPE_USER(0, "用户创建"),
    CREATE_TYPE_ADMIN(1, "管理员创建");

    private Integer code;
    private String desc;

    private RetordCreateType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
