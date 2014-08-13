package com.meiliwan.emall.sf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;

import com.meiliwan.emall.commons.util.DateUtil;

public class HttpPost {

	
	 /**
     * 发送Post请求
     */
    public void sendPostRequest() {

    	
//    	<?xml version='1.0' encoding='UTF-8'?><Request service="RoutePushService" lang="zh-CN"><Body>
//    	<WaybillRoute id="481641" mailno="587000136175" orderid="000004258221" acceptTime="2014-02-21 18:19:32"
//    	acceptAddress="深圳市" remark="便利店交接" opCode=""/>
//    	<WaybillRoute id="481642" mailno="587000136175" orderid="000004258221" acceptTime="2014-02-21 18:14:32"
//    	acceptAddress="深圳市" remark="正在派件.." opCode=""/>
//    	<WaybillRoute id="481643" mailno="587000136175" orderid="000004258221" acceptTime="2014-02-21 18:09:32" 
//    	acceptAddress="深圳市" remark="代理收件" opCode=""/>
//    	<WaybillRoute id="481644" mailno="587000136175" orderid="000004258221" acceptTime="2014-02-21 16:39:32" 
//    	acceptAddress="深圳市" remark="已收件" opCode=""/></Body></Request>
//    	
    	
    		SFPushData sfdata = new SFPushData();
    		for (int i = 0; i < 4; i++) {
    			WaybillRoute sf = new WaybillRoute();
    			sf.setOrderid("000004258188");
    			sf.setAcceptAddress("深圳市");
    			sf.setId("12481544"+i);
    			sf.setAcceptTime(new Date());
    			sf.setMailno("587000136175");
    			sf.setOpCode("");
    			sf.setRemark("已收件");
    			sfdata.add(sf);
		}
    		
    		
        //Build parameter string
        String data = sfdata.toXml();
        System.out.println(data);
        try {

            // Send the request
            URL url = new URL("http://localhost:8080/transport/sf");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            String wwwCode = URLEncoder.encode(data,"utf-8");
            //write parameters
            writer.write(wwwCode);
            writer.flush();

            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();

            //Output the response
            System.out.println(answer.toString());

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
    		HttpPost post = new HttpPost();
    		post.sendPostRequest();
	}
}
