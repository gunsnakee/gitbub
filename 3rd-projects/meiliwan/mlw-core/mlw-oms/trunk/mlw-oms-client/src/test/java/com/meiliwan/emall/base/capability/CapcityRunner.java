package com.meiliwan.emall.base.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.meiliwan.emall.base.capability.AbstractWorker;
import com.meiliwan.emall.base.capability.Print;

public class CapcityRunner{

	private List<Print> list = new ArrayList<Print>();
	private long timeConsume=0;
	private int threadCount=0;
	private int requestCount=0;
	private int SIZE=10;
	private AbstractWorker worker;
	
	public void setWorker(AbstractWorker worker) {
		this.worker = worker;
		
	}

	public void setRequestSize(int size) {
		SIZE = size;
	}

	/**
	 * 
	 * @param threadCount 线程数量
	 */
	public CapcityRunner(int threadCount) {
		this.threadCount = threadCount;
	}

	
	public void startThread(){
		CountDownLatch doneSignal = new CountDownLatch(threadCount);  
		CountDownLatch startSignal = new CountDownLatch(1);//开始执行信号
		
		
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < threadCount; i++) {
			
			worker.setDoneSignal(doneSignal);
			worker.setStartSignal(startSignal);
			worker.setPrintList(list);
			requestCount+=SIZE;
			worker.setRequestSize(SIZE);
			Thread t = new Thread(worker);
			t.start();
		}
	    System.out.println("begin------------");  
	    startSignal.countDown();//开始执行啦  
		
		
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//等待所有的线程执行完毕  
		long end = System.currentTimeMillis();
		timeConsume = end-start;
		print();
	}
	
	public void print(){
		for (Print p : list) {
			System.out.println(p.toString());
		}
		System.out.println("一共线程数:"+threadCount);
		System.out.println("一共请求次数:"+requestCount);
		System.out.println("总共消耗时间:"+timeConsume+"ms");
		System.out.println("请求平均消耗时间:"+(timeConsume/requestCount)+"ms");
	}
	public static void main(String[] args) {
		/*Transport transport = new Transport(10);
		
		transport.setWorker(new AbstractWorker(){

			@Override
			public void runTask() {
				// TODO Auto-generated method stub
				TransportDTO dto = new TransportDTO();
				UserRecvAddr addr = new UserRecvAddr();
				addr.setCountyCode("51719");
				dto.setUserRecvAddr(addr);
				BigDecimal price = BaseTransportAreaPayClient.getPrice(dto);
			}
		});
		transport.setSIZE(100);
		transport.startThread();
		System.exit(1);
		SendMessageTest test = new SendMessageTest();*/
		
	}
}
