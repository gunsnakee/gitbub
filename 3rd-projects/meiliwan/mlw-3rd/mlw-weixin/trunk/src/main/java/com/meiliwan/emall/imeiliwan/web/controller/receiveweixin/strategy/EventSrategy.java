package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.imeiliwan.model.message.receive.BaseMsgReceive;
import com.meiliwan.emall.imeiliwan.util.WXSafeTool;
import com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.content.ContentProvider;
import com.meiliwan.emall.mms.bean.UserForeign;
import com.meiliwan.emall.mms.client.UserForeignClient;
import com.meiliwan.emall.oms.bean.OrdTransport;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.client.OrdTransportClient;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-29
 * Time: 上午11:24
 * 接收事件推送
 */
public class EventSrategy implements  IMsgtypeStrategy {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(EventSrategy.class);
    @Override
    public void operate(HttpServletRequest request, HttpServletResponse response,BaseMsgReceive msgReceive) {
        logger.info("接收事件推送",msgReceive,null);
        if(msgReceive!=null && StringUtils.isNotBlank(msgReceive.getEvent())){
            if("subscribe".equals(msgReceive.getEvent())){
                //关注事 业务处理
                 doSubscribe(response,msgReceive);
            }
            if("unsubscribe".equals(msgReceive.getEvent())){  //取消关注事件

            }
            if("CLICK".equals(msgReceive.getEvent())) {    //自定义菜单事件
                doClick(response,msgReceive);
            }
        }
    }


    /**
     * 用户关注消息处理
     * @param response
     * @param msgReceive
     */
    private void doSubscribe(HttpServletResponse response,BaseMsgReceive msgReceive){
        try {
            String xml = ContentProvider.getBdXMl(msgReceive,"湾湾终于等到你了！","，快速绑定美丽湾账号，享受湾湾高端大气上档次的服务");
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(xml);
            response.getWriter().close();
            logger.info("用户关注事件回复完毕",msgReceive,null);
        } catch (IOException e) {
            Map map = new HashMap();
            map.put("errorMsg","用户关注事件回复异常");
            map.put("msgReceive",msgReceive);
            logger.error(e,map,null);
        }
    }

    /**
     * 自定义菜单事件
     * @param response
     * @param msgReceive
     */
    private void doClick(HttpServletResponse response,BaseMsgReceive msgReceive){
        if(StringUtils.isNotBlank(msgReceive.getEventKey())){
            //点击我的订单
            if("my_order".equals(msgReceive.getEventKey())){
                doClickMyOrd(response,msgReceive);
            }
            //点击物流查询
            if("logistics_query".equals(msgReceive.getEventKey())){
                doClickLogisticsQuery(response,msgReceive);
            }
            //点击 app下载
            if("app_download".equals(msgReceive.getEventKey())){
                doAppDownload(response,msgReceive);
            }
        }
    }

    /**
     * 自定义菜单事件--我的订单
     * 判断账号是否已经绑定：
     * 1、未绑定：推送提示绑定信息
     * 2、已经绑定：推送进入订单按钮
     * @param response
     * @param msgReceive
     */
    private void doClickMyOrd(HttpServletResponse response,BaseMsgReceive msgReceive){
        try {
            String xml = null; ContentProvider.getBdXMl(msgReceive,"湾湾终于等到你了！","，快速绑定美丽湾账号，享受湾湾高端大气上档次的服务");
            if(msgReceive!=null && StringUtils.isNotBlank(msgReceive.getFromUserName())){
                UserForeign userForeign = UserForeignClient.getForeignByFid(msgReceive.getFromUserName(),"weixin");
                //一、已经绑定
                if(userForeign!=null && userForeign.getUid()>0 && userForeign.getState()==1){
                    //查看用户是否y订单数据
                    OrderQueryDTO dto = new OrderQueryDTO();
                    dto.setUid(userForeign.getUid());
                    PagerControl<OrdDTO> pc = OrdClient.getOrderListSortCreateTime(dto, new PageInfo(), false);

                    if(pc!=null && pc.getEntityList()!=null && pc.getEntityList().size()>0){
                        //1、有订单 进入h5订单列表
                        xml = ContentProvider.getHrefMsgXMl(msgReceive, "查看订单",
                                WXSafeTool.TO_ORDLIST_RESP_URL, "点击进入", "");
                    }
                    else{
                        //2、没有订单的 提示进入美丽湾 H5
                        xml = ContentProvider.getHrefMsgXMl(msgReceive, "您目前还没有订单，现在就去美丽湾看看吧，给我一次为您服务的机会。",
                                WXSafeTool.MLW_M_URL, "点击进入美丽湾", "");
                    }
                }else{
                    //二、未绑定
                    xml = ContentProvider.getBdXMl(msgReceive,"您还没有绑定美丽湾账号，","，快速绑定美丽湾账号，享受湾湾高端大气上档次的服务");
                }
            }
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(xml);
            response.getWriter().close();
            logger.info("自定义菜单事件-我的订单-回复完毕",msgReceive,null);
        } catch (IOException e) {
            Map map = new HashMap();
            map.put("errorMsg","自定义菜单事件-我的订单-回复异常");
            map.put("msgReceive",msgReceive);
            logger.error(e,map,null);
        }
    }

    /**
     * 自定义菜单事件--物流查询
     * 判断账号是否已经绑定：
     * 1、未绑定：推送提示绑定信息
     * 2、已经绑定：推送物流信息
     *     a、无在途订单
     *     b、有在途订单，推送订单物流信息
     * @param response
     * @param msgReceive
     */
    private void doClickLogisticsQuery(HttpServletResponse response,BaseMsgReceive msgReceive){
        try {
            String xml = null; ContentProvider.getBdXMl(msgReceive,"湾湾终于等到你了！","，快速绑定美丽湾账号，享受湾湾高端大气上档次的服务");
            if(msgReceive!=null && StringUtils.isNotBlank(msgReceive.getFromUserName())){
                UserForeign userForeign = UserForeignClient.getForeignByFid(msgReceive.getFromUserName(),"weixin");
                //一、已经绑定
                if(userForeign!=null && userForeign.getUid()>0 && userForeign.getState()==1){
                    //查看用户是否无在途订单 （就是等待确认收货状态）
                    OrderQueryDTO dto = new OrderQueryDTO();
                    dto.setUid(userForeign.getUid());
                    dto.setOrderItemStatus(OrdITotalStatus.ORDI_CONSINGMENT.getCode());
                    PagerControl<OrdDTO> pc = OrdClient.getOrderListSortCreateTime(dto, new PageInfo(), false);

                    if(pc!=null && pc.getEntityList()!=null && pc.getEntityList().size()>0){
                        //1、有在途订单的 获取 物流信息
                        Map<String,String> logicInfo = getLogicMsg( pc.getEntityList()) ;
                        if(logicInfo!=null){
                            String logicMsg = "您共有"+logicInfo.get("ordNum")+"件在途 订单：</br>"+logicInfo.get("logicMsg");
                            xml = ContentProvider.getTextXMl(msgReceive,logicMsg);
                        }else {
                            //2、没有在途订单 提示进入美丽湾 H5
                            xml = ContentProvider.getHrefMsgXMl(msgReceive, "您目前没有在途的订单，现在就去美丽湾看看吧，给我一次为您服务的机会",
                                    WXSafeTool.MLW_M_URL, "点击进入美丽湾", "");
                        }
                    }
                    else{
                        //2、没有在途订单 提示进入美丽湾 H5
                        xml = ContentProvider.getHrefMsgXMl(msgReceive, "您目前没有在途的订单，现在就去美丽湾看看吧，给我一次为您服务的机会",
                                WXSafeTool.MLW_M_URL, "点击进入美丽湾", "");
                    }
                }else{
                    //二、未绑定
                    xml = ContentProvider.getBdXMl(msgReceive,"您还没有绑定美丽湾账号，","，快速绑定美丽湾账号，享受湾湾高端大气上档次的服务");
                }
            }
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(xml);
            response.getWriter().close();
            logger.info("自定义菜单事件-物流查询-回复完毕",msgReceive,null);
        } catch (IOException e) {
            Map map = new HashMap();
            map.put("errorMsg","自定义菜单事件-我的订单-回复异常");
            map.put("msgReceive",msgReceive);
            logger.error(e,map,null);
        }
    }

    /**
     * 自定义菜单事件--app下载
     * 推送 app下载链接
     * @param response
     * @param msgReceive
     */
    private void doAppDownload(HttpServletResponse response,BaseMsgReceive msgReceive){
        try {
            String xml = null; ContentProvider.getHrefMsgXMl(msgReceive,"下载美丽湾手机客户端抢购更快更便捷，下载地址：",
                    "http://app.qq.com/#id=detail&appid=100491211","Android用户请点击此链接","");
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(xml);
            response.getWriter().close();
            logger.info("自定义菜单事件-app下载链接-回复完毕",msgReceive,null);
        } catch (IOException e) {
            Map map = new HashMap();
            map.put("errorMsg","自定义菜单事件-app下载链接-回复异常");
            map.put("msgReceive",msgReceive);
            logger.error(e,map,null);
        }
    }

    private Map<String,String> getLogicMsg(List<OrdDTO> list){
        Integer ordNum = 0; //
        StringBuffer logicMsg  = new StringBuffer(); //物流信息
        if(list!=null){
            for (OrdDTO ord:list){
                if(StringUtils.isNotBlank(ord.getOrderId())){
                    List<OrdTransport> otList = OrdTransportClient.getListByOrderId(ord.getOrderId());
                    if(otList!=null && otList.size()>0){   //取最后一条是最新的
                        OrdTransport ot = otList.get(otList.size()-1);
                        if(ot!=null && ot.getTransportTime()!=null &&
                                StringUtils.isNotBlank(ot.getInfo())) {
                            ordNum = ordNum + 1;
                            String transTimeStr = DateUtil.formatDate(ot.getTransportTime(),DateUtil.FORMAT_DATETIME);
                            String iLogicMsg = "您的订单"+ord.getOrderId()+"物流情况："+ transTimeStr+" "+ot.getInfo()+" </br>";
                            logicMsg.append(iLogicMsg);
                        }
                    }
                }
            }
        }
        if(ordNum>0 && logicMsg.length()>0){
            Map<String,String>  logicInfo = new HashMap();
            logicInfo.put("ordNum",ordNum+"");
            logicInfo.put("logicMsg",logicMsg.toString());
            return logicInfo;
        }
        return null;
    }

}
