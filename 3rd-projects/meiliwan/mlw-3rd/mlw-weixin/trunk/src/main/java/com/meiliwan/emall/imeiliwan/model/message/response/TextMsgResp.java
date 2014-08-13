package com.meiliwan.emall.imeiliwan.model.message.response;

import com.meiliwan.emall.imeiliwan.model.message.send.BaseMsgSend;
import com.meiliwan.emall.imeiliwan.model.message.send.Text;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:24
 *  文本消息-发送被动响应消息
 */
public class TextMsgResp extends BaseMsgResp{
    //回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
