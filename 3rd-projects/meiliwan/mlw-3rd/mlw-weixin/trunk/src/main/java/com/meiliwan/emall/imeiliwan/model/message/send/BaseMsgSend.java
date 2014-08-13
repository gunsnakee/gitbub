package com.meiliwan.emall.imeiliwan.model.message.send;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-6
 * Time: 上午11:05
 * 发送客服消息基类（公众号 -> 微信服务器）
 */
public class BaseMsgSend {
    // 接收方帐号（普通用户openid ）
    private String toUser ;
    // 消息类型 (text/image/voice/video/music/news)
    private String msgType;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
