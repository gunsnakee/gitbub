package com.meiliwan.emall.imeiliwan.model.message.receive;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-6
 * Time: 下午5:16
 * 图片消息-接收
 */
public class ImageMsgReceive extends BaseMsgReceive {
    // 图片链接
    private String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
