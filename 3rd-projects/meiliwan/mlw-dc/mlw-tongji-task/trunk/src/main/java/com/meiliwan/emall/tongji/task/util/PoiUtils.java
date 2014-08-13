package com.meiliwan.emall.tongji.task.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 13-11-19.
 */
public class PoiUtils {

    /**
     * 构建标题
     *
     * @param sheet
     * @param titles
     */
    public static void buildTitles(HSSFSheet sheet, String[] titles) {
        int i = 0;
        // 创建一行
        HSSFRow row = sheet.createRow((short) 0);
        // 填充标题
        for (String s : titles) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(s);
            i++;
        }
    }

    /**
     * 去单元格内容 null 转空串处理
     *
     * @param rowdata
     * @param startIndex
     * @param endIndex
     */
    public static void dealWithCellsNullString(HSSFRow rowdata, int startIndex, int endIndex) {
        if (rowdata != null && startIndex < endIndex) {
            for (int i = 0; i < endIndex - startIndex; i++) {
                HSSFCell cell = rowdata.getCell(startIndex + i);
                //只处理cell 类型为  CELL_TYPE_STRING 的
                if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String cellValue = cell.getStringCellValue();
                    if (StringUtils.isNotBlank(cellValue) && cellValue.trim().equals("null")) {
                        rowdata.getCell(startIndex + i).setCellValue("");
                    }
                }
            }
        }
    }

    /**
     * 在服务器端指定路径 写excel文件
     *
     * @param writePath
     * @param writeName
     * @param wb
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void createFile(String writePath, String writeName, HSSFWorkbook wb) throws FileNotFoundException, IOException {
        File file = new File(writePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = writePath + writeName;
        FileOutputStream fileOut = new FileOutputStream(path);
        wb.write(fileOut);
        fileOut.close();
    }
}
