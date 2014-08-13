package com.meiliwan.emall.union.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.commons.util.SpringContextUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.bean.UnionOrder;
import com.meiliwan.emall.union.service.UnionOrderService;
import com.meiliwan.emall.union.util.PoiExcelUtil;

/**
 * 
 * @author lsf
 * 
 */
@Controller
@RequestMapping("/channel")
public class ChannelController {
	private static final MLWLogger LOG = MLWLoggerFactory
			.getLogger(ChannelController.class);
	private final static String FILEPATH_STRING = "/data/file/union_orders/";
	
	@RequestMapping("login")
	public String login(HttpServletRequest request,
			HttpServletResponse response){
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			String passwd = ConfigOnZk.getInstance().getValue("union-web/app_channel.properties", userName + ".password");
			if(StringUtils.isNotBlank(password) && password.equals(passwd)){
				WebUtils.setCookieValue(response, "_mun", userName, "/", -1);
				return "redirect:/channel/query?source=" + ("admin".equals(userName) ? "" : ConfigOnZk.getInstance().getValue("union-web/app_channel.properties", userName + ".source", userName));
			}
			
		} catch (BaseException e) {
			LOG.error(e, "get config on zk failure.",
					WebUtils.getIpAddr(request));
		}
		request.setAttribute("errorMsg", "用户名或密码不正确，请重新输入");
		return "channel/login";
	}
	
	@RequestMapping("index")
	public String toIndex(){
		return "channel/login";
	}

	@RequestMapping("/query")
	public String query(HttpServletRequest request) {
		
		String userName = WebUtils.getCookieValue(request, "_mun");
		if(StringUtils.isBlank(userName)){
			return "channel/login";
		}
		String source = request.getParameter("source");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		Integer ordStatus = StringUtils.isBlank(request
				.getParameter("orderStatus")) ? null : Integer.valueOf(request
				.getParameter("orderStatus"));

		if(StringUtils.isBlank(source) && !"admin".equals(userName)){
			request.setAttribute("errorMsg", "当前用户存在异常，请重新登录");
			return "channel/login";
		}
		
		Map<String, String> channelMap = new HashMap<String, String>();
		String[] sources = null;
		StringBuilder scBuilder = new StringBuilder();
		try {
			sources = ConfigOnZk.getInstance().getArrayValue(
					"union-web/app_channel.properties", "app.channel.sources");
			if (sources != null && sources.length > 0) {
				for (String sc : sources) {
					channelMap.put(
							sc,
							ConfigOnZk.getInstance().getValue(
									"union-web/app_channel.properties",
									"channel." + sc));
					scBuilder.append(sc);
					scBuilder.append(",");
				}
			}
		} catch (BaseException e) {
			LOG.error(e, "get config on zk failure.",
					WebUtils.getIpAddr(request));
		}

		if (StringUtils.isBlank(source) && scBuilder.length() > 0) {
			scBuilder.setLength(scBuilder.length() - 1);

			source = scBuilder.toString();
		}

		if (StringUtils.isBlank(endTime)) {
			endTime = DateUtil.getCurrentDateTimeStr();
		}

		if (StringUtils.isBlank(startTime)) {
			startTime = DateUtil.getDateTimeStr(DateUtil.parseDate(DateUtil
					.getDateStr(DateUtil.timeAddByDays(
							DateUtil.parseDateTime(endTime), -7))));
		}
		
		Date startDate = DateUtil.parseDateTime(startTime);
		Date endDate = DateUtil.parseDateTime(endTime);
		if(startDate.getTime() > endDate.getTime()){
			request.setAttribute("dateInValid", "开始时间不能大于结束时间");
		}

		UnionOrderService uoService = (UnionOrderService) SpringContextUtil
				.getBean("unionOrderService");
		List<UnionOrder> uoList = uoService.get2wCodeListBySource(source,
				startTime, endTime, ordStatus);

		request.setAttribute("totalPrice", getTotalOrderPrice(uoList));
		request.setAttribute("uoList", uoList);
		request.setAttribute("startTime", startDate);
		request.setAttribute("endTime", endDate);
		request.setAttribute("orderStatus",
				ordStatus == null ? "" : ordStatus.intValue());
		request.setAttribute("source", StringUtils.isBlank(source) ? ""
				: source);
		request.setAttribute("channelMap", channelMap);
		request.setAttribute("userName", userName);

		return "channel/query_result";
	}
	
	private String getTotalOrderPrice(List<UnionOrder> uoList){
		String priceStr = "";
		if(uoList != null){
			double totalPrice = 0.0;
			for(UnionOrder order : uoList){
				totalPrice += order.getTotalPrice();
			}
			
			priceStr = NumberUtil.format(totalPrice);
		}
		
		return priceStr;
	}

	@RequestMapping("export")
	public String exportData(HttpServletRequest request,
			HttpServletResponse response) {
		String userName = WebUtils.getCookieValue(request, "_mun");
		if(StringUtils.isBlank(userName)){
			return "channel/login";
		}
		
		String source = request.getParameter("source");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		Integer ordStatus = StringUtils.isBlank(request
				.getParameter("orderStatus")) ? null : Integer.valueOf(request
				.getParameter("orderStatus"));

		if(StringUtils.isBlank(source) && !"admin".equals(userName)){
			request.setAttribute("errorMsg", "当前用户存在异常，请重新登录");
			return "channel/login";
		}
		
		Map<String, String> channelMap = new HashMap<String, String>();
		String[] sources = null;
		StringBuilder scBuilder = new StringBuilder();
		try {
			sources = ConfigOnZk.getInstance().getArrayValue(
					"union-web/app_channel.properties", "app.channel.sources");
			if (sources != null && sources.length > 0) {
				for (String sc : sources) {
					channelMap.put(
							sc,
							ConfigOnZk.getInstance().getValue(
									"union-web/app_channel.properties",
									"channel." + sc));
					scBuilder.append(sc);
					scBuilder.append(",");
				}
			}
		} catch (BaseException e) {
			LOG.error(e, "get config on zk failure.",
					WebUtils.getIpAddr(request));
		}

		String fileNamePrefix = source;
		if (StringUtils.isBlank(source) && scBuilder.length() > 0) {
			scBuilder.setLength(scBuilder.length() - 1);

			source = scBuilder.toString();
			
			fileNamePrefix = "all";
		}

		if (StringUtils.isBlank(endTime)) {
			endTime = DateUtil.getCurrentDateTimeStr();
		}

		if (StringUtils.isBlank(startTime)) {
			startTime = DateUtil.getDateTimeStr(DateUtil.parseDate(DateUtil
					.getDateStr(DateUtil.timeAddByDays(
							DateUtil.parseDateTime(endTime), -7))));
		}
		
		UnionOrderService uoService = (UnionOrderService) SpringContextUtil
				.getBean("unionOrderService");
		List<UnionOrder> unionOrders = uoService.get2wCodeListBySource(source,
				startTime, endTime, ordStatus);
		
		String fileName = fileNamePrefix + "_" + startTime + "_" + "_" + endTime + "_"
				+ DateUtil.getCurrentDateTimeStr() + ".xls";
		fileName = StringUtils.replace(StringUtils.replace(fileName, ":", "-"),
				" ", "-");
		boolean result = getExcelFile(FILEPATH_STRING, fileName, unionOrders,channelMap, getTotalOrderPrice(unionOrders));
		String errorMsg = "";
		if (result) {
			try {
				PoiExcelUtil.download(request, response, "application/x-excel",
						FILEPATH_STRING + fileName, fileName, true);
				
				return null;
			} catch (Exception e) {
				LOG.error(e, "source:" + source + " startTime:" + startTime
						+ " endTime:" + endTime + " orderStatus:" + ordStatus,
						WebUtils.getClientIp(request));
				errorMsg = "下载过程出错！ <a href='javascript:history.back();'>返 回</a>";
				request.setAttribute("errorMsg", errorMsg);
				return "channel/export";
			}
		}

		errorMsg = "程序在服务器端生成excel文件失败! <a href='javascript:history.back();'>返 回</a>";
		request.setAttribute("errorMsg", errorMsg);
		return "channel/export";
	}

	/**
	 * 根据 UnionOrder列表生成excel
	 * 
	 * @param writePath
	 *            存储路径
	 * @param writeName
	 *            文件名
	 * @param unionOrders
	 *            订单列表
	 * @return
	 */
	private boolean getExcelFile(String writePath, String writeName,
			List<UnionOrder> unionOrders, Map<String, String> channelMap, String totalPriceStr) {
		if (unionOrders == null || unionOrders.size() <= 0) {
			return false;
		}

		HSSFWorkbook excelWorkbook = new HSSFWorkbook();
		String[] colums = { "序号", "渠道", "订单号", "订单价格",
				"订单状态", "下单时间", "商品ID", "商品名称", "商品单价", "商品数量"};
		HSSFSheet excelSheet = PoiExcelUtil.buildTitles(
				excelWorkbook.createSheet(writeName), colums);

		short rowNum = 1;
		int mergeRowNum = 1;
		for (UnionOrder unionOrder : unionOrders) {
			short tmpRowNum = rowNum;
			for (UnionOrder.ProductInfo productInfo : unionOrder.getProInfos()) {
				Row row = excelSheet.createRow(tmpRowNum);
				row.createCell(0);
				row.createCell(1);
				row.createCell(2);
				row.createCell(3);
				row.createCell(4);
				row.createCell(5);
				row.createCell(6).setCellValue(productInfo.getProId());
				row.createCell(7).setCellValue(productInfo.getProName());
				row.createCell(8).setCellValue(productInfo.getPrice());
				row.createCell(9).setCellValue(productInfo.getBuyNum());

				tmpRowNum++;
			}

			Row row = excelSheet.getRow(rowNum);
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,
					tmpRowNum - 1, 0, 0));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,
					tmpRowNum - 1, 1, 1));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,
					tmpRowNum - 1, 2, 2));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,
					tmpRowNum - 1, 3, 3));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,
					tmpRowNum - 1, 4, 4));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,
					tmpRowNum - 1, 5, 5));

			row.getCell(0).setCellValue(mergeRowNum++);
			row.getCell(1).setCellValue(channelMap.get(unionOrder.getSourceidId()));
			row.getCell(2).setCellValue(unionOrder.getOrderId());
			row.getCell(3).setCellValue(unionOrder.getTotalPrice());
			row.getCell(4).setCellValue(unionOrder.getOrderStringStatus());
			row.getCell(5).setCellValue(
					DateUtil.getDateTimeStr(unionOrder.getCreateTime()));

			rowNum = tmpRowNum;
		}

		try {
//			Row row = excelSheet.getRow(rowNum);
			PoiExcelUtil.createFile(writePath, writeName, excelWorkbook);
		} catch (FileNotFoundException e) {
			LOG.error(e, "writePath:" + writePath + " writeName:" + writeName,
					"");
			return false;
		} catch (IOException e) {
			LOG.error(e, "writePath:" + writePath + " writeName:" + writeName,
					"");
			return false;
		}

		return true;
	}

}
