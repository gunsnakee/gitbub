package com.meiliwan.dc;

import java.util.Comparator;
import java.util.Map;

public class PageView {
	
	public static class Sorter implements Comparator<PageView>{
		public int compare(PageView o1, PageView o2) {
			int f1 = o1.cookieId.compareTo(o2.cookieId);
			if (f1 == 0){
				long diff = o1.accessTime - o2.accessTime;
				return (int)diff;
			}else{
				return f1;
			}
		}
	}
	

	String mc;
	String ml;
	long accessTime;
	String agent;
	String cookieId;
	String mjid;
	String visitPage;
	String domain;
	String uri;
	String mcps;
	
	Map<String, String> args;
	String ip;

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getMcps() {
		return mcps;
	}
	public void setMcps(String mcps) {
		this.mcps = mcps;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getMl() {
		return ml;
	}
	public void setMl(String ml) {
		this.ml = ml;
	}
	public long getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getCookieId() {
		return cookieId;
	}
	public void setCookieId(String cookieId) {
		this.cookieId = cookieId;
	}
	public String getMjid() {
		return mjid;
	}
	public void setMjid(String mjid) {
		this.mjid = mjid;
	}

	public String getVisitPage() {
		return visitPage;
	}
	public void setVisitPage(String visitPage) {
		this.visitPage = visitPage;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Map<String, String> getArgs() {
		return args;
	}
	public void setArgs(Map<String, String> args) {
		this.args = args;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
