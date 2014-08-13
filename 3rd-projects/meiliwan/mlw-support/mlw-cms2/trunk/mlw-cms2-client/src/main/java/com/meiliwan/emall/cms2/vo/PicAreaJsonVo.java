package com.meiliwan.emall.cms2.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 14-4-15
 * Time: 下午4:57
 * 图片区域 自定义属性 json Vo
 */
public class PicAreaJsonVo {
    //锚点图片
    private String  anchorPic;

    //图片列表
    private List<PicJsonVo> picList=new ArrayList<PicJsonVo>();


	public String getAnchorPic() {
        return anchorPic;
    }

    public void setAnchorPic(String anchorPic) {
        this.anchorPic = anchorPic;
    }

    public List<PicJsonVo> getPicList() {
        return picList;
    }

    public void setPicList(List<PicJsonVo> picList) {
        this.picList = picList;
    }
    
    public void addPicJsonVo(PicJsonVo pic){
    		picList.add(pic);
    }
}
