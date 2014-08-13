package com.meiliwan.emall.datacenter;

import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-11-8
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
public class DataCenterMsgRecv implements MsgTaskWorker {
    @Override
    public void handleMsg(String msg) {
        //处理信息
        System.out.println("********************");
        System.out.println(msg);
    }
}
