package org.jiawu.gitbub.core.main;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.jiawu.gitbub.core.excel.ReadExcelTool;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by jiawuwu on 14-7-11.
 */
public class PropertiesMeger{

    public static  void  main(String[] args) throws IOException, ConfigurationException {
        PropertiesConfiguration configuration = new PropertiesConfiguration("/data/mygit/oschina/src/main/resources/messages_lo.properties");

        List<List<Map<Integer,String>>> exceldata = ReadExcelTool.readAll("/data/mygit/oschina/src/main/resources/B2B翻译内容项（老挝）wujiawu-edit.xls");
        List<Map<Integer, String>> lo = exceldata.get(0);
        for(int i=1;i<lo.size();i++){
            Map<Integer, String> row = lo.get(i);
            if(StringUtils.isNotBlank(row.get(3))){
                if(configuration.containsKey(row.get(1))){
                    //System.out.println("替换 "+row.get(1)+" 的值为 "+row.get(3));
                    configuration.setProperty(row.get(1),row.get(3));
                }
            }
        }

        System.out.println();
        System.out.println("==========");
        System.out.println();

        configuration.save(System.out);
        configuration.save(new File("/data/mygit/oschina/src/main/resources/messages_lo_new.properties"));

        // oschina 有 ascii 转 native 的在线工具


    }
}
