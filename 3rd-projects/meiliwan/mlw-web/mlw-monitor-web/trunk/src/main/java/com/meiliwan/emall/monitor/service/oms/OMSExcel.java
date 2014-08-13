package com.meiliwan.emall.monitor.service.oms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.service.wms.OrdSheet;

public class OMSExcel {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(OMSExcel.class);
	static String  title[] = {"平台单号","平台名称","店铺名称",
		"交易状态","下单时间","更新时间",
		"支付时间","是否货到付款","会员昵称","发票抬头",
		"发票类型","发票明细","卖家留言","买家留言",
		"商家留言","应收金额","实际支付","收件人",
		"电话","收件地址","省","市","区","邮编"};
	static String proTitle[]={"平台单号","平台SKU","店铺SKU","数量","商品标题","价格","实际金额","优惠金额","应付金额","开票金额"};
	
	public static HSSFWorkbook createExcel(List<OrdSheet> sheets) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(); // 新建一个工作簿
			HSSFSheet ordSheet = workbook.createSheet("订单包裹信息");// 创建一个工作表
			HSSFSheet proSheet = workbook.createSheet("订单商品信息");// 创建一个工作表
			HSSFRow rowTitle = ordSheet.createRow(0);// 创建一行
			for (int k = 0; k < title.length; k++) {
				HSSFCell cell = rowTitle.createCell(k);
				cell.setCellValue(title[k]);
			}
			int rowIndex = 1;
			for (OrdSheet<OMSBean,OMSProduct> sheetBean : sheets) {
				for (OMSBean wms : sheetBean.getOrds()) {
					if(!wms.isValid()){
						continue;
					}
					HSSFRow rowData = ordSheet.createRow(rowIndex);// 创建一行
					rowIndex++;
					List<String> attrs = wms.toList();
					for (int i = 0; i < attrs.size(); i++) {
						HSSFCell cell = rowData.createCell(i);
						if (StringUtils.isBlank(attrs.get(i))) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(attrs.get(i));
						}
					}
				}
			}
			
			HSSFRow rowTitlePro = proSheet.createRow(0);// 创建一行
			for (int k = 0; k < proTitle.length; k++) {
				HSSFCell cell = rowTitlePro.createCell(k);
				cell.setCellValue(proTitle[k]);
			}
			
				
			rowIndex = 1;
			for (OrdSheet<OMSBean,OMSProduct> sheetBean : sheets) {
				for (OMSProduct pro : sheetBean.getPros()) {
					HSSFRow rowData = proSheet.createRow(rowIndex);// 创建一行
					rowIndex++;
					List<String> attrs = pro.toList();
					for (int i = 0; i < attrs.size(); i++) {
						HSSFCell cell = rowData.createCell(i);
						if (StringUtils.isBlank(attrs.get(i))) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(attrs.get(i));
						}
					}
				}
			}
			return workbook;
		} catch (Exception e) {
			logger.error(e, null, null);
		}
		return null;
	}

}