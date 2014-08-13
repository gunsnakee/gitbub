package com.meiliwan.emall.commons.constant.base;

import com.meiliwan.emall.commons.constant.user.UserCommon;
import com.meiliwan.emall.commons.util.MailUtils;

import java.util.List;
import java.util.Map;

/**
 * 邮件发送所需参数实体类
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-8-7
 * Time: 下午2:21
 */
public class MailEntity {

    public MailEntity(){
        this.sender= MailUtils.getSend();
        this.senderName = UserCommon.MLW_EMAIL_SHOWNAME;
    }

    /**
     * 邮件发送者的email地址
     */
   private String sender;
    /**
     * 邮件发送者的名称
     */
   private String senderName;
    /**
     * 邮件接收者的地址，多个地址之间用,隔开
     */
   private String receivers;
    /**
     * 邮件标题
     */
   private String title;
    /**
     * 邮件内容
     */
   private String content;



    /**
     * url数据资源,map的key包含两个cid、url，cid：为资源ID，url为数据资源的url
     *
    */
   private List<Map<String,String>> urlDataSources;

    public List<Map<String, String>> getUrlDataSources() {
        return urlDataSources;
    }

    public void setUrlDataSources(List<Map<String, String>> urlDataSources) {
        this.urlDataSources = urlDataSources;
    }

    public String getSender() {
        return  sender;
    }

    public MailEntity setSender(String sender) {
        this.sender = (sender==null)?"":sender.trim();
        return this;
    }

    public String getSenderName() {
        return  senderName;
    }

    public MailEntity setSenderName(String senderName) {
        this.senderName = (senderName==null)?"":senderName;
        return this;
    }

    public String getReceivers() {
        return receivers;
    }

    public MailEntity setReceivers(String receivers) {
        this.receivers = (receivers==null)?"":receivers.trim();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MailEntity setTitle(String title) {
        this.title = (title==null)?"":title.trim();
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailEntity setContent(String content) {
        this.content = (content==null)?"":content.trim();
        return this;
    }
}
