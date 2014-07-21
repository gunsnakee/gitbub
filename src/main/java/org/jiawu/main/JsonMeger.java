package org.jiawu.main;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jiawu.excel.ReadExcelTool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiawuwu on 14-7-11.
 */
public class JsonMeger {
    public static void main(String[] args) throws IOException {

        List<List<Map<Integer,String>>> exceldata = ReadExcelTool.readAll("/data/mygit/oschina/src/main/resources/B2B翻译内容项（老挝）wujiawu-edit.xls");
        List<Map<Integer, String>> lo = exceldata.get(1);
        Map<String,String> addLanuageDic = new HashMap<String, String>();
        for(int i=1;i<lo.size();i++){
            Map<Integer, String> row = lo.get(i);
            if(StringUtils.isNotBlank(row.get(3))){
                addLanuageDic.put(row.get(1),row.get(3));
            }
        }

        String s = FileUtils.readFileToString(new File("/data/mygit/oschina/src/main/resources/msg.json"));
        Map map = new Gson().fromJson(s, Map.class);

        String addLanuage = "lo";

        StringBuffer stringBuffer = new StringBuffer("{");
        String enVal = "";

        int  sizeOut = map.size();

        int idxOut=0;




        for(Object o :map.entrySet()){
            idxOut++;
            Map.Entry<String,StringMap> one = (Map.Entry<String,StringMap>)o;
            String key = one.getKey();
            //System.out.println("==="+key);
            stringBuffer.append("'"+key+"':{");
            StringMap trans = one.getValue();
            for(Object tran: trans.entrySet()){
                StringMap.Entry<String,String> tranObj = (StringMap.Entry<String,String>) tran;
                String lang = tranObj.getKey();
                String value = tranObj.getValue();
                //System.out.println("======"+lang+" "+value);
                stringBuffer.append("'"+lang+"':'"+value+"',");
                if("en".equals(lang)){
                    enVal = value;
                }
            }
            if(addLanuageDic.containsKey(key)){
                stringBuffer.append("'"+addLanuage+"':'"+addLanuageDic.get(key)+"'");
            }else{
                stringBuffer.append("'"+addLanuage+"':'"+enVal+"'");
            }
            stringBuffer.append("}");
            if(idxOut<sizeOut){
                stringBuffer.append(",");
            }

        }

        stringBuffer.append("}");

        FileUtils.writeStringToFile(new File("/data/mygit/oschina/src/main/resources/msg-"+addLanuage+".json"),stringBuffer.toString(),"UTF-8");


        System.out.println(stringBuffer.toString());




    }
}
