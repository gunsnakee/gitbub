package org.jiawu.main;

import org.apache.commons.lang.StringUtils;
import org.jiawu.excel.ReadExcelTool;

import java.util.List;
import java.util.Map;

/**
 * Created by jiawuwu on 14-7-25.
 */
public class CategoryTrModifyTh {

    public static void main(String[] args){
        List<Map<Integer,String>> sheet = ReadExcelTool.readOneSheet("/data/mygit/gitbub/src/main/resources/b2b类目翻译_泰语版本.xls", 0);
        int initCategoryId = 2009469;
        int idx=0;
        for(int i=3,len=sheet.size();i<len;i++){
            Map<Integer, String> row = sheet.get(i);
            for(int j=0,in_len =row.size();j<len;j+=2){
                String s = row.get(j);
                if(StringUtils.isNotBlank(s)){
                     //if(StringUtils.isBlank(row.get(j+1))){
                     //    System.out.println(initCategoryId+" "+s);
                     //}
                    System.out.println("update CATEGORY_TR set NAME='"+(row.get(j+1)==null?"":row.get(j+1).trim().replace("\n",""))+"' where LOCALE_CODE='th' and CATEGORY_ID="+initCategoryId+";");
                    initCategoryId++;
                    idx++;
                }
            }
        }

       // System.out.println();
        //System.out.println("===== "+idx);
    }
}
