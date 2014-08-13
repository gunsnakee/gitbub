package com.meiliwan.emall.pms.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * User: guangdetang
 * Date: 13-6-13
 * Time: 下午5:19
 */
public class ConsultDTO {

    private String content;

    private Integer proId;

    private String proName;

    private String nickName;

    private Integer consultType;

    private String startTime;

    private String endTime;

    private Short isWebVisible;

    private String isReply;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getConsultType() {
        return consultType;
    }

    public void setConsultType(Integer consultType) {
        this.consultType = consultType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Short getIsWebVisible() {
        return isWebVisible;
    }

    public void setIsWebVisible(Short isWebVisible) {
        this.isWebVisible = isWebVisible;
    }

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
