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
 * Created by jiawuwu on 14-4-9.
 */
public class MlwB2bRegister {

    public static void main(String[] args) throws IOException {

        // 创建httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://www.melibay.com/member/register");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            System.out.println(response.getStatusLine());
            System.out.println(entity.getContent());
        } finally {
            response.close();
        }



        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("email", "mlw1001@meiliwan.com"));
        formparams.add(new BasicNameValuePair("nickname", "mlw1001"));
        formparams.add(new BasicNameValuePair("password", "abcd1234"));
        formparams.add(new BasicNameValuePair("rePassword", "abcd1234"));
        formparams.add(new BasicNameValuePair("identity", "ROOT_USER"));
        formparams.add(new BasicNameValuePair("vcode", "5daac8ee0ddf4c66a68625592ee317eb"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost("http://www.melibay.com/member/processRegister");
        httppost.setEntity(entity);
        CloseableHttpResponse response1 =  httpclient.execute(httppost);
        try {
            HttpEntity entity1 = response1.getEntity();
            EntityUtils.consume(entity1);
            System.out.println(response1.getStatusLine());
            System.out.println(entity1.getContent());
        } finally {
            response1.close();
        }

    }
}
