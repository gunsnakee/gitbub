package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class ProAction extends BaseEntity {
    private static final long serialVersionUID = 1586646843257650842L;
    private Integer proId;

    private Integer love;

    private Integer oneScoreNum;

    private Integer twoScoreNum;

    private Integer threeScoreNum;

    private Integer fourScoreNum;

    private Integer fiveScoreNum;

    private Float score;

    private Integer realSaleNum;

    private Integer showSaleNum;

    private Integer realViewNum;

    private Integer showViewNum;

    private Integer commentId;

    private Date updateTime = new Date();

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getOneScoreNum() {
        return oneScoreNum;
    }

    public void setOneScoreNum(Integer oneScoreNum) {
        this.oneScoreNum = oneScoreNum;
    }

    public Integer getTwoScoreNum() {
        return twoScoreNum;
    }

    public void setTwoScoreNum(Integer twoScoreNum) {
        this.twoScoreNum = twoScoreNum;
    }

    public Integer getThreeScoreNum() {
        return threeScoreNum;
    }

    public void setThreeScoreNum(Integer threeScoreNum) {
        this.threeScoreNum = threeScoreNum;
    }

    public Integer getFourScoreNum() {
        return fourScoreNum;
    }

    public void setFourScoreNum(Integer fourScoreNum) {
        this.fourScoreNum = fourScoreNum;
    }

    public Integer getFiveScoreNum() {
        return fiveScoreNum;
    }

    public void setFiveScoreNum(Integer fiveScoreNum) {
        this.fiveScoreNum = fiveScoreNum;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getRealSaleNum() {
        return realSaleNum;
    }

    public void setRealSaleNum(Integer realSaleNum) {
        this.realSaleNum = realSaleNum;
    }

    public Integer getShowSaleNum() {
        return showSaleNum;
    }

    public void setShowSaleNum(Integer showSaleNum) {
        this.showSaleNum = showSaleNum;
    }

    public Integer getRealViewNum() {
        return realViewNum;
    }

    public void setRealViewNum(Integer realViewNum) {
        this.realViewNum = realViewNum;
    }

    public Integer getShowViewNum() {
        return showViewNum;
    }

    public void setShowViewNum(Integer showViewNum) {
        this.showViewNum = showViewNum;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public int getTotalCmtNum() {
        return (this.getOneScoreNum() == null ? 0 : this.getOneScoreNum())
                + (this.getTwoScoreNum() == null ? 0 : this.getTwoScoreNum())
                + (this.getThreeScoreNum() == null ? 0 : this.getThreeScoreNum())
                + (this.getFourScoreNum() == null ? 0 : this.getFourScoreNum())
                + (this.getFiveScoreNum() == null ? 0 : this.getFiveScoreNum());
    }

    public double getAvgScore() {
        int totalNum = getTotalCmtNum();
        double avgScore = (totalNum <= 0 ? 0 : ((this.getOneScoreNum() == null ? 0 : this.getOneScoreNum())
                + (this.getTwoScoreNum() == null ? 0 : this.getTwoScoreNum() * 2)
                + (this.getThreeScoreNum() == null ? 0 : this.getThreeScoreNum() * 3)
                + (this.getFourScoreNum() == null ? 0 : this.getFourScoreNum() * 4)
                + (this.getFiveScoreNum() == null ? 0 : this.getFiveScoreNum() * 5)) / (totalNum * 1.0));

        return (Math.round(avgScore * 10)) / 10.0;
    }


    @Override
    public Integer getId() {
        return this.proId;
    }
}