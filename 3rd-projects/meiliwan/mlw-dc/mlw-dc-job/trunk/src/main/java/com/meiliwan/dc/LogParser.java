package com.meiliwan.dc;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;

public class LogParser {
	
	static Pattern p = Pattern.compile("tm\\((.*)\\)ip\\((.*)\\)mjid\\((.*)\\)ml\\((.*)\\)mc\\((.*)\\)mcps\\((.*)\\)dm\\((.*)\\)uri\\((.*)\\)rf\\((.*)\\)ag\\((.*)\\)fw\\((.*)\\)args\\((.*)\\)ot\\((.*)\\)");
	
	public static PageView parse(String line){
		try {
			Matcher m = p.matcher(line);
			if (m.find()) {
//				System.out.println(m.groupCount());
				String tm = m.group(1);
				String ip = m.group(2);
				String mjid = m.group(3);
				String ml = m.group(4);
				String mc = m.group(5);
				String mcps = m.group(6);
				String dm = m.group(7);
				String uri = m.group(8);
				String rf = m.group(9);
				String ag = m.group(10);
//				String fw = m.group(10);
				String args = m.group(12);
				if (uri.equals("/u.gif") ){
					PageView pv = new PageView();
					pv.accessTime = Long.parseLong(tm.split("-")[0]);
					pv.ip = ip;
					pv.agent = ag;
					pv.cookieId = mjid;
					pv.visitPage = rf;
					pv.uri = uri;
					pv.mjid = mjid;
					pv.mc = mc;
					pv.ml = ml;
					pv.domain = dm;
					pv.mcps = mcps;
					if (args.equals("-")){
						pv.args = new HashMap<String, String>();
					}else{
						pv.args = Splitter.on("&").withKeyValueSeparator("=").split(args);
					}
					return pv;
				}else{
					return null;
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("err line . " + line);
			return null;
		} 
	}
	
	
	public static long convertTimeToSecond(String timeStr) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"d/MMM/yyyy:hh:mm:ss Z", new DateFormatSymbols(Locale.US));
		try {
			java.util.Date d = sdf.parse(timeStr);
			return d.getTime() / 1000; //seconds
		} catch (ParseException e) {
			return  System.currentTimeMillis() / 1000;
		}
	}
}
