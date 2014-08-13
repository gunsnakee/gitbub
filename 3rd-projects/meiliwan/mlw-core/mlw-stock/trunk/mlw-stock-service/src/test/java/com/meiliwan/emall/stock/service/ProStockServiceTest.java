package com.meiliwan.emall.stock.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.bean.SellStockStatus;
import com.meiliwan.emall.stock.base.BaseTest;
import com.meiliwan.emall.stock.bean.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ProStockServiceTest extends BaseTest{

    @Autowired
    private ProStockService stockService;

    private JsonObject resultObj = new JsonObject();

    public void testInsertStock() throws Exception {

    }

    @Test
    public void testAddStock() throws Exception {
        stockService.addSellStock(resultObj,10235404,1,111,"admin");
    }

    @Test
    public void testSubStock() throws Exception {
        stockService.subSellStock(resultObj, 15247078, 1,123456,"admin");
    }

    @Test
    public void testGetProStock() throws Exception {
        stockService.getStock(resultObj,10235583);
    }

    @Test
    public void testGetProSellStock() throws Exception {
        stockService.getSellStock(resultObj, 10235583);
    }

    @Test
    public void testGetProOrderStock() throws Exception {
        stockService.getOrderStock(resultObj, 10235583);
    }

    @Test
    public void testCheckSellStockStatus() throws Exception {
        stockService.checkSellStockStatus(resultObj,10235583);
        SellStockStatus status = new Gson().fromJson(resultObj.get("resultObj"), SellStockStatus.class);
        System.out.println(status);
    }

    @Test
    public void testCheckSellStockByNum() throws Exception {
        stockService.checkSellStockByNum(resultObj,10235583,20);
    }

    @Test
    public void testCheckSellStockIfSub() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();
        StockItem item = new StockItem();
        item.setProId(10235404);
        item.setBuyNum(20);
        list.add(item);

        StockItem item1 = new StockItem();
        item1.setProId(10235405);
        item1.setBuyNum(20);
        list.add(item1);

        stockService.checkSellStockIfSub(resultObj,list.toArray(new StockItem[0]));
        List<StockItemStatus> itemList = new Gson().fromJson(resultObj.get("resultObj"),new TypeToken<List<StockItemStatus>>() {
        }.getType());

        System.out.println(itemList);
    }

    @Test
    public void testUpdateStockOnOrderSubmit() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();
        StockItem item = new StockItem();
        item.setProId(10235404);
        item.setBuyNum(1);
        item.setOrderItem(new OrderItem());
        list.add(item);

        StockItem item1 = new StockItem();
        item1.setProId(10235405);
        item1.setBuyNum(2);
        item1.setOrderItem(new OrderItem());
        list.add(item1);


        stockService.updateStockOnOrderSubmit(resultObj, list.toArray(new StockItem[0]),123456,"user");
        StockItemsUpdateResult itemList = new Gson().fromJson(resultObj.get("resultObj"),StockItemsUpdateResult.class);

        System.out.println(itemList);
    }

    @Test
    public void testStockUpdateOnSendOutPay() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();
        StockItem item = new StockItem();
        item.setProId(10235404);
        item.setBuyNum(1);
        list.add(item);

        StockItem item1 = new StockItem();
        item1.setProId(10235405);
        item1.setBuyNum(2);
        list.add(item1);

        stockService.stockUpdateOnSendOutPay(resultObj,list.toArray(new StockItem[0]),123456,"user");
        if (resultObj.get("resultObj").getAsBoolean()){
            System.out.println("success");
        }else {
            System.out.println("faulue");
        }
    }

    @Test
    public void testStockUpdateOnSendOutCOD() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();
        StockItem item = new StockItem();
        item.setProId(10235404);
        item.setBuyNum(3);
        list.add(item);

        StockItem item1 = new StockItem();
        item1.setProId(10235405);
        item1.setBuyNum(1);
        list.add(item1);

        stockService.stockUpdateOnSendOutCOD(resultObj,list.toArray(new StockItem[0]),123456,"admin");
        StockItemsUpdateResult itemList = new Gson().fromJson(resultObj.get("resultObj"),StockItemsUpdateResult.class);
        System.out.println(itemList);
    }

    @Test
    public void testAddUnsellStock() throws Exception {
        stockService.addUnsellStock(resultObj,10235583,5,12345,"admin");
    }

    @Test
    public void testGetRand(){
        String s = stockService.getRandBatchNo();
        System.out.println("################:"+s);
    }

    @Test
    public void testGetListByIds(){
        int [] a = {10235582,10235583,10235584};
        stockService.getListByIds(resultObj,a);
    }

    @Test
    public void testGetSafeStockList(){
        stockService.getSafeStockList(resultObj);
    }

    @Test
    public void testImportStock(){
        StockImportLog[] logs = new StockImportLog[2];
        StockImportLog log1 = new StockImportLog();
        log1.setBarCode("300900173");
        log1.setChangeStock(-5);
        logs[0] = log1;

        StockImportLog log2 = new StockImportLog();
        log2.setBarCode("300700024");
        log2.setChangeStock(2);
        logs[1] = log2;

        stockService.importExcel(resultObj,logs,"20131202ck",123456,"wuzixin");
    }
}
