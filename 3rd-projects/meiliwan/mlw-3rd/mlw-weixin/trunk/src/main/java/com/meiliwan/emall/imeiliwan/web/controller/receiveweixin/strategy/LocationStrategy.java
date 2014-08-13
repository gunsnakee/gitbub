package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy;

import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 上午11:28
 * 接收普通消息--地理位置消息
 * 可以过去用户发的地理位置坐标做相关关联业务，如：
 * 天气查询，酒店预订。房屋搜索。。。
 * 配送区域查询  地理坐标所在地特色商品搜索。。。
 */
public class LocationStrategy implements IMsgtypeStrategy {
    @Override
    public void operate(HttpServletRequest request, HttpServletResponse response,BaseMsgReceive msgReceive) {
        System.out.println("接收普通消息--地理位置消息");

    }
}
