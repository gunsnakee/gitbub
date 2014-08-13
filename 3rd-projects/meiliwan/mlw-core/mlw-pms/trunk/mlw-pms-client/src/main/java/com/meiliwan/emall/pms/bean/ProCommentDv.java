package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class ProCommentDv extends BaseEntity {
    private static final long serialVersionUID = 1875134399088107033L;
    private Integer id;

    private String score;

    private String content;

    private Date createTime;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "ProCommentDv{" +
                "id=" + id +
                ", score='" + score + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                '}';
    }
}