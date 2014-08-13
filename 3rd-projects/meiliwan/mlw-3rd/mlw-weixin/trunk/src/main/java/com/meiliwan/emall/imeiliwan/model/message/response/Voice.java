package com.meiliwan.emall.imeiliwan.model.message.response;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-10
 * Time: 下午2:01
 *语音模型--发送被动响应消息
 */
public class Voice {
    //通过上传多媒体文件，得到的id
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
