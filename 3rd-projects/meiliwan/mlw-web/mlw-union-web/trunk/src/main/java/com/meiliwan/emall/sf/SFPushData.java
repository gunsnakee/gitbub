package com.meiliwan.emall.sf;

import java.util.ArrayList;
import java.util.List;

public class SFPushData {

	private String service="RoutePushService";
	private String lang="zh-CN";
	private List<WaybillRoute> list = new ArrayList<WaybillRoute>();
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public void add(WaybillRoute way){
		list.add(way);
	}
	
	public List<WaybillRoute> getList() {
		return list;
	}

	public void setList(List<WaybillRoute> list) {
		this.list = list;
	}

	public String toXml(){
		StringBuilder sb = new StringBuilder();
		sb.append("<Request service=\"RoutePushService\" lang=\"zh-CN\"><Body>");
		for (WaybillRoute way : list) {
			sb.append(way.toXml());
		}
		sb.append("</Body></Request>");
		return sb.toString();
	}

	
}
