package com.meiliwan.emall.dc.inbox;

import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class DcInboxData {
	private final static Logger log= Logger.getLogger(DcInboxData.class);
	
	public static LinkedBlockingQueue<String> queue= new LinkedBlockingQueue<String> ();
	
	public static void addData(String pv){
		try{
			queue.put(pv);
		}
		catch(Exception exp){
			log.error("",exp); 
			//todo 这里要短信通知
		}
	}
}
