package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;
import com.meiliwan.emall.sp2.dto.ExamAnswerVo;

import java.util.Date;
import java.util.List;

public class ExamTopic extends BaseEntity {
    private static final long serialVersionUID = 870845357645443306L;
    private Integer topicId;

    private String topicName;

    private String answerJson;

    private String correctVal;

    private Date createTime = new Date();

    private List<ExamAnswerVo> answers;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName == null ? null : topicName.trim();
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson == null ? null : answerJson.trim();
    }

    public String getCorrectVal() {
        return correctVal;
    }

    public void setCorrectVal(String correctVal) {
        this.correctVal = correctVal == null ? null : correctVal.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ExamAnswerVo> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ExamAnswerVo> answers) {
        this.answers = answers;
    }

    @Override
    public Integer getId() {
        return topicId;
    }
}