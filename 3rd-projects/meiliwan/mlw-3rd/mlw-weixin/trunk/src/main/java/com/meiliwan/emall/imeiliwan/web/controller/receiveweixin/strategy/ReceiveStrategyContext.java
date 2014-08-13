package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy;

import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 下午2:59
 * 接受微信处理 策略上下文
 */
public class ReceiveStrategyContext {
    //处理不同类型新消息消息的策略
    private IMsgtypeStrategy strategy;

    public ReceiveStrategyContext(IMsgtypeStrategy strategy) {
        this.strategy = strategy;
    }

    public void operate(HttpServletRequest request, HttpServletResponse response,BaseMsgReceive msgReceive){
        strategy.operate(request,response,msgReceive);
    }
}
