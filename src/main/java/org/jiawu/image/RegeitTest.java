package org.jiawu.image;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class RegeitTest {
    static public void main(String[] args) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpUriRequest getMethod = new HttpGet("验证码URL");
        for(int i=1;i<=10;i++){
            try {
                String yzm = "";
                HttpResponse res = httpclient.execute(getMethod);
                HttpEntity entity = res.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    BufferedImage bi = ImageIO.read(instream);
                    instream.close();
                    /************************************/
                    yzm = ImageRead.read(bi,i);
/********************************************/
                }
                System.out.println(yzm+":===="+i+"   ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}