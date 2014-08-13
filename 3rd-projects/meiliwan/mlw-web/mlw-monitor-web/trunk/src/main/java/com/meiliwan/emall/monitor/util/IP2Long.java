package com.meiliwan.emall.monitor.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP2Long {
	


	public static int byte2int(byte b) {
		int l = b & 0x07f;
		if (b < 0) {
			l |= 0x80;
		}
		return l;
	}



	public static long int2long(int i) {
		long l = i & 0x7fffffffL;
		if (i < 0) {
			l |= 0x080000000L;
		}
		return l;
	}

	public static String long2ip(long ip) {
		int[] b = new int[4];
		b[0] = (int) ((ip >> 24) & 0xff);
		b[1] = (int) ((ip >> 16) & 0xff);
		b[2] = (int) ((ip >> 8) & 0xff);
		b[3] = (int) (ip & 0xff);
		String x;
		Integer p;
		p = new Integer(0);
		x = p.toString(b[0]) + "." + p.toString(b[1]) + "." + p.toString(b[2])
				+ "." + p.toString(b[3]);
		return x;
	}

	public static long ip2long(String dottedIP) {
	    String[] addrArray = dottedIP.split("\\.");        
	    long num = 0;        
	    for (int i=0;i<addrArray.length;i++) {            
	        int power = 3-i;            
	        num += ((Integer.parseInt(addrArray[i]) % 256) * Math.pow(256,power));        
	    }        
	    return num;    
	}
	
	// 测试函数
	public static void main(String[] args) throws Exception {
		
		Long ip2 = ip2long("203.208.60.184");
		System.out.println(ip2);
	}
}