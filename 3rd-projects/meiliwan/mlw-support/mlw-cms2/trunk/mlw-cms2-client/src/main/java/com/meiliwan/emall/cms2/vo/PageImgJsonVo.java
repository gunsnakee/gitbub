package com.meiliwan.emall.cms2.vo;

/**
 * 专题页 页面设置相关信息json 对应的VO
 * User: wuzixin
 * Date: 14-4-15
 * Time: 下午2:17
 */
public class PageImgJsonVo {
    /**
     * 页面背景色值
     */
    private String bgColor;

    /**
     * 头图
     */
    private String headImg;
    
    private String html5HeadImg;

    /**
     * 锚点头图
     */
    private String mdHeadImg;

    /**
     * 返回顶部图片
     */
    private String returnTopImg;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getMdHeadImg() {
        return mdHeadImg;
    }

    public void setMdHeadImg(String mdHeadImg) {
        this.mdHeadImg = mdHeadImg;
    }

    public String getReturnTopImg() {
        return returnTopImg;
    }

    public void setReturnTopImg(String returnTopImg) {
        this.returnTopImg = returnTopImg;
    }

	public String getHtml5HeadImg() {
		return html5HeadImg;
	}

	public void setHtml5HeadImg(String html5HeadImg) {
		this.html5HeadImg = html5HeadImg;
	}
    
}
