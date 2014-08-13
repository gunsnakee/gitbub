package com.meiliwan.emall.pms.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * User: guangdetang
 * Date: 13-6-13
 * Time: 下午5:19
 */
public class CommentDTO {

    private String orderId;

    private Integer proId;

    private String proName;

    private String nickName;

    private Integer startUseful;

    private Integer endUseful;

    private Integer startUseless;

    private Integer endUseless;

    private String startTime;

    private String endTime;

    private Integer startScore;

    private Integer endScore;

    private String content;

    private Short isTop;

    private String isReply;

    private Short state;

    private Integer uid;

    private Short isAdminDel;

    private Short check; //  0未审核  1已审核
    
    private String startCreateTime;

    private String endCreateTime;

    public String getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(String startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public String getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Short getAdminDel() {
        return isAdminDel;
    }

    public void setAdminDel(Short adminDel) {
        isAdminDel = adminDel;
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

    public Integer getStartUseful() {
        return startUseful;
    }

    public void setStartUseful(Integer startUseful) {
        this.startUseful = startUseful;
    }

    public Integer getEndUseful() {
        return endUseful;
    }

    public void setEndUseful(Integer endUseful) {
        this.endUseful = endUseful;
    }

    public Integer getStartUseless() {
        return startUseless;
    }

    public void setStartUseless(Integer startUseless) {
        this.startUseless = startUseless;
    }

    public Integer getEndUseless() {
        return endUseless;
    }

    public void setEndUseless(Integer endUseless) {
        this.endUseless = endUseless;
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

    public Integer getStartScore() {
        return startScore;
    }

    public void setStartScore(Integer startScore) {
        this.startScore = startScore;
    }

    public Integer getEndScore() {
        return endScore;
    }

    public void setEndScore(Integer endScore) {
        this.endScore = endScore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Short getIsTop() {
        return isTop;
    }

    public void setIsTop(Short isTop) {
        this.isTop = isTop;
    }

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
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
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Short getCheck() {
        return check;
    }

    public void setCheck(Short check) {
        this.check = check;
    }
}
