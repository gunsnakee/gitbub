package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class ExamLog extends BaseEntity {
    private static final long serialVersionUID = -285967081184969652L;
    private Integer id;

    private Integer uid;

    private Short state;

    private String answerDate;

    private Integer topicCount;

    private Date createTime = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(String answerDate) {
        this.answerDate = answerDate;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Integer getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(Integer topicCount) {
        this.topicCount = topicCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}