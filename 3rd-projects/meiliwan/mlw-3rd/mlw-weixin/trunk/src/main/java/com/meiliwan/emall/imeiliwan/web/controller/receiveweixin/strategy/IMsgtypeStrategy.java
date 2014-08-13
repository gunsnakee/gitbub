package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy;

import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 上午11:03
 * 微信推送消息策略接口
 */
public interface IMsgtypeStrategy {
    /**
     * 处理消息类型策略方法
     */
    public void operate(HttpServletRequest request, HttpServletResponse response,BaseMsgReceive msgReceive);
}
