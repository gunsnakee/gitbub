package com.meiliwan.emall.monitor.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Row;

import com.meiliwan.emall.monitor.util.PoiExcelUtil;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.monitor.bean.UnionOrder;
import com.meiliwan.emall.monitor.service.UnionOrderService;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/union")
public class UnionStatController {

	private final static MLWLogger MLW_LOGGER = MLWLoggerFactory.getLogger(UnionStatController.class);
	private UnionOrderService uoService = new UnionOrderService();
	private final static String FILEPATH_STRING = "/data/file/union_orders/";
	
	
	/***
	 * 内部查询订单接口  默认给出7天内的订单数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/query")
	public String query(HttpServletRequest request, HttpServletResponse response){
		
		String source = request.getParameter("source");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		Integer ordStatus = StringUtils.isBlank(request.getParameter("orderStatus")) ? null : Integer.valueOf(request.getParameter("orderStatus"));
		
		if(StringUtils.isBlank(source)){
			source = "2wCode_sf";
		}
		
		if (StringUtils.isBlank(endTime)) {
			endTime = DateUtil.getCurrentDateTimeStr();
		}
		
		if (StringUtils.isBlank(startTime)) {
			startTime = DateUtil.getDateTimeStr(DateUtil.parseDate(DateUtil.getDateStr(DateUtil.timeAddByDays(DateUtil.parseDateTime(endTime), -7))));
		}
		
		List<UnionOrder> uoList = null;
		if(source.startsWith("2wCode")){
			uoList = uoService.get2wCodeListBySourceId(source.split("_")[1], startTime, endTime, ordStatus);
		}
		
		request.setAttribute("uoList", uoList);
		request.setAttribute("startTime", DateUtil.parseDateTime(startTime));
		request.setAttribute("endTime", DateUtil.parseDateTime(endTime));
		request.setAttribute("orderStatus", ordStatus==null?"":ordStatus.intValue());
		return "/union/query_result";
	}
	
	/**
	 * 将订单数据导出到excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/download")
	public String getOrderExeclFile(HttpServletRequest request, HttpServletResponse response){
		String source = request.getParameter("source");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		Integer ordStatus = StringUtils.isBlank(request.getParameter("orderStatus")) ? null : Integer.valueOf(request.getParameter("orderStatus"));
		
		if(StringUtils.isBlank(source)){
			source = "2wCode_sf";
		}
		
		String errorMsg = "";
		if (StringUtils.isBlank(source) || !source.startsWith("2wCode") || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
			errorMsg = "请求参数异常！";
			request.setAttribute("errorMsg", errorMsg);
			return "union/UnionDowload";
		}
		
		List<UnionOrder> unionOrders = uoService.get2wCodeListBySourceId(source.split("_")[1], startTime, endTime, ordStatus);
		String fileName = source + "_" + startTime + "_" + "_" + endTime + "_" +DateUtil.getCurrentDateTimeStr() + ".xls";
		fileName = StringUtils.replace(StringUtils.replace(fileName, ":", "-"), " ", "-");
		boolean result = getExcelFile(FILEPATH_STRING, fileName, unionOrders); 
		if (result) {
			try {
				PoiExcelUtil.download(request, response, "application/x-excel", FILEPATH_STRING+fileName, fileName);
				return null;
			} catch (Exception e) {
				MLW_LOGGER.error(e, "source:" +source +" startTime:"+startTime +" endTime:"+endTime +" orderStatus:"+ ordStatus, WebUtils.getClientIp(request));
				errorMsg = "下载过程出错！";
				request.setAttribute("errorMsg", errorMsg);
				return "union/UnionDowload";
			}
		}else {
			errorMsg = "程序在服务器端生成excel文件失败!";
			request.setAttribute("errorMsg", errorMsg);
			return "union/UnionDowload";
		}
	}

	/**
	 * 根据 UnionOrder列表生成excel
	 * @param writePath  存储路径
	 * @param writeName  文件名
	 * @param unionOrders  订单列表
	 * @return
	 */
	private boolean getExcelFile(String writePath,String writeName,List<UnionOrder> unionOrders){
		if (unionOrders == null || unionOrders.size() <= 0) {
			return false;
		}
		
		HSSFWorkbook excelWorkbook = new HSSFWorkbook();
		String[] colums = {"订单号",	"商品ID","商品名称","商品单价","商品数量","订单价格","订单状态","下单时间"};
		HSSFSheet excelSheet = PoiExcelUtil.buildTitles(excelWorkbook.createSheet(writeName), colums);
		
		short rowNum = 1;
		for (UnionOrder unionOrder : unionOrders) {
			short tmpRowNum = rowNum;
			for (UnionOrder.ProductInfo productInfo : unionOrder.getProInfos()) {
				Row row = excelSheet.createRow(tmpRowNum);
				row.createCell(0);
				row.createCell(1).setCellValue(productInfo.getProId());
				row.createCell(2).setCellValue(productInfo.getProName());
				row.createCell(3).setCellValue(productInfo.getPrice());
				row.createCell(4).setCellValue(productInfo.getBuyNum());
				row.createCell(5);
				row.createCell(6);
				row.createCell(7);
				
				tmpRowNum ++;
			}
			
			Row row = excelSheet.getRow(rowNum);
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,0,0));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,5,5));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,6,6));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,7,7));
			
			row.getCell(0).setCellValue(unionOrder.getOrderId());
			row.getCell(5).setCellValue(unionOrder.getTotalPrice());
			row.getCell(6).setCellValue(unionOrder.getOrderStringStatus());
			row.getCell(7).setCellValue(DateUtil.getDateTimeStr(unionOrder.getCreateTime()));
			
			rowNum = tmpRowNum;
		}
		
		try {
			PoiExcelUtil.createFile(writePath, writeName, excelWorkbook);
		} catch (FileNotFoundException e) {
			MLW_LOGGER.error(e, "writePath:"+writePath + " writeName:" +writeName, "");
			return false;
		} catch (IOException e) {
			MLW_LOGGER.error(e, "writePath:"+writePath + " writeName:" +writeName, "");
			return false;
		}
		
		return true;
	}
}
