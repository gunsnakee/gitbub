package com.meiliwan.emall.imeiliwan.util;


import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.meiliwan.emall.imeiliwan.GlobalVars;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: yyluo
 * Date: 14-5-5
 * Time: 下午4:32
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientUtils {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(HttpClientUtils.class);

    public static String getStringFromHttp(String serverUrl, String methodPath, String httpMethod, Map<String, Object> params) {
        HttpResponse response = getHttpRespon(serverUrl, methodPath, httpMethod, params);
        int httpCode = response.getStatusLine().getStatusCode();
        String result = "";
        if(httpCode == 200){
            HttpEntity entity = response.getEntity();
            try {
                result = EntityUtils.toString(entity,"utf-8");
            } catch (Exception e) {
                logger.error(e, httpMethod + "httpClient请求异常", null);
            }
        }
        return result;
    }

    public static HttpResponse getHttpRespon(String serverUrl, String methodPath, String httpMethod, Map<String, Object> params) {
        HttpClient client = new DefaultHttpClient();
        List<NameValuePair> paramsPairs = getParams(params);
        HttpResponse result = null;
        try {
            if (GlobalVars.HTTP_GET.equals(httpMethod)) {
                HttpGet request = new HttpGet(serverUrl +methodPath+ "?" + URLEncodedUtils.format(paramsPairs, "UTF-8"));
                result = client.execute(request);
            } else {
                HttpPost request = new HttpPost(serverUrl + methodPath);
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramsPairs, "utf-8");
                request.setEntity(formEntity);
                result = client.execute(request);
            }
        } catch (Exception e) {
            logger.error(e, httpMethod + "httpClient请求异常", null);
        }
        return result;
    }

    private static List<NameValuePair> getParams(Map<String, Object> params) {
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            Gson gson = new Gson();
            NameValuePair param = new BasicNameValuePair(key, gson.toJson(params.get(key)));
            result.add(param);
        }
        return result;
    }

    public static StringBuffer doPost(String data, String strurl)
            throws IOException {
        URL url = new URL(strurl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        con.connect();
        //DataOutputStream out = new DataOutputStream(con.getOutputStream());
        OutputStream out = con.getOutputStream();
        //处理中文乱码问题
        OutputStreamWriter writer = new OutputStreamWriter(out,"utf-8");
        writer.write(data.toString());
        writer.flush();
        writer.close();
        out.flush();
        out.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        reader.close();
        // 断开连接
        con.disconnect();
        return sb;
    }
}
