package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy;

import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 上午11:26
 * 接收普通消息--视频消息
 */
public class VideoStrategy implements IMsgtypeStrategy {
    @Override
    public void operate(HttpServletRequest request, HttpServletResponse response,BaseMsgReceive msgReceive) {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println("接收普通消息--视频消息");
    }
}
