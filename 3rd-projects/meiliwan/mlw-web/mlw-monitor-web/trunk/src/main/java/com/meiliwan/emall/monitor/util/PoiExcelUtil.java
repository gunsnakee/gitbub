package com.meiliwan.emall.monitor.util;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-10-31
 * Time: 上午10:48
 * POI excel 工具类
 * To change this template use File | Settings | File Templates.
 */
public class PoiExcelUtil {
    private static final MLWLogger logger = MLWLoggerFactory
            .getLogger(PoiExcelUtil.class);
    /**
     * 构建标题
     * @param sheet
     * @param titles
     */
    public static HSSFSheet buildTitles(HSSFSheet sheet,String[] titles){
        int i = 0;
        // 创建一行
        HSSFRow row = sheet.createRow((short) 0);
        // 填充标题
        for (String s : titles) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(s);
            i++;
        }
        return sheet;
    }

    /**
     *
     *  在服务器端指定路径 写excel文件
     *
     * @param writePath
     * @param writeName
     * @param wb
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void createFile(String writePath,String writeName, HSSFWorkbook wb) throws FileNotFoundException ,IOException{
        File file = new File(writePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = writePath + writeName;
        FileOutputStream fileOut = new FileOutputStream(path);
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     *
     * 在服务器端下载指定文件，完成后删除该文件
     *
     * @param request
     * @param response
     * @param contentType
     * @param path
     * @param fileName
     * @throws Exception
     */
    public static boolean download(HttpServletRequest request,
                                   HttpServletResponse response,String contentType,
                                   String path,String fileName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        boolean success = false;
        try{
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            File file = new File(path);
            if (file.isFile() && file.exists()) {
                long fileLength = file.length();
                response.setContentType(contentType);
                response.setHeader("Content-disposition", "attachment; filename="
                        + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
                response.setHeader("Content-Length", String.valueOf(fileLength));
                bis = new BufferedInputStream(new FileInputStream(path));
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
                bis.close();
                bos.close();
                file.delete();
                success =  true;
            }
        } catch(Exception e){
            Map map = new HashMap();
            map.put("reason"," 文件下载出错！");
            map.put("fileName",fileName);
            map.put("path",path);
            map.put("contentType",contentType);
            logger.error(e,map, WebUtils.getIpAddr(request));
        }
        return  success;
    }

    /**
     * 去单元格内容 null 转空串处理
     * @param rowdata
     * @param startIndex
     * @param endIndex
     */
    public static void dealWithCellsNullString( HSSFRow rowdata,int startIndex, int endIndex){
        if(rowdata!=null && startIndex<endIndex){
            for(int i=0; i<endIndex-startIndex; i++){
                HSSFCell cell = rowdata.getCell(startIndex + i);
                //只处理cell 类型为  CELL_TYPE_STRING 的
                if(cell!=null && cell.getCellType()==Cell.CELL_TYPE_STRING)  {
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue) && cellValue.trim().equals("null")){
                        rowdata.getCell(startIndex + i).setCellValue("");
                    }
                }
            }
        }


    }
}
