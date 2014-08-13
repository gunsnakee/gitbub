package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.ProAction;
import com.meiliwan.emall.pms.util.ActionOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class ProActionServiceTest extends BaseTest{

    @Autowired
    private ProActionService actionService;
    private JsonObject resultObj = new JsonObject();

    @Test
    public void testAddProAction() throws Exception {
        ProAction action = new ProAction();
        action.setProId(15247067);
        actionService.addProAction(resultObj,action);
    }

    @Test
    public void testUpdateActionByOpt() throws Exception {
       actionService.updateActionByOpt(resultObj,15247067, ActionOpt.LOVE);
    }

    public void testGetActionByProId() throws Exception {

    }

    public void testGetMapByProIds() throws Exception {

    }

    @Test
    public void testUpdateProSale() throws Exception {
        actionService.updateProSale(resultObj,15247067,5);
    }

    public void testUpdateProScan() throws Exception {

    }

}
