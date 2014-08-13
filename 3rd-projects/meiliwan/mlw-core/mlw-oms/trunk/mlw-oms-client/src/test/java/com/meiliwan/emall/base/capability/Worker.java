package com.meiliwan.emall.base.capability;

public interface Worker extends Runnable{
	
	/**
	 * 设置请求次数
	 */
	void setRequestSize(int size);
	
	/**
	 * 
	 */
	void runTask();
	//doneSignal, startSignal, list
	
}
