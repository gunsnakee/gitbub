package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.content;

import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;
import com.meiliwan.emall.imeiliwan.model.message.response.TextMsgResp;
import com.meiliwan.emall.imeiliwan.util.WXSafeTool;
import com.meiliwan.emall.imeiliwan.util.xml.MessageToXmlUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * 内容提供者
 * 提供各种推送内容
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-11
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class ContentProvider {

    /**
     * 获取账号绑定推送XML数据
     * @param msgReceive
     * @return
     */
    public static String getBdXMl(BaseMsgReceive msgReceive,String pre,String later){
        String bandingUrl = WXSafeTool.getSafeBandingUrl(msgReceive.getFromUserName());
        TextMsgResp textMsgResp = new TextMsgResp();
        textMsgResp.setToUserName(msgReceive.getFromUserName());
        textMsgResp.setFromUserName(msgReceive.getToUserName());
        textMsgResp.setCreateTime(MessageToXmlUtil.NO_CDATA + new Date().getTime());
        textMsgResp.setMsgType("text");
        //  您还没有绑定美丽湾账号，点击绑定，快速绑定美丽湾账号，享受湾湾高端大气上档次的服务
        textMsgResp.setContent(pre+"<a href='"+bandingUrl+"'>点击绑定</a>"+later);
        String xml = MessageToXmlUtil.textMessageToXml(textMsgResp);
        return xml;
    }

    /**
     * 获取带超链接文本消息XML数据
     * 如：美丽湾欢迎你<a href='m.meiliwan.com'>单击绑定</a>,美丽湾给你提供东南亚风情的服务
     *  pre+"<a href='"+url+"'>"+urlMsg+"</a>"+later
     * @param msgReceive
     * @param pre
     * @param url
     * @param urlMsg
     * @param later
     * @return
     */
    public static String getHrefMsgXMl(BaseMsgReceive msgReceive,String pre,String url,String urlMsg,String later){
        TextMsgResp textMsgResp = new TextMsgResp();
        textMsgResp.setToUserName(msgReceive.getFromUserName());
        textMsgResp.setFromUserName(msgReceive.getToUserName());
        textMsgResp.setCreateTime(MessageToXmlUtil.NO_CDATA + new Date().getTime());
        textMsgResp.setMsgType("text");
        textMsgResp.setContent(pre+"<a href='"+url+"'>"+urlMsg+"</a>"+later);
        String xml = MessageToXmlUtil.textMessageToXml(textMsgResp);
        return xml;
    }

    /**
     * 获取账号绑定推送XML数据
     * @param msgReceive
     * @return
     */
    public static String getTextXMl(BaseMsgReceive msgReceive,String text){
        String bandingUrl = WXSafeTool.getSafeBandingUrl(msgReceive.getFromUserName());
        TextMsgResp textMsgResp = new TextMsgResp();
        textMsgResp.setToUserName(msgReceive.getFromUserName());
        textMsgResp.setFromUserName(msgReceive.getToUserName());
        textMsgResp.setCreateTime(MessageToXmlUtil.NO_CDATA + new Date().getTime());
        textMsgResp.setMsgType("text");
        //  您还没有绑定美丽湾账号，点击绑定，快速绑定美丽湾账号，享受湾湾高端大气上档次的服务
        textMsgResp.setContent(text);
        String xml = MessageToXmlUtil.textMessageToXml(textMsgResp);
        return xml;
    }

    /**
     * 获取关键字 回复内容
     * @param msgReceive
     * @return
     */
    public static String getKeyWordReturnXMl(BaseMsgReceive msgReceive){
        String xml = null;
        if(msgReceive!=null && StringUtils.isNotBlank(msgReceive.getContent())){
            KeyWordEnum keyWordEnum =KeyWordEnum.getKeyWordEnum(msgReceive.getContent());
            if (keyWordEnum!=null){
                TextMsgResp textMsgResp = new TextMsgResp();
                textMsgResp.setToUserName(msgReceive.getFromUserName());
                textMsgResp.setFromUserName(msgReceive.getToUserName());
                textMsgResp.setCreateTime(MessageToXmlUtil.NO_CDATA + new Date().getTime());
                textMsgResp.setMsgType("text");
                textMsgResp.setContent(keyWordEnum.getReturnMsg());//获取回复消息
                xml = MessageToXmlUtil.textMessageToXml(textMsgResp);
            }
        }
        return xml;
    }
}
