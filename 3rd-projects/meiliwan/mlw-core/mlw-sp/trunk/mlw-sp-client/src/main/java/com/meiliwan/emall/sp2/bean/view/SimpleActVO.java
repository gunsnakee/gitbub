package com.meiliwan.emall.sp2.bean.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRule;
import com.meiliwan.emall.sp2.constant.ActType;
import com.meiliwan.emall.sp2.constant.ActState;

/**
 * Created by Sean on 13-12-24.
 */
public class SimpleActVO implements Serializable,Comparable {
    private static final long serialVersionUID = 8671905193305620197L;

    public SimpleActVO() {
    }

    public SimpleActVO(List<ActivityRule> actRule, int actId, String actName, ActType actType, ActState actState, String actDesc, long startTime, long endTime, Date createTime) {
        this.actRule = actRule;
        this.actId = actId;
        this.actName = actName;
        this.actType = actType;
        this.actState = actState;
        this.actDesc = actDesc;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createTime = createTime;
    }

    /**
     * 活动规则集合， 非持久化
     */
    protected transient List<ActivityRule> actRule; // (抽象)

    public List<ActivityRule> getActRule() {
        return actRule;
    }

    public void setActRule(List<ActivityRule> actRule) {
        this.actRule = actRule;
    }


    /**
     * 活动Id
     */
    protected int actId;
    /**
     * 活动名称
     */
    protected String actName;
    /**
     * 活动类型
     */
    protected ActType actType;

    /**
     * 活动类型
     */
    protected ActState actState;

    /**
     * 活动描述
     */
    protected String actDesc;
    /**
     * 活动文案
     */
    protected String actWenan;

    protected long startTime;

    protected long endTime;

    protected Date createTime = new Date();

    @Override
    public String toString() {
        return "{" +
                "actId=" + actId +
                ", actName=\\'" + actName + "\\'" +
                ", actRule=\\'" + actRule + "\\'" +
                ", actType=\\'" + actType +"\\'" +
                ", actDesc=\\'" + actDesc + "\\'" +
                ", startTime=\\'" + startTime + "\\'" +
                ", endTime=\\'" + endTime + "\\'" +
                ", createTime=\\'" + createTime +"\\'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleActVO sa2 = (SimpleActVO)o;
        if (actId != sa2.getActId()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return actId;
    }

    @Override
    public int compareTo(Object o) {
        SimpleActVO sa2 = (SimpleActVO)o;
        return (createTime.after(sa2.getCreateTime()) ? -1 : (createTime.equals(sa2.getCreateTime()) ? 0 : 1));
    }

    public String getActWenan() {
        return actWenan;
    }

    public void setActWenan(String actWenan) {
        this.actWenan = actWenan;
    }

    public ActState getActState() {
        return actState;
    }

    public void setActState(ActState actState) {
        this.actState = actState;
    }

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public ActType getActType() {
        return actType;
    }

    public void setActType(ActType actType) {
        this.actType = actType;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }
    
    public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
