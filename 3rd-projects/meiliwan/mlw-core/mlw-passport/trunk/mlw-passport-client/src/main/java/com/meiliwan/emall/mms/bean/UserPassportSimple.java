package com.meiliwan.emall.mms.bean;

import com.google.gson.Gson;
import com.meiliwan.emall.core.bean.BaseEntity;

public class UserPassportSimple extends BaseEntity implements Cloneable {


    private static final long serialVersionUID = 7435472722364474283L;

    private Integer uid;

    private String userName;

    private String nickName;

    private String email;

    private String mobile;

    private Short state;


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    @Override
    public Integer getId() {
        return uid;
    }

    @Override
    public String toString(){
        if(this == null){
            return "";
        }else{
            return new Gson().toJson(this);
        }
    }
}