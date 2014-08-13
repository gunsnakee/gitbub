package com.meiliwan.emall.oms.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;
import com.meiliwan.emall.commons.rabbitmq.asyncmsg.AsyncMsg;
import com.meiliwan.emall.oms.BaseTest;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdLogistics;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by guangdetang on 13-8-26.
 */
public class OrdLogisticsServiceTest extends BaseTest {
    @Autowired
    private OrdLogisticsService logisticsService;
    JsonObject resultObj = new JsonObject();

    @Test
    public void testAddOrdLogistics() throws Exception {
        OrdLogistics logistics = new OrdLogistics();
        logistics.setUid(17);
        logistics.setBillType(0);
        logistics.setLogisticsCompany("顺丰");
        logistics.setLogisticsNumber("12345465");
        logistics.setOrderId("1232132432");
        logisticsService.addOrdLogistics(resultObj, logistics);
    }

    @Test
    public void testGetOrdLogisticsById() throws Exception {
        logisticsService.getOrdLogisticsById(resultObj, 1);
    }

    @Test
    public void testGetOrdLogisticsByUid() throws Exception {
    }

    @Test
    public void testGetListByProIdss(){
        MsgSender.delaySend(new AsyncMsg.AsyncMsgBuilder(MqModel.ORDER, MqModel.ORDER, "cancelOrder").setMsg("0000095562", "oderId").setTime(new Date(new Date().getTime()+60000)).build());
    }
    
   
    
    
    
}
