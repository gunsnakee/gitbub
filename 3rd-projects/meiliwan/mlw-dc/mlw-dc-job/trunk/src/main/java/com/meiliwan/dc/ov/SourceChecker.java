package com.meiliwan.dc.ov;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import com.meiliwan.dc.PageView;

public class SourceChecker {

	public static class SourceInfo{
		String sourceSite;
		int indexOfList;

		public SourceInfo(){
			sourceSite = "直接访问";
			indexOfList = 0;
		}

		public String getSourceSite() {
			return sourceSite;
		}

		public void setSourceSite(String sourceSite) {
			this.sourceSite = sourceSite;
		}

		public int getIndexOfList() {
			return indexOfList;
		}

		public void setIndexOfList(int indexOfList) {
			this.indexOfList = indexOfList;
		}
		
	}


	public static String extractDomain(String rf){
		int d = rf.indexOf("/", 9);
		if (d < 0){
			d = rf.length();
		}
		int c = rf.indexOf("://");
		String dm = rf.substring(c + 3, d);
		return dm;
	}

	public SourceInfo getSource(List<PageView> pvs){
		SourceInfo si = new SourceInfo();

		Map<String, String> args = pvs.get(0).getArgs();
		if (pvs.get(0).getUri().equals("/u.gif") && args.containsKey("surl")){
			String sdomain = null;
			try {
				sdomain = URLDecoder.decode(args.get("surl"), "utf-8");
				sdomain = extractDomain(sdomain);
			} catch (UnsupportedEncodingException e) {
			}
			if (sdomain != null)
				si.sourceSite = sdomain;
			si.indexOfList = 0;
			return si;
		}
		return si;
	}
}
