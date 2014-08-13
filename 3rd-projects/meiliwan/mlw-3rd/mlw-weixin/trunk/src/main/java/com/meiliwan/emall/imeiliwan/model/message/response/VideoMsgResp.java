package com.meiliwan.emall.imeiliwan.model.message.response;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:24
 *  视频消息-发送被动响应消息
 */
public class VideoMsgResp extends BaseMsgResp{
    //视频模型
    private Video Video;

    public Video getVideo() {
        return Video;
    }

    public void setVideo(Video video) {
        Video = video;
    }
}
