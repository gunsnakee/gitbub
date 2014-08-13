package com.meiliwan.emall.imeiliwan.model.message.response;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-10
 * Time: 下午2:05
 * 视频模型--发送被动响应消息
 */
public class Video {
    //通过上传多媒体文件，得到的id
    private String MediaId;
    //视频消息的标题
    private String Title;
    //视频消息的描述
    private String Description;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
