package com.meiliwan.emall.imeiliwan.comments.msgstrategy;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 下午1:57
 *  接受微信消息策略枚举类
 *
 *  接收事件推送
 *
 */
public enum ReceiveMsgtpyeEnum {
    TEXT("text","com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.TextStrategy","文本消息"),
    IMAGE("image","com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.ImageStrategy","图片消息"),
    LOCATION("location","com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.LocationStrategy","地理位置消息"),
    LINK("link","com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.LinkStrategy","链接消息"),
    VIDEO("video","com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.VideoStrategy","视频消息"),
    VOICE("voice","com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.VoiceSrategy","语音消息"),
    EVENT("event","com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.EventSrategy","事件推送");

    private String msgType;// 消息类型
    private String strategyClass;// 具体消息策略类
    private String desc; //策略描述

    private ReceiveMsgtpyeEnum(String msgType, String strategyClass, String desc) {
        this.msgType = msgType;
        this.strategyClass = strategyClass;
        this.desc = desc;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getStrategyClass() {
        return strategyClass;
    }

    public void setStrategyClass(String strategyClass) {
        this.strategyClass = strategyClass;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 依据msgType返回ReceiveMsgtpyeEnum对象
     *
     * @param msgType
     * @return
     */
    public static ReceiveMsgtpyeEnum getReceiveMsgtpyeEnum(String msgType) {
        ReceiveMsgtpyeEnum[] receiveMsgtpyeEnums = ReceiveMsgtpyeEnum.values();
        for (ReceiveMsgtpyeEnum obj : receiveMsgtpyeEnums) {
            if (obj.msgType.equals(msgType)) {
                return obj;
            }
        }
        return null;
    }
}
