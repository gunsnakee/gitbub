package com.meiliwan.emall.imeiliwan.model.message.receive;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-6
 * Time: 下午5:16
 * 链接消息-接收
 */
public class LinkMsgReceive extends BaseMsgReceive {
    // 消息标题
    private String title;
    // 消息描述
    private String description;
    // 消息链接
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
