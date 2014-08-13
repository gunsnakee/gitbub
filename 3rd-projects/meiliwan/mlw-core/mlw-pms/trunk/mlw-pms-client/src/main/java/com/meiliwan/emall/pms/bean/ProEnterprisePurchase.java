package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;
import java.util.Date;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 企业采购管理
 * create by yiyou.luo 2013-6-18
 *
 */
public  class ProEnterprisePurchase extends BaseEntity {

    private static final long serialVersionUID = 1690417934520186000L;
    private Integer id;
    
    @NotBlank(message="联系人姓名不能为空!")@Pattern(regexp="[\\u4e00-\\u9fa5][a-zA-Z\\s\\.]{2,20}",message="联系人姓名,2-20个字符,可以是中文,英文")
    private String contact;
    
    @NotBlank(message="联系人电话不能为空!")@Pattern(regexp="[0-9]{7,11}",message="联系人电话,7-11个数字")
    private String contactPhone;
    
    @NotBlank(message="公司名称不能为空!")@Length(min=4,max=50,message="公司名称4-50个字符")
    private String enterprise;
    
    @NotBlank(message="公司地址不能为空!")@Length(min=4,max=80,message="公司地址4-80个字符")
    private String enterpriseAddr;
    
    @NotBlank(message="采购需求不能为空!")@Length(min=4,max=400,message="采购需求4-400个字符")
    private String purchaseDemand;

    private Short isRead;

    private Date createTime;

    private Date updateTime;

    private Short isDel;

    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise == null ? null : enterprise.trim();
    }

    public String getEnterpriseAddr() {
        return enterpriseAddr;
    }

    public void setEnterpriseAddr(String enterpriseAddr) {
        this.enterpriseAddr = enterpriseAddr == null ? null : enterpriseAddr.trim();
    }

    public String getPurchaseDemand() {
        return purchaseDemand;
    }

    public void setPurchaseDemand(String purchaseDemand) {
        this.purchaseDemand = purchaseDemand == null ? null : purchaseDemand.trim();
    }

    public Short getIsRead() {
        return isRead;
    }

    public void setIsRead(Short isRead) {
        this.isRead = isRead;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getIsDel() {
        return isDel;
    }

    public void setIsDel(Short isDel) {
        this.isDel = isDel;
    }
}