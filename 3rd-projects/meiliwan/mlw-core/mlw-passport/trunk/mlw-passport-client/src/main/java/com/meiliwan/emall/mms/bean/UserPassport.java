package com.meiliwan.emall.mms.bean;

import com.google.gson.Gson;
import com.meiliwan.emall.core.bean.BaseEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;

public class UserPassport extends BaseEntity implements Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 7435472722364474283L;

    /**
     *
     */

    private Integer uid;

    @NotBlank(message="会员ID不能为空!")
    @Length(min=14,message="至少{min}个字符")
    private String userName;

    @NotBlank(message="昵称不能为空!")
    @Length(min=5,max=25,message="{min}-{max}个字符,可以是数字、中文、英文、特殊字符（不包含空格）")
    private String nickName;

    @NotBlank(message="邮箱地址不能为空!")
    @Length(min=5,max=25,message="{min}-{max}个字符")
    @Email
    private String email;

    @NotBlank(message="手机号码不能为空!")
    @Length(min=11,max=11,message="手机号码必须是{min}位数字")
    @Pattern(regexp = "^1(3|4|5|8)[0-9]{9}$")
    private String mobile;

    @NotBlank(message="登录密码不能为空!")
    @Length(min=6,max=20,message="{min}-{max}个字符")
    private String password;


    private String headUri;

    private Date createTime;

    private Short emailActive;

    private Short mobileActive;

    private Date loginTime;

    private String loginEquip;

    private String loginIp;

    private String loginEquipId;

    private Short state;

    public Short getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Short userLevel) {
        this.userLevel = userLevel;
    }

    private Short userLevel;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadUri() {
        // return "http://img.meiliwan.com/head/" + this.getId() + ".jpg";
        return headUri;
    }

    public void setHeadUri(String headUri) {
        this.headUri = headUri == null ? null : headUri.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getEmailActive() {
        return emailActive;
    }

    public void setEmailActive(Short emailActive) {
        this.emailActive = emailActive;
    }

    public Short getMobileActive() {
        return mobileActive;
    }

    public void setMobileActive(Short mobileActive) {
        this.mobileActive = mobileActive;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginEquip() {
        return loginEquip;
    }

    public void setLoginEquip(String loginEquip) {
        this.loginEquip = loginEquip == null ? null : loginEquip.trim();
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public String getLoginEquipId() {
        return loginEquipId;
    }

    public void setLoginEquipId(String loginEquipId) {
        this.loginEquipId = loginEquipId == null ? null : loginEquipId.trim();
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