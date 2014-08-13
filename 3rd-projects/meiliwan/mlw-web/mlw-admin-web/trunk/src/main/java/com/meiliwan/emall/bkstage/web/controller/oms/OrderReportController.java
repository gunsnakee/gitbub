package com.meiliwan.emall.bkstage.web.controller.oms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.account.template.LimitException;
import com.meiliwan.emall.bkstage.web.controller.oms.orderreporthelper.OrderReportControllerHelper;
import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserPassportSimple;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.oms.bean.OrdAddr;
import com.meiliwan.emall.oms.bean.OrdRemark;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.Retord;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.client.OrdExportClient;
import com.meiliwan.emall.oms.client.ReturnOrderClient;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.oms.constant.TransportCompany;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrdiDTO;
import com.meiliwan.emall.oms.dto.export.EMSExcelEntity;
import com.meiliwan.emall.oms.dto.export.OrdLogisticsReport;
import com.meiliwan.emall.oms.dto.export.OrdLogisticsReportRow;
import com.meiliwan.emall.oms.dto.export.RetOrdReportRow;
import com.meiliwan.emall.oms.dto.export.SFWaybillExcelEntity;
import com.meiliwan.emall.oms.dto.export.SendGoodsListExcelEntity;
import com.meiliwan.emall.pms.bean.ProPropertyValue;
import com.meiliwan.emall.pms.client.ProPropertyClient;

/**
 * 订单报表管理
 */
@Controller
@RequestMapping("/oms/report")
public class OrderReportController {
    final static String exportPath = "/data/www/exportfiles/";

    private static final MLWLogger logger = MLWLoggerFactory
            .getLogger(OrderReportController.class);

    /**
     * 导出“运费统计表”报表
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getCarriagesExcel")
    public void getCarriagesExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String excelName = "运费统计表";
        String sheet1Name = "出货运费-EMS非代收";
        String sheet2Name = "出货运费-EMS代收货款";
        String sheet3Name = "出货运费-SF代收&非代收";
        boolean success = false;
        try {
            //获取运费数据
            OrdLogisticsReport report = reportLogistics(request, response, model);
//            List<OrdLogisticsReportRow> EMSROD = report.getEMSROD();
//            List<OrdLogisticsReportRow> EMSRCD = report.getEMSRCD();
//            List<OrdLogisticsReportRow> SF = report.getSF();

            // 创建一个EXCEL  出货运费-EMS非代收 工作簿
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建sheet
            HSSFSheet sheet1 = wb.createSheet(sheet1Name);
            HSSFSheet sheet2 = wb.createSheet(sheet2Name);
            HSSFSheet sheet3 = wb.createSheet(sheet3Name);

            String[] sheet1Titles = {"序号", "订单日期", "出库日期", "订单号", "运单号",
                    "商品数量", "订单金额", "包裹重量（KG)", "配送城市/区域", "首重单价（2KG）",
                    "0-1000单续重1KG单价", "超1000单(含)续重1KG单价", "运费合计"};

            String[] sheet2Titles = {"序号", "订单日期", "出库日期", "订单号", "运单号",
                    "商品数量", "订单金额", "包裹重量（KG)", "配送城市/区域", "首重单价（2KG）",
                    "0-700单续重1KG单价", "含700-1000单续重1KG单价", "超1000单(含)续重1KG单价", "运费小计", "代收货款金额",
                    "派送费", "代收服务费", "实收服务费", "合计运费"};

            String[] sheet3Titles = {"序号", "订单日期", "出库日期", "订单号", "运单号",
                    "商品数量", "订单金额", "包裹重量（KG)", "配送城市/区域", "陆运首重单价（1KG）",
                    "50KG以内续重1KG单价", "运费合计", "代收货款金额", "代收服务费", "实收服务费",
                    "返款合计"};

            //创建标题
            PoiExcelUtil.buildTitles(sheet1, sheet1Titles);
            PoiExcelUtil.buildTitles(sheet2, sheet2Titles);
            PoiExcelUtil.buildTitles(sheet3, sheet3Titles);
            //创建内容行
            OrderReportControllerHelper.createEMSRODRows(sheet1, report.getEMSROD(), report.getEMSRODTotal());
            OrderReportControllerHelper.createEMSRCDRows(sheet2, report.getEMSRCD(), report.getEMSRCDTotal());
            OrderReportControllerHelper.createSFRows(sheet3, report.getSF(), report.getSFTotal());

            String writeName = "SendGoodsListExcelEntity" + new Date().getTime() + "";
            PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
            success = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
            if (!success) {
                StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
            }
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
        }
    }

    /**
     * 导出“顺丰运单表”报表
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getSFWaybillExcel")
    public void getSFWaybillExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String excelName = "顺丰运单表";
        String sheet1Name = "顺丰运单表";
        boolean success = false;
        try {
            String saveStateGroupId = ServletRequestUtils.getStringParameter(request, "saveStateGroupId", "");//对应选中的tag状态
            String isSelectedExport = ServletRequestUtils.getStringParameter(request, "type", null);//是否选择到处
            // OrderQueryDTO orderQuery = new OrderQueryDTO();
            int orderStatus = ServletRequestUtils.getIntParameter(request, "os", 1);//订单的状态，为什么说有状态都是1
            OrderQueryDTO dto = initExportQueryDTO(request, orderStatus);
            dto.setStatusType(OrderStatusType.ID.getType());
            int length = ServletRequestUtils.getIntParameter(request, "length", 1000); //最大页数
            PageInfo pageInfo = new PageInfo(1, length);
            List<SFWaybillExcelEntity> billList = null;
            if (isSelectedExport != null) {//tag区域选择导出
                billList = OrdExportClient.exportSFWaybillByIds(StageHelper.getOptSelectSaveState(request, saveStateGroupId).toArray(new String[0]));
            } else {//条件区域的导出Excel
                billList = OrdExportClient.exportSFWaybillExcel(dto, pageInfo, true);
            }

            //没有可打印单据 提示用户
            if (billList == null || billList.isEmpty()) {
                StageHelper.writeString("没有符合条件的订单 ，无法导出", response);
                return;
            }

            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet1 = wb.createSheet(sheet1Name);
            String[] titles = {"寄件客户编码", "寄件人", "寄件公司", "寄件人联系电话", "寄件人地址",
                    "托寄物内容", "数量", "收货人", "收货公司", "收货人联系电话",
                    "收货地址", "订单号码", "付款方式", "保价", "保价金额",
                    "代收货款", "代收货款卡号", "代收货款金额", "备注1", "备注2",
                    "备注3"};

            PoiExcelUtil.buildTitles(sheet1, titles);
            int rowNum = 1;//新行号
            for (int i = 0; i < billList.size(); i++) {
                SFWaybillExcelEntity bill = billList.get(i);
                HSSFRow rowdata = sheet1.createRow(rowNum);
                sheet1.autoSizeColumn(4);
                sheet1.autoSizeColumn(5);
                sheet1.autoSizeColumn(6);
                // 下面是填充数据
                rowdata.createCell(0).setCellValue("");//寄件客户 编码 （自填）
                rowdata.createCell(1).setCellValue(bill.getConsignorName()); //寄件人
                rowdata.createCell(2).setCellValue(bill.getConsignorCompany());//寄件公司
                rowdata.createCell(3).setCellValue(bill.getConsignorPhone());//寄件人联系电话
                rowdata.createCell(4).setCellValue(bill.getConsignorAddress());//寄件人地址

                rowdata.createCell(5).setCellValue(bill.getConsignorContent());//托寄物内容
                rowdata.createCell(6).setCellValue(""); //数量  ( 不填)
                rowdata.createCell(7).setCellValue(bill.getConsigneeName());//收货人
                rowdata.createCell(8).setCellValue(bill.getConsigneeCompany());//收货公司
                rowdata.createCell(9).setCellValue(bill.getConsigneePhone());//收货人联系电话

                rowdata.createCell(10).setCellValue(bill.getConsigneeAddress());//收货地址
                rowdata.createCell(11).setCellValue(bill.getOrderId()); //订单号码
                rowdata.createCell(12).setCellValue(bill.getPayType());//付款方式
                rowdata.createCell(13).setCellValue(bill.getIsDeposit());//保价
                rowdata.createCell(14).setCellValue("");//保价金额   （不填）

                rowdata.createCell(15).setCellValue(bill.getIsCOD());//代收货款
                rowdata.createCell(16).setCellValue(""); //代收货款卡号       (自填)
                rowdata.createCell(17).setCellValue(bill.getCODTotalPrice());//代收货款金额
                rowdata.createCell(18).setCellValue(bill.getWaybillNum());//备注1  填入运单号
                rowdata.createCell(19).setCellValue("");//备注2  （不填）

                rowdata.createCell(20).setCellValue("");//备注3  ( 不填)
                PoiExcelUtil.dealWithCellsNullString(rowdata, 0, 20);
                rowNum++;
            }
            String writeName = "SFWaybillExcel" + new Date().getTime();
            PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
            success = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
            if (!success) {
                StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
            } else {
                StageHelper.optSelectSaveState(request, saveStateGroupId, null, -1);
            }
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
        }
    }


    /**
     * 导出“出货明细表”报表
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getSendGoodsListExcel")
    public void getSendGoodsListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String excelName = "出货明细表";
        String sheet1Name = "出货明细表";
        boolean success = false;
        try {
            String saveStateGroupId = ServletRequestUtils.getStringParameter(request, "saveStateGroupId", "");
            String isSelectedExport = ServletRequestUtils.getStringParameter(request, "type", null);
            int orderStatus = ServletRequestUtils.getIntParameter(request, "os", 1);
            OrderQueryDTO dto = initExportQueryDTO(request,orderStatus);

            dto.setStatusType(OrderStatusType.ID.getType());

            //   OrderQueryDTO orderQuery = new OrderQueryDTO();
            int length = ServletRequestUtils.getIntParameter(request, "length", 1000); //最大页数
            PageInfo pageInfo = new PageInfo(1, length);
            List<SendGoodsListExcelEntity> billList = new ArrayList<SendGoodsListExcelEntity>();
            if (isSelectedExport != null) {
                billList = OrdExportClient.exportSendGoodsByIds(StageHelper.getOptSelectSaveState(request, saveStateGroupId).toArray(new String[0]));
            } else {
                billList = OrdExportClient.exportSendGoodsList(dto, pageInfo, true);
            }

            //没有可打印单据 提示用户
            if (billList == null || billList.isEmpty()) {
                StageHelper.writeString("没有符合条件的订单 ，无法导出", response);
                return;
            }

            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet1 = wb.createSheet(sheet1Name);
            /*
            序号	订单日期	出库日期	订单号	运单号	商品编号	商品名称	商品储位	商品数量	单价	金额
             */
            String[] titles = {"序号", "订单日期", "出库日期", "订单号", "运单号",
                    "商品编号", "商品名称", "商品储位", "商品数量", "单价",
                    "金额"};

            PoiExcelUtil.buildTitles(sheet1, titles);
            int rowNum = 1;//新行号
            for (int i = 0; i < billList.size(); i++) {
                SendGoodsListExcelEntity bill = billList.get(i);
                HSSFRow rowdata = sheet1.createRow(rowNum);
                // 下面是填充数据
                rowdata.createCell(0).setCellValue((i + 1));//序号
                rowdata.createCell(1).setCellValue(DateUtil.getDateTimeStr(bill.getOrderCreateTime())); //订单日期
                rowdata.createCell(2).setCellValue(DateUtil.getDateTimeStr(bill.getStoreHouseSendTime()));//出库日期
                rowdata.createCell(3).setCellValue(bill.getOrderId());//订单号
                rowdata.createCell(4).setCellValue(bill.getWaybillNum());//运单号

                rowdata.createCell(5).setCellValue(bill.getProductNum());//商品编号
                rowdata.createCell(6).setCellValue(bill.getProductName()); //商品名称
                rowdata.createCell(7).setCellValue("");//商品储位 (空)
                rowdata.createCell(8).setCellValue(bill.getCount());//商品数量
                rowdata.createCell(9).setCellValue(bill.getPrice());//单价

                rowdata.createCell(10).setCellValue(bill.getCurProductTotal());//金额
                PoiExcelUtil.dealWithCellsNullString(rowdata, 0, 10);
                rowNum++;
            }
            String writeName = "SendGoodsListExcel" + new Date().getTime();
            PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
            success = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
            if (!success) {
                StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
            } else {
                StageHelper.optSelectSaveState(request, saveStateGroupId, null, -1);
            }
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
        }
    }

    /**
     * url:/oms/report/reportLogistics
     * 出货运费报表
     * 1.出货运费-EMS非代收  orderType＝RCD&Logistics=EMS
     * 2.出货运费-EMS代收货款 orderType＝ROD&Logistics=EMS
     * 3.出货运费-SF代收&非代收 Logistics=EMS
     *
     * @param request  会多出orderType, Logistics
     * @param response
     * @param model
     * @return
     */
    public OrdLogisticsReport reportLogistics(HttpServletRequest request, HttpServletResponse response, Model model) {
        String saveStateGroupId = ServletRequestUtils.getStringParameter(request, "saveStateGroupId", "");
        String isSelectedExport = ServletRequestUtils.getStringParameter(request, "type", null);


        int orderStatus = ServletRequestUtils.getIntParameter(request, "os", 1);
        int length = ServletRequestUtils.getIntParameter(request, "length", 1000); //最大页数
        // 针对页面的
        OrderQueryDTO dto = initExportQueryDTO(request,orderStatus);
        dto.setStatusType(OrderStatusType.ID.getType());
        List<OrdDTO> list = null;
        // 排序
        boolean sort_value = ServletRequestUtils.getBooleanParameter(request,
                "sort_value", false);
        if (isSelectedExport != null) {
            list = OrdClient.getOrdDTOListBy(StageHelper.getOptSelectSaveState(request, saveStateGroupId).toArray(new String[0]));
        } else {
            PagerControl<OrdDTO> pc = OrdClient.getOrderList(dto, new PageInfo(1, length), sort_value);
            list = pc.getEntityList();
        }


        System.out.println("list:" + list.size());
        OrdLogisticsReport report = changeToLogisticsReportList(list);
        System.out.println(report);
        return report;
    }

    /**
     * 拉取相关信息
     *
     * @param list
     * @return
     */
    private OrdLogisticsReport changeToLogisticsReportList(List<OrdDTO> list) {
        OrdLogisticsReport report = new OrdLogisticsReport();
        for (OrdDTO ordDTO : list) {
            double totalPay = 0d;
            //代收
            double totalInstead = 0d;
            int ordiSaleNum = 0;

            OrdLogisticsReportRow row = new OrdLogisticsReportRow();
            //出库时间
            row.setOutDepositoryDate(ordDTO.getStockTime());
            totalPay = NumberUtil.add(totalPay, ordDTO.getRealPayAmount());
            row.setOrderId(ordDTO.getOrderId());
            row.setRealPayAmount(ordDTO.getRealPayAmount());
            row.setLogisticsNumber(ordDTO.getLogisticsNumber());

            List<OrdiDTO> ordis = ordDTO.getOrdiList();
            for (OrdiDTO ordi : ordis) {
                int tempNum = ordi.getSaleNum();
                ordiSaleNum += tempNum;
                row.setSaleNumber(ordiSaleNum);
            }
            OrdAddr addr = null;
            if (ordDTO.getRecvAddrId() > 0) {
                addr = OrdClient.getOrdAddrById(ordDTO.getOrderId());
                if (addr != null) {
                    StringBuilder daddr = new StringBuilder();
                    daddr.append(addr.getProvince()).append(addr.getCity()).append(addr.getCounty());
                    row.setDeliveryAddress(daddr.toString());
                }
            }
            if (ordDTO.getOrderType() != null && ordDTO.getOrderType().equals(OrderType.ROD)) {
//                row.setWaitOutDepositoryDate(ordDTO.getPayTime());
                row.setWaitOutDepositoryDate(ordDTO.getCreateTime());//按照订单创建时间 2013年11月21日 14:12:52
                if (ordDTO.getLogisticsCompany() != null && ordDTO.getLogisticsCompany().equals(TransportCompany.EMS.getCode())) {
                    report.addEMSROD(row);
                    report.addEMSRODRealPayAmount(totalPay);
                    report.addEMSRODSaleNum(ordiSaleNum);
                    continue;
                }
            }
            if (ordDTO.getOrderType() != null && ordDTO.getOrderType().equals(OrderType.RCD)) {
                row.setWaitOutDepositoryDate(ordDTO.getCreateTime());
                row.setInsteadRealPayAmount(ordDTO.getRealPayAmount());
                //

                if (ordDTO.getLogisticsCompany() != null && ordDTO.getLogisticsCompany().equals(TransportCompany.EMS.getCode())) {
                    report.addEMSRCD(row);
                    report.addEMSRCDRealPayAmount(totalPay);
                    report.addEMSRCDSaleNum(ordiSaleNum);
                    continue;
                }
                if (ordDTO.getLogisticsCompany() != null && ordDTO.getLogisticsCompany().equals(TransportCompany.SF.getCode())) {
                    totalInstead = NumberUtil.add(totalInstead, ordDTO.getRealPayAmount());
                    report.addSFInsteadPayAmount(totalInstead);
                }
            }
            if (ordDTO.getLogisticsCompany() != null && ordDTO.getLogisticsCompany().equals(TransportCompany.SF.getCode())) {
                report.addSF(row);
                report.addSFRealPayAmount(totalPay);
                report.addSFSaleNum(ordiSaleNum);

            }
        }
        return report;
    }

    /**
     * 导出“EMS运单表”报表
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getEMSWaybillExcel")
    public void getEMSWaybillExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String excelName = "EMS运单表";
        String sheet1Name = "EMS运单表";
        boolean success = false;
        try {
            String saveStateGroupId = ServletRequestUtils.getStringParameter(request, "saveStateGroupId", "");//对应选中的tag状态
            String isSelectedExport = ServletRequestUtils.getStringParameter(request, "type", null);//是否选择到处
            // OrderQueryDTO orderQuery = new OrderQueryDTO();
            int orderStatus = ServletRequestUtils.getIntParameter(request, "os", 1);//订单的状态，为什么说有状态都是1
            OrderQueryDTO dto = initExportQueryDTO(request, orderStatus);

            dto.setStatusType(OrderStatusType.ID.getType());
            int length = ServletRequestUtils.getIntParameter(request, "length", 1000); //最大页数
            PageInfo pageInfo = new PageInfo(1, length);
            List<EMSExcelEntity> billList = null;
            if (isSelectedExport != null) {//tag区域选择导出
                billList = OrdExportClient.exportEMSExcelByIds(StageHelper.getOptSelectSaveState(request, saveStateGroupId).toArray(new String[0]));
            } else {//条件区域的导出Excel
                billList = OrdExportClient.exportEMSExcel(dto, pageInfo, true);
            }

            //如果 没有EMS运单
            if (billList == null || billList.isEmpty()) {
                StageHelper.writeString("没有符合条件的订单 ，无法导出", response);
                return;
            }

            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet1 = wb.createSheet(sheet1Name);
            String[] titles = {"寄件客户编码", "收货人","收货公司", "收货人联系电话",
                    "收货地址", "运单号",
                    "代收货款", "代收货款金额", "订单号", "备注2"};

            PoiExcelUtil.buildTitles(sheet1, titles);
            for (int i = 0; i < billList.size(); i++) {
                int column = 0;
                EMSExcelEntity bill = billList.get(i);
                HSSFRow rowdata = sheet1.createRow(i+1);//创建第i+1行
                // 下面是填充数据
                rowdata.createCell(column).setCellValue("");//寄件客户 编码 （自填）
                rowdata.createCell(++column).setCellValue(bill.getConsigneeName()); //收件人
                rowdata.createCell(++column).setCellValue(bill.getConsigneeCompany());//收件公司
                rowdata.createCell(++column).setCellValue(bill.getConsigneePhone());//收件人联系方式
                rowdata.createCell(++column).setCellValue(bill.getConsigneeAddress());//收件人地址
                rowdata.createCell(++column).setCellValue(bill.getWaybillNum());//运单号
                rowdata.createCell(++column).setCellValue(bill.getIsCOD());//是否货到付款
                rowdata.createCell(++column).setCellValue(bill.getCODTotalPrice()); //货到付款代收金额
                rowdata.createCell(++column).setCellValue(bill.getOrderId());//订单号
                rowdata.createCell(++column).setCellValue("");
                PoiExcelUtil.dealWithCellsNullString(rowdata, 0, column);
            }
            String writeName = "EMSWaybillExcel" + new Date().getTime();
            PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
            success = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
            if (!success) {
                StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/transportList", response);
            } else {
                StageHelper.optSelectSaveState(request, saveStateGroupId, null, -1);
            }
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/transportList", response);
        }
    }


    private OrderQueryDTO initExportQueryDTO(HttpServletRequest request, int orderStatus) {
        OrderQueryDTO dto = OrderController.setSearchParams(request, orderStatus);
        dto.setSort(ServletRequestUtils.getStringParameter(request, "sort", "create_asc"));
        return dto;
    }
    
    /**
     * 导出退换货列表 备用，暂时不用
     * @param request
     * @param response
     */
//    @RequestMapping("/getretorderexcel")
    public void getRetOrderExcel(HttpServletRequest request,HttpServletResponse response){
    	String beginDate = ServletRequestUtils.getStringParameter(request,"retord_start",null);
        String endDate = ServletRequestUtils.getStringParameter(request,"retord_end",null);
        if(beginDate == null || endDate == null || "".equals(beginDate) || "".equals(endDate)){
            StageHelper.dwzFailForward("请选择起始时间和终止时间!", "143", "/account/export/port", response);
            return;
        }
        //获取数据列表
        List<RetOrdReportRow> list = ReturnOrderClient.getRetOrderExcel(beginDate + " 00:00:00", endDate + " 23:59:59");
//        List<RetOrdReportRow> list = new ArrayList<RetOrdReportRow>();
        //构建表格
        String sheetName = "美丽湾退换货记录表";
        String[] title ={"序号","创建日期","退货编号","原订单编号","商品编码","商品条形码","商品名称","商品购买价","退商品数量（负数）","退运费金额","补偿运费金额","退款总金额","订单状态"};
        try {
        	// 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet = wb.createSheet(sheetName);
            // 创建标题
            PoiExcelUtil.buildTitles(sheet, title);
            // 创建内容行
            if(list != null && list.size() > 0){
            	int rowNum = 1;//新行号
            	for(RetOrdReportRow row : list){
            		if(row != null){
            			HSSFRow rowdata = sheet.createRow(rowNum);
            			// 下面是填充数据
            			rowdata.createCell(0).setCellValue(rowNum) ;// 序号
            			rowdata.createCell(1).setCellValue(DateUtil.getDateTimeStr(row.getCreateTime())) ;// 创建日期
            			rowdata.createCell(2).setCellValue(row.getRetordOrderId()) ;// 退货编号
            			rowdata.createCell(3).setCellValue(row.getOldOrderId()) ;// 原订单编号
            			rowdata.createCell(4).setCellValue(row.getProId()) ;// 商品编码
            			rowdata.createCell(5).setCellValue(row.getProBarCode()) ;// 商品条形码
            			rowdata.createCell(6).setCellValue(row.getProName()) ;// 商品名称
            			rowdata.createCell(7).setCellValue(row.getProdTotalAmount()) ;// 商品购买价
            			rowdata.createCell(8).setCellValue(row.getSaleNum()) ;// 退商品数量
            			rowdata.createCell(9).setCellValue(row.getRetPayFare()) ;// 退运费金额
            			rowdata.createCell(10).setCellValue(row.getRetPayCompensate()) ;// 补偿运费金额
            			rowdata.createCell(11).setCellValue(row.getRetTotalAmount()) ;// 订单价格
            			rowdata.createCell(12).setCellValue(row.getIsEndNode() == 1 ? "已退款":"未退款") ;// 订单状态
            			
            			rowNum++;
            		}
            	}
            }
            String writeName = "getRetOrderExcelList" + new Date().getTime() + "";
            PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
        	PoiExcelUtil.download(request, response, "xls", exportPath + writeName, sheetName + ".xls");
            
        }catch (LimitException e){
            logger.error(e, "导出的总记录条数超过5万条", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出的总记录条数超过5万条，请联系技术部门", "143", "/account/export/port", response);
        }
        catch (Exception e) {
            logger.error(e, "美丽湾退换货记录表导出失败", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出美丽湾退换货记录表失败，请联系技术部门", "143", "/account/export/port", response);
        }
    }
    
    /**
     * 导出退换货列表
     * @param request
     * @param response
     */
    @RequestMapping("/getretorderexcel")
    public void getRetOrderListExcel(HttpServletRequest request,HttpServletResponse response){
    	String beginDate = ServletRequestUtils.getStringParameter(request,"retord_start",null);
        String endDate = ServletRequestUtils.getStringParameter(request,"retord_end",null);
        if(beginDate == null || endDate == null || "".equals(beginDate) || "".equals(endDate)){
            StageHelper.dwzFailForward("请选择起始时间和终止时间!", "143", "/account/export/port", response);
            return;
        }
        //获取数据列表
//        List<RetOrdReportRow> list = ReturnOrderClient.getRetOrderExcel(beginDate + " 00:00:00", endDate + " 23:59:59");
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(beginDate)) {
        	beginDate = beginDate + " 00:00:00" ;
            sb.append(" create_time>=\"" + beginDate + "\"");
        }
        if (!StringUtils.isBlank(endDate)) {
        	endDate = endDate + " 23:59:59" ;
        	sb.append(" and create_time<=\"" + endDate + "\"");
        }
        PageInfo pageInfo = new PageInfo() ;
		pageInfo.setPagesize(Integer.MAX_VALUE);
        PagerControl<Retord> pc = ReturnOrderClient.getRetordPager(new Retord(), pageInfo, sb.toString());
        List<Retord> list = pc.getEntityList();
        //构建表格
        String sheetName = "美丽湾退换货记录表";
        String[] title ={"创建日期","退货编号","原订单编号","商品编码","商品条形码","商品名称","商品购买价","退商品数量（负数）","退运费金额","补偿运费金额","退款总金额","订单状态"};
        try {
        	// 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet = wb.createSheet(sheetName);
            // 创建标题
            PoiExcelUtil.buildTitles(sheet, title);
            // 创建内容行
            if(list != null && list.size() > 0){
            	int rowNum = 1;//新行号
            	for(Retord ret : list){
            		if(ret != null){
            			ret.setOrdiList(OrdClient.getOrdiListByOrderId(ret.getRetordOrderId()));
            			int newRowNum = rowNum;
            			for(Ordi reti : ret.getOrdiList() ){
            				HSSFRow rowdata = sheet.createRow(rowNum);
            				// 下面是填充数据
                			rowdata.createCell(0).setCellValue(DateUtil.getDateTimeStr(ret.getCreateTime())) ;// 创建日期
                			rowdata.createCell(1).setCellValue(ret.getRetordOrderId()) ;// 退货编号
                			rowdata.createCell(2).setCellValue(ret.getOldOrderId()) ;// 原订单编号
                			rowdata.createCell(3).setCellValue(reti.getProId()) ;// 商品编码
                			rowdata.createCell(4).setCellValue(reti.getProBarCode()) ;// 商品条形码
                			rowdata.createCell(5).setCellValue(reti.getProName()) ;// 商品名称
                			rowdata.createCell(6).setCellValue(reti.getUintPrice()) ;// 商品购买价
                			rowdata.createCell(7).setCellValue(reti.getSaleNum() > 0 ? "-"+reti.getSaleNum() : ""+reti.getSaleNum()) ;// 退商品数量
                			rowdata.createCell(8).setCellValue(ret.getRetPayFare()) ;// 退运费金额
                			rowdata.createCell(9).setCellValue(ret.getRetPayCompensate()) ;// 补偿运费金额
                			rowdata.createCell(10).setCellValue(ret.getRetTotalAmount() == null ? 0.00 : ret.getRetTotalAmount()) ;// 订单价格
                			rowdata.createCell(11).setCellValue(ret.getIsEndNode() == 1 && "80".equals(ret.getRetStatus()) ? "已退款":"未退款") ;// 订单状态
                			
                			rowNum++;
            			}
            			
            			if(ret.getOrdiList().size() > 1){
            				mergedRegion(newRowNum, rowNum - 1, 0, 0, sheet);
            				mergedRegion(newRowNum, rowNum - 1, 1, 1, sheet);
            				mergedRegion(newRowNum, rowNum - 1, 2, 2, sheet);
            				mergedRegion(newRowNum, rowNum - 1, 8, 8, sheet);
            				mergedRegion(newRowNum, rowNum - 1, 9, 9, sheet);
            				mergedRegion(newRowNum, rowNum - 1, 10, 10, sheet);
            				mergedRegion(newRowNum, rowNum - 1, 11, 11, sheet);
            			}
            			
            		}
            	}
            }
            String writeName = "getRetOrderExcelList" + new Date().getTime() + "";
            PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
        	PoiExcelUtil.download(request, response, "xls", exportPath + writeName, sheetName + ".xls");
            
        }catch (LimitException e){
            logger.error(e, "导出的总记录条数超过5万条", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出的总记录条数超过5万条，请联系技术部门", "143", "/account/export/port", response);
        }
        catch (Exception e) {
            logger.error(e, "美丽湾退换货记录表导出失败", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出美丽湾退换货记录表失败，请联系技术部门", "143", "/account/export/port", response);
        }
    }
    
    /**
     * 导出“发货明细表”报表
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getSendListExcel")
    public void getSendListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String excelName = "发货明细表";
        String sheet1Name = "发货明细表";
        boolean success = false;
        try {
            String saveStateGroupId = ServletRequestUtils.getStringParameter(request, "saveStateGroupId", "");
            String isSelectedExport = ServletRequestUtils.getStringParameter(request, "type", null);
            int orderStatus = ServletRequestUtils.getIntParameter(request, "os", 1);
            OrderQueryDTO dto = initExportQueryDTO(request,orderStatus);

            dto.setStatusType(OrderStatusType.ID.getType());

            int length = ServletRequestUtils.getIntParameter(request, "length", 1000); //最大页数
            PageInfo pageInfo = new PageInfo(1, length);
            List<OrdDTO> billList = new ArrayList<OrdDTO>();
            if (isSelectedExport != null) {
                billList = OrdExportClient.exportSendByIds(StageHelper.getOptSelectSaveState(request, saveStateGroupId).toArray(new String[0]));
            } else {
//                billList = OrdExportClient.exportSendList(dto, pageInfo, true).getEntityList();
            	boolean isOk = OrdExportClient.exportSendListForReport(dto, pageInfo, true);
            	if(isOk){
	            	try{
	                	String pcStr = ShardJedisTool.getInstance().get(JedisKey.oms$report, "exportSendListForReport");
	                	if(!StringUtils.isBlank(pcStr)){
	                		PagerControl<OrdDTO> pc = new Gson().fromJson(pcStr ,new TypeToken<PagerControl<OrdDTO>>() {}.getType()) ;
	                		billList = pc.getEntityList();
	                	}
	                	
	                }catch (Exception e) {
	        			logger.error(e, "读取缓存数据失败.", WebUtils.getIpAddr(request));
	        			StageHelper.writeString("读取缓存数据失败异常，无法导出，请联系技术部", response);
	        			return;
	        		}
            	}
            }

            //没有可打印单据 提示用户
            if (billList == null || billList.isEmpty()) {
                StageHelper.writeString("没有符合条件的订单 ，无法导出", response);
                return;
            }

            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet = wb.createSheet(sheet1Name);
            String[] titles = {"订单号", "支付日期", "订单来源", "商品ID", "商品条形码", "商品名称", "商品数量", "订单价格",
                    "收货人", "收货电话", "收货地址", "订单备注", "快递公司", "运单号"};

            PoiExcelUtil.buildTitles(sheet, titles);
            int rowNum = 1;//新行号
            for (int i = 0; i < billList.size(); i++) {
            	OrdDTO bill = billList.get(i);
            	if(bill == null){
            		continue ;
            	}
            	OrdAddr ordAddr = new OrdAddr() ;
            	if(!StringUtils.isBlank(bill.getOrderId())){
            		ordAddr = OrdClient.getOrdAddrById(bill.getOrderId());
            	}
            	List<OrdiDTO> blist = bill.getOrdiList() ;
                
                int newRowNum = rowNum;
            	for(OrdiDTO odi : blist ){
            		HSSFRow rowdata = sheet.createRow(rowNum);
            		// 下面是填充数据
                    rowdata.createCell(0).setCellValue(bill.getOrderId());
                    rowdata.createCell(1).setCellValue(DateUtil.getDateTimeStr(bill.getPayTime()));
                    boolean isSF = checkSFUser(bill.getUserName(), bill.getUid());
                    rowdata.createCell(2).setCellValue(isSF ? "顺丰" : "自营");
                    rowdata.createCell(3).setCellValue(odi.getProId());
                    rowdata.createCell(4).setCellValue(odi.getProBarCode());
                    rowdata.createCell(5).setCellValue(odi.getProName());
                    rowdata.createCell(6).setCellValue(odi.getSaleNum());
                    rowdata.createCell(7).setCellValue(String.valueOf(bill.getRealPayAmount()));
                    rowdata.createCell(8).setCellValue(bill.getRecvName());
                    rowdata.createCell(9).setCellValue(ordAddr == null ? "" : (!StringUtils.isBlank(ordAddr.getMobile()) ? ordAddr.getMobile() : ordAddr.getPhone()) );
                    rowdata.createCell(10).setCellValue(ordAddr != null ? (ordAddr.getProvince()+ordAddr.getCity()+ordAddr.getCounty()+ordAddr.getDetailAddr()) : "");
                    StringBuffer cStr = new StringBuffer() ;
                    int rCount = bill.getRemarkCount();
                    if(rCount > 0){
                    	List<OrdRemark> remarkList = OrdClient.getRemarksByOrderId(bill.getOrderId());
                    	if(remarkList != null && remarkList.size() > 0){
                    		for(int n=0; n < remarkList.size(); n++){
                    			OrdRemark r = remarkList.get(n);
                    			cStr.append(n+1).append(".").append(r.getContent()).append("; \r\n");
                    		}
                    	}
                    }
                    rowdata.createCell(11).setCellValue(cStr.toString());
                    rowdata.createCell(12).setCellValue(bill.getLogisticsCompany());
                    rowdata.createCell(13).setCellValue(bill.getLogisticsNumber());
                    
                    rowNum++;
            	}
                
                if(blist != null && blist.size() > 1){
    				mergedRegion(newRowNum, rowNum - 1, 0, 0, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 1, 1, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 2, 2, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 7, 7, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 8, 8, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 9, 9, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 10, 10, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 11, 11, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 12, 12, sheet);
    				mergedRegion(newRowNum, rowNum - 1, 13, 13, sheet);
    			}
            }
            
            String writeName = "SendListExcel" + new Date().getTime();
            PoiExcelUtil.createFile(exportPath, writeName, wb); 
            success = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls"); 
            if (!success) {
                StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
            } else {
                StageHelper.optSelectSaveState(request, saveStateGroupId, null, -1);
            }
            
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
        }
    }
    
    /**
     * 条件运单表：条件下的全部订单运单表，无运单号的，默认运单号为空即可
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getTotalSendExcel")
    public void getTotalSendExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String excelName = "条件运单表";
        String sheet1Name = "条件运单表";
        boolean success = false;
        try {
            String saveStateGroupId = ServletRequestUtils.getStringParameter(request, "saveStateGroupId", "");
            String isSelectedExport = ServletRequestUtils.getStringParameter(request, "type", null);
            int orderStatus = ServletRequestUtils.getIntParameter(request, "os", 1);
            OrderQueryDTO dto = initExportQueryDTO(request,orderStatus);

            dto.setStatusType(OrderStatusType.ID.getType());

            int length = ServletRequestUtils.getIntParameter(request, "length", 1000); //最大页数
            PageInfo pageInfo = new PageInfo(1, length);
            List<OrdDTO> billList = new ArrayList<OrdDTO>();
            if (isSelectedExport != null) {
                billList = OrdExportClient.exportSendByIds(StageHelper.getOptSelectSaveState(request, saveStateGroupId).toArray(new String[0]));
            } else {
//                billList = OrdExportClient.exportSendList(dto, pageInfo, true).getEntityList();
            	boolean isOk = OrdExportClient.exportSendListForReport(dto, pageInfo, true);
            	if(isOk){
	            	try{
	                	String pcStr = ShardJedisTool.getInstance().get(JedisKey.oms$report, "exportSendListForReport");
	                	if(!StringUtils.isBlank(pcStr)){
	                		PagerControl<OrdDTO> pc = new Gson().fromJson(pcStr ,new TypeToken<PagerControl<OrdDTO>>() {}.getType()) ;
	                		billList = pc.getEntityList();
	                	}
	                	
	                }catch (Exception e) {
	        			logger.error(e, "读取缓存数据失败.", WebUtils.getIpAddr(request));
	        			StageHelper.writeString("读取缓存数据失败异常，无法导出，请联系技术部", response);
	        			return;
	        		}
            	}
            }

            //没有可打印单据 提示用户
            if (billList == null || billList.isEmpty()) {
                StageHelper.writeString("没有符合条件的订单 ，无法导出", response);
                return;
            }

            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet = wb.createSheet(sheet1Name);
            String[] titles = {"订单号", "寄件公司", "联系人", "联系电话", "寄件地址", "收件公司",
                    "联系人", "联系电话", "手机号码", "收件详细地址", "付款方式","托寄物内容","托寄物数量",
                    "件数","实际重量(KG)","计费重量(KG)","业务类型","是否代收货款","代收货款金额","是否保价","保价金额",
                    "其他费用(元)","是否自取","是否签回单","收件员","寄方签名","寄件日期","MSG","派件出仓短信(SMS)",
                    "寄方客户备注","自定义字段1","自定义字段2","自定义字段3"};

            PoiExcelUtil.buildTitles(sheet, titles);
            int rowNum = 1;//新行号
            for (int i = 0; i < billList.size(); i++) {
            	OrdDTO bill = billList.get(i);
            	if(bill == null){
            		continue ;
            	}
            	OrdAddr ordAddr = new OrdAddr() ;
            	if(!StringUtils.isBlank(bill.getOrderId())){
            		ordAddr = OrdClient.getOrdAddrById(bill.getOrderId());
            	}
                
        		HSSFRow rowdata = sheet.createRow(rowNum);
        		// 下面是填充数据
                rowdata.createCell(0).setCellValue(bill.getOrderId());
                rowdata.createCell(1).setCellValue("美丽传说");
                rowdata.createCell(2).setCellValue("美丽湾");
                rowdata.createCell(3).setCellValue("4006-887-887");
                rowdata.createCell(4).setCellValue("广西省南宁市西乡塘区东盟科技企业孵化基地二期1号厂房2楼");
                rowdata.createCell(5).setCellValue("个人");
                rowdata.createCell(6).setCellValue(bill.getRecvName());
                rowdata.createCell(7).setCellValue(ordAddr == null ? "" : (!StringUtils.isBlank(ordAddr.getPhone()) ? ordAddr.getPhone() : "") );
                rowdata.createCell(8).setCellValue(ordAddr == null ? "" : (!StringUtils.isBlank(ordAddr.getMobile()) ? ordAddr.getMobile() : "") );
                rowdata.createCell(9).setCellValue(ordAddr != null ? (ordAddr.getProvince()+ordAddr.getCity()+ordAddr.getCounty()+ordAddr.getDetailAddr()) : "");
                rowdata.createCell(10).setCellValue(bill.getOrderType().equals(OrderType.RCD) ? "货到付款" : "在线支付");//付款方式
                rowdata.createCell(11).setCellValue("");//托寄物内容
                int totalNum = 0 ;
                for(OrdiDTO odi : bill.getOrdiList()){
                	totalNum = totalNum + odi.getSaleNum();
                }
                rowdata.createCell(12).setCellValue(totalNum);//托寄物数量(目前为种数bill.getTotalItem())
                rowdata.createCell(13).setCellValue("1");//件数
                rowdata.createCell(14).setCellValue("");//实际重量
                rowdata.createCell(15).setCellValue("");//计费重量
                rowdata.createCell(16).setCellValue("陆运");//业务类型
                rowdata.createCell(17).setCellValue(bill.getOrderType().equals(OrderType.RCD) ? "Y" : "N");//是否代收货款
                rowdata.createCell(18).setCellValue(bill.getOrderType().equals(OrderType.RCD) ? String.valueOf(bill.getRealPayAmount()) : "0.00") ;
                rowdata.createCell(19).setCellValue("") ;//是否保价
                rowdata.createCell(20).setCellValue("") ;//保价金额
                rowdata.createCell(21).setCellValue("") ;//其他费用
                rowdata.createCell(22).setCellValue("") ;//是否自取
                rowdata.createCell(23).setCellValue("") ;//是否签回单
                rowdata.createCell(24).setCellValue("") ;//收件员
                rowdata.createCell(25).setCellValue("") ;//签方签名
                rowdata.createCell(26).setCellValue(DateUtil.getDateStr(new Date())) ;//寄件日期
                rowdata.createCell(27).setCellValue("") ;//MSG
                rowdata.createCell(28).setCellValue("") ;//派件出仓短信
                rowdata.createCell(29).setCellValue(bill.getOrderId()) ;//寄方客户备注 订单号
                rowdata.createCell(30).setCellValue("") ;//自定义字段1
                rowdata.createCell(31).setCellValue("") ;//自定义字段2
                rowdata.createCell(32).setCellValue("") ;//自定义字段3
                
                rowNum++;
            }
            
            String writeName = "getTotalSendExcel" + new Date().getTime();
            PoiExcelUtil.createFile(exportPath, writeName, wb); 
            success = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls"); 
            if (!success) {
                StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
            } else {
                StageHelper.optSelectSaveState(request, saveStateGroupId, null, -1);
            }
            
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
        }
    }
    
    /**
     * 导出给wms用的表格
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getExcelForWms")
    public void getExcelForWms(Model model, HttpServletRequest request, HttpServletResponse response) {
        String excelName = "销售订单导入模板-美丽湾";
        String sheet1Name = "订单包裹信息";
        String sheet2Name = "订单商品信息";
        boolean success = false;
        try {
            String saveStateGroupId = ServletRequestUtils.getStringParameter(request, "saveStateGroupId", "");
            String isSelectedExport = ServletRequestUtils.getStringParameter(request, "type", null);
            int orderStatus = ServletRequestUtils.getIntParameter(request, "os", 1);
            OrderQueryDTO dto = initExportQueryDTO(request,orderStatus);

            dto.setStatusType(OrderStatusType.ID.getType());

            int length = ServletRequestUtils.getIntParameter(request, "length", 1000); //最大页数
            PageInfo pageInfo = new PageInfo(1, length);
            List<OrdDTO> billList = new ArrayList<OrdDTO>();
            if (isSelectedExport != null) {
                billList = OrdExportClient.exportSendByIds(StageHelper.getOptSelectSaveState(request, saveStateGroupId).toArray(new String[0]));
            } else {
//                billList = OrdExportClient.exportSendList(dto, pageInfo, true).getEntityList();
                boolean isOk = OrdExportClient.exportSendListForReport(dto, pageInfo, true);
            	if(isOk){
	            	try{
	                	String pcStr = ShardJedisTool.getInstance().get(JedisKey.oms$report, "exportSendListForReport");
	                	if(!StringUtils.isBlank(pcStr)){
	                		PagerControl<OrdDTO> pc = new Gson().fromJson(pcStr ,new TypeToken<PagerControl<OrdDTO>>() {}.getType()) ;
	                		billList = pc.getEntityList();
	                	}
	                	
	                }catch (Exception e) {
	        			logger.error(e, "读取缓存数据失败.", WebUtils.getIpAddr(request));
	        			StageHelper.writeString("读取缓存数据失败异常，无法导出，请联系技术部", response);
	        			return;
	        		}
            	}
            }

            //没有可打印单据 提示用户
            if (billList == null || billList.isEmpty()) {
                StageHelper.writeString("没有符合条件的订单 ，无法导出", response);
                return;
            }

            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet1 = wb.createSheet(sheet1Name);
            HSSFSheet sheet2 = wb.createSheet(sheet2Name);
            String[] titles1 = {"订单编号", "下单时间", "仓库编码", "物流公司编码", "物流公司名称", "收件人",
                    "收货地址", "邮政编码", "联系手机", "联系电话", "邮费","省","市",
                    "区","买家备注","卖家备注","店铺名字","买家昵称","买家实际付款","客户编码","预存款",
                    "发票抬头","货到付款","是否需要发票true/false", "是否紧急", "运费到付","预售类型","组织架构","销售类型"};
            String[] titles2 = {"订单编号", "物料编码", "物料名称", "规格", "花色", "数量",
                    "单价", "优惠金额", "总价"};

            PoiExcelUtil.buildTitles(sheet1, titles1);
            PoiExcelUtil.buildTitles(sheet2, titles2);
            int rowNum1 = 1;//新行号
            int rowNum2 = 1;//新行号
            for (int i = 0; i < billList.size(); i++) {
            	OrdDTO bill = billList.get(i);
            	List<OrdiDTO> blist = bill.getOrdiList() ;
            	if(bill == null){
            		continue ;
            	}
            	OrdAddr ordAddr = new OrdAddr() ;
            	if(!StringUtils.isBlank(bill.getOrderId())){
            		ordAddr = OrdClient.getOrdAddrById(bill.getOrderId());
            	}
                
        		HSSFRow rowdata = sheet1.createRow(rowNum1);
        		// 下面是填充数据
                rowdata.createCell(0).setCellValue(bill.getOrderId());//订单编号
                rowdata.createCell(1).setCellValue(DateUtil.getDateStr(bill.getCreateTime()));//下单时间
                rowdata.createCell(2).setCellValue("");//仓库编码
                rowdata.createCell(3).setCellValue("");//物流公司编码
                rowdata.createCell(4).setCellValue("");//物流公司名称
                rowdata.createCell(5).setCellValue(bill.getRecvName());//收件人
                rowdata.createCell(6).setCellValue(ordAddr != null ? (ordAddr.getProvince()+ordAddr.getCity()+ordAddr.getCounty()+ordAddr.getDetailAddr()) : "无");//收货地址
                rowdata.createCell(7).setCellValue(ordAddr == null ? "" : ordAddr.getZipCode());//邮政编码
                rowdata.createCell(8).setCellValue(ordAddr == null ? "" : (!StringUtils.isBlank(ordAddr.getMobile()) ? ordAddr.getMobile() : "") );//联系手机
                rowdata.createCell(9).setCellValue(ordAddr == null ? "" : (!StringUtils.isBlank(ordAddr.getPhone()) ? ordAddr.getPhone() : "") );//联系电话
                rowdata.createCell(10).setCellValue(String.valueOf(bill.getTransportFee()));//邮费
                rowdata.createCell(11).setCellValue(ordAddr == null ? "" : ordAddr.getProvince());//省
                rowdata.createCell(12).setCellValue(ordAddr == null ? "" : ordAddr.getCity());//市
                rowdata.createCell(13).setCellValue(ordAddr == null ? "" : ordAddr.getCounty());//区
                rowdata.createCell(14).setCellValue("无");//买家备注
                rowdata.createCell(15).setCellValue(StringUtils.isBlank(bill.getOrderComments()) ? "无": bill.getOrderComments());//卖家备注
                rowdata.createCell(16).setCellValue("无");//店铺名字
                rowdata.createCell(17).setCellValue(bill.getUserName());//买家昵称
                rowdata.createCell(18).setCellValue(String.valueOf(bill.getRealPayAmount()));//买家实际付款
                rowdata.createCell(19).setCellValue(bill.getUid());//客户编码
                rowdata.createCell(20).setCellValue("0");//预存款
                //简单处理下发票抬头
                String titleI = "" ;
                if(OrdClient.getDetail(bill.getOrderId()) != null && OrdClient.getDetail(bill.getOrderId()).getInvoice() != null){
                	titleI = OrdClient.getDetail(bill.getOrderId()).getInvoice().getInvoiceHead() ;
                	if(!StringUtils.isBlank(titleI)){
                		if(titleI.indexOf("&#40;") > 0){
                			titleI = titleI.replace("&#40;", "(");
                		}
                		if(titleI.indexOf("&#41;") > 0){
                			titleI = titleI.replace("&#41;", ")");
                		}
                	}
                }
                rowdata.createCell(21).setCellValue(!StringUtils.isBlank(titleI) ? titleI : "无");//发票抬头
                rowdata.createCell(22).setCellValue(bill.getOrderType().equals(OrderType.RCD) ? "1" : "0");//货到付款
                rowdata.createCell(23).setCellValue(bill.getIsInvoice() != 1 ? "FALSE" : "TRUE");//是否需要发票true/false
                rowdata.createCell(24).setCellValue("0");//是否紧急
                rowdata.createCell(25).setCellValue(bill.getOrderType().equals(OrderType.RCD) ? "TRUE" : "FALSE");//运费到付
                rowdata.createCell(26).setCellValue(bill.getOrderType() == "RPS" ? "2" : "0") ;//预售类型
                rowdata.createCell(27).setCellValue("01") ;//组织架构
                rowdata.createCell(28).setCellValue("01") ;//销售类型
                
                int newRowNum = rowNum2 ;
                for(OrdiDTO odi : blist ){
	        		HSSFRow rowdata2 = sheet2.createRow(rowNum2);
	        		// 下面是填充数据
	        		rowdata2.createCell(0).setCellValue(bill.getOrderId());//订单编号
	        		rowdata2.createCell(1).setCellValue(odi.getProBarCode());//物料编码
	        		rowdata2.createCell(2).setCellValue(odi.getProName());//物料名称
	        		List<ProPropertyValue> plist = ProPropertyClient.getSkuProVsByProId(odi.getProId()) ;
	        		ProPropertyValue p1 = null ;
	        		ProPropertyValue p2 = null ;
	        		if(plist != null && plist.size() > 0){
	        			p1 = plist.get(0);
	        			if(plist.size() > 1){
	        				p2 = plist.get(1);
	        			}
	        		}
	        		rowdata2.createCell(3).setCellValue(p2 == null ? "无" : p2.getName());//规格
	        		rowdata2.createCell(4).setCellValue(p1 == null ? "无" : p1.getName());//花色
	        		rowdata2.createCell(5).setCellValue(odi.getSaleNum());//数量
	        		rowdata2.createCell(6).setCellValue(String.valueOf(odi.getUintPrice()));//单价
	        		rowdata2.createCell(7).setCellValue(String.valueOf(odi.getFavorableAmount()));//优惠金额
	        		rowdata2.createCell(8).setCellValue(String.valueOf(bill.getRealPayAmount()));//总价
	        		
	        		rowNum2 ++ ;
                }
                
//                if(blist != null && blist.size() > 1){
//    				mergedRegion(newRowNum, rowNum2 - 1, 0, 0, sheet2);
//    				mergedRegion(newRowNum, rowNum2 - 1, 8, 8, sheet2);
//    			}
                
                rowNum1 ++ ;
            }
            
            String writeName = "getExcelForWms" + new Date().getTime();
            PoiExcelUtil.createFile(exportPath, writeName, wb); 
            success = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls"); 
            if (!success) {
                StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
            } else {
                StageHelper.optSelectSaveState(request, saveStateGroupId, null, -1);
            }
            
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "143", "/oms/order/list", response);
        }
    }
    
    
    //合并单元格
    public void mergedRegion(int intFirstRow, int intLastRow, int intFirstColumn, int intLastColumn, HSSFSheet sheet) {
        //合并数据、数据仅保留一个
        for (int i = intFirstRow; i <= intLastRow; i++) {
            HSSFRow row = sheet.getRow(i);
            for (int j = intFirstColumn; j <= intLastColumn; j++) {
                if (sheet.getRow(intFirstRow).getCell(intFirstColumn) != row.getCell(j)) {
                    row.getCell(j).setCellValue("");
                }
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(intFirstRow, intLastRow, intFirstColumn, intLastColumn));
    }
    
    /**
     * 检查是否顺丰用户
     * @param userName
     * @param uid
     * @return
     */
    private boolean checkSFUser(String userName, Integer uid){
    	boolean isSF = false ;
    	if(!StringUtils.isBlank(userName) && userName.indexOf("@sf-express.com") != -1){
        	isSF = true ;
        }else{
        	Integer[] uids = {uid};
			List<UserPassportSimple> listu = UserPassportClient.getPassportSimpleByUids(uids) ;
			if(listu!= null && listu.size()>0){
				UserPassportSimple simpleu = listu.get(0);
				if(simpleu != null && !StringUtils.isBlank(simpleu.getEmail()) && simpleu.getEmail().indexOf("@sf-express.com") != -1){
					isSF = true ;
				}
			}
        }
    	return isSF ;
    }
}
