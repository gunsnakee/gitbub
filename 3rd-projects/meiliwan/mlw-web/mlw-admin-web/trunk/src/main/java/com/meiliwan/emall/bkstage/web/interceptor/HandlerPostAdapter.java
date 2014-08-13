package com.meiliwan.emall.bkstage.web.interceptor;

import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;
import com.meiliwan.emall.pms.constant.Constant;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-3-6
 * Time: 下午5:41
 */
public class HandlerPostAdapter extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        int cacheState = ServletRequestUtils.getIntParameter(request, "cacheState", 0);
        int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
        int[] ids = null;
        try {
            ids = ServletRequestUtils.getRequiredIntParameters(request, "proId");
        } catch (Exception e) {
        }
        List<Integer> proIds = new ArrayList<Integer>();
        if (ids != null && ids.length > 0) {
            for (Integer id : ids) {
                proIds.add(id);
            }
        } else {
            int[] skuIds = null;
            try {
                skuIds = ServletRequestUtils.getRequiredIntParameters(request, "ids");
            } catch (Exception e) {
            }
            if (skuIds != null && skuIds.length > 0) {
                for (Integer id : skuIds) {
                    proIds.add(id);
                }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("spuId", spuId);
        map.put("ids", proIds);
        map.put("cacheState", cacheState);
        System.out.println("发送消息更新商品详情页缓存，spuId:" + spuId + ",proIds:" + proIds.toString());
        MsgSender.getInstance(MqModel.PRODUCT).send(MqModel.PRODUCT, "spuOrskuUp", map.toString());
    }
}
