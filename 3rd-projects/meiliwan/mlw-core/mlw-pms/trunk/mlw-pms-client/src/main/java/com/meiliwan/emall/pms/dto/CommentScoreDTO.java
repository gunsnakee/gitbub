package com.meiliwan.emall.pms.dto;

import java.io.Serializable;

/**
 * Created by guangdetang on 13-10-10.
 */
public class CommentScoreDTO implements Serializable {

    private static final long serialVersionUID = -8031418469291486806L;
    /** 评论分数类型（1好评，2中评，3差评）*/
    private int scoreType;
    /** 对应分数类型的数量*/
    private int counts;

    public int getScoreType() {
        return scoreType;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = scoreType;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }
}
