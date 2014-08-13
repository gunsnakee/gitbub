package com.meiliwan.search.press;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import com.meiliwan.search.util.io.FileUtil;


public class ABTester implements Runnable{
	
    ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager();
    DefaultHttpClient httpClient = new DefaultHttpClient(mgr);
	
	int threadNum = 10; //number of thread 
	Random rand = new Random();
	int probes = 1000; //# each thread to probe
	long interval = 0; //interval between two probes
	int connTimeOut;
	int soTimeOut;
	boolean isKeepAlive = false;//
	static volatile AtomicLong  totalProbes = new AtomicLong(0);
	
	
	List<String> candidates;
	String urlPrefixString;
	List<Thread> attackers ;
	
	Thread printer = null;
	public static long printIntervalInMs = 10 * 1000;  
	
	public ABTester(String urlPrefix, String candidatePath,
			int connTimeOut, int soTimeOut) throws IOException{
		BufferedReader br = FileUtil.UTF8Reader(candidatePath);
		urlPrefixString = urlPrefix;
		candidates = new ArrayList<String>();
		for(String line = br.readLine(); line != null; line = br.readLine()){
			candidates.add(URLEncoder.encode(line.split("\\s+")[0], "UTF-8"));
		}
		br.close();
		System.out.println("loading candidate finished!");
		ThreadSafeClientConnManager mgr = (ThreadSafeClientConnManager)httpClient.getConnectionManager();
		mgr.setMaxTotal(256);
		mgr.setDefaultMaxPerRoute(50);
	    HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 		  connTimeOut);
	    HttpConnectionParams.setSoTimeout(httpClient.getParams(),  		soTimeOut);
		
		printer = new Thread(this);
		printer.setDaemon(true);
		printer.start();
	}
	
	public ABTester(String urlPrefix, String candidatePath) throws IOException{
		this(urlPrefix, candidatePath, 3000, 3000);
	}
	
	
	public void attackingKeepAlive(int threads, int eachProbes, long interval) throws InterruptedException{
		this.interval = interval;
		this.isKeepAlive = true;
		this.probes = eachProbes;
		this.threadNum = threads;
		
		attacking();
	}
	
	
	public void attackingNoKeepAlive(int threads, int eachProbes, long interval) throws InterruptedException{
		this.interval = interval;
		this.isKeepAlive = false;
		this.probes = eachProbes;
		this.threadNum = threads;
		attacking();
	}
	
	
	private void attacking() throws InterruptedException{
		attackers = new ArrayList<Thread>(threadNum);
		for(int i= 0 ; i < threadNum; i++ ){
			attackers.add(
					new Attacker(httpClient,  urlPrefixString, isKeepAlive));
		}
		long t = System.currentTimeMillis();
		for(Thread thr : attackers){
			thr.start();
		}
		for(Thread thr : attackers){
			thr.join();
		}
		long t2 = System.currentTimeMillis();
		long totalTime = t2 - t;
		int totalSuccess = 0;
		for(int i = 0 ; i < threadNum; i++){
			Attacker attacker = (Attacker)(attackers.get(i));
			totalSuccess += attacker.success;
		}
		double totalProbes = threadNum * probes * 1.0;
		
		System.out.println(String.format("\n=================\n"+
				"allProbes:\t%.0f\nsuccess:\t%d\ntime(ms):\t%d\nTPS:\t%.1f\nOkRate:\t%.4f", 
				totalProbes, totalSuccess, totalTime, (totalProbes * 1000)/ totalTime , (totalSuccess/totalProbes)));
	}
	
	
	public class Attacker extends Thread{
		HttpClient httpclient ;
		String urlPrefix;
		public int success = 0;
		public double okRate = 0.0;
		public Attacker(HttpClient client, String urlPrefix, boolean keepAlive) {
			httpclient = client;

			this.urlPrefix = urlPrefix;

		}
		
		public void run() {
			int size = candidates.size();
			for(int i = 0 ; i < probes; i++){
				if (interval > 0){
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {}
				}
				int idx = rand.nextInt(size);
				String url = urlPrefix+ candidates.get(idx);
				HttpGet httpget = new HttpGet( url);
				if (isKeepAlive){
					httpget.addHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
				}else{
					httpget.addHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
				}
				try{
					String a = httpclient.execute(httpget, new BasicResponseHandler());
					totalProbes.incrementAndGet();
					if (a.startsWith("{\"status\":\"ok"))
						success += 1;
				}catch (Exception e) {
					e.printStackTrace();
				}

			}
			okRate = (success + 0.0) / probes;
		}
	}


	public void run() {
		
		while(true){
			System.out.println(String.format("totalProbe:%d, current invertal: %d", ABTester.totalProbes.get(), ABTester.printIntervalInMs));
			try {
				Thread.sleep(printIntervalInMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
