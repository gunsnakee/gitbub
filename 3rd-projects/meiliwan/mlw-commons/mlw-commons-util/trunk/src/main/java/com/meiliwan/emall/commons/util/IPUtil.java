package com.meiliwan.emall.commons.util;

import org.apache.commons.lang.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	/**
	 * 
	 * <Description>获取浏览器真实IP地址</Description>
	 *
	 * @param request 
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for"); 
		if(StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP"); 
		}
		
		if(StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		}
		
		if(StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr(); 
		} 
		
		return ip; 
	}
	
	/**
	 * @return 服务器ip
	 */
	public static String  getLocalIp() {  
		try {
            for (Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces(); ni
                .hasMoreElements();) {
                NetworkInterface eth = ni.nextElement();
                for (Enumeration<InetAddress> add = eth.getInetAddresses(); add.hasMoreElements();) {
                    InetAddress i = add.nextElement();
                    if (i instanceof Inet4Address) {
                        if (i.isSiteLocalAddress()) {
                            return i.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            //e.printStackTrace();
        }
        return "";  
	}
	
	public static String parseIceConIP(String iceConInfo){
		return iceConInfo.replaceAll(":", "_").replace("local address =", "localIP:").replace("remote address =", "clientIP:").replace("\n", ",");
	}
	
	public static String[] parseIceConfToIPArr(String iceConInfo){
		return iceConInfo.replaceAll(":", "_").replace("local address =", "").replace("remote address =", "").replace("\n", ",").split(",");
	}
	
	
	public static void main(String[] args) {
		System.out.println(getLocalIp());
	}
	
}
