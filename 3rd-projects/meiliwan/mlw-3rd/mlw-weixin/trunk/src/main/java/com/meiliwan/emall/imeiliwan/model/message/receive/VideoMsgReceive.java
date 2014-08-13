package com.meiliwan.emall.imeiliwan.model.message.receive;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:16
 * 视频消息-接收
 */
public class VideoMsgReceive extends BaseMsgReceive {
    // 媒体ID
    private String mediaId;
    // 视频消息缩略图的媒体id
    private String thumbMediaId ;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }


}
