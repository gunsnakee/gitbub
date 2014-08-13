package com.meiliwan.emall.imeiliwan.model.message.response;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:24
 *  音乐消息-发送被动响应消息
 */
public class MusicMsgResp extends BaseMsgResp{
    //音乐模型
    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}
