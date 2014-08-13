package com.meiliwan.emall.mms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

public class UserRecvAddr extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1120504663953274574L;

	private Integer recvAddrId;

    private Integer uid;
    @NotBlank(message="省直辖市不可为空!") @Length(min=2,max=128)
    private String province;
    @NotBlank(message="省直辖市编码不可为空!") @Length(min=4,max=20)
    private String provinceCode;

    @NotBlank(message="市不可为空!") @Length(min=1,max=128)
    private String city;
    @NotBlank(message="市编码不可为空!") @Length(min=4,max=20)
    private String cityCode;
    @NotBlank(message="区/县不可为空!") @Length(min=2,max=256)
    private String county;
    @NotBlank(message="区/县编码不可为空!") @Length(min=4,max=20)
    private String countyCode;

    @Length(max=256)
    private String town;
    @Length(max=20)
    private String townCode;

    @NotBlank(message="详细地址不可为空!") @Length(min=2,max=512)
    private String detailAddr;
    @NotBlank(message="收货人不可为空!") @Length(min=2,max=128)
    private String recvName;
    @Pattern(regexp="([1]{1}[0-9]{10})?",message="手机号码由11位数字组成，并以1开头!")
    private String mobile;
    @Length(max=32)
    private String phone;
    @Email(message="不是合法的邮件地址")
    private String email;

    private Byte isDefault;

    private Date createTime;

    @Pattern(regexp="(^[0-9]{6}$)?",message="邮政编码由六位数字组成!")
    private String zipCode;

    private Byte isDel;

    public Integer getRecvAddrId() {
        return recvAddrId;
    }

    public void setRecvAddrId(Integer recvAddrId) {
        this.recvAddrId = recvAddrId;
    }

    @Min(1)
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? "" : province.trim();
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? "" : city.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? "" : county.trim();
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode == null ? null : countyCode.trim();
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town == null ? null : town.trim();
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode == null ? null : townCode.trim();
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr == null ? null : detailAddr.trim();
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName == null ? null : recvName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.recvAddrId;
	}
}