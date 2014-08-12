package org.jiawu.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jiawu.excel.ReadExcelTool;
import org.jiawu.tools.TranslationsUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by jiawuwu on 14-4-10.
 */
public class MlwB2bCategory {

    public static void main(String[] args) throws IOException {



        List<Map<Integer,String>> maps = ReadExcelTool.readOneSheet("/data/temp/美丽湾B2B类目信息结构表20140402.xls", 0);

        int maps_size = maps.size();

        String level_first = "";
        String level_second = "";
        String level_three = "";

        Set<String> srcTexts = new HashSet<String>();

        for(int i=2;i<maps_size;i++){
            Map<Integer,String> rowData = maps.get(i);
            if(rowData.size()<4){
                continue;
            }
            level_first = rowData.get(1);
            level_second = rowData.get(2);
            level_three = rowData.get(3);

            if(StringUtils.isNotBlank(level_first)){
                srcTexts.add(level_first.trim());
            }

            if(StringUtils.isNotBlank(level_second)){
                srcTexts.add(level_second.trim());
            }
            if(StringUtils.isNotBlank(level_three)){
                String[] level_three_items = level_three.split("/");
                for(String level_tree_item : level_three_items){
                    srcTexts.add(level_tree_item.trim());
                }
            }

        }

        Map<String, String> enMap = TranslationsUtil.translate(srcTexts, "en");
        Map<String, String> viMap = TranslationsUtil.translate(srcTexts, "vi");
        Map<String, String> thMap = TranslationsUtil.translate(srcTexts, "th");


        // 创建httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();


        // 登陆melibay后台管理系统
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("j_username", "admin"));
        formparams.add(new BasicNameValuePair("j_password", "123456"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost("http://admin.melibay.com/login_admin_post");
        httppost.setEntity(entity);
        httpclient.execute(httppost);

        HttpGet httpGet = null;
        String pidOne = "1001";
        String pidTwo = "";
        String pidThree = "";
        String en="";
        String vi="";
        String th="";

        for(int i=2;i<maps_size;i++){
            Map<Integer,String> rowData = maps.get(i);
            if(rowData.size()<4){
                continue;
            }
            level_first = rowData.get(1);
            level_second = rowData.get(2);
            level_three = rowData.get(3);

            if(StringUtils.isNotBlank(level_first)){
                level_first = level_first.trim();
                en = URLEncoder.encode(enMap.get(level_first)==null?"":enMap.get(level_first),"UTF-8");
                vi = URLEncoder.encode(viMap.get(level_first)==null?"":viMap.get(level_first),"UTF-8");
                th = URLEncoder.encode(thMap.get(level_first)==null?"":thMap.get(level_first),"UTF-8");
                level_first = URLEncoder.encode(level_first,"UTF-8");
                httpGet = new HttpGet("http://admin.melibay.com/category/submitForImport?pId="+pidOne+"&nameZh="+level_first+"&nameEn="+en+"&nameVn="+vi+"&nameTh="+th);
                CloseableHttpResponse response1 = httpclient.execute(httpGet);

                try {
                    HttpEntity entity1 = response1.getEntity();
                    pidTwo=EntityUtils.toString(entity1);
                    EntityUtils.consume(entity1);
                } finally {
                    response1.close();
                }
            }

            System.out.println("pidTwo="+pidTwo);

            if("fail".equals(pidTwo)){
                break;
            }

            if(StringUtils.isNotBlank(level_second)){
                level_second = level_second.trim();
                en = URLEncoder.encode(enMap.get(level_second)==null?"":enMap.get(level_second),"UTF-8");
                vi = URLEncoder.encode(viMap.get(level_second)==null?"":viMap.get(level_second),"UTF-8");
                th = URLEncoder.encode(thMap.get(level_second)==null?"":thMap.get(level_second),"UTF-8");
                level_second = URLEncoder.encode(level_second,"UTF-8");
                httpGet = new HttpGet("http://admin.melibay.com/category/submitForImport?pId="+pidTwo+"&nameZh="+level_second+"&nameEn="+en+"&nameVn="+vi+"&nameTh="+th);
                CloseableHttpResponse response1 = httpclient.execute(httpGet);

                try {
                    HttpEntity entity1 = response1.getEntity();
                    pidThree=EntityUtils.toString(entity1);
                    EntityUtils.consume(entity1);
                } finally {
                    response1.close();
                }

            }

            System.out.println("pidThree="+pidThree);

            if("fail".equals(pidThree)){
                break;
            }

            if(StringUtils.isNotBlank(level_three)){
                String[] level_three_items = level_three.split("/");
                for(String level_tree_item : level_three_items){
                    level_tree_item = level_tree_item.trim();
                    en = URLEncoder.encode(enMap.get(level_tree_item)==null?"":enMap.get(level_tree_item),"UTF-8");
                    vi = URLEncoder.encode(viMap.get(level_tree_item)==null?"":viMap.get(level_tree_item),"UTF-8");
                    th = URLEncoder.encode(thMap.get(level_tree_item)==null?"":thMap.get(level_tree_item),"UTF-8");
                    level_tree_item = URLEncoder.encode(level_tree_item,"UTF-8");
                    httpGet = new HttpGet("http://admin.melibay.com/category/submitForImport?pId="+pidThree+"&nameZh="+level_tree_item+"&nameEn="+en+"&nameVn="+vi+"&nameTh="+th);
                    CloseableHttpResponse response1 = httpclient.execute(httpGet);
                    try {
                        HttpEntity entity1 = response1.getEntity();
                        EntityUtils.consume(entity1);
                    } finally {
                        response1.close();
                    }
                }
            }

        }







    }
}
