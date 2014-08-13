package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy;

import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 上午11:14
 *   接收普通消息--图片消息
 *   图片类消息目前还未未有更好的业务，建议直接走客服推送服务。
 */
public class ImageStrategy implements IMsgtypeStrategy {
    @Override
    public void operate(HttpServletRequest request, HttpServletResponse response,BaseMsgReceive msgReceive) {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println("接收普通消息--图片消息");
    }
}
