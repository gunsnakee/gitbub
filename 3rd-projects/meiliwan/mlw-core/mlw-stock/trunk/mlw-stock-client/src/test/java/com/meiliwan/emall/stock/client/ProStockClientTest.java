
package com.meiliwan.emall.stock.client;


import com.meiliwan.emall.commons.bean.SellStockStatus;
import com.meiliwan.emall.stock.bean.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProStockClientTest {

    @Test
    public void testAddStock() throws Exception {
        boolean count = ProStockClient.addSellStock(10235583, 20, 123456, "admin");
    }

    @Test
    public void testAddUnsellStock() throws Exception {
        boolean count = ProStockClient.addUnsellStock(10235583, 2, 123456, "admin");
    }

    @Test
    public void testSubStock() throws Exception {
        boolean suc = ProStockClient.subSellStock(10337403, 1, 123456, "admin");
        System.out.print(suc);

    }

    @Test
    public void testGetProStock() throws Exception {
        int stock = ProStockClient.getStock(10235395);
    }

    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testGetProSellStock() throws Exception {
        int stock = ProStockClient.getSellStock(10235395);
    }

    @Test
    public void testGetProOrderStock() throws Exception {
        int stock = ProStockClient.getOrderStock(10235395);
    }

    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testCheckSellStockStatus() throws Exception {
        SellStockStatus suc = ProStockClient.checkSellStockStatus(10235395);
    }

    @Test
    public void testCheckSellStockIfSub() throws Exception {
        boolean suc = ProStockClient.checkSellStockIfSub(10235395, 20);
    }

    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testCheckSellStockIfSub1() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();


        List<StockItemStatus> list1 = ProStockClient.checkSellStockIfSub(list);
    }

    @Test(invocationCount = 1000, threadPoolSize = 20)
    public void testUpdateStockOnOrderSubmit() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId("0000001000022");
        orderItem.setOrderItemId("0000012222231");

        StockItem item1 = new StockItem();
        item1.setProId(10235431);
        item1.setBuyNum(1);
        item1.setOrderItem(orderItem);
        list.add(item1);

        StockItem item2 = new StockItem();
        item2.setProId(10235432);
        item2.setBuyNum(1);
        item2.setOrderItem(orderItem);
        list.add(item2);

        StockItem item3 = new StockItem();
        item3.setProId(10235433);
        item3.setBuyNum(1);
        item3.setOrderItem(orderItem);
        list.add(item3);

        StockItem item4 = new StockItem();
        item4.setProId(10235434);
        item4.setBuyNum(1);
        item4.setOrderItem(orderItem);
        list.add(item4);

        StockItem item5 = new StockItem();
        item5.setProId(10235435);
        item5.setBuyNum(1);
        item5.setOrderItem(orderItem);
        list.add(item5);

        StockItem item6 = new StockItem();
        item6.setProId(10235436);
        item6.setBuyNum(1);
        item6.setOrderItem(orderItem);
        list.add(item6);

        StockItem item7 = new StockItem();
        item7.setProId(10235437);
        item7.setBuyNum(1);
        item7.setOrderItem(orderItem);
        list.add(item7);

        StockItem item8 = new StockItem();
        item8.setProId(10235438);
        item8.setBuyNum(1);
        item8.setOrderItem(orderItem);
        list.add(item8);

        StockItem item9 = new StockItem();
        item9.setProId(10235439);
        item9.setBuyNum(1);
        item9.setOrderItem(orderItem);
        list.add(item9);

        StockItem item10 = new StockItem();
        item10.setProId(10235440);
        item10.setBuyNum(1);
        item10.setOrderItem(orderItem);
        list.add(item10);

        StockItem item11 = new StockItem();
        item11.setProId(10235441);
        item11.setBuyNum(1);
        item11.setOrderItem(orderItem);
        list.add(item11);

        StockItem item12 = new StockItem();
        item12.setProId(10235442);
        item12.setBuyNum(1);
        item12.setOrderItem(orderItem);
        list.add(item12);

        StockItem item13 = new StockItem();
        item13.setProId(10235443);
        item13.setBuyNum(1);
        item13.setOrderItem(orderItem);
        list.add(item13);

        StockItem item14 = new StockItem();
        item14.setProId(10235444);
        item14.setBuyNum(1);
        item14.setOrderItem(orderItem);
        list.add(item14);

        StockItem item15 = new StockItem();
        item15.setProId(10235445);
        item15.setBuyNum(1);
        item15.setOrderItem(orderItem);
        list.add(item15);

        StockItem item16 = new StockItem();
        item16.setProId(10235446);
        item16.setBuyNum(1);
        item16.setOrderItem(orderItem);
        list.add(item16);

        StockItem item17 = new StockItem();
        item17.setProId(10235447);
        item17.setBuyNum(1);
        item17.setOrderItem(orderItem);
        list.add(item17);

        StockItem item18 = new StockItem();
        item18.setProId(10235448);
        item18.setBuyNum(1);
        item18.setOrderItem(orderItem);
        list.add(item18);

        StockItem item19 = new StockItem();
        item19.setProId(10235449);
        item19.setBuyNum(1);
        item19.setOrderItem(orderItem);
        list.add(item19);

        StockItem item20 = new StockItem();
        item20.setProId(10235450);
        item20.setBuyNum(1);
        item20.setOrderItem(orderItem);
        list.add(item20);

        StockItemsUpdateResult result = ProStockClient.updateStockOnOrderSubmit(list, 12345, "user");
        System.out.println("#########:" + result);
    }

    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testStockUpdateOnSendOutPay() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId("0000001000022");
        orderItem.setOrderItemId("0000012222231");

        StockItem item = new StockItem();
        item.setProId(10235583);
        item.setBuyNum(20);
        item.setOrderItem(orderItem);
        list.add(item);

        StockItem item1 = new StockItem();
        item1.setProId(10235584);
        item1.setBuyNum(20);
        item.setOrderItem(orderItem);
        list.add(item1);

        boolean suc = ProStockClient.stockUpdateOnSendOutPay(list, 12345, "admin");
    }

    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testStockUpdateOnSendOutCOD() throws Exception {
        List<StockItem> list = new ArrayList<StockItem>();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId("0000001000022");
        orderItem.setOrderItemId("0000012222231");

        StockItem item = new StockItem();
        item.setProId(10235583);
        item.setBuyNum(20);
        item.setOrderItem(orderItem);
        list.add(item);

        StockItem item1 = new StockItem();
        item1.setProId(10235584);
        item1.setBuyNum(20);
        item.setOrderItem(orderItem);
        list.add(item1);

        StockItemsUpdateResult result = ProStockClient.stockUpdateOnSendOutCOD(list, 123456, "admin");
    }

    @Test
    public void testGetSafeStockList() {
        List<SafeStockItem> list = ProStockClient.getSafeStockList();
        System.out.println(list);
    }

    public static void main(String[] args) throws IOException {
        List<ProStock> list = readXls();
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        boolean suc = true;
        for (ProStock product : list) {
            if (map.get(product.getProId()) != null && map.get(product.getProId()) > 0) {
                System.out.println("重复的商品ID为：" + product.getProId());
                suc = false;

            } else {
                map.put(product.getProId(), product.getProId());
            }
        }

        if (suc) {
            for (ProStock product : list) {
                ProStockClient.updateStock(product);
            }
        } else {
            System.out.println("商品有重复的ID,无法完成数据导入");
        }
    }

    private static List<ProStock> readXls() throws IOException {
        InputStream is = new FileInputStream("/Users/leo/workspace/tmp/stock.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        ProStock xlsDto = null;
        List<ProStock> list = new ArrayList<ProStock>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                xlsDto = new ProStock();
                // 商品ID
                HSSFCell proId = hssfRow.getCell(0);
                if (proId == null || "".equals(getValue(proId)) ) {
                    continue;
                }
                xlsDto.setProId(Integer.valueOf(getValue(proId)));

                //获取库存
                HSSFCell stock = hssfRow.getCell(2);
                if (stock == null) {
                    continue;
                }
                xlsDto.setStock(Integer.valueOf(getValue(stock)));
                xlsDto.setSellStock(Integer.valueOf(getValue(stock)));

                list.add(xlsDto);
            }
        }
        return list;
    }

    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(String.format("%.0f", hssfCell.getNumericCellValue()));
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
}