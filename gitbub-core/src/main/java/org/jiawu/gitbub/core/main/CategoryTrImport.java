package org.jiawu.gitbub.core.main;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;
import org.core.excel.ReadExcelTool;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiawuwu on 14-7-13.
 */
public class CategoryTrImport {

    public static void main(String[] args) throws ConfigurationException {


        List<Map<Integer,String>> zhMaps = ReadExcelTool.readOneSheet("/data/mygit/oschina/src/main/resources/美丽湾B2B类目信息结构表0630(老挝)_wujiawu.xls", 0);
        List<Map<Integer,String>> loMaps = ReadExcelTool.readOneSheet("/data/mygit/oschina/src/main/resources/美丽湾B2B类目信息结构表0630(老挝)_wujiawu.xls", 1);

        int length = zhMaps.size();


        Map<String,String> loDics = new HashMap<String, String>();

        for(int i=1;i<length;i++){
            if(StringUtils.isNotBlank(zhMaps.get(i).get(0))){
                loDics.put(zhMaps.get(i).get(0),loMaps.get(i).get(0));
            }
            if(StringUtils.isNotBlank(zhMaps.get(i).get(1))){
                loDics.put(zhMaps.get(i).get(1),loMaps.get(i).get(1));
            }
            if(StringUtils.isNotBlank(zhMaps.get(i).get(2))){
                String[] ss = zhMaps.get(i).get(2).split("/");
                String[] dd = loMaps.get(i).get(2).split("/");

                int lt = ss.length;

                for(int j=0;j<lt;j++){
                    loDics.put(ss[j],dd[j]);
                }

            }
        }






        XMLConfiguration config = new XMLConfiguration("/data/mygit/oschina/src/main/resources/translation_tr.xml");

        int lengthX = config.getRoot().getChildren().size();

        int initTransId = 8751;
        String lang = "lo";

        for(int i=0;i<lengthX;i++){
            config.setProperty("RECORD("+i+").TRANSLATION_ID",initTransId++);
            config.setProperty("RECORD("+i+").LOCALE_CODE",lang);
            String zh = config.getString("RECORD("+i+").NAME");
            if(loDics.containsKey(zh)){
                config.setProperty("RECORD("+i+").NAME",loDics.get(zh));
            }else{
                config.setProperty("RECORD("+i+").NAME",zh);
            }
            config.setProperty("RECORD("+i+").DESCRIPTION","");
        }

        config.save(new File("/data/mygit/oschina/src/main/resources/translation_tr_"+lang+".xml"));
    }

}
