package com.meiliwan.emall.bkstage.web.controller.oms.orderreporthelper;

import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.oms.dto.export.OrdLogisticsReportRow;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.List;

/**
 * 订单报表管理 帮助类
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-11-1
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class OrderReportControllerHelper {
    /**
     * 构建“出货运费-EMS非代收”内容行
     * @param sheet
     * @param EMSROD
     * @param EMSRODTotal
     * @return
     */
    public static void createEMSRODRows(HSSFSheet sheet,List<OrdLogisticsReportRow> EMSROD,OrdLogisticsReportRow EMSRODTotal ){
        if(EMSROD != null && EMSROD.size() > 0){
            int rowNum = 1;//新行号
            for (int i = 0; i < EMSROD.size(); i++) {
                OrdLogisticsReportRow bill = EMSROD.get(i);
                HSSFRow rowdata = sheet.createRow(rowNum);
                // 下面是填充数据
                rowdata.createCell(0).setCellValue((i+1));//序号
                rowdata.createCell(1).setCellValue(DateUtil.getDateTimeStr(bill.getWaitOutDepositoryDate())); //订单日期
                rowdata.createCell(2).setCellValue(DateUtil.getDateTimeStr(bill.getOutDepositoryDate()));//出库日期
                rowdata.createCell(3).setCellValue(bill.getOrderId());//订单号
                rowdata.createCell(4).setCellValue(bill.getLogisticsNumber());//运单号

                rowdata.createCell(5).setCellValue(bill.getSaleNumber());//商品数量
                rowdata.createCell(6).setCellValue(bill.getRealPayAmount2()); //订单金额
                rowdata.createCell(7).setCellValue("");//包裹重量（KG)
                rowdata.createCell(8).setCellValue(bill.getDeliveryAddress());//配送城市/区域
                rowdata.createCell(9).setCellValue("");//首重单价（2KG）

                rowdata.createCell(10).setCellValue("");//0-1000单续重1KG单价
                rowdata.createCell(11).setCellValue("");//超1000单(含)续重1KG单价
                rowdata.createCell(12).setCellValue("");//运费合计
                PoiExcelUtil.dealWithCellsNullString(rowdata,0,12);
                rowNum++;
            }
            // 汇总
            if(EMSRODTotal != null){
                HSSFRow totalRowdata = sheet.createRow(rowNum);
                totalRowdata.createCell(0).setCellValue("汇总");//序号
                totalRowdata.createCell(1).setCellValue(""); //订单日期
                totalRowdata.createCell(2).setCellValue("");//出库日期
                totalRowdata.createCell(3).setCellValue(EMSROD.size());//订单号
                totalRowdata.createCell(4).setCellValue(EMSROD.size());//运单号

                totalRowdata.createCell(5).setCellValue(EMSRODTotal.getSaleNumber());//商品数量
                totalRowdata.createCell(6).setCellValue(EMSRODTotal.getRealPayAmount2()); //订单金额
                totalRowdata.createCell(7).setCellValue("");//包裹重量（KG)
                totalRowdata.createCell(8).setCellValue("");//配送城市/区域
                totalRowdata.createCell(9).setCellValue("");//首重单价（2KG）

                totalRowdata.createCell(10).setCellValue("");//0-1000单续重1KG单价
                totalRowdata.createCell(11).setCellValue("");//超1000单(含)续重1KG单价
                totalRowdata.createCell(12).setCellValue("");//运费合计
                PoiExcelUtil.dealWithCellsNullString(totalRowdata,0,12);
            }
        }
    }

    /**
     * 构建“出货运费-EMS代收货款”内容行
     * @param sheet
     * @param EMSRCD
     * @param EMSRCDTotal
     * @return
     */
    public static void createEMSRCDRows(HSSFSheet sheet,List<OrdLogisticsReportRow> EMSRCD,OrdLogisticsReportRow EMSRCDTotal ){
        /**
         * 序号	 订单日期	出库日期	订单号	运单号
         * 商品数量	 订单金额	包裹重量（KG)	配送城市/区域	首重单价（2KG）
         * 0-700单续重1KG单价	含700-1000单续重1KG单价	超1000单(含)续重1KG单价	运费小计	代收货款金额
         * 派送费	代收服务费	实收服务费	合计运费
         */
        if(EMSRCD != null && EMSRCD.size() > 0){
            int rowNum = 1;//新行号
            for (int i = 0; i < EMSRCD.size(); i++) {
                OrdLogisticsReportRow bill = EMSRCD.get(i);
                HSSFRow rowdata = sheet.createRow(rowNum);
                // 下面是填充数据
                rowdata.createCell(0).setCellValue((i+1));//序号
                rowdata.createCell(1).setCellValue(DateUtil.getDateTimeStr(bill.getWaitOutDepositoryDate())); //订单日期
                rowdata.createCell(2).setCellValue(DateUtil.getDateTimeStr(bill.getOutDepositoryDate()));//出库日期
                rowdata.createCell(3).setCellValue(bill.getOrderId());//订单号
                rowdata.createCell(4).setCellValue(bill.getLogisticsNumber());//运单号

                rowdata.createCell(5).setCellValue(bill.getSaleNumber());//商品数量
                rowdata.createCell(6).setCellValue(bill.getRealPayAmount2()); //订单金额
                rowdata.createCell(7).setCellValue("");//包裹重量（KG)
                rowdata.createCell(8).setCellValue(bill.getDeliveryAddress()+ "");//配送城市/区域
                rowdata.createCell(9).setCellValue("");//首重单价（2KG）

                rowdata.createCell(10).setCellValue("");//0-700单续重1KG单价
                rowdata.createCell(11).setCellValue("");//含700-1000单续重1KG单价
                rowdata.createCell(12).setCellValue("");// 超1000单(含)续重1KG单价
                rowdata.createCell(13).setCellValue("");//运费小计
                rowdata.createCell(14).setCellValue(bill.getInsteadRealPayAmount2());//  代收货款金额

                rowdata.createCell(15).setCellValue("");//派送费
                rowdata.createCell(16).setCellValue("");//代收服务费
                rowdata.createCell(17).setCellValue("");//实收服务费
                rowdata.createCell(18).setCellValue("");//合计运费
                PoiExcelUtil.dealWithCellsNullString(rowdata,0,18);
                rowNum++;
            }
            // 汇总
            if(EMSRCDTotal != null){
                HSSFRow totalRowdata = sheet.createRow(rowNum);
                // 下面是填充数据
                totalRowdata.createCell(0).setCellValue("汇总");//序号
                totalRowdata.createCell(1).setCellValue(""); //订单日期
                totalRowdata.createCell(2).setCellValue("");//出库日期
                totalRowdata.createCell(3).setCellValue(EMSRCD.size());//订单号
                totalRowdata.createCell(4).setCellValue(EMSRCD.size());//运单号

                totalRowdata.createCell(5).setCellValue(EMSRCDTotal.getSaleNumber());//商品数量
                totalRowdata.createCell(6).setCellValue(EMSRCDTotal.getRealPayAmount2()); //订单金额
                totalRowdata.createCell(7).setCellValue("");//包裹重量（KG)
                totalRowdata.createCell(8).setCellValue("");//配送城市/区域
                totalRowdata.createCell(9).setCellValue("");//首重单价（2KG）

                totalRowdata.createCell(10).setCellValue("");//0-700单续重1KG单价
                totalRowdata.createCell(11).setCellValue("");//含700-1000单续重1KG单价
                totalRowdata.createCell(12).setCellValue("");// 超1000单(含)续重1KG单价
                totalRowdata.createCell(13).setCellValue("");//运费小计
                totalRowdata.createCell(14).setCellValue(EMSRCDTotal.getRealPayAmount2());//  代收货款金额

                totalRowdata.createCell(15).setCellValue("");//派送费
                totalRowdata.createCell(16).setCellValue("");//代收服务费
                totalRowdata.createCell(17).setCellValue("");//实收服务费
                totalRowdata.createCell(18).setCellValue("");//合计运费
                PoiExcelUtil.dealWithCellsNullString(totalRowdata,0,18);
            }
        }
    }

    /**
     * 构建“出货运费-SF代收&非代收”内容行
     * @param sheet
     * @param SF
     * @param SFTotal
     * @return
     */
    public static void createSFRows(HSSFSheet sheet,List<OrdLogisticsReportRow> SF,OrdLogisticsReportRow SFTotal ){
        /**
         * 序号	  订单日期	出库日期	订单号	运单号
         * 商品数量	  订单金额	 包裹重量（KG)	 配送城市/区域	陆运首重单价（1KG）
         * 50KG以内续重1KG单价	运费合计	代收货款金额	代收服务费	实收服务费
         * 返款合计
         */
        if(SF != null && SF.size() > 0){
            int rowNum = 1;//新行号
            for (int i = 0; i < SF.size(); i++) {
                OrdLogisticsReportRow bill = SF.get(i);
                HSSFRow rowdata = sheet.createRow(rowNum);
                // 下面是填充数据
                rowdata.createCell(0).setCellValue((i+1));//序号
                rowdata.createCell(1).setCellValue(DateUtil.getDateTimeStr(bill.getWaitOutDepositoryDate())); //订单日期
                rowdata.createCell(2).setCellValue(DateUtil.getDateTimeStr(bill.getOutDepositoryDate()));//出库日期
                rowdata.createCell(3).setCellValue(bill.getOrderId());//订单号
                rowdata.createCell(4).setCellValue(bill.getLogisticsNumber());//运单号

                rowdata.createCell(5).setCellValue(bill.getSaleNumber());//商品数量
                rowdata.createCell(6).setCellValue(bill.getRealPayAmount2()); //订单金额
                rowdata.createCell(7).setCellValue("");//包裹重量（KG)
                rowdata.createCell(8).setCellValue(bill.getDeliveryAddress());//配送城市/区域
                rowdata.createCell(9).setCellValue("");//陆运首重单价（1KG）

                rowdata.createCell(10).setCellValue( "");//50KG以内续重1KG单价
                rowdata.createCell(11).setCellValue( "");//运费合计
                rowdata.createCell(12).setCellValue(bill.getInsteadRealPayAmount2());// 代收货款金额
                rowdata.createCell(13).setCellValue(  "");//代收服务费
                rowdata.createCell(14).setCellValue("");//  实收服务费

                rowdata.createCell(15).setCellValue(  "");//返款合计
                PoiExcelUtil.dealWithCellsNullString(rowdata,0,15);
                rowNum++;
            }
            // 汇总
            if(SFTotal != null){
                HSSFRow totalRowdata = sheet.createRow(rowNum);
                // 下面是填充数据
                totalRowdata.createCell(0).setCellValue("汇总");//序号
                totalRowdata.createCell(1).setCellValue(""); //订单日期
                totalRowdata.createCell(2).setCellValue("");//出库日期
                totalRowdata.createCell(3).setCellValue(SF.size());//订单号
                totalRowdata.createCell(4).setCellValue(SF.size());//运单号

                totalRowdata.createCell(5).setCellValue(SFTotal.getSaleNumber());//商品数量
                totalRowdata.createCell(6).setCellValue(SFTotal.getRealPayAmount2()); //订单金额
                totalRowdata.createCell(7).setCellValue("");//包裹重量（KG)
                totalRowdata.createCell(8).setCellValue("");//配送城市/区域
                totalRowdata.createCell(9).setCellValue("");//陆运首重单价（1KG）

                totalRowdata.createCell(10).setCellValue( "");//50KG以内续重1KG单价
                totalRowdata.createCell(11).setCellValue( "");//运费合计
                totalRowdata.createCell(12).setCellValue(SFTotal.getInsteadRealPayAmount2());// 代收货款金额
                totalRowdata.createCell(13).setCellValue(  "");//代收服务费
                totalRowdata.createCell(14).setCellValue("");//  实收服务费

                totalRowdata.createCell(15).setCellValue("");//返款合计
                PoiExcelUtil.dealWithCellsNullString(totalRowdata,0,15);
            }
        }
    }
}
