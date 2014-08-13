package com.meiliwan.emall.imeiliwan.model.message.notify;

/**
 * 微信告警消息
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-30
 * Time: 下午3:02
 * To change this template use File | Settings | File Templates.
 */
/*
数据格式如下：
<xml>
<AppId><![CDATA[wxf8b4f85f3a794e77]]></AppId>
<ErrorType>1001</ErrorType>
<Description><![CDATA[错诨描述]]></Description>
<AlarmContent><![CDATA[错诨详情]]></AlarmContent>
<TimeStamp>1393860740</TimeStamp>
<AppSignature><![CDATA[f8164781a303f4d5a944a2dfc68411a8c7e4fbea]]></AppSignatur
e>
<SignMethod><![CDATA[sha1]]></SignMethod>
</xml>
 */
public class WarnMsg {
    private String AppId;
    // 错误类型
    private String ErrorType;
    //错误描述
    private String Description;
    //错误详情
    private String AlarmContent;
    //时间戳
    private long TimeStamp;
    //安全签名
    private String AppSignature;
    //签名加密方式
    private String SignMethod;

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getErrorType() {
        return ErrorType;
    }

    public void setErrorType(String errorType) {
        ErrorType = errorType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAlarmContent() {
        return AlarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        AlarmContent = alarmContent;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
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
}
