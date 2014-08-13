package com.meiliwan.emall.union.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.bean.SFOrder;
import com.meiliwan.emall.union.bean.SFShop;
import com.meiliwan.emall.union.service.SFUnionOrderService;
import com.meiliwan.emall.union.service.SFUnionOrderService.FSOrderEnum;
import com.meiliwan.emall.union.util.PoiExcelReadUtil;
import com.meiliwan.emall.union.util.PoiExcelUtil;

/**
 * 顺丰订单查询控制器
 * 
 * @author yuzhe
 * 
 */
@Controller
@RequestMapping("/sf")
public class SFOrderController {
	private final static MLWLogger MLW_LOGGER = MLWLoggerFactory
			.getLogger(SFOrderController.class);
//	private final String DOMAIN_URL = "http://127.0.0.1:8087";
	private final String LOACL_SERVER_DIR = "/data/file/sf";
	private final String LOACL_SERVER_FILE_NAME = "sf_shop_info";
	private final String LOCAL_SHOP_TEMPLATE_FILE_NAME = "template.xlsx";
	private final String UPLOAD_FILE_NAME = "upfile";
	private final String ERROR_RETURN_STRING="<a href='/sf/query?source=error'>返 回</a>";

	@Autowired
	private SFUnionOrderService sfUnionOrderService;

	public void setSfUnionOrderService(SFUnionOrderService sfUnionOrderService) {
		this.sfUnionOrderService = sfUnionOrderService;
	}

	private File getExcelFile() {
		File xlsxFile = new File(LOACL_SERVER_DIR + File.separatorChar
				+ LOACL_SERVER_FILE_NAME + ".xlsx");
		File xlsFile = new File(LOACL_SERVER_DIR + File.separatorChar
				+ LOACL_SERVER_FILE_NAME + ".xls");

		if (xlsFile.isFile()) {
			return xlsFile;
		}

		if (xlsxFile.isFile()) {
			return xlsxFile;
		}

		return null;
	}

	private void deleteFile() {
		File dir = new File(LOACL_SERVER_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
			return;
		}

		File xlsxFile = new File(LOACL_SERVER_DIR + File.separatorChar
				+ LOACL_SERVER_FILE_NAME + ".xlsx");
		File xlsFile = new File(LOACL_SERVER_DIR + File.separatorChar
				+ LOACL_SERVER_FILE_NAME + ".xls");
		if (xlsxFile.exists() || !xlsxFile.isDirectory()) {
			xlsxFile.delete();
		}
		if (xlsFile.exists() || !xlsFile.isDirectory()) {
			xlsFile.delete();
		}

	}

	private String getEmailList(File file) throws IOException {
		List<String> eamiList = null;
		FileInputStream inputStream = new FileInputStream(file);

		if (StringUtils.endsWith(file.getName(), ".xlsx")) {
			XSSFSheet xssfSheet = PoiExcelReadUtil.getXssfSheet(inputStream, 0);
			int rowCount = xssfSheet.getPhysicalNumberOfRows();
			eamiList = PoiExcelReadUtil.getStringCellList(xssfSheet, 1,
					rowCount, 5);
		} else {
			HSSFSheet hssfSheet = PoiExcelReadUtil.getHssfSheet(inputStream, 0);
			int rowCount = hssfSheet.getPhysicalNumberOfRows();
			eamiList = PoiExcelReadUtil.getStringCellList(hssfSheet, 1,
					rowCount, 5);
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (String string : eamiList) {
			stringBuilder.append(killBlank(string)).append(" ");
		}
		if(stringBuilder.length() > 0){
			stringBuilder.setLength(stringBuilder.length() - 1);
		}
		return stringBuilder.toString();
	}

	private String killBlank(String line){
		return StringUtils.replace(line, " ", "");
	}
	private TreeMap<String, SFOrder> getEmailOrderMap(File file)
			throws IOException {
		TreeMap<String, SFOrder> emailOrderMap = new TreeMap<String, SFOrder>();
		FileInputStream inputStream = new FileInputStream(file);
		List<Integer> shopIds = null;
		List<String> emails = null;
		List<String> provinces = null;
		List<String> areas = null;
		List<String> shopNames = null;
		if (StringUtils.endsWith(file.getName(), ".xlsx")) {
			XSSFSheet xssfSheet = PoiExcelReadUtil.getXssfSheet(inputStream, 0);
			int rowCount = xssfSheet.getPhysicalNumberOfRows();
			shopIds = PoiExcelReadUtil.getIntegerCellList(xssfSheet, 1,
					rowCount, 0);
			emails = PoiExcelReadUtil.getStringCellList(xssfSheet, 1, rowCount,
					5);
			provinces = PoiExcelReadUtil.getStringCellList(xssfSheet, 1, rowCount, 1);
			areas = PoiExcelReadUtil.getStringCellList(xssfSheet, 1, rowCount, 2);
			shopNames = PoiExcelReadUtil.getStringCellList(xssfSheet, 1, rowCount, 3);
		} else {
			HSSFSheet hssfSheet = PoiExcelReadUtil.getHssfSheet(inputStream, 0);
			int rowCount = hssfSheet.getPhysicalNumberOfRows();
			shopIds = PoiExcelReadUtil.getIntegerCellList(hssfSheet, 1,
					rowCount, 0);
			emails = PoiExcelReadUtil.getStringCellList(hssfSheet, rowCount,
					rowCount, 5);
			provinces = PoiExcelReadUtil.getStringCellList(hssfSheet, 1, rowCount, 1);
			areas = PoiExcelReadUtil.getStringCellList(hssfSheet, 1, rowCount, 2);
			shopNames = PoiExcelReadUtil.getStringCellList(hssfSheet, 1, rowCount, 3);
		}

		for (int i = 0; i < shopIds.size(); i++) {
			String email = killBlank(emails.get(i));
			SFOrder order = new SFOrder();
			order.setShopId(shopIds.get(i));
			order.setProvince(provinces.get(i));
			order.setArea(areas.get(i));
			order.setShopName(shopNames.get(i));
			order.setShopEmail(email);
			
			emailOrderMap.put(email, order);
			
		}

		return emailOrderMap;
	}

	private List<Row> getRowList(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);

		if (StringUtils.endsWith(file.getName(), ".xlsx")) {
			XSSFSheet xssfSheet = PoiExcelReadUtil.getXssfSheet(
					fileInputStream, 0);
			int rowCount = xssfSheet.getPhysicalNumberOfRows();
			return PoiExcelReadUtil.getXSSFRowList(xssfSheet, 1, rowCount);
		}

		HSSFSheet hssfSheet = PoiExcelReadUtil.getHssfSheet(fileInputStream, 0);
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		return PoiExcelReadUtil.getHSSFRowList(hssfSheet, 1, rowCount);
	}

	private List<SFShop> getSHShopByEmailList(List<String> emaiList, File file)
			throws IOException {
		TreeSet<String> emaillSet = new TreeSet<String>();
		List<SFShop> sfShops = new ArrayList<SFShop>();

		for (String string : emaiList) {
			emaillSet.add(string);
		}
		List<Row> rowList = getRowList(file);

		for (Row row : rowList) {
			String shopEmail = null;
			Cell cell = row.getCell(5);
			if(cell.getCellType() == Cell.CELL_TYPE_STRING){
				shopEmail = cell.getStringCellValue();
			}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				double val = cell.getNumericCellValue();
				shopEmail = String.valueOf(val);
			}
			if (emaillSet.contains(shopEmail)) {
				SFShop sfShop = new SFShop();
				sfShop.setShopId(String.valueOf((int)row.getCell(0).getNumericCellValue()));
				sfShop.setProvinces(row.getCell(1).getStringCellValue());
				sfShop.setArea(row.getCell(2).getStringCellValue());
				sfShop.setShopName(row.getCell(3).getStringCellValue());
				sfShop.setShopCode(row.getCell(4).getStringCellValue());
				sfShop.setEmail(shopEmail);

				sfShops.add(sfShop);
			}
		}
		return sfShops;
	}

	private FSOrderEnum getOrderEnum(Integer integer) {
		if (integer.intValue() == FSOrderEnum.ORDI_RECEIPTED.getValue()) {
			return FSOrderEnum.ORDI_RECEIPTED;
		} else if (FSOrderEnum.ORDI_FINISHED.getValue() == integer.intValue()) {
			return FSOrderEnum.ORDI_FINISHED;
		} else if (FSOrderEnum.ORDI_EFFECTIVED.getValue() == integer.intValue()) {
			return FSOrderEnum.ORDI_EFFECTIVED;
		} else if (FSOrderEnum.ORDI_CONSINGMENT.getValue() == integer
				.intValue()) {
			return FSOrderEnum.ORDI_CONSINGMENT;
		} else {
			return FSOrderEnum.ORDI_ALL;
		}
	}

	@SuppressWarnings("unused")
	private String[] getTitle(File file) throws IOException {
		FileInputStream inputStream = new FileInputStream(file);
		if (StringUtils.endsWith(file.getName(), ".xlsx")) {
			return PoiExcelReadUtil.getXSSFSheetTilte(PoiExcelReadUtil
					.getXssfSheet(inputStream, 0));
		}

		return PoiExcelReadUtil.getHSSFSheetTilte(PoiExcelReadUtil
				.getHssfSheet(inputStream, 0));
	}

	private boolean CreateExcelFile(String fileName, String path,
			List<SFOrder> sfOrders, String sheetName) {
		if (sfOrders == null || sfOrders.size() == 0) {
			return false;
		}

		HSSFWorkbook excelWorkbook = new HSSFWorkbook();
		String[] columns = { "门店序号", "省份", "区域", "门店名称", "门店帐号", "订单号", "下单时间", "付款时间", "订单状态",
				"订单价格","订单价格(不含邮费)", "商品ID", "商品名称", "商品单价" , "商品数量"};
		HSSFSheet excelSheet = PoiExcelUtil.buildTitles(
				excelWorkbook.createSheet(sheetName), columns);

		short rowNum = 1;
		for (SFOrder sfOrder : sfOrders) {
			short tmpRowNum = rowNum;
			for (SFOrder.Goods goods : sfOrder.getOrderGoodsItems()) {
				Row row = excelSheet.createRow(tmpRowNum);
				row.createCell(0);
				row.createCell(1);
				row.createCell(2);
				row.createCell(3);
				row.createCell(4);
				row.createCell(5);
				row.createCell(6);
				row.createCell(7);
				row.createCell(8);
				row.createCell(9);
				row.createCell(10);
				

				row.createCell(11).setCellValue(goods.getGoodsId());
				row.createCell(12).setCellValue(goods.getGoodsName());
				row.createCell(13).setCellValue(goods.getGoodsPrice());
				row.createCell(14).setCellValue(goods.getSaleNum());

				tmpRowNum++;
			}
			
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,0,0));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,1,1));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,2,2));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,3,3));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,4,4));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,5,5));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,6,6));
			excelSheet.addMergedRegion(new CellRangeAddress(rowNum,tmpRowNum-1,7,7));

			Row row = excelSheet.getRow(rowNum);
			row.getCell(0).setCellValue(sfOrder.getShopId());
			row.getCell(1).setCellValue(sfOrder.getProvince()); 
			row.getCell(2).setCellValue(sfOrder.getArea()); 
			row.getCell(3).setCellValue(sfOrder.getShopName()); 
			row.getCell(4).setCellValue(sfOrder.getShopEmail());
			row.getCell(5).setCellValue(sfOrder.getOrderId());
			row.getCell(6).setCellValue(DateUtil.getDateTimeStr(sfOrder.getCreateTime()));
			String payTime = "";
			if(sfOrder.getPayTime() != null){
				payTime = DateUtil.getDateTimeStr(sfOrder.getPayTime());
			}
			row.getCell(7).setCellValue(payTime);
			row.getCell(8).setCellValue(sfOrder.getOrderStatus());
			String totalPrice = "";
			if(sfOrder.getOrderTotalPrice() != null){
				totalPrice = NumberUtil.format(sfOrder.getOrderTotalPrice());
			}
			row.getCell(9).setCellValue(totalPrice);
			row.getCell(10).setCellValue(sfOrder.getOrderRealPay());

			rowNum = tmpRowNum;
		}

		try {
			PoiExcelUtil.createFile(path, fileName, excelWorkbook);
		} catch (FileNotFoundException e) {
			MLW_LOGGER.error(e, "writePath:" + path + " writeName:" + fileName,
					"");
			return false;
		} catch (IOException e) {
			MLW_LOGGER.error(e, "writePath:" + path + " writeName:" + fileName,
					"");
			return false;
		}
		return true;
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String username = ServletRequestUtils.getStringParameter(request,
				"username", "");
		String password = ServletRequestUtils.getStringParameter(request,
				"password", "");
		if (StringUtils.isBlank(username) && StringUtils.isBlank(password)) {
			return "/sf/login";
		}

		if (!sfUnionOrderService.UserCheck(username, password)) {
			model.addAttribute("ERROR_MSG", "无效的用户名和密码");
			return "/sf/error";
		}

		try {
			response.sendRedirect("/sf/query?source=login");
			return null;
		} catch (IOException e) {
			MLW_LOGGER.error(e, " failed to edirect to " + 
					 "/sf/query", WebUtils.getClientIp(request));
		}
		model.addAttribute("ERROR_MSG", "failed to edirect to "
				+ "/sf/query");
		return "/sf/error";
	}

	@RequestMapping("/download")
	public String DowloadShopTemplate(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		File file = new File(LOACL_SERVER_DIR + File.separatorChar
				+ LOCAL_SHOP_TEMPLATE_FILE_NAME);

		if (!file.isFile()) {
			model.addAttribute("ERROR_MSG", "文件模板不存在");
			return "/sf/error";
		}

		try {
			PoiExcelUtil.download(request, response, "application/x-excel",
					LOACL_SERVER_DIR + File.separatorChar
							+ LOCAL_SHOP_TEMPLATE_FILE_NAME,
					LOCAL_SHOP_TEMPLATE_FILE_NAME,false);
		} catch (Exception e) {
			MLW_LOGGER.error(e, "file path:" + LOACL_SERVER_DIR
					+ File.separatorChar + LOCAL_SHOP_TEMPLATE_FILE_NAME,
					WebUtils.getClientIp(request));
			request.setAttribute("errorMsg", "下载过程出错！");
			return "/sf/error";
		}
		return null;
	}

	@RequestMapping("/uploadShopInfo")
	public String uploadExcelFile(MultipartHttpServletRequest request,
			HttpServletResponse response, Model model) {
		MultipartFile multipart = request.getFile(UPLOAD_FILE_NAME);
		if (multipart == null) {
			String error_msg = "请选定要上传的excel文件名字！！";
			model.addAttribute("ERROR_MSG", error_msg);
			model.addAttribute("back_html", ERROR_RETURN_STRING);
			return "/sf/error";
		}

		deleteFile();
		try {
			byte[] fileByes = IOUtils.toByteArray(multipart.getInputStream());
			FileOutputStream outputStream = null;
			if (StringUtils.endsWith(multipart.getOriginalFilename(), ".xlsx")) {
				outputStream = new FileOutputStream(LOACL_SERVER_DIR
						+ File.separatorChar + LOACL_SERVER_FILE_NAME + ".xlsx");
			} else {
				outputStream = new FileOutputStream(LOACL_SERVER_DIR
						+ File.separatorChar + LOACL_SERVER_FILE_NAME + ".xls");
			}
			outputStream.write(fileByes);
			outputStream.close();
			MLW_LOGGER.info("upload file success",
					"file name:" + multipart.getOriginalFilename(),
					WebUtils.getClientIp(request));
			response.sendRedirect("/sf/query?source=uploadShopInfo");
		} catch (IOException e) {
			MLW_LOGGER.error(e,
					" fail to upload file:" + multipart.getContentType(),
					WebUtils.getClientIp(request));
		}

		return null;
	}

	@RequestMapping("/query")
	public String queryOrder(HttpServletRequest request,
			HttpServletResponse reqHttpServletResponse, Model model) {
		String startTime = ServletRequestUtils.getStringParameter(request,
				"startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request,
				"endTime", "");
		String orderStatus = request.getParameter("orderStatus");
		if (StringUtils.isBlank(orderStatus)) {
			orderStatus = "-1";
		}

		if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
			endTime = DateUtil.getCurrentDateTimeStr();
			startTime = DateUtil.getDateTimeStr(DateUtil.parseDate(DateUtil
					.getDateStr(DateUtil.timeAddByDays(
							DateUtil.parseDateTime(endTime), -7))));
		}
		
		String source = request.getParameter("source");
		if ("login".equalsIgnoreCase(source) || "error".equalsIgnoreCase(source) ||
				"uploadShopInfo".equalsIgnoreCase(source)) {
			return "/sf/query";
		}

		File excelFile = getExcelFile();
		if (excelFile == null || !excelFile.isFile()) {
			model.addAttribute("ERROR_MSG", "请先上传excel模板文件");
			model.addAttribute("back_html", "<a href='/sf/query?source=error'>返 回</a>");
			return "/sf/error";
		}

		if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
			model.addAttribute("ERROR_MSG", "提交参数不合法");
			model.addAttribute("back_html", ERROR_RETURN_STRING);
			return "/sf/error";
		}
		List<String> invalidEmail = new ArrayList<String>();
		String emailList = "";
		try {
			emailList = getEmailList(excelFile);
		} catch (IOException e) {
			MLW_LOGGER.error(e, "fail to extract email list for file:"
					+ excelFile.getName(), WebUtils.getClientIp(request));
		}
		
		invalidEmail = sfUnionOrderService.verifyEmailUser(emailList);
		try {
			if (invalidEmail.size() > 0) {
				List<SFShop> sfShops = getSHShopByEmailList(invalidEmail,
						excelFile);
//				String error_msg="您所上传的店面信息内，有以下帐号有错误，无法查询店面信息的销售情况，请重新确认您所填的帐号的有效性，您可以修改帐号信息然后重新上传，继续操作";
				model.addAttribute("sfShops", sfShops);
			}
		} catch (IOException e) {
			MLW_LOGGER.error(e,
					"handle for invalid sf email error ,eamilList: "
							+ emailList, WebUtils.getClientIp(request));
		}

		try {
			Map<String, SFOrder> sfShop = getEmailOrderMap(excelFile);
			List<SFOrder> sfOrders = sfUnionOrderService.getSFOrderList(sfShop,
					DateUtil.parseDate(startTime), DateUtil.parseDate(endTime),
					getOrderEnum(Integer.valueOf(orderStatus)));
			model.addAttribute("sfOrders", sfOrders);
			model.addAttribute("startTime", DateUtil.parseDateTime(startTime));
			model.addAttribute("endTime", DateUtil.parseDateTime(endTime));
			model.addAttribute("orderStatus", orderStatus);
		} catch (IOException e) {
			MLW_LOGGER.error(e, "fail to get sf order",
					WebUtils.getClientIp(request));
		}

		return "/sf/query";
	}

	@RequestMapping("/exportFile")
	public String exportOrder(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String startTime = ServletRequestUtils.getStringParameter(request,
				"startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request,
				"endTime", "");
		String orderStatus = request.getParameter("orderStatus");
		if (StringUtils.isBlank(orderStatus)) {
			orderStatus = "-1";
		}

		File excelFile = getExcelFile();
		if (excelFile == null || !excelFile.isFile()) {
			model.addAttribute("ERROR_MSG", "请先上传excel模板文件");
			model.addAttribute("back_html", ERROR_RETURN_STRING);
			return "/sf/error";
		}

		if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)
				) {
			model.addAttribute("ERROR_MSG", "提交参数不合法");
			model.addAttribute("back_html", ERROR_RETURN_STRING);
			return "/sf/error";
		}
//		List<String> invalidEmail = new ArrayList<String>();
//		String emailList = "";
//		try {
//			emailList = getEmailList(excelFile);
//		} catch (IOException e) {
//			MLW_LOGGER.error(e, "fail to extract email list for file:"
//					+ excelFile.getName(), WebUtils.getClientIp(request));
//		}

//		try {
//			invalidEmail = sfUnionOrderService.verifyEmailUser(emailList);
//			if (invalidEmail.size() > 0) {
//				List<SFShop> sfShops = getSHShopByEmailList(invalidEmail,
//						excelFile);
//				String error_msg="您所上传的店面信息内，一下帐号有错误，无法查询店面信息的销售情况，请重新确认您所填的帐号的有效性，您可以修改帐号信息然后重新上传，继续操作";
//				model.addAttribute("sfShops", sfShops);
//				model.addAttribute("ERROR_MSG", error_msg);
//				model.addAttribute("back_html", ERROR_RETURN_STRING);
//				return "/sf/error";
//			}
//		} catch (IOException e) {
//			MLW_LOGGER.error(e,
//					"handle for invalid sf email error ,eamilList: "
//							+ emailList, WebUtils.getClientIp(request));
//		}

		List<SFOrder> sfOrders = new ArrayList<SFOrder>();
		Map<String, SFOrder> sfShop = new HashMap<String, SFOrder>();
		try {
			sfShop = getEmailOrderMap(excelFile);
			sfOrders = sfUnionOrderService.getSFOrderList(sfShop,
					DateUtil.parseDate(startTime), DateUtil.parseDate(endTime),
					getOrderEnum(Integer.valueOf(orderStatus)));
			
			if (sfOrders.size() == 0 ) {
				model.addAttribute("ERROR_MSG","没有订单记录");
				model.addAttribute("back_html", ERROR_RETURN_STRING);
				return "/sf/error";
			}
		} catch (IOException e) {
			MLW_LOGGER.error(e, "fail to get sf order",
					WebUtils.getClientIp(request));
		}

		String time = StringUtils.remove(
				StringUtils.remove(StringUtils.remove(
						DateUtil.getCurrentDateTimeStr(), " "), ":"), "-");
		String fileName = "sf-order-" + time + ".xls";
		if (!CreateExcelFile(fileName, LOACL_SERVER_DIR+File.separatorChar, sfOrders, "sf order")) {
			model.addAttribute("ERROR_MSG", "服务端生成execl失败");
			return "/sf/error";
		}

		try {
			PoiExcelUtil.download(request, response, "application/x-excel",
					LOACL_SERVER_DIR + File.separatorChar + fileName, fileName,true);
		} catch (Exception e) {
			MLW_LOGGER.error(e, "download error ,full path:" + LOACL_SERVER_DIR
					+ File.separatorChar + fileName,
					WebUtils.getClientIp(request));
			String errorMsg = "下载过程出错！";
			model.addAttribute("errorMsg", errorMsg);
			model.addAttribute("back_html", ERROR_RETURN_STRING);
			return "/sf/error";
		}
		return null;
	}
}
