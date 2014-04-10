package org.jiawu.http;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiawuwu on 14-4-10.
 */
public class MlwB2bCategory {

    public static void main(String[] args) throws IOException {

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






        HttpGet httpGet = new HttpGet("http://admin.melibay.com/category/submit?pId=1001&nameZh=testZH&nameEn=testEn&nameVn=testVn&nameTh=testTh");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            System.out.println(EntityUtils.toString(entity1));
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
    }
}
