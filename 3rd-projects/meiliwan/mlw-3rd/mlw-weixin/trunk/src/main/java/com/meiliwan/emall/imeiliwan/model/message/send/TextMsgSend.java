package com.meiliwan.emall.imeiliwan.model.message.send;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:24
 *  文本消息-发送客服消息
 */
public class TextMsgSend extends BaseMsgSend{
    //文本消息
    private Text text;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
