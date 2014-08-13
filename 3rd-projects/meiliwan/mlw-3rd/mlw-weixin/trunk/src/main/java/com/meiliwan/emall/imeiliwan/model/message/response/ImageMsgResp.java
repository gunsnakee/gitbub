package com.meiliwan.emall.imeiliwan.model.message.response;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:24
 *  图片消息-发送被动响应消息
 */
public class ImageMsgResp extends BaseMsgResp{
    //图片模型
    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        Image = image;
    }
}
