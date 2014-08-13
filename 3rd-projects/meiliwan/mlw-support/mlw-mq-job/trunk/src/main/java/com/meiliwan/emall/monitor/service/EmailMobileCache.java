package com.meiliwan.emall.monitor.service;

import java.util.LinkedHashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.log.msgrcv.WatchLogTaskWorker;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.client.AllocationClient;

public class EmailMobileCache {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(EmailMobileCache.class);
	private static EmailMobileCache emailMobileCache = new EmailMobileCache();
	
	private BlockingDeque<RequestLog> cacheDeque = new LinkedBlockingDeque<RequestLog>();
	
	
	private int DAOResumeTime; 
	private int ICESResumeTime; 
	private int ICECResumeTime; 
	private int INTRResumeTime; 
	
	public static EmailMobileCache getInstance(){
		if(emailMobileCache==null){
			return new EmailMobileCache();
		}
		return emailMobileCache;
	}
	
	private EmailMobileCache() {
		LinkedHashMap<String, Integer> resumeTimeConfigList = AllocationClient.getResumeTimeConfigList();
		 DAOResumeTime = resumeTimeConfigList.get(AllocationClient.DAO);
		 ICESResumeTime = resumeTimeConfigList.get(AllocationClient.ICES);
		 ICECResumeTime = resumeTimeConfigList.get(AllocationClient.ICEC);
		 INTRResumeTime = resumeTimeConfigList.get(AllocationClient.INTR);
		 EmailMobileSenderTread emailMobileSenderTread = new EmailMobileSenderTread(this);
	}

	public synchronized void add(RequestLog log){
		if(log.isDAOType()&&log.getTimeConsume().longValue()<=WatchLogTaskWorker.DAO_NOT_INSERT){
			return ;
		}
		if(!log.isDAOType()&&log.getTimeConsume().longValue()<=WatchLogTaskWorker.OTHER_NOT_INSERT){
			return ;
		}
		
		boolean filter = false;
		
		if(daoFilter(log)||iceSFilter(log)||iceCFilter(log)||intrFilter(log)){
			filter=true;
		}
		if(filter){
			cacheDeque.add(log);
		}
		
	}
	
	
	private boolean daoFilter(RequestLog requestLog){
		
		if(requestLog.isDAOType()){
			if(requestLog.getTimeConsume()>=DAOResumeTime){
				return true;
			}
		}
		return false;
	}
	
	private boolean iceSFilter(RequestLog requestLog){
		if(requestLog.isICESType()){
			if(requestLog.getTimeConsume()>=ICESResumeTime){
				return true;
			}
		}
		return false;
	}
	
	private boolean iceCFilter(RequestLog requestLog){
		if(requestLog.isICECType()){
			if(requestLog.getTimeConsume()>=ICECResumeTime){
				return true;
			}
		}
		return false;
	}
	
	private boolean intrFilter(RequestLog requestLog){
		if(requestLog.isINTRType()){
			if(requestLog.getTimeConsume()>=INTRResumeTime){
				return true;
			}
		}
		return false;
	}
	
	public RequestLog take() throws InterruptedException{
		return cacheDeque.take();
	}
}
