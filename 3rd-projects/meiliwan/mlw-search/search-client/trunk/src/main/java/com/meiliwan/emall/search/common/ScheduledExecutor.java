package com.meiliwan.emall.search.common;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;



public class ScheduledExecutor {
	
	static class SearchTF implements ThreadFactory{

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "SearchClientScheduledExecutorThread");
			t.setDaemon(true);
			return t;
		}
		
	}
	
	final public static ScheduledExecutorService ScheduledService = Executors.newSingleThreadScheduledExecutor(new SearchTF());
	
	
	public static void submit(Runnable cmd, long delayMilliSenconds){
		ScheduledService.scheduleAtFixedRate(cmd, 0, delayMilliSenconds, TimeUnit.MILLISECONDS);
	}
	
}
