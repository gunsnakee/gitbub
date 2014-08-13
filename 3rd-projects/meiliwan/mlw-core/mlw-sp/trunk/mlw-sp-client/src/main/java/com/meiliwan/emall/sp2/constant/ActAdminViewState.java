package com.meiliwan.emall.sp2.constant;

/**
 * Created by yiyou.luo on 13-12-30.
 *
 * 活动列表页显示状态
 *未上线：0
 *未开始(上线)：10
 *正在进行（(上线）：11
 *已结束（(上线）： 12
 *已取消（下线） ：-1
 */
public enum ActAdminViewState {

    CREATED(0, "未上线"),
    UNSTART(10, "未开始"),
    GOING(11, "正在进行"),
    END(12, "已结束"),
    DOWN(-1, "已取消");

    private String desc;
    private short state;
    private ActAdminViewState(int state, String desc){
        this.state = (short)state;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public short getState() {
        return state;
    }

    public static ActAdminViewState getByCode(int stateCode){
        ActAdminViewState[] stateArr = ActAdminViewState.values();
        for(ActAdminViewState actState : stateArr){
            if((short)stateCode == actState.getState()){
                return actState;
            }
        }

        return null;
    }
}
