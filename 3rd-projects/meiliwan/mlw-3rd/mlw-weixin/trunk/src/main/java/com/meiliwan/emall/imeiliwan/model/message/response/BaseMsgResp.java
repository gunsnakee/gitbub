package com.meiliwan.emall.imeiliwan.model.message.response;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-6
 * Time: 上午11:05
 * 发送被动响应消息（公众号 -> 微信服务器）
 */
public class BaseMsgResp {
    // 接收方帐号（收到的OpenID）
    private String ToUserName ;
    // 开发者微信号
    private String FromUserName ;
    // 消息创建时间 （整型）
   // private Integer CreateTime;
    private String CreateTime;
    // 消息类型
    private String MsgType;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }


}
