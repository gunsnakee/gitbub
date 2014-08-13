package com.meiliwan.emall.pms.client;

import com.meiliwan.emall.pms.bean.ProAction;
import com.meiliwan.emall.pms.dto.ProductStockItem;
import com.meiliwan.emall.pms.util.ActionOpt;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 13-8-26
 * Time: 上午10:57
 */
public class ProActionClientTest {
    @Test
    public void testAddProAction() throws Exception {
        ProAction action = new ProAction();
        action.setProId(10235101);
        action.setLove(1);
        ProActionClient.addProAction(action);
    }

    @Test
    public void testUpdateActionByOpt() throws Exception {
       ProActionClient.updateActionByOpt(10235101, ActionOpt.LOVE);
    }

    @Test
    public void testGetActionByProId() throws Exception {
       ProAction action = ProActionClient.getActionByProId(10235101);
    }

    @Test
    public void testGetMapByProIds() throws Exception {
        Map<String,String> map = ProActionClient.getMapByProIds("10235101,10235102");
    }

    @Test
    public void testupdateSaleByOrderConfm(){
       List<ProductStockItem> items = new ArrayList<ProductStockItem>();

        ProductStockItem item = new ProductStockItem(10235405,5);
        items.add(item);

        ProActionClient.updateSaleByOrderConfm(items);
    }
}
