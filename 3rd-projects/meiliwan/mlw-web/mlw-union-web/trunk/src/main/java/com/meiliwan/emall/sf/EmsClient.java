package com.meiliwan.emall.sf;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.gxems.ws.VipOrderIdBindingEmsNoWs;

public class EmsClient {

	public static void main(String[] args) throws UnknownHostException {
		 JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
         svr.setServiceClass(VipOrderIdBindingEmsNoWs.class);
         //测试
         //svr.setAddress("http://219.134.187.132:9090/bsp-ois/ws/expressService");
         //线上
         svr.setAddress("http://211.138.242.142/qjlup/VipOrderIdBindingEmsNoWsService/VipOrderIdBindingEmsNoWs");
         VipOrderIdBindingEmsNoWs service = (VipOrderIdBindingEmsNoWs) svr.create();
         
         InetAddress addr = InetAddress.getLocalHost();
 		String ip=addr.getHostAddress().toString();//获得本机IP
 		String reverseIp = new StringBuilder(ip).reverse().toString();
 		System.out.println(reverseIp);
         List<String> order = new ArrayList<String>();
         order.add("000000000054");
         List<String> expressNo = new ArrayList<String>();
         expressNo.add("903114021930");
         
         String cip = getMd5String("121.31.30.146"); 
         
         
         String result = service.vipOrderIdBindingEmsNo(cip,"45010103633000", order, expressNo);
         System.out.println(result);
	}
	
	public static String getMd5String(String input) {
		byte[] md5byte = null;
		String md5String = "";
		try {
			// use MD5 of java core
			MessageDigest alg;

			alg = MessageDigest.getInstance("MD5");

			md5byte = alg.digest(input.getBytes());
			for (byte b : md5byte) {
				md5String += b + "";
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md5String;
	}

	
}
