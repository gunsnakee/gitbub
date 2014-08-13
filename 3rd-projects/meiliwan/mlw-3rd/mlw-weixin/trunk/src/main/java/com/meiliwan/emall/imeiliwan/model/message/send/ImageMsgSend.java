package com.meiliwan.emall.imeiliwan.model.message.send;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:24
 *  图片消息-发送客服消息
 */
public class ImageMsgSend extends BaseMsgSend{
    //图片消息
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
