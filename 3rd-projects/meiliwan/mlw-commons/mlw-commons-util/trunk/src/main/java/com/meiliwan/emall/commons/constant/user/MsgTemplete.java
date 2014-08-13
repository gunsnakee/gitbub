package com.meiliwan.emall.commons.constant.user;

import com.meiliwan.emall.commons.util.ObjectUtils;

import java.util.Map;

/**
 * 信息提示模板
 * Created by jiawu.wu on 13-6-21.
 */
public enum MsgTemplete {

    PASSWORD("亲爱的 #nickName#： 您的新密码是#code#,登录后建议您到【我的美丽湾-帐号安全】中修改登录密码，谢谢您对美丽湾的支持！"),
    MOBILE_VALIDATE_CODE("亲爱的 #nickName#：您手机验证的验证码是：#code#，验证码5分钟后失效，请及时操作。"),
    FIND_PWD_MSG("亲爱的美丽湾用户，您申请找回密码的验证码是：#code#，验证码5分钟后失效，请及时操作。","code"),
    PAY_PWD_MSG("亲爱的 #nickName#：您修改支付密码的验证码是：#code#，验证码5分钟后失效，请及时操作。"),
    EAMIL_VALIDATE_CODE("亲爱的美丽湾用户，您在美丽湾的验证码为：#code#，验证码5分钟后失效，请及时操作。【美丽湾】","code");

    private String msg;
    private String key;

    MsgTemplete(){
        this.msg = this.name();
    }

    MsgTemplete(String str){
        this.msg = str;
    }

    MsgTemplete(String str,String key){
        this.msg = str;
        this.key = key;
    }

    public String getMsg(){
        return  msg;
    }

    /**
     * 根据key，用map中的value替换msg中的#key# ，此方法必须先用  MsgTemplete(String str) 构造
     * @param map
     * @return
     */
    public String getMsg(Map<String,String> map){
        String rsValue = this.msg;
        if(ObjectUtils.isAllObjNotNull(rsValue,map)){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key =   entry.getKey();
                if(rsValue.lastIndexOf("#"+key+"#")> -1){
                    rsValue = rsValue.replaceAll("#"+key+"#",entry.getValue());
                }
            }
        }
        return rsValue ;
    }

    /**
     * 根据key，替换msg中的#key#，为value,此方法必须用 MsgTemplete(String str,String key) 构造器
     * @param value
     * @return
     */
    public String getMsg(String value){
        String rsValue = this.msg;
        if(ObjectUtils.isAllObjNotNull(rsValue,this.key,value)){
            if(rsValue.lastIndexOf("#"+this.key+"#")>-1){
                rsValue = rsValue.replaceAll("#"+this.key+"#",value);
            }
        }
        return rsValue;
    }



}
