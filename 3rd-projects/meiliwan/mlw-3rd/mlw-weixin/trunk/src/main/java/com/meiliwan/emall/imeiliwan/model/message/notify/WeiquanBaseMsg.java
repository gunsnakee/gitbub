package com.meiliwan.emall.imeiliwan.model.message.notify;

/**
 * 维权通知接口 信息
 * 用于接收：confirm / reject 这两种类型的消息
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-30
 * Time: 下午5:04
 * To change this template use File | Settings | File Templates.
 *
 *
 * <xml>
 <OpenId><![CDATA[111222]]></OpenId>
 <AppId><![CDATA[wwwwb4f85f3a797777]]></AppId>
 <TimeStamp> 1369743511</TimeStamp>
 <MsgType><![CDATA[confirm/reject]]></MsgType>
 <FeedBackId><![CDATA[5883726847655944563]]></FeedBackId>
 <Reason><![CDATA[商品质量有问题]]></Reason>
 <AppSignature><![CDATA[bafe07f060f22dcda0bfdb4b5ff756f973aecffa]]>
 </AppSignature>
 <SignMethod><![CDATA[sha1]]></ SignMethod >
 </xml>
 */
public class WeiquanBaseMsg {
    //支付该笔订单的用户 ID
    private String  OpenId;
    // 公众号 id
    private String  AppId;
    //时间戳
    private long  TimeStamp;
    /*
     通知类型
    request 用户提交投诉
    confirm 用户确认消除 投诉
    reject 用户拒绝消除投诉
     */
    private String  MsgType;
    //投诉单号
    private String  FeedBackId;
    //用户投诉原因
    private String  Reason;
    //签名
    private String  AppSignature;
    //签名加密方式
    private String  SignMethod;

    private String TransId;
    private String Solution;
    private String ExtInfo;

    private String  PicInfo;



    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getFeedBackId() {
        return FeedBackId;
    }

    public void setFeedBackId(String feedBackId) {
        FeedBackId = feedBackId;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getAppSignature() {
        return AppSignature;
    }

    public void setAppSignature(String appSignature) {
        AppSignature = appSignature;
    }

    public String getSignMethod() {
        return SignMethod;
    }

    public void setSignMethod(String signMethod) {
        SignMethod = signMethod;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public String getSolution() {
        return Solution;
    }

    public void setSolution(String solution) {
        Solution = solution;
    }

    public String getExtInfo() {
        return ExtInfo;
    }

    public void setExtInfo(String extInfo) {
        ExtInfo = extInfo;
    }

    public String getPicInfo() {
        return PicInfo;
    }

    public void setPicInfo(String picInfo) {
        PicInfo = picInfo;
    }
}
