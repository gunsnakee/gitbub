package com.meiliwan.emall.bkstage.web.controller.giftcard.template;

import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.mms.bean.UserPassportSimple;
import com.meiliwan.emall.mms.client.UserPassportClient;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出excel 父类
 * User: wenlepeng
 * Date: 13-11-26
 * Time: 下午6:32
 * To change this template use File | Settings | File Templates.
 */
public abstract class Export<T> {
    protected PageInfo pageInfo;
    protected int limitNum = 1000;
    protected int step = 100;
    protected HSSFWorkbook wb;
    protected int rowNum = 1;
    protected HSSFSheet sheet;
    protected String fileName;
    protected String exportPath = "/data/www/wallet/";
    private String filePath;
    private int totalNum = 0;


    public Export(String sheetName,String title[],String fileName){
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(sheetName);
        PoiExcelUtil.buildTitles(sheet, title);
        pageInfo = new PageInfo(1,limitNum);
        this.fileName = fileName;
    }

    public void export(String startDate,String endDate) throws IOException, LimitException {
       List<T> dataList = getDataFromDB(startDate,endDate);
       while(dataList != null && dataList.size() > 0){
           totalNum += dataList.size();
           if(totalNum > 50000) throw new LimitException("导出的总数据量超过 5 万条");
           int loopTimes = dataList.size() > limitNum ?limitNum:dataList.size();
           int startIndex = 0;
           int endIndex = 0;
           for(int i =0;i<loopTimes;i=i+step){
               //获取要查询的用户id 数组
               endIndex += dataList.size()>step?step:dataList.size();
               if(endIndex > dataList.size()){
                   endIndex = dataList.size();
               }
               List<T> subList =  dataList.subList(startIndex, endIndex);
               Integer [] ids = getUids(subList);

               //获取用户信息
               if(ids != null){
                   Map<Integer,UserPassportSimple> map = UserPassportClient.getPassportSimpleMapByUids(ids);
                   fillRowData(subList,map);
               }

               startIndex = endIndex;
           }

           int currentPageNum = pageInfo.getPage()+1;
           if(currentPageNum <= pageInfo.getAllPage()){
               pageInfo.setPage(currentPageNum);
               dataList = getDataFromDB(startDate,endDate);
           }else{
               dataList = null;
           }
       }


        File file = new File(exportPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String exportName = new Date().getTime() + "";
        filePath= exportPath + exportName + getFileName();
        FileOutputStream fileOut = new FileOutputStream(filePath);
        wb.write(fileOut);
        fileOut.close();
    }

    public abstract void fillRowData(List<T> sublist,Map<Integer,UserPassportSimple> map);
    public abstract List<T> getDataFromDB(String startDate,String endDate);
    public abstract Integer[] getUids(List<T> list);
    public String getFileName(){
        return fileName;
    }
    public String getFilePath(){
        return filePath;
    }
}
