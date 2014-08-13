package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;
import org.apache.commons.lang.StringUtils;

public class ProBrand extends BaseEntity{
	
    private static final long serialVersionUID = 6305675847879845885L;
    private Integer brandId;

    private String name;

    private String enName;

    private String otherName;

    private String brandUri;

    private String descp;

    private String logoUri;

    private String firstChar;

    private short state;
    //三级类目
    private int categoryId;
    
    /**
     * 增加时的必填字段
     * @return
     */
    public boolean isRequiredFieldNotNull(){
    		if(StringUtils.isEmpty(getName())
		||StringUtils.isEmpty(getFirstChar())){
    			return false;
    		}
    		return true;
    }
    public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName == null ? null : otherName.trim();
    }

    public String getBrandUri() {
        return brandUri;
    }

    public void setBrandUri(String brandUri) {
        this.brandUri = brandUri == null ? null : brandUri.trim();
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri == null ? null : logoUri.trim();
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }

    @Override
    public Integer getId() {
        return this.brandId;
    }

	@Override
	public String toString() {
		return "ProBrand [brandId=" + brandId + ", name=" + name + ", enName="
				+ enName + ", otherName=" + otherName + ", brandUri="
				+ brandUri + ", descp=" + descp + ", logoUri=" + logoUri
				+ ", firstChar=" + firstChar + ", state=" + state
				+ ", categoryId=" + categoryId + "]";
	}
    public static void main(String[] args) {
		System.out.println(new ProBrand());
	}
    
}