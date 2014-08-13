package com.meiliwan.emall.mms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class UserForeign  extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2880473917412580011L;

	private Integer uid;

    private String source;

    private String sourceKey;

    private String foreignUid;

    private Short state;

    //
    private String unionType;
    
    private Date unionLoginTime;
    
    
	public String getUnionType() {
		return unionType;
	}

	public void setUnionType(String unionType) {
		this.unionType = unionType;
	}

	public Date getUnionLoginTime() {
		return unionLoginTime;
	}

	public void setUnionLoginTime(Date unionLoginTime) {
		this.unionLoginTime = unionLoginTime;
	}

	public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey == null ? null : sourceKey.trim();
    }

    public String getForeignUid() {
        return foreignUid;
    }

    public void setForeignUid(String foreignUid) {
        this.foreignUid = foreignUid == null ? null : foreignUid.trim();
    }

	@Override
	public Integer getId() {
		return uid;
	}
}