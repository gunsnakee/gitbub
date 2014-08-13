package com.meiliwan.emall.union.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;    
import org.apache.poi.xssf.usermodel.XSSFSheet;    
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

/**
 * 读取excel 操作
 * HSSF API 合适2003版及以前版本
 * XSSF适用于 2007及以前版本
 * @author yuzhe
 *
 */
public class PoiExcelReadUtil {
	private final static MLWLogger MLW_LOGGER = MLWLoggerFactory.getLogger(PoiExcelReadUtil.class);
	
	/**
	 * get workbook  2003 版本  *xls 文件
	 * @param excelStream
	 * @return
	 * @throws java.io.IOException
	 */
	public static XSSFWorkbook getXssfWorkbook(InputStream excelStream) throws IOException{
		if (excelStream == null) {
			MLW_LOGGER.info("inputStream cannot be null,return a new HSSFWorkbook object", excelStream, "");
			return new XSSFWorkbook();
		}
		 return new XSSFWorkbook(excelStream);
	}
	
	/**
	 * get workbook  2007 版本  *xlsx 文件
	 * @param inputStream
	 * @return
	 * @throws java.io.IOException
	 */
	public static HSSFWorkbook getHssfWorkbook(InputStream inputStream) throws IOException{
		if (inputStream == null) {
			MLW_LOGGER.info("inputStream cannot be null,return a new HSSFWorkbook object", inputStream, "");
			return new HSSFWorkbook();
		}
		
		return new HSSFWorkbook(inputStream);
	}
	
	
	/**
	 * get sheet 2007
	 * @param inputStream
	 * @param sheetIndex
	 * @return
	 * @throws java.io.IOException
	 */
	public static XSSFSheet getXssfSheet(InputStream inputStream,int sheetIndex) throws IOException{
		if (inputStream == null || sheetIndex < 0) {
			MLW_LOGGER.info("input param error,return null object", "inputStream:" +inputStream +" sheepIndex:"+sheetIndex, "");
			return null;
		}
		
		XSSFWorkbook hssfWorkbook = getXssfWorkbook(inputStream);
		return hssfWorkbook.getSheetAt(sheetIndex);
	}
	
	
	/**
	 * get sheet  2003  *xls
	 * @param inputStream
	 * @param sheetIndex
	 * @return
	 * @throws java.io.IOException
	 */
	public static HSSFSheet getHssfSheet(InputStream inputStream,int sheetIndex) throws IOException{
		if (inputStream == null || sheetIndex < 0) {
			MLW_LOGGER.info("input param error,return null object", "inputStream:" +inputStream +" sheepIndex:"+sheetIndex, "");
			return null;
		}
		
		HSSFWorkbook hssfWorkbook = getHssfWorkbook(inputStream);
		return hssfWorkbook.getSheetAt(sheetIndex);
	}
	/**
	 * get sheet title
	 * @param hssfSheet
	 * @return
	 */
	public static String[] getXSSFSheetTilte(XSSFSheet hssfSheet){
		if (hssfSheet == null || hssfSheet.getPhysicalNumberOfRows() <=0) {
			MLW_LOGGER.info("HSSFSheet cannot be null or HSSFSheet row size cannot be 0",hssfSheet, "");
			return null;
		}
		Row row = hssfSheet.getRow(0);
		int columnsCount = row.getPhysicalNumberOfCells();
		String[] title = new String[columnsCount];
		for (int i = 0; i < title.length; i++) {
			title[i] = row.getCell(i).getStringCellValue();
		}
		
		return title;
	}
	/**
	 * get sheet title
	 * @param hssfSheet
	 * @return
	 */
	public static String[] getHSSFSheetTilte(HSSFSheet hssfSheet){
		if (hssfSheet == null || hssfSheet.getPhysicalNumberOfRows() <=0) {
			MLW_LOGGER.info("HSSFSheet cannot be null or HSSFSheet row size cannot be 0",hssfSheet, "");
			return null;
		}
		Row row = hssfSheet.getRow(0);
		int columnsCount = row.getPhysicalNumberOfCells();
		String[] title = new String[columnsCount];
		for (int i = 0; i < title.length; i++) {
			title[i] = row.getCell(i).getStringCellValue();
		}
		
		return title;
	}

	/**
	 * get range row
	 * @param hssfSheet
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Row> getHSSFRowList(HSSFSheet hssfSheet,int start, int end){
		if (hssfSheet == null || start >end || start <=0 ||hssfSheet.getPhysicalNumberOfRows() < end) {
			MLW_LOGGER.info("input param error", "hssfSheet:"+hssfSheet+ " start:"+start+" end:"+end, "");
			return new ArrayList<Row>();
		}
		
		List<Row> rowList = new ArrayList<Row>();
		
		for (int i = start; i < end; i++) {
			Row row = hssfSheet.getRow(i);
			if (row != null) {
				rowList.add(row);
			}
		}
		
		return rowList;
	}
	
	
	
	/**
	 * get range row
	 * @param hssfSheet
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Row> getXSSFRowList(XSSFSheet hssfSheet,int start, int end){
		if (hssfSheet == null || start >end || start <=0 ||hssfSheet.getPhysicalNumberOfRows() < end) {
			MLW_LOGGER.info("input param error", "hssfSheet:"+hssfSheet+ " start:"+start+" end:"+end, "");
			return new ArrayList<Row>();
		}
		
		List<Row> rowList = new ArrayList<Row>();
		
		for (int i = start; i < end; i++) {
			Row row = hssfSheet.getRow(i);
			if (row!=null) {
				rowList.add(row);
			}
		}
		
		return rowList;
	}
	
	/**
	 * get cell list
	 * @param hssfSheet
	 * @param rowStart
	 * @param rowEnd
	 * @param columnsIndex
	 * @return
	 */
	public static List<String> getStringCellList(XSSFSheet hssfSheet,int rowStart,int rowEnd,int columnsIndex){
		List<Row> rowList = getXSSFRowList(hssfSheet, rowStart, rowEnd);
		if (rowList.size() == 0 || rowList.get(0).getPhysicalNumberOfCells() <= columnsIndex) {
			MLW_LOGGER.info("input param error", "hssfSheet:"+hssfSheet+" rowStart:"+rowStart + "rowEnd:"+rowEnd +" columnsIndex:"+columnsIndex, "");
			return new ArrayList<String>();
		}
		
		List<String> cellList = new ArrayList<String>();
		
		for (int i = 0; i < rowList.size(); i++) {
			Cell cell = rowList.get(i).getCell(columnsIndex);
			if(cell.getCellType() == Cell.CELL_TYPE_STRING){
				cellList.add(cell.getStringCellValue());
			}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				double val = cell.getNumericCellValue();
				cellList.add(String.valueOf(val));
			}
		}
		return cellList;
	}
	
	public static List<Integer> getIntegerCellList(XSSFSheet hssfSheet,int rowStart,int rowEnd,int columnsIndex){
		List<Row> rowList = getXSSFRowList(hssfSheet, rowStart, rowEnd);
		if (rowList.size() == 0 || rowList.get(0).getPhysicalNumberOfCells() <= columnsIndex) {
			MLW_LOGGER.info("input param error", "hssfSheet:"+hssfSheet+" rowStart:"+rowStart + "rowEnd:"+rowEnd +" columnsIndex:"+columnsIndex, "");
			return new ArrayList<Integer>();
		}
		
		List<Integer> cellList = new ArrayList<Integer>();
		
		for (int i = 0; i < rowList.size(); i++) {
			Cell cell = rowList.get(i).getCell(columnsIndex);
			if(cell.getCellType() == Cell.CELL_TYPE_STRING){
				String val = cell.getStringCellValue();
				cellList.add(Integer.valueOf(val));
			}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				cellList.add((int)cell.getNumericCellValue());
			}
//			cellList.add((int)rowList.get(i).getCell(columnsIndex).getNumericCellValue());
		}
		return cellList;
	}
	
	/**
	 * get cell list
	 * @param hssfSheet
	 * @param rowStart
	 * @param rowEnd
	 * @param columnsIndex
	 * @return
	 */
	public static List<Integer> getIntegerCellList(HSSFSheet hssfSheet,int rowStart,int rowEnd,int columnsIndex){
		List<Row> rowList = getHSSFRowList(hssfSheet, rowStart, rowEnd);
		if (rowList.size() == 0 || rowList.get(0).getPhysicalNumberOfCells() <= columnsIndex) {
			MLW_LOGGER.info("input param error", "hssfSheet:"+hssfSheet+" rowStart:"+rowStart + "rowEnd:"+rowEnd +" columnsIndex:"+columnsIndex, "");
			return new ArrayList<Integer>();
		}
		
		List<Integer> cellList = new ArrayList<Integer>();
		
		for (int i = 0; i < rowList.size(); i++) {
//			cellList.add((int)rowList.get(i).getCell(columnsIndex).getNumericCellValue());
			Cell cell = rowList.get(i).getCell(columnsIndex);
			if(cell.getCellType() == Cell.CELL_TYPE_STRING){
				String val = cell.getStringCellValue();
				cellList.add(Integer.valueOf(val));
			}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				cellList.add((int)cell.getNumericCellValue());
			}
		}
		return cellList;
	}

	/**
	 * get cell list
	 * @param hssfSheet
	 * @param rowStart
	 * @param rowEnd
	 * @param columnsIndex
	 * @return
	 */
	public static List<String> getStringCellList(HSSFSheet hssfSheet,int rowStart,int rowEnd,int columnsIndex){
		List<Row> rowList = getHSSFRowList(hssfSheet, rowStart, rowEnd);
		if (rowList.size() == 0 || rowList.get(0).getPhysicalNumberOfCells() <= columnsIndex) {
			MLW_LOGGER.info("input param error", "hssfSheet:"+hssfSheet+" rowStart:"+rowStart + "rowEnd:"+rowEnd +" columnsIndex:"+columnsIndex, "");
			return new ArrayList<String>();
		}
		
		List<String> cellList = new ArrayList<String>();
		
		for (int i = 0; i < rowList.size(); i++) {
//			cellList.add(rowList.get(i).getCell(columnsIndex).getStringCellValue());
			Cell cell = rowList.get(i).getCell(columnsIndex);
			if(cell.getCellType() == Cell.CELL_TYPE_STRING){
				cellList.add(cell.getStringCellValue());
			}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				double val = cell.getNumericCellValue();
				cellList.add(String.valueOf(val));
			}
		}
		return cellList;
	}
}
