package com.meiliwan.emall.sp2.constant;

/**
 * Created by xiongyu on 13-12-24.
 */
public enum PrivilegeType {

    SINGLE("单品优惠"),
    MULTI("多品优惠");

    private String desc;
    private PrivilegeType(String desc){
        this.desc = desc;
    }

    public enum Single {
        DOWN_PRICE("直降"), GIVE_PRODUCT("赠品"), GIVE_COUPON("赠券");
        private String desc;
        private Single(String desc){
            this.desc = desc;
        }
    }

    public enum Multi {
        DOWN_PRICE("直降"), GIVE_PRODUCT("赠品"), GIVE_COUPON("赠券");
        private String desc;
        private Multi(String desc){
            this.desc = desc;
        }
    }

    @Override
    public String toString() {
        return "{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
