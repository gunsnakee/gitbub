package com.meiliwan.emall.monitor.service.wms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.monitor.service.oms.NuoMi;

public class OrderExcelService {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(OrderExcelService.class);


		
	public void readExcel(String path,ExcelInterface intr) {
		try {
			readExcel(new FileInputStream(new File(path)),intr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 读取一个Excel文件
	public static OrdSheet readExcel(InputStream fis,ExcelInterface intr) throws Exception {
		OrdSheet wmsSheet = new OrdSheet();
		Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fis);
        } catch (Exception ex) {
        		ex.printStackTrace();
            try {
				workbook = new HSSFWorkbook(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
        }
		DecimalFormat df = new DecimalFormat("#.##");
		// 开始读取
		// 获得一个Sheet
		Sheet sheet = workbook.getSheetAt(0);
		if(sheet==null){
			return wmsSheet;
		}
		
		for (int rowNumOfSheet = 1; rowNumOfSheet <= sheet
				.getLastRowNum(); rowNumOfSheet++) {
			Row row = sheet.getRow(rowNumOfSheet);
			if(row==null){
				continue;
			}
			logger.debug("第" + rowNumOfSheet + "行   ");
			WMSBean ord = new WMSBean();
			WMSProduct pro = new WMSProduct();
			for (short cellNumOfRow = 0; cellNumOfRow < row
					.getLastCellNum(); cellNumOfRow++) {

				Cell cell = row.getCell(cellNumOfRow);
				int cellType=-1;
				if(cell!=null){
					cellType = cell.getCellType();
				}
				switch (cellType) {
				case HSSFCell.CELL_TYPE_NUMERIC:// Numberic
					String strCell=null;
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date d = cell.getDateCellValue();
						strCell = DateUtil.getDateTimeStr(d);
					} else {
						strCell = df.format(cell.getNumericCellValue());
					}  
					
					intr.change(cellNumOfRow,strCell,wmsSheet);
					break;
				case 1:
					strCell = cell.getRichStringCellValue()
							.getString();
					intr.change(cellNumOfRow,strCell,wmsSheet);
					break;
				default:
					intr.change(cellNumOfRow,"",wmsSheet);
				}
			}
			
		}
		intr.extra(wmsSheet);
		return wmsSheet;
	}
	
	/**
	 * tmall"/>天
taocz"/>淘
wowotuan"
yhd"/>一号
	 * @param type
	 */
	public static ExcelInterface getWMSInterface(String type){
		if(StringUtils.isBlank(type)){
			return null;
		}
		if(type.equals("tmall")){
			return new Tmall();
		}
		if(type.equals("taoczwms")){
			return new Taocz();
		}
		if(type.equals("wowotuanwms")){
			return new Wowotuan();
		}
		if(type.equals("taocz")){
			return new com.meiliwan.emall.monitor.service.oms.Taocz();
		}
		if(type.equals("wowotuan")){
			return new com.meiliwan.emall.monitor.service.oms.Wowotuan();
		}
		if(type.equals("yhd")){
			return new Yhd();
		}
		if(type.equals("jd")){
			return new JD();
		}
		if(type.equals("nuomi")){
			return new NuoMi();
		}
		return null;
	}
	
}
