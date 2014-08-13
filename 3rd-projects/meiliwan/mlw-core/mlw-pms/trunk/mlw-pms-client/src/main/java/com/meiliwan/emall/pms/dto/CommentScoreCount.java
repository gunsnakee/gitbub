package com.meiliwan.emall.pms.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guangdetang on 13-10-10.
 */
public class CommentScoreCount implements Serializable {

    private static final long serialVersionUID = 2753853872917224577L;

    /** 评论总数量*/
    private int totalCount;
    private List<CommentScoreDTO> scoreDTOList;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<CommentScoreDTO> getScoreDTOList() {
        return scoreDTOList;
    }

    public void setScoreDTOList(List<CommentScoreDTO> scoreDTOList) {
        this.scoreDTOList = scoreDTOList;
    }
}
