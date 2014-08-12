package org.jiawu.gitbub.core.excel;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiawuwu on 14-4-10.
 */
public class ReadExcelTool{


   private static  List<Map<Integer,String>> readSheetData(Workbook book,int index){
       List<Map<Integer,String>> sheetData = new ArrayList<Map<Integer, String>>();
       if(index>book.getNumberOfSheets()){
           return null;
       }
       Sheet sheet = book.getSheet(index);
       int rows_length = sheet.getRows();
       for(int i=0;i<rows_length;i++){
           Map<Integer,String> rowData = new HashMap<Integer, String>();
           Cell[] cells = sheet.getRow(i);
           int cells_length = cells.length;
           for(int j=0;j<cells_length;j++){
               rowData.put(j,cells[j].getContents());
           }
           sheetData.add(rowData);
       }
       return sheetData;
   }



   public static List<List<Map<Integer,String>>> readAll(String path){
       List<List<Map<Integer,String>>> data = new ArrayList<List<Map<Integer,String>>>();
       Workbook book = null;
       try {
           book = Workbook.getWorkbook(new File(path));
           int sheet_length = book.getNumberOfSheets();
           for(int i=0;i<sheet_length;i++){
               data.add(readSheetData(book,i));
           }
       } catch (IOException e) {
           e.printStackTrace();
       } catch (BiffException e) {
           e.printStackTrace();
       }finally {
           if(book!=null){
               book.close();
           }
       }
       return data;
   }

   public static List<Map<Integer,String>> readOneSheet(String path,int index){
       Workbook book = null;
       try {
           book = Workbook.getWorkbook(new File(path));
           return readSheetData(book,index);
       } catch (IOException e) {
           e.printStackTrace();
       } catch (BiffException e) {
           e.printStackTrace();
       }finally {
           if(book!=null){
               book.close();
           }
       }
       return null;
   }




}
